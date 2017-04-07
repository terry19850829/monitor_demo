package pers.terry.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by terry on 2017/4/7.
 * <p>
 * 分布
 */
public class TestHistograms {


    private static final Logger logger = LoggerFactory.getLogger(TestHistograms.class);

    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ConsoleReporter consoleReportor = ConsoleReporter.forRegistry(metricRegistry).build();

    private static final Histogram randomNums = metricRegistry.histogram(metricRegistry.name(TestHistograms.class, "random"));

    public static void main(String[] args) throws InterruptedException {
        consoleReportor.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true) {
            logger.info("handleRequest");
            handleRequest(random.nextDouble());
            Thread.sleep(1000);
        }
    }

    public static void handleRequest(double random) {
        randomNums.update((int) (random * 100));
    }

}
