package pers.terry.common;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by terry on 2017/4/6.
 * <p>
 * gauges demo
 */
public class QueueManager {

    private final Queue queue;

    public QueueManager(MetricRegistry metrics, String name) {
        this.queue = new LinkedBlockingQueue();
        metrics.register(MetricRegistry.name(QueueManager.class, name, "size"), new Gauge<Integer>() {
            public Integer getValue() {
                return queue.size(); // O(n) slow => lock
            }
        });
    }

    public void add(int num) {
        queue.offer(num);
    }

    public void del() {
        queue.poll();
    }

}
