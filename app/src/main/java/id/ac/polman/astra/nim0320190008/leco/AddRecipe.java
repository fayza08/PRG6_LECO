package id.ac.polman.astra.nim0320190008.leco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.Date;
import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AddRecipe extends AppCompatActivity {
    private ResepService mResepService;
    private EditText mResep;
    private EditText mAlatBahan;
    private EditText mStep;
    private EditText mKeterangan;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Uri mPhotoUri;
    private static final int REQUEST_PHOTO = 2;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera;

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
        mPhotoButton =  findViewById(R.id.recipe_camera);
        mPhotoView = findViewById(R.id.resep_photo);

        PackageManager packageManager = getPackageManager();

        findViewById(R.id.btnSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });

        //Foto
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(packageManager.resolveActivity(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY) == null){
            mPhotoButton.setEnabled(false);
        }

        activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                mPhotoView.setImageBitmap(bitmap);
            }
        });

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncherCamera.launch(intentTakePicture);
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

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        mPhotoFile = new File(getApplicationContext().getFilesDir(), "foto_");
        mPhotoUri = FileProvider.getUriForFile(this.getApplicationContext(),
                "id.ac.polman.astra.nim0320190008.leco",
                mPhotoFile);
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_PHOTO && resultCode == RESULT_OK && data != null){
            revokeUriPermission(mPhotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
             Bundle bundle = data.getExtras();
             Bitmap bitmap =(Bitmap) bundle.get("data");
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void saveRecipe() {
        String recipe = mResep.getText().toString().trim();
        String alatbahan = mAlatBahan.getText().toString().trim();
        String step = mStep.getText().toString().trim();
        String keterangan = mKeterangan.getText().toString().trim();
        String foto = "";
        Integer idsp = sharedPreferences.getInt(ID, 0);
        try{
            foto = (Picture.convertToString(((BitmapDrawable)mPhotoView.getDrawable()).getBitmap()));
        }catch (Exception e){
            Log.e("RecipeAdd", "gagal menyimpan foto" + e.getMessage());
        }

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
            Call<Resep> call = mResepService.addResep(new Resep(0,idsp,recipe,alatbahan,step,0,keterangan,foto,0));
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
