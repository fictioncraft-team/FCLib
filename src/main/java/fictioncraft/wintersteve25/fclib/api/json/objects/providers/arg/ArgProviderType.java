package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import fictioncraft.wintersteve25.fclib.common.helper.TriFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class ArgProviderType {

    private final String id;
    private final String requiredMod;
    private final Class<? extends SimpleArgProvider> argProviderClass;
    private final TriFunction<PlayerEntity, Entity, SimpleArgProvider, Boolean> argExecutor;

    public ArgProviderType(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, TriFunction<PlayerEntity, Entity, SimpleArgProvider, Boolean> argExecutor) {
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

    public TriFunction<PlayerEntity, Entity, SimpleArgProvider, Boolean> getArgExecutor() {
        return argExecutor;
    }

    public boolean isCondition() {
        return isCondition(this);
    }

    public static boolean isCondition(ArgProviderType type) {
       return type.getID().startsWith("C_");
    }

    public boolean canArgumentBeLoaded() {
        return canArgumentBeLoaded(this);
    }

    public static boolean canArgumentBeLoaded(ArgProviderType type) {
        if (!MiscHelper.isStringValid(type.getRequiredMod())) return true;
        return ModListHelper.isModLoaded(type.getRequiredMod());
    }

    public static ArgProviderType registerProvider(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, TriFunction<PlayerEntity, Entity, SimpleArgProvider, Boolean> argExecutor) {
        return new ArgProviderType(id, requiredMod, argProviderClass, argExecutor);
    }

    public static ArgProviderType getFromName(String name) {
        for (ArgProviderType providerType : FCLibMod.ARG_PROVIDER_TYPES) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }
}
