package techsayas.in.psctrolls.psctroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.widget.Toast.LENGTH_LONG;

public class Viewprofile extends AppCompatActivity {
ImageView photo,facebook,instagaram;
TextView  name ,email;
Intent as;
    ScaleAnimation shrinkAnim;
    ImageView cmimg;
    TextView replay,user,time;
    Intent a;
    FirebaseListAdapter<Personelmsg> adapter;
    FloatingActionButton fab,fab5, cam;
    SweetAlertDialog pDialog;
    // ShimmerLayout shimmerText;
    ListView listOfMessages;
    CircleImageView img;
    View view;
    ImageView image_message_profile,imagview;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignInClient;
    EditText input;
    String personName;
    Bitmap bitmap;
    String personId;
    String personEmail;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    // request code
    TextView messageTime;
    Query reference;
    ProgressDialog progress;

    TextView messageUser;
    private final int PICK_IMAGE_REQUEST = 71;
    private MessageViewModel messageViewModel;
    private TextView tvNoMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);


       as=getIntent();

        photo=findViewById(R.id.photos11);
        name=findViewById(R.id.name11);
        email=findViewById(R.id.email11);
        name.setText(as.getStringExtra("a"));
        email.setText(as.getStringExtra("email"));

        Picasso.get().load(as.getStringExtra("b")).into(photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, v, as.getStringExtra("b"), null);

            }
        });




        imagview=findViewById(R.id.imgcomment7);


        fab = (FloatingActionButton)findViewById(R.id.fab7);
        fab5 = (FloatingActionButton)findViewById(R.id.fab8);

        cam = (FloatingActionButton)findViewById(R.id.cam3);
        fab.setVisibility(View.INVISIBLE);
        // fab.setEnabled(false);


        listOfMessages = (ListView)  findViewById(R.id.list_of_messages7);
        input = (EditText)  findViewById(R.id.input7);
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    fab.setVisibility(View.INVISIBLE);
                    fab5.setVisibility(View.VISIBLE);

                } else {
                    fab.setVisibility(View.VISIBLE);
                    fab5.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }


            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();


                if(filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(Viewprofile.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String image = taskSnapshot.getDownloadUrl().toString();
                                    //  Toast.makeText(Userchatimage.this, image, Toast.LENGTH_SHORT).show();
                                    progress = ProgressDialog.show(Viewprofile.this, "Loading...",
                                            "Plz Wait", true);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("PERSONELMSG").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("photo1", image);
                                    map.put("photo", String.valueOf(personPhoto));
                                    map.put("messageUser", personName);
                                    map.put("email", personEmail);
                                    map.put("id", personId);
                                    String mGroupId = rootRef.push().getKey();
                                    map.put("idd", mGroupId);
                                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                                    map.put("stamp", timeStamp);
                                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                                    map.put("messageTime", currentTime);
                                    namesRef.updateChildren(map);
                                    rootRef.child("PERSONELMSG");
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progress.dismiss();
                                            progressDialog.dismiss();
                                            imagview.setImageDrawable(null);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(Viewprofile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });

                }


            }
        });

        //   img=root.findViewById(R.id.img);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Empty value is not allowed",
                            LENGTH_LONG)
                            .show();

                }

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                else {

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference namesRef = rootRef.child("PERSONELMSG").push();
                    Map<String, Object> map = new HashMap<>();
                    map.put("messageText", input.getText().toString());
                    map.put("photo", String.valueOf(personPhoto));
                    map.put("messageUser", personName);
                    map.put("email", personEmail);
                    map.put("person", as.getStringExtra("id"));
                    map.put("id", personId);
                    String mGroupId = rootRef.push().getKey();
                    map.put("idd", mGroupId);
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    map.put("stamp", timeStamp);
                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    map.put("messageTime", currentTime);
                    namesRef.updateChildren(map);
                    rootRef.child("PERSONELMSG");
                    input.getText().clear();
                    rootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // Log.d(TAG, "This: "+dataSnapshot.getValue());
                            //Toast.makeText(getActivity(),"gfdg",Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//
                }
            }


        });
        displayChatMessages();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

        }



    }


    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<Personelmsg>(Viewprofile.this, Personelmsg.class,
                R.layout.personalmsg_in,

                reference=  FirebaseDatabase.getInstance().getReference("PERSONELMSG").orderByChild("person").equalTo(as.getStringExtra("id"))) {
            @Override
            protected void populateView(View v, final Personelmsg model, int position) {
                final ImageView postimg = v.findViewById(R.id.postimg12333);
                final TextView messageText = (TextView) v.findViewById(R.id.message_text33);
                messageUser = (TextView) v.findViewById(R.id.message_user33);
                messageTime = (TextView) v.findViewById(R.id.message_time33);
                final ImageView image_message_profile = v.findViewById(R.id.image_message_profile33);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).resize(700, 700).centerCrop().into(postimg);


                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).resize(700, 700).centerCrop().into(postimg);
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

                String timeAgo = null;

                if (timeDIM == 0) {
                    timeAgo = "just now";
                } else if (timeDIM == 1) {
                    //  return  "1 minute";
                    timeAgo="1 minute ago";
                } else if (timeDIM >= 2 && timeDIM <= 44) {
                    timeAgo = (Math.round(timeDIM)) + " minutes ago";
                } else if (timeDIM >= 45 && timeDIM <= 89) {
                    timeAgo = " hour ago";
                } else if (timeDIM >= 90 && timeDIM <= 1439) {
                    timeAgo =  (Math.round(timeDIM / 60)) + " hours ago";
                } else if (timeDIM >= 1440 && timeDIM <= 2519) {
                    timeAgo = "1 day ago";
                } else if (timeDIM >= 2520 && timeDIM <= 43199) {
                    timeAgo = (Math.round(timeDIM / 1440)) + " days";
                } else if (timeDIM >= 43200 && timeDIM <= 86399) {
                    timeAgo = "about a month ago";
                } else if (timeDIM >= 86400 && timeDIM <= 525599) {
                    timeAgo = (Math.round(timeDIM / 43200)) + " months";
                } else if (timeDIM >= 525600 && timeDIM <= 655199) {
                    timeAgo = "about a year ago";
                } else if (timeDIM >= 655200 && timeDIM <= 914399) {
                    timeAgo = "over a year ago";
                } else if (timeDIM >= 914400 && timeDIM <= 1051199) {
                    timeAgo = "almost 2 years ago";
                } else {
                    timeAgo = "about " + (Math.round(timeDIM / 525600)) + " years";
                }

                // return timeAgo + " ago";

                messageTime.setText(timeAgo);






                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        //   Toast.makeText(getActivity(),"jghfg",LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //  }

                postimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new PhotoFullPopupWindow(getApplicationContext(), R.layout.popup_photo_full, v, model.getPhoto1(), null);

                    }
                });

            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                Personelmsg chatMessage = getItem(position);
                if (chatMessage.getId().equals(personId)) {
                    view = Viewprofile.this.getLayoutInflater().inflate(R.layout.personalmsg_in, viewGroup, false);

                } else {
                    view = Viewprofile.this.getLayoutInflater().inflate(R.layout.personalmsg_out, viewGroup, false);
                }
//generating view}
                populateView(view, chatMessage, position);

                return view;
            }

            @Override
            public int getViewTypeCount() {
// return the total number of view types. this value should never change
// at runtime
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
// return a value between 0 and (getViewTypeCount - 1)
                return position % 2;
            }

        };


        listOfMessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagview.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }}

}



