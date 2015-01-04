package org.code_revue.knavery.persistence;

import org.code_revue.dns.server.resolver.DnsResolver;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Mike Fanning
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractResolver implements DnsResolver {

    private String resolverId;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getResolverId() {
        return resolverId;
    }

    public void setResolverId(String resolverId) {
        this.resolverId = resolverId;
    }

}
