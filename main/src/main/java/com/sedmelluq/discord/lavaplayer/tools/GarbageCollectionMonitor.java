package com.sedmelluq.discord.lavaplayer.tools;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Garbage collection monitor which records all GC pause lengths and logs them. In case the GC pause statistics are
 * considered bad for latency, the statistics are logged at a warning level.
 */
public class GarbageCollectionMonitor implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(GarbageCollectionMonitor.class);

  private static final long REPORTING_FREQUENCY = TimeUnit.MINUTES.toMillis(2);

  private final ScheduledExecutorService reportingExecutor;
  private final AtomicBoolean enabled;
  private final AtomicReference<ScheduledFuture<?>> executorFuture;

  /**
   * Create an instance of GC monitor. Does nothing until enabled.
   * @param reportingExecutor Executor to use for scheduling reporting task
   */
  public GarbageCollectionMonitor(ScheduledExecutorService reportingExecutor) {
    this.reportingExecutor = reportingExecutor;
    enabled = new AtomicBoolean();
    executorFuture = new AtomicReference<>();
  }

  /**
   * Enable GC monitoring and reporting.
   */
  public void enable() {
    if (enabled.compareAndSet(false, true)) {
      //registerBeanListener();

      executorFuture.set(reportingExecutor.scheduleAtFixedRate(this, REPORTING_FREQUENCY, REPORTING_FREQUENCY, TimeUnit.MILLISECONDS));

      log.info("GC monitoring enabled, reporting results every 2 minutes.");
    }
  }

  /**
   * Disable GC monitoring and reporting.
   */
  public void disable() {
    if (enabled.compareAndSet(true, false)) {
      //unregisterBeanListener();

      ScheduledFuture<?> scheduledTask = executorFuture.getAndSet(null);
      if (scheduledTask != null) {
        scheduledTask.cancel(false);
      }

      log.info("GC monitoring disabled.");
    }
  }

  @Override
  public void run() {
    log.debug("GC result is unable for android platform");
  }
}
