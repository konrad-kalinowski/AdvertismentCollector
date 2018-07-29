package com.github.gumtree.crawler.nominatim;

import com.github.gumtree.crawler.nominatim.model.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface NominatimService {
    @Headers({"User-Agent: Ad-info-collector"})
    @GET("search")
    Call<List<SearchResult>> findCoordinates(
            @Query("format") String format,
            @Query("street") String street,
            @Query("city") String city,
            @Query("country") String country);

}
