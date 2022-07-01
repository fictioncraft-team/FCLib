package fictioncraft.wintersteve25.fclib.content.base.blocks;

import fictioncraft.wintersteve25.fclib.content.base.blocks.bounding.IHasBoundingBlocks;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IForceStoppable;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IRenderTypeProvider;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IVoxelShapeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FCLibBlock extends Block {

    // block builder properties
    private IVoxelShapeProvider hitBox;
    private IRenderTypeProvider renderType;

    public FCLibBlock(int harvestLevel, float hardness, float resistance) {
        this(harvestLevel, hardness, resistance, SoundType.STONE);
    }

    public FCLibBlock(int harvestLevel, float hardness, float resistance, SoundType soundType) {
        this(harvestLevel, hardness, resistance, soundType, Material.STONE);
    }

    public FCLibBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material) {
        this(Properties.of(material).strength(hardness, resistance).sound(soundType));
    }

    public FCLibBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return getHitBox() == null ? super.getShape(state, worldIn, pos, context) : getHitBox().createShape(state, worldIn, pos, context);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return getRenderType() == null ? super.getRenderShape(state) : getRenderType().createRenderType(state);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        FCLibBE tile = (FCLibBE) worldIn.getBlockEntity(pos);

        if (tile == null) {
            return;
        }

        if (tile instanceof IHasBoundingBlocks block) {
            block.onPlace();
        }

        if (tile instanceof IForceStoppable forceStoppable) {
            if (forceStoppable.isInverted()) {
                forceStoppable.setForceStopped(!worldIn.hasNeighborSignal(pos));
            } else {
                forceStoppable.setForceStopped(worldIn.hasNeighborSignal(pos));
            }
        }

        tile.onPlacedBy(worldIn, pos, state, placer, stack);
    }

    public IVoxelShapeProvider getHitBox() {
        return hitBox;
    }

    public void setHitBox(IVoxelShapeProvider hitBox) {
        this.hitBox = hitBox;
    }

    public IRenderTypeProvider getRenderType() {
        return renderType;
    }

    public void setRenderType(IRenderTypeProvider renderType) {
        this.renderType = renderType;
    }
}