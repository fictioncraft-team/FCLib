package fictioncraft.wintersteve25.fclib.api.json.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.*;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DumpInfoCommand implements Command<CommandSource> {

    private static final DumpInfoCommand INSTANCE = new DumpInfoCommand();
    private static ProviderType providerType = ProviderType.ITEM;

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        List<String> providerTypes = Arrays.stream(ProviderType.values()).map(ProviderType::getID).collect(Collectors.toList());

        return Commands.literal("info")
                .then(Commands.argument("outputTagInstead", StringArgumentType.string()).suggests(CommandsHelper.booleanSuggestion())
                        .then(Commands.argument("tagIndex", IntegerArgumentType.integer())
                                .then(Commands.argument("type", StringArgumentType.string()).suggests((ctx, sb) -> ISuggestionProvider.suggest(providerTypes, sb))
                                        .executes(INSTANCE))));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {

        providerType = ProviderType.getFromName(StringArgumentType.getString(context, "type"));
        ServerPlayerEntity player = context.getSource().asPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        boolean outputTag = StringArgumentType.getString(context, "outputTagInstead").equals("true");
        int index = IntegerArgumentType.getInteger(context, "tagIndex");

        if (!stack.isEmpty()) {
            switch (providerType) {
                case ITEM:
                    SimpleItemProvider itemProvider = JsonSerializer.ItemStackSerializer.fromStackToJson(stack, outputTag, index);
                    String output = JsonUtils.jsonStringFromObject(itemProvider);
                    context.getSource().sendFeedback(new TranslationTextComponent(output), false);

                    FCLibMod.logger.info(output);
                    return 1;
                case BLOCK:
                    if (stack.getItem() instanceof BlockItem) {
                        BlockItem item = (BlockItem) stack.getItem();
                        Block block = item.getBlock();

                        SimpleBlockProvider blockProvider = JsonSerializer.BlockSerializer.fromBlockToJson(stack, block.getDefaultState(), outputTag, index);
                        String outputBlock = JsonUtils.jsonStringFromObject(blockProvider);
                        context.getSource().sendFeedback(new TranslationTextComponent(outputBlock), false);

                        FCLibMod.logger.info(outputBlock);
                        return 1;
                    }
                case FLUID:
                    if (FluidUtil.getFluidHandler(stack).isPresent()) {
                        if (FluidUtil.getFluidContained(stack).isPresent()) {
                            FluidStack fluid = FluidUtil.getFluidContained(stack).get();

                            SimpleFluidProvider fluidProvider = JsonSerializer.FluidSerializer.fromFluidToJson(fluid, outputTag, index);
                            String outputFluid = JsonUtils.jsonStringFromObject(fluidProvider);
                            context.getSource().sendFeedback(new TranslationTextComponent(outputFluid), false);

                            FCLibMod.logger.info(outputFluid);
                            return 1;
                        }
                    }
                case ENTITY:
                    if (stack.getItem() instanceof SpawnEggItem) {
                        SpawnEggItem item = (SpawnEggItem) stack.getItem();

                        SimpleEntityProvider entityProvider = JsonSerializer.EntitySerialization.fromEntityToJson(item.getType(null), outputTag, index);
                        String outputEntity = JsonUtils.jsonStringFromObject(entityProvider);
                        context.getSource().sendFeedback(new TranslationTextComponent(outputEntity), false);

                        FCLibMod.logger.info(outputEntity);
                        return 1;
                    }
                default:
                    if (providerType.getCommandProcessor() == null) return 0;
                    return providerType.getCommandProcessor().apply(player, stack, outputTag, index);
            }
        }
        return 0;
    }
}
