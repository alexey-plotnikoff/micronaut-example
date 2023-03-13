package am.plotnikov.example.grpc.config;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import me.dinowernli.grpc.prometheus.Configuration;
import me.dinowernli.grpc.prometheus.MonitoringServerInterceptor;

@Factory
public class MonitorInterceptorFactory {

    @Singleton
    public MonitoringServerInterceptor monitoringServerInterceptor(PrometheusMeterRegistry registry) {
        return MonitoringServerInterceptor.create(Configuration.allMetrics().withCollectorRegistry(
                registry.getPrometheusRegistry()
        ));
    }

}
