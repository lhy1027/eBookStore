package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Favorite;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.FavoriteMapper;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.IFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {

    private final IBookService bookService;

    @Override
    public void addFavorite(Long userId, Long bookId) {
        Favorite exist = getOne(new QueryWrapper<Favorite>()
                .eq("user_id", userId).eq("book_id", bookId));
        if (exist != null) {
            throw new BusinessException("已收藏该图书");
        }
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setBookId(bookId);
        save(fav);
    }

    @Override
    public void removeFavorite(Long userId, Long bookId) {
        Favorite exist = getOne(new QueryWrapper<Favorite>()
                .eq("user_id", userId).eq("book_id", bookId));
        if (exist == null) {
            throw new BusinessException("尚未收藏该图书");
        }
        removeById(exist.getId());
    }

    @Override
    public boolean isFavorited(Long userId, Long bookId) {
        return count(new QueryWrapper<Favorite>()
                .eq("user_id", userId).eq("book_id", bookId)) > 0;
    }

    @Override
    public List<Map<String, Object>> getUserFavorites(Long userId) {
        List<Favorite> favs = list(new QueryWrapper<Favorite>()
                .eq("user_id", userId).orderByDesc("create_time"));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Favorite fav : favs) {
            Book book = bookService.getById(fav.getBookId());
            if (book != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", fav.getId());
                map.put("bookId", book.getId());
                map.put("bookName", book.getName());
                map.put("author", book.getAuthor());
                map.put("coverUrl", book.getCoverUrl());
                map.put("price", book.getPrice());
                map.put("salePrice", book.getSalePrice());
                map.put("status", book.getStatus());
                map.put("createTime", fav.getCreateTime());
                result.add(map);
            }
        }
        return result;
    }
}