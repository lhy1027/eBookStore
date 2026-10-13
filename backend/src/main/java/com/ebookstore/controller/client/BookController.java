package com.ebookstore.controller.client;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ebookstore.dto.response.BookResp;
import com.ebookstore.entity.Review;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.IReviewService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/books")
@RequiredArgsConstructor
public class BookController {

    private final IBookService bookService;
    private final IReviewService reviewService;

    @GetMapping
    public Result<IPage<BookResp>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Integer recommendStatus) {
        return Result.success(bookService.searchBooks(page, size, keyword, categoryId, minPrice, maxPrice, sortBy, recommendStatus));
    }

    @GetMapping("/{id}")
    public Result<BookResp> detail(@PathVariable Long id) {
        return Result.success(bookService.getBookDetail(id));
    }

    @GetMapping("/{id}/reviews")
    public Result<List<Review>> reviews(@PathVariable Long id) {
        return Result.success(reviewService.getBookReviews(id));
    }

    @GetMapping("/recommended")
    public Result<IPage<BookResp>> recommended(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size) {
        return Result.success(bookService.searchBooks(page, size, null, null, null, null, null, 1));
    }

    @GetMapping("/new-books")
    public Result<IPage<BookResp>> newBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size) {
        return Result.success(bookService.searchBooks(page, size, null, null, null, null, "createTime", null));
    }

    @GetMapping("/hot-books")
    public Result<IPage<BookResp>> hotBooks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "8") Integer size) {
        return Result.success(bookService.searchBooks(page, size, null, null, null, null, "sales", null));
    }
}
