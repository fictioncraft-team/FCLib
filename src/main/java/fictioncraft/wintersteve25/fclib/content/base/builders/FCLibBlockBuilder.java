package fictioncraft.wintersteve25.fclib.content.base.builders;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBlock;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FClibDirectionalBlock;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibMachineBlock;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IHasGui;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IPlacementCondition;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IRenderTypeProvider;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IToolTipCondition;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IVoxelShapeProvider;
import fictioncraft.wintersteve25.fclib.content.base.items.FCLibItemBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Function;
import java.util.function.Supplier;

public class FCLibBlockBuilder<T extends FCLibBlock> {

    private final String regName;
    private final Supplier<T> block;
    private final FCLibItemBuilder<FCLibItemBlock> blockItem;

    private IVoxelShapeProvider hitBox;
    private boolean allowVertical;
    private boolean allowRotateShape;
    private IRenderTypeProvider renderType;
    private IHasGui container;
    private boolean doStateGen = false;
    private boolean doModelGen = true;
    private boolean doLangGen = true;
    private boolean doLootableGen = true;
    
    public FCLibBlockBuilder(String regName, Supplier<T> block) {
        this(regName, block, FCLibCore.defaultProperty());
    }

    public FCLibBlockBuilder(String regName, Supplier<T> block, Item.Properties properties) {
        this.block = block;
        this.regName = regName;
        this.blockItem = new FCLibItemBuilder<>(this.regName, (b) -> new FCLibItemBlock(b, properties));
    }

    public FCLibBlockBuilder<T> placementCondition(IPlacementCondition condition) {
        this.blockItem.placementCondition(condition);
        return this;
    }

    public FCLibBlockBuilder<T> shiftToolTip() {
        this.blockItem.shiftToolTip();
        return this;
    }

    public FCLibBlockBuilder<T> tooltipCondition(Supplier<IToolTipCondition> condition) {
        this.blockItem.tooltipCondition(condition);
        return this;
    }

    public FCLibBlockBuilder<T> tooltip(Component... tooltips) {
        this.blockItem.tooltip(tooltips);
        return this;
    }

    public FCLibBlockBuilder<T> coloredName(Supplier<ChatFormatting> color) {
        this.blockItem.coloredName(color);
        return this;
    }

    public FCLibBlockBuilder<T> shape(IVoxelShapeProvider voxelShape) {
        this.hitBox = voxelShape;
        return this;
    }

    public FCLibBlockBuilder<T> allowVerticalPlacement() {
        this.allowVertical = true;
        return this;
    }

    public FCLibBlockBuilder<T> autoRotateShape() {
        this.allowRotateShape = true;
        return this;
    }

    public FCLibBlockBuilder<T> renderType(IRenderTypeProvider renderType) {
        this.renderType = renderType;
        return this;
    }

    public FCLibBlockBuilder<T> container(IHasGui gui) {
        container = gui;
        return this;
    }

    public FCLibBlockBuilder<T> doStateGen() {
        doStateGen = true;
        return this;
    }

    public FCLibBlockBuilder<T> noModelGen() {
        doModelGen = false;
        blockItem.noModelGen();
        return this;
    }

    public FCLibBlockBuilder<T> noLangGen() {
        doLangGen = false;
        blockItem.noLangGen();
        return this;
    }

    public FCLibBlockBuilder<T> noLootableGen() {
        doLootableGen = false;
        return this;
    }

    public Tuple<Lazy<T>, Function<FCLibBlock, FCLibItemBlock>> build() {
        return new Tuple<>(Lazy.of(() -> {
            T b = block.get();
            b.setHitBox(hitBox);
            ((FClibDirectionalBlock) b).setAllowVertical(allowVertical);
            ((FClibDirectionalBlock) b).setAutoRotateShape(allowRotateShape);
            b.setRenderType(renderType);
            ((FCLibMachineBlock<?>) b).setGui(container);
            return b;
        }), this.blockItem.build());
    }

    public String getRegName() {
        return regName;
    }

    public boolean isDoStateGen() {
        return doStateGen;
    }

    public boolean isDoModelGen() {
        return doModelGen;
    }

    public boolean isDoLangGen() {
        return doLangGen;
    }

    public boolean isDoLootableGen() {
        return doLootableGen;
    }
}