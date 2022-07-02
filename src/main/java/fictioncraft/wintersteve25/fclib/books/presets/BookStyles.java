package fictioncraft.wintersteve25.fclib.books.presets;

import fictioncraft.wintersteve25.fclib.books.book.BookStyle;
import fictioncraft.wintersteve25.fclib.utils.FCLibConstants;
import fictioncraft.wintersteve25.fclib.utils.TextureElement;
import net.minecraft.network.chat.Style;

public class BookStyles {
    public static final BookStyle DEFAULT_BOOK_STYLE;
    
    static {
        DEFAULT_BOOK_STYLE = new BookStyle(
                new TextureElement(2, 230, 17, 17, FCLibConstants.Resources.DEFAULT_BOOK_STYLE),
                new TextureElement(3, 194, 18, 10, FCLibConstants.Resources.DEFAULT_BOOK_STYLE),
                new TextureElement(26, 194, 18, 10, FCLibConstants.Resources.DEFAULT_BOOK_STYLE),
                new TextureElement(20, 1, 146, 180, FCLibConstants.Resources.DEFAULT_BOOK_STYLE),
                Style.EMPTY.withBold(true),
                Style.EMPTY,
                true,
                true
        );
    }
}
