package pers.terry.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Created by terry on 2017/4/7.
 * <p>
 * 统计数量
 */
public class TestCounter {

    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
    private static final Counter pendingJobs = metricRegistry.counter(MetricRegistry.name(TestCounter.class, "pending-job"));

    private static final Queue<String> queue = new LinkedList<String>();


    public static final void add(String str) {
        pendingJobs.inc();
        queue.offer(str);
    }

    public static final String take() {
        pendingJobs.dec();
        return queue.poll();
    }

    public static void main(String[] args) throws InterruptedException {
        consoleReporter.start(3, TimeUnit.SECONDS);
        while (true) {
            add("1");
            Thread.sleep(1000);
        }
    }

}
