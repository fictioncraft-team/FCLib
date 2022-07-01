package fictioncraft.wintersteve25.fclib.content.base.interfaces.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@FunctionalInterface
public interface IVoxelShapeProvider {
    VoxelShape createShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context);
}
