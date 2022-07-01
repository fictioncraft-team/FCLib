package fictioncraft.wintersteve25.fclib.content.registration.blocks;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.registration.FCLibDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibBlockDataGenContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class FCLibBlockDeferredRegister extends FCLibDeferredRegister<Block, FCLibBlockRegistryObject<? extends Block, ? extends Item>, FCLibBlockDataGenContext> {

    private final DeferredRegister<Item> itemDeferredRegister;
    private final Item.Properties defaultProperties;

    public FCLibBlockDeferredRegister(String modID) {
        this(modID, FCLibCore.defaultProperty());
    }
    
    public FCLibBlockDeferredRegister(String modID, Item.Properties defaultProperties) {
        super(ForgeRegistries.BLOCKS, modID);
        itemDeferredRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modID);
        this.defaultProperties = defaultProperties;
    }
    
    public <B extends Block> FCLibBlockRegistryObject<B, BlockItem> register(String name, Supplier<B> blockCreator) {
        return register(name, blockCreator, new FCLibBlockDataGenContext());
    }
    
    public <B extends Block> FCLibBlockRegistryObject<B, BlockItem> register(String name, Supplier<B> blockCreator, FCLibBlockDataGenContext dataGenContext) {
        return register(name, blockCreator, (b) -> new BlockItem(b, defaultProperties), dataGenContext);
    }

    public <B extends Block, I extends BlockItem> FCLibBlockRegistryObject<B, I> register(String name, Supplier<B> blockCreator, Function<B, I> itemCreator) {
        return register(name, blockCreator, itemCreator, new FCLibBlockDataGenContext());
    }
    
    public <B extends Block, I extends BlockItem> FCLibBlockRegistryObject<B, I> register(String name, Supplier<B> blockCreator, Function<B, I> itemCreator, FCLibBlockDataGenContext dataGenContext) {
        RegistryObject<B> block = register.register(name, blockCreator);
        RegistryObject<I> item = itemDeferredRegister.register(name, () -> itemCreator.compose((Function<RegistryObject<B>, B>) RegistryObject::get).apply(block));
        FCLibBlockRegistryObject<B, I> registryObject = new FCLibBlockRegistryObject<>(block, item);
        allRegisteredObjects.put(registryObject, dataGenContext);
        return registryObject;
    }

    public void register(IEventBus bus) {
        register.register(bus);
        itemDeferredRegister.register(bus);
    }
}
