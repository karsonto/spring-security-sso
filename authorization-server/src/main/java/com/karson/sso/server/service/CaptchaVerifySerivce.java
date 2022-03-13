package com.karson.sso.server.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class CaptchaVerifySerivce {
    public boolean checkcode(
            HttpSession session,
            String checkCode){
        // 获得验证码对象
        Object cko = session.getAttribute("simpleCaptcha");
        String captcha = cko.toString();
        if (StringUtils.isEmpty(checkCode) || captcha == null || !captcha.equalsIgnoreCase(checkCode)) {
            return false;
        }
        Date now = new Date();
        Long codeTime = Long.valueOf(session.getAttribute("codeTime") + "");
        if ((now.getTime() - codeTime) / 1000 / 60 > 1) {
            return false;
        } else {
            return true;
        }

    }
}
