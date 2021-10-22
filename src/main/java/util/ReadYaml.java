package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.CommonParam;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author helinfeng
 * 读取yaml文件类
 */
public class ReadYaml {


    /**
     * 公共配置信息、用例数据分类
     *
     * @param fileInputStream 用例数据文件流
     * @throws IOException 文件流异常
     */
    public static void getAllData(FileInputStream fileInputStream) throws IOException {
        List<Object> listAllData = readYaml(fileInputStream);
        for (int i = 0; i < listAllData.size(); i++) {
            Object obj = listAllData.get(i);
            if (i == 0) {
                //从公共配置对象中，获取环境url,全局公共参数，本次测试名称
                getCommonData(obj);
                continue;
            }
            //提取全部case用例数据Object对象，转换为HashMap并存放
            getCasesData(obj);
        }

    }

    /**
     * 获取公共公共配置信息,并存放缓存
     *
     * @param common 公共配置信息object
     */
    private static void getCommonData(Object common) {
        Object config = Utils.convertJsonObject(common).get("config");
        JSONObject configJson = Utils.convertJsonObject(config);
        CommonParam.configUrl = Utils.convertJsonObject(configJson.get("request")).get("base_url").toString();
        CommonParam.variables = JSON.parseObject(Utils.convertJsonObject(configJson.get("variables")).toString(), HashMap.class);
        CommonParam.testTile = configJson.get("name").toString();
    }

    /**
     * 获取全部用例数据，并以map集合方式存放缓存
     *
     * @param obj 待转换全部用例数据Object对象
     */
    private static void getCasesData(Object obj) {
        JSONObject jsonObject = Utils.convertJsonObject(obj);
        Map<String, Object> map = JSONObject.parseObject(jsonObject.toString(), Map.class);
        Set<String> keySet = map.keySet();
        for (String k : keySet) {
            Object v = map.get(k);
            CommonParam.casesData.put(k, v);
        }
    }

    /**
     * 获取yaml文件所有数据流
     *
     * @param fileInputStream 用例数据文件流
     * @return 返回数据list集合
     * @throws IOException 文件流异常
     */
    private static List<Object> readYaml(FileInputStream fileInputStream) throws IOException {
        Yaml yaml = new Yaml();

        Object load = yaml.load(fileInputStream);
        List<Object> loadList = null;
        if (load instanceof List) {
            loadList = (List) load;
        } else {
            System.out.println("数据类型补匹配，无法转换成list集合");
        }
        return loadList;
    }


}
