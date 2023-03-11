package com.example.springgraphqlserver.presentation.form;

public record AddingBookInput(String name, int pageCount, String author_firstName, String author_lastName) {
}
