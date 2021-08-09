package fictioncraft.wintersteve25.fclib.common.base;

import fictioncraft.wintersteve25.fclib.common.interfaces.IColoredName;
import fictioncraft.wintersteve25.fclib.common.interfaces.IHasToolTip;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FCLibBlockItem extends BlockItem {

    public FCLibBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        if (this instanceof IColoredName) {
            IColoredName coloredName = (IColoredName) this;
            return super.getDisplayName(stack).copyRaw().mergeStyle(coloredName.color());
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this instanceof IHasToolTip) {
            if (Screen.hasShiftDown()) {
                IHasToolTip toolTipBlock = (IHasToolTip) this;
                tooltip.addAll(toolTipBlock.tooltip());
            } else {
                tooltip.add(new TranslationTextComponent("fclib.shiftInfo"));
            }
        }
    }
}
