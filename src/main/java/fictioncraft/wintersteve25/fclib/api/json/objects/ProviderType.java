package fictioncraft.wintersteve25.fclib.api.json.objects;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function4;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.ISimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.SimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.templates.SimpleBlockProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.templates.SimpleFluidProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.templates.SimpleItemProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.SimpleObjProviderJsonAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class ProviderType {

    /**
     * Default ProviderTypes does not have the serializers because they are implemented in {@link JsonSerializer}
     * commandProcessor are implemented in {@link DumpInfoCommand}
     */
    public static final ProviderType FLUID = new ProviderType("Fluid", null, null, null, SimpleFluidProvider.class);
    public static final ProviderType BLOCK = new ProviderType("Block", null, null, null, SimpleBlockProvider.class);
    public static final ProviderType ITEM = new ProviderType("Item", null, null, null, SimpleItemProvider.class);
    public static final ProviderType ENTITY = new ProviderType("Entity", null, null, null, SimpleEntityProvider.class);
    public static final ProviderType DIMENSION = new ProviderType("Dimensions", null, null, null, SimpleObjProvider.class);
    public static final ProviderType OTHERS = new ProviderType("Others", null, null, null, SimpleObjProvider.class);

    private final String id;
    @Nullable
    private final Function<ResourceLocation, ITag> tagSerializer;
    @Nullable
    private final Predicate<ResourceLocation> isTargetValidSerializer;
    @Nullable
    private final Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor;
    @Nonnull
    private final Class<? extends SimpleObjProvider> providerClass;

    /**
     * @param id Used in dump commands as type
     * @param tagSerializer Used in {@link JsonSerializer#getTagFromJson(String, ProviderType)} to get ITag from a {@link ResourceLocation}
     * @param isTargetValidSerializer Used in {@link JsonSerializer#isValidTarget(ISimpleObjProvider)} to check if a provided target from json is an valid object in registry
     * @param commandProcessor Used in {@link DumpInfoCommand#run(CommandContext)} to process the command and output appropriate info
     * @param providerClass Defines the provider class for given ProviderType. Used in {@link SimpleObjProviderJsonAdapter} for Json Deserialization
     */
    public ProviderType(String id, @Nullable Function<ResourceLocation, ITag> tagSerializer, @Nullable Predicate<ResourceLocation> isTargetValidSerializer, @Nullable Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor, Class<? extends SimpleObjProvider> providerClass) {
        this.id = id;
        this.tagSerializer = tagSerializer;
        this.isTargetValidSerializer = isTargetValidSerializer;
        this.commandProcessor = commandProcessor;
        this.providerClass = providerClass;

        FCLibMod.PROVIDER_TYPES.add(this);
    }

    public String getID() {
        return id;
    }

    @Nullable
    public Function<ResourceLocation, ITag> getTagSerializer() {
        return tagSerializer;
    }

    @Nullable
    public Predicate<ResourceLocation> getIsTargetValidSerializer() {
        return isTargetValidSerializer;
    }

    @Nullable
    public Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> getCommandProcessor() {
        return commandProcessor;
    }

    @Nonnull
    public Class<? extends SimpleObjProvider> getProviderClass() {
        return providerClass;
    }

    public static ProviderType getFromName(String name) {
        for (ProviderType providerType : FCLibMod.PROVIDER_TYPES) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }

    public static void register() {
    }
}