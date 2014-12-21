package org.code_revue.knavery.domain;

import org.code_revue.dhcp.server.StandardIp4AddressPool;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllPools", query = "select new org.code_revue.knavery.domain.KnaveryAddressPool(p.start, " +
            "p.end) from KnaveryAddressPool p")
})
public class KnaveryAddressPool extends StandardIp4AddressPool {

    private int poolId;

    public KnaveryAddressPool() {
        super(new byte[]{0, 0, 0, 0}, new byte[]{0, 0, 0, 0});
    }

    public KnaveryAddressPool(byte[] start, byte[] end) {
        super(start, end);
    }

    @Id
    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    @Override
    @Column
    public byte[] getStart() {
        return super.getStart();
    }

    @Override
    @Column
    public byte[] getEnd() {
        return super.getEnd();
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public Set<byte[]> getExclusions() {
        Set<byte[]> answer = new HashSet<>();
        for (byte[] data: super.getExclusions()) {
            answer.add(data);
        }
        return answer;
    }

    public void setExclusions(Set<byte[]> exclusions) {
        for (byte[] e: exclusions) {
            super.addExclusion(e);
        }
    }

}
