package link.botwmcs.samchai.ecohelper.impl.essentials;

import link.botwmcs.samchai.ecohelper.EcoHelper;
import net.minecraft.server.level.ServerPlayer;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class YamlReader {
    private static final String path = "plugins/Essentials/userdata/";
    Map<String, Object> properties;

    public YamlReader() {}

    public YamlReader(ServerPlayer player) {
        InputStream inputStream = null;
        String filePath = path + player.getStringUUID() + ".yml";
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            EcoHelper.LOGGER.error("Cannot read Essentials userdata YAML file: ", e);
        }
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(inputStream, Map.class);
    }

    public void initWithString(String content) {
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(content, Map.class);
    }

    public <T> T getValueByKey(String key, T defaultValue) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            Object res = properties.get(key);
            return res == null ? defaultValue : (T) res;
        }
        String finalValue = null;
        Object tempObject = properties;
        for (int i = 0; i < separatorKeys.length; i++) {
            String innerKey = separatorKeys[i];
            Integer index = null;
            if (innerKey.contains("[")) {
                index = Integer.valueOf(Objects.requireNonNull(StringTools.getSubstringBetweenFF(innerKey, "[", "]"))[0]);
                innerKey = innerKey.substring(0, innerKey.indexOf("["));
            }
            Map<String, Object> mapTempObj = (Map<String, Object>) tempObject;
            Object object = mapTempObj.get(innerKey);
            if (object == null) {
                return defaultValue;
            }
            Object targetObj = object;
            if (index != null) {
                targetObj = ((ArrayList) object).get(index);
            }
            tempObject = targetObj;
            if (i == separatorKeys.length - 1) {
                return (T) targetObj;
            }

        }
        return null;
    }

    private static class StringTools {
        public static String[] getSubstringBetweenFF(String str, String start, String end) {
            String[] result = new String[2];
            int startIndex = str.indexOf(start);
            int endIndex = str.indexOf(end);
            if (startIndex == -1 || endIndex == -1) {
                return null;
            }
            result[0] = str.substring(startIndex + 1, endIndex);
            result[1] = str.substring(endIndex + 1);
            return result;
        }
    }
}
