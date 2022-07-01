package fictioncraft.wintersteve25.fclib.content.registration.blocks;

import fictioncraft.wintersteve25.fclib.content.registration.FCLibDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FCLibBlockEntityDeferredRegister extends FCLibDeferredRegister<BlockEntityType<?>, IRegistryObjectWrapper<?>, Object> {
    public FCLibBlockEntityDeferredRegister(String modid) {
        super(ForgeRegistries.BLOCK_ENTITIES, modid);
        
    }

    public <BE extends BlockEntity, BET extends BlockEntityType<BE>> RegistryObject<BlockEntityType<BE>> registerBE(FCLibBlockRegistryObject<?, ?> block, BlockEntityType.BlockEntitySupplier<BE> factory) {
        return register.register(block.getRegistryName().getPath(), () -> BlockEntityType.Builder.of(factory, block.asBlock()).build(null));
    }
}
