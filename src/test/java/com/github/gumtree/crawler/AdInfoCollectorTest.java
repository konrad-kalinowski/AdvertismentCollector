package com.github.gumtree.crawler;

import com.github.gumtree.crawler.model.Advertisement;
import com.github.gumtree.crawler.parser.AdInfoCollector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

class AdInfoCollectorTest {

    @Test
    void collectAdInfo() throws URISyntaxException {
        //given
        URL sectionsIo = AdInfoCollectorTest.class.getResource("/single-test-advert.html");
        File htmlFile = new File(sectionsIo.toURI());
        AdInfoCollector adInfoCollector = new AdInfoCollector();
        //when
        Advertisement advert = adInfoCollector.collectAdFromFile(htmlFile);

        //then
        Assertions.assertThat(advert).isNotNull();
        Assertions.assertThat(advert.getLink()).endsWith("single-test-advert.html");
        Assertions.assertThat(advert.getPrice()).isEqualTo(790_000d);
        Assertions.assertThat(advert.getArea()).isEqualTo(136);
        Assertions.assertThat(advert.getDescription())
                .isNotBlank()
                .startsWith("Okazja! W ofercie apartament cztero pokojowy w ścisłym centrum Krakowa.")
                .endsWith("The flat has been div");


    }
}