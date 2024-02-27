package paperUser.Impl;

import com.google.rpc.context.AttributeContext;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import paperUser.entity.*;
import paperUser.entity.enums.ConferenceState;
import paperUser.entity.enums.Confidence;
import paperUser.exception.WebException;
import paperUser.repository.*;
import paperUser.service.StartReviewService;

import java.util.*;

@Service
public class StartReviewServiceImpl implements StartReviewService {
    private RefereeRepository refereeRepository;
    private ConferenceRepository conferenceRepository;
    private PaperRepository paperRepository;
    private AuthorRepository authorRepository;
    private CommentRepository commentRepository;
    private TopicRepository topicRepository;
    private RelateRepository relateRepository;
    private ChargeRepository chargeRepository;

    @Autowired
    public StartReviewServiceImpl(RefereeRepository refereeRepository, ConferenceRepository conferenceRepository,
                                  PaperRepository paperRepository, AuthorRepository authorRepository,
                                  CommentRepository commentRepository, TopicRepository topicRepository,
                                  RelateRepository relateRepository, ChargeRepository chargeRepository) {
        this.refereeRepository = refereeRepository;
        this.conferenceRepository = conferenceRepository;
        this.paperRepository = paperRepository;
        this.authorRepository = authorRepository;
        this.commentRepository = commentRepository;
        this.topicRepository = topicRepository;
        this.chargeRepository = chargeRepository;
        this.relateRepository = relateRepository;
    }

    //判断当前paper是否与当前pcMember相关
    private boolean findRelatedPaper(Paper paper, User pcMember){
        //判断paper是否为该pcMember投稿
        System.out.println("判断相关性： paper提交者id- "+paper.getUser().getUid()+"  审稿人id- "+ pcMember.getUid());
        if (Objects.equals(paper.getUser().getUid(), pcMember.getUid()))
            return true;
        //判断paper的作者是否为该pcMember
        List<Author> authorList = authorRepository.findByPaper(paper);
        System.out.println("判断相关性： 作者列表- "+authorList);
        for(Author item : authorList){
            System.out.println("判断相关性： paper作者名- "+item.getAuthorName()+"  审稿人id- "+ pcMember.getUsername());
            if(Objects.equals(item.getAuthorName(), pcMember.getUsername()) &&
                    Objects.equals(item.getEmail(), pcMember.getEmail()))
                return true;
        }
        return false;
    }

    //更新选择的3个审稿人(当前最已分配稿件最少的数目list和相应下标的list)
    //判断当前pcMember的审稿数是否小于目前最大的审稿数，若小于则替换掉当前最大的审稿数
    //参数：
    // minThree：已分配稿件最少的审稿人的稿件数目list
    // minThreeIndex：已分配稿件最少的审稿人的下标list
    // paperNum：当前审稿人的已分配稿件数
    // index：当前审稿人的下标
    private void changeMinThree(List<Integer> minThree, List<Integer> minThreeIndex, int paperNum, int index){
        int maxPaperNum = 0;
        for(Integer i : minThree){
            if(maxPaperNum < i)
                maxPaperNum = i;
        }
        if(paperNum < maxPaperNum) {
            //得到list中当前最大的审稿数的下标
            int beReplacedIndex = minThree.indexOf(maxPaperNum);
            minThree.set(beReplacedIndex, paperNum);
            minThreeIndex.set(beReplacedIndex, index);
        }
    }

    //得到paper的可分配的且已分配稿件数最少的3个审稿人
    //参数：
    // pcMemberPaperNum：每个审稿人当前已分配的稿件数
    // assignedState: 当前论文的可分配状态
    //返回：
    // 找到的已分配稿件数最少的3个审稿人的下标（可以小于等于3）
    private List<Integer> minThreeReviewer(List<Integer> pcMemberPaperNum, List<Integer> assignedState){
        List<Integer> minThree = new ArrayList<>();
        List<Integer> minThreeIndex = new ArrayList<>();
        int cols = pcMemberPaperNum.size();
        //用来记录当前可分配的审稿人数量上限，count<=3
        int count = 0;
        //遍历当前论文可分配状态
        for(int j = 0; j < cols; j++) {
            if(assignedState.get(j) == 0 && count < 3){//此时可分配的审稿人数目小于3
                minThree.add(pcMemberPaperNum.get(j));
                minThreeIndex.add(j);
                count ++;
            }else if (assignedState.get(j) == 0 && count == 3){//此时已有3个可分配的审稿人，需要用已分配稿件数更少的审稿人进行替换
                changeMinThree(minThree, minThreeIndex, pcMemberPaperNum.get(j), j);
            }
        }
        return minThreeIndex;
    }

    private void addComment(Paper paper, User user){
        Comment comment = new Comment();
        Conference conference = paper.getConference();
        Referee referee = refereeRepository.findByConferenceAndUser(conference, user);
        System.out.println("referee:"+referee);
        comment.setReferee(referee);
        comment.setPaper(paper);
        comment.setContent("null");
        comment.setConfidence(Confidence.LOW);
        comment.setConfirm(false);
        comment.setSubmit(false);
        System.out.println("insert-comment:"+comment);
        commentRepository.save(comment);
    }

