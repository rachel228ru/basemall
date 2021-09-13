package com.medusa.gruul.order.controller.point;

import com.medusa.gruul.common.core.util.PageUtils;
import com.medusa.gruul.common.core.util.Result;
import com.medusa.gruul.order.model.*;
import com.medusa.gruul.order.service.IPointOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提货点端
 *
 * @author alan
 * @date 2019 /11/13 20:14
 */
@RestController
@RequestMapping("/point")
@Api(tags = "提货点端订单接口")
public class PointOrderController {
    @Resource
    private IPointOrderService pointOrderService;

    //Todo 阉割
}
