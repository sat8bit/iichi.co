package co.iichi.controller;

import co.iichi.SessionHandler;
import co.iichi.common.exception.InternalServerErrorException;
import co.iichi.domain.User;
import co.iichi.domain.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "accounts")
public class AccountsController {

    @Autowired
    protected SessionHandler sessionHandler;

    @Autowired
    protected UserManager userManager;

    Logger logger = LoggerFactory.getLogger(AccountsController.class);

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ModelAndView getIndex(
    ) {
        return new ModelAndView("accounts/index");
    }

    @RequestMapping(path = "edit/nickname", method = RequestMethod.GET)
    public ModelAndView getEditNickname(
    ) {
        return new ModelAndView("accounts/edit/nickname");
    }

    @RequestMapping(path = "edit/nickname", method = RequestMethod.POST)
    public View postEditNickname(
            HttpServletRequest httpServletRequest,
            @RequestParam String nickname
    ) throws InternalServerErrorException {
        Optional<User> userOptional = sessionHandler.getUser(httpServletRequest);

        if (!userOptional.isPresent()) {
            throw new InternalServerErrorException();
        }



        User user = userOptional.get();
        user.setNickname(nickname);
        userManager.update(user);

        return new RedirectView("/accounts");
    }
}
