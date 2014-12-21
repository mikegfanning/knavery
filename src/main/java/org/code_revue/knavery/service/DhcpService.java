package org.code_revue.knavery.service;

import org.code_revue.dhcp.message.DhcpOption;
import org.code_revue.dhcp.message.DhcpOptionType;
import org.code_revue.dhcp.server.StandardEngine;
import org.code_revue.knavery.domain.ByteArrayDhcpOption;
import org.code_revue.knavery.domain.KnaveryAddressPool;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Service
public class DhcpService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private StandardEngine dhcpEngine;

    private KnaveryAddressPool addressPool;

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        Session session = sessionFactory.openSession();
        List<DhcpOption> options = session.getNamedQuery("findAllOptions").list();
        dhcpEngine.setConfigurations(options);

        List<KnaveryAddressPool> pools = session.getNamedQuery("findAllPools").list();
        if (pools.isEmpty()) {
            addressPool = new KnaveryAddressPool();
        } else {
            addressPool = pools.get(0);
        }
        dhcpEngine.setAddressPool(addressPool);
    }

    @Transactional
    public void addAddressPoolExclusion(byte[] exclusion) {
        assert null != exclusion;
        addressPool.addExclusion(exclusion);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(addressPool);
    }

    @Transactional
    public void removeAddressPoolExclusion(byte[] exclusion) {
        assert null != exclusion;
        addressPool.removeExclusion(exclusion);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(addressPool);
    }

    @Transactional
    public void setAddressPoolRange(byte[] start, byte[] end) {
        assert null != start && null != end;
        addressPool.setStart(start);
        addressPool.setEnd(end);
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(addressPool);
    }

    @Transactional
    public void setDhcpConfigurationOption(DhcpOptionType optionType, byte[] data) {
        assert null != optionType && null != data;
        Session session = sessionFactory.getCurrentSession();
        ByteArrayDhcpOption option = new ByteArrayDhcpOption(optionType, data);
        session.saveOrUpdate(option);
        dhcpEngine.setConfiguration(option);
    }

    @Transactional
    public boolean removeDhcpConfigurationOption(DhcpOptionType optionType) {
        assert null != optionType;
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("findDhcpOptionByType").setInteger("type", optionType.getNumericCode());
        List<ByteArrayDhcpOption> options = q.list();
        for (ByteArrayDhcpOption option: options) {
            session.delete(option);
        }
        dhcpEngine.removeConfiguration(optionType);
        return !options.isEmpty();
    }

    public StandardEngine getDhcpEngine() {
        return dhcpEngine;
    }

    public void setDhcpEngine(StandardEngine dhcpEngine) {
        this.dhcpEngine = dhcpEngine;
    }

}
