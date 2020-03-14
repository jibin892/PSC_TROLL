package techsayas.in.psctrolls.psctroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.ui.notifications.NotificationsFragment;

public class Viewotherprofile extends AppCompatActivity {
Intent aw;
    ImageView photo, photo1;
    ImageView facebbok, google, instagram;
    EditText write;
    GridView listOfMessages;
    View root;
    String personId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Query mDatabaseReference ;
    private RecyclerView mRecyclerView;
    TextView nameTV;
    TextView emailTV;
    private StaggeredGridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewotherprofile);

        nameTV = findViewById(R.id.othrname);
        emailTV = findViewById(R.id.othremail);
//        idTV = root.findViewById(R.id.id);
        photo = findViewById(R.id.otherphoto);
        aw=getIntent();
        Crouton.makeText(Viewotherprofile.this,aw.getStringExtra("name"), Style.INFO).show();
        nameTV.setText(aw.getStringExtra("name"));
        emailTV.setText(aw.getStringExtra("email"));
//            idTV.setText("ID: "+personGivenName);
        //  Picasso.get().load(personPhoto).into(photo1);
        Picasso.get().load(aw.getStringExtra("photo")).into(photo);
        mRecyclerView = (RecyclerView) findViewById(R.id.orheview);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Say Hello to our new Firebase UI Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<Otherptofilevisit, Viewotherprofile.other> adapter = new FirebaseRecyclerAdapter<Otherptofilevisit, Viewotherprofile.other>(
                Otherptofilevisit.class,
                R.layout.otyherptofileview,
                Viewotherprofile.other.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference=  FirebaseDatabase.getInstance().getReference("POST").orderByChild("id").equalTo(aw.getStringExtra("id"))) {
            @Override
            protected void populateViewHolder(Viewotherprofile.other viewHolder, final Otherptofilevisit model, int position) {


                //  Picasso.with(getActivity()).load(model.getMoviePoster()).into(viewHolder.ivMoviePoster);
                Picasso.get().load(model.photo1).resize(400, 400).centerCrop().into(viewHolder.pics);


                //Crouton.makeText(Viewotherprofile.this,model.photo1, Style.INFO).show();


                viewHolder.pics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent a= new Intent(getApplicationContext(), Viewuploaded.class);
                        a.putExtra("ab",model.photo1);
                        a.putExtra("abc",model.getId());
                        startActivity(a);




                    }
                });


            }
        };

        mRecyclerView.setAdapter(adapter);


    }
    public static class other extends RecyclerView.ViewHolder{

        TextView tvMovieName;
        RatingBar ratingBar;
        ImageView pics;
        public other(View v) {
            super(v);
            pics = (ImageView) v.findViewById(R.id.otater1);
        }}
}
