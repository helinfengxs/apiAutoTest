package cases;

import org.testng.annotations.Test;
import providerData.Datas;
import util.Utils;

/**
 * @author helinfeng
 */
public class QueryCases {
    @Test(dataProvider = "query",dataProviderClass = Datas.class)
    public void queryTest(Object obj){
        Utils.runCase(obj);
    }
}
