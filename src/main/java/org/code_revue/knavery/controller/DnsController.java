package org.code_revue.knavery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mike Fanning
 */
@Controller
@RequestMapping("/admin/dns")
public class DnsController {

    private static final Logger logger = LoggerFactory.getLogger(DnsController.class);

    @RequestMapping
    public String dns() {
        return "dns";
    }

    @RequestMapping("/connectors")
    public String dnsConnectors() {
        return "dns-connectors";
    }

    @RequestMapping("/engine")
    public String dnsEngine() {
        return "dns-engine";
    }

    @RequestMapping("/resolver-chain")
    public String dnsResolverChain() {
        return "dns-resolver-chain";
    }


}
