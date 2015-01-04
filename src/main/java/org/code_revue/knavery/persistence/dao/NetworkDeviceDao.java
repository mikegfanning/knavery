package org.code_revue.knavery.persistence.dao;

import org.code_revue.dhcp.device.DeviceRegistry;
import org.code_revue.dhcp.device.DeviceStatus;
import org.code_revue.dhcp.device.NetworkDevice;
import org.code_revue.knavery.persistence.NetworkDeviceAdapter;
import org.code_revue.knavery.tags.ByteArrayUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Repository
public class NetworkDeviceDao implements DeviceRegistry {

    private Logger logger = LoggerFactory.getLogger(NetworkDeviceDao.class);

    private SessionFactory sessionFactory;

    @Override
    @Transactional(readOnly = true)
    public Collection<NetworkDevice> getAllDevices() {
        Session session = sessionFactory.getCurrentSession();
        return session.getNamedQuery("findAllNetworkDevices").list();
    }

    @Override
    @Transactional
    public NetworkDevice getDevice(byte[] hardwareAddress) {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("findNetworkDevice");
        q.setString("deviceId", ByteArrayUtils.byteArrayToNumericString(hardwareAddress));
        List<NetworkDeviceAdapter> devices = q.list();
        if (devices.isEmpty()) {
            logger.debug("Device not found, creating new record for {}", hardwareAddress);
            NetworkDeviceAdapter device = new NetworkDeviceAdapter();
            device.setStatus(DeviceStatus.DISCOVERED);
            device.setHardwareAddress(hardwareAddress);
            session.saveOrUpdate(device);
            return device;
        } else {
            return devices.get(0);
        }
    }

    @Override
    @Transactional
    public NetworkDevice resetDevice(byte[] hardwareAddress) {
        Session session = sessionFactory.getCurrentSession();
        NetworkDevice device = getDevice(hardwareAddress);
        device.setStatus(DeviceStatus.DISCOVERED);
        session.saveOrUpdate(device);
        return device;
    }

    @Override
    @Transactional
    public NetworkDevice updateDevice(NetworkDevice networkDevice) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(networkDevice);
        return networkDevice;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
