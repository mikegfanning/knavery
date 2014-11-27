package org.code_revue.knavery.controller;

import org.code_revue.dhcp.message.ByteArrayOption;
import org.code_revue.dhcp.message.DhcpOptionType;
import org.code_revue.dhcp.server.DhcpEngine;
import org.code_revue.dhcp.server.DhcpServer;
import org.code_revue.dhcp.server.StandardEngine;
import org.code_revue.dhcp.server.StandardIp4AddressPool;
import org.code_revue.knavery.service.StringConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.Executor;

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
    private DhcpServer dhcpServer;

    @Autowired
    private StandardEngine dhcpEngine;

    @Autowired
    private StandardIp4AddressPool dhcpAddressPool;

    @PostConstruct
    public void init() throws IOException {
        dhcpServer.start();
    }

    @PreDestroy
    public void destroy() throws IOException {
        dhcpServer.stop();
    }

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
    public String dhcpAddressPoolExclusionAdd(@RequestParam int[] exclusion, Model model) {
        logger.debug("Adding DHCP address pool exclusion {}", exclusion);
        byte[] temp = new byte[exclusion.length];
        for (int i = 0; i < exclusion.length; i++) {
            temp[i] = (byte) exclusion[i];
        }
        dhcpAddressPool.addExclusion(temp);
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
    public String dhcpEngine(Model model) {
        model.addAttribute("engine", dhcpEngine);
        return "dhcp-engine";
    }

    @RequestMapping("/engine/configuration/update")
    public String dhcpConfigurationUpdate(@RequestParam DhcpOptionType optionType, @RequestParam String data,
                                          Model model) {
        dhcpEngine.setConfiguration(new ByteArrayOption(optionType, stringConverterService.convertToByteArray(data)));
        return dhcpEngine(model);
    }

}
