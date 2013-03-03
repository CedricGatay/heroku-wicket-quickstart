package nl.topicus.heroku.wicket;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomePage2 extends HomePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage2.class);

    public HomePage2(final PageParameters parameters) {
        super(parameters);
    }

    protected void createLink(final String wicketId){
        add(new Link(wicketId){
            @Override
            public void onClick() {
                setResponsePage(new HomePage(null));
            }
        });
    }


}
