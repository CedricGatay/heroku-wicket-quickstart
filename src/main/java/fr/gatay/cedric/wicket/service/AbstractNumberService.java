package fr.gatay.cedric.wicket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * User: cgatay
 * Date: 27/02/13
 * Time: 16:55
 */
public abstract class AbstractNumberService implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNumberService.class);
    private Double rnd;

    @PostConstruct
    public void postConstruct(){
        rnd = Math.random();
        LOGGER.info("PostConstruct Called For {} : {}", getClass().getName(), rnd);
    }
    public String getValue(){
        return rnd.toString();
    }
}
