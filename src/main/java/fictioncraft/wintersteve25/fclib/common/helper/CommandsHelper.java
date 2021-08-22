package fictioncraft.wintersteve25.fclib.common.helper;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import fictioncraft.wintersteve25.fclib.common.base.FCList;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

import java.util.List;

public class CommandsHelper {
    public static List<String> booleanTypes = new FCList<String>().addContent("false").addContent("true");

    public static SuggestionProvider<CommandSource> booleanSuggestion() {
        return (ctx, sb) -> ISuggestionProvider.suggest(booleanTypes.stream(), sb);
    }
}
