package fictioncraft.wintersteve25.fclib.books.presets;

import fictioncraft.wintersteve25.fclib.books.book.Book;

import java.util.ArrayList;

public class TestBooks {
    public static final Book TEST_BOOK;

    static {
        TEST_BOOK = new Book(
                BookStyles.DEFAULT_BOOK_STYLE,
                new ArrayList<>()
        );
    }
}
