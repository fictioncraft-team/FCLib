package fictioncraft.wintersteve25.fclib.utils;

import net.minecraft.network.chat.TranslatableComponent;

public final class FCLibConstants {
    public static final class PacketType {
        public static final byte REDSTONE_INPUT = 0;
        public static final byte REDSTONE_OUTPUT_LOW = 1;
        public static final byte REDSTONE_OUTPUT_HIGH = 2;
    }

    public static final class LangKeys {
        public static final TranslatableComponent HOLD_SHIFT = new TranslatableComponent("oniutils.tooltips.items.holdShiftInfo");
    }
}
