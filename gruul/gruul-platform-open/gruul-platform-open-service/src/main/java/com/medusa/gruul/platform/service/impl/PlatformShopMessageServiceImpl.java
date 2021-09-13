package com.medusa.gruul.platform.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.medusa.gruul.common.core.constant.CommonConstants;
import com.medusa.gruul.common.core.exception.ServiceException;
import com.medusa.gruul.common.data.tenant.TenantContextHolder;
import com.medusa.gruul.platform.api.entity.MiniInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopInfo;
import com.medusa.gruul.platform.api.entity.PlatformShopMessage;
import com.medusa.gruul.platform.api.entity.PlatformShopTemplateDetail;
import com.medusa.gruul.platform.api.model.dto.SubscribeMsgSendDto;
import com.medusa.gruul.platform.api.model.vo.MiniMsgVo;
import com.medusa.gruul.platform.api.model.vo.ShopMessageDetailVo;
import com.medusa.gruul.platform.api.model.vo.ShopMessageVo;
import com.medusa.gruul.platform.mapper.PlatformShopMessageMapper;
import com.medusa.gruul.platform.model.dto.MiniSubscriberiMessageDto;
import com.medusa.gruul.platform.model.dto.MotifyMsgStateDto;
import com.medusa.gruul.platform.service.*;
import lombok.extern.log4j.Log4j2;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenMaService;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 店铺消息配置 服务实现类
 * </p>
 *
 * @author whh
 * @since 2020-05-22
 */
@Service
@Log4j2
public class PlatformShopMessageServiceImpl extends ServiceImpl<PlatformShopMessageMapper, PlatformShopMessage> implements IPlatformShopMessageService {

    @Autowired
    private IDefaultValueService defaultValueService;
    @Autowired
    private IPlatformShopTemplateDetailService platformShopTemplateDetailService;
    @Autowired
    private IPlatformShopInfoService platformShopInfoService;
    @Autowired
    private IMiniInfoService miniInfoService;
    @Autowired
    private IWechatPlatformService wechatPlatformService;
    @Autowired
    private WxOpenService wxOpenService;

    @Override
    public void generateInitMsg(String version) {
        String tenantId = TenantContextHolder.getTenantId();
        String unique = "PLATFROM_SHOP_MSG";
        String defaultValue = defaultValueService.getDefaultValue(version, unique);
        if (StrUtil.isEmpty(defaultValue)) {
            log.debug("未找到消息版本,未生成数据; tenantId: {} ; version {}", tenantId, version);
            return;
        }
        generteMsg(defaultValue, version);
    }


    /**
     * 生成默认消息
     *
     * @param defaultValue 版本默认值
     */
    private void generteMsg(String defaultValue, String version) {
        JSONObject jsonObject = JSONObject.parseObject(defaultValue);
        Integer useType = jsonObject.getInteger("useType");
        JSONArray items = jsonObject.getJSONArray("items");
        synchronized (PlatformShopMessageServiceImpl.class) {
            for (Object item : items) {
                JSONObject obj = (JSONObject) item;
                PlatformShopMessage platformShopMessage = obj.toJavaObject(PlatformShopMessage.class);
                PlatformShopMessage one = this.getOne(new QueryWrapper<PlatformShopMessage>()
                        .eq("use_type", useType)
                        .eq("version", version)
                        .eq("title", platformShopMessage.getTitle())
                        .eq("tenant_id", TenantContextHolder.getTenantId())
                );
                if (one != null) {
                    BeanUtil.copyProperties(platformShopMessage, one, "id");
                    this.baseMapper.updateById(one);
                    continue;
                }
                platformShopMessage.setUseType(useType);
                platformShopMessage.setVersion(version);
                this.baseMapper.insert(platformShopMessage);
            }
        }
    }


