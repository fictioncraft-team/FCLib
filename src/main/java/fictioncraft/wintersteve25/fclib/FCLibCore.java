package fictioncraft.wintersteve25.fclib;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FCLibCore.MOD_ID)
public class FCLibCore {
    public static final String MOD_ID = "fclib";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Item.Properties defaultProperty() {
        return new Item.Properties().tab(CreativeModeTab.TAB_MISC);
    } 
    
    public FCLibCore() {
        
    }
}
