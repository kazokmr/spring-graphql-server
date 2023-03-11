package com.example.springgraphqlserver.presentation.controller;

import com.example.springgraphqlserver.domain.entities.Author;
import com.example.springgraphqlserver.domain.entities.Book;
import com.example.springgraphqlserver.presentation.form.AddingBookInput;
import com.example.springgraphqlserver.presentation.form.UpdateBookPayload;
import jakarta.validation.constraints.Pattern;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Controller
@Validated
public class BookController {

    @QueryMapping
    public Book bookById(
            @Argument
            @Pattern(regexp = "\\Abook-[1-9]\\d*$", message = "idは\"book-\"の後に数値を入れてください")
            String id) {
        return Book.getById(id);
    }

    @QueryMapping
    public List<Book> books() {
        return Book.getAll();
    }

//    @SchemaMapping
//    public Author author(Book book) {
//        return Author.getById(book.authorId());
//    }

    @BatchMapping
    public Map<Book, Author> author(List<Book> books) {
        return Author.getAllByBook(books);
    }

//    @BatchMapping
//    public List<Author> author(List<Book> books) {
//        return Author.getAllByBook(books).values().stream().toList();
//    }


    @MutationMapping
    public UpdateBookPayload addBook(@Argument AddingBookInput input) {
        Author author = Author.addAuthor(input.author_firstName(), input.author_lastName());
        Book book = Book.add(input.name(), input.pageCount(), author.id());
        return new UpdateBookPayload(true, book);
    }
}
