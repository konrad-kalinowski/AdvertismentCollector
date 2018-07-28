package com.github.gumtree.crawler.nominatim;

import com.github.gumtree.crawler.nominatim.model.SearchResult;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class NominatinClient {

    public static void main(String[] args) throws IOException {
        NominatimService client = new NominatinClient().createClient();
        List<SearchResult> body = client.findCoordinates("json", "czyżyńska 21", "kraków", "poland").execute().body();
        for (SearchResult searchResult : body) {
            System.out.println(searchResult);
        }
    }

    public NominatimService createClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://nominatim.openstreetmap.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(NominatimService.class);
    }

}
