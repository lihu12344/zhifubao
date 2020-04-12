package com.example.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AccPayeeInfo;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.example.demo.config.AlipayProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class Alipay4Controller {

    @Resource
    public AlipayClient alipayClient;

    @Resource
    public AlipayProperties alipayProperties;

    @RequestMapping("/transfer")
    public String transfer() throws Exception{
        AlipayFundTransUniTransferModel model=new AlipayFundTransUniTransferModel();
        model.setOutBizNo("10000");
        model.setBizScene("DIRECT_TRANSFER");
        model.setTransAmount("100");
        model.setProductCode("STD_RED_PACKET");

        Participant payer=new Participant();
        payer.setIdentityType("ALIPAY_USER_ID");
        payer.setIdentity("2088102177171733");
        model.setPayerInfo(payer);

        Participant payee=new Participant();
        payee.setIdentityType("ALIPAY_LOGON_ID");
        payee.setIdentity("lqinon9872@sandbox.com");
        payee.setName("沙箱环境");
        model.setPayeeInfo(payee);

        AlipayFundTransUniTransferRequest request=new AlipayFundTransUniTransferRequest();
        request.setBizModel(model);

        AlipayFundTransUniTransferResponse response=alipayClient.execute(request);
        return response.getBody();
    }

    @RequestMapping("/transferQuery")
    public String query() throws Exception{
        AlipayFundTransCommonQueryModel model=new AlipayFundTransCommonQueryModel();
        model.setOutBizNo("10000");
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setBizScene("DIRECT_TRANSFER");

        AlipayFundTransCommonQueryRequest request=new AlipayFundTransCommonQueryRequest();
        request.setBizModel(model);

        AlipayFundTransCommonQueryResponse response=alipayClient.execute(request);
        return response.getBody();
    }
}
