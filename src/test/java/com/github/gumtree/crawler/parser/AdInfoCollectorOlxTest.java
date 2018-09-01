package com.github.gumtree.crawler.parser;

import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.adparsers.olx.AdInfoCollectorOlx;
import com.github.gumtree.crawler.model.Advertisement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class AdInfoCollectorOlxTest {

    @Test
    void collectAdFromFile() throws URISyntaxException {
        JsoupProvider jsoupProviderSpy = spy(new JsoupProvider());
        doReturn(mock(org.jsoup.nodes.Document.class)).when(jsoupProviderSpy).connect(any());

        AdInfoCollectorOlx adInfoCollectorOlx = new AdInfoCollectorOlx(jsoupProviderSpy, null, null);
        URL resource = AdInfoCollectorOlxTest.class.getResource("/olx_advert.html");
        File file = new File(resource.toURI());
        Advertisement advertisement = adInfoCollectorOlx.collectInfo(file);


        Assertions.assertThat(advertisement).isNotNull();
        Assertions.assertThat(advertisement.getPrice()).isEqualTo(220000d);
        Assertions.assertThat(advertisement.getCity()).isEqualTo("");
        Assertions.assertThat(advertisement.getDescription().startsWith("Sprzedam jasne i przestronne mieszkanie blisko morza w Kołobrzegu znajdujące się na "));
        Assertions.assertThat(advertisement.getDescription().endsWith("Więcej informacji udzielę telefonicznie."));
        Assertions.assertThat(advertisement.getTitle()).isEqualTo("Sprzedam 2 pok. mieszkanie blisko morza Kołobrzeg • OLX.pl");


    }
}