package nl.topicus.heroku.wicket;

import nl.topicus.heroku.wicket.service.RequestNumberService;
import nl.topicus.heroku.wicket.service.SessionNumberService;
import nl.topicus.heroku.wicket.service.ApplicationNumberService;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.inject.Inject;

public class HomePage extends WebPage {
    @Inject
    RequestNumberService requestScopedService;
    @Inject
    SessionNumberService sessionScopedService;
    @Inject
    ApplicationNumberService applicationScopedService;

    public HomePage(final PageParameters parameters) {
		add(new Label("request", requestScopedService.getValue()));
		add(new Label("session", sessionScopedService.getValue()));
		add(new Label("application", applicationScopedService.getValue()));
    }
}
