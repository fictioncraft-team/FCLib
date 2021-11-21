package fictioncraft.wintersteve25.fclib.common.helper;

import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.List;

public class CommandsHelper {
    public static List<String> booleanTypes = Lists.newArrayList("true", "false");

    public static SuggestionProvider<CommandSource> booleanSuggestion() {
        return (ctx, sb) -> ISuggestionProvider.suggest(booleanTypes.stream(), sb);
    }

    public static int executeCommand(PlayerEntity playerEntity, String command, boolean asPlayer) {
        World world = playerEntity.getEntityWorld();
        if (world.isRemote()) return 0;
        MinecraftServer server = world.getServer();
        if (server == null) return 0;
        Commands commands = server.getCommandManager();
        return asPlayer ? commands.handleCommand(playerEntity.getCommandSource(), command) : commands.handleCommand(server.getCommandSource(), command);
    }
}
