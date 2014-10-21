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

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin/dhcp")
public class DhcpController {

    private static final Logger logger = LoggerFactory.getLogger(DhcpController.class);

    @Autowired
    private StandardIp4AddressPool dhcpAddressPool;

    @RequestMapping
    public String dhcp() {
        return "dhcp";
    }

    @RequestMapping("/address-pool")
    public String dhcpAddressPool(Model model) {
        model.addAttribute("addressPool", dhcpAddressPool);
        return "dhcp-address-pool";
    }

    @RequestMapping(value = "/address-pool/exclusion/add", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionAdd(@RequestParam byte[] exclusion, Model model) {
        logger.debug("Adding DHCP address pool exclusion {}", exclusion);
        dhcpAddressPool.addExclusion(exclusion);
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/address-pool/exclusion/remove", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionRemove(@RequestParam byte[] exclusion, Model model) {
        logger.debug("Removing DHCP address pool exclusion {}", exclusion);
        dhcpAddressPool.removeExclusion(exclusion);
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/address-pool/update", method = RequestMethod.POST)
    public String dhcpAddressPoolUpdate(@RequestParam byte[] start, @RequestParam byte[] end, Model model) {
        // TODO: Update start and end address - need to update DHCP library
        return dhcpAddressPool(model);
    }

    @RequestMapping("/engine")
    public String dhcpEngine() {
        return "dhcp-engine";
    }

}
