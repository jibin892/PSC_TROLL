package techsayas.in.psctrolls.psctroll;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Messageimagesent extends AppCompatActivity implements View.OnClickListener {
    GoogleSignInClient mGoogleSignInClient;
    TextView sign_out;
    TextView nameTV;
    TextView emailTV;
    TextView idTV,video,post;
    ImageView galary;
    ImageView photo,imgview;
    ImageView facebbok,google,instagram;
    EditText write,caption;
    private Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    String personEmail;
    private final int PICK_IMAGE_REQUEST = 71;
    boolean img_visible = true;
    VideoView videoview;
    private static final int SELECT_VIDEO = 3;
    ProgressDialog progress;
    Uri personPhoto;
    String personName;
    String personId;
    String ser;
    private BottomSheetDialog bottomSheetDialog;

    private String selectedPath;
    MediaController mediac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messageimagesent);



        bottomSheetDialog = new BottomSheetDialog(Messageimagesent.this);
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.chosepic, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        View camera  = bottomSheetDialogView.findViewById(R.id.camarass1);
        View galarys = bottomSheetDialogView.findViewById(R.id.gal);
        camera.setOnClickListener(this);
        galarys.setOnClickListener(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nameTV =   findViewById(R.id.name11);
        emailTV =  findViewById(R.id.email11);
//        idTV = root.findViewById(R.id.id);
        photo =  findViewById(R.id.photos11);
        // write =root. findViewById(R.id.text1);
        galary =  findViewById(R.id.selectimg11);
        galary.setOnClickListener(this);

        imgview =  findViewById(R.id.img11);
        caption = findViewById(R.id.text11);

        post = findViewById(R.id.post11);


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

            nameTV.setText(personName);
            emailTV.setText(personEmail);
//            idTV.setText("ID: "+personGivenName);
            // Picasso.get().load(personPhoto).into(photo1);
            Picasso.get().load(personPhoto).into(photo);


        }




                post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();


                if (filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(Messageimagesent.this);
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
                                    progress = ProgressDialog.show(Messageimagesent.this, "Loading...",
                                            "Plz Wait", true);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("MSG").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("messageText", caption.getText().toString());
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
                                    rootRef.child("MSG");
                                    caption.getText().clear();
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progress.dismiss();
                                            progressDialog.dismiss();
                                            imgview.setImageDrawable(null);
                                            Crouton.makeText(Messageimagesent.this, "Troll Uploaded For Admin Approval", Style.CONFIRM).show();
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
                                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                }}
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgview.getLayoutParams().height = 900;
                imgview.getLayoutParams().width = 1200;
                imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.selectimg11:
                bottomSheetDialog.show();
                break;

            case R.id.camarass1:

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);


                break;

            case R.id.gal:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);



                break;


        }


    }
}









