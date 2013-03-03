package nl.topicus.heroku.wicket;

import nl.topicus.heroku.wicket.service.ApplicationNumberService;
import nl.topicus.heroku.wicket.service.RequestNumberService;
import nl.topicus.heroku.wicket.service.SessionNumberService;
import nl.topicus.heroku.wicket.jpa.entities.Toto;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.time.Duration;
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
            LOGGER.error("Unable to query the database : {}", e.getMessage());
        }
        add(new Label("request", requestScopedService.getValue()));
		add(new Label("session", sessionScopedService.getValue()));
		add(new Label("application", applicationScopedService.getValue()));
        createLink("link");
    }

    protected void createLink(final String wicketId){
        add(new Link(wicketId){
            @Override
            public void onClick() {
                setResponsePage(new HomePage2(null));
            }
        });
    }


}
