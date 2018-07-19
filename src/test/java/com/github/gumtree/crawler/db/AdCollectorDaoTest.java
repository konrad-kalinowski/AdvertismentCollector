package com.github.gumtree.crawler.db;

import com.github.gumtree.crawler.model.Advertisement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class AdCollectorDaoTest {

    @Test
    void addAdvert() {
        AdCollectorDao adCollectorDao = new AdCollectorDao();
        adCollectorDao.initialize();
        adCollectorDao.addAdvert(new Advertisement.AdvertBuilder("Pokoje do wynajecia", "www.gumtree.pl").build());
        List<Advertisement> advertisements = adCollectorDao.showAdverts();
        Assertions.assertThat(advertisements).hasSize(1);
        Assertions.assertThat(advertisements.get(0).getTitle()).isEqualTo("Pokoje do wynajecia");
        Assertions.assertThat(advertisements.get(0).getLocation()).isNotEqualTo("dasdada");
        adCollectorDao.close();

    }
}