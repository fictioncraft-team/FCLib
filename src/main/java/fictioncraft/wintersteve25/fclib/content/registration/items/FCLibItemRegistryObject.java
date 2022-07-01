package fictioncraft.wintersteve25.fclib.content.registration.items;

import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class FCLibItemRegistryObject<I extends Item> implements ItemLike, IRegistryObjectWrapper<I> {
    private final RegistryObject<I> itemRegistryObject;
    private final ResourceLocation registryName;
    
    public FCLibItemRegistryObject(RegistryObject<I> itemRegistryObject) {
        this.itemRegistryObject = itemRegistryObject;
        this.registryName = itemRegistryObject.getId();
    }

    @Override
    public @NotNull Item asItem() {
        return itemRegistryObject.get();
    }

    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public String getName() {
        return registryName.getPath();
    }

    @Override
    public RegistryObject<I> getRegistryObject() {
        return itemRegistryObject;
    }
}
