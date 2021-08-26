package fictioncraft.wintersteve25.fclib.api.json;

import fictioncraft.wintersteve25.fclib.api.json.objects.SimpleObjectMap;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import fictioncraft.wintersteve25.fclib.api.json.base.IJsonConfig;
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

    public static void handle(PlayerEntity player) {
        JsonUtils.createJson();
        JsonUtils.loadJson();

        for (IJsonConfig config : JsonConfigManager.jsonConfigMap.keySet()) {
            if (config != null && config.finishedConfig() != null) {
                SimpleObjectMap cfg = config.finishedConfig();

                if (cfg == null) {
                    sendError(ErrorTypes.NO_VALID_CONFIG, player, config);
                    return;
                }

                if (!MiscHelper.isMapValid(cfg.getConfigs())) {
                    sendError(ErrorTypes.NO_VALID_CONFIG, player, config);
                    return;
                }

                Map<String, List<SimpleConfigObject>> map = cfg.getConfigs();

                map.values().forEach((list) -> list.forEach((configObject -> {
                    if (!JsonSerializer.isValidTarget(configObject.getTarget()))
                        sendError(new TranslationTextComponent(TARGET_NOT_FOUND, config.UID(), configObject.getTarget().getName()), player);
                })));
            } else {
                sendError(ErrorTypes.NO_VALID_CONFIG, player, config);
                return;
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
