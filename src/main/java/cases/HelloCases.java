package cases;


import org.testng.annotations.Test;
import providerData.Datas;
import util.Utils;


/**
 * @author helinfeng
 */
public class HelloCases {

    @Test(dataProvider = "login",dataProviderClass = Datas.class)
    public void test(Object obj){
        Utils.runCase(obj);
    }
}
