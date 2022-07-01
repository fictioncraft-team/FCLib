package fictioncraft.wintersteve25.fclib.content.base.blocks.bounding;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Should be implemented on a {@link net.minecraft.world.level.block.entity.BlockEntity}
 */
public interface IHasBoundingBlocks {
    void onPlace();

    void onBreak(BlockState oldState);
}

