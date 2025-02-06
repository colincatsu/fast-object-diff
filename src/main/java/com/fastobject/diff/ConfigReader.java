package com.fastobject.diff;

/**
 * Created by colinsu
 *
 * @date 2025/2/6.
 */

import com.fastobject.diff.DiffConfig.DiffLogKeyConfig;
import com.fastobject.diff.DiffConfig.FieldConfig;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfigReader {
    public static DiffConfig readConfig(String filePath) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(filePath);
        Map<String, Object> yamlData = yaml.load(inputStream);
        return parseYaml(yamlData);
    }

    private static DiffConfig parseYaml(Map<String, Object> yamlData) {
        DiffConfig classConfigs = new DiffConfig();
        List<Map<String, Object>> classes = (List<Map<String, Object>>) yamlData.get("classes");

        for (Map<String, Object> classMap : classes) {
            String className = (String) classMap.get("className");
            List<Map<String, Object>> fields = (List<Map<String, Object>>) classMap.get("fields");
            Map<String, FieldConfig> fieldConfigs = new HashMap<>();

            for (Map<String, Object> fieldMap : fields) {
                String name = (String) fieldMap.get("name");
                String fullName = (String) fieldMap.get("fullName");
                String dateFormat = (String) fieldMap.get("dateFormat");
                Boolean ignore = (Boolean) fieldMap.get("ignore");
                Map<String, Object> diffLogKeyMap = (Map)fieldMap.get("diffLogKey");
                DiffLogKeyConfig diffLogKey = null;
                if (diffLogKeyMap!=null){
                    diffLogKey = new DiffLogKeyConfig();
                    diffLogKey.setName((String)diffLogKeyMap.get("name"));
                }
                fieldConfigs.put(name,new FieldConfig(name, fullName,dateFormat, ignore == null ?false:ignore,diffLogKey));
            }
            classConfigs.getClassFields().put(className, fieldConfigs);
        }

        return classConfigs;
    }
}