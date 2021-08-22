package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Function4;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IExtensibleEnum;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;

import java.util.function.Function;

@SuppressWarnings("all")
public enum ProviderType implements IExtensibleEnum {

    /**
     * Default ProviderTypes does not have the serializers because they are implemented in {@link JsonSerializer}
     * commandProcessor are implemented in {@link fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand}
     */
    FLUID("Fluid", null, null, null),
    BLOCK("Block", null, null, null),
    ITEM("Item", null, null, null),
    ENTITY("Entity", null, null, null);

    private final String id;
    private final Function<ResourceLocation, ITag> tagSerializer;
    private final Function<ResourceLocation, Boolean> isTargetValidSerializer;
    private final Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor;

    /**
     * @param id Used in dump commands as type
     * @param tagSerializer Used in {@link JsonSerializer#getTagFromJson(String, ProviderType)} to get ITag from a {@link ResourceLocation}
     * @param isTargetValidSerializer Used in {@link JsonSerializer#isValidTarget(SimpleObjProvider)} to check if a provided target from json is an valid object in registry
     * @param commandProcessor Used in {@link fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand#run(CommandContext)} to process the command and output appropriate info
     */
    ProviderType(String id, Function<ResourceLocation, ITag> tagSerializer, Function<ResourceLocation, Boolean> isTargetValidSerializer, Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor) {
        this.id = id;
        this.tagSerializer = tagSerializer;
        this.isTargetValidSerializer = isTargetValidSerializer;
        this.commandProcessor = commandProcessor;
    }

    public String getID() {
        return id;
    }

    public Function<ResourceLocation, ITag> getTagSerializer() {
        return tagSerializer;
    }

    public Function<ResourceLocation, Boolean> getIsTargetValidSerializer() {
        return isTargetValidSerializer;
    }

    public Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> getCommandProcessor() {
        return commandProcessor;
    }

    public static ProviderType getFromName(String name) {
        for (ProviderType providerType : ProviderType.values()) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }

    public static ProviderType create(String name, String id, Function<ResourceLocation, ITag> tagSerializer, Function<ResourceLocation, Boolean> isTargetValidSerializer, Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor) {
        throw new IllegalStateException("Enum not extended");
    }
}
