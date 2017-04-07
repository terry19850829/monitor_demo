package pers.terry.common;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import java.util.Map;
import java.util.Random;

/**
 * Created by terry on 2017/4/7.
 */
public class TestHealthCheck extends HealthCheck {

    private final Database database;

    public TestHealthCheck(Database database) {
        this.database = database;
    }

    static class Database {

        public boolean ping() {
            Random random = new Random();
            return random.nextBoolean();
        }

    }


    protected Result check() throws Exception {

        if (database.ping()) {
            return Result.healthy();
        }

        return Result.unhealthy("Can't ping database.");
    }

    public static void main(String[] args) {
        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        healthCheckRegistry.register("database1", new TestHealthCheck(new Database()));
        healthCheckRegistry.register("database2", new TestHealthCheck(new Database()));
        while (true) {
            for (Map.Entry<String, Result> entry : healthCheckRegistry.runHealthChecks().entrySet()) {
                if (entry.getValue().isHealthy()) {
                    System.out.println(entry.getKey() + ":OK");
                } else {
                    System.err.println(entry.getKey() + ":FAIL, error message:" + entry.getValue().getMessage());
                    final Throwable e = entry.getValue().getError();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
