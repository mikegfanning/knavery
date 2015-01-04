package org.code_revue.knavery.persistence;

import org.code_revue.dns.server.engine.AddressRegexResolverRule;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Mike Fanning
 */
@Entity
public class RegexResolverRuleAdapter extends AddressRegexResolverRule {

    private String ruleId;

    public RegexResolverRuleAdapter() {
        super("", null);
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
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
