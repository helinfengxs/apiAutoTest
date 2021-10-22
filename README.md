# apiAutoTest
接口测试脚本
# 用例数据
用例数据存放于yaml文件中，yaml文件存放在source目录下
# yaml数据约定规则
1.测试集名与测试类名保持一致
2.参数值前缀带有$，表示执行该条用例会替换全局参数中的列表中存放的值；
3.参数值前缀带有—，表示执行该条用例会执行自定义函数，自定义函数在自定义公共方法类中实现并可以使用；
4.参数前缀带有@，表示执行该条用例会删除该参数；
5.断言选择了JSONSchema方式，断言json文件存放source目录下，yaml文件用例数据中，jsonPath字段后跟上json断言文件名，断言时会自动去根据该字段值选择json文件，不带.json后缀名
6.数据库方面暂时未写；
# 运行方式
1.准备yaml文件数据
2.在DataProvider-Datas中，写入DataProvider，需要什么测试数据，根据测试类名获取
```
    @DataProvider(name = "query")
    public static Object[][] query(){
        return CommonParam.newCasesData.get("QueryCases");
```
3.写入测试类
```
public class QueryCases {
    @Test(dataProvider = "query",dataProviderClass = Datas.class)
    public void queryTest(Object obj){
        Utils.runCase(obj);
    }
}
```
4.xml文件加入该测试类，并引入监听类
![image](https://user-images.githubusercontent.com/58164963/138423195-7cf45a7d-a95a-4a06-95b3-afd314a2214f.png)
5.运行xml文件，工程根目录下生成html测试报告

