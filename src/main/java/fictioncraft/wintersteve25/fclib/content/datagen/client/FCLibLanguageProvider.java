package fictioncraft.wintersteve25.fclib.content.datagen.client;

import fictioncraft.wintersteve25.fclib.content.registration.FCLibDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.blocks.FCLibBlockDeferredRegister;
import fictioncraft.wintersteve25.fclib.content.registration.data.FCLibItemDataGenContext;
import fictioncraft.wintersteve25.fclib.content.registration.data.IRegistryObjectWrapper;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

public class FCLibLanguageProvider extends LanguageProvider {
    private final FCLibDeferredRegister<?, ?, ?> register;
    
    public FCLibLanguageProvider(DataGenerator gen, String modid, FCLibBlockDeferredRegister register) {
        super(gen, modid, "en_us");
        this.register = register;
    }

    @Override
    protected void addTranslations() {
        autoGenLang();
    }

    protected void autoGenLang() {
        var objects = register.getAllRegisteredObjects();
        
        for (IRegistryObjectWrapper<?> b : objects.keySet()) {
            Object data = objects.get(b);
            
            if (data instanceof FCLibItemDataGenContext context) {
                if (context.isDoLangGen()) {
                    add(register.getRegistryName() + "." + modid + "." + b.getName(), WordUtils.capitalizeFully(b.getName().replace("_", " ")));
                }
            }
        }
    }
}