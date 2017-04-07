package pers.terry.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by terry on 2017/4/7.
 */
public class TestTimers {

    private static final MetricRegistry metrics = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
    private static final Timer requests = metrics.timer(metrics.name(TestTimers.class, "request"));

    public static void handleRequest(int sleep) {
        Timer.Context context = requests.time();
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            context.stop();
        }
    }

    public static void main(String[] args) {
        reporter.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true)
            handleRequest(random.nextInt(1000));
    }

}
