package com.example.springgraphqlserver.form;

import com.example.springgraphqlserver.entities.Book;

public record UpdateBookPayload(boolean success, Book book) {
}
