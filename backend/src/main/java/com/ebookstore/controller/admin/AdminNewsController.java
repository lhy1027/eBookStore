package com.ebookstore.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ebookstore.annotation.Log;
import com.ebookstore.entity.News;
import com.ebookstore.service.interf.INewsService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminNewsController {

    private final INewsService newsService;

    @GetMapping
    public Result<IPage<News>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(newsService.page(new Page<>(page, size),
                new QueryWrapper<News>().orderByDesc("create_time")));
    }

    @PostMapping
    @Log("发布公告")
    public Result<String> add(@RequestBody News news) {
        newsService.save(news);
        return Result.success("发布成功");
    }

    @PutMapping("/{id}")
    @Log("修改公告")
    public Result<String> update(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        newsService.updateById(news);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    @Log("删除公告")
    public Result<String> delete(@PathVariable Long id) {
        newsService.removeById(id);
        return Result.success("删除成功");
    }
}
