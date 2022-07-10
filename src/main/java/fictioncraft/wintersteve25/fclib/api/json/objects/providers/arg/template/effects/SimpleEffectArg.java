package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleEffectArg extends SimpleArgProvider {
    private final String effectName;
    private final int effectDuration;
    private final int effectLevel;
    private final boolean player;

    public SimpleEffectArg(String effectName, int effectDuration, int effectLevel, boolean player) {
        super("E_Effects");
        this.effectName = effectName;
        this.effectDuration = effectDuration;
        this.effectLevel = effectLevel;
        this.player = player;
    }

    public String getEffectName() {
        return effectName;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public boolean isPlayer() {
        return player;
    }
}
