package co.iichi.web.controller;

import co.iichi.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @Autowired
    protected SessionHandler sessionHandler;

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(
    ) {
        return new ModelAndView("index");
    }

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public ModelAndView getLogin(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        sessionHandler.remove(httpServletRequest, httpServletResponse);
        return new ModelAndView("login");
    }

    @RequestMapping(path = "logout", method = RequestMethod.GET)
    public View getLogout(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        sessionHandler.remove(httpServletRequest, httpServletResponse);
        return new RedirectView("/");
    }
}
