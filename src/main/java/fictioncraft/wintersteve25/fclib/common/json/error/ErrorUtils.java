package fictioncraft.wintersteve25.fclib.common.json.error;

import fictioncraft.wintersteve25.fclib.common.json.base.IJsonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class ErrorUtils {

    public static final String TARGET_NOT_FOUND = "fclib.reload.failed.targetNotFound";
    public static final String FILE_NOT_FOUND = "fclib.reload.failed.fileNotFound";
    public static final TranslationTextComponent RELOAD_SUCCESS = new TranslationTextComponent("fclib.reload.success");

    public static void sendError(String text, PlayerEntity player, IJsonConfig config) {
        player.sendMessage(new TranslationTextComponent(text, config.UID()), player.getUniqueID());
    }

    public static void sendError(ErrorTypes text, PlayerEntity player, IJsonConfig config) {
        sendError(text.getText(), player, config);
    }

    public enum ErrorTypes {
        TARGET_NOT_FOUND(ErrorUtils.TARGET_NOT_FOUND),
        FILE_NOT_FOUND(ErrorUtils.FILE_NOT_FOUND);

        String text;

        ErrorTypes(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
