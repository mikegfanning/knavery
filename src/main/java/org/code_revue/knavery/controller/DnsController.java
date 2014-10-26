package org.code_revue.knavery.controller;

import org.code_revue.dns.server.DnsServer;
import org.code_revue.dns.server.connector.DatagramConnector;
import org.code_revue.dns.server.connector.DnsConnector;
import org.code_revue.dns.server.engine.*;
import org.code_revue.dns.server.resolver.DnsResolver;
import org.code_revue.dns.server.resolver.NullResolver;
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
import java.util.concurrent.Executor;

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
    private DnsResolver localhostResolver;

    @Autowired
    private ResolverChain resolverChain;

    @Autowired
    private Executor executor;

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
    public String dnsEngine() {
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
        if ("localhost".equals(type)) {
            resolver = localhostResolver;
        } else {
            resolver = new NullResolver();
        }
        resolverChain.addRule(new AddressRegexResolverRule(regex, resolver));
        return dnsResolverChain(model);
    }

    @RequestMapping("/resolver-chain/remove")
    public String removeResolverChainEntry(Integer index, Model model) {
        resolverChain.removeRule(index);
        return dnsResolverChain(model);
    }

}
