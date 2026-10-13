package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.request.ReviewReq;
import com.ebookstore.entity.OrderItem;
import com.ebookstore.entity.Review;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.OrderItemMapper;
import com.ebookstore.mapper.ReviewMapper;
import com.ebookstore.service.interf.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements IReviewService {

    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReview(ReviewReq req, Long userId) {
        OrderItem orderItem = orderItemMapper.selectById(req.getOrderItemId());
        if (orderItem == null) {
            throw new BusinessException("订单明细不存在");
        }
        if (orderItem.getReviewed() == 1) {
            throw new BusinessException("该商品已评价");
        }
        Review review = new Review();
        review.setUserId(userId);
        review.setOrderItemId(req.getOrderItemId());
        review.setBookId(req.getBookId());
        review.setRating(req.getRating());
        review.setContent(req.getContent());
        review.setCreateTime(LocalDateTime.now());
        save(review);
        // 标记已评�?        orderItem.setReviewed(1);
        orderItemMapper.updateById(orderItem);
    }

    @Override
    public List<Review> getBookReviews(Long bookId) {
        return list(new QueryWrapper<Review>().eq("book_id", bookId)
                .orderByDesc("create_time"));
    }

    @Override
    public void deleteReview(Long id) {
        removeById(id);
    }
}
