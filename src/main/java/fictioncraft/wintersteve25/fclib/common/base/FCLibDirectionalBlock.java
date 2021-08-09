package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraftforge.common.ToolType;

@SuppressWarnings("deprecation")
public class FCLibDirectionalBlock extends DirectionalBlock implements IFCDataGenObject<Block> {
    private final String regName;

    public FCLibDirectionalBlock(int harvestLevel, float hardness, float resistance, String regName) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).hardnessAndResistance(hardness, resistance));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
        this.regName = regName;
    }

    public FCLibDirectionalBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, String regName) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(soundType).hardnessAndResistance(hardness, resistance));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
        this.regName = regName;
    }

    public FCLibDirectionalBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material, String regName) {
        super(Properties.create(material).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(soundType).hardnessAndResistance(hardness, resistance));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
        this.regName = regName;
    }

    public FCLibDirectionalBlock(Properties properties, String regName) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
        this.regName = regName;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.with(FACING, rotation.rotate(blockState.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.with(FACING, mirror.mirror(blockState.get(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        return this.getDefaultState().with(FACING, blockItemUseContext.getFace());
    }

    @Override
    public String regName() {
        return regName;
    }

    @Override
    public Block getOg() {
        return this;
    }
}
