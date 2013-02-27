= Simple quickstart for Wicket 6 + Heroku

This quickstart deploys fine on Heroku with OpenJDK 7 (Cedar stack)
You have to do the following to enable JDK 7

    $ APP_PATH=`heroku config:get PATH`
    $ heroku config:add PATH=/app/.jdk/bin:$APP_PATH

Simple CDI integration is bundled, with standard scopes.

Taken and adapted from :
 * https://github.com/dashorst/heroku-wicket-quickstart
 * https://www.42lines.net/category/blog/software-engineering/full-stack-implementation/

