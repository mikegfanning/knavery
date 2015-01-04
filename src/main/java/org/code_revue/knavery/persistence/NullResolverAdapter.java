package org.code_revue.knavery.persistence;

import org.code_revue.dns.message.DnsQuestion;
import org.code_revue.dns.message.DnsRecord;
import org.code_revue.dns.server.resolver.NullResolver;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllNullResolvers", query = "select r from NullResolverAdapter r")
})
public class NullResolverAdapter extends AbstractResolver {

    private NullResolver nullResolver = new NullResolver();

    @Override
    public List<DnsRecord> resolve(DnsQuestion dnsQuestion) {
        return nullResolver.resolve(dnsQuestion);
    }
}
