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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.widget.Toast.LENGTH_LONG;

public class Viewuploaded extends AppCompatActivity {
    FirebaseListAdapter<Uploadedview> adapter;
    FloatingActionButton fab,cam;
    SweetAlertDialog pDialog;
    ListView listOfMessages;
    CircleImageView img;
    ImageView  download,share,ivEllipses;
    Uri personPhoto;
    SwipeRefreshLayout swipe;
    ImageView imgh,imgnh;
    GoogleSignInClient mGoogleSignInClient;
    String personName;
    String personId;
    String personEmail;
    private Uri filePath;
    FirebaseStorage storage;
    TextView messageTime;
    StorageReference storageReference;
    // request code
//    ImageView user;
    ImageView chosee;
//    EditText somthing;
    Intent a;
    ImageView  bookmark;
    TextView comme;
    TextView textView;
    //ImageButton bookmark;
    DoubleTapLikeView mDoubleTapLikeView;
    Query reference;
    Query reference1;
FirebaseDatabase  firebaseDatabase;
    private ShimmerFrameLayout mShimmerViewContainer;
    int j=0;
    private final int PICK_IMAGE_REQUEST = 71;
    private  MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewuploaded);
a=getIntent();
String as=a.getStringExtra("abc");
//Toast.makeText(getApplicationContext(),as,LENGTH_LONG).show();
        listOfMessages = (ListView)findViewById(R.id.mypostview);

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
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
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
            // Picasso.get().load(personPhoto).into(user);


        }

//somthing.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//
//        Intent a=new Intent(getActivity(), Upload.class);
//        startActivity(a);
//    }
//});





        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        adapter = new FirebaseListAdapter<Uploadedview>(Viewuploaded.this, Uploadedview.class,
                R.layout.myprofilepost,
                reference =   FirebaseDatabase.getInstance().getReference("POST").orderByChild("id").equalTo(as))
        {
            @Override
            protected void populateView(View v, final Uploadedview model, int position) {





                // Get references to the views of message.xml
                final ImageView postimg= v.findViewById(R.id.post12);
                final TextView messageText = (TextView)v.findViewById(R.id.userdis12);
                TextView messageUser = (TextView)v.findViewById(R.id.username12);
                messageTime = (TextView)v.findViewById(R.id.uploadtime12);
                ImageView comment = (ImageView) v.findViewById(R.id.comment12);
             chosee=v.findViewById(R.id.chosee12);

                ImageView image_message_profile=v.findViewById(R.id.userimg12);
                //mDoubleTapLikeView = v.findViewById(R.id.layout_double_tap_like);
                textView=v.findViewById(R.id.yu12);
                bookmark=v.findViewById(R.id.bookmark12);
                comme = (TextView)v.findViewById(R.id.image_comments_link12);


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
                    timeAgo = (Math.round(timeDIM)) + " minutes ago";
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





                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQueryy = ref.child("COMMENT").orderByChild("postid").equalTo(model.getIdd());




                applesQueryy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //    Toast.makeText(getActivity(),"yes",LENGTH_LONG).show();
                        comme.setText("ghfhgf");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

chosee.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Viewuploaded.this);
// add a li
        String[] animals = {"Share", "Download","Delet Post"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {

                        Bitmap bm = ((android.graphics.drawable.BitmapDrawable) postimg.getDrawable()).getBitmap();
                        try {
                            java.io.File file = new java.io.File(getExternalCacheDir() + "/image.jpg");
                            java.io.OutputStream out = new java.io.FileOutputStream(file);
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) { e.toString(); }
                        Intent iten = new Intent(android.content.Intent.ACTION_SEND);
                        iten.setType("*/*");
                        iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
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
                        Toast.makeText(getApplicationContext(),"Troll Downloaded",Toast.LENGTH_LONG).show();
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
                    case 2:{

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child("POST").orderByChild("specialid").equalTo(model.getIdd()+personId);
                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                    //    Toast.makeText(getActivity(),"jghfg",LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });



                    }break;

                }
            }
        });
// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();












    }
});

                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("BOOKMARK");
                        Query applesQueryw = ref.orderByChild("bookmarkid").equalTo(model.getIdd()+personId);

                        applesQueryw.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    Crouton.makeText(Viewuploaded.this,"Troll Already Added As Favourite", Style.INFO).show();
                                } else {
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("BOOKMARK").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Like", 1);
                                    map.put("photo",model.getPhoto());
                                    map.put("messageUser", model.getMessageUser());
                                    map.put("id", personId);
                                    map.put("photo1", model.getPhoto1());
                                    map.put("user", model.getMessageUser());
                                    String mGroupId = rootRef.push().getKey();
                                    map.put("idd", mGroupId);
                                    map.put("stamp", model.getStamp());
                                    map.put("bookmarkid", model.getIdd()+acct.getId());
                                    map.put("postid",model.getIdd());
                                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                                    map.put("messageTime", currentTime);
                                    namesRef.updateChildren(map);
                                    rootRef.child("BOOKMARK");

                                    // bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            reference1 = FirebaseDatabase.getInstance().getReference().child("BOOKMARK").orderByChild("postid").equalTo(model.getIdd());
// Log.d(TAG, "This: "+dataSnapshot.getValue());
///Toast.makeText(getActivity(), String.valueOf(dataSnapshot.getValue()),Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    Crouton.makeText(Viewuploaded.this,"Troll Added As Favourite",Style.CONFIRM).show();
                                }
                            }
                            // Toast.makeText(getActivity(),"jghfg",LENGTH_LONG).show();
                            //  bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.favi));





                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Log.e(TAG, "onCancelled", databaseError.toException());
                            }

                        });


                    }
                });









                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent ash=new Intent(getApplicationContext(), Comment.class);
                        ash.putExtra("a",model.getIdd());

                        startActivity(ash);


                    }
                });

                postimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, model.getPhoto1(), null);

                    }
                });

//
//                mDoubleTapLikeView.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
//                    @Override
//                    public void onDoubleTap(View view) {
//// Toast.makeText(MainActivity.this, "Double TAPPED !", Toast.LENGTH_SHORT).show();
//                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                        DatabaseReference namesRef = rootRef.child("LIKE").push();
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("Like", 1);
//                        map.put("photo", String.valueOf(personPhoto));
//                        map.put("messageUser", personName);
//                        map.put("email", personEmail);
//                        map.put("id", personId);
//                        String mGroupId = rootRef.push().getKey();
//
//                        map.put("idd", mGroupId);
//                        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
//                        map.put("stamp", timeStamp);
//                        map.put("postid", model.getIdd());
//                        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
//                        map.put("messageTime", currentTime);
//                        namesRef.updateChildren(map);
//                        rootRef.child("LIKE");
//
//
//                        rootRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                reference1 = FirebaseDatabase.getInstance().getReference().child("LIKE").orderByChild("postid").equalTo(model.getIdd());
//// Log.d(TAG, "This: "+dataSnapshot.getValue());
/////Toast.makeText(getActivity(), String.valueOf(dataSnapshot.getValue()),Toast.LENGTH_LONG).show();
//                                textView.setText("Likeserd");
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//
//                    }
//                    @Override
//                    public void onTap() {
//// This method will be called if user didn't tap again after PRESS_TIME_TERM (default is 200)
//// So keep PRESS_TIME_GAP short ( 200~400 )^.
//// Due to Thread for single Tap, if you want to change UI through "onTap()", you should use Activity.runOnUiThread()
//
//                    }
//                });




//             share.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });






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






