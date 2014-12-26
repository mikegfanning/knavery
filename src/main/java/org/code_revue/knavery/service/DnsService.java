package org.code_revue.knavery.service;

import org.code_revue.dns.server.DnsServer;
import org.code_revue.dns.server.connector.DatagramConnector;
import org.code_revue.dns.server.engine.ResolverChain;
import org.code_revue.dns.server.engine.ResolverRule;
import org.code_revue.dns.server.engine.StandardEngine;
import org.code_revue.knavery.domain.NullResolverAdapter;
import org.code_revue.knavery.domain.RegexResolverRuleAdapter;
import org.code_revue.knavery.domain.ResolverChainAdapter;
import org.code_revue.knavery.domain.SingleHostResolverAdapter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Service
public class DnsService {

    private DnsServer dnsServer;

    private DatagramConnector dnsConnector;

    private StandardEngine dnsEngine;

    private SingleHostResolverAdapter singleHostResolver;

    private NullResolverAdapter nullResolver;

    private ResolverChain resolverChain;

    // TODO: Move this persistence junk to DAO.
    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void init() throws IOException {
        Session session = sessionFactory.openSession();

        List<SingleHostResolverAdapter> hostResolvers = session.getNamedQuery("findAllSingleHostResolvers").list();
        if (hostResolvers.isEmpty()) {
            singleHostResolver = new SingleHostResolverAdapter();
        } else {
            singleHostResolver = hostResolvers.get(0);
        }

        List<NullResolverAdapter> nullResolvers = session.getNamedQuery("findAllNullResolvers").list();
        if (nullResolvers.isEmpty()) {
            nullResolver = new NullResolverAdapter();
        } else {
            nullResolver = nullResolvers.get(0);
        }

        List<ResolverChainAdapter> resolverChains = session.getNamedQuery("findAllResolverChains").list();
        if (resolverChains.isEmpty()) {
            resolverChain = new ResolverChainAdapter();
        } else {
            resolverChain = resolverChains.get(0);
        }

        dnsEngine.setResolverChain(resolverChain);

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

    @Transactional
    public ResolverChain addNullHostResolver(String regex) {
        RegexResolverRuleAdapter rule = new RegexResolverRuleAdapter();
        rule.setRegex(regex);
        rule.setResolver(nullResolver);
        resolverChain.addRule(rule);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(nullResolver);
        session.saveOrUpdate(rule);
        session.saveOrUpdate(resolverChain);

        return resolverChain;
    }

    @Transactional
    public ResolverChain addSingleHostResolver(String regex) {
        RegexResolverRuleAdapter rule = new RegexResolverRuleAdapter();
        rule.setRegex(regex);
        rule.setResolver(singleHostResolver);
        resolverChain.addRule(rule);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(singleHostResolver);
        session.saveOrUpdate(rule);
        session.saveOrUpdate(resolverChain);

        return resolverChain;
    }

    @Transactional
    public void moveResolverChainRule(int index, boolean direction) {
        resolverChain.moveRule(index, direction);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(resolverChain);
    }

    @Transactional
    public ResolverRule removeResolverChainRule(int index) {
        ResolverRule rule = resolverChain.removeRule(index);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(resolverChain);

        return rule;
    }

    @Transactional
    public void addResolverException(String exception) {
        assert null != exception;
        singleHostResolver.addException(exception);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(singleHostResolver);
    }

    @Transactional
    public void removeResolverException(String exception) {
        assert null != exception;
        singleHostResolver.removeException(exception);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(singleHostResolver);
    }

    @Transactional
    public void setSingleHostResolverIp(byte[] address) {
        assert null != address;
        singleHostResolver.setHostIp(address);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(singleHostResolver);
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

    public ResolverChain getResolverChain() {
        return resolverChain;
    }

    public SingleHostResolverAdapter getSingleHostResolver() {
        return singleHostResolver;
    }
}
