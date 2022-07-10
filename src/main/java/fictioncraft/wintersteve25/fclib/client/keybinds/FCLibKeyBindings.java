package fictioncraft.wintersteve25.fclib.client.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class FCLibKeyBindings {
    public static KeyBinding registerKeyBind(String name, int key) {
        KeyBinding keyBinding = new KeyBinding(name, key, "fclib.keybinds.category");
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }
}
