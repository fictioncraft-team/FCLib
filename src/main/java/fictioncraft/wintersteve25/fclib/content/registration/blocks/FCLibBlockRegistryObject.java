package fictioncraft.wintersteve25.fclib.content.registration.blocks;

import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class FCLibBlockRegistryObject<B extends Block, I extends Item> implements ItemLike, IRegistryObjectWrapper<B> {
    private final RegistryObject<B> blockRegistryObject;
    private final RegistryObject<I> itemRegistryObject;
    private final ResourceLocation registryName;

    public FCLibBlockRegistryObject(RegistryObject<B> blockRegistryObject, RegistryObject<I> itemRegistryObject) {
        this.blockRegistryObject = blockRegistryObject;
        this.itemRegistryObject = itemRegistryObject;
        this.registryName = blockRegistryObject.getId();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }
    
    @Override
    public String getName() {
        return getRegistryName().getPath();
    }

    @Override
    public RegistryObject<B> getRegistryObject() {
        return blockRegistryObject;
    }

    public RegistryObject<I> getItemRegistryObject() {
        return itemRegistryObject;
    }

    public B asBlock() {
        return blockRegistryObject.get();
    }

    @Override
    public @NotNull Item asItem() {
        return itemRegistryObject.get();
    }
}
