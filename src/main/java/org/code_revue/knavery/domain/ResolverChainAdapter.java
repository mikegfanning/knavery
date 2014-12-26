package org.code_revue.knavery.domain;

import org.code_revue.dns.server.engine.ResolverChain;
import org.code_revue.dns.server.engine.ResolverRule;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllResolverChains", query = "select c from ResolverChainAdapter c")
})
public class ResolverChainAdapter extends ResolverChain {

    private String chainId;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    @OneToMany(fetch = FetchType.EAGER, targetEntity = RegexResolverRuleAdapter.class)
    @OrderColumn
    public List getRegexResolverRules() {
        return super.getResolverRules();
    }

    public void setRegexResolverRules(List rules) {
        super.setResolverRules((List<ResolverRule>) rules);
    }

}
