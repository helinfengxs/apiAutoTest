package util.service.imp;

import com.alibaba.fastjson.JSON;
import entity.SingleCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import util.service.SendService;

import java.util.Map;
import java.util.Set;


/**
 * @author helinfeng
 */
public class SendServiceImpl implements SendService {
    @Override
    public Response post(SingleCase singleCase) {
        String url = singleCase.getUrl();
        Map<String, String> headers = singleCase.getHeaders();
        Map<String, Object> params = singleCase.getParams();
        Response response = null;
        if(!params.isEmpty()){
            String type = params.keySet().toArray()[0].toString();
            switch (type){
                case "json":
                    response = RestAssured.given()
                            .headers(headers)
                            .body(params.get(type))
                            .when()
                            .post(url);
                    break;
                case "form":
                    response = RestAssured.given()
                            .headers(headers)
                            .formParams((Map)params.get(type))
                            .when()
                            .post(url);
                    break;
                default:
            }

        }
        else {
            response = RestAssured.given()
                    .headers(headers)
                    .when()
                    .post(url);
        }
        return response;
    }

    @Override
    public Response posts(SingleCase singleCase) {
        String url = singleCase.getUrl();
        Map<String, String> headers = singleCase.getHeaders();
        Map<String, Object> params = singleCase.getParams();
        Response response = null;
        if(!params.isEmpty()){
            String type = params.keySet().toArray()[0].toString();
            switch (type){
                case "json":
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .body(params.get(type))
                            .when()
                            .post(url);
                    break;
                case "form":
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .formParams((Map)params.get(type))
                            .when()
                            .post(url);
                    break;
                default:
            }
    }
        else {
            response = RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .headers(headers)
                    .when()
                    .post(url);
        }
        return response;
    }

    @Override
    public Response get(SingleCase singleCase) {
        String url = singleCase.getUrl();
        Map<String, String> headers = singleCase.getHeaders();
        Map<String, Object> params = singleCase.getParams();
        Response response = null;
        if(!params.isEmpty()){
            String type = params.keySet().toArray()[0].toString();
            switch (type){
                case "json":
                    response = RestAssured.given()
                            .headers(headers)
                            .body(params.get(type))
                            .when()
                            .get(url);
                    break;
                case "form":
                    response = RestAssured.given()
                            .headers(headers)
                            .formParams((Map)params.get(type))
                            .when()
                            .get(url);
                    break;
                default:
            }

        }
        else {
            response = RestAssured.given()
                    .headers(headers)
                    .when()
                    .post(url);
        }

        return response;
    }

    @Override
    public Response gets(SingleCase singleCase) {
        String url = singleCase.getUrl();
        Map<String, String> headers = singleCase.getHeaders();
        Map<String, Object> params = singleCase.getParams();
        Response response = null;
        if(!params.isEmpty()){
            String type = params.keySet().toArray()[0].toString();
            switch (type){
                case "json":
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .body(params.get(type))
                            .when()
                            .get(url);
                    break;
                case "form":
                    response = RestAssured.given()
                            .relaxedHTTPSValidation("TLS")
                            .headers(headers)
                            .formParams((Map)params.get(type))
                            .when()
                            .get(url);
                    break;
                default:
            }
        }
        else {
            response = RestAssured.given()
                    .relaxedHTTPSValidation("TLS")
                    .headers(headers)
                    .when()
                    .get(url);
        }
        return response;
    }
}
