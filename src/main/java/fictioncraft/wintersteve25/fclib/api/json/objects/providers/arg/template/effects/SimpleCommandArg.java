package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleCommandArg extends SimpleArgProvider {

    private final String command;
    private final boolean runAsPlayer;

    public SimpleCommandArg(String command, boolean runAsPlayer) {
        super("E_Commands");
        this.command = command;
        this.runAsPlayer = runAsPlayer;
    }

    public String getCommand() {
        return command;
    }

    public boolean isRunAsPlayer() {
        return runAsPlayer;
    }
}
