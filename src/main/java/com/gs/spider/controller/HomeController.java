package com.gs.spider.controller;
import com.gs.spider.utils.AppInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@RequestMapping("/")
@Controller
public class HomeController {

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/welcome");
        modelAndView.addObject("appName", AppInfo.APP_NAME)
                .addObject("appVersion", AppInfo.APP_VERSION)
                .addObject("onlineDocumentation",AppInfo.ONLINE_DOCUMENTATION);
        return modelAndView;
    }
}
