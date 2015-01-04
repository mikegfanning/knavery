package org.code_revue.knavery.persistence;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author Mike Fanning
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllRedirects", query = "select r from Redirect r")
})
public class Redirect {

    @Id
    private String url;

    // Maybe add weight? Other metadata about the redirect?

    public Redirect() { }

    public Redirect(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Redirect redirect = (Redirect) o;

        if (url != null ? !url.equals(redirect.url) : redirect.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return url;
    }

}
