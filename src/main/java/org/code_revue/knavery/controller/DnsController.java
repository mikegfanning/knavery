package org.code_revue.knavery.controller;

import org.code_revue.dns.server.DnsServer;
import org.code_revue.dns.server.connector.DatagramConnector;
import org.code_revue.dns.server.engine.AddressRegexResolverRule;
import org.code_revue.dns.server.engine.ResolverChain;
import org.code_revue.dns.server.engine.StandardEngine;
import org.code_revue.dns.server.resolver.DnsResolver;
import org.code_revue.dns.server.resolver.NullResolver;
import org.code_revue.dns.server.resolver.SingleHostResolver;
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
import java.util.Collections;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin/dns")
public class DnsController {

    private static final Logger logger = LoggerFactory.getLogger(DnsController.class);

    @Autowired
    private DnsServer dnsServer;

    @Autowired
    private DatagramConnector dnsConnector;

    @Autowired
    private StandardEngine dnsEngine;

    @Autowired
    private SingleHostResolver singleHostResolver;

    @Autowired
    private ResolverChain resolverChain;

    @Autowired
    private StringConverterService stringConverterService;

    @PostConstruct
    public void init() throws IOException {
        dnsEngine.start();
        dnsConnector.start();
        dnsServer.start();
    }

    @PreDestroy
    public void destroy() throws IOException {
        dnsServer.stop();
        dnsConnector.stop();
        dnsEngine.stop();
    }

    @RequestMapping
    public String dns() {
        return "dns";
    }

    @RequestMapping("/connectors")
    public String dnsConnectors(Model model) {
        model.addAttribute("connectors", Collections.singletonList(dnsConnector));
        return "dns-connectors";
    }

    @RequestMapping("/engine")
    public String dnsEngine(Model model) {
        model.addAttribute("singleHostResolver", singleHostResolver);
        return "dns-engine";
    }

    @RequestMapping("/resolver-chain")
    public String dnsResolverChain(Model model) {
        model.addAttribute("resolverChain", resolverChain);
        return "dns-resolver-chain";
    }

    @RequestMapping(value = "/resolver-chain/add", method = RequestMethod.POST)
    public String addAddressRegexResolverRule(@RequestParam String regex, @RequestParam String type, Model model) {
        DnsResolver resolver;
        if ("single".equals(type)) {
            resolver = singleHostResolver;
        } else {
            resolver = new NullResolver();
        }
        resolverChain.addRule(new AddressRegexResolverRule(regex, resolver));
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/resolver-chain/move", method = RequestMethod.POST)
    public String moveResolverRule(@RequestParam Integer index, @RequestParam Boolean direction, Model model) {
        resolverChain.moveRule(index, direction);
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/resolver-chain/remove", method = RequestMethod.POST)
    public String removeResolverChainEntry(@RequestParam Integer index, Model model) {
        resolverChain.removeRule(index);
        return dnsResolverChain(model);
    }

    @RequestMapping(value = "/single-host-resolver/exception/add", method = RequestMethod.POST)
    public String addResolverException(@RequestParam String exception, Model model) {
        if (null == exception || "".equals(exception)) {
            throw new IllegalArgumentException("Exception must not be blank.");
        }

        singleHostResolver.addException(exception);
        return dnsEngine(model);
    }

    @RequestMapping(value = "/single-host-resolver/exception/remove", method = RequestMethod.POST)
    public String removeResolverException(@RequestParam String exception, Model model) {
        singleHostResolver.removeException(exception);
        return dnsEngine(model);
    }

    @RequestMapping(value = "/single-host-resolver/update", method = RequestMethod.POST)
    public String updateSingleHostResolver(@RequestParam String hostIp, Model model) {
        if (!stringConverterService.isIpAddress(hostIp)) {
            throw new IllegalArgumentException("Invalid IP Address.");
        }
        singleHostResolver.setHostIp(stringConverterService.convertToByteArray(hostIp));
        return dnsEngine(model);
    }

}
