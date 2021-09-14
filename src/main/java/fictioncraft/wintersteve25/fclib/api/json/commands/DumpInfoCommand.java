package fictioncraft.wintersteve25.fclib.api.json.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleBlockProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleFluidProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import fictioncraft.wintersteve25.fclib.common.helper.CommandsHelper;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.List;
import java.util.stream.Collectors;

import static fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.ObjProviderType.*;

public class DumpInfoCommand implements Command<CommandSource> {

    private static final DumpInfoCommand INSTANCE = new DumpInfoCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        List<String> providerTypes = FCLibMod.OBJ_PROVIDER_TYPES.stream().map(ObjProviderType::getID).collect(Collectors.toList());

        providerTypes.remove(OTHERS.getID());

        return Commands.literal("info")
                .then(Commands.argument("type", StringArgumentType.string()).suggests((ctx, sb) -> ISuggestionProvider.suggest(providerTypes, sb))
                        .then(Commands.argument("outputTagInstead", StringArgumentType.string()).suggests(CommandsHelper.booleanSuggestion())
                                .then(Commands.argument("tagIndex", IntegerArgumentType.integer())
                                        .executes(INSTANCE))));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {

        ObjProviderType providerType = ObjProviderType.getFromName(StringArgumentType.getString(context, "type"));
        ServerPlayerEntity player = context.getSource().asPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        boolean outputTag = StringArgumentType.getString(context, "outputTagInstead").equals("true");
        int index = IntegerArgumentType.getInteger(context, "tagIndex");

        if (providerType == null) return 0;
        if (ITEM.equals(providerType)) {
            if (!stack.isEmpty()) {
                SimpleItemProvider itemProvider = JsonSerializer.ItemStackSerializer.fromStackToJson(stack, outputTag, index);
                String output = JsonUtils.jsonStringFromObject(itemProvider);
                IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent(output)).mergeStyle(TextFormatting.GRAY).modifyStyle((modifier) -> modifier.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, output))).modifyStyle((modifier) -> modifier.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("fclib.hoverText.clickToCopy"))));
                context.getSource().sendFeedback(iformattabletextcomponent1, false);

                FCLibMod.logger.info(output);
                return 1;
            }
            return 0;
        } else if (BLOCK.equals(providerType)) {
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BlockItem) {
                    BlockItem item = (BlockItem) stack.getItem();
                    Block block = item.getBlock();

                    SimpleBlockProvider blockProvider = JsonSerializer.BlockSerializer.fromBlockToJson(stack, block.getDefaultState(), outputTag, index);
                    String outputBlock = JsonUtils.jsonStringFromObject(blockProvider);
                    IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent(outputBlock)).mergeStyle(TextFormatting.GRAY).modifyStyle((modifier) -> modifier.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, outputBlock))).modifyStyle((modifier) -> modifier.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("fclib.hoverText.clickToCopy"))));
                    context.getSource().sendFeedback(iformattabletextcomponent1, false);

                    FCLibMod.logger.info(outputBlock);
                    return 1;
                }
            }
            return 0;
        } else if (FLUID.equals(providerType)) {
            if (!stack.isEmpty()) {
                if (FluidUtil.getFluidHandler(stack).isPresent()) {
                    if (FluidUtil.getFluidContained(stack).isPresent()) {
                        FluidStack fluid = FluidUtil.getFluidContained(stack).get();

                        SimpleFluidProvider fluidProvider = JsonSerializer.FluidSerializer.fromFluidToJson(fluid, outputTag, index);
                        String outputFluid = JsonUtils.jsonStringFromObject(fluidProvider);
                        IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent(outputFluid)).mergeStyle(TextFormatting.GRAY).modifyStyle((modifier) -> modifier.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, outputFluid))).modifyStyle((modifier) -> modifier.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("fclib.hoverText.clickToCopy"))));
                        context.getSource().sendFeedback(iformattabletextcomponent1, false);
                        FCLibMod.logger.info(outputFluid);
                        return 1;
                    }
                }
            }
            return 0;
        } else if (ENTITY.equals(providerType)) {
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof SpawnEggItem) {
                    SpawnEggItem item = (SpawnEggItem) stack.getItem();

                    SimpleEntityProvider entityProvider = JsonSerializer.EntitySerialization.fromEntityToJson(item.getType(null), outputTag, index);
                    String outputEntity = JsonUtils.jsonStringFromObject(entityProvider);
                    IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent(outputEntity)).mergeStyle(TextFormatting.GRAY).modifyStyle((modifier) -> modifier.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, outputEntity))).modifyStyle((modifier) -> modifier.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("fclib.hoverText.clickToCopy"))));
                    context.getSource().sendFeedback(iformattabletextcomponent1, false);
                    FCLibMod.logger.info(outputEntity);
                    return 1;
                }
            }
            return 0;
        } else if (DIMENSION.equals(providerType)) {
            context.getSource().sendFeedback(new TranslationTextComponent("fclib.reload.useForgeDimension"), false);
            return 1;
        } else {
            if (providerType.getCommandProcessor() == null) {
                context.getSource().sendFeedback(new TranslationTextComponent("fclib.reload.notSupported"), false);
                return 0;
            }
            return providerType.getCommandProcessor().apply(player, stack, outputTag, index);
        }
    }
}