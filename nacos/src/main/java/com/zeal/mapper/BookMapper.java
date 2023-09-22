package com.zeal.mapper;

import com.zeal.dto.Book;

import java.util.List;

public interface BookMapper {

    void save(Book book);

    List<Book> selectAll();
}
