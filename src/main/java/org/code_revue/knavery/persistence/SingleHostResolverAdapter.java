package org.code_revue.knavery.persistence;

import org.code_revue.dns.message.DnsQuestion;
import org.code_revue.dns.message.DnsRecord;
import org.code_revue.dns.server.resolver.SingleHostResolver;

import javax.persistence.*;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllSingleHostResolvers", query = "select r from SingleHostResolverAdapter r")
})
public class SingleHostResolverAdapter extends AbstractResolver {

    private SingleHostResolver resolver;

    public SingleHostResolverAdapter() throws UnknownHostException {
        resolver = new SingleHostResolver();
    }

    @Column
    public byte[] getHostIp() {
        return resolver.getHostIp();
    }

    public void setHostIp(byte[] address) {
        resolver.setHostIp(address);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> getExceptionList() {
        return resolver.getExceptionList();
    }

    public void setExceptionList(List<String> exceptionList) {
        resolver.setExceptionList(exceptionList);
    }

    public void addException(String exception) {
        resolver.addException(exception);
    }

    public void removeException(String exception) {
        resolver.removeException(exception);
    }

    @Override
    public List<DnsRecord> resolve(DnsQuestion dnsQuestion) {
        return resolver.resolve(dnsQuestion);
    }
}
