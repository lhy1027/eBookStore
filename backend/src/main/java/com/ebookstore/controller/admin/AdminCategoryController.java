package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.Category;
import com.ebookstore.service.interf.ICategoryService;
import com.ebookstore.service.interf.IBookService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final ICategoryService categoryService;
    private final IBookService bookService;

    @GetMapping
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    @PostMapping
    @Log("新增分类")
    public Result<String> add(@RequestBody Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error(400, "分类名称不能为空");
        }
        categoryService.save(category);
        return Result.success("新增成功");
    }

    @PutMapping("/{id}")
    @Log("修改分类")
    public Result<String> update(@PathVariable Long id, @RequestBody Category category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return Result.error(400, "分类名称不能为空");
        }
        category.setId(id);
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Log("删除分类")
    public Result<String> delete(@PathVariable Long id) {
        long count = bookService.count(new QueryWrapper<com.ebookstore.entity.Book>().eq("category_id", id));
        if (count > 0) {
            return Result.error(400, "该分类下有关联图书，无法删除");
        }
        categoryService.removeById(id);
        return Result.success("删除成功");
    }
}
