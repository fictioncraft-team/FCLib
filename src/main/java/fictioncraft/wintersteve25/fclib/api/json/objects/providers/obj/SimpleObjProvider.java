package fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj;

import com.google.gson.annotations.JsonAdapter;

@JsonAdapter(SimpleObjProviderJsonAdapter.class)
public class SimpleObjProvider implements ISimpleObjProvider {
    private final String name;
    private final boolean isTag;
    private final String type;

    public SimpleObjProvider(String name, boolean isTag, String type) {
        this.name = name;
        this.isTag = isTag;
        this.type = type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTag() {
        return this.isTag;
    }

    @Override
    public ObjProviderType getType() {
        return ObjProviderType.getFromName(this.type);
    }
}
