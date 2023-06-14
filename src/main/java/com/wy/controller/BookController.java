package com.wy.controller;

import com.wy.dto.Book;
import com.wy.mapper.BookMapper;
import com.wy.utils.RoutingCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @RequestMapping("/save")
    public void save(String name, String code) {
        try {
            RoutingCodeUtils.setCode(code);
            Book book = new Book();
            book.setName(name);
            bookMapper.save(book);
        } finally {
            RoutingCodeUtils.clear();
        }
    }

}
