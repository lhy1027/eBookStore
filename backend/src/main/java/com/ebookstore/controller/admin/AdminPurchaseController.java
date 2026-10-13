package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.dto.request.PurchaseReq;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Purchase;
import com.ebookstore.entity.User;
import com.ebookstore.mapper.BookMapper;
import com.ebookstore.mapper.UserMapper;
import com.ebookstore.service.interf.IPurchaseService;
import com.ebookstore.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/purchases")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPurchaseController {

    private final IPurchaseService purchaseService;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    @GetMapping
    public Result<IPage<Purchase>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<Purchase> result = purchaseService.page(new Page<>(page, size),
                new QueryWrapper<Purchase>().orderByDesc("create_time"));
        // 填充关联字段
        List<Purchase> records = result.getRecords();
        if (!records.isEmpty()) {
            List<Long> bookIds = records.stream().map(Purchase::getBookId).distinct().collect(Collectors.toList());
            Map<Long, String> bookNameMap = bookMapper.selectBatchIds(bookIds).stream()
                    .collect(Collectors.toMap(Book::getId, Book::getName));
            for (Purchase p : records) {
                p.setBookName(bookNameMap.getOrDefault(p.getBookId(), "未知"));
                p.setPrice(p.getPurchasePrice());
            }
        }
        return Result.success(result);
    }

    @PostMapping
    @Log("进货")
    public Result<String> add(@Valid @RequestBody PurchaseReq req) {
        purchaseService.addPurchase(req);
        return Result.success("进货成功");
    }
}