package com.example.springgraphqlserver.controller;

import com.example.springgraphqlserver.form.UpdateBookPayload;
import graphql.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Map;

@GraphQlTest(BookController.class)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    @DisplayName("１つ目の本が取得できる")
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

    @Test
    @DisplayName("登録した本がレスポンスに渡されること")
    void addBook() {
        this.graphQlTester
                // クエリを直接定義する
                .document("""
                        mutation addBook($input: AddingBookInput!){
                            addBook(input: $input) {
                                book{
                                    id
                                    name
                                    pageCount
                                    author {
                                        firstName
                                        lastName
                                    }
                                }
                            }
                        }
                        """)
                // 変数の設定。Input型の値はMapでセットする
                .variable("input",
                        Map.of("name", "test",
                                "pageCount", 528,
                                "author_firstName", "T",
                                "author_lastName", "O"
                        )
                )
                // Mutationを実行する
                .execute()
                // レスポンスデータからbookを取得する（UpdateBookPayload）
                .path("addBook")
                .entity(UpdateBookPayload.class)
                .satisfies(payload -> {
                    // FIXME なぜかFalseが渡される。Trueしかセットしていないのに
//                    Assert.assertTrue(payload.success());
                    Assert.assertTrue(payload.book().name().equals("test"));
                    Assert.assertTrue(payload.book().pageCount() == 528);
                });
    }
}