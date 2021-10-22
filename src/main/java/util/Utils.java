package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.lang.reflect.Method;
import entity.CommonParam;
import entity.RunResult;
import entity.SingleCase;
import io.restassured.response.Response;
import util.service.SendService;
import util.service.imp.SendServiceImpl;
import java.lang.reflect.Proxy;
import java.util.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


/**
 * @author helinfeng
 */
public class Utils {

    /**
     * object对象转换Json对象
     *
     * @param obj 需要转换的object对象
     * @return Json对象
     */
    public static JSONObject convertJsonObject(Object obj) {
        String str = JSON.toJSONString(obj);
        return JSONObject.parseObject(str);
    }

    /**
     * obj转换list
     *
     * @param obj   待转换对象
     * @param clazz 属性类型
     * @param <T>   。
     * @return list集合
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 构造二维数据
     *
     * @param str 待构造数据
     * @return 二维数据
     */
    public static Object[][] getArray(String str) {
        JSONArray objects = JSONArray.parseArray(str);
        List jsonArray = JSONObject.parseArray(objects.toString());
        Object[][] array = new Object[jsonArray.size()][];
        for (int i = 0; i < objects.size(); i++) {
            array[i] = new Object[]{jsonArray.get(i)};
        }
        return array;
    }

    /**
     * 反射调用自定义函数
     *
     * @param str 需要调用的方法名字符串
     * @return 调用对象返回值
     * @throws Exception 调用一次
     */
    public static Object proxy(String str) throws Exception {
        Class<?> clz = Class.forName("util.service.imp.CustomUtilsImpl");
        Object obj = clz.newInstance();
        Method method = obj.getClass().getDeclaredMethod(str);
        return method.invoke(obj);
    }

    /**
     * 发送请求
     *
     * @param obj 用例数据
     */
    public static void runCase(Object obj) {
        SingleCase singleCase = JSON.parseObject(obj.toString(), SingleCase.class);
        Map<String, Object> params = singleCase.getParams();
        singleCase.setParams(checkParam(params));
        String method = singleCase.getMethod();
        SendService re = new SendServiceImpl();
        ProxyHandler proxyHandler = new ProxyHandler(re);
        SendService proxy = (SendService) Proxy.newProxyInstance(re.getClass().getClassLoader(), re.getClass().getInterfaces(), proxyHandler);
        Response response = null;
        switch (method) {
            case "post":
                response = proxy.post(singleCase);
                break;
            case "posts":
                response = proxy.posts(singleCase);
                break;
            case "get":
                response = proxy.get(singleCase);
                break;
            case "gets":
                response = proxy.gets(singleCase);
                break;
            default:
        }
        assertData(response, singleCase);
    }

    /**
     * 检查用例请求参数值是否含有$，进行全局参数列表替换
     *
     * @param params 请求参数Map
     * @return 请求参数Map
     */
    public static Map<String, Object> checkParam(Map<String, Object> params) {
        Set<String> keySet = params.keySet();
        for (String k : keySet) {
            JSONObject jsonObject = Utils.convertJsonObject(params.get(k));
            Map<String, Object> map = JSON.parseObject(jsonObject.toString(), Map.class);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String value = map.get(key).toString();
                if (value.startsWith("$")) {
                    Object reVal = ConstructData.findValue(value.substring(1));
                    map.put(key, reVal);
                }
            }
            params.put(k, map);
        }

        return params;

    }

    /**
     * 响应结果断言
     *
     * @param response   响应结果
     * @param singleCase 用例数据
     */
    public static void assertData(Response response, SingleCase singleCase) {

        RunResult runResult = new RunResult();
        runResult.setDescription(singleCase.getCaseName());
        runResult.setClassName(singleCase.getClassName());
        runResult.setUrl(singleCase.getUrl());
        runResult.setSpendTime(response.time() + "ms");
        List<String> logs = new ArrayList<>();
        singleCase.getParams().forEach((k, v) -> {
            logs.add("请求参数:" + Utils.convertJsonObject(v).toJSONString());
        });
        logs.add("接口响应结果：" + response.asString());
        try {
            response.then().assertThat().statusCode(200).assertThat().body((matchesJsonSchemaInClasspath(singleCase.getAssertFilePath() + ".json")));
            runResult.setStatus("成功");
            HashMap<String, String> extract = singleCase.getExtract();
            extract.forEach((k, v) -> {
                CommonParam.variables.put(v, response.jsonPath().get(k));
            });
            runResult.setLog(new ArrayList<>());
            CommonParam.rightCount++;

        } catch (AssertionError e) {
            runResult.setStatus("失败");
            String[] split = e.getLocalizedMessage().split("\n");
            logs.addAll(Arrays.asList(split));
            CommonParam.errorCount++;
        }
        runResult.setLog(logs);
        CommonParam.resultData.add(runResult);


    }
}
