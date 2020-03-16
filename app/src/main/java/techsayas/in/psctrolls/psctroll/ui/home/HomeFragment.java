package techsayas.in.psctrolls.psctroll.ui.home;


import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.L;
import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.facebook.share.internal.LikeButton;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.ChatMessage;
import techsayas.in.psctrolls.psctroll.Comment;
import techsayas.in.psctrolls.psctroll.Homeview;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Upload;
import techsayas.in.psctrolls.psctroll.Userchatimage;
import techsayas.in.psctrolls.psctroll.Viewotherprofile;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment {
    private ClipboardManager myClipboard;
    private ClipData myClip;
    FirebaseListAdapter<Homeview> adapter;
    FloatingActionButton fab,cam;
    SweetAlertDialog pDialog;
     int positio=0;

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
    ImageView  heart,postimg;
    private Uri filePath;
    FirebaseStorage storage;
    TextView messageTime;
    StorageReference storageReference;
    // request code
//    ImageView user;
//    EditText somthing;
    ImageView  bookmark;
    TextView comme;
    TextView textView;
    //ImageButton bookmark;
    DoubleTapLikeView mDoubleTapLikeView;
    Query reference;
    Query reference1;

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
        root = inflater.inflate(R.layout.fragment_home, container, false);
        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);
        EnableRuntimePermission();
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

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
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


        adapter = new FirebaseListAdapter<Homeview>(getActivity(), Homeview.class,
                R.layout.hompageview,

                reference =   FirebaseDatabase.getInstance().getReference().child("POST").orderByChild("stamp").limitToLast(15)) {
            @Override
            protected void populateView(View v, final Homeview model, final int position) {





                // Get references to the views of message.xml
                  postimg= v.findViewById(R.id.post1);
                   heart=v.findViewById(R.id.image_heart);
                 TextView messageText = (TextView)v.findViewById(R.id.userdis);
                TextView messageUser = (TextView)v.findViewById(R.id.username);
                TextView  messageTime = (TextView)v.findViewById(R.id.uploadtime);
                  mDoubleTapLikeView = v.findViewById(R.id.layout_double_tap_like);
                ImageView comment = (ImageView) v.findViewById(R.id.comment);
                ImageView ivEllipses=v.findViewById(R.id.ivEllipses);
                ImageView image_message_profile=v.findViewById(R.id.userimg1);
                //mDoubleTapLikeView = v.findViewById(R.id.layout_double_tap_like);
                textView=v.findViewById(R.id.yu);
                bookmark=v.findViewById(R.id.bookmark);
                comme = (TextView)v.findViewById(R.id.image_comments_link);



                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).into(postimg);

                //
                String s=String.valueOf(model.getStamp());
                s=s.substring(1);
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();//get your local time zone.
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                sdf.setTimeZone(tz);//set time zone.
                String localTime =sdf.format(new Date(Long.parseLong(String.valueOf(s))* 1000));
                Date date = new Date();
                try {
                    date = sdf.parse(String.valueOf(localTime));//get local date
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
                final Query applesQueryy = ref.child("COMMENT").orderByChild("postid").equalTo(model.getIdd());



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


                image_message_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        Intent aw= new Intent(getActivity(), Viewotherprofile.class);
                        aw.putExtra("id",model.getId());
                        aw.putExtra("photo",model.getPhoto());
                        aw.putExtra("email",model.getEmail());
                        aw.putExtra("name",model.getMessageUser());

                        startActivity(aw);





                    }
                });

//        user=root.findViewById(R.id.photos123);
//        somthing=root.findViewById(R.id.write123);

               heart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LIKE");
                        Query applesQueryw = ref.orderByChild("likeid").equalTo(model.getIdd()+personId);
                        applesQueryw.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Crouton.makeText(getActivity(),"Troll Already Added As Favourite",Style.INFO).show();
                                    heart.setImageResource(R.drawable.vector_heart_white);
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query applesQuery = ref.child("LIKE").orderByChild("likeid").equalTo(model.getIdd()+personId);
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

                                } else {
                                   // heart.setImageResource(R.drawable.heart);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("LIKE").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Like", 1);
                                    map.put("likeid",model.getIdd()+personId);
                                    map.put("photo",model.getPhoto());
                                    map.put("messageUser", model.getMessageUser());
                                    map.put("email", model.getEmail());
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
                                    rootRef.child("LIKE");

                                    // bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            reference1 = FirebaseDatabase.getInstance().getReference().child("BOOKMARK").orderByChild("postid").equalTo(model.getIdd());
// Log.d(TAG, "This: "+dataSnapshot.getValue());
///Toast.makeText(getActivity(), String.valueOf(dataSnapshot.getValue()),Toast.LENGTH_LONG).show();

                                            textView.setText("1");



                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    Crouton.makeText(getActivity(),"Troll Added As Favourite",Style.CONFIRM).show();
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


                mDoubleTapLikeView.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
                    @Override
                    public void onDoubleTap(View view) {


                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("LIKE");
                        Query applesQueryw = ref.orderByChild("likeid").equalTo(model.getIdd()+personId);

                        applesQueryw.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    Crouton.makeText(getActivity(),"Troll Already Added As Favourite",Style.INFO).show();
                                    heart.setImageResource(R.drawable.vector_heart_white);

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query applesQuery = ref.child("LIKE").orderByChild("likeid").equalTo(model.getIdd()+personId);
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

                                } else {
                                   // heart.setImageResource(R.drawable.heart);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("LIKE").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Like", 1);
                                    map.put("likeid",model.getIdd()+personId);
                                    map.put("photo",model.getPhoto());
                                    map.put("messageUser", model.getMessageUser());
                                    map.put("email", model.getEmail());
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
                                    rootRef.child("LIKE");

                                    // bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            reference1 = FirebaseDatabase.getInstance().getReference().child("BOOKMARK").orderByChild("postid").equalTo(model.getIdd());
// Log.d(TAG, "This: "+dataSnapshot.getValue());
///Toast.makeText(getActivity(), String.valueOf(dataSnapshot.getValue()),Toast.LENGTH_LONG).show();

                                            textView.setText("1");



                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    Crouton.makeText(getActivity(),"Troll Added As Favourite",Style.CONFIRM).show();
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

                    @Override
                    public void onTap() {

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
                                  //  bookmark.setImageResource(R.drawable.bookok);
                                    Crouton.makeText(getActivity(),"Troll Already Added As Favourite",Style.INFO).show();
                                } else {
                                  //  bookmark.setImageResource(R.drawable.bookmarked);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("BOOKMARK").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Like", 1);
                                    map.put("photo",model.getPhoto());
                                    map.put("messageUser", model.getMessageUser());
                                    map.put("email", model.getEmail());
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


                                    Crouton.makeText(getActivity(),"Troll Added As Favourite",Style.CONFIRM).show();
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




                ivEllipses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {





                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
// add a li
                        String[] animals = {"Share", "Download"};
                        builder.setItems(animals, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: {

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



                                    }break;


                                }
                            }
                        });
// create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();















                    }
                });


               /* if(readState())
                {
                    bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fav));
                }
                else
                {
                    bookmark.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.favi));
                }*/





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
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference();
                reff.child("LIKE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int total = 0,
                                count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.child("Like").getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
Toast.makeText(getActivity(),total,LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException(); // don't ignore errors
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
                        if(dataSnapshot.exists()){


                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                        }

                        else{

                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                        }





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
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();

    }

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


    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.CAMERA)) {

// Toast.makeText(Cpature_image.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);


        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case 12:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

// Toast.makeText(Cpature_image.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }}




}








