package id.ac.polman.astra.nim0320190008.leco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.User;
import id.ac.polman.astra.nim0320190008.leco.api.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private UserService mUserService;
    private EditText mNama;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mNama = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPassword2 = findViewById(R.id.password2);

        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        findViewById(R.id.loginText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    private void registerUser() {
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
            Call<User> call = mUserService.addUser(new User(0,name, email, password, 0));
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response != null){
                        Toast.makeText(SignUp.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignUp.this,Login.class));
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Sign Up Error : ", t.getMessage());
                    Toast.makeText(SignUp.this, "Data gagal tersimpan!", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}