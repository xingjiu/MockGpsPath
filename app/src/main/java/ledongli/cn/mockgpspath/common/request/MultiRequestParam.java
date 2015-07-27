package ledongli.cn.mockgpspath.common.request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyida on 15-7-23.
 */
public class MultiRequestParam {

    Map<String, String> stringMap = new HashMap<>();
    Map<String, byte[]> dataMap = new HashMap<>();
    Map<String, File> fileMap = new HashMap<>();

    public Map<String, String> getStringPart() {
        return stringMap;
    }

    public Map<String, byte[]> getDataPart() {
        return dataMap;
    }

    public Map<String, File> getFileMap() {
        return fileMap;
    }

    public void put(String key, String value) {
        stringMap.put(key, value);
    }

    public void put(String key, byte[] value) {
        dataMap.put(key, value);
    }

    public void put(String key, File file) {
        fileMap.put(key, file);
    }

}
