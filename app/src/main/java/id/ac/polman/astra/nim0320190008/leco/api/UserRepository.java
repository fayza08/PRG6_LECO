package id.ac.polman.astra.nim0320190008.leco.api;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepost";
    private static UserRepository INSTANCE;
    private UserService mUserService;

    private UserRepository(Context context){
        mUserService = ApiUtils.getUserService();
    }

    public static void initialize(Context context){
        if(INSTANCE==null){
            INSTANCE =  new UserRepository(context);
        }
    }

    public static UserRepository get(){
        return INSTANCE;
    }

    public MutableLiveData<List<User>> getUsers(){
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        Call<List<User>> call = mUserService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    users.setValue(response.body());
                    Log.i(TAG, "getUsers.onResponse() called");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });
        return users;
    }

    public boolean getLogin(String email, String password){
        boolean cek = false;
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        Call<List<User>> call = mUserService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    users.setValue(response.body());
                    Log.i(TAG, "getUsers.onResponse() called");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });

        for(int i =0; i < users.getValue().size(); i++){
            if(email.equals(users.getValue().get(i).getEmail())){
                if(password.equals(users.getValue().get(i).getPassword())){
                    cek=true;
                }
            }
        }

        return cek;
    }

    public MutableLiveData<User> getUser(String Id){
        MutableLiveData<User> user = new MutableLiveData<>();

        Call<User> call = mUserService.getUserById(Id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    user.setValue(response.body());
                    Log.i(TAG, "getUser.onResponse() called");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });
        return user;
    }

    public void updateUser(User user){
        Log.i(TAG, "updateUser() called");
        Call<User> call = mUserService.updateUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "User updated " + user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });
    }

    public void addUser(User user){
        Log.i(TAG, "addUser() called");
        Call<User> call = mUserService.addUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "User successfully added " + user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });
    }


}
