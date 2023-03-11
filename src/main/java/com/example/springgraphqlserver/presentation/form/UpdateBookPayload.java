package com.example.springgraphqlserver.presentation.form;

import com.example.springgraphqlserver.domain.entities.Book;

public record UpdateBookPayload(boolean success, Book book) {
}
