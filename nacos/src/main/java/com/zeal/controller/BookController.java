package com.zeal.controller;

import com.zeal.dto.Book;
import com.zeal.mapper.BookMapper;
import com.zeal.utils.RoutingCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @RequestMapping("/batchSelect")
    public void batchSelect(String code) {
        try {
            RoutingCodeUtils.setCode(code);
            Long startTime = System.currentTimeMillis();
            Long currentTime;
            while((currentTime = System.currentTimeMillis()) - startTime < 1000 * 10) {
                List<Book> bookList = bookMapper.selectAll();
                System.out.println(bookList + " => " + currentTime);
            }
        } finally {
            RoutingCodeUtils.clear();
        }
    }

}
