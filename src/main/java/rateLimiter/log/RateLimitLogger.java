package rateLimiter.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Iancy
 * @date 2022/3/7
 */
public final class RateLimitLogger {
    private  static final Logger LOGGER = LoggerFactory.getLogger(RateLimitLogger.class);
    private static final  String LOGGER_TITLE="[[title=RateLimitExecutor]] ";

    /**
     * 打印Info日志
     */
    public static void infoLog(String msg,Object ... params){
        LOGGER.info(LOGGER_TITLE+msg,params);
    }

    /**
     * 打印Warn级别日志
     */
    public static void warnLog(String msg,Object ... params){
        LOGGER.warn(LOGGER_TITLE+msg,params);
    }

    /**
     * 打印Error级别日志
     */
    public static void errorLog(String msg,Throwable ex){
        LOGGER.error(LOGGER_TITLE+msg,ex);
    }
}
