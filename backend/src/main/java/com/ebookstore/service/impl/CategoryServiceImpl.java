package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.Category;
import com.ebookstore.mapper.CategoryMapper;
import com.ebookstore.service.interf.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Category> getParentCategories() {
        return list(new QueryWrapper<Category>()
                .eq("parent_id", 0).eq("status", 1)
                .orderByAsc("sort_order"));
    }

    @Override
    public List<Category> getChildren(Long parentId) {
        return list(new QueryWrapper<Category>()
                .eq("parent_id", parentId).eq("status", 1)
                .orderByAsc("sort_order"));
    }

    @Override
    public Map<String, List<Category>> getCategoryTree() {
        List<Category> parents = getParentCategories();
        Map<String, List<Category>> tree = new LinkedHashMap<>();
        for (Category parent : parents) {
            List<Category> children = getChildren(parent.getId());
            tree.put(parent.getName(), children);
        }
        return tree;
    }
}
