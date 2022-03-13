package com.karson.sso.server.filter;

import com.karson.sso.server.service.CaptchaVerifySerivce;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private String codeParameter = "code";
    CaptchaVerifySerivce captchaVerifySerivce;

    public LoginFilter(CaptchaVerifySerivce captchaVerifySerivce) {
        super();
        this.captchaVerifySerivce = captchaVerifySerivce;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String code = this.obtainCode(request);
        HttpSession session = request.getSession(false);
        boolean checkcode = captchaVerifySerivce.checkcode(session, code);
        if(!checkcode){
            try {
                response.sendRedirect("/login?code=error");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  throw new BadCredentialsException("Parameter code is required.") ;
        }
        return super.attemptAuthentication(request, response);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }


}
