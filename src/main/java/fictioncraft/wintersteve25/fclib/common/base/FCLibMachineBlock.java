package fictioncraft.wintersteve25.fclib.common.base;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class FCLibMachineBlock extends FCLibDirectionalBlock {

    public FCLibMachineBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material, String regName) {
        super(harvestLevel, hardness, resistance, soundType, material, regName);
    }

    public FCLibMachineBlock(AbstractBlock.Properties properties, String regName) {
        super(properties, regName);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
}
