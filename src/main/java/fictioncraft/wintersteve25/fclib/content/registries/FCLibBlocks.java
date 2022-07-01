package fictioncraft.wintersteve25.fclib.content.registries;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.base.blocks.bounding.FCLibBoundingBlock;
import fictioncraft.wintersteve25.fclib.content.base.blocks.bounding.FClibBoundingBE;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockEntityDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockRegistryObject;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibBlockDataGenContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class FCLibBlocks {
    private static final FCLibBlockDeferredRegister BLOCKS = new FCLibBlockDeferredRegister(FCLibCore.MOD_ID);
    private static final FCLibBlockEntityDeferredRegister BLOCK_ENTITIES = new FCLibBlockEntityDeferredRegister(FCLibCore.MOD_ID);

    public static final FCLibBlockRegistryObject<FCLibBoundingBlock, BlockItem> BOUNDING_BLOCK = BLOCKS.register("bounding_block", FCLibBoundingBlock::new, new FCLibBlockDataGenContext(false, false, false, false));
    public static final RegistryObject<BlockEntityType<FClibBoundingBE>> BOUNDING_TE = BLOCK_ENTITIES.registerBE(BOUNDING_BLOCK, FClibBoundingBE::new);
}
