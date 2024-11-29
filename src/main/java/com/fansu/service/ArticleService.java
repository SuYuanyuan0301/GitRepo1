package com.fansu.service;

import com.fansu.pojo.Article;
import com.fansu.pojo.PageBean;

public interface ArticleService {

    //添加文章
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
