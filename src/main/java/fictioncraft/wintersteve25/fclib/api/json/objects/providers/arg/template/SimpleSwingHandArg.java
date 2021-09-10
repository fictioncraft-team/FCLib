package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;

public class SimpleSwingHandArg extends SimpleArgProvider {
    private final JsonHandTypes hand;

    public SimpleSwingHandArg(JsonHandTypes hand) {
        super("SwingHand");
        this.hand = hand;
    }

    public JsonHandTypes handType() {
        return hand;
    }

    public enum JsonHandTypes {
        MAIN,
        OFF,
        DEFAULT
    }
}
