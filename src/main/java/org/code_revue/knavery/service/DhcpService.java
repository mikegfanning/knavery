package org.code_revue.knavery.service;

import org.code_revue.dhcp.message.DhcpOption;
import org.code_revue.dhcp.message.DhcpOptionType;
import org.code_revue.dhcp.server.StandardEngine;
import org.code_revue.knavery.domain.ByteArrayDhcpOption;
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

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        Session session = sessionFactory.openSession();
        List<DhcpOption> options = session.getNamedQuery("findAllOptions").list();
        dhcpEngine.setConfigurations(options);
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
        return !options.isEmpty();
    }

    public StandardEngine getDhcpEngine() {
        return dhcpEngine;
    }

    public void setDhcpEngine(StandardEngine dhcpEngine) {
        this.dhcpEngine = dhcpEngine;
    }
}
