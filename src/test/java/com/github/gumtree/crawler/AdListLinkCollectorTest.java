package com.github.gumtree.crawler;

import com.github.gumtree.crawler.parser.AdListLinkCollector;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class AdListLinkCollectorTest {

    @Test
    public void shouldFetchLinksFromAdvert() throws URISyntaxException {
        AdListLinkCollector adListLinkCollector = new AdListLinkCollector();
        URL sectionsIo = AdListLinkCollectorTest.class.getResource("/sections.html");
        File htmlFile = new File(sectionsIo.toURI());
        List<String> advertsLinks = adListLinkCollector.getAdvertsLinks(htmlFile, 1, 1000);

        assertThat(advertsLinks).hasSize(23);
        assertThat(advertsLinks.get(0)).startsWith("https://www.gumtree.pl");
    }

}