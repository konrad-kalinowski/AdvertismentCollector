package com.github.gumtree.crawler.config;

import com.github.gumtree.crawler.model.AdLink;
import com.github.gumtree.crawler.model.Advertisement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfiguration {

    @Bean
    public LinkedBlockingQueue<AdLink> adLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public LinkedBlockingQueue<AdLink> olxAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public LinkedBlockingQueue<AdLink> otoDomAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public LinkedBlockingQueue<AdLink> gumtreeAdLinksQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public LinkedBlockingQueue<Advertisement> advertismentsQueue() {
        return new LinkedBlockingQueue<>(100);
    }


}
