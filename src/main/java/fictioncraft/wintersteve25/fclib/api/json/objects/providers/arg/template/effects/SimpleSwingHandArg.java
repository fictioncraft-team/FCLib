package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

public class SimpleSwingHandArg extends SimpleArgProvider {
    private final JsonUtils.JsonHandTypes hand;

    public SimpleSwingHandArg(JsonUtils.JsonHandTypes hand) {
        super("E_SwingHand");
        this.hand = hand;
    }

    public JsonUtils.JsonHandTypes getHandType() {
        return hand;
    }
}
