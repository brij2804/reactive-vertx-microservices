package com.brijesh.vertx.rest.api;

import com.brijesh.vertx.rest.api.verticle.MainVerticle;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

public class MainClass {

  public static void main(String[] args) {
    System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");

    PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    new UptimeMetrics().bindTo(registry);

    final Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new MicrometerMetricsOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
        .setJvmMetricsEnabled(true)
        .setMicrometerRegistry(registry)
        .setEnabled(true)));

    vertx.deployVerticle(MainVerticle.class.getName())
      .onFailure(throwable -> System.exit(-1));
  }
}
