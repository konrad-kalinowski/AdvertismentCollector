package com.github.gumtree.crawler.adparsers.gumtree;

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

class AdInfoCollectorGumTreeTest {

    @Test
    void shouldFetchAdInfo() throws URISyntaxException {
        JsoupProvider jsoupProviderSpy = spy(new JsoupProvider());
        doReturn(mock(Document.class)).when(jsoupProviderSpy).connect(any());

        AdInfoCollectorGumTree adInfoCollectorGumTree = new AdInfoCollectorGumTree(jsoupProviderSpy);

        URL sectionsIo = AdListLinkCollectorGumTreeTest.class.getResource("/single-test-advert.html");
        File htmlFile = new File(sectionsIo.toURI());
        Advertisement advertisement = adInfoCollectorGumTree.collectInfo(htmlFile);


        Assertions.assertThat(advertisement).isNotNull();
        Assertions.assertThat(advertisement.getLink()).endsWith("single-test-advert.html");
        double expectedPrice = 790_000d;
        Assertions.assertThat(advertisement.getPrice()).isEqualTo(expectedPrice);
        double expectedArea = 136;
        Assertions.assertThat(advertisement.getArea()).isEqualTo(expectedArea);
        Assertions.assertThat(advertisement.getPricePerSquareMeter()).isEqualTo(expectedPrice/expectedArea);
        Assertions.assertThat(advertisement.getDescription())
                .isNotBlank()
                .startsWith("Okazja! W ofercie apartament cztero pokojowy w ścisłym centrum Krakowa.")
                .endsWith("The flat has been div");


    }
}