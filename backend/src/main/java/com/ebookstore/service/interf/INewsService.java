package com.ebookstore.service.interf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ebookstore.entity.News;
import java.util.List;

public interface INewsService extends IService<News> {
    List<News> getPublishedNews();
}
