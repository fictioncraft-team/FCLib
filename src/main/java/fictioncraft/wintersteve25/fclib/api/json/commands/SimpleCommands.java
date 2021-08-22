package fictioncraft.wintersteve25.fclib.api.json.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fictioncraft.wintersteve25.fclib.api.json.ErrorUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class SimpleCommands {
    public static ArgumentBuilder<CommandSource, ?> registerReloadCommand (CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reload").executes((cs) -> reload(cs.getSource()));
    }

    public static int reload(CommandSource source) throws CommandSyntaxException {
        ErrorUtils.handle(source.asPlayer());
        return 1;
    }
}
