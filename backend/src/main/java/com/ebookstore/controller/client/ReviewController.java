package com.ebookstore.controller.client;

import com.ebookstore.entity.Review;
import com.ebookstore.dto.request.ReviewReq;
import com.ebookstore.service.interf.IReviewService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<String> add(@Valid @RequestBody ReviewReq req) {
        Long userId = SecurityUtil.getCurrentUserId();
        reviewService.addReview(req, userId);
        return Result.success("评价成功");
    }

    @GetMapping("/book/{bookId}")
    public Result<List<Review>> bookReviews(@PathVariable Long bookId) {
        return Result.success(reviewService.getBookReviews(bookId));
    }
}