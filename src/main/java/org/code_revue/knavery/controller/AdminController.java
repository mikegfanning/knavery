package org.code_revue.knavery.controller;

import org.code_revue.dhcp.server.StandardIp4AddressPool;
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

    @Resource(name = "redirectUrls")
    private List<String> redirectUrls;

    @Autowired
    private StandardIp4AddressPool dhcpAddressPool;

    @RequestMapping("/")
    public String overview() {
        return "overview";
    }

    @RequestMapping("/dhcp")
    public String dhcp() {
        return "dhcp";
    }

    @RequestMapping("/dhcp-address-pool")
    public String dhcpAddressPool(Model model) {
        model.addAttribute("addressPool", dhcpAddressPool);
        return "dhcp-address-pool";
    }

    @RequestMapping(value = "/dhcp-address-pool/exclusion/add", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionAdd(@RequestParam byte[] exclusion, Model model) {
        dhcpAddressPool.addExclusion(exclusion);
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/dhcp-address-pool/exclusion/remove", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionRemove(@RequestParam byte[] exclusion, Model model) {
        dhcpAddressPool.removeExclusion(exclusion);
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/dhcp-address-pool/update", method = RequestMethod.POST)
    public String dhcpAddressPoolUpdate(@RequestParam byte[] start, @RequestParam byte[] end, Model model) {
        // TODO: Update start and end address - need to update DHCP library
        return dhcpAddressPool(model);
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
