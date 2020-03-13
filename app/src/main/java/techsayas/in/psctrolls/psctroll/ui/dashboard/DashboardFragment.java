package techsayas.in.psctrolls.psctroll.ui.dashboard;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import techsayas.in.psctrolls.psctroll.Bookmark;
import techsayas.in.psctrolls.psctroll.Comment;
import techsayas.in.psctrolls.psctroll.Homeview;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.Profileview;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static techsayas.in.psctrolls.psctroll.ui.home.HomeFragment.currentDate;


public class DashboardFragment extends Fragment {

    FirebaseListAdapter<Profileview> adapter;
    FloatingActionButton fab,cam;
    SweetAlertDialog pDialog;
    ListView listOfMessages;
    CircleImageView img;
    ImageView  download,share,ivEllipses;
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
    View root;



    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listOfMessages = (ListView) root.findViewById(R.id.bookmarkv);

        listOfMessages.post(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                listOfMessages.smoothScrollToPosition(0);
            }
        });

        sign_out = root.findViewById(R.id.log_out);
        nameTV = root.findViewById(R.id.name);
        emailTV = root.findViewById(R.id.email);
//        idTV = root.findViewById(R.id.id);
        photo = root.findViewById(R.id.photos);
        photo1 = root.findViewById(R.id.photos12);
        facebbok = root.findViewById(R.id.facebook);
        google = root.findViewById(R.id.google);
        instagram = root.findViewById(R.id.instagaram);
        write = root.findViewById(R.id.write);
        // listOfMessages = (ListView)root.findViewById(R.id.list_of_messages1);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();




        }









        facebbok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        adapter = new FirebaseListAdapter<Profileview>(getActivity(), Profileview.class,
                R.layout.profileview,

                reference=  FirebaseDatabase.getInstance().getReference("POST")) {
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
                //
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();//get your local time zone.
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                sdf.setTimeZone(tz);//set time zone.
                String localTime = sdf.format(new Date(Long.parseLong(model.getStamp() )* 1000));
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
                    timeAgo = timeDIM + " minutes ago";
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


    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    }













