package sdk.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class ScheduleDao {
    private static final Logger logger = LogManager.getLogger(ScheduleDao.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public void reportCurrentTime() {

        logger.info("The time is now {}", dateFormat.format(new Date()));
    }
    public void reportCurrentTime2() {

        logger.info("The time2 is now {}", dateFormat.format(new Date()));
    }

    public  long getDelay () {
        return 1000;
    }

    public  long getDelay2 () {
        return 2000;
    }

    public  Long getDelay3 () {
        return 5000L;
    }


}
