package com.example.demo.repo.gem;

import com.example.demo.model.Book;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGemFireRepository extends GemfireRepository<Book, String> {

    @Query("SELECT * FROM /book b WHERE b.id = $1")
    Book findBookById(String id);

}
