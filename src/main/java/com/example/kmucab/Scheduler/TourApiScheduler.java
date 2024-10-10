package com.example.kmucab.Scheduler;

import com.example.kmucab.Service.TourApiService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class TourApiScheduler {

    private final TourApiService tourApiService;

    public TourApiScheduler(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    // 일주일에 한 번 실행 (밀리초 기준, 아래는 7일마다 실행)
    @Scheduled(fixedRate = 604800000)
    public void fetchTourData() {
        tourApiService.fetchAndStoreTourData();
    }
}
