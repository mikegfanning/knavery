package org.code_revue.knavery.service;

import org.code_revue.knavery.domain.Redirect;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mike Fanning
 */
@Service
public class RedirectService {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Random random = new Random(System.nanoTime());

    private List<Redirect> redirects = new ArrayList<>();

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        final Session session = sessionFactory.openSession();
        try {
            List<Redirect> redirectList = session.getNamedQuery("findAllRedirects").list();
            redirects.addAll(redirectList);
        } finally {
            session.close();
        }
    }

    @Transactional
    public boolean addRedirectUrl(String url) {
        // TODO: Validate url
        Session session = sessionFactory.getCurrentSession();
        Redirect redirect = new Redirect(url);
        session.save(redirect);
        redirects.add(redirect);
        return true;
    }

    @Transactional
    public boolean removeRedirectUrl(String url) {
        boolean answer;
        Session session = sessionFactory.getCurrentSession();
        Redirect redirect = new Redirect(url);
        session.delete(redirect);
        answer = redirects.remove(redirect);
        return answer;
    }

    public List<String> getAllUrls() {
        // Not super efficient, but this should be a small list that is only retrieved from the admin console.
        List<String> answer = new ArrayList<>();
        for (Redirect r: redirects) {
            answer.add(r.getUrl());
        }
        return answer;
    }

    public String getRandomUrl() {
        int size = redirects.size();
        if (size > 0) {
            String url = redirects.get(random.nextInt(size)).getUrl();
            return url;
        } else {
            throw new IllegalStateException("No URLs configured!");
        }
    }

}
