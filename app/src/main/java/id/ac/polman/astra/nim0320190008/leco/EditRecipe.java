package id.ac.polman.astra.nim0320190008.leco;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRecipe extends AppCompatActivity {
    private ResepService mResepService;
    private EditText mEditResep;
    private EditText mEditAlatBahan;
    private EditText mEditStep;
    private EditText mEditKeterangan;
    private Integer id_resep;

    SharedPreferences sharedPreferences;
    private Integer id_user;
    private final static String APP_NAME= "LECO";
    private final static String ID = "id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_edit);
        sharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);

        //GetData
        id_resep = getIntent().getIntExtra("id",0);
        id_user = sharedPreferences.getInt(ID, 0);
        String ResepEdit = getIntent().getStringExtra("nama");
        String AlatBahanEdit = getIntent().getStringExtra("alat");
        String StepEdit = getIntent().getStringExtra("tahap");
        String KetEdit = getIntent().getStringExtra("ket");

        mEditResep = findViewById(R.id.editresep);
        mEditAlatBahan = findViewById(R.id.editalatbahan);
        mEditStep = findViewById(R.id.editstep);
        mEditKeterangan = findViewById(R.id.editketerangan);


        mEditResep.setText(ResepEdit);
        mEditAlatBahan.setText(AlatBahanEdit);
        mEditStep.setText(StepEdit);
        mEditKeterangan.setText(KetEdit);

        findViewById(R.id.btnEditSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifRecipe();
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

    private void modifRecipe(){
        String recipe = mEditResep.getText().toString().trim();
        String alatbahan = mEditAlatBahan.getText().toString().trim();
        String step = mEditStep.getText().toString().trim();
        String keterangan = mEditKeterangan.getText().toString().trim();
        Integer idsp = sharedPreferences.getInt(ID, 0);
        Integer Id = id_resep;

        Log.i("IdResep", "Id resep ="+id_resep);


        if(recipe.isEmpty()) {
            mEditResep.setError("Harap mengisi Field Resep");
            mEditResep.requestFocus();
            return;
        } else if (alatbahan.isEmpty()) {
            mEditAlatBahan.setError("Harap mengisi Field Alat Bahan");
            mEditAlatBahan.requestFocus();
            return;
        } else if (step.isEmpty()) {
            mEditStep.setError("Harap mengisi Field Tahapan");
            mEditStep.requestFocus();
            return;
        } else if (keterangan.isEmpty()) {
            mEditKeterangan.setError("Harap mengisi Field Keterangan");
            mEditKeterangan.requestFocus();
            return;
        } else {
            mResepService = ApiUtils.getResepService();
            Call<Resep> call = mResepService.updateResep(new Resep(Id,idsp,recipe,alatbahan,step,0,keterangan,null,0));
            call.enqueue(new Callback<Resep>() {
                @Override
                public void onResponse(Call<Resep> call, Response<Resep> response) {
                    if(response != null){
                        Toast.makeText(EditRecipe.this, "Data saved successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EditRecipe.this, Recipe.class));
                    }
                }

                @Override
                public void onFailure(Call<Resep> call, Throwable t) {
                    Log.e("Update Error : ", t.getMessage());
                    Toast.makeText(EditRecipe.this, "Data gagal tersimpan!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
