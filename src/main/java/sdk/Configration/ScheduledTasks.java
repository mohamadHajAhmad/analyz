package sdk.Configration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sdk.dao.implementation.CISDAOImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
@EnableAsync
public class ScheduledTasks {

    private static final Logger logger = LogManager.getLogger(CISDAOImpl.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Async
    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        logger.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Async
    @Scheduled(fixedDelay = 9000)
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                "Fixed rate task async - " + dateFormat.format(new Date()));
    }

    @Async
    @Scheduled(fixedDelay = 9000, initialDelay = 1000)
    public void scheduleFixedRateWithInitialDelayTask() {

        Date now = new Date();
        System.out.println(
                "Fixed rate task with one second initial delay - " + dateFormat.format(now));
    }

    @Scheduled(cron = "0 15 15 15 * ?")
    public void scheduleTaskUsingCronExpression() {

        long now = System.currentTimeMillis() / 1000;
        System.out.println(
                "schedule tasks using cron jobs - " + dateFormat.format(now));
    }
}
