package fictioncraft.wintersteve25.fclib.api.json;

import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.base.JsonConfig;
import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleConfigObject;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonConfigManager;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Map;

public class ErrorUtils {

    public static final String TARGET_NOT_FOUND = "fclib.reload.failed.targetNotFound";
    public static final String NO_VALID_CONFIG = "fclib.reload.failed.noValidConfigs";
    public static final TranslationTextComponent RELOAD_SUCCESS = new TranslationTextComponent("fclib.reload.success");

    public static void sendError(TranslationTextComponent text, PlayerEntity player) {
        player.sendMessage(text, player.getUniqueID());
    }

    public static void sendError(String text, PlayerEntity player, IJsonConfig config) {
        player.sendMessage(new TranslationTextComponent(text, config.UID()), player.getUniqueID());
    }

    public static void sendError(ErrorTypes text, PlayerEntity player, IJsonConfig config) {
        sendError(text.getText(), player, config);
    }

    /**
     * The default ErrorHandler only work for JsonConfigs made with JsonConfigBuilder
     */
    public static void handle(PlayerEntity player) {
        JsonUtils.createJson();
        JsonUtils.loadJson();

        for (IJsonConfig config : JsonConfigManager.jsonConfigMap.keySet()) {
            if (config instanceof JsonConfig) {
                JsonConfig cfg = (JsonConfig) config;

                if (cfg.configData == null) {
                    sendError(ErrorTypes.NO_VALID_CONFIG, player, cfg);
                    return;
                }

                if (!MiscHelper.isMapValid(cfg.configData.getConfigs())) {
                    sendError(ErrorTypes.NO_VALID_CONFIG, player, cfg);
                    return;
                }

                Map<String, List<SimpleConfigObject>> map = cfg.configData.getConfigs();

                map.values().forEach((list) -> list.forEach((configObject -> {
                    if (!JsonSerializer.isValidTarget(configObject.getTarget()))
                        sendError(new TranslationTextComponent(TARGET_NOT_FOUND, config.UID(), configObject.getTarget().getName()), player);
                })));
            }
        }

        sendError(RELOAD_SUCCESS, player);
    }

    public enum ErrorTypes {
        NO_VALID_CONFIG(ErrorUtils.NO_VALID_CONFIG);

        String text;

        ErrorTypes(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
