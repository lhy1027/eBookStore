package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.CartItem;
import java.util.List;

public interface ICartService extends IService<CartItem> {
    List<CartItem> getUserCart(Long userId);
    void addToCart(Long userId, Long bookId, Integer quantity);
    void updateQuantity(Long id, Long userId, Integer quantity);
    void removeFromCart(Long id, Long userId);
    void clearCart(Long userId);
    List<CartItem> getSelectedItems(Long userId, List<Long> cartItemIds);
    void removeSelected(Long userId, List<Long> cartItemIds);
}
