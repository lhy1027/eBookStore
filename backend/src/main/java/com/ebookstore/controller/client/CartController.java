package com.ebookstore.controller.client;

import com.ebookstore.dto.response.CartItemResp;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.CartItem;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.ICartService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartService cartService;
    private final IBookService bookService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Result<List<CartItemResp>> list() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<CartItem> items = cartService.getUserCart(userId);
        List<CartItemResp> resp = items.stream().map(item -> {
            CartItemResp r = new CartItemResp();
            r.setId(item.getId());
            r.setBookId(item.getBookId());
            r.setQuantity(item.getQuantity());
            Book book = bookService.getById(item.getBookId());
            if (book != null) {
                r.setBookName(book.getName());
                r.setCoverUrl(book.getCoverUrl());
                r.setAuthor(book.getAuthor());
                r.setPrice(book.getPrice());
                r.setSalePrice(book.getSalePrice());
                r.setSubtotal(book.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            return r;
        }).collect(Collectors.toList());
        return Result.success(resp);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Result<String> add(@RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Long bookId = Long.valueOf(params.get("bookId").toString());
        Integer quantity = Integer.valueOf(params.getOrDefault("quantity", 1).toString());
        cartService.addToCart(userId, bookId, quantity);
        return Result.success("已加入购物车");
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<String> update(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        cartService.updateQuantity(id, userId, quantity);
        return Result.success("已更新数量");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Result<String> remove(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.removeFromCart(id, userId);
        return Result.success("已删除");
    }

    @DeleteMapping("/clear")
    @PreAuthorize("isAuthenticated()")
    public Result<String> clear() {
        Long userId = SecurityUtil.getCurrentUserId();
        cartService.clearCart(userId);
        return Result.success("购物车已清空");
    }
}