package fictioncraft.wintersteve25.fclib.content.base.items;

import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IPlacementCondition;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IToolTipCondition;
import fictioncraft.wintersteve25.fclib.utils.FCLibConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface ONIIItem {
    default Supplier<ChatFormatting> getColorName() {
        return null;
    }

    default void setColorName(Supplier<ChatFormatting> colorName) {

    }

    default Supplier<List<Component>> getTooltips() {
        return null;
    }

    default void setTooltips(Supplier<List<Component>> tooltips) {

    }

    default Supplier<IToolTipCondition> getTooltipCondition() {
        return IToolTipCondition.DEFAULT;
    }

    default void setTooltipCondition(Supplier<IToolTipCondition> condition) {

    }

    default IPlacementCondition getPlacementCondition() {
        return null;
    }

    default void setPlacementCondition(IPlacementCondition placementCondition) {
    }

    default void tooltip(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (getTooltips() != null && getTooltips().get() != null && !getTooltips().get().isEmpty()) {
            IToolTipCondition condition = getTooltipCondition().get();
            if (condition == null) {
                tooltip.addAll(getTooltips().get());
            } else {
                if (condition == IToolTipCondition.DEFAULT.get()) {
                    if (condition.canShow(stack, worldIn, tooltip, flagIn)) {
                        tooltip.addAll(getTooltips().get());
                    } else {
                        tooltip.add(FCLibConstants.LangKeys.HOLD_SHIFT);
                    }
                } else {
                    if (condition.canShow(stack, worldIn, tooltip, flagIn)) {
                        tooltip.addAll(getTooltips().get());
                    } else {
                        tooltip.add(condition.textWhenNotShown());
                    }
                }
            }
        }
    }
}
