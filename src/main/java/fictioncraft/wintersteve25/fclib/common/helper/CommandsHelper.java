package fictioncraft.wintersteve25.fclib.common.helper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import fictioncraft.wintersteve25.fclib.common.base.FCList;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CommandsHelper {
    public static List<String> booleanTypes = new FCList<String>().addContent("false").addContent("true");
    public static Logger LOGGER = LogManager.getLogger();
    public static CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public static SuggestionProvider<CommandSource> booleanSuggestion() {
        return (ctx, sb) -> ISuggestionProvider.suggest(booleanTypes.stream(), sb);
    }

    //Taken from Commands class so I am probably doing something stupid
    public static int executeCommand(CommandSource source, String command, CommandDispatcher<CommandSource> dispatcher) {
        StringReader stringreader = new StringReader(command);
        if (stringreader.canRead() && stringreader.peek() == '/') {
            stringreader.skip();
        }

        source.getServer().getProfiler().startSection(command);

        try {
            try {
                ParseResults<CommandSource> parse = dispatcher.parse(stringreader, source);
                net.minecraftforge.event.CommandEvent event = new net.minecraftforge.event.CommandEvent(parse);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.getException() != null) {
                        com.google.common.base.Throwables.throwIfUnchecked(event.getException());
                    }
                    return 1;
                }
                return dispatcher.execute(event.getParseResults());
            } catch (CommandException commandexception) {
                source.sendErrorMessage(commandexception.getComponent());
                return 0;
            } catch (CommandSyntaxException commandsyntaxexception) {
                source.sendErrorMessage(TextComponentUtils.toTextComponent(commandsyntaxexception.getRawMessage()));
                if (commandsyntaxexception.getInput() != null && commandsyntaxexception.getCursor() >= 0) {
                    int j = Math.min(commandsyntaxexception.getInput().length(), commandsyntaxexception.getCursor());
                    IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent("")).mergeStyle(TextFormatting.GRAY).modifyStyle((p_211705_1_) -> {
                        return p_211705_1_.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
                    });
                    if (j > 10) {
                        iformattabletextcomponent1.appendString("...");
                    }

                    iformattabletextcomponent1.appendString(commandsyntaxexception.getInput().substring(Math.max(0, j - 10), j));
                    if (j < commandsyntaxexception.getInput().length()) {
                        ITextComponent itextcomponent = (new StringTextComponent(commandsyntaxexception.getInput().substring(j))).mergeStyle(new TextFormatting[]{TextFormatting.RED, TextFormatting.UNDERLINE});
                        iformattabletextcomponent1.append(itextcomponent);
                    }

                    iformattabletextcomponent1.append((new TranslationTextComponent("command.context.here")).mergeStyle(new TextFormatting[]{TextFormatting.RED, TextFormatting.ITALIC}));
                    source.sendErrorMessage(iformattabletextcomponent1);
                }
            } catch (Exception exception) {
                IFormattableTextComponent iformattabletextcomponent = new StringTextComponent(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.error("Command exception: {}", command, exception);
                    StackTraceElement[] astacktraceelement = exception.getStackTrace();

                    for(int i = 0; i < Math.min(astacktraceelement.length, 3); ++i) {
                        iformattabletextcomponent.appendString("\n\n").appendString(astacktraceelement[i].getMethodName()).appendString("\n ").appendString(astacktraceelement[i].getFileName()).appendString(":").appendString(String.valueOf(astacktraceelement[i].getLineNumber()));
                    }
                }

                source.sendErrorMessage((new TranslationTextComponent("command.failed")).modifyStyle((p_211704_1_) -> {
                    return p_211704_1_.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, iformattabletextcomponent));
                }));
                if (SharedConstants.developmentMode) {
                    source.sendErrorMessage(new StringTextComponent(Util.getMessage(exception)));
                    LOGGER.error("'" + command + "' threw an exception", (Throwable)exception);
                }

                return 0;
            }

            return 0;
        } finally {
            source.getServer().getProfiler().endSection();
        }
    }

    public static int executeCommand(PlayerEntity playerEntity, String command, boolean asPlayer) {
        World world = playerEntity.getEntityWorld();
        if (!world.isRemote()) return 0;
        MinecraftServer server = world.getServer();
        if (server == null) return 0;
        return asPlayer ? executeCommand(playerEntity.getCommandSource(), command, dispatcher) : executeCommand(server.getCommandSource(), command, dispatcher);
    }
}
