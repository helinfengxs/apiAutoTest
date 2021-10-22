package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author helinfeng
 * 公共请求信息配置类
 */
public class CommonParam {
    public static String testTile;
    public static String configUrl;
    public static HashMap<String,Object> variables;
    public static LinkedHashMap<String,Object> casesData = new LinkedHashMap<>();
    public static LinkedHashMap<String,Object[][]> newCasesData = new LinkedHashMap<>();
    public static List<RunResult> resultData = new ArrayList<>();
    public static int rightCount = 0;
    public static int errorCount = 0;
}
