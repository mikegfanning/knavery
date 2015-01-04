package org.code_revue.knavery.controller;

import org.code_revue.knavery.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mike Fanning
 */
@Controller
public class RedirectController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);

    @Autowired
    private RedirectService redirectService;

    @RequestMapping("/*")
    public String redirect(HttpServletRequest request) {
        String url = redirectService.getRandomUrl();
        logger.debug("Redirecting sucker on IP {} from {} to {}", request.getRemoteAddr(), request.getRequestURI(),
                url);
        return "redirect:" + url;
    }

}
