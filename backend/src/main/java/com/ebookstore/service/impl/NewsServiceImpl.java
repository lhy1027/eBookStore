package com.ebookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ebookstore.entity.News;
import com.ebookstore.mapper.NewsMapper;
import com.ebookstore.service.interf.INewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Override
    public List<News> getPublishedNews() {
        return list(new QueryWrapper<News>().eq("status", 1)
                .orderByDesc("create_time"));
    }
}
