package techsayas.in.psctrolls.psctroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

public class Bookmarkview extends AppCompatActivity {
    FirebaseListAdapter<Profileview> adapter;
    FloatingActionButton fab,cam;
    SweetAlertDialog pDialog;
    ListView listOfMessages;
    CircleImageView img;
    ImageView download,share,ivEllipses;
    Uri personPhoto;
    SwipeRefreshLayout swipe;
    String personName;
    String personId;
    String personEmail;
    private Uri filePath;
    FirebaseStorage storage;
    TextView messageTime;
    StorageReference storageReference;
    // request code
//    ImageView user;
//    EditText somthing;
    ImageView  bookmark;
    TextView textView;
    //ImageButton bookmark;
    DoubleTapLikeView mDoubleTapLikeView;
    Query reference;
    Query reference1;

    private ShimmerFrameLayout mShimmerViewContainer;
    int j=0;
    private final int PICK_IMAGE_REQUEST = 71;
    private MessageViewModel messageViewModel;



    GoogleSignInClient mGoogleSignInClient;
    TextView sign_out;
    TextView nameTV;
    TextView emailTV;
    TextView idTV;
    ImageView photo, photo1;
    ImageView facebbok, google, instagram;
    EditText write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarkview);




        listOfMessages = (ListView) findViewById(R.id.bookmarkv);

        listOfMessages.post(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                listOfMessages.smoothScrollToPosition(0);
            }
        });

        sign_out = findViewById(R.id.log_out);
        nameTV = findViewById(R.id.name);
        emailTV = findViewById(R.id.email);
