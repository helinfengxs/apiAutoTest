package providerData;

import entity.CommonParam;
import org.testng.annotations.DataProvider;

/**
 * @author helinfeng
 */
public class Datas {
    @DataProvider(name = "login")
    public static Object[][] init(){
        return CommonParam.newCasesData.get("HelloCases");
    }

    @DataProvider(name = "query")
    public static Object[][] query(){
        return CommonParam.newCasesData.get("QueryCases");
    }
}
