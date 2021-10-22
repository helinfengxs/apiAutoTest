package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.CommonParam;
import entity.SingleCase;
import entity.SingleCommonCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author helinfeng
 */
public class ConstructData {

    /**
     * 解析公共参数，替换自定义函数，和变量
     *
     * @param singleCaseCom 公共参数实例对象
     * @param obj           待解析的公共参数对象
     */
    public static void commonData(SingleCommonCase singleCaseCom, Object obj) {
        JSONObject commonJson = Utils.convertJsonObject(obj);
        JSONObject requestJson = Utils.convertJsonObject(commonJson.get("request"));
        Map<String, String> map = JSONObject.parseObject(JSONObject.toJSONString(requestJson.get("header")), Map.class);
        singleCaseCom.setHeaders(map);
        singleCaseCom.setMethod(requestJson.get("method").toString());
        singleCaseCom.setUrl(requestJson.get("url").toString());


        Object jsonObj = requestJson.get("json");
        Object formObj = requestJson.get("form");
        if (jsonObj != null) {
            Map<String, Object> mapParam = new HashMap<>();
            //替换 json格式 请求参数值带有"$" 和 带有"_" 接口请求参数
            mapParam.put("json", replaceCommon(JSON.parseObject(Utils.convertJsonObject(jsonObj).toString(), Map.class)));
            singleCaseCom.setParams(mapParam);
        } else if (formObj != null) {
            Map<String, Object> mapParam = new HashMap<>();
            //替换 form 请求参数值带有"$" 和 带有"_" 接口请求参数
            mapParam.put("form", replaceCommon(JSON.parseObject(Utils.convertJsonObject(formObj).toString(), Map.class)));
            singleCaseCom.setParams(mapParam);
        }else {
            System.out.println("该接口没有请求参数");
        }
    }

    /**
     * 查找公共参数列表值
     *
     * @param str 待查找字符串
     * @return 查询的value值
     */
    public static Object findValue(String str) {
        Object result = null;
        if (CommonParam.variables.containsKey(str)) {
            result = CommonParam.variables.get(str);
        }
        return result;
    }

    /**
     * 替换common中参数value值
     *
     * @param valueMap 公共请求参数Map
     * @return 替换后的请求参数Map
     */
    public static Map<String, Object> replaceCommon(Map<String, Object> valueMap) {
        String checkStr = "$";
        String checkFuncStr = "_";
        Set<String> valueMapKeys = valueMap.keySet();
        for (String key : valueMapKeys) {
            String findStr = valueMap.get(key).toString();
            if (findStr.startsWith(checkStr)) {
                //前缀带有$符，去掉$符后，进入全局公共参数查询，如果查询无结果填入null
                valueMap.put(key, findValue(findStr.substring(1)));
            } else if (findStr.startsWith(checkFuncStr)) {
                //前缀带有_符，去掉_符后，调用自定义函数，如果无该自定义函数，原来值不变。
                Object proxy = valueMap.get(key);
                try {
                    proxy = Utils.proxy(findStr.substring(1));
                } catch (Exception e) {
                    System.out.println(findStr.substring(1) + " 自定义函数暂未实现，请补充");
                }
                valueMap.put(key, proxy);
            }
        }
        return valueMap;
    }

