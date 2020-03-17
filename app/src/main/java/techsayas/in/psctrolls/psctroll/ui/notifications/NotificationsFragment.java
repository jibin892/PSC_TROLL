package techsayas.in.psctrolls.psctroll.ui.notifications;



import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import techsayas.in.psctrolls.psctroll.Bookmarkview;
import techsayas.in.psctrolls.psctroll.Login;
import techsayas.in.psctrolls.psctroll.Movie;
import techsayas.in.psctrolls.psctroll.Mypost;
import techsayas.in.psctrolls.psctroll.Psc_notification;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Upload;
import techsayas.in.psctrolls.psctroll.Viewuploaded;


public class NotificationsFragment extends Fragment  implements View.OnClickListener {
    GoogleSignInClient mGoogleSignInClient;
    TextView sign_out;
    TextView nameTV;
    TextView emailTV;
    TextView idTV;
    ImageView photo, photo1;
    EditText write;
    GridView listOfMessages;
    View root;
    BottomSheetBehavior behavior;
    RecyclerView recyclerView;
    private BottomSheetDialog bottomSheetDialog;
    String personId;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Query mDatabaseReference;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    // FirebaseListAdapter<Homeview> adapter;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.di, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        View shareView = bottomSheetDialogView.findViewById(R.id.share);
        View getLinkView = bottomSheetDialogView.findViewById(R.id.get_link);
        View editNameView = bottomSheetDialogView.findViewById(R.id.bokkmarkpage);
        View deleteView = bottomSheetDialogView.findViewById(R.id.delete);


        View facebook = bottomSheetDialogView.findViewById(R.id.facebook);

        View instagaram = bottomSheetDialogView.findViewById(R.id.instagaram);

        View twiterew = bottomSheetDialogView.findViewById(R.id.google);

        shareView.setOnClickListener(this);
        getLinkView.setOnClickListener(this);
        editNameView.setOnClickListener(this);
        deleteView.setOnClickListener(this);
        facebook.setOnClickListener(this);
        instagaram.setOnClickListener(this);
        twiterew.setOnClickListener(this);

     ImageView d = root.findViewById(R.id.imageView2);

        d.setOnClickListener(this);


        nameTV = root.findViewById(R.id.name);
        emailTV = root.findViewById(R.id.email);
//        idTV = root.findViewById(R.id.id);
        photo = root.findViewById(R.id.photos);
       // write = root.findViewById(R.id.write);
        // listOfMessages = (ListView)root.findViewById(R.id.list_of_messages1);








        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            nameTV.setText(personName);
            emailTV.setText(personEmail);
//            idTV.setText("ID: "+personGivenName);
          //  Picasso.get().load(personPhoto).into(photo1);
            Picasso.get().load(personPhoto).into(photo);
        }
        mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Say Hello to our new Firebase UI Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<Movie,MovieViewHolder> adapter = new FirebaseRecyclerAdapter<Movie, MovieViewHolder>(
                Movie.class,
                R.layout.movie_board_item,
                MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference=  FirebaseDatabase.getInstance().getReference("POST").orderByChild("id").equalTo(acct.getId())) {
            @Override
            protected void populateViewHolder(MovieViewHolder viewHolder, final Movie model, int position) {


                //  Picasso.with(getActivity()).load(model.getMoviePoster()).into(viewHolder.ivMoviePoster);
                Picasso.get().load(model.photo1).resize(400, 400).centerCrop().into(viewHolder.ivMoviePoster);



//Toast.makeText(getActivity(),model.getId(), Toast.LENGTH_LONG).show();

               viewHolder.ivMoviePoster.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Intent a= new Intent(getActivity(), Viewuploaded.class);
                       a.putExtra("ab",model.getPhoto1());
                       a.putExtra("abc",model.getId());
                       startActivity(a);




                   }
               });





            }
        };

        mRecyclerView.setAdapter(adapter);

        return root;
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(),"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Login.class));

                    }
                });

    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMoviePoster;
        TextView tvMovieName;
        RatingBar ratingBar;

        public MovieViewHolder(View v) {
            super(v);
            ivMoviePoster = (ImageView) v.findViewById(R.id.iv_movie_poster);
        }

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.imageView2:
                bottomSheetDialog.show();
                break;

            case R.id.share:
                Intent abb=new Intent(getActivity(), Psc_notification.class);
                startActivity(abb);
                break;

            case R.id.get_link:

                Intent ab=new Intent(getActivity(), Mypost.class);
                startActivity(ab);


                break;

            case R.id.bokkmarkpage:
                Intent a=new Intent(getActivity(), Bookmarkview.class);
                startActivity(a);
              //  Toast.makeText(getActivity(), "Edit name", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete:

                signOut();



                Toast.makeText(getActivity(), "Delete collection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.facebook:

             faebook();

                break;
            case R.id.instagaram:

                insta();

                break;
            case R.id.google:

                goog();

                break;

        }

    }

    private void goog() {


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/pscTrolls"));
        startActivity(intent);


    }

    private void insta() {




        Uri uri = Uri.parse("https://www.instagram.com/psctrolls/?hl=en");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/psctrolls/?hl=en")));
        }




    }

    private void faebook() {



        String facebookUrl = "https://www.facebook.com/techsayssoftwaresolutions/?modal=admin_todo_tour";
        String facebookID = "1739755902960987";

        try {
            int versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if (!facebookID.isEmpty()) {
                // open the Facebook app using facebookID (fb://profile/facebookID or fb://page/facebookID)
                Uri uri = Uri.parse("fb://page/" + facebookID);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else if (versionCode >= 3002850 && !facebookUrl.isEmpty()) {
                // open Facebook app using facebook url
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // Facebook is not installed. Open the browser
                Uri uri = Uri.parse(facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            Uri uri = Uri.parse(facebookUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }




    }

}




