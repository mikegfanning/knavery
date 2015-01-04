package org.code_revue.knavery.persistence;

import org.code_revue.dhcp.device.DeviceStatus;
import org.code_revue.dhcp.device.NetworkDevice;
import org.code_revue.dhcp.message.DhcpOption;
import org.code_revue.dhcp.message.DhcpOptionType;
import org.code_revue.knavery.tags.ByteArrayUtils;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllNetworkDevices", query = "select d from NetworkDeviceAdapter d"),
        @NamedQuery(name = "findNetworkDevice", query = "select d from NetworkDeviceAdapter d where d.deviceId = :deviceId")
})
public class NetworkDeviceAdapter extends NetworkDevice {

    @Id
    public String getDeviceId() {
        return ByteArrayUtils.byteArrayToNumericString(getHardwareAddress());
    }

    public void setDeviceId(String deviceId) {
        byte[] address = new byte[6];
        ByteArrayUtils.numericStringToByteArray(deviceId, address);
        setHardwareAddress(address);
    }

    @Override
    @Column
    public byte[] getIpAddress() {
        return super.getIpAddress();
    }

    @Override
    @Column
    public Date getLeaseExpiration() {
        return super.getLeaseExpiration();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public Map<Integer, byte[]> getDhcpOptions() {
        Map<Integer, byte[]> answer = new HashMap<>();
        for (DhcpOption o: super.getOptions().values()) {
            answer.put(o.getType().getNumericCode(), o.getOptionData());
        }
        return answer;
    }

    public void setDhcpOptions(Map<Integer, byte[]> options) {
        Map<DhcpOptionType, DhcpOption> newOpts = new HashMap<>();
        for (Map.Entry<Integer, byte[]> e: options.entrySet()) {
            DhcpOptionType type = DhcpOptionType.getByNumericCode(e.getKey());
            newOpts.put(type, new ByteArrayDhcpOption(type, e.getValue()));
        }
        super.setOptions(newOpts);
    }

    @Override
    @Column
    public DeviceStatus getStatus() {
        return super.getStatus();
    }
}
