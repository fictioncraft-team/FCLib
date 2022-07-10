package fictioncraft.wintersteve25.fclib.api.json.objects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;

public class SimpleConfigObject {
    private final SimpleObjProvider target;

    public SimpleConfigObject(SimpleObjProvider target) {
        this.target = target;
    }

    public SimpleObjProvider getTarget() {
        return target;
    }
}
