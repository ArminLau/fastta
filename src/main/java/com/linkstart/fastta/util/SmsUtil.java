package com.linkstart.fastta.util;

/**
 * @Author: Armin
 * @Date: 2023/3/19
 * @Description: 短信处理的工具类
 */

import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import java.util.*;
import com.aliyuncs.dysmsapi.model.v20170525.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Slf4j
public class SmsUtil {

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String msgTemplate;

    private static boolean enableSms;

    public void setAccessKeyId(String accessKeyId){
        SmsUtil.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret){
        SmsUtil.accessKeySecret = accessKeySecret;
    }

    public void setMsgTemplate(String template){
        SmsUtil.msgTemplate = template;
    }

    public void setEnable(boolean enable){
        SmsUtil.enableSms = enable;
    }

    /**
     * 发送给指定的电话用户发送指定位数的验证码
     * @param phoneNumbers 电话号码
     * @param codeDigit 验证码的位数(仅支持4位或者6位)
     * @return
     */
    public static String sendMessage(String phoneNumbers, int codeDigit) throws ServerException,ClientException{
        //验证码仅支持4位或者6位
        codeDigit = codeDigit == 4 ? 4 : 6;
        //随机生成的验证码
        String code = RandomUtil.randomNumbers(codeDigit);
        log.info("生成的验证码为: {}", code);

        if(enableSms){
            DefaultProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
            /** use STS Token
             DefaultProfile profile = DefaultProfile.getProfile(
             "<your-region-id>",           // The region ID
             "<your-access-key-id>",       // The AccessKey ID of the RAM account
             "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
             "<your-sts-token>");          // STS Token
             **/
            IAcsClient client = new DefaultAcsClient(profile);
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phoneNumbers);//接收短信的手机号码
            request.setSignName("极速外卖");//短信签名名称
            request.setTemplateCode(msgTemplate);//短信模板CODE
            request.setTemplateCode(code);
            SendSmsResponse response = client.getAcsResponse(request);
            boolean sendSuccess = "OK".equals(response.getCode());
            System.out.println(new Gson().toJson(response));

            if(sendSuccess){
                log.info("成功给{}发送验证码{}", phoneNumbers, code);
            }else {
                log.error("给{}发送验证码{}出现异常，发送失败", phoneNumbers, code);
            }
            //如果发送成功，返回验证码，否则返回空值
            return sendSuccess ? code : null;
        }
        return code;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(sendMessage("18888888888", 6));
    }
}