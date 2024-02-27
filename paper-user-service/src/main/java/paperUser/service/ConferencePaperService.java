package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.Paper;
import paperUser.vo.PaperVo;

import java.util.List;

@Service
public interface ConferencePaperService {
    void submitPaper(String uId, String cId, PaperVo paperVo);

    List<Paper> getPaperByUId(String uId, String username);

    void revisePaper(PaperVo paperVo, String username);

    void withdrawPaper(String pId, String username);
}
