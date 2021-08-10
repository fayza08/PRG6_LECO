package id.ac.polman.astra.nim0320190008.leco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EditRecipe extends AppCompatActivity {
    private ResepService mResepService;
    private EditText mEditResep;
    private EditText mEditAlatBahan;
    private EditText mEditStep;
    private EditText mEditKeterangan;
    private Integer id_resep;
    private ImageButton mPhotoButton;
    private ImageButton mGalleryButton;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private Uri mPhotoUri;
    private static final int REQUEST_PHOTO = 2;
    private ActivityResultLauncher<Intent> activityResultLauncherCamera, activityResultLauncherGallery;


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
        String FotoEdit = getIntent().getStringExtra("foto");

        mEditResep = findViewById(R.id.editresep);
        mEditAlatBahan = findViewById(R.id.editalatbahan);
        mEditStep = findViewById(R.id.editstep);
        mEditKeterangan = findViewById(R.id.editketerangan);
        mPhotoButton =  findViewById(R.id.recipe_camera);
        mPhotoView = findViewById(R.id.resep_photo);
        mGalleryButton = findViewById(R.id.recipeedit_gallery);

        mEditAlatBahan.setMovementMethod(new ScrollingMovementMethod());
        mEditStep.setMovementMethod(new ScrollingMovementMethod());

        mEditResep.setText(ResepEdit);
        mEditAlatBahan.setText(AlatBahanEdit);
        mEditStep.setText(StepEdit);
        mEditKeterangan.setText(KetEdit);

        if (FotoEdit == null || FotoEdit.equals("")) {

        } else {
            mPhotoView.setImageBitmap(Picture.convertToImage(FotoEdit));
        }

        Log.e("Photo", "Isinya = " +FotoEdit);

        findViewById(R.id.btnEditSimpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifRecipe();
            }
        });

        PackageManager packageManager = getPackageManager();
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

        activityResultLauncherGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImage = result.getData().getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (selectedImage != null) {
                    Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);

                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        mPhotoView.setImageBitmap(bitmap);
                        Log.e("Bitmap Gallery" , "Adalah : " +bitmap);
                        cursor.close();
                    }
                }
            }
        });

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncherCamera.launch(intentTakePicture);
            }
        });

        mGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherGallery.launch(intentFromGallery);
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

    private void modifRecipe(){
        String recipe = mEditResep.getText().toString().trim();
        String alatbahan = mEditAlatBahan.getText().toString().trim();
        String step = mEditStep.getText().toString().trim();
        String keterangan = mEditKeterangan.getText().toString().trim();
        Integer idsp = sharedPreferences.getInt(ID, 0);
        Integer Id = id_resep;
        String foto = "";
        try{
            Bitmap bitmap = Picture.Compress(((BitmapDrawable)mPhotoView.getDrawable()).getBitmap(),50);
            foto = (Picture.convertToString(bitmap));
        }catch (Exception e){
            Log.e("RecipeAdd", "gagal menyimpan foto" + e.getMessage());
        }
        Log.i("IdResep", "Id resep ="+id_resep);
        if(recipe.isEmpty()) {
            mEditResep.setError("Please fill this field!");
            mEditResep.requestFocus();
            return;
        } else if (alatbahan.isEmpty()) {
            mEditAlatBahan.setError("Please fill this field!");
            mEditAlatBahan.requestFocus();
            return;
        } else if (step.isEmpty()) {
            mEditStep.setError("Please fill this field!");
            mEditStep.requestFocus();
            return;
        } else if (keterangan.isEmpty()) {
            mEditKeterangan.setError("Please fill this field!");
            mEditKeterangan.requestFocus();
            return;
        } else {
            mResepService = ApiUtils.getResepService();
            Call<Resep> call = mResepService.updateResep(new Resep(Id,idsp,recipe,alatbahan,step,0,keterangan,foto,0));
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
