package techsayas.in.psctrolls.psctroll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Viewprofile extends AppCompatActivity {
ImageView photo,facebook,instagaram;
TextView  name ,email;
Intent as;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);


       as=getIntent();

        photo=findViewById(R.id.photos11);
        name=findViewById(R.id.name11);
        email=findViewById(R.id.email11);
        facebook=findViewById(R.id.facebook1);
        instagaram=findViewById(R.id.instagaram1);


        name.setText(as.getStringExtra("a"));
        Picasso.get().load(as.getStringExtra("b")).into(photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, v, as.getStringExtra("b"), null);

            }
        });



        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (facebook != null) {
                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    facebookIntent.setData(Uri.parse(as.getStringExtra("a")));
                    startActivity(facebookIntent);
                }
            }
        });

        DatabaseReference users = FirebaseDatabase.getInstance().getReference("MSG");
        Query query = users.orderByChild("messageUse").equalTo(as.getStringExtra("a"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);

                email.setText((CharSequence) user1);
               // Toast.makeText(Viewprofile.this,user1, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
