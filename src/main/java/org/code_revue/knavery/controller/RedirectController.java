package org.code_revue.knavery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

/**
 * @author Mike Fanning
 */
@Controller
public class RedirectController {

    private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);
    private static final Random random = new Random(System.nanoTime());

    @Resource(name = "redirectUrls")
    private List<String> redirectUrls;

    @RequestMapping("/*")
    public String redirect(HttpServletRequest request) {
        String url = redirectUrls.get(random.nextInt(redirectUrls.size()));
        logger.debug("Redirecting sucker on IP {} to {}", request.getRemoteAddr(), url);
        return "redirect:" + url;
    }

}
