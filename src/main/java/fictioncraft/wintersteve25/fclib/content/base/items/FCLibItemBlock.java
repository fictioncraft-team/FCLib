package fictioncraft.wintersteve25.fclib.content.base.items;

import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IPlacementCondition;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IToolTipCondition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class FCLibItemBlock extends BlockItem implements ONIIItem {

    // item builder properties
    private Supplier<ChatFormatting> colorName;
    private Supplier<List<Component>> tooltips;
    private Supplier<IToolTipCondition> tooltipCondition = IToolTipCondition.DEFAULT;
    private IPlacementCondition placementCondition;

    public FCLibItemBlock(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (getColorName() != null && getColorName().get() != null) {
            return super.getName(stack).plainCopy().withStyle(getColorName().get());
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip(stack, worldIn, tooltip, flagIn);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        if (placementCondition != null) {
            return super.canPlace(context, state) && placementCondition.test(context, state);
        }
        return super.canPlace(context, state);
    }

    @Override
    public Supplier<ChatFormatting> getColorName() {
        if (colorName != null) {
            return colorName;
        }

        return ()->ChatFormatting.WHITE;
    }

    @Override
    public void setColorName(Supplier<ChatFormatting> colorName) {
        this.colorName = colorName;
    }

    @Override
    public Supplier<List<Component>> getTooltips() {
        return tooltips;
    }

    @Override
    public void setTooltips(Supplier<List<Component>> tooltips) {
        this.tooltips = tooltips;
    }

    @Override
    public Supplier<IToolTipCondition> getTooltipCondition() {
        return tooltipCondition;
    }

    @Override
    public void setTooltipCondition(Supplier<IToolTipCondition> condition) {
        this.tooltipCondition = condition;
    }

    @Override
    public IPlacementCondition getPlacementCondition() {
        return placementCondition;
    }

    @Override
    public void setPlacementCondition(IPlacementCondition placementCondition) {
        this.placementCondition = placementCondition;
    }
}