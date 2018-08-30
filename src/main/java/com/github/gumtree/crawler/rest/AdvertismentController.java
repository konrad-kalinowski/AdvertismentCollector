package com.github.gumtree.crawler.rest;

import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.model.filter.AdvertsFilter;
import com.github.gumtree.crawler.model.filter.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adverts")
public class AdvertismentController {
    private final AdCollectorDao adCollectorDao;

    @Autowired
    public AdvertismentController(AdCollectorDao adCollectorDao) {
        this.adCollectorDao = adCollectorDao;
    }

    @GetMapping
    public List<Advertisement> getAdvertisment(@RequestParam(name = "id", defaultValue = "0") int startId,
                                               @RequestParam(name = "limit", defaultValue = "10") int limit,
                                               @RequestParam(name = "areaMin", required = false) Double areaMin,
                                               @RequestParam(name = "areaMax", required = false) Double areaMax) {
        AdvertsFilter advertsFilter = new AdvertsFilter(new Range<Double>(areaMin, areaMax));
        return adCollectorDao.showAdverts(advertsFilter, startId, limit);
    }

}
