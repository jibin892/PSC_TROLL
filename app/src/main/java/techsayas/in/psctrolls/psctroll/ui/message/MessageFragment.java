package techsayas.in.psctrolls.psctroll.ui.message;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.style.AlignmentSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import techsayas.in.psctrolls.psctroll.ChatMessage;
import techsayas.in.psctrolls.psctroll.Homepage;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Userchatimage;
import techsayas.in.psctrolls.psctroll.Viewprofile;
import techsayas.in.psctrolls.psctroll.ui.profile.ProfileViewModel;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

public class MessageFragment extends Fragment {
    FirebaseListAdapter<ChatMessage> adapter;
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
    DatabaseReference reference;
    DatabaseReference a;
    TextView messageUser;
    private final int PICK_IMAGE_REQUEST = 71;
    private MessageViewModel messageViewModel;
    View root;
public  static boolean isInFront;
    private ShimmerFrameLayout mShimmerViewContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        messageViewModel =
                ViewModelProviders.of(this).get(MessageViewModel.class);
        root = inflater.inflate(R.layout.fragment_message, container, false);


        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        cam = (FloatingActionButton) root.findViewById(R.id.cam);
        fab.setVisibility(View.INVISIBLE);
        // fab.setEnabled(false);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent a = new Intent(getActivity(), Userchatimage.class);
                startActivity(a);
            }
        });
        listOfMessages = (ListView) root.findViewById(R.id.list_of_messages);
        input = (EditText) root.findViewById(R.id.input);
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
                    Toast.makeText(getActivity(), "Empty value is not allowed",
                            LENGTH_LONG)
                            .show();

                }

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                else {

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference namesRef = rootRef.child("MSG").push();
                    Map<String, Object> map = new HashMap<>();
                    map.put("messageText", input.getText().toString());
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
                    rootRef.child("MSG");
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
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

        }

        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container);
        return root;
    }


    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.item_in_message,

                reference = FirebaseDatabase.getInstance().getReference().child("MSG")) {
            @Override
            protected void populateView(View v, final ChatMessage model, int position) {
                // Get references to the views of message.xml
                ImageView postimg = v.findViewById(R.id.postimg123);
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);
                ImageView image_message_profile = v.findViewById(R.id.image_message_profile);
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                Picasso.get().load(model.getPhoto()).into(image_message_profile);
                Picasso.get().load(model.getPhoto1()).resize(700, 700).centerCrop().into(postimg);
                messageTime.setText(model.getMessageTime());


                messageText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(getActivity());

                        builder.setMessage("Do you want to delet ?");

                        builder.setTitle("Alert !");

                        builder.setCancelable(false);


                        builder
                                .setPositiveButton(
                                        "Yes",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {







                                                delet();
                                            }
                                        });

                        builder
                                .setNegativeButton(
                                        "No",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {


                                                dialog.cancel();
                                            }
                                        });

// Create the Alert dialog
                        AlertDialog alertDialog = builder.create();

// Show the Alert Dialog box
                        alertDialog.show();


                        return false;
                    }

                    private void delet() {


                        if (model.getId() == personId) {


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("MSG").orderByChild("id").equalTo(personId);

                            if (applesQuery != null) {

                                Query applesQuery1 = ref.child("MSG").orderByChild("stamp").equalTo(model.getStamp());
                                if (applesQuery1 == null) {

                                    Toast.makeText(getActivity(), "gkfjgud", LENGTH_LONG).show();

                                } else {


                                    applesQuery1.addListenerForSingleValueEvent(new ValueEventListener() {
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
                                }


                            }


                        } else {


                            Toast.makeText(getActivity(), "Not delet", LENGTH_LONG).show();

                        }


                    }
                });

                image_message_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent as = new Intent(getActivity(), Viewprofile.class);
                        as.putExtra("a", model.getMessageUser());
                        as.putExtra("b", model.getPhoto());
                        //as.putExtra("c", model.getMessageUser());
                        startActivity(as);
                    }
                });


                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
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
                        new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, v, model.getPhoto1(), null);

                    }
                });

            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                ChatMessage chatMessage = getItem(position);
                if (chatMessage.getId().equals(personId)) {
                    view = getActivity().getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);

                } else {
                    view = getActivity().getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
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


    }



