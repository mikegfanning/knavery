package org.code_revue.knavery.persistence;

import org.code_revue.dhcp.server.SetBasedAddressPool;
import org.code_revue.knavery.tags.ByteArrayUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllPools", query = "select p from KnaveryAddressPool p")
})
public class KnaveryAddressPool extends SetBasedAddressPool {

    private int poolId;

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
    public Set<Integer> getExcludedAddresses() {
        Set<Integer> answer = new HashSet<>();
        for (byte[] data: super.getExclusions()) {
            answer.add(ByteArrayUtils.byteArrayToInt(data));
        }
        return answer;
    }

    public void setExcludedAddresses(Set<Integer> exclusions) {
        for (int e: exclusions) {
            super.addExclusion(ByteArrayUtils.intToByteArray(e));
        }
    }

}
