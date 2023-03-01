package com.example.springgraphqlserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(BookController.class)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldGetFirstBook() {
        this.graphQlTester
                // 指定したファイル名およびqueryのariasを指定する
                .documentName("bookDetails")
                // queryに渡すパラメータの値
                .variable("id", "book-1")
                // GraphQLリクエストを送信し、レスポンスデータまたはエラーを取得する
                .execute()
                // レスポンスの"data"セクション内のJsonキーの値を指定し、Jsonオブジェクトを抽出する
                .path("bookById")
                // 取得したJsonオブジェクトと比較する
                .matchesJson("""
                        {
                            "id": "book-1",
                            "name": "Effective Java",
                            "pageCount": 416,
                            "author": {
                                "firstName": "Joshua",
                                "lastName": "Block"
                            }
                        }
                        """);
        // レスポンスデータをオブジェクトにしてデータを比較する例
//                .entity(Book.class)
//                .matches(book -> book.id().equals("book-1"));
        // Authorオブジェクトを取りたい場合のPath指定
//                .path("bookById.author")
//                .entity(Author.class)
//                .matches(author -> author.firstName().equals("Joshua"))
//                .matches(author -> author.lastName().equals("Block"));

    }
}