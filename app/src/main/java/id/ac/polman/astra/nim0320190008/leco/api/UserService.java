package id.ac.polman.astra.nim0320190008.leco.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {
    @GET("user")
    Call<User> getUserById(@Query("id") Integer id);

    @GET("login")
    Call<User> getLogin(@Query("email") String email, @Query("password") String password);

    @GET("users")
    Call<List<User>> getUsers();

    @POST("user")
    Call<User> addUser(@Body User user);

    @PUT("user")
    Call<User> updateUser(@Body User user);

    @DELETE("user")
    Call<User> deleteUserById(@Query("id") Integer id);
}
