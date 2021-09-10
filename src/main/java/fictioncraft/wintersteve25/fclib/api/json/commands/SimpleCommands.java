package fictioncraft.wintersteve25.fclib.api.json.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fictioncraft.wintersteve25.fclib.FCLibMod;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.ArgProviderType;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class SimpleCommands {
    public static ArgumentBuilder<CommandSource, ?> registerReloadCommand (CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reload").executes((cs) -> reload(cs.getSource()));
    }

    public static ArgumentBuilder<CommandSource, ?> registerArgsCommand (CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("args").executes((cs) -> args(cs.getSource()));
    }

    public static int reload(CommandSource source) throws CommandSyntaxException {
        ErrorUtils.handle(source.asPlayer());
        return 1;
    }

    public static int args(CommandSource source) throws CommandSyntaxException {
        List<ArgProviderType> types = FCLibMod.ARG_PROVIDER_TYPES;
        if (MiscHelper.isListValid(types)) {
            for (ArgProviderType type : types) {
                if (type.canArgumentBeLoaded()) {
                    source.sendFeedback(new TranslationTextComponent(type.getID()), true);
                    return 1;
                }
            }
        }
        return 0;
    }
}
