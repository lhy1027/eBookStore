package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Review;
import com.ebookstore.entity.User;
import com.ebookstore.mapper.BookMapper;
import com.ebookstore.mapper.UserMapper;
import com.ebookstore.service.interf.IReviewService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final IReviewService reviewService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    @GetMapping
    public Result<IPage<Review>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Review> result = reviewService.page(new Page<>(page, size),
                new QueryWrapper<Review>().orderByDesc("create_time"));
        // 填充用户名和书名
        List<Review> reviews = result.getRecords();
        if (!reviews.isEmpty()) {
            List<Long> userIds = reviews.stream().map(Review::getUserId).distinct().collect(Collectors.toList());
            List<Long> bookIds = reviews.stream().map(Review::getBookId).distinct().collect(Collectors.toList());
            Map<Long, String> userMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, User::getUsername));
            Map<Long, String> bookMap = bookMapper.selectBatchIds(bookIds).stream()
                    .collect(Collectors.toMap(Book::getId, Book::getName));
            reviews.forEach(r -> {
                r.setUsername(userMap.getOrDefault(r.getUserId(), "未知用户"));
                r.setBookName(bookMap.getOrDefault(r.getBookId(), "未知图书"));
            });
        }
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    @Log("删除评价")
    public Result<String> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return Result.success("删除成功");
    }
}
