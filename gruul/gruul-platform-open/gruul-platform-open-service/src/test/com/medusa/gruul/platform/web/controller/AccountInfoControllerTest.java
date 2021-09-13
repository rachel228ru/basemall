package com.medusa.gruul.platform.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.medusa.gruul.platform.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class AccountInfoControllerTest extends BaseTest {

    private static String baseUrl = "/account-info";

    /**
     * 管理台获取商家管理
     */
    @Test
    void merchanList() {
        String url = baseUrl.concat("/merchan");
        JSONObject param = new JSONObject();
        param.put("page", 1);
        param.put("size", 10);
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

        param.put("name", "1588803");
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);
    }


    /**
     * 平台后台登录接口
     */
    @Test
    void add() {
        String url = baseUrl.concat("/plateform/login");
        JSONObject param = new JSONObject();
        param.put("username", "admin");
        param.put("password", 10);
        //登录失败
        MvcResult result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result, 400);

        //登录成功
        param.put("password", "admin");
        result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result);


        //登录失败
        param.put("username", "test");
        result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result, 400);


    }

    /**
     * 账号校验是否存在
     */
    @Test
    void checkoutAccount() {
        String url = baseUrl.concat("/checkout/account");
        JSONObject param = new JSONObject();
        //校验账号存在
        param.put("phone", "15888030961");
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

        //校验账号不存在
        param.put("phone", "1588803096122");
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result, 100100);


        //校验账号存在
        param.put("phone", "15888030961");
        param.put("type", 1);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

        //校验账号不存在
        param.put("phone", "15888030961");
        param.put("type", 2);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result, 500);

        //校验非法请求
        param.put("phone", "15888030961");
        param.put("type", 3);
        result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result, 500);

    }

    /**
     * 根据请求token获取当前用户最新的信息
     */
    @Test
    void info() {
        String url = baseUrl.concat("/info");
        JSONObject param = new JSONObject();
        //校验账号存在
        MvcResult result = super.getMockTestGet(url, param);
        super.printResult(result);
        super.assertCodeSuccess(result);

    }

    /**
     * 登录接口
     */
    @Test
    void login1() {
        //密码登录
        String url = baseUrl.concat("/login-v1");
        JSONObject param = new JSONObject();
        param.put("loginType", "1");
        param.put("phone", 15888030961L);
        param.put("password", "123456");
        param.put("certificate", "");
        MvcResult result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result);


        //密码错误
        param.put("password", "2222");
        result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result,500);


        //手机号错误
        param.put("phone", "12345");
        result = super.getMockTestPost(url, param.toJSONString());
        super.printResult(result);
        super.assertCodeSuccess(result,100100);




    }


}