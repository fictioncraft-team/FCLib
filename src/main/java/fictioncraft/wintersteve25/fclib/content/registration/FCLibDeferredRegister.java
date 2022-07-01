package fictioncraft.wintersteve25.fclib.content.registration;

import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

public class FCLibDeferredRegister<T extends IForgeRegistryEntry<T>, REG_OBJ extends IRegistryObjectWrapper<?>, DATA_CONTEXT> {
    protected final DeferredRegister<T> register;
    protected final Map<REG_OBJ, DATA_CONTEXT> allRegisteredObjects;
    protected final String registryName;
    
    public FCLibDeferredRegister(IForgeRegistry<T> registry, String modID) {
        this.registryName = registry.getRegistryName().getPath();
        this.register = DeferredRegister.create(registry, modID);
        this.allRegisteredObjects = new HashMap<>();
    }

    public Map<REG_OBJ, DATA_CONTEXT> getAllRegisteredObjects() {
        return allRegisteredObjects;
    }

    public void register(IEventBus bus) {
        register.register(bus);
    }

    public String getRegistryName() {
        return registryName;
    }
}
