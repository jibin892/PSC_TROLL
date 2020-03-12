package techsayas.in.psctrolls.psctroll.ui.home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import techsayas.in.psctrolls.psctroll.ChatMessage;
import techsayas.in.psctrolls.psctroll.Comment;
import techsayas.in.psctrolls.psctroll.Homeview;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Upload;
import techsayas.in.psctrolls.psctroll.Userchatimage;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.widget.Toast.LENGTH_LONG;

public class HomeFragment extends Fragment {

    FirebaseListAdapter<Homeview> adapter;
    FloatingActionButton fab,cam;
    SweetAlertDialog pDialog;
    ListView listOfMessages;
    CircleImageView img;
    ImageView  download,share;
    Uri personPhoto;
    SwipeRefreshLayout swipe;

    GoogleSignInClient mGoogleSignInClient;
    String personName;
    String personId;
    String personEmail;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    // request code
    ImageView user;
    EditText somthing;
    TextView textView;
    ImageButton bookmark;
    DoubleTapLikeView mDoubleTapLikeView;
    DatabaseReference reference;

    private ShimmerFrameLayout mShimmerViewContainer;
    int j=0;
    private final int PICK_IMAGE_REQUEST = 71;
    private  MessageViewModel messageViewModel;
    View root;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        user=root.findViewById(R.id.photos123);
        somthing=root.findViewById(R.id.write123);
     //   mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);





        listOfMessages = (ListView) root.findViewById(R.id.list_of_view);

        listOfMessages.post(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                listOfMessages.smoothScrollToPosition(0);
            }
        });


        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

//            nameTV.setText(personName);
//            emailTV.setText(personEmail);
//           idTV.setText("ID: "+personGivenName);
            // Picasso.get().load(personPhoto).into(photo1);
            Picasso.get().load(personPhoto).into(user);


        }
somthing.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        Intent a=new Intent(getActivity(), Upload.class);
        startActivity(a);
    }
});


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        adapter = new FirebaseListAdapter<Homeview>(getActivity(), Homeview.class,
                R.layout.hompageview,

                reference =   FirebaseDatabase.getInstance().getReference().child("POST")) {
            @Override
            protected void populateView(View v, final Homeview model, int position) {
                // Get references to the views of message.xml
               final ImageView postimg= v.findViewById(R.id.post1);
                final TextView messageText = (TextView)v.findViewById(R.id.userdis);
                TextView messageUser = (TextView)v.findViewById(R.id.username);
                TextView messageTime = (TextView)v.findViewById(R.id.uploadtime);
                ImageButton comment = (ImageButton)v.findViewById(R.id.comment);

                ImageView image_message_profile=v.findViewById(R.id.userimg1);
                mDoubleTapLikeView = v.findViewById(R.id.layout_double_tap_like);
                textView=v.findViewById(R.id.yu);
                bookmark=v.findViewById(R.id.bookmark);
            download = (ImageView) v.findViewById(R.id.download);
                share = (ImageView) v.findViewById(R.id.share);


                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
              Picasso.get().load(model.getPhoto1()).resize(900, 550).centerCrop().into(postimg);
                messageTime.setText(model.getMessageTime());

comment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

Intent ash=new Intent(getActivity(), Comment.class);
ash.putExtra("a",model.getIdd());

startActivity(ash);


    }
});

                postimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, v, model.getPhoto1(), null);

                    }
                });
                if(readState())
                {
                   bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmark));
                }
                else
                {
                   bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmarked));
                }
             bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isFavourite = readState();

                        if (isFavourite) {
                         bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmarked));
                            isFavourite = false;
                            saveState(isFavourite);
                            Toast.makeText(getActivity(),"Favourite Troll Removed",Toast.LENGTH_LONG).show();

                        } else {
                     bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bookmark));
                            isFavourite = true;
                            saveState(isFavourite);
                            Toast.makeText(getActivity(),"Favourite Troll Added",Toast.LENGTH_LONG).show();

                        }
                    }
                });
               mDoubleTapLikeView.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
                    @Override
                    public void onDoubleTap(View view) {
// Toast.makeText(MainActivity.this, "Double TAPPED !", Toast.LENGTH_SHORT).show();

                        j=j+1;
                     textView.setText(j+"Likes");

                    }

                    @Override
                    public void onTap() {
// This method will be called if user didn't tap again after PRESS_TIME_TERM (default is 200)
// So keep PRESS_TIME_GAP short ( 200~400 )^.
// Due to Thread for single Tap, if you want to change UI through "onTap()", you should use Activity.runOnUiThread()

                    }
                });




             share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bm = ((android.graphics.drawable.BitmapDrawable) postimg.getDrawable()).getBitmap();
                        try {
                            java.io.File file = new java.io.File(getActivity().getExternalCacheDir() + "/image.jpg");
                            java.io.OutputStream out = new java.io.FileOutputStream(file);
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) { e.toString(); }
                        Intent iten = new Intent(android.content.Intent.ACTION_SEND);
                        iten.setType("*/*");
                        iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getActivity().getExternalCacheDir() + "/image.jpg")));
                        iten.putExtra(android.content.Intent.EXTRA_TEXT, model.getMessageText());
                        getActivity().startActivity(Intent.createChooser(iten, "Share Troll"));
                    }
                });




download.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        BitmapDrawable draw = (BitmapDrawable) postimg.getDrawable();
        Bitmap bitmap = draw.getBitmap();


        File sdCard = Environment.getExternalStorageDirectory().getAbsoluteFile();
        File dir = new File(sdCard.getAbsolutePath() + "/Psctrolls");
        dir.mkdirs();
        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        FileOutputStream outStream = null;
        Toast.makeText(getActivity(),"Troll Downloaded",Toast.LENGTH_LONG).show();
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        try {
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(outFile));
        getActivity().sendBroadcast(intent);
    }
});


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });


//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
//                         // Toast.makeText(getActivity(),"jghfg",LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//
//
//
//
//
//
//                });

                //  }

//                postimg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, v, model.getPhoto1(), null);
//
//                    }
//                });

            }
        };

        listOfMessages.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        return root;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        mShimmerViewContainer.startShimmerAnimation();
//    }
//
//    @Override
//    public void onPause() {
//        mShimmerViewContainer.stopShimmerAnimation();
//        super.onPause();
//
//    }

    private void saveState(boolean isFavourite) {
        SharedPreferences aSharedPreferences = getActivity().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.apply();
    }
    private boolean readState() {
        SharedPreferences aSharedPreferences = getActivity().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
    }


            }





