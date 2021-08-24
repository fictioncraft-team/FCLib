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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class ProviderType {

    /**
     * Default ProviderTypes does not have the serializers because they are implemented in {@link JsonSerializer}
     * commandProcessor are implemented in {@link DumpInfoCommand}
     */
    public static final ProviderType FLUID = new ProviderType("Fluid", null, null, null);
    public static final ProviderType BLOCK = new ProviderType("Block", null, null, null);
    public static final ProviderType ITEM = new ProviderType("Item", null, null, null);
    public static final ProviderType ENTITY = new ProviderType("Entity", null, null, null);
    public static final ProviderType DIMENSION = new ProviderType("Dimensions", null, null, null);
    public static final ProviderType OTHERS = new ProviderType("Others", null, null, null);

    public static final List<ProviderType> values = new ArrayList<>();

    private final String id;
    private final Function<ResourceLocation, ITag> tagSerializer;
    private final Predicate<ResourceLocation> isTargetValidSerializer;
    private final Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor;

    /**
     * @param id Used in dump commands as type
     * @param tagSerializer Used in {@link JsonSerializer#getTagFromJson(String, ProviderType)} to get ITag from a {@link ResourceLocation}
     * @param isTargetValidSerializer Used in {@link JsonSerializer#isValidTarget(SimpleObjProvider)} to check if a provided target from json is an valid object in registry
     * @param commandProcessor Used in {@link fictioncraft.wintersteve25.fclib.api.json.commands.DumpInfoCommand#run(CommandContext)} to process the command and output appropriate info
     */
    public ProviderType(String id, Function<ResourceLocation, ITag> tagSerializer, Predicate<ResourceLocation> isTargetValidSerializer, Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> commandProcessor) {
        this.id = id;
        this.tagSerializer = tagSerializer;
        this.isTargetValidSerializer = isTargetValidSerializer;
        this.commandProcessor = commandProcessor;

        values.add(this);
    }

    public String getID() {
        return id;
    }

    public Function<ResourceLocation, ITag> getTagSerializer() {
        return tagSerializer;
    }

    public Predicate<ResourceLocation> getIsTargetValidSerializer() {
        return isTargetValidSerializer;
    }

    public Function4<ServerPlayerEntity, ItemStack, Boolean, Integer, Integer> getCommandProcessor() {
        return commandProcessor;
    }

    public static ProviderType getFromName(String name) {
        for (ProviderType providerType : ProviderType.values) {
            if (providerType.getID().equals(name)) return providerType;
        }

        return null;
    }
}
