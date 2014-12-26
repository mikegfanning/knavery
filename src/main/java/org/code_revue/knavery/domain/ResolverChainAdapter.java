package org.code_revue.knavery.domain;

import org.code_revue.dns.server.engine.ResolverChain;
import org.code_revue.dns.server.engine.ResolverRule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllResolverChains", query = "select c from ResolverChainAdapter c")
})
public class ResolverChainAdapter extends ResolverChain {

    private int chainId;

    @Id
    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    @OneToMany(fetch = FetchType.EAGER)
    @OrderColumn
    public List<RegexResolverRuleAdapter> getRegexResolverRules() {
        List<RegexResolverRuleAdapter> rules = new ArrayList<>();
        for (ResolverRule r: super.getResolverRules()) {
            rules.add((RegexResolverRuleAdapter) r);
        }
        return rules;
    }

    public void setRegexResolverRules(List<RegexResolverRuleAdapter> rules) {
        for (RegexResolverRuleAdapter r: rules) {
            super.addRule(r);
        }
    }

}
