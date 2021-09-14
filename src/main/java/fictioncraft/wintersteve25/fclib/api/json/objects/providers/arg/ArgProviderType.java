package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg;

import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleEffectCondition;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleExperienceCondition;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleHungerCondition;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition.SimpleItemCondition;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.*;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BiFunction;

public class ArgProviderType {

    public static final ArgProviderType E_COMMAND = new ArgProviderType("E_Commands", "", SimpleCommandArg.class, null);
    public static final ArgProviderType E_EFFECT = new ArgProviderType("E_Effects", "", SimpleEffectArg.class, null);
    public static final ArgProviderType E_SWING_HAND = new ArgProviderType("E_SwingHand", "", SimpleSwingHandArg.class, null);
    public static final ArgProviderType E_KILL = new ArgProviderType("E_Kill", "", SimpleKillArg.class, null);
    public static final ArgProviderType E_TRANSFORM = new ArgProviderType("E_Transform", "", SimpleTransformArg.class, null);
    public static final ArgProviderType E_XP = new ArgProviderType("E_XP", "", SimpleExperienceArg.class, null);
    public static final ArgProviderType E_HUNGER = new ArgProviderType("E_Hunger", "", SimpleHungerArg.class, null);
    public static final ArgProviderType E_SHRINK = new ArgProviderType("E_Shrink", "", SimpleShrinkArg.class, null);
    public static final ArgProviderType E_PARTICLE = new ArgProviderType("E_Particle", "", SimpleParticleArg.class, null);
    public static final ArgProviderType E_SUMMON = new ArgProviderType("E_Summon", "", SimpleSummonArg.class, null);
    public static final ArgProviderType E_Sound = new ArgProviderType("E_Sound", "", SimpleSoundArg.class, null);

    public static final ArgProviderType C_ITEMSTACK = new ArgProviderType("C_ItemStack", "", SimpleItemCondition.class, null);
    public static final ArgProviderType C_XP = new ArgProviderType("C_XP", "", SimpleExperienceCondition.class, null);
    public static final ArgProviderType C_HUNGER = new ArgProviderType("C_Hunger", "", SimpleHungerCondition.class, null);
    public static final ArgProviderType C_EFFECT = new ArgProviderType("C_Effects", "", SimpleEffectCondition.class, null);

    private final String id;
    private final String requiredMod;
    private final Class<? extends SimpleArgProvider> argProviderClass;
    private final BiFunction<PlayerEntity, SimpleArgProvider, Boolean> argExecutor;

    public ArgProviderType(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, BiFunction<PlayerEntity, SimpleArgProvider, Boolean> argExecutor) {
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

    public BiFunction<PlayerEntity, SimpleArgProvider, Boolean> getArgExecutor() {
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

    public static ArgProviderType registerProvider(String id, String requiredMod, Class<? extends SimpleArgProvider> argProviderClass, BiFunction<PlayerEntity, SimpleArgProvider, Boolean> argExecutor) {
        return new ArgProviderType(id, requiredMod, argProviderClass, argExecutor);
    }

    public static ArgProviderType getFromName(String name) {
        for (ArgProviderType providerType : FCLibMod.ARG_PROVIDER_TYPES) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }
}
