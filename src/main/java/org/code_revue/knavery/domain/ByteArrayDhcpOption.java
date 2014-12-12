package org.code_revue.knavery.domain;

import org.code_revue.dhcp.message.DhcpOption;
import org.code_revue.dhcp.message.DhcpOptionType;

import javax.persistence.*;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllOptions", query = "select o from ByteArrayDhcpOption o"),
        @NamedQuery(name = "findDhcpOptionByType", query = "select o from ByteArrayDhcpOption o where o.type = :type")
})
public class ByteArrayDhcpOption implements DhcpOption {

    private DhcpOptionType type;

    private byte[] optionData;

    public ByteArrayDhcpOption() { }

    public ByteArrayDhcpOption(DhcpOptionType type, byte[] optionData) {
        this.type = type;
        this.optionData = optionData;
    }

    @Override
    @Id
    public DhcpOptionType getType() {
        return type;
    }

    public void setType(DhcpOptionType type) {
        this.type = type;
    }

    @Override
    @Column
    public byte[] getOptionData() {
        return optionData;
    }

    public void setOptionData(byte[] optionData) {
        this.optionData = optionData;
    }
}
