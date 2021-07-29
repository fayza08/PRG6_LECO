package id.ac.polman.astra.nim0320190008.leco.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DisukaiService {
    @GET("/disukai/history")
    Call<List<Resep>> getDisukaiByID(@Query("id") Integer id);

    @GET("disukais")
    Call<List<Disukai>> getDisukais();

    @POST("disukai")
    Call<Disukai> addDisukai(@Body Disukai Disukai);
}
