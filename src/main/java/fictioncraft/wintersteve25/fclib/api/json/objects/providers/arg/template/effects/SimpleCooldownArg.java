package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

public class SimpleCooldownArg extends SimpleArgProvider {
    private final int cooldown;
    private final JsonUtils.JsonHandTypes handType;

    public SimpleCooldownArg(int cooldown, JsonUtils.JsonHandTypes handType) {
        super("E_Cooldown");
        this.cooldown = cooldown;
        this.handType = handType;
    }

    public int getCooldown() {
        return cooldown;
    }

    public JsonUtils.JsonHandTypes getHandType() {
        return handType;
    }
}
