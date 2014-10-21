package org.code_revue.knavery.controller;

import org.code_revue.dhcp.server.StandardIp4AddressPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource(name = "redirectUrls")
    private List<String> redirectUrls;

    @RequestMapping
    public String overview() {
        return "overview";
    }

    @RequestMapping("/redirect")
    public String redirect(Model model) {
        model.addAttribute("redirectUrls", Collections.unmodifiableList(redirectUrls));
        return "redirect";
    }

    @RequestMapping(value = "/redirect/add", method = RequestMethod.POST)
    public String addRedirect(@RequestParam String url, Model model) {
        // TODO: Validate this URL
        logger.debug("Adding redirect URL {}", url);
        redirectUrls.add(url);
        return redirect(model);
    }

    @RequestMapping(value = "/redirect/remove", method = RequestMethod.POST)
    public String removeRedirect(@RequestParam String url, Model model) {
        logger.debug("Removing redirect URL {}", url);
        redirectUrls.remove(url);
        return redirect(model);
    }

}
