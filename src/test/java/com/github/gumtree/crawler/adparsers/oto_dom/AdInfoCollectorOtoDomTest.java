package com.github.gumtree.crawler.adparsers.oto_dom;

import com.github.gumtree.crawler.adparsers.JsoupProvider;
import com.github.gumtree.crawler.model.Advertisement;
import org.assertj.core.api.Assertions;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class AdInfoCollectorOtoDomTest {

    @Test
    void collectInfo() throws URISyntaxException {
        JsoupProvider jsoupeSpy = spy(new JsoupProvider());
        doReturn(mock(Document.class)).when(jsoupeSpy).connect(any());

        AdInfoCollectorOtoDom adInfoCollectorOtoDom = new AdInfoCollectorOtoDom(jsoupeSpy);

        URL resource = AdListLinkCollectorOtoDomTest.class.getResource("/oto_dom_single_advert.html");
        File file = new File(resource.toURI());

        Advertisement advertisement = adInfoCollectorOtoDom.collectInfo(file);

        Assertions.assertThat(advertisement.getPrice()).isEqualTo(267000);
        Assertions.assertThat(advertisement.getLocation()).isEqualTo("Kraków, Bieńczyce, Józefa Strusia");
        Assertions.assertThat(advertisement.getDescription()).startsWith("Oferuję Państwu mieszk").endsWith("E-mail: Jan Kowalski");
        Assertions.assertThat(advertisement.getTitle()).isEqualTo("Sprzedam mieszkanie 44m2, 3 pok. balkon, Strusia");
        Assertions.assertThat(advertisement.getArea()).isEqualTo(44.0);




    }
}