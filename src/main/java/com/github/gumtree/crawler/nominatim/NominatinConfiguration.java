package com.github.gumtree.crawler.nominatim;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class NominatinConfiguration {

    @Bean
    public NominatimService nominatimService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://nominatim.openstreetmap.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(NominatimService.class);
    }
}
