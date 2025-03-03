package sdk.Configration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import sdk.dao.ScheduleDao;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class DynamicSchedulingConfig implements SchedulingConfigurer {

    @Autowired
    private ScheduleDao scheduleDao;
    @Bean
    public Executor taskExecutor() {

        return Executors.newSingleThreadScheduledExecutor();
    }

    //we configured the TaskScheduler with a pool size of five,
    // but keep in mind that the actual configuration should be
    // fine-tuned to one's specific needs
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());


        //add Runnable with fixedDelay
        Map<Runnable, Long> fixedDelayTasks =new HashMap<>();
        fixedDelayTasks.put(new Runnable() {
            @Override
            public void run() {
                scheduleDao.reportCurrentTime();
            }
        },scheduleDao.getDelay3());

        taskRegistrar.setFixedDelayTasks(fixedDelayTasks);


        //add Runnable with trigger
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                        scheduleDao.reportCurrentTime();
                    }
                },
                new Trigger() {

                    @Override
                    public Instant nextExecution(TriggerContext triggerContext) {
                        Optional<Date> lastCompletionTime =
                                Optional.ofNullable(triggerContext.lastCompletionTime());
                        Instant nextExecutionTime =
                                lastCompletionTime.orElseGet(Date::new).toInstant()
                                        .plusMillis(scheduleDao.getDelay());
                        return Date.from(nextExecutionTime).toInstant();
                    }
                }
        );
    }
}
