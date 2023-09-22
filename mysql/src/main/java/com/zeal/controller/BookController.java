package com.zeal.controller;

import com.zeal.dto.Book;
import com.zeal.mapper.BookMapper;
import com.zeal.utils.RoutingCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
