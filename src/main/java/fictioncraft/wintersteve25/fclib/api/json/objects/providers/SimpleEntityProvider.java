package fictioncraft.wintersteve25.fclib.api.json.objects.providers;

public class SimpleEntityProvider extends SimpleObjProvider{
    public SimpleEntityProvider(String name, boolean isTag) {
        super(name, isTag, ProviderType.ENTITY);
    }
}