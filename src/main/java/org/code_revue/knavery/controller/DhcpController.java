package org.code_revue.knavery.controller;

import org.code_revue.dhcp.message.DhcpOptionType;
import org.code_revue.knavery.service.DhcpService;
import org.code_revue.knavery.service.StringConverterService;
import org.code_revue.knavery.tags.ByteArrayUtils;
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
    private StringConverterService stringConverterService;

    @Autowired
    private DhcpService dhcpService;

    @RequestMapping
    public String dhcp() {
        return "dhcp";
    }

    @RequestMapping("/address-pool")
    public String dhcpAddressPool(Model model) {
        model.addAttribute("addressPool", dhcpService.getDhcpEngine().getAddressPool());
        return "dhcp-address-pool";
    }

    @RequestMapping(value = "/address-pool/exclusion/add", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionAdd(@RequestParam int[] exclusion, Model model) {
        logger.debug("Adding DHCP address pool exclusion {}", exclusion);
        dhcpService.addAddressPoolExclusion(ByteArrayUtils.intArrayToByteArray(exclusion));
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/address-pool/exclusion/remove", method = RequestMethod.POST)
    public String dhcpAddressPoolExclusionRemove(@RequestParam int[] exclusion, Model model) {
        logger.debug("Removing DHCP address pool exclusion {}", exclusion);
        dhcpService.removeAddressPoolExclusion(ByteArrayUtils.intArrayToByteArray(exclusion));
        return dhcpAddressPool(model);
    }

    @RequestMapping(value = "/address-pool/update", method = RequestMethod.POST)
    public String dhcpAddressPoolUpdate(@RequestParam int[] start, @RequestParam int[] end, Model model) {
        logger.debug("Modifying DHCP address pool range {} to {}", start, end);
        dhcpService.setAddressPoolRange(ByteArrayUtils.intArrayToByteArray(start),
                ByteArrayUtils.intArrayToByteArray(end));
        return dhcpAddressPool(model);
    }

    @RequestMapping("/engine")
    public String dhcpEngine(Model model) {
        model.addAttribute("engine", dhcpService.getDhcpEngine());
        model.addAttribute("dhcpOptionTypes", DhcpOptionType.values());
        return "dhcp-engine";
    }

    @RequestMapping(value = "/engine/configuration/update", method = RequestMethod.POST)
    public String dhcpConfigurationUpdate(@RequestParam DhcpOptionType optionType, @RequestParam String data,
                                          Model model) {
        dhcpService.setDhcpConfigurationOption(optionType, stringConverterService.convertToByteArray(data));
        return dhcpEngine(model);
    }

    @RequestMapping(value = "/engine/configuration/remove", method = RequestMethod.POST)
    public String dhcpConfigurationRemove(@RequestParam DhcpOptionType optionType, Model model) {
        dhcpService.removeDhcpConfigurationOption(optionType);
        return dhcpEngine(model);
    }

}
