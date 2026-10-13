package com.ebookstore.controller.client;

import com.ebookstore.entity.Category;
import com.ebookstore.service.interf.ICategoryService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/tree")
    public Result<Map<String, List<Category>>> tree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @GetMapping
    public Result<List<Category>> list() {
        return Result.success(categoryService.getParentCategories());
    }

    @GetMapping("/{parentId}/children")
    public Result<List<Category>> children(@PathVariable Long parentId) {
        return Result.success(categoryService.getChildren(parentId));
    }
}
