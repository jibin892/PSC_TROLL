package techsayas.in.psctrolls.psctroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.widget.Toast.LENGTH_LONG;

public class Commentreplay extends AppCompatActivity {
ImageView cmimg;
TextView replay,user,time;
Intent a;
    FirebaseListAdapter<Commentreplayclass> adapter;
    FloatingActionButton fab, cam;
    SweetAlertDialog pDialog;
    // ShimmerLayout shimmerText;
    ListView listOfMessages;
    CircleImageView img;
    View view;
    ImageView image_message_profile;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignInClient;
    EditText input;
    String personName;
    String personId;
    String personEmail;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    // request code
    TextView messageTime;
    Query reference;

    TextView messageUser;
    private final int PICK_IMAGE_REQUEST = 71;
    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentreplay);
        a=getIntent();
        cmimg=findViewById(R.id.cimg);
        user=findViewById(R.id.cuser);
        time=findViewById(R.id.ctime);

        replay=findViewById(R.id.ctxt);
        replay.setText( a.getStringExtra("d"));
        user.setText( a.getStringExtra("a"));
        time.setText( a.getStringExtra("c"));
        Picasso.get().load(a.getStringExtra("b")).into(cmimg);


        fab = (FloatingActionButton)findViewById(R.id.fab3);
        cam = (FloatingActionButton)findViewById(R.id.cam3);
       fab.setVisibility(View.INVISIBLE);
        // fab.setEnabled(false);


        listOfMessages = (ListView)  findViewById(R.id.list_of_messages3);
        input = (EditText)  findViewById(R.id.input3);
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    fab.setVisibility(View.INVISIBLE);
                } else {
                    fab.setVisibility(View.VISIBLE);
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
                    DatabaseReference namesRef = rootRef.child("COMMENTREPLAY").push();
                    Map<String, Object> map = new HashMap<>();
                    map.put("messageText", input.getText().toString());
                    map.put("photo", String.valueOf(personPhoto));
                    map.put("messageUser", personName);
                    map.put("email", personEmail);
                    map.put("id", personId);
                    map.put("cament", a.getStringExtra("a"));
                    String mGroupId = rootRef.push().getKey();
                    map.put("idd", mGroupId);
                    map.put("comentid", a.getStringExtra("re"));
                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    map.put("stamp", timeStamp);
                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                    map.put("messageTime", currentTime);
                    namesRef.updateChildren(map);
                    rootRef.child("COMMENTREPLAY");
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

        adapter = new FirebaseListAdapter<Commentreplayclass>(Commentreplay.this, Commentreplayclass.class,
                R.layout.commentreplys,

                reference=  FirebaseDatabase.getInstance().getReference("COMMENTREPLAY").orderByChild("comentid").equalTo(a.getStringExtra("re"))) {
            @Override
            protected void populateView(View v, final Commentreplayclass model, int position) {
                // Get references to the views of message.xml
                ImageView postimg = v.findViewById(R.id.postimg123t1);
                TextView messageText = (TextView) v.findViewById(R.id.message_textt1);
                messageUser = (TextView) v.findViewById(R.id.message_usert1);
                TextView replay = (TextView) v.findViewById(R.id.message_reply1);

                messageTime = (TextView) v.findViewById(R.id.message_timet1);
                ImageView image_message_profile = v.findViewById(R.id.image_message_profilet1);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).resize(700, 700).centerCrop().into(postimg);
                messageTime.setText(model.getMessageTime());

                replay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent a = new Intent(getApplicationContext(), Commentreplay.class);
                        a.putExtra("a", model.getMessageUser());
                        a.putExtra("b", model.getPhoto());
                        a.putExtra("c", model.getMessageTime());
                        a.putExtra("d", model.getMessageText());
                        startActivity(a);
                    }
                });


                image_message_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent as = new Intent(getApplicationContext(), Viewprofile.class);
                        as.putExtra("a", model.getMessageUser());
                        as.putExtra("b", model.getPhoto());
                        //as.putExtra("c", model.getMessageUser());
                        startActivity(as);
                    }
                });


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
                Commentreplayclass chatMessage = getItem(position);
                if (chatMessage.getId().equals(personId)) {
                    view = Commentreplay.this.getLayoutInflater().inflate(R.layout.commentreplys, viewGroup, false);

                } else {
                    view = Commentreplay.this.getLayoutInflater().inflate(R.layout.commentreplys1, viewGroup, false);
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



}



