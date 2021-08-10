package id.ac.polman.astra.nim0320190008.leco.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ResepService {
    @GET("resep")
    Call<Resep> getResepByID(@Query("id") String id);

    @GET("resep/user")
    Call<List<Resep>> getResepByUser(@Query("id") Integer id);

    @GET("reseps")
    Call<List<Resep>> getReseps();

    @POST("resep")
    Call<Resep> addResep(@Body Resep resep);

    @PUT("resep")
    Call<Resep> updateResep(@Body Resep resep);

    @DELETE("resep")
    Call<Resep> deleteResepById(@Query("id") Integer id);
}
