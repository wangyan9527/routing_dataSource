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
        RoutingCodeUtils.setCode(code);
        Book book = new Book();
        book.setName(name);
        bookMapper.save(book);
    }

    @RequestMapping("/test")
    public void test() {
        List<Book> bookList = new ArrayList<>();
        int i = 1;
        while(true) {
            Book book = new Book();
            book.setName("test" + i++);
            bookList.add(book);
        }
    }

}
