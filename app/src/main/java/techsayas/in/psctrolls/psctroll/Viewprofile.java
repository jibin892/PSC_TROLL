package techsayas.in.psctrolls.psctroll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
ImageView photo;
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


        name.setText(as.getStringExtra("a"));
        Picasso.get().load(as.getStringExtra("b")).into(photo);


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
