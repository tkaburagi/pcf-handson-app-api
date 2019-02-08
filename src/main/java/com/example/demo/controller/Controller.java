package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repo.jpa.BookJpaRepository;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    BookService bookService;

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping("/")
    public String helloWolrd() {
        return "Hello world";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allbooks")
    public String getAllBook() throws Exception {

        return mapper.writeValueAsString(bookJpaRepository.findAll());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/book")
    public String getBookById(@RequestParam(value = "id") String id) throws Exception {

        String ds="";
        Book book = bookService.getBookById(id);
        Boolean b = bookService.isCacheMiss();
        if (b) {
            ds = "MYSQL";
        } else if (!b) {
            ds = "PCC";
        }

        JSONObject jsonObject = new JSONObject(mapper.writeValueAsString(book));
        jsonObject.put("ds",ds);

        return jsonObject.toString();
    }

}
