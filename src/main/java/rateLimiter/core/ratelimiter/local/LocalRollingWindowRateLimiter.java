package rateLimiter.core.ratelimiter.local;

import lombok.Setter;
import rateLimiter.config.BasicProperties;
import rateLimiter.config.local.LocalRollingWindowProperties;
import rateLimiter.core.ratelimiter.AbstractRateLimiter;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocalRollingWindowRateLimiter extends AbstractRateLimiter {
    private final Lock lock = new ReentrantLock();
    private final RollingWindow rollingWindow = new RollingWindow();

    public LocalRollingWindowRateLimiter(BasicProperties basicProperties, LocalRollingWindowProperties properties) {
        super(basicProperties);
        rollingWindow.setMax(properties.getMax());
        rollingWindow.setTime(properties.getTime());
    }

    @Override
    public boolean limit() throws Exception {
        try {
            lock.lock();
            boolean pass = false;
            Deque<Long> queue = rollingWindow.queue;
            int size = queue.size();
            long cur = System.currentTimeMillis();
            if (size < rollingWindow.max) {
                queue.add(cur);
                pass = true;
            } else {
                assert !queue.isEmpty();
                long peek = queue.peekFirst();
                if (peek < cur - rollingWindow.time) {
                    queue.pollFirst();
                    queue.addLast(cur);
                    pass = true;
                }
            }
            return pass;
        } finally {
            lock.unlock();
        }
    }

    /**
     * time window
     */
    @Setter
    public static class RollingWindow {
        private long time;
        private long max;
        private Deque<Long> queue = new LinkedList<>();
        private RollingWindow(){}
    }
}
