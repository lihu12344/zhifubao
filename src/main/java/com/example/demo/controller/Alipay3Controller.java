package com.example.demo.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.example.demo.config.AlipayProperties;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class Alipay3Controller {

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AlipayProperties alipayProperties;

    @RequestMapping("/precreate")
    public void precreate(HttpServletResponse response) throws Exception{
        AlipayTradePrecreateModel model=new AlipayTradePrecreateModel();
        model.setOutTradeNo("303");
        model.setProductCode("FACE_TO_FACE_PAYMENT");
        model.setSubject("海贼王");
        model.setTotalAmount("300");

        AlipayTradePrecreateRequest request=new AlipayTradePrecreateRequest();
        request.setBizModel(model);

        AlipayTradePrecreateResponse alipayTradePrecreateResponse=alipayClient.execute(request);
        String content=alipayTradePrecreateResponse.getQrCode();

        makeQRCode(content,response.getOutputStream());
    }

    public void makeQRCode(String content, OutputStream outputStream) throws Exception{
        Map<EncodeHintType,Object> map=new HashMap<>();
        map.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        map.put(EncodeHintType.MARGIN,2);

        QRCodeWriter qrCodeWriter=new QRCodeWriter();
        BitMatrix bitMatrix=qrCodeWriter.encode(content, BarcodeFormat.QR_CODE,200,200,map);
        MatrixToImageWriter.writeToStream(bitMatrix,"jpeg",outputStream);
    }
}
