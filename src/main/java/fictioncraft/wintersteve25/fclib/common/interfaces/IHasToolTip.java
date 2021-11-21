package fictioncraft.wintersteve25.fclib.common.interfaces;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Should be implemented in a {@link net.minecraft.item.Item}
 */
public interface IHasToolTip {
    List<ITextComponent> tooltip();
}
