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

    @RequestMapping("/dns-connectors")
    public String dnsConnectors() {
        return "dns-connectors";
    }

    @RequestMapping("/dns-engine")
    public String dnsEngine() {
        return "dns-engine";
    }

    @RequestMapping("/dns-resolver-chain")
    public String dnsResolverChain() {
        return "dns-resolver-chain";
    }

    @RequestMapping("/redirect")
    public String redirect() {
        return "redirect";
    }

}
