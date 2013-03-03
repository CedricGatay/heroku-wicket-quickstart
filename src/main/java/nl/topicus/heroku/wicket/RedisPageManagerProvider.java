package nl.topicus.heroku.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.pageStore.IDataStore;

/**
* User: cgatay
* Date: 03/03/13
* Time: 18:41
*/
public class RedisPageManagerProvider extends DefaultPageManagerProvider {
    public RedisPageManagerProvider(final Application application) {
        super(application);
    }

    @Override
    protected IDataStore newDataStore() {
        return new RedisDataStore(application.getName());
    }
}
