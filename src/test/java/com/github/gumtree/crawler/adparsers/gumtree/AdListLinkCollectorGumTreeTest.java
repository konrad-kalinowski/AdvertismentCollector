package com.github.gumtree.crawler.adparsers.gumtree;

import com.github.gumtree.crawler.adparsers.JsoupProvider;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static com.github.gumtree.crawler.adparsers.GumTreeLinkUtil.GUMTREE_DOMAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;


class AdListLinkCollectorGumTreeTest {

    @Test
    public void shouldFetchLinksFromAdvert() throws URISyntaxException {
        JsoupProvider jsoupProviderSpy = spy(new JsoupProvider());
        doReturn(mock(Document.class) ).when(jsoupProviderSpy).connect(any());

        AdListLinkCollectorGumTree adListLinkCollectorGumTree = new AdListLinkCollectorGumTree(jsoupProviderSpy);
        URL sectionsIo = AdListLinkCollectorGumTreeTest.class.getResource("/sections.html");
        File htmlFile = new File(sectionsIo.toURI());
        List<String> advertsLinks = adListLinkCollectorGumTree.getAdvertsLinks(htmlFile, 1, 0);

        assertThat(advertsLinks).hasSize(23);
        assertThat(advertsLinks.get(0)).startsWith(GUMTREE_DOMAIN);
    }

}