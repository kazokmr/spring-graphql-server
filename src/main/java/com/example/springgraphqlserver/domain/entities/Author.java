package com.example.springgraphqlserver.domain.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Author(String id, String firstName, String lastName) {

    private static final List<Author> authors = new ArrayList<>(
            Arrays.asList(
                    new Author("author-1", "Joshua", "Block"),
                    new Author("author-2", "Douglas", "Adams"),
                    new Author("author-3", "Bill", "Bryson")
            )
    );

    public static Author getById(String id) {
        return authors.stream().filter(author -> author.id().equals(id)).findFirst().orElse(null);
    }

    public static Author addAuthor(String firstName, String lastName) {

        Author author = authors.stream().filter(a -> a.firstName().equals(firstName) && a.lastName().equals(lastName))
                .findFirst().orElse(null);
        if (author == null) {
            author = new Author("author-" + (authors.size() + 1), firstName, lastName);
            authors.add(author);
        }
        return author;
    }

    public static Map<Book, Author> getAllByBook(List<Book> books) {
        return books.stream()
                .collect(Collectors.toMap(
                        book -> book,
                        book -> getById(book.authorId())
                ));
    }
}
