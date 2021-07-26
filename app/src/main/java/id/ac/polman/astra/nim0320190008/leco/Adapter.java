package id.ac.polman.astra.nim0320190008.leco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import id.ac.polman.astra.nim0320190008.leco.api.Resep;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterHolder> {

    private Context mContext;
    private List<Resep> mReseps;
    private String pathImage = "https://asset.kompas.com/crops/YKSBLbCigyp8uVtrfdqq57cS4Is=/0x3:977x654/750x500/data/photo/2020/06/30/5efaf91e0ec2c.jpg";

    public Adapter(Context context, List<Resep> dataList){
        this.mContext = context;
        this.mReseps = dataList;
    }

    @NonNull
    @Override
    public Adapter.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list, parent, false);
        AdapterHolder holder = new AdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.AdapterHolder holder, int position) {
        final Resep resep = mReseps.get(position);
        String title = resep.getNama();
        String desk = resep.getKeterangan();
        String image = resep.getFoto();
        holder.title.setText(title);
        holder.keterangan.setText(desk);

        Glide.with(holder.itemView.getContext()).load(pathImage)
                .apply(new RequestOptions().fitCenter())
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return mReseps.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
        TextView title, keterangan;
        ImageView foto;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recipe_list_title);
            keterangan = itemView.findViewById(R.id.recipe_list_desc);
            foto = itemView.findViewById(R.id.recipe_list_image);
        }
    }
}
