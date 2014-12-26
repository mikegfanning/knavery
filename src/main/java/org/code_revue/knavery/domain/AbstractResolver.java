package org.code_revue.knavery.domain;

import org.code_revue.dns.server.resolver.DnsResolver;

import javax.persistence.*;

/**
 * @author Mike Fanning
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AbstractResolver implements DnsResolver {

    private int resolverId;

    @Id
    public int getResolverId() {
        return resolverId;
    }

    public void setResolverId(int resolverId) {
        this.resolverId = resolverId;
    }

}
