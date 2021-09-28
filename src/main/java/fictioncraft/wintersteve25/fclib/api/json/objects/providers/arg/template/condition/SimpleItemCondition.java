package fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.condition;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleItemProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;

public class SimpleItemCondition extends SimpleArgProvider {
    private final SimpleItemProvider itemRequired;
    private final JsonUtils.JsonHandTypes handType;

    public SimpleItemCondition(SimpleItemProvider itemRequired, JsonUtils.JsonHandTypes handType, boolean cooldownMatters) {
        super("C_ItemStack");
        this.itemRequired = itemRequired;
        this.handType = handType;
    }

    public SimpleItemProvider getItemRequired() {
        return itemRequired;
    }

    public JsonUtils.JsonHandTypes getHandType() {
        return handType;
    }
}
