package org.code_revue.knavery.controller;

import org.code_revue.knavery.service.DnsService;
import org.code_revue.knavery.service.StringConverterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin/dns")
public class DnsController {

    private static final Logger logger = LoggerFactory.getLogger(DnsController.class);

    @Autowired
    private DnsService dnsService;

    @Autowired
    private StringConverterService stringConverterService;

    @RequestMapping
    public String dns() {
        return "dns";
    }

    @RequestMapping("/connectors")
    public String dnsConnectors(Model model) {
        model.addAttribute("connectors", Collections.singletonList(dnsService.getDnsConnector()));
        return "dns-connectors";
    }

    @RequestMapping("/engine")
    public String dnsEngine(Model model) {
        model.addAttribute("singleHostResolver", dnsService.getSingleHostResolver());
        return "dns-engine";
    }

    @RequestMapping("/resolver-chain")
    public String dnsResolverChain(Model model) {
        model.addAttribute("resolverChain", dnsService.getResolverChain());
        return "dns-resolver-chain";
    }

    @RequestMapping(value = "/resolver-chain/add", method = RequestMethod.POST)
    public String addAddressRegexResolverRule(@RequestParam String regex, @RequestParam String type, Model model) {
        if ("single".equals(type)) {
            dnsService.addSingleHostResolver(regex);
        } else {
            dnsService.addNullHostResolver(regex);
        }
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/resolver-chain/move", method = RequestMethod.POST)
    public String moveResolverRule(@RequestParam Integer index, @RequestParam Boolean direction, Model model) {
        dnsService.moveResolverChainRule(index, direction);
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/resolver-chain/remove", method = RequestMethod.POST)
    public String removeResolverChainEntry(@RequestParam Integer index, Model model) {
        dnsService.removeResolverChainRule(index);
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/single-host-resolver/exception/add", method = RequestMethod.POST)
    public String addResolverException(@RequestParam String exception, Model model) {
        if (null == exception || "".equals(exception)) {
            throw new IllegalArgumentException("Exception must not be blank.");
        }

        dnsService.addResolverException(exception);
        return dnsEngine(model);
    }

    @RequestMapping(value = "/single-host-resolver/exception/remove", method = RequestMethod.POST)
    public String removeResolverException(@RequestParam String exception, Model model) {
        dnsService.removeResolverException(exception);
        return dnsEngine(model);
    }

    @RequestMapping(value = "/single-host-resolver/update", method = RequestMethod.POST)
    public String updateSingleHostResolver(@RequestParam String hostIp, Model model) {
        if (!stringConverterService.isIpAddress(hostIp)) {
            throw new IllegalArgumentException("Invalid IP Address.");
        }
        dnsService.setSingleHostResolverIp(stringConverterService.convertToByteArray(hostIp));
        return dnsEngine(model);
    }

}
