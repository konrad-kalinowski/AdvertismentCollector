package com.github.gumtree.crawler.rest;

import com.github.gumtree.crawler.db.AdCollectorDao;
import com.github.gumtree.crawler.model.Advertisement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<Advertisement> getAdvertisment(){
        return adCollectorDao.showAdverts();
    }
}