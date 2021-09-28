package fictioncraft.wintersteve25.example;

import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;

import java.util.List;

public class SimpleCILConfigObject extends SimpleConfigObject {
    private final List<SimpleArgProvider> conditions;
    private final List<SimpleArgProvider> args;

    public SimpleCILConfigObject(SimpleEntityProvider target, List<SimpleArgProvider> conditions, List<SimpleArgProvider> args) {
        super(target);
        this.conditions = conditions;
        this.args = args;
    }

    public List<SimpleArgProvider> getArgs() {
        return args;
    }

    public List<SimpleArgProvider> getConditions() {
        return conditions;
    }
}
