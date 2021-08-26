package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

public class SimpleObjProvider implements IObjProvider {
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