//        idTV = root.findViewById(R.id.id);
        photo = findViewById(R.id.photos);
        facebbok = findViewById(R.id.facebook);
        google = findViewById(R.id.google);
        instagram = findViewById(R.id.instagaram);
        //write = findViewById(R.id.write);
        // listOfMessages = (ListView)root.findViewById(R.id.list_of_messages1);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();




        }











        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        adapter = new FirebaseListAdapter<Profileview>(Bookmarkview.this, Profileview.class,
                R.layout.profileview,

                reference=  FirebaseDatabase.getInstance().getReference("BOOKMARK").orderByChild("id").equalTo(acct.getId())) {
            @Override
            protected void populateView(View v, final Profileview model, int position) {

                // Get references to the views of message.xml
                final ImageView postimg= v.findViewById(R.id.mypost);
                final TextView messageText = (TextView)v.findViewById(R.id.mydis1);
                TextView messageUser = (TextView)v.findViewById(R.id.myname);
                messageTime = (TextView)v.findViewById(R.id.mytime);
                ImageView comment = (ImageView) v.findViewById(R.id.comment);
                ImageView ivEllipses=v.findViewById(R.id.ivEllipses1);

                ImageView image_message_profile=v.findViewById(R.id.myimage);
                //mDoubleTapLikeView = v.findViewById(R.id.layout_double_tap_like);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).into(postimg);



                String s=String.valueOf(model.getStamp());
                s=s.substring(1);
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();//get your local time zone.
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                sdf.setTimeZone(tz);//set time zone.
                String localTime =sdf.format(new Date(Long.parseLong(String.valueOf(s))* 1000));
                Date date = new Date();
                try {
                    date = sdf.parse(localTime);//get local date
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(date == null) {
                    //  return null;
                }

                long time = date.getTime();

                Date curDate = currentDate();
                long now = curDate.getTime();
                if (time > now || time <= 0) {
                    //  return null;
                }

                float timeDIM = getTimeDistanceInMinutes(time);

                String timeAgo;

                if (timeDIM == 0) {
                    timeAgo = "just now";
                } else if (timeDIM == 1) {
                    //  return  "1 minute";
                    timeAgo="1 minute ago";
                } else if (timeDIM >= 2 && timeDIM <= 44) {
                    timeAgo = Math.round(timeDIM) + " minutes ago";
                } else if (timeDIM >= 45 && timeDIM <= 89) {
                    timeAgo = "1 hour ago";
                } else if (timeDIM >= 90 && timeDIM <= 1439) {
                    timeAgo =  + (Math.round(timeDIM / 60)) + " hours ago";
                } else if (timeDIM >= 1440 && timeDIM <= 2519) {
                    timeAgo = "1 day ago";
                } else if (timeDIM >= 2520 && timeDIM <= 43199) {
                    timeAgo = (Math.round(timeDIM / 1440)) + " days ago";
                } else if (timeDIM >= 43200 && timeDIM <= 86399) {
                    timeAgo = " 1 month ago";
                } else if (timeDIM >= 86400 && timeDIM <= 525599) {
                    timeAgo = (Math.round(timeDIM / 43200)) + " months ago";
                } else if (timeDIM >= 525600 && timeDIM <= 655199) {
                    timeAgo = " year ago";
                } else if (timeDIM >= 655200 && timeDIM <= 914399) {
                    timeAgo = "year ago";
                } else if (timeDIM >= 914400 && timeDIM <= 1051199) {
                    timeAgo = "2 years ago";
                } else {
                    timeAgo = "about " + (Math.round(timeDIM / 525600)) + " years ago";
                }

                // return timeAgo + " ago";

                messageTime.setText(timeAgo);







                ivEllipses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {





                        AlertDialog.Builder builder = new AlertDialog.Builder(Bookmarkview.this);
// add a li
                        String[] animals = {"Share", "Download","Remove Favourite"};
                        builder.setItems(animals, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {

                                        Bitmap bm = ((android.graphics.drawable.BitmapDrawable) postimg.getDrawable()).getBitmap();
                                        try {
                                            java.io.File file = new java.io.File( getExternalCacheDir() + "/image.jpg");
                                            java.io.OutputStream out = new java.io.FileOutputStream(file);
                                            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                            out.flush();
                                            out.close();
                                        } catch (Exception e) { e.toString(); }
                                        Intent iten = new Intent(android.content.Intent.ACTION_SEND);
                                        iten.setType("*/*");
                                        iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File( getExternalCacheDir() + "/image.jpg")));
                                        iten.putExtra(android.content.Intent.EXTRA_TEXT, model.getMessageText());
                                        getApplicationContext().startActivity(Intent.createChooser(iten, "Share Troll"));





                                    }
                                    break;
                                    case 1:
                                    {

                                        BitmapDrawable draw = (BitmapDrawable) postimg.getDrawable();
                                        Bitmap bitmap = draw.getBitmap();


                                        File sdCard = Environment.getExternalStorageDirectory().getAbsoluteFile();
                                        File dir = new File(sdCard.getAbsolutePath() + "/Psctrolls");
                                        dir.mkdirs();
                                        String fileName = String.format("%d.jpg", System.currentTimeMillis());
                                        File outFile = new File(dir, fileName);
                                        FileOutputStream outStream = null;
                                        Toast.makeText( getApplicationContext(),"Troll Downloaded",Toast.LENGTH_LONG).show();
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
                                        getApplicationContext().sendBroadcast(intent);



                                    }break;

                                    case 2:
                                    {

                                        // Toast.makeText(getActivity(),model.getIdd(),LENGTH_LONG).show();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query applesQuery = ref.child("BOOKMARK").orderByChild("idd").equalTo(model.getIdd());
                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                    Crouton.makeText(Bookmarkview.this,"Troll Added As Removed", Style.ALERT).show();

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Log.e(TAG, "onCancelled", databaseError.toException());
                                            }
                                        });


                                    }
                                }
                            }
                        });
// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();















                    }
                });







                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent ash=new Intent(getApplicationContext(), Comment.class);
                        ash.putExtra("a",model.getPostid());
//Toast.makeText(getActivity(),model.getPostid(), LENGTH_LONG).show();
                        startActivity(ash);


                    }
                });

                postimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, model.getPhoto1(), null);

                    }
                });







                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                        if (dataSnapshot.exists()){

                            Crouton.makeText(Bookmarkview.this,"YOUR FAVARITES", Style.CONFIRM).show();



                        }

                        else{


                            Crouton.makeText(Bookmarkview.this,"NO ITEM FOUND", Style.ALERT).show();



                        }







                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });



            }
        };

        listOfMessages.setAdapter(adapter);

        adapter.notifyDataSetChanged();





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
        SharedPreferences aSharedPreferences = getApplicationContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferencesEdit = aSharedPreferences
                .edit();
        aSharedPreferencesEdit.putBoolean("State", isFavourite);
        aSharedPreferencesEdit.apply();
    }
    private boolean readState() {
        SharedPreferences aSharedPreferences = getApplicationContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferences.getBoolean("State", true);
    }


    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

}













