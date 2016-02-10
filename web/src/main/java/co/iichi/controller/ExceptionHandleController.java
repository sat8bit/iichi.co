package co.iichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandleController {
    @ExceptionHandler
    public ModelAndView exceptionHandle() {
        return new ModelAndView("error");
    }
}
