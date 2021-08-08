package id.ac.polman.astra.nim0320190008.leco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.ApiUtils;
import id.ac.polman.astra.nim0320190008.leco.api.DisukaiService;
import id.ac.polman.astra.nim0320190008.leco.api.Resep;
import id.ac.polman.astra.nim0320190008.leco.api.ResepService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterHolder> implements Filterable {

    SharedPreferences sharedPreferences;
    private Integer id_user;
    private final static String APP_NAME= "LECO";
    private final static String ID = "id";
    private DisukaiService mDisukaiService;
    private Context mContext;
    private List<Resep> mReseps;
    private String pathImage = "https://asset.kompas.com/crops/YKSBLbCigyp8uVtrfdqq57cS4Is=/0x3:977x654/750x500/data/photo/2020/06/30/5efaf91e0ec2c.jpg";
    private List<Resep> mResepAll;
    private String mText;

    public Adapter(List<Resep> dataList, Context context, String text){
        this.mReseps = dataList;
        this.mText = text;
        this.mContext = context;
        this.mResepAll = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public Adapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        mContext = parent.getContext();
//        View view =  LayoutInflater.from(mContext).inflate(R.layout.recipe_list, parent, false);
//        AdapterHolder holder = new AdapterHolder(view);
//        return holder;

        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list, parent, false);
        AdapterHolder holder = new AdapterHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        final Resep resep = mReseps.get(position);


        String title = resep.getNama();
        String desk = resep.getKeterangan();
        String image = resep.getFoto();
        holder.title.setText(title);
        holder.keterangan.setText(desk);


        sharedPreferences = mContext.getSharedPreferences(APP_NAME, MODE_PRIVATE);
        id_user = sharedPreferences.getInt(ID, 0);
        mDisukaiService = ApiUtils.getDisukaiService();
        Call<List<Resep>> call = mDisukaiService.getDisukaiByID(id_user);
        call.enqueue(new Callback<List<Resep>>() {
            @Override
            public void onResponse(Call<List<Resep>> call, Response<List<Resep>> response) {
                if(response.isSuccessful()){
                    boolean liked = false;
                    List<Resep> reseps = response.body();
                    for( Resep r : reseps){
                        if(resep.getId().equals(r.getId())){liked = true;}
                    }
                    if(liked){
                        holder.keterangan.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext,R.drawable.ic_like), null);
                    }else {
                        holder.keterangan.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.ic_liked), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Resep>> call, Throwable t) {
                Log.e("Get Resep Error : ", t.getMessage());
//                Toast.makeText(Recipe.this, "Gagal Get Data!", Toast.LENGTH_LONG).show();
            }
        });


        if (image == null || image.equals("")) {
        Glide.with(holder.itemView.getContext()).load(pathImage)
                .apply(new RequestOptions().fitCenter())
                .into(holder.foto);
        } else {
            holder.foto.setImageBitmap(Picture.convertToImage(image));
        }

        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mText.equals("Dashboard")){
                    Intent intent = new Intent(mContext, recipe_detail.class);
                    intent.putExtra("data", resep);
                    intent.putExtra("fav", holder.fav.getText().toString());
                    mContext.startActivity(intent);
                }else{
                    Log.e("Adapter","Berhasil Masuk Edit");

                    Intent intent = new Intent(mContext, EditRecipe.class);
                    intent.putExtra("id", resep.getId());
                    intent.putExtra("nama", resep.getNama());
                    intent.putExtra("alat", resep.getAlat_bahan());
                    intent.putExtra("tahap",resep.getTahap());
                    intent.putExtra("ket", resep.getKeterangan());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReseps.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Resep> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(mResepAll);
            }else{
                for (Resep r : mResepAll){
                    if(r.getNama().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(r);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //runs on a ui
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mReseps.clear();
            mReseps.addAll((Collection<? extends Resep>) results.values);
            notifyDataSetChanged();
        }
    };


    public class AdapterHolder extends RecyclerView.ViewHolder {
        TextView title, keterangan, fav;
        ImageView foto;
        RelativeLayout select;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recipe_list_title);
            keterangan = itemView.findViewById(R.id.recipe_list_desc);
            foto = itemView.findViewById(R.id.recipe_list_image);
            fav = itemView.findViewById(R.id.recipe_liked);
            select = itemView.findViewById(R.id.selectResep);
        }

//        @Override
//        public void onClick(View v) {
//
//            Log.e("Adapter","Berhasil Masuk Edit");
//
//            Intent intent = new Intent(mContext, EditRecipe.class);
//            intent.putExtra("id", mResep.getId());
//            intent.putExtra("nama", mResep.getNama());
//            intent.putExtra("alat", mResep.getAlat_bahan());
//            intent.putExtra("tahap",mResep.getTahap());
//            intent.putExtra("ket", mResep.getKeterangan());
//            mContext.startActivity(intent);
//
//        }
    }
}
