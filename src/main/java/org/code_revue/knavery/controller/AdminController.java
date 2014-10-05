package org.code_revue.knavery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/")
    public String overview() {
        return "overview";
    }

    @RequestMapping("/dhcp")
    public String dhcp() {
        return "dhcp";
    }

    @RequestMapping("/dns")
    public String dns() {
        return "dns";
    }

    @RequestMapping("/redirect")
    public String redirect() {
        return "redirect";
    }

}
