package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.model.Book;
import com.example.demo.repo.jpa.BookJpaRepository;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.LinkedHashMap;


@RestController
public class ApiController {

    private final BookJpaRepository bookJpaRepository;
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public ApiController(BookJpaRepository bookJpaRepository, BookService bookService, ObjectMapper objectMapper) {
        this.bookJpaRepository = bookJpaRepository;
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    private static Logger log = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping("/")
    public Map helloWolrd() throws Exception {

        System.out.println(this.objectMapper.readTree(System.getenv("VCAP_SERVICES")).
                get("p-cloudcache").get(0).get("credentials").get("users").get(1).get("password"));

        log.info("Handling home");      
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message","Helloworld V2");
        body.put("index", System.getenv("CF_INSTANCE_INDEX"));
        body.put("host", System.getenv("CF_INSTANCE_IP"));
        body.put("java", System.getProperty("java.vm.version"));
        return body;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allbooks")
    public Object getAllBook() throws Exception {
        log.info("Handling allbooks");      
        return bookJpaRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/book")
    public Object getBookById(@RequestParam(value = "id") String id) throws Exception {
        log.info("Handling book");
        String ds="";
        Book book = bookService.getBookById(id);
        Boolean b = bookService.isCacheMiss();
        if (b) {
            ds = "MYSQL";
        } else if (!b) {
            ds = "PCC";
        }

        JSONObject jsonObject = new JSONObject(this.objectMapper.writeValueAsString(book));

        return jsonObject.put("ds",ds).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dummy")
    public Object getText() {
        return "I'm available!";
    }

}
