package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.CartItem;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.CartItemMapper;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements ICartService {

    private final IBookService bookService;

    @Override
    public List<CartItem> getUserCart(Long userId) {
        return list(new QueryWrapper<CartItem>().eq("user_id", userId));
    }

    @Override
    public void addToCart(Long userId, Long bookId, Integer quantity) {
        Book book = bookService.getById(bookId);
        if (book == null || book.getStatus() != 1) {
            throw new BusinessException("图书不存在或已下架");
        }
        if (book.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }
        CartItem exist = getOne(new QueryWrapper<CartItem>()
                .eq("user_id", userId).eq("book_id", bookId));
        if (exist != null) {
            int newQty = exist.getQuantity() + quantity;
            if (newQty > book.getStock()) {
                throw new BusinessException("库存不足");
            }
            exist.setQuantity(newQty);
            updateById(exist);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setBookId(bookId);
            item.setQuantity(quantity);
            save(item);
        }
    }

    @Override
    public void updateQuantity(Long id, Long userId, Integer quantity) {
        CartItem item = getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        Book book = bookService.getById(item.getBookId());
        if (book != null && quantity > book.getStock()) {
            throw new BusinessException("库存不足");
        }
        item.setQuantity(quantity);
        updateById(item);
    }

    @Override
    public void removeFromCart(Long id, Long userId) {
        CartItem item = getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        removeById(id);
    }

    @Override
    public void clearCart(Long userId) {
        remove(new QueryWrapper<CartItem>().eq("user_id", userId));
    }

    @Override
    public List<CartItem> getSelectedItems(Long userId, List<Long> cartItemIds) {
        return list(new QueryWrapper<CartItem>()
                .eq("user_id", userId)
                .in("id", cartItemIds));
    }

    @Override
    public void removeSelected(Long userId, List<Long> cartItemIds) {
        for (Long id : cartItemIds) {
            removeById(id);
        }
    }
}
