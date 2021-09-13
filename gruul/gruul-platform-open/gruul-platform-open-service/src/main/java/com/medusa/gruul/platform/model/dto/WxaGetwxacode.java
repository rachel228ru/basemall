package com.medusa.gruul.platform.model.dto;

import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 获取小程序码dto
 *
 * @author whh
 * @description
 * @data: 2020/1/12
 */
@Data
public class WxaGetwxacode {


    @ApiModelProperty(value = "扫码进入的小程序页面路径，最大长度 128 字节，不能为空,可页面参数,请求头需带上租户id", required = true)
    @NotBlank(message = "路径不能为空")
    private String path;


    @ApiModelProperty("二维码的宽度，单位 px。最小 280px，最大 1280px,非必填,默认430px")
    private Integer width;

//    @ApiModelProperty(value = "最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式） \n" +
//            "   * 用户扫描该码进入小程序后，将统一打开首页，开发者需在对应页面根据获取的码中 scene 字段的值，再做处理逻辑。\n" +
//            "   * 使用如下代码可以获取到二维码中的 scene 字段的值。\n" +
//            "   * 调试阶段可以使用开发工具的条件编译自定义参数 scene=xxxx 进行模拟，开发工具模拟时的 scene 的参数值需要进行 urlencode", required = true)
//    @NotBlank(message = "scenebu不能为空")
//    private String scene;


}
