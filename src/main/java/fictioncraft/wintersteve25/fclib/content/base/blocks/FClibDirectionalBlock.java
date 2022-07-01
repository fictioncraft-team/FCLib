package fictioncraft.wintersteve25.fclib.content.base.blocks;

import fictioncraft.wintersteve25.fclib.utils.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FClibDirectionalBlock extends FCLibBlock {

    protected static final DirectionProperty FACING = DirectionalBlock.FACING;
    // for the block builder
    private boolean autoRotateShape = false;
    private boolean allowVertical = false;

    public FClibDirectionalBlock(int harvestLevel, float hardness, float resistance) {
        this(harvestLevel, hardness, resistance, SoundType.STONE, Material.STONE);
    }

    public FClibDirectionalBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material) {
        this(Properties.of(material).strength(hardness, resistance).sound(soundType));
    }

    public FClibDirectionalBlock(Properties properties) {
        super(properties);

        BlockState state = this.getStateDefinition().any();
        state.setValue(FACING, Direction.NORTH);

        this.registerDefaultState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(FACING, mirror.mirror(blockState.getValue(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockItemUseContext) {

        BlockState state = super.getStateForPlacement(blockItemUseContext);

        if (state == null) {
            return null;
        }

        state.setValue(FACING, blockItemUseContext.getNearestLookingDirection());

        return allowVertical ? this.defaultBlockState().setValue(FACING, blockItemUseContext.getNearestLookingDirection()) : this.defaultBlockState().setValue(FACING, blockItemUseContext.getHorizontalDirection());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (autoRotateShape && getHitBox() != null) {
            if (allowVertical) {
                switch (state.getValue(FACING)) {
                    case NORTH:
                        return super.getShape(state, worldIn, pos, context);
                    case EAST:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.CLOCKWISE_90);
                    case SOUTH:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.CLOCKWISE_180);
                    case WEST:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.COUNTERCLOCKWISE_90);
                    case UP:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Direction.UP);
                    case DOWN:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Direction.DOWN);
                }
            } else {
                switch (state.getValue(FACING)) {
                    case NORTH:
                        return super.getShape(state, worldIn, pos, context);
                    case EAST:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.CLOCKWISE_90);
                    case SOUTH:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.CLOCKWISE_180);
                    case WEST:
                        return VoxelShapeUtils.rotate(super.getShape(state, worldIn, pos, context), Rotation.COUNTERCLOCKWISE_90);
                }
            }
        }
        return super.getShape(state, worldIn, pos, context);
    }

    public boolean isAutoRotateShape() {
        return autoRotateShape;
    }

    public void setAutoRotateShape(boolean autoRotateShape) {
        this.autoRotateShape = autoRotateShape;
    }

    public boolean isAllowVertical() {
        return allowVertical;
    }

    public void setAllowVertical(boolean allowVertical) {
        this.allowVertical = allowVertical;
    }
}