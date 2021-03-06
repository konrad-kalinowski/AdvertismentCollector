package com.github.gumtree.crawler.adparsers.olx;

import com.github.gumtree.crawler.adparsers.Domain;
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

class AdListLinkCollectorOlxTest {

    @Test
    void getAdvertsLinks() throws URISyntaxException {
        JsoupProvider jsoupeSpy = spy(new JsoupProvider());
        doReturn(mock(Document.class)).when(jsoupeSpy).connect(any());

        AdListLinkCollectorOlx adListLinkCollectorOlx = new AdListLinkCollectorOlx(jsoupeSpy,
                new DuplicatedLinkChecker(Collections.emptyList()),1);
        URL resource = AdListLinkCollectorOlxTest.class.getResource("/list_of_ads_olx.html");
        File file = new File(resource.toURI());
        List<String> advertsLinks = adListLinkCollectorOlx.getAdvertsLinks(file, 1);

        Assertions.assertThat(advertsLinks).contains("https://www.olx.pl/oferta/mieszkanie-przy-umk-na-ul-falata" +
                "-bez-posrednika-po-remoncie-CID3-IDwJGeZ.html#f3c4691bd2;promoted");
        Assertions.assertThat(advertsLinks.size()).isEqualTo(26);
        Assertions.assertThat(advertsLinks).allMatch(link -> link.startsWith(Domain.OLX_DOMAIN));


    }
}