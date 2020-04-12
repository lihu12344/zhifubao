package com.example.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.example.demo.config.AlipayProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AlipayController {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AlipayProperties alipayProperties;

    @RequestMapping("/pagePay")
    public String pagePay(){
        AlipayTradePagePayModel model=new AlipayTradePagePayModel();
        model.setOutTradeNo("108");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setSubject("海贼王");
        model.setTotalAmount("100");
        model.setTimeoutExpress("1h");

        ExtendParams extendParams=new ExtendParams();
        extendParams.setHbFqNum("3");
        extendParams.setHbFqSellerPercent("100");
        model.setExtendParams(extendParams);
        
        AlipayTradePagePayRequest request=new AlipayTradePagePayRequest();
        request.setBizModel(model);
        request.setReturnUrl(alipayProperties.getReturnUrl());
        request.setNotifyUrl(alipayProperties.getNotifyUrl());

        AlipayTradePagePayResponse response=null;
        try{
            response= alipayClient.pageExecute(request);
        }catch (Exception e){
            e.printStackTrace();
        }

        assert response != null;
        return response.getBody();
    }

    @RequestMapping("/wapPay")
    public String wapPay(){
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo("108");
        model.setProductCode("QUICK_WAP_WAY");
        model.setSubject("海贼王 wap");
        model.setTotalAmount("1000");
        model.setTimeoutExpress("2h");

        ExtendParams extendParams=new ExtendParams();
        extendParams.setHbFqNum("3");
        extendParams.setHbFqSellerPercent("100");
        model.setExtendParams(extendParams);

        AlipayTradeWapPayRequest request=new AlipayTradeWapPayRequest();
        request.setBizModel(model);
        request.setReturnUrl(alipayProperties.getReturnUrl());
        request.setNotifyUrl(alipayProperties.getNotifyUrl());

        AlipayTradeWapPayResponse response=null;
        try{
            response= alipayClient.pageExecute(request);
        }catch (Exception e){
            e.printStackTrace();
        }

        assert response != null;
        return response.getBody();
    }


    @RequestMapping("/appPay")
    public String appPay(){
        AlipayTradeAppPayModel model=new AlipayTradeAppPayModel();
        model.setOutTradeNo("108");
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setSubject("海贼王");
        model.setTotalAmount("2000");
        model.setTimeoutExpress("2h");

        AlipayTradeAppPayRequest request=new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setReturnUrl(alipayProperties.getReturnUrl());
        request.setNotifyUrl(alipayProperties.getNotifyUrl());

        AlipayTradeAppPayResponse response=null;
        try{
            response= alipayClient.sdkExecute(request);
        }catch (Exception e){
            e.printStackTrace();
        }

        assert response != null;
        return response.getBody();
    }

    @RequestMapping("/query")
    public String query() throws Exception{
        AlipayTradeQueryModel model=new AlipayTradeQueryModel();
        model.setOutTradeNo("108");

        AlipayTradeQueryRequest request=new AlipayTradeQueryRequest();
        request.setBizModel(model);

        return alipayClient.execute(request).getTradeStatus();
    }

    @RequestMapping("/close")
    public String close() throws Exception{
        AlipayTradeCloseModel model=new AlipayTradeCloseModel();
        model.setOutTradeNo("108");

        AlipayTradeCloseRequest request=new AlipayTradeCloseRequest();
        request.setBizModel(model);

        return alipayClient.execute(request).getBody();
    }

    @RequestMapping("/refund")
    public String refund() throws Exception{
        AlipayTradeRefundModel model=new AlipayTradeRefundModel();
        model.setOutTradeNo("108");
        model.setRefundAmount("20");
        model.setOutRequestNo("5");

        AlipayTradeRefundRequest refundRequest=new AlipayTradeRefundRequest();
        refundRequest.setBizModel(model);

        AlipayTradeRefundResponse refundResponse=alipayClient.execute(refundRequest);

        return "退款总金额："+refundResponse.getRefundFee();
    }

    @RequestMapping("/refundQuery")
    public String refundQuery() throws Exception{
        AlipayTradeFastpayRefundQueryModel model=new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo("108");
        model.setOutRequestNo("5");

        AlipayTradeFastpayRefundQueryRequest refundQueryRequest=new AlipayTradeFastpayRefundQueryRequest();
        refundQueryRequest.setBizModel(model);

        AlipayTradeFastpayRefundQueryResponse refundQueryResponse=alipayClient.execute(refundQueryRequest);

        return "本次退款金额为："+refundQueryResponse.getRefundAmount()+
                "\n订单总金额为："+refundQueryResponse.getTotalAmount();
    }

    @RequestMapping("/queryDownload")
    public String downloadUrl() throws Exception{
        AlipayDataDataserviceBillDownloadurlQueryModel model=new AlipayDataDataserviceBillDownloadurlQueryModel();
        model.setBillType("trade");
        model.setBillDate("2020-04-06");

        AlipayDataDataserviceBillDownloadurlQueryRequest request=new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizModel(model);

        AlipayDataDataserviceBillDownloadurlQueryResponse response=alipayClient.execute(request);

        return response.getBillDownloadUrl();
    }

    @RequestMapping("/notify")
    public void notify(HttpServletRequest request) throws Exception{
        if (check(request.getParameterMap())){
            System.out.println(request.getParameter("trade_status"));
            System.out.println("异步通知 "+ Instant.now());
        }else {
            System.out.println("验签失败");
        }
    }
    
    @RequestMapping("/return")
    public String returnUrl(HttpServletRequest request) throws Exception{
        if (check(request.getParameterMap())){
            return "success";
        }else {
            return "false";
        }
    }

    private boolean check(Map<String,String[]> requestParams) throws Exception{
        Map<String,String> params = new HashMap<>();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
            System.out.println(name+" ==> "+valueStr);
        }

        return AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(),
                alipayProperties.getCharset(), alipayProperties.getSignType()); //调用SDK验证签名
    }
}