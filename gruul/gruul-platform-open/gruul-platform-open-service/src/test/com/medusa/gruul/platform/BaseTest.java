package com.medusa.gruul.platform;


import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.client.naming.utils.StringUtils;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.data.tenant.TenantContextHolderFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PlatformApplication.class, WebApplicationContext.class})
@WebAppConfiguration
@ContextConfiguration
public class BaseTest {
    public MockMvc mvc;
    @Autowired
    public WebApplicationContext wac;
    @Autowired
    private TenantContextHolderFilter tenantContextHolderFilter;

    @BeforeEach
    public void setupMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(tenantContextHolderFilter)
                .build(); // 初始化MockMvc对象
    }


    public MvcResult getMockTestPost(String url, String json) {
        return this.getMockTestPost(url, json, this.headers());

    }

    public MvcResult getMockTestPost(String url, String json, HttpHeaders headers) {
        try {
            MockHttpServletRequestBuilder b = MockMvcRequestBuilders.post(url)
                    .header("Content-type", "application/json")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .content(json.getBytes());
            if (headers != null) {
                b.headers(headers);
            }
            MvcResult v = mvc.perform(b)
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public MvcResult getMockTestGet(String url, HttpHeaders headers) {
        try {
            MockHttpServletRequestBuilder b = MockMvcRequestBuilders.get(url)
                    .header("Content-type", "application/json");
            if (headers != null) {
                b.headers(headers);
            }
            MvcResult v = mvc.perform(b)
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MvcResult getMockTestGet(String url) {
        return this.getMockTestGet(url, this.headers());
    }

    public MvcResult getMockTestGet(String url, Map<String, Object> paramsMap) {
        List<String> params = new ArrayList<>(10);
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }
        Collections.sort(params);
        return this.getMockTestGet(url.concat("?").concat(StringUtils.join(params, "&")));
    }

    public MvcResult getMockTestPut(String url) {
        return this.getMockTestPut(url, this.headers());
    }

    private MvcResult getMockTestPut(String url, HttpHeaders headers) {
        try {
            MockHttpServletRequestBuilder b = MockMvcRequestBuilders.put(url)
                    .header("Content-type", "application/json");
            if (headers != null) {
                b.headers(headers);
            }
            MvcResult v = mvc.perform(b)
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printResult(MvcResult v) {
        try {
            log.info(v.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public JSONObject assertCodeSuccess(MvcResult v) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtil.parseObj(v.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            assert v.getResponse().getStatus() == 200;
            jsonObject = new JSONObject();
            try {
                jsonObject.put("value", v.getResponse().getContentAsString());
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
            }
            return jsonObject;
        }
        assert jsonObject.get("code").equals(200);
        return jsonObject;
    }

    public JSONObject assertCodeSuccess(MvcResult v, int code) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONUtil.parseObj(v.getResponse().getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert jsonObject.get("code").equals(code);
        return jsonObject;
    }

    public HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.add(CommonConstants.TENANT_ID, "100002");
        h.add(CommonConstants.SHOP_ID, "100002100001");
        h.add(CommonConstants.TOKEN, "platform:tk:1f40c6591f33ec0c97add53a8bb58d8ct8l0rla7932e479e536daf728808a2eecdc07a");
        return h;
    }


}
