package com.ebookstore.controller.client;

import com.ebookstore.entity.News;
import com.ebookstore.service.interf.INewsService;
import com.ebookstore.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/news")
@RequiredArgsConstructor
public class NewsController {

    private final INewsService newsService;

    @GetMapping
    public Result<List<News>> list() {
        return Result.success(newsService.getPublishedNews());
    }

    @GetMapping("/{id}")
    public Result<News> detail(@PathVariable Long id) {
        return Result.success(newsService.getById(id));
    }
}
