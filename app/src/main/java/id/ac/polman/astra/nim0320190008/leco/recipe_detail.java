package id.ac.polman.astra.nim0320190008.leco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Disukai;
import id.ac.polman.astra.nim0320190008.leco.api.DisukaiService;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recipe_detail extends AppCompatActivity {

    TextView desc, title, ingredients, steps, liked;
    private DisukaiService mDisukaiService;

    private Resep mResep;
    SharedPreferences sharedPreferences;
    private final static String APP_NAME= "LECO";
    private final static String ID = "id";
    private boolean cek = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        desc = findViewById(R.id.detail_desk);
        title = findViewById(R.id.detail_title);
        ingredients = findViewById(R.id.detail_ingredients);
        steps = findViewById(R.id.detail_step);
        liked = findViewById(R.id.detail_liked);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            mResep = (Resep) intent.getSerializableExtra("data");
            desc.setText(mResep.getKeterangan());
            title.setText(mResep.getNama());
            ingredients.setText(mResep.getAlat_bahan());
            steps.setText(mResep.getTahap());
            liked.setText(mResep.getNilai().toString());
            String fav = intent.getStringExtra("fav");

            if(fav.equals("Liked")){
                cek=true;
                liked.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_like), null);
            }else{
                liked.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this,R.drawable.ic_liked), null);
            }

        }

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
                Integer idsp = sharedPreferences.getInt(ID, 0);

                if(!cek){
                    mDisukaiService = ApiUtils.getDisukaiService();
                    Call<Disukai> call = mDisukaiService.addDisukai(new Disukai(1, idsp, mResep.getId(), new Date()));
                    call.enqueue(new Callback<Disukai>() {
                        @Override
                        public void onResponse(Call<Disukai> call, Response<Disukai> response) {
                            if(response != null) {
                                Toast.makeText(recipe_detail.this,"Data Saved Succesfully", Toast.LENGTH_LONG).show();
                                liked.setText(mResep.getNilai().toString());
                                liked.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_like), null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Disukai> call, Throwable t) {
                            Log.e("Create Error : ", t.getMessage());
                            Toast.makeText(recipe_detail.this, "Data Gagal Disimpan", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(recipe_detail.this,"Data Saved Succesfully", Toast.LENGTH_LONG).show();
                    liked.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_liked), null);
                }
            }
        });


        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.recipe);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.recipe: return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0,0); return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0,0); return true;
                }
                return false;
            }
        });
    }
}