package com.example.springgraphqlserver.controller;

import com.example.springgraphqlserver.entities.Author;
import com.example.springgraphqlserver.entities.Book;
import com.example.springgraphqlserver.form.AddingBookInput;
import com.example.springgraphqlserver.form.UpdateBookPayload;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @QueryMapping
    public Book bookById(@Argument String id) {
        return Book.getById(id);
    }

    @QueryMapping
    public List<Book> books() {
        return Book.getAll();
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.authorId());
    }

    @MutationMapping
    public UpdateBookPayload addBook(@Argument AddingBookInput input) {
        Author author = Author.addAuthor(input.author_firstName(), input.author_lastName());
        Book book = Book.add(input.name(), input.pageCount(), author.id());
        return new UpdateBookPayload(true, book);
    }
}