    @Override
    public List<MiniMsgVo> getCurrentMiniMsg() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("非法请求");
        }

        PlatformShopInfo info = platformShopInfoService.getByTenantId(tenantId);
        PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailService.getById(info.getShopTemplateDetailId());
        List<PlatformShopMessage> list = this.baseMapper.selectList(new QueryWrapper<PlatformShopMessage>()
                .eq("version", templateDetail.getVersion())
                .eq("use_type", CommonConstants.NUMBER_ONE));
        if (CollectionUtil.isEmpty(list)) {
            List<PlatformShopMessage> lastMiniMsg = getLastMiniMsg();
            if (CollectionUtil.isEmpty(lastMiniMsg)) {
                return new LinkedList<>();
            }
            return lastMiniMsg.stream().map(obj -> BeanUtil.toBean(obj, MiniMsgVo.class)).collect(Collectors.toList());
        }
        return list.stream().map(obj -> BeanUtil.toBean(obj, MiniMsgVo.class)).collect(Collectors.toList());
    }

    @Override
    public List<ShopMessageVo> msgAll() {
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isEmpty(tenantId)) {
            throw new ServiceException("非法请求");
        }
        PlatformShopInfo info = platformShopInfoService.getByTenantId(tenantId);
        PlatformShopTemplateDetail templateDetail = platformShopTemplateDetailService.getById(info.getShopTemplateDetailId());
        List<PlatformShopMessage> list = this.baseMapper.selectList(new QueryWrapper<PlatformShopMessage>()
                .eq("version", templateDetail.getVersion())
                .eq("use_type", CommonConstants.NUMBER_ONE));
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<Integer, List<PlatformShopMessage>> listMap = list.stream().collect(Collectors.groupingBy(PlatformShopMessage::getMessageType));
        List<ShopMessageVo> vos = new LinkedList<>();
        //获取订单消息
        getVos(listMap.get(CommonConstants.NUMBER_ONE), vos, "订单消息");
        //获取用户消息
        getVos(listMap.get(CommonConstants.NUMBER_THREE), vos, "用户消息");
        return vos;
    }


    private List<PlatformShopMessage> getLastMiniMsg() {
        return this.baseMapper.getLastMiniMsg();
    }


    @Override
    public void upSubscriptionMsg(String version, String tenantId) {
        miniInfoService.checkCurrentTenantId(tenantId);
        wechatPlatformService.addSubscribeMsg(tenantId, version);
    }


    @Override
    public List<PlatformShopMessage> getByVersionMes(String version, Integer useType) {
        return this.baseMapper.selectList(new QueryWrapper<PlatformShopMessage>()
                .eq("version", version)
                .eq("use_type", useType));
    }

    /**
     * 封装数据
     *
     * @param platformShopMessages 指定类型数据
     * @param list                 数组
     * @param title                标题
     */
    private void getVos(List<PlatformShopMessage> platformShopMessages, List<ShopMessageVo> list, String title) {
        if (CollectionUtil.isEmpty(platformShopMessages)) {
            return;
        }
        ShopMessageVo vo = new ShopMessageVo();
        vo.setMsgTitle(title);
        List<ShopMessageDetailVo> vos = new LinkedList<>();
        for (PlatformShopMessage platformShopMessage : platformShopMessages) {
            ShopMessageDetailVo shopMessageDetailVo = BeanUtil.toBean(platformShopMessage, ShopMessageDetailVo.class);
            vos.add(shopMessageDetailVo);
        }
        vo.setShopMessageDetailVos(vos);
        list.add(vo);
    }


    @Override
    public void subscribeMsgSend(SubscribeMsgSendDto msgSendDto) {
        MiniInfo miniInfo = miniInfoService.getByMiniTenantId(msgSendDto.getTenantId());
        PlatformShopMessage miniSubscriberiMessage = this.getOne(new QueryWrapper<PlatformShopMessage>().eq("mini_template_id", msgSendDto.getTemplateId()));
        if (miniSubscriberiMessage == null) {
            throw new ServiceException("无效templateId");
        }

        MiniSubscriberiMessageDto miniSubscriberiMessageDto = JSONObject.parseObject(miniSubscriberiMessage.getMiniMsg(), MiniSubscriberiMessageDto.class);
        String kIds = miniSubscriberiMessageDto.getKIds();
        String rules = miniSubscriberiMessageDto.getRules();
        String[] kIdArr = kIds.split(",");
        String[] rulesArr = rules.split(",");
        LinkedList<String> sendDatas = msgSendDto.getSendDatas();
        if (CollectionUtil.isEmpty(sendDatas)) {
            throw new ServiceException("sendDatas=null");
        }
        if (kIdArr.length != sendDatas.size()) {
            throw new ServiceException("消息内容数据错误,kIdArr.length=".concat(String.valueOf(kIdArr.length)).concat("sendDatas.size=").concat(String.valueOf(msgSendDto.getSendDatas().size())));
        }
        String miniprogramState = "";
        if (StrUtil.isNotEmpty(msgSendDto.getToPath())) {
            miniprogramState = WxMaConstants.MiniprogramState.DEVELOPER;
        }
        //判断发送那个环境
        List<WxMaSubscribeMessage.Data> datas = new ArrayList<>();
        for (int i = 0; i < rulesArr.length; i++) {
            WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
            data.setName(rulesArr[i] + kIdArr[i]);
            data.setValue(sendDatas.get(i));
            datas.add(data);
        }
        sendSubscribeMsg(miniInfo.getAppId(), msgSendDto.getOpenId(), msgSendDto.getTemplateId(), msgSendDto.getToPath(), datas, miniprogramState);

    }

    @Override
    public void motifyState(MotifyMsgStateDto msgStateDto) {
        PlatformShopMessage shopMessage = this.getById(msgStateDto.getId());
        if (shopMessage == null) {
            throw new ServiceException("不存在指定消息");
        }
        if (msgStateDto.getMiniOpen() != null && msgStateDto.getMiniOpen() > 0) {
            String miniTemplateId = shopMessage.getMiniTemplateId();
            if (StrUtil.isEmpty(miniTemplateId)) {
                throw new ServiceException("请上传审核小程序之后再开启");
            }
        }
        PlatformShopMessage platformShopMessage = BeanUtil.toBean(msgStateDto, PlatformShopMessage.class);
        this.updateById(platformShopMessage);
    }

    /**
     * @param appId      小程序Appid
     * @param openId     openId
     * @param templateId 订阅消息模板id
     * @param toPath     跳转页面
     * @param data       发送数据
     */
    private void sendSubscribeMsg(String appId, String openId, String templateId, String toPath, List<WxMaSubscribeMessage.Data> data, String miniprogramState) {
        WxOpenMaService openMaService = wxOpenService.getWxOpenComponentService().getWxMaServiceByAppid(appId);
        WxMaMsgService msgService = openMaService.getMsgService();
        WxMaSubscribeMessage.WxMaSubscribeMessageBuilder builder = WxMaSubscribeMessage.builder();
        builder.toUser(openId);
        builder.templateId(templateId);
        builder.page(toPath);
        builder.miniprogramState(miniprogramState);
        builder.data(data);
        WxMaSubscribeMessage build = builder.build();
        try {
            msgService.sendSubscribeMsg(build);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

}
