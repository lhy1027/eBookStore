package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.dto.request.PurchaseReq;
import com.ebookstore.entity.Book;
import com.ebookstore.entity.Purchase;
import com.ebookstore.exception.BusinessException;
import com.ebookstore.mapper.PurchaseMapper;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.service.interf.IPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements IPurchaseService {

    private final IBookService bookService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPurchase(PurchaseReq req) {
        Book book = bookService.getById(req.getBookId());
        if (book == null) {
            throw new BusinessException("图书不存在");
        }
        Purchase purchase = new Purchase();
        purchase.setBookId(req.getBookId());
        purchase.setQuantity(req.getQuantity());
        purchase.setPurchasePrice(req.getPrice());
        purchase.setSupplier(req.getSupplier());
        purchase.setOperator(req.getOperator());
        purchase.setCreateTime(LocalDateTime.now());
        save(purchase);
        // 增加库存
        book.setStock(book.getStock() + req.getQuantity());
        bookService.updateById(book);
    }
}
