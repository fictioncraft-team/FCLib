package fictioncraft.wintersteve25.fclib.content.registries;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.books.book.BookItem;
import fictioncraft.wintersteve25.fclib.books.presets.TestBooks;
import fictioncraft.wintersteve25.fclib.content.registration.items.FCLibItemDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.items.FCLibItemRegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

public class FCLibItems {
    private static final FCLibItemDeferredRegister ITEMS = new FCLibItemDeferredRegister(FCLibCore.MOD_ID);
    public static final FCLibItemRegistryObject<BookItem> BOOK_ITEM = ITEMS.register("book_item", pProperties -> new BookItem(pProperties, TestBooks.TEST_BOOK));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}