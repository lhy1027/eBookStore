package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.dto.response.BookResp;
import com.ebookstore.entity.Book;

public interface IBookService extends IService<Book> {
    IPage<BookResp> searchBooks(Integer page, Integer size, String keyword, Long categoryId,
                                Double minPrice, Double maxPrice, String sortBy, Integer recommendStatus);
    BookResp getBookDetail(Long id);
    void updateStock(Long bookId, Integer quantity);
}
