package com.example.springgraphqlserver.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Book(String id, String name, int pageCount, String authorId) {

    private static List<Book> books = new ArrayList<>(
            Arrays.asList(
                    new Book("book-1", "Effective Java", 416, "author-1"),
                    new Book("book-2", "Hitchhiker's Guide to the Galaxy", 208, "author-2"),
                    new Book("book-3", "Down Under", 436, "author-3")
            ));

    public static Book getById(String id) {
        return books.stream().filter(book -> book.id().equals(id)).findFirst().orElse(null);
    }

    public static Book add(String name, int pageCount, String authorId) {
        String id = "book-" + (books.size() + 1);
        Book book = new Book(id, name, pageCount, authorId);
        books.add(book);
        return book;
    }

}