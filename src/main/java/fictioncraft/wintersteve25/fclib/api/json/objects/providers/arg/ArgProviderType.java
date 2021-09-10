package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.*;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ArgProviderType {

    public static final ArgProviderType COMMAND = new ArgProviderType("Commands", "", SimpleCommandArg.class, null);
    public static final ArgProviderType EFFECT = new ArgProviderType("Effects", "", SimpleEffectArg.class, null);
    public static final ArgProviderType SWING_HAND = new ArgProviderType("SwingHand", "", SimpleSwingHandArg.class, null);
    public static final ArgProviderType KILL = new ArgProviderType("Kill", "", SimpleKillArg.class, null);
    public static final ArgProviderType TRANSFORM = new ArgProviderType("Transform", "", SimpleTransformArg.class, null);

    private final String id;
    private final String requiredMod;
    private final Class<? extends SimpleArgProvider> argProviderClass;
    private final BiConsumer<PlayerEntity, SimpleArgProvider> argExecutor;

    public ArgProviderType(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, BiConsumer<PlayerEntity, SimpleArgProvider> argExecutor) {
        this.id = id;
        this.requiredMod = requiredMod;
        this.argProviderClass = argProviderClass;
        this.argExecutor = argExecutor;

        FCLibMod.ARG_PROVIDER_TYPES.add(this);
    }

    public String getID() {
        return id;
    }

    public String getRequiredMod() {
        return requiredMod;
    }

    public Class<? extends SimpleArgProvider> getArgProviderClass() {
        return argProviderClass;
    }

    public BiConsumer<PlayerEntity, SimpleArgProvider> getArgExecutor() {
        return argExecutor;
    }

    public boolean canArgumentBeLoaded() {
        return canArgumentBeLoaded(this);
    }

    public static boolean canArgumentBeLoaded(ArgProviderType type) {
        if (!MiscHelper.isStringValid(type.getRequiredMod())) return true;
        return ModListHelper.isModLoaded(type.getRequiredMod());
    }

    public static ArgProviderType registerProvider(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, BiConsumer<PlayerEntity, SimpleArgProvider> argExecutor) {
        return new ArgProviderType(id, requiredMod, argProviderClass, argExecutor);
    }

    public static ArgProviderType getFromName(String name) {
        for (ArgProviderType providerType : FCLibMod.ARG_PROVIDER_TYPES) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }
}
