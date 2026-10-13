package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Book;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.vo.Result;      
import lombok.RequiredArgsConstructor;  
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookController {

    private final IBookService bookService;

    @GetMapping
    public Result<IPage<Book>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword).or().like("book_no", keyword);
        }
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        wrapper.orderByDesc("create_time");
        return Result.success(bookService.page(new Page<>(page, size), wrapper));
    }

    @GetMapping("/{id}")
    public Result<Book> detail(@PathVariable Long id) {
        return Result.success(bookService.getById(id));
    }

    @PostMapping
    @Log("新增图书")
    public Result<String> add(@RequestBody Book book) {
        if (book.getBookNo() == null || book.getBookNo().trim().isEmpty()) {
            return Result.error(400, "图书编号不能为空");
        }
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            return Result.error(400, "图书名称不能为空");
        }
        bookService.save(book);
        return Result.success("新增成功");
    }

    @PutMapping("/{id}")
    @Log("修改图书")
    public Result<String> update(@PathVariable Long id, @RequestBody Book book) {
        book.setId(id);
        bookService.updateById(book);
        return Result.success("修改成功");
    }

    @PutMapping("/{id}/status")
    @Log("上下架图书")
    public Result<String> toggleStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Book book = bookService.getById(id);
        book.setStatus(params.get("status"));
        bookService.updateById(book);
        return Result.success("操作成功");
    }

    @PutMapping("/{id}/recommend")
    @Log("推荐图书")
    public Result<String> toggleRecommend(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Book book = bookService.getById(id);
        book.setRecommendStatus(params.get("recommendStatus"));
        bookService.updateById(book);
        return Result.success("操作成功");
    }

    @PutMapping("/{id}/stock")
    @Log("调整库存")
    public Result<String> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        bookService.updateStock(id, params.get("quantity"));
        return Result.success("库存调整成功");
    }

    @GetMapping("/warning")
    public Result<Object> stockWarning() {
        long count = bookService.count(new QueryWrapper<Book>().le("stock", 5));
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        return Result.success(map);
    }
}
