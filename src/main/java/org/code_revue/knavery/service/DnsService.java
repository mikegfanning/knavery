package org.code_revue.knavery.service;

import org.code_revue.dns.server.DnsServer;
import org.code_revue.dns.server.connector.DatagramConnector;
import org.code_revue.dns.server.engine.AddressRegexResolverRule;
import org.code_revue.dns.server.engine.ResolverChain;
import org.code_revue.dns.server.engine.ResolverRule;
import org.code_revue.dns.server.engine.StandardEngine;
import org.code_revue.dns.server.resolver.DnsResolver;
import org.code_revue.dns.server.resolver.SingleHostResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Mike Fanning
 */
@Service
public class DnsService {

    private DnsServer dnsServer;

    private DatagramConnector dnsConnector;

    private StandardEngine dnsEngine;

    private SingleHostResolver singleHostResolver;

    private ResolverChain resolverChain;

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

    public ResolverChain addResolverChainRule(String regex) {
        return addResolverChainRule(regex, singleHostResolver);
    }

    public ResolverChain addResolverChainRule(String regex, DnsResolver resolver) {
        return addResolverChainRule(new AddressRegexResolverRule(regex, resolver));
    }

    public ResolverChain addResolverChainRule(ResolverRule rule) {
        return resolverChain.addRule(rule);
    }

    public void moveResolverChainRule(int index, boolean direction) {
        resolverChain.moveRule(index, direction);
    }

    public ResolverRule removeResolverChainRule(int index) {
        return resolverChain.removeRule(index);
    }

    public void addResolverException(String exception) {
        singleHostResolver.addException(exception);
    }

    public void removeResolverException(String exception) {
        singleHostResolver.removeException(exception);
    }

    public void setSingleHostResolverIp(byte[] address) {
        singleHostResolver.setHostIp(address);
    }

    public DnsServer getDnsServer() {
        return dnsServer;
    }

    @Autowired
    public void setDnsServer(DnsServer dnsServer) {
        this.dnsServer = dnsServer;
    }

    public DatagramConnector getDnsConnector() {
        return dnsConnector;
    }

    @Autowired
    public void setDnsConnector(DatagramConnector dnsConnector) {
        this.dnsConnector = dnsConnector;
    }

    public StandardEngine getDnsEngine() {
        return dnsEngine;
    }

    @Autowired
    public void setDnsEngine(StandardEngine dnsEngine) {
        this.dnsEngine = dnsEngine;
    }

    public SingleHostResolver getSingleHostResolver() {
        return singleHostResolver;
    }

    @Autowired
    public void setSingleHostResolver(SingleHostResolver singleHostResolver) {
        this.singleHostResolver = singleHostResolver;
    }

    public ResolverChain getResolverChain() {
        return resolverChain;
    }

    @Autowired
    public void setResolverChain(ResolverChain resolverChain) {
        this.resolverChain = resolverChain;
    }
}
