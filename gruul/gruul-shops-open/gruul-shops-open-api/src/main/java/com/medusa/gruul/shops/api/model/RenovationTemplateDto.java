package com.medusa.gruul.shops.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description: RenovationTemplateDto.java
 * @author: alan
 * @date: 2020/4/22 22:58
 */
@NoArgsConstructor
@Data
public class RenovationTemplateDto implements Serializable {

    private static final long serialVersionUID = -7526899743761309130L;


    private String type;
    private String shopId;
    private String tenantId;
    private String colour;
    private String onlineStatus;
    private String isDevTemplate;
    private String name;
    private String isCopyTemplate;
    private List<Pages> pages;
    private List<Plugins> plugins;

    @NoArgsConstructor
    @Data
    public static class Pages {
        private String pageName;
        private String isDef;
        private List<Assemblies> assemblies;

        @NoArgsConstructor
        @Data
        public static class Assemblies {

            private String properties;
        }
    }

    @NoArgsConstructor
    @Data
    public static class Plugins {

        private String pluginProperties;
        private String pluginNameCn;
        private String pluginNameEn;
        private String isSelection;
        private String isMandatory;
    }
}
