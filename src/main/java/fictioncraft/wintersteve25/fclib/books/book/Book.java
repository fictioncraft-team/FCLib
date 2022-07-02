package fictioncraft.wintersteve25.fclib.books.book;

import fictioncraft.wintersteve25.fclib.books.entries.BookEntry;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

public class Book extends ForgeRegistryEntry<Book> {
    private final BookStyle style;
    private final List<BookEntry> entries;

    public Book(BookStyle style, List<BookEntry> entries) {
        this.style = style;
        this.entries = entries;
    }
    
    public BookStyle getStyle() {
        return style;
    }

    public List<BookEntry> getEntries() {
        return entries;
    }
}