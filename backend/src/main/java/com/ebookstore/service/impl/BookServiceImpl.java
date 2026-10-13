package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.response.BookResp;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Category;
import com.ebookstore.entity.Review;
import com.ebookstore.mapper.BookMapper;
import com.ebookstore.mapper.ReviewMapper;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.ICategoryService;
import com.ebookstore.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    private final ICategoryService categoryService;
    private final ReviewMapper reviewMapper;

    @Override
    public IPage<BookResp> searchBooks(Integer page, Integer size, String keyword, Long categoryId,
                                       Double minPrice, Double maxPrice, String sortBy, Integer recommendStatus) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like("name", keyword)
                    .or().like("author", keyword)
                    .or().like("publisher", keyword)
                    .or().like("isbn", keyword));
        }
        if (categoryId != null) {
            // 检查是否有子分类，如果有则查询所有子分类的图书
            List<Category> children = categoryService.getChildren(categoryId);
            if (children != null && !children.isEmpty()) {
                List<Long> childIds = children.stream().map(Category::getId).collect(Collectors.toList());
                childIds.add(categoryId);
                wrapper.in("category_id", childIds);
            } else {
                wrapper.eq("category_id", categoryId);
            }
        }
        if (minPrice != null) {
            wrapper.ge("sale_price", minPrice);
        }
        if (maxPrice != null) {
            wrapper.le("sale_price", maxPrice);
        }
        if (recommendStatus != null) {
            wrapper.eq("recommend_status", recommendStatus);
        }
        if ("sales".equals(sortBy)) {
            wrapper.orderByDesc("sales_count");
        } else if ("price_asc".equals(sortBy)) {
            wrapper.orderByAsc("sale_price");
        } else if ("price_desc".equals(sortBy)) {
            wrapper.orderByDesc("sale_price");
        } else {
            wrapper.orderByDesc("create_time");
        }

        Page<Book> bookPage = new Page<>(page, size);
        IPage<Book> result = page(bookPage, wrapper);

        Map<Long, String> categoryMap = categoryService.list().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        return result.convert(book -> {
            BookResp resp = new BookResp();
            resp.setId(book.getId());
            resp.setBookNo(book.getBookNo());
            resp.setIsbn(book.getIsbn());
            resp.setName(book.getName());
            resp.setAuthor(book.getAuthor());
            resp.setPublisher(book.getPublisher());
            resp.setPublishDate(book.getPublishDate());
            resp.setCategoryId(book.getCategoryId());
            resp.setCategoryName(categoryMap.getOrDefault(book.getCategoryId(), ""));
            resp.setCoverUrl(book.getCoverUrl());
            resp.setDescription(book.getDescription());
            resp.setPrice(book.getPrice());
            resp.setSalePrice(book.getSalePrice());
            resp.setStock(book.getStock());
            resp.setStatus(book.getStatus());
            resp.setRecommendStatus(book.getRecommendStatus());
            resp.setSalesCount(book.getSalesCount());
            resp.setAvgRating(getAvgRating(book.getId()));
            resp.setReviewCount(getReviewCount(book.getId()));
            return resp;
        });
    }

    @Override
    public BookResp getBookDetail(Long id) {
        Book book = getById(id);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }
        Category category = categoryService.getById(book.getCategoryId());
        BookResp resp = new BookResp();
        resp.setId(book.getId());
        resp.setBookNo(book.getBookNo());
        resp.setIsbn(book.getIsbn());
        resp.setName(book.getName());
        resp.setAuthor(book.getAuthor());
        resp.setPublisher(book.getPublisher());
        resp.setPublishDate(book.getPublishDate());
        resp.setCategoryId(book.getCategoryId());
        resp.setCategoryName(category != null ? category.getName() : "");
        resp.setCoverUrl(book.getCoverUrl());
        resp.setDescription(book.getDescription());
        resp.setDetail(book.getDetail());
        resp.setPrice(book.getPrice());
        resp.setSalePrice(book.getSalePrice());
        resp.setStock(book.getStock());
        resp.setStatus(book.getStatus());
        resp.setRecommendStatus(book.getRecommendStatus());
        resp.setSalesCount(book.getSalesCount());
        resp.setAvgRating(getAvgRating(id));
        resp.setReviewCount(getReviewCount(id));
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long bookId, Integer quantity) {
        Book book = getById(bookId);
        if (book == null) {
            throw new BusinessException("图书不存在");
        }
        int newStock = book.getStock() + quantity;
        if (newStock < 0) {
            throw new BusinessException("库存不足");
        }
        book.setStock(newStock);
        updateById(book);
    }

    private Double getAvgRating(Long bookId) {
        List<Review> reviews = reviewMapper.selectList(
                new QueryWrapper<Review>().eq("book_id", bookId));
        if (reviews.isEmpty()) return 0.0;
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        return Math.round(avg * 10.0) / 10.0;
    }

    private Integer getReviewCount(Long bookId) {
        return Math.toIntExact(reviewMapper.selectCount(
                new QueryWrapper<Review>().eq("book_id", bookId)));
    }
}
