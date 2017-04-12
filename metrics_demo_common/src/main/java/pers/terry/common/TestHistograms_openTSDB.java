package pers.terry.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.github.sps.metrics.OpenTsdbReporter;
import com.github.sps.metrics.TaggedMetricRegistry;
import com.github.sps.metrics.opentsdb.OpenTsdb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by terry on 2017/4/11.
 */
public class TestHistograms_openTSDB {

    private static final Logger logger = LoggerFactory.getLogger(TestHistograms_openTSDB.class);


    public static void main(String[] args) throws InterruptedException {

        TaggedMetricRegistry metrics = new TaggedMetricRegistry();
        ConsoleReporter.forRegistry(metrics).build().start(3, TimeUnit.SECONDS);

        Map<String, String> tags = new HashMap<String, String>();
        tags.put("host", "localhost");
        OpenTsdbReporter.forRegistry(metrics)
                .withTags(tags)
                .convertDurationsTo(TimeUnit.SECONDS)
                .convertRatesTo(TimeUnit.MILLISECONDS)
                .build(OpenTsdb
                        .forService("http://localhost:4242")
                        .create())
                .start(3L, TimeUnit.SECONDS);

        Histogram randomNums = metrics.histogram(metrics.name(TestHistograms_openTSDB.class, "random"));
        Random random = new Random();
        while (true) {
            logger.info("handleRequest");
            handleRequest(randomNums, random.nextDouble());
            Thread.sleep(1000);
        }

    }


    public static void handleRequest(Histogram histogram, double random) {
        histogram.update((int) (random * 100));
    }

}
