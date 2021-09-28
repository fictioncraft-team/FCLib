package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

public class SimpleDamageItemArg extends SimpleArgProvider {
    private final int amount;
    private final JsonUtils.JsonHandTypes handType;

    public SimpleDamageItemArg(int amount, JsonUtils.JsonHandTypes handType) {
        super("E_Damage");
        this.amount = amount;
        this.handType = handType;
    }

    public int getAmount() {
        return amount;
    }

    public JsonUtils.JsonHandTypes getHandType() {
        return handType;
    }
}
