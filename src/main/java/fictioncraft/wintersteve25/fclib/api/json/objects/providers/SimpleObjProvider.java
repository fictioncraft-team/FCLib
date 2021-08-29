package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

import com.google.gson.annotations.JsonAdapter;
import fictioncraft.wintersteve25.fclib.api.json.objects.ProviderType;

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
    public ProviderType getType() {
        return ProviderType.getFromName(this.type);
    }
}
