package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

public class SimpleCooldownCondition extends SimpleArgProvider {
    private final JsonUtils.JsonHandTypes handType;

    public SimpleCooldownCondition(JsonUtils.JsonHandTypes handType) {
        super("C_Cooldown");
        this.handType = handType;
    }

    public JsonUtils.JsonHandTypes getHandType() {
        return handType;
    }
}
