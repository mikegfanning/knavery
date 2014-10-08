package org.code_revue.knavery.controller;

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

    @Resource(name = "redirectUrls")
    private List<String> redirectUrls;

    @RequestMapping("/")
    public String overview() {
        return "overview";
    }

    @RequestMapping("/dhcp")
    public String dhcp() {
        return "dhcp";
    }

    @RequestMapping("/dhcp-address-pool")
    public String dhcpAddressPool() {
        return "dhcp-address-pool";
    }

    @RequestMapping("/dhcp-engine")
    public String dhcpEngine() {
        return "dhcp-engine";
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
    public String redirect(Model model) {
        model.addAttribute("redirectUrls", Collections.unmodifiableList(redirectUrls));
        return "redirect";
    }

    @RequestMapping(value = "/redirect/add", method = RequestMethod.POST)
    public String addRedirect(@RequestParam String url, Model model) {
        // TODO: Validate this URL
        redirectUrls.add(url);
        return redirect(model);
    }

    @RequestMapping(value = "/redirect/remove", method = RequestMethod.POST)
    public String removeRedirect(@RequestParam String url, Model model) {
        redirectUrls.remove(url);
        return redirect(model);
    }

}
