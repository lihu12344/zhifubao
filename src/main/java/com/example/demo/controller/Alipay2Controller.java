package com.example.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.MonitorHeartbeatSynRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.example.demo.config.AlipayProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Alipay2Controller {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AlipayProperties alipayProperties;

    @RequestMapping("/barPay")
    public String BarPay() throws Exception{
        AlipayTradePayModel model=new AlipayTradePayModel();
        model.setOutTradeNo("202");
        model.setScene("bar_code");
        model.setAuthNo("28763443825664394");
        model.setProductCode("FACE_TO_FACE_PAYMENT");
        model.setSubject("海贼王");
        model.setTotalAmount("1000");

        AlipayTradePayRequest request=new AlipayTradePayRequest();
        request.setBizModel(model);

        AlipayTradePayResponse response=alipayClient.execute(request);

        return response.getBody();
    }

    @RequestMapping("/cancel")
    public String cancel() throws Exception{
        AlipayTradeCancelModel model=new AlipayTradeCancelModel();
        model.setOutTradeNo("202");

        AlipayTradeCancelRequest request=new AlipayTradeCancelRequest();
        request.setBizModel(model);

        AlipayTradeCancelResponse response=alipayClient.execute(request);
        return response.getBody();
    }

    @RequestMapping("/monitor")
    public String monitor() throws Exception{
        MonitorHeartbeatSynRequest request=new MonitorHeartbeatSynRequest();
        request.setBizContent("任意值");

        MonitorHeartbeatSynResponse response=alipayClient.execute(request);
        return response.getBody();
    }
}
