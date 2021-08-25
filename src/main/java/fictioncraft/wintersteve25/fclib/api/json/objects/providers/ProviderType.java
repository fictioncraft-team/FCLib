package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function4;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IExtensibleEnum;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public enum ProviderType {

    /**
     * Default ProviderTypes does not have the serializers because they are implemented in {@link JsonSerializer}
     * commandProcessor are implemented in {@link DumpInfoCommand}
     */
    FLUID("Fluid", null, null, null),
    BLOCK("Block", null, null, null),
    ITEM("Item", null, null, null),
    ENTITY("Entity", null, null, null),
    DIMENSION("Dimensions", null, null, null),
    OTHERS("Others", null, null, null);

    private final String id;
    @Nullable
    private final Function<ResourceLocation, ITag> tagSerializer;
    @Nullable
    private final Predicate<ResourceLocation> isTargetValidSerializer;
    @Nullable
    private final Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor;

    /**
     * @param id Used in dump commands as type
     * @param tagSerializer Used in {@link JsonSerializer#getTagFromJson(String, ProviderType)} to get ITag from a {@link ResourceLocation}
     * @param isTargetValidSerializer Used in {@link JsonSerializer#isValidTarget(SimpleObjProvider)} to check if a provided target from json is an valid object in registry
     * @param commandProcessor Used in {@link fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand#run(CommandContext)} to process the command and output appropriate info
     */
    ProviderType(String id, @Nullable Function<ResourceLocation, ITag> tagSerializer, @Nullable Predicate<ResourceLocation> isTargetValidSerializer, @Nullable Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor) {
        this.id = id;
        this.tagSerializer = tagSerializer;
        this.isTargetValidSerializer = isTargetValidSerializer;
        this.commandProcessor = commandProcessor;
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

    public static ProviderType getFromName(String name) {
        for (ProviderType providerType : ProviderType.values()) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }
}
