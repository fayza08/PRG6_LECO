package id.ac.polman.astra.nim0320190008.leco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.User;
import id.ac.polman.astra.nim0320190008.leco.api.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private UserService mUserService;
    private EditText mEmail;
    private EditText mPassword;
    SharedPreferences sharedPreferences;
    private final static String APP_NAME= "LECO";
    private final static String EMAIL = "email";
    private final static String PASSWORD = "password";
    private final static String NAMA = "nama";
    private final static String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);

        String emailsp = sharedPreferences.getString(EMAIL, null);
        String pwdsp = sharedPreferences.getString(PASSWORD, null);
        if(emailsp != null && pwdsp != null){
            startActivity(new Intent(Login.this, Dashboard.class));
        }

        mEmail = findViewById(R.id.emailLog);
        mPassword = findViewById(R.id.passwordLog);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.signUpText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    private void login(){
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (password.isEmpty()) {
            mPassword.setError("Harap isi Field Password!");
            mPassword.requestFocus();
            return;
        } else if (email.isEmpty()) {
            mEmail.setError("Harap isi Field Email!");
            mEmail.requestFocus();
            return;
        }

        mUserService = ApiUtils.getUserService();
        Call<User> call = mUserService.getLogin(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null){
                    User user = response.body();
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(EMAIL, user.getEmail());
                    edit.putString(PASSWORD, user.getPassword());
                    edit.putString(NAMA, user.getNama());
                    edit.putInt(ID, user.getId());
                    edit.apply();
                    startActivity(new Intent(Login.this, Dashboard.class));
                    mEmail.setText("");
                    mPassword.setText("");
                    Toast.makeText(Login.this, "Login Berhasil!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this, "Failed To Login !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Log In Error : ", t.getMessage());
                Toast.makeText(Login.this, "Gagal Login!", Toast.LENGTH_LONG).show();
            }
        });


    }

}