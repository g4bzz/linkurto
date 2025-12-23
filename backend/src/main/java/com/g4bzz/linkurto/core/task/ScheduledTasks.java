package com.g4bzz.linkurto.core.task;

import com.g4bzz.linkurto.model.Url;
import com.g4bzz.linkurto.repository.UrlRepository;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {
    private final UrlRepository urlRepository;
    private final Logger log;

    public ScheduledTasks(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        this.log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    }

    // Short time interval for testing purposes
    @Scheduled(fixedRateString = "${tasks.expired-urls-cleaning-interval-in-minutes}", timeUnit = TimeUnit.MINUTES)
    public void cleanExpiredUrls() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Cleaning up expired urls");

        List<Url> expiredUrls = urlRepository.findAllWithExpirationDateBefore(now);
        urlRepository.deleteAll(expiredUrls);
    }
}
