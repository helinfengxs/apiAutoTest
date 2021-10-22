package util.service;
import entity.SingleCase;
import io.restassured.response.Response;

/**
 * @author helinfeng
 */
public interface SendService {
    /**
     * 发送post  http请求
     * @param singleCase 用例请求数据
     * @return 响应结果
     */
    Response post(SingleCase singleCase);
    /**
     * 发送post  https请求
     * @param singleCase 用例请求数据
     * @return 响应结果
     */
    Response posts(SingleCase singleCase);
    /**
     * 发送get http请求
     * @param singleCase 用例请求数据
     * @return 响应结果
     */
    Response get(SingleCase singleCase);
    /**
     * 发送get https请求
     * @param singleCase 用例请求数据
     * @return 响应结果
     */
    Response gets(SingleCase singleCase);
}
