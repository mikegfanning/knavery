package org.code_revue.knavery.domain;

import org.code_revue.dns.server.engine.AddressRegexResolverRule;

import javax.persistence.*;

/**
 * @author Mike Fanning
 */
@Entity
public class RegexResolverRuleAdapter extends AddressRegexResolverRule {

    private int ruleId;

    public RegexResolverRuleAdapter() {
        super("", null);
    }

    @Id
    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    @Column
    public String getRegex() {
        return super.getRegex();
    }

    @ManyToOne
    public AbstractResolver getResolver() {
        return (AbstractResolver) super.getResolver();
    }

    public void setResolver(AbstractResolver resolver) {
        super.setResolver(resolver);
    }

}
