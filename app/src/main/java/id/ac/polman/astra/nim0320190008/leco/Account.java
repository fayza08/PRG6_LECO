package id.ac.polman.astra.nim0320190008.leco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.User;
import id.ac.polman.astra.nim0320190008.leco.api.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account extends AppCompatActivity {

    private UserService mUserService;
    SharedPreferences sharedPreferences;
    private EditText mNama;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;
    private final static String APP_NAME= "LECO";
    private final static String EMAIL = "email";
    private final static String PASSWORD = "password";
    private final static String NAMA = "nama";
    private final static String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.account);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.account: return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0); return true;
                    case R.id.recipe:
                        startActivity(new Intent(getApplicationContext(), Recipe.class));
                        overridePendingTransition(0,0); return true;
                }
                return false;
            }
        });

        sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        mNama= (EditText) findViewById(R.id.updatename);
        mEmail = (EditText) findViewById(R.id.updateemail);
        mPassword = (EditText) findViewById(R.id.updatepassword);
        mPassword2 = (EditText) findViewById(R.id.updatepassword2);

        //Cek SharedPreferences
        String emailsp = sharedPreferences.getString(EMAIL, null);
        String pwdsp = sharedPreferences.getString(PASSWORD, null);
        String namasp = sharedPreferences.getString(NAMA, null);
        Integer idsp = sharedPreferences.getInt(ID, 0);

        if(emailsp != null && pwdsp != null){
            mNama.setText(namasp);
            mEmail.setText(emailsp);
            mPassword.setText(pwdsp);
            mPassword2.setText(pwdsp);
        }

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(idsp);
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(idsp);
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(Account.this, MainActivity.class));
                finish();
            }
        });
    }


    private void save(Integer id){
        String name = mNama.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();

        if (name.isEmpty()) {
            mNama.setError("Harap isi Field Nama!");
            mNama.requestFocus();
            return;
        } else if (email.isEmpty()) {
            mEmail.setError("Harap isi Field Email!");
            mEmail.requestFocus();
            return;
        }else if (password.isEmpty()) {
            mPassword.setError("Harap isi Field Password!");
            mPassword.requestFocus();
            return;
        }else if (password2.isEmpty()) {
            mPassword2.setError("Harap isi Field Confirm Password!");
            mPassword2.requestFocus();
            return;
        }

        if(!password.equals(password2)){
            mPassword2.setError("Password Doesn't Match!");
            mPassword2.requestFocus();
            return;
        }else{
            mUserService = ApiUtils.getUserService();
            Call<User> call = mUserService.updateUser(new User(id,name, email, password, 0));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response != null){
                        Toast.makeText(Account.this, "Data saved successfully", Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(SignUp.this,Login.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Update Error : ", t.getMessage());
                    Toast.makeText(Account.this, "Data gagal tersimpan!", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void delete(Integer id){
        mUserService = ApiUtils.getUserService();
        Call<User> call = mUserService.deleteUserById(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null){
                    Toast.makeText(Account.this, "It Sad To See You Go! See you!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Account.this, SignUp.class));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Delete Error : ", t.getMessage());
                Toast.makeText(Account.this, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }


}