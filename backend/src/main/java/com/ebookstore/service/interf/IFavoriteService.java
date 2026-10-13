package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Favorite;
import java.util.List;
import java.util.Map;

public interface IFavoriteService extends IService<Favorite> {
    void addFavorite(Long userId, Long bookId);
    void removeFavorite(Long userId, Long bookId);
    boolean isFavorited(Long userId, Long bookId);
    List<Map<String, Object>> getUserFavorites(Long userId);
}