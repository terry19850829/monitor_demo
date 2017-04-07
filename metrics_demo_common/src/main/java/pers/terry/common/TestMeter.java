package pers.terry.common;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * Created by terry on 2017/4/6.
 * <p>
 * meter 为用表计量（某个时间段内的 数量） 默认 1 5 15 TPS
 */
public class TestMeter {

    static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args) {
        startReport();
        Meter requests = metrics.meter("requests");
        requests.mark(); //计量次数
        final QueueManager queueManager = new QueueManager(metrics, "size");
        for (int i = 0; i < 20; i++) {
            queueManager.add(1);
        }
        new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 20; i++) {
                    queueManager.del();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        wait5Seconds();


    }

    static void startReport() {
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }

    static void wait5Seconds() {
        try {
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
        }
    }

}
