package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Review;
import com.ebookstore.dto.request.ReviewReq;
import java.util.List;

public interface IReviewService extends IService<Review> {
    void addReview(ReviewReq req, Long userId);
    List<Review> getBookReviews(Long bookId);
    void deleteReview(Long id);
}
