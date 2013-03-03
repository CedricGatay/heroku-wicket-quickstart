package nl.topicus.heroku.wicket;

import org.apache.wicket.pageStore.IDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * User: cgatay
 * Date: 01/03/13
 * Time: 08:59
 */
public class RedisDataStore implements IDataStore {
    public static final String STORE_PREFIX = "Wicket_Store";
    public static final String SEPARATOR = ":::";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDataStore.class);
    private JedisPool jedisPool;
    private final String appName;

    public RedisDataStore(final String appName) {
        this.appName = appName;
        try {
            URI redisUri = new URI(System.getenv("REDISCLOUD_URL")); //REDISTOGO_URL if you're using RedisToGo
            jedisPool = new JedisPool(new JedisPoolConfig(),
                                           redisUri.getHost(),
                                           redisUri.getPort(),
                                           Protocol.DEFAULT_TIMEOUT,
                                           redisUri.getUserInfo().split(":",2)[1]);
        } catch (Exception e) {
            LOGGER.error("Unable to connect to Redis, fallbacking to 127.0.0.1 : {}",
                         e.getMessage());
            jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        }
        if (jedisPool == null){
            throw new RuntimeException("Unable to connect to any Redis server");
        }
    }

    public byte[] getData(final String sessionId, final int pageId) {
        LOGGER.debug("Reading data from Redis store for sessionId : {} and pageId : {}",
                     sessionId, pageId);
        final Jedis jedis = jedisPool.getResource();
        try {
            final StringBuilder key = buildKeyForSessionId(sessionId);
            final byte[] bytes = jedis.hget(bytes(key), bytes(pageId));
            LOGGER.debug("Successfully read data from Redis store for sessionId : {} and pageId : {}",
                         sessionId, pageId);
            return bytes;
        } catch (Exception e) {
            LOGGER.error("Unable to get data from redis backend : {}",
                         e.getMessage());
        } finally {
            jedisPool.returnResource(jedis);
        }
        LOGGER.debug("Nothing found for sessionId : {} and pageId : {}",
                     sessionId, pageId);
        return new byte[0];
    }

    public void removeData(final String sessionId, final int pageId) {
        LOGGER.debug("Removing data from Redis store for sessionId : {} and pageId : {}",
                     sessionId, pageId);
        final Jedis jedis = jedisPool.getResource();
        try {
            final StringBuilder key = buildKeyForSessionId(sessionId);
            final Long amountDeleted = jedis.hdel(bytes(key), bytes(pageId));
            LOGGER.debug("Successfully deleted data ({} entries) from Redis store for sessionId : {} and pageId : {}",
                         new Object[]{amountDeleted, sessionId, pageId});
        } catch (Exception e) {
            LOGGER.error("Unable to delete data from redis backend : {}",
                         e.getMessage());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public void removeData(final String sessionId) {
        LOGGER.debug("Removing data from Redis store for sessionId : {}",
                     sessionId);
        final Jedis jedis = jedisPool.getResource();
        try {
            final StringBuilder key = buildKeyForSessionId(sessionId);
            final Long amountDeleted = jedis.hdel(bytes(key));
            LOGGER.debug("Successfully deleted data ({} entries) from Redis store for sessionId : {}",
                         amountDeleted, sessionId);
        } catch (Exception e) {
            LOGGER.error("Unable to store data to redis backend : {}",
                         e.getMessage());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public void storeData(final String sessionId, final int pageId, final byte[] data) {
        final Jedis jedis = jedisPool.getResource();
        try {
            final StringBuilder key = buildKeyForSessionId(sessionId);
            jedis.hset(bytes(key), bytes(pageId), data);
        } catch (Exception e) {
            LOGGER.error("Unable to store data to redis backend : {}",
                         e.getMessage());
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    private StringBuilder buildKeyForSessionId(final String sessionId) {
        return new StringBuilder(STORE_PREFIX).append(SEPARATOR)
                .append(appName).append(SEPARATOR)
                .append(sessionId);
    }

    public void destroy() {
        jedisPool.destroy();
    }

    public boolean isReplicated() {
        return true;
    }

    public boolean canBeAsynchronous() {
        return false;
    }

    private byte[] bytes(final StringBuilder target) {
        return bytes(target.toString());
    }

    private byte[] bytes(final String string) {
        byte[] result = null;
        try {
            result = string.getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            LOGGER.error("Unsupported UTF-8 encoding?!");
        }
        return result;
    }

    private byte[] bytes(final int i) {
        return bytes(String.valueOf(i));
    }
}
