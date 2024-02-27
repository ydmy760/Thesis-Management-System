package paperUser.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paperUser.entity.*;
import paperUser.exception.WebException;
import paperUser.repository.*;
import paperUser.service.ConferencePaperService;
import paperUser.vo.PaperVo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ConferencePaperServiceImpl implements ConferencePaperService {
    private final PaperRepository paperRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;
    private final RefereeRepository refereeRepository;
    private final AuthorRepository authorRepository;
    private final RelateRepository relateRepository;

    @Autowired
    ConferencePaperServiceImpl(PaperRepository paperRepository, UserRepository userRepository,
                               ConferenceRepository conferenceRepository, RefereeRepository participateRepository,
                               AuthorRepository authorRepository, RelateRepository relateRepository) {
        this.paperRepository = paperRepository;
        this.userRepository = userRepository;
        this.conferenceRepository = conferenceRepository;
        this.refereeRepository = participateRepository;
        this.authorRepository = authorRepository;
        this.relateRepository = relateRepository;
    }


    @Override
    public void submitPaper(String uId, String cId, PaperVo paperVo) {
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new WebException("用户不存在"));
        conferenceRepository.findById(cId).orElseThrow(() -> new WebException("会议不存在"));
        String pid = paperVo.getPaper().getPid();
//        savePaperFile(pid,file);
        paperRepository.save(paperVo.getPaper());
        List<Topic> topicList = paperVo.getTopicList();
        for(Topic topic : topicList){
            Relate relate = new Relate();
            relate.setPaper(paperVo.getPaper());
            relate.setTopic(topic);
            relateRepository.save(relate);
        }
    }

    @Override
    public List<Paper> getPaperByUId(String uId, String username) {
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new WebException("用户不存在"));
        if (!user.getUsername().equals(username)) {
            throw new WebException("用户不匹配");
        }
        List<Paper> paperList = paperRepository.findByUser(user);
        for (Paper paper : paperList) {
            List<Author> authorList = authorRepository.findByPaper(paper);
            paper.setAuthorList(authorList);
        }
        return paperList;
    }

    static public  boolean savePaperFile(String fileName , MultipartFile file) {
        if(file.isEmpty()) {
            return false;
        }
        String uploadDir = "./temp/";
        Path filePath = Paths.get(uploadDir, fileName);
        // 检查文件是否存在
        File currFile = new File(filePath.toString());
        if (currFile.exists() && currFile.isFile()) {
            // 文件存在,删除旧文件
            currFile.delete();
        }
        // 保存新文件
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    @Override
    public void revisePaper(PaperVo paperVo, String username) {
        Paper paper = paperRepository.findById(paperVo.getPaper().getPid())
                .orElseThrow(() -> new WebException("投稿不存在"));
        if (!paper.getUser().getUsername().equals(username)) {
            throw new WebException("用户不匹配");
        }
        paperRepository.delete(paper);
//        savePaperFile(new_paper.getPaper().getPid(),file);
        paperRepository.save(paperVo.getPaper());
        List<Topic> topicList = paperVo.getTopicList();
        // 不为空说明要更新
        if (!topicList.isEmpty()){
            List<Relate> relateList = relateRepository.findByPaper(paper);
            relateRepository.deleteAll(relateList);
            for(Topic topic : topicList){
                Relate  relate = new Relate();
                relate.setPaper(paperVo.getPaper());
                relate.setTopic(topic);
                relateRepository.save(relate);
            }
        }
    }

    @Override
    public void withdrawPaper(String pId, String username) {
        Paper paper = paperRepository.findById(pId)
                .orElseThrow(() -> new WebException("投稿不存在"));
        if (!paper.getUser().getUsername().equals(username)) {
            throw new WebException("用户不匹配");
        }
        paperRepository.delete(paper);
    }
}
