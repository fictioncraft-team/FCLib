package fictioncraft.wintersteve25.fclib.content.base.builders;

import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBlock;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IPlacementCondition;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.functional.IToolTipCondition;
import fictioncraft.wintersteve25.fclib.content.base.items.FCLibItem;
import fictioncraft.wintersteve25.fclib.content.base.items.ONIIItem;
import fictioncraft.wintersteve25.fclib.utils.LangHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FCLibItemBuilder<T extends ONIIItem> {
    private final String regName;
    private final Function<FCLibBlock, T> item;

    private Supplier<IToolTipCondition> toolTipCondition = IToolTipCondition.DEFAULT;
    private Supplier<List<Component>> tooltips;
    private Supplier<ChatFormatting> color;
    private IPlacementCondition placementCondition;
    private boolean takeDurabilityDamage = false;

    private boolean doModelGen = true;
    private boolean doLangGen = true;

    public FCLibItemBuilder(String regName, Function<FCLibBlock, T> item) {
        this.regName = regName;
        this.item = item;
    }

    public FCLibItemBuilder<T> shiftToolTip() {
        toolTipCondition = IToolTipCondition.DEFAULT;
        return this;
    }

    public FCLibItemBuilder<T> tooltipCondition(Supplier<IToolTipCondition> condition) {
        toolTipCondition = condition;
        return this;
    }

    public FCLibItemBuilder<T> tooltip(Component... tooltips) {
        this.tooltips = () -> Arrays.asList(tooltips);
        return this;
    }

    public FCLibItemBuilder<T> defaultTooltip() {
        tooltips = () -> List.of(LangHelper.itemTooltip(regName));
        return this;
    }

    public FCLibItemBuilder<T> coloredName(Supplier<ChatFormatting> color) {
        this.color = color;
        return this;
    }

    public FCLibItemBuilder<T> placementCondition(IPlacementCondition placementCondition) {
        this.placementCondition = placementCondition;
        return this;
    }
    
    public FCLibItemBuilder<T> noModelGen() {
        doModelGen = false;
        return this;
    }

    public FCLibItemBuilder<T> noLangGen() {
        doLangGen = false;
        return this;
    }

    public FCLibItemBuilder<T> takeDurabilityDamage() {
        takeDurabilityDamage = true;
        return this;
    }

    public Function<FCLibBlock, T> build() {
        return (b) -> {
            T i = item.apply(b);
            i.setTooltipCondition(toolTipCondition);
            i.setTooltips(tooltips);
            i.setColorName(color);
            i.setPlacementCondition(placementCondition);
            if (i instanceof FCLibItem i1) {
                i1.setTakeDurabilityDamage(takeDurabilityDamage);
            }
            return i;
        };
    }

    public String getRegName() {
        return regName;
    }

    public boolean isDoModelGen() {
        return doModelGen;
    }

    public boolean isDoLangGen() {
        return doLangGen;
    }
}