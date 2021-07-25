package id.ac.polman.astra.nim0320190008.leco.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit sRetrofit = null;

    public static Retrofit getClient(String url){
        if(sRetrofit==null){
            sRetrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
