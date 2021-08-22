package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

public class SimpleObjProvider {
    private final String name;
    private final boolean isTag;
    private final ProviderType type;

    public SimpleObjProvider(String name, boolean isTag, ProviderType type) {
        this.name = name;
        this.isTag = isTag;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isTag() {
        return isTag;
    }

    /**
     * ProviderType is used by getTagFromJson method in JsonSerializer class
     */
    public ProviderType getType() {
        return type;
    }
}