    /**
     * 整理用例数据
     * @param filePath yaml文件路径
     */
    public static void cleanData(String filePath) {
        try{
            FileInputStream fileInputStream = new FileInputStream(filePath);
            ReadYaml.getAllData(fileInputStream);
            LinkedHashMap<String, Object> casesData = CommonParam.casesData;
            Set<String> keySet = casesData.keySet();
            //用例集遍历，k为用例集名,得到用例集所有数据
            for (String k : keySet) {
                String className = k;
                List<Object> lists = Utils.castList(casesData.get(k), Object.class);
                SingleCommonCase singleCaseCom = new SingleCommonCase();
                List<SingleCase> singleCaseList = new ArrayList<>();
                //遍历用例集数据，得到每条用例数据
                for (Object value : lists) {
                    JSONObject jsonObject = Utils.convertJsonObject(value);
                    Object obj = jsonObject.get("common");
                    //转换common数据；
                    if (obj != null) {

                        ConstructData.commonData(singleCaseCom, obj);
                        continue;
                    }
                    //转换用例数据
                    SingleCase singleCase = new SingleCase();
                    Map<String, Object> map = JSON.parseObject(jsonObject.toString(), Map.class);
                    Set<String> keys = map.keySet();
                    //遍历每条数据，得到具体数据信息
                    for (String key : keys) {
                        JSONObject valJson = Utils.convertJsonObject(map.get(key));
                        JSONObject request = Utils.convertJsonObject(valJson.get("request"));
                        singleCase.setUrl(singleCaseCom.getUrl());
                        singleCase.setHeaders(singleCaseCom.getHeaders());
                        singleCase.setMethod(singleCaseCom.getMethod());
                        singleCase.setCaseName(valJson.get("name").toString());
                        singleCase.setClassName(className);
                        Map<String, Object> finalMap = ConstructData.caseParamData(singleCaseCom, request);
                        singleCase.setParams(finalMap);
                        //接口返回值待提取参数整理，并存放
                        Object extractObj = request.get("extract");
                        if (extractObj != null) {
                            singleCase.setExtract(JSON.parseObject(Utils.convertJsonObject(extractObj).toString(), HashMap.class));
                        }
                        //获取jsonschema路径，并存放
                        Object assertFilePathObj = request.get("assertFilePath");
                        if (assertFilePathObj != null) {
                            singleCase.setAssertFilePath(assertFilePathObj.toString());
                        }

                    }
                    singleCaseList.add(singleCase);
                }

                CommonParam.newCasesData.put(k, Utils.getArray(JSON.toJSONString(singleCaseList)));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 逻辑：先从公共用例中，取出用例需要的公共数据，并set进用例数据对象SingleCase中；
     * 首先得到公共参数请求头、请求方式等信息，再遍历公共参数Map(SingleCommonCase.getParams())
     * 得到 接口参数提交的方式（json、form等）以及对应的参数map,再次遍历该mao,得到每个具体的
     * 参数名，参数值，并存放到新的map（newMap）中；再得到用例的原
     * 始数据，并遍历依次判断参数是否要替换、删除 newMap中的参数以及参数值；
     *
     * @param singleCaseCom 公共参数实例
     * @param request       每条用例原始值
     * @return Map<String, Object>
     */
    public static Map<String, Object> caseParamData(SingleCommonCase singleCaseCom, JSONObject request) {
        //新的每条请求参数Map,用于存储请求参数数据
        Map<String, Object> newMap = new HashMap<>();
        //存放每条用例数据请求格式，例如：json提交，form表单提交
        String paramKey = "";
        Map<String, Object> finalMap = new HashMap<>();

        //获取用例集公共请求参数内容
        Map<String, Object> params = singleCaseCom.getParams();
        if(params != null){
            for (String k1 : params.keySet()) {
                paramKey = k1;
                Object v1 = params.get(k1);
                //公共参数Map,取出每个 k,v ，并set进newMap中
                Map<String, Object> comMaps = JSON.parseObject(Utils.convertJsonObject(v1).toString(), Map.class);
                for (String k2 : comMaps.keySet()) {
                    newMap.put(k2, comMaps.get(k2));
                }
            }
            //获取每条用例key--request，参数内容
            //获取每条用例，yaml文件request字段下请求参数，并改变newMap相同key存放的参数value值；
            Object body = request.get("body");
            if (body != null) {
                Map<String, Object> caseMap = JSON.parseObject(Utils.convertJsonObject(body).toString(), Map.class);
                for (String k3 : caseMap.keySet()) {
                    Object v3 = caseMap.get(k3);
                    String va = v3.toString();
                    //判断前缀带有@符号的参数名，需要在newMap中删除;
                    if (k3.startsWith("@")) {
                        newMap.remove(k3.substring(1));
                        //判断前缀带有$符号的value值，需要在newMap中替换全局参数列表的值(CommonParam.variables);
                    } else if (va.startsWith("$")) {

                        Object reVal = ConstructData.findValue(va.substring(1));
                        if(reVal == null){
                            newMap.put(k3,va);
                        }else {
                            newMap.put(k3, reVal);
                        }

                        //判断带有"-"符号前缀的value值，需要调用自定义函数生成新的value值；
                    } else if (va.startsWith("_")) {
                        Object proxy = v3;
                        try {
                            proxy = Utils.proxy(va.substring(1));
                        } catch (Exception e) {
                            //自定义函数未实现
                            System.out.println(va.substring(1) + " 自定义函数暂未实现，请补充");
                        }
                        newMap.put(k3, proxy);
                    } else {
                        //前缀不带任何符号的，直接替换newMap相同的key-value
                        newMap.put(k3, v3);
                    }

                }

            }
            finalMap.put(paramKey, newMap);
        }

        return finalMap;
    }
}
