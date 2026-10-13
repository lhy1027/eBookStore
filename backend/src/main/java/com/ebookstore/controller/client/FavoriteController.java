package com.ebookstore.controller.client;

import com.ebookstore.service.interf.IFavoriteService;
import com.ebookstore.util.SecurityUtil;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final IFavoriteService favoriteService;

    @GetMapping
    public Result<List<Map<String, Object>>> list() {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(favoriteService.getUserFavorites(userId));
    }

    @PostMapping
    public Result<String> add(@RequestBody Map<String, Long> params) {
        Long userId = SecurityUtil.getCurrentUserId();
        favoriteService.addFavorite(userId, params.get("bookId"));
        return Result.success("收藏成功");
    }

    @DeleteMapping("/{bookId}")
    public Result<String> remove(@PathVariable Long bookId) {
        Long userId = SecurityUtil.getCurrentUserId();
        favoriteService.removeFavorite(userId, bookId);
        return Result.success("已取消收藏");
    }

    @GetMapping("/check/{bookId}")
    public Result<Boolean> check(@PathVariable Long bookId) {
        Long userId = SecurityUtil.getCurrentUserId();
        return Result.success(favoriteService.isFavorited(userId, bookId));
    }
}