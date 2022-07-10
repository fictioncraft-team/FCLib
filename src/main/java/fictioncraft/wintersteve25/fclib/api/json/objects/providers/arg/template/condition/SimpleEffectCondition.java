package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleEffectCondition extends SimpleArgProvider {
    private final String effectName;
    private final int effectLevel;
    private final boolean player;

    public SimpleEffectCondition(String effectName, int effectLevel, boolean player) {
        super("C_Effects");
        this.effectName = effectName;
        this.effectLevel = effectLevel;
        this.player = player;
    }

    public String getEffectName() {
        return effectName;
    }

    public int getEffectLevel() {
        return effectLevel;
    }

    public boolean isPlayer() {
        return player;
    }
}
