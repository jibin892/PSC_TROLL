package techsayas.in.psctrolls.psctroll;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtUserName,txtUserSurname,txtUserNumber;


    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    @Override
    public void onClick(View v) {

    }
}
