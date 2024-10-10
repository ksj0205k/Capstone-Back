package com.example.kmucab.Controller;

import com.example.kmucab.Domain.TourData;
import com.example.kmucab.Repository.TourDataRepository;
import com.example.kmucab.Service.TourApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TourDataController {

    private final TourDataRepository tourDataRepository;
    private final TourApiService tourApiService;

    public TourDataController(TourDataRepository tourDataRepository, TourApiService tourApiService) {
        this.tourDataRepository = tourDataRepository;
        this.tourApiService = tourApiService;
    }

    // 모든 TourData 조회
    @GetMapping("/api/tourdata")
    public List<TourData> getAllTourData() {
        return tourDataRepository.findAll();
    }

    //TourAPI에서 데이터 끌어오기
    @GetMapping("/api/gettourapi")
    public void getTourApiService() {
        tourApiService.fetchAndStoreTourData();
    }
}
