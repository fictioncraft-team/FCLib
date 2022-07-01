package fictioncraft.wintersteve25.fclib.content.registration.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public interface IRegistryObjectWrapper<T extends IForgeRegistryEntry<? super T>> {
    ResourceLocation getRegistryName();
    String getName();
    RegistryObject<T> getRegistryObject();
}