    //基于审稿平均负担的分配策略
    //输入：待分配的稿件列表、参与审稿的用户（PC Member）
    private boolean basedAverage(List<Paper> paper, List<User> pcMember){
        //对每个pcMember维护一个一维数组，内容为当前pcMember已分配到的论文数，初始值为0
        List<Integer> pcMemberPaperNum = new ArrayList<>();
        for(User user : pcMember){
            pcMemberPaperNum.add(0);
        }
        System.out.println("pcMemberPaperNum: "+pcMemberPaperNum);
        //对每个paper维护一个二维数组，一维下标为paper数，二维下标为pcMember数
        //初始化该二维表：-1表示该paper不能分配给该pcMember，0表示可以但未分配，1表示分配
        int rows = paper.size();
        int cols = pcMember.size();
        List<List<Integer>> isAssigned = new ArrayList<>(rows);
        //debug-test相关
        System.out.println("初始化的二维表");
        for (Paper value : paper) {
            List<Integer> rowList = new ArrayList<>(cols);
            for (User user : pcMember) {
                if (findRelatedPaper(value, user))
                    rowList.add(-1);
                else
                    rowList.add(0);
            }
            isAssigned.add(rowList);
            System.out.println(rowList);
        }
        //debug-test相关
        System.out.println("分配结束的二维表");
        //预分配稿件
        for (int i=0; i<rows; i++) {
            //对于每个paper[i]
            List<Integer> assignedState = isAssigned.get(i);
            //得到paper[i]可分配的且已分配稿件数最少的3个审稿人下标
            List<Integer> assignedReviewer = minThreeReviewer(pcMemberPaperNum, assignedState);
            System.out.println("第"+i+"个paper可分配的稿件数最少的3个审稿人下标"+assignedReviewer);
            //如果可分配的审稿人数目小于3，那么直接返回分配不成功，这也代表整个稿件分配策略不成功
            if(assignedReviewer == null || assignedReviewer.size() < 3)
                return false;
            //预分配：把二维表的内容进行修改
            for (int j=0; j<3; j++){
                int index = assignedReviewer.get(j);
                assignedState.set(index, 1);
                pcMemberPaperNum.set(index, pcMemberPaperNum.get(index)+1);
            }
            //debug-test相关
            System.out.println("paper"+i+assignedState);
        }
        //真实分配稿件
        for (int i=0; i<rows; i++){//对于第i个paper
            List<Integer> assignedState = isAssigned.get(i);
            for (int j=0; j<cols; j++){
                //添加相应comment表的信息
                if(assignedState.get(j) == 1){
                    addComment(paper.get(i), pcMember.get(j));
                }
            }
        }
        return true;
    }

    //基于topic相关度的分配策略
    //输入：待分配的稿件列表、参与审稿的用户（PC Member）
    //需要修改
    private boolean basedTopic(Conference conference, List<User> pcMember){
        //得到该会议的所有topic
        List<Topic> topicList = topicRepository.findByConference(conference);
        //按topic得到每个topic下的paper和pcMember
        for (Topic topic : topicList){
            List<Relate> relateList = relateRepository.findByTopic(topic);
            List<Paper> paperList = new ArrayList<>();
            for (Relate relate : relateList){
                paperList.add(relate.getPaper());
            }
            List<Charge> chargeList = chargeRepository.findByTopic(topic);
            List<User> pcMemberList = new ArrayList<>();
            for (Charge charge : chargeList){
                pcMemberList.add(charge.getReferee().getUser());
            }
            if(pcMemberList.size() >= 3){//若该主题下的审稿人大于等于3个人，在这个主题下平均分配
                if(!basedAverage(paperList, pcMemberList))
                    return false;
            }
            else {//若该主题下的审稿人不到3个人
                if(!basedAverage(paperList, pcMember))
                    return false;
            }
        }
        return true;
    }

    //测试接口的
    private boolean testAssignment(int strategy, List<Paper> paper, List<User> pcMember){
        if(strategy != 1 && strategy != 2)
            return false;
        //添加一个审稿信息作为测试 把paper[0]分配给pcMember[0]
        Comment comment = new Comment();
        Conference conference = paper.get(0).getConference();
        Referee referee = refereeRepository.findByConferenceAndUser(conference, pcMember.get(0));
        System.out.println("referee:"+referee);
        comment.setReferee(referee);
        comment.setPaper(paper.get(0));
        comment.setContent("null");
        comment.setConfidence(Confidence.LOW);
        comment.setConfirm(false);
        comment.setSubmit(false);
        System.out.println("insert-comment:"+comment);
        commentRepository.save(comment);
        return true;
    }

    //稿件分配
    private boolean assignmentPaper(Conference conference, int strategy, List<Paper> paper, List<User> pcMember){
        if(strategy == 1)
            return basedTopic(conference, pcMember);
        else if(strategy == 2)
            return basedAverage(paper, pcMember);
        return false;
    }

    @Override
    public boolean startReview(String cid, int strategy) {
        //通过cid找到会议
        Conference conference = conferenceRepository.findById(cid)
                .orElseThrow(() -> new WebException("会议不存在"));
        System.out.println("conf:"+conference);
        //判断会议是否至少有2个pcMember
        List<Referee> refereeList = refereeRepository.findByConference(conference);
        if(refereeList.size() < 2)
            return false;
        //判断会议是否在投稿状态
        if(conference.getConferenceState() != ConferenceState.UNDER_SUBMISSION)
            return false;
        System.out.println("11");
        //若满足上述两个条件，进行分配
        //得到该会议的所有投稿
        List<Paper> paperList = paperRepository.findByConference(conference);
        System.out.println("paperList:");
        for(Paper item : paperList){
            System.out.println(item);
        }
        //同时得到该会议所有的审稿人
        System.out.println("pcMemberList:");
        List<User> pcMemberList = new ArrayList<>();
        for(Referee item : refereeList){
            pcMemberList.add(item.getUser());
            System.out.println("pcMember"+item.getUser().getUid()+" : "+item.getUser());
        }
        //return assignmentPaper(conference, strategy, paperList, pcMemberList);
        //test
        return assignmentPaper(conference, strategy, paperList, pcMemberList);
    }
}
