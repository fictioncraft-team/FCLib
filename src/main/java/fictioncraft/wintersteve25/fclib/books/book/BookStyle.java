package fictioncraft.wintersteve25.fclib.books.book;

import fictioncraft.wintersteve25.fclib.utils.TextureElement;
import net.minecraft.network.chat.Style;

public record BookStyle(
        TextureElement itemSlots,

        TextureElement arrow,
        TextureElement arrowHovered,
        
        TextureElement background,

        Style titleTextStyle,
        Style normalTextStyle,
        
        boolean bookPausesGame,
        boolean drawBG
) {
}
