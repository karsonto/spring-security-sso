package com.karson.sso.server.controller;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {
    @GetMapping("/")
    public ModelAndView home(Map<String, Object> model, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("home");
        return view;
    }

    @GetMapping("/login")
    public ModelAndView login(Map<String, Object> model, HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        view.setViewName("login");
        return view;
    }

    @RequestMapping("/oauth/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("clientName", authorizationRequest.getClientId());
        return view;
    }

        @RequestMapping("/user")
        @ResponseBody
        public Principal getCurrentUser(Principal principal) {
            return principal;
        }

}