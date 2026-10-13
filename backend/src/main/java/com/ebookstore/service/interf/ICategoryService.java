package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.Category;
import java.util.List;
import java.util.Map;

public interface ICategoryService extends IService<Category> {
    List<Category> getParentCategories();
    List<Category> getChildren(Long parentId);
    Map<String, List<Category>> getCategoryTree();
}
