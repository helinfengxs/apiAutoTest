package listener;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.CommonParam;
import entity.SingleCase;
import entity.SingleCommonCase;
import io.restassured.RestAssured;
import org.testng.*;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;
import org.testng.internal.annotations.TestAnnotation;
import util.ConstructData;
import util.ReadYaml;
import util.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author helinfeng
 */

public class CustomizeListener implements IInvokedMethodListener, ITestListener,IAnnotationTransformer,IAnnotationTransformer2  {
    static {
        ConstructData.cleanData("./src/main/resources/test.yaml");
    }
    @Override
    public void onTestStart(ITestResult result) {

    }
    @Override
    public void onTestSuccess(ITestResult result) {

    }
    @Override
    public void onTestFailure(ITestResult result) {



    }
    @Override
    public void onTestSkipped(ITestResult result) {


    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {



    }

    @Override
    public void onStart(ITestContext context) {
        RestAssured.baseURI= CommonParam.configUrl;

    }


    @Override
    public void onFinish(ITestContext context) {

    }
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    private boolean annotationPresent(IInvokedMethod method, Class<MyAnnotation> clazz) {

        return method.getTestMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(clazz) ? true : false;
    }
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {


    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        String className = testMethod.getDeclaringClass().getName();
        className = className.substring(className.lastIndexOf(".")+1);


    }

    @Override
    public void transform(IConfigurationAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

    }

    @Override
    public void transform(IDataProviderAnnotation annotation, Method method) {

    }

    @Override
    public void transform(IFactoryAnnotation annotation, Method method) {

    }
}
