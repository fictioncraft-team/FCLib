package fictioncraft.wintersteve25.fclib.content.registration.items;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.books.book.Book;
import fictioncraft.wintersteve25.fclib.books.book.BookItem;
import fictioncraft.wintersteve25.fclib.content.registration.FCLibDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibItemDataGenContext;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class FCLibItemDeferredRegister extends FCLibDeferredRegister<Item, FCLibItemRegistryObject<? extends Item>, FCLibItemDataGenContext> {
    public FCLibItemDeferredRegister(String modid) {
        super(ForgeRegistries.ITEMS, modid);
    }

    public <ITEM extends Item> FCLibItemRegistryObject<ITEM> register(String name, Function<Item.Properties, ITEM> sup) {
        return register(name, () -> sup.apply(FCLibCore.defaultProperty()));
    }

    public <ITEM extends Item> FCLibItemRegistryObject<ITEM> register(String name, Function<Item.Properties, ITEM> sup, FCLibItemDataGenContext registryData) {
        return register(name, () -> sup.apply(FCLibCore.defaultProperty()), registryData);
    }

    public <ITEM extends Item> FCLibItemRegistryObject<ITEM> register(String name, Supplier<? extends ITEM> sup) {
        return register(name, sup, new FCLibItemDataGenContext());
    }

    public FCLibItemRegistryObject<BookItem> registerBook(String name, Book book) {
        return registerBook(name, book, b -> new BookItem(FCLibCore.defaultProperty(), b));
    }
    
    public <ITEM extends BookItem> FCLibItemRegistryObject<ITEM> registerBook(String name, Book book, Function<Book, ITEM> sup) {
        return register(name, () -> sup.apply(book), new FCLibItemDataGenContext());
    }
    
    public <ITEM extends Item> FCLibItemRegistryObject<ITEM> register(String name, Supplier<? extends ITEM> sup, FCLibItemDataGenContext dataGenContext) {
        FCLibItemRegistryObject<ITEM> registeredItem = new FCLibItemRegistryObject<>(register.register(name, sup));
        allRegisteredObjects.put(registeredItem, dataGenContext);
        return registeredItem;
    }
}
