package fictioncraft.wintersteve25.fclib.api.json.objects;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.IObjProvider;

public class SimpleConfigObject {
    private final IObjProvider target;

    public SimpleConfigObject(IObjProvider target) {
        this.target = target;
    }

    public IObjProvider getTarget() {
        return target;
    }
}
