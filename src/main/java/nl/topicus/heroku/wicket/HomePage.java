package nl.topicus.heroku.wicket;

import nl.topicus.heroku.wicket.service.ApplicationNumberService;
import nl.topicus.heroku.wicket.service.RequestNumberService;
import nl.topicus.heroku.wicket.service.SessionNumberService;
import nl.topicus.heroku.wicket.jpa.entities.*;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class HomePage extends WebPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);

    @Inject
    EntityManager em;
    @Inject
    RequestNumberService requestScopedService;
    @Inject
    SessionNumberService sessionScopedService;
    @Inject
    ApplicationNumberService applicationScopedService;

    public HomePage(final PageParameters parameters) {
        try {
            em.find(Toto.class, 1L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(new Label("request", requestScopedService.getValue()));
		add(new Label("session", sessionScopedService.getValue()));
		add(new Label("application", applicationScopedService.getValue()));
    }
}
