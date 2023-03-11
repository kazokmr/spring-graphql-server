package com.example.springgraphqlserver.presentation.controller;

import com.example.springgraphqlserver.domain.entities.Author;
import com.example.springgraphqlserver.presentation.form.UpdateBookPayload;
import org.assertj.core.api.SoftAssertions;
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
                // クエリを直接定義する。評価するFieldsを指定すること
                .document("""
                        mutation addBook($input: AddingBookInput!){
                            addBook(input: $input) {
                                success
                                book{
                                    id
                                    name
                                    pageCount
                                    author {
                                        id
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
                // レスポンスデータをObjectに変換する
                .path("addBook")
                .entity(UpdateBookPayload.class)
                .satisfies(payload -> {
                    SoftAssertions assertions = new SoftAssertions();
                    assertions.assertThat(payload.success()).as("成功したのでtrueが返る").isTrue();
                    assertions.assertThat(payload.book().name()).as("登録した書籍名").isEqualTo("test");
                    assertions.assertThat(payload.book().pageCount()).as("書籍のページ数").isEqualTo(528);
                    assertions.assertAll();
                })
                .path("addBook.book.author")
                .entity(Author.class)
                .satisfies(author -> {
                    SoftAssertions assertions = new SoftAssertions();
                    assertions.assertThat(author.firstName()).isEqualTo("T");
                    assertions.assertThat(author.lastName()).isEqualTo("O");
                    assertions.assertAll();
                });
//                .matchesJsonStrictly("""
//                        {
//                            "success": true,
//                            "book": {
//                                "id": "book-4",
//                                "name": "test",
//                                "pageCount": 528,
//                                "author": {
//                                    "id": "author-4",
//                                    "firstName": "T",
//                                    "lastName": "O"
//                                }
//                            }
//                        }
//                        """);
    }
}