package fictioncraft.wintersteve25.fclib.common.helper;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;

public class ResourceLocationHelper {
    //taken from BlockStateProvider class
    public static ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public enum RLType {
        TEXTURE,
        TEXTURE_ENTITIES,
        TEXTURE_GUI,
        MODEL,
        STATE,
        ITEM,
        BLOCK,
        GEO_MODEL,
        GEO_ANIMATION,
        SOUND
    }

    public static class ResourceLocationBuilder {
        private final String modID;
        private String pathIn = "";
        private Object[] args;

        public ResourceLocationBuilder(String modID) {
            this.modID = modID;
        }

        public static ResourceLocationBuilder getBuilder(String modID) {
            return new ResourceLocationBuilder(modID);
        }

        public ResourceLocationBuilder template(RLType type) {
            switch (type) {
                case TEXTURE:
                    this.pathIn = "textures/" + pathIn;
                    return this;
                case BLOCK:
                    this.pathIn = "block/" + pathIn;
                    return this;
                case ITEM:
                    this.pathIn = "item/" + pathIn;
                    return this;
                case TEXTURE_ENTITIES:
                    this.pathIn = "entities/" + pathIn;
                    return this;
                case TEXTURE_GUI:
                    this.pathIn = "textures/gui/" + pathIn;
                    return this;
                case MODEL:
                    this.pathIn = "models/" + pathIn;
                    return this;
                case STATE:
                    this.pathIn = "blockstates/" + pathIn;
                    return this;
                case GEO_MODEL:
                    this.pathIn = "geo/" + pathIn;
                    return this;
                case GEO_ANIMATION:
                    this.pathIn = "animations" + pathIn;
                    return this;
                case SOUND:
                    return this;
                default:
                    LogManager.getLogger("FCLibResourceLocationHelper").warn("Tried to get ResourceLocation for {}:{} with unsupported type", modID, pathIn);
                    return this;
            }
        }

        public ResourceLocationBuilder clearPath() {
            this.pathIn = "";
            return this;
        }

        public ResourceLocationBuilder addPath(String pathIn) {
            this.pathIn += pathIn;
            return this;
        }

        public boolean isPathEmpty() {
            return pathIn.isEmpty();
        }

        public ResourceLocationBuilder texture() {
            this.pathIn += "textures/";
            return this;
        }

        public ResourceLocationBuilder entity() {
            this.pathIn += "entities/";
            return this;
        }

        public ResourceLocationBuilder block() {
            this.pathIn += "block/";
            return this;
        }

        public ResourceLocationBuilder item() {
            this.pathIn += "item/";
            return this;
        }

        public ResourceLocationBuilder gui() {
            this.pathIn += "gui/";
            return this;
        }

        public ResourceLocationBuilder model() {
            this.pathIn += "models/";
            return this;
        }

        public ResourceLocationBuilder blockstate() {
            this.pathIn += "blockstates/";
            return this;
        }

        public ResourceLocationBuilder geoModel() {
            this.pathIn += "geo/";
            return this;
        }

        public ResourceLocationBuilder geoAnimations() {
            this.pathIn += "animations/";
            return this;
        }

        public ResourceLocationBuilder misc() {
            this.pathIn += "misc/";
            return this;
        }

        public ResourceLocationBuilder machines() {
            this.pathIn += "machines/";
            return this;
        }

        public ResourceLocationBuilder rocks() {
            this.pathIn += "rocks/";
            return this;
        }

        public ResourceLocationBuilder ingredients() {
            this.pathIn += "ingredients/";
            return this;
        }

        public ResourceLocationBuilder tooltip() {
            this.pathIn += "tooltip/";
            return this;
        }

        public ResourceLocationBuilder messages() {
            this.pathIn += "messages/";
            return this;
        }

        public ResourceLocationBuilder translationTextArg(Object... args) {
            this.args = args;
            return this;
        }

        public ResourceLocationBuilder config() {
            this.pathIn += "config/";
            return this;
        }

        public ResourceLocationBuilder jei() {
            this.pathIn += "jei/";
            return this;
        }

        public ResourceLocationBuilder failed() {
            this.pathIn += "failed/";
            return this;
        }

        public ResourceLocationBuilder error() {
            this.pathIn += "error/";
            return this;
        }

        public ResourceLocation build() {
            if (pathIn.charAt(pathIn.length() - 1) == '/') {
                //remove / if the last character in the path is /
                String fixedPath = pathIn.substring(0, pathIn.length() - 1);
                return new ResourceLocation(modID, fixedPath);
            }
            return new ResourceLocation(modID, pathIn);
        }

        public ResourceLocation buildAsUID() {
            String fixedString = pathIn.replace('/', '_');
            if (fixedString.charAt(fixedString.length() - 1) == '_') {
                String fixed = fixedString.substring(0, fixedString.length() - 1);
                return new ResourceLocation(modID, fixed);
            }
            return new ResourceLocation(modID, fixedString);
        }

        public TranslationTextComponent buildItemGroupTextComponent() {
            String fixedString = "itemGroup." + modID;
            return new TranslationTextComponent(fixedString, args);
        }

        public TranslationTextComponent buildTranslationTextComponent() {
            String fixedString = modID + "." + pathIn.replace('/', '.');
            if (fixedString.charAt(fixedString.length() - 1) == '.') {
                String fixed = fixedString.substring(0, fixedString.length() - 1);
                return new TranslationTextComponent(fixed, args);
            }
            return new TranslationTextComponent(fixedString, args);
        }
    }
}
