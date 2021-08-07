package id.ac.polman.astra.nim0320190008.leco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipe extends AppCompatActivity {
    private ResepService mResepService;
    private EditText mResep;
    private TextView mAlatBahan;
    private TextView mStep;
    private EditText mKeterangan;

    SharedPreferences sharedPreferences;
    private Integer id_user;
    private final static String APP_NAME= "LECO";
    private final static String ID = "id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_add);

        sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        id_user = sharedPreferences.getInt(ID, 0);

        mResep = findViewById(R.id.resep);
        mAlatBahan = findViewById(R.id.alatbahan);
        mStep = findViewById(R.id.step);
        mKeterangan = findViewById(R.id.keterangan);

        findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
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
            Call<Resep> call = mResepService.addResep(new Resep(0,idsp,recipe,alatbahan,step,0,keterangan,null,0));
            call.enqueue(new Callback<Resep>() {
                @Override
                public void onResponse(Call<Resep> call, Response<Resep> response) {
                    if(response != null) {
                        Toast.makeText(AddRecipe.this,"Data Saved Succesfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddRecipe.this,Recipe.class));
                    }
                }

                @Override
                public void onFailure(Call<Resep> call, Throwable t) {
                    Log.e("Create Error : ", t.getMessage());
                    Toast.makeText(AddRecipe.this, "Data Gagal Disimpan", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
