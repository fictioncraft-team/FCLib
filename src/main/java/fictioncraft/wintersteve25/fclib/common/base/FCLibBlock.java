package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IFCDataGenObject;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class FCLibBlock extends Block implements IFCDataGenObject<Block> {
    private final String regName;

    public FCLibBlock(int harvestLevel, float hardness, float resistance, String regName) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).hardnessAndResistance(hardness, resistance).setRequiresTool());
        this.regName = regName;
    }

    public FCLibBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, String regName) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(soundType).hardnessAndResistance(hardness, resistance).setRequiresTool());
        this.regName = regName;
    }

    public FCLibBlock(int harvestLevel, float hardness, float resistance, SoundType soundType, Material material, String regName) {
        super(Properties.create(material).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE).sound(soundType).hardnessAndResistance(hardness, resistance).setRequiresTool());
        this.regName = regName;
    }

    public FCLibBlock(Properties properties, String regName) {
        super(properties);
        this.regName = regName;
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
