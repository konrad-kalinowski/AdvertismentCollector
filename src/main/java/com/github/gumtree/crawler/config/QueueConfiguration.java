package com.github.gumtree.crawler.config;

import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfiguration {

    @Bean
    public BlockingQueue<AdLink> adLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public BlockingQueue<AdLink> olxAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public BlockingQueue<AdLink> gumtreeAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public BlockingQueue<AdLink> otodomAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public BlockingQueue<Advertisement> advertismentsQueue() {
        return new LinkedBlockingQueue<>(100);
    }


}
