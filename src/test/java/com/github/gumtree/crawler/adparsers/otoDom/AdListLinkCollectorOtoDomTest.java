package com.github.gumtree.crawler.adparsers.otoDom;

import com.github.gumtree.crawler.adparsers.DuplicatedLinkChecker;
import com.github.gumtree.crawler.adparsers.JsoupProvider;
import org.assertj.core.api.Assertions;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class AdListLinkCollectorOtoDomTest {

    @Test
    void getLinks() throws URISyntaxException {
        JsoupProvider jsoupeSpy = spy(new JsoupProvider());
        doReturn(mock(Document.class)).when(jsoupeSpy).connect(any());

        AdListLinkCollectorOtoDom adListLinkCollectorOtoDom = new AdListLinkCollectorOtoDom(jsoupeSpy, new DuplicatedLinkChecker(Collections.emptyList()), 1);
        URL resource = AdInfoCollectorOtoDomTest.class.getResource("/list_of_adverts_otodom.html");
        File file = new File(resource.toURI());
        List<String> advertsLinks = adListLinkCollectorOtoDom.getAdvertsLinks(file, 1);

        Assertions.assertThat(advertsLinks).isNotEmpty();
        Assertions.assertThat(advertsLinks.size()).isEqualTo(31);
        Assertions.assertThat(advertsLinks).contains("https://www.otodom.pl/oferta/atrakcyjne-mieszkania-na-krakowskim-" +
                "biezanowie-ID3HuAo.html#c0ec293530");
    }
}