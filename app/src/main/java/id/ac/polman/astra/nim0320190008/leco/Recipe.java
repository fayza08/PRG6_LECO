package id.ac.polman.astra.nim0320190008.leco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Recipe extends AppCompatActivity {
    private ResepService mResepService;
    private EditText mResep;
    private EditText mAlatBahan;
    private EditText mStep;
    private EditText mKeterangan;

    Adapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    SharedPreferences sharedPreferences;
    private Integer id_user;
    private final static String APP_NAME= "LECO";
    private final static String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        id_user = sharedPreferences.getInt(ID, 0);


        mRecyclerView = findViewById(R.id.recipeList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        findViewById(R.id.addRecipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, AddRecipe.class));
            }
        });

        mResepService = ApiUtils.getResepService();
        Call<List<Resep>> call = mResepService.getResepByUser(id_user);

        call.enqueue(new Callback<List<Resep>>() {
            @Override
            public void onResponse(Call<List<Resep>> call, Response<List<Resep>> response) {
                if(response.isSuccessful()){
                    List<Resep> posts = response.body();
                    mAdapter = new Adapter(Recipe.this, posts);
                    mRecyclerView.setAdapter(mAdapter);
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Resep>> call, Throwable t) {
                Log.e("Get Resep Error : ", t.getMessage());
                Toast.makeText(Recipe.this, "Gagal Get Data!", Toast.LENGTH_LONG).show();
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

    private void saveRecipe() {
        String recipe = mResep.getText().toString().trim();
        String alatbahan = mAlatBahan.getText().toString().trim();
        String step = mStep.getText().toString().trim();
        String keterangan = mKeterangan.getText().toString().trim();
        Integer idsp = sharedPreferences.getInt(ID, 0);

        if(recipe.isEmpty()) {
            mResep.setError("Harap mengisi Field Resep");
            mResep.requestFocus();
            return;
        } else if (alatbahan.isEmpty()) {
            mAlatBahan.setError("Harap mengisi Field Alat Bahan");
            mAlatBahan.requestFocus();
            return;
        } else if (step.isEmpty()) {
            mStep.setError("Harap mengisi Field Tahapan");
            mStep.requestFocus();
            return;
        } else if (keterangan.isEmpty()) {
            mKeterangan.setError("Harap mengisi Field Keterangan");
            mKeterangan.requestFocus();
            return;
        } else {
            mResepService = ApiUtils.getResepService();
            Call<Resep> call = mResepService.addResep(new Resep(0,idsp,recipe,alatbahan,step,1,keterangan,null,0));
            call.enqueue(new Callback<Resep>() {
                @Override
                public void onResponse(Call<Resep> call, Response<Resep> response) {
                    if(response != null) {
                        Toast.makeText(Recipe.this,"Data Saved Succesfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Recipe.this,Login.class));
                    }
                }

                @Override
                public void onFailure(Call<Resep> call, Throwable t) {
                    Log.e("Create Error : ", t.getMessage());
                    Toast.makeText(Recipe.this, "Data Gagal Disimpan", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}