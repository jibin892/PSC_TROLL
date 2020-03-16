package techsayas.in.psctrolls.psctroll;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> values,photo1;
    public RecyclerViewAdapter(ArrayList<String> photo1) {
        this.values = values;
        this.photo1 = photo1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hompageview,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    //  holder.pic.setText(values.get(position));
        Picasso.get().load(values.get(position)).resize(400, 400).centerCrop().into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return photo1.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        ViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.post1);
        }
    }
}