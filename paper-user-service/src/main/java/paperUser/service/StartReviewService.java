package paperUser.service;

import org.springframework.stereotype.Service;

@Service
public interface StartReviewService {
    //开启审稿的接口
    boolean startReview(String cid, int strategy);
}
