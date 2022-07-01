package fictioncraft.wintersteve25.fclib.content.base.interfaces.functional;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface IPlacementCondition extends BiPredicate<BlockPlaceContext, BlockState> {
}
