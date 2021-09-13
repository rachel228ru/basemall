package com.medusa.gruul.afs.controller;


import com.medusa.gruul.afs.api.entity.AfsNegotiateHistory;
import com.medusa.gruul.afs.model.NegotiateHistoryDto;
import com.medusa.gruul.afs.service.IAfsNegotiateHistoryService;
import com.medusa.gruul.common.core.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 协商历史 前端控制器
 * </p>
 *
 * @author alan
 * @since 2020 -08-05
 */
@RestController
@Api(tags = "协商历史接口")
@RequestMapping("/negotiate")
public class AfsNegotiateHistoryController {
    @Resource
    private IAfsNegotiateHistoryService afsNegotiateHistoryService;

    /**
     * 获取订单的售后协商历史
     *
     * @param dto the dto
     * @return the result
     */
    @ApiOperation(value = "获取订单的售后协商历史")
    @GetMapping("/history")
    public Result<List<AfsNegotiateHistory>> negotiateHistoryList(NegotiateHistoryDto dto) {
        List<AfsNegotiateHistory> historyList = afsNegotiateHistoryService.negotiateHistoryList(dto);
        return Result.ok(historyList);

    }

}
