package techsayas.in.psctrolls.psctroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.ui.notifications.NotificationsFragment;
import techsayas.in.psctrolls.psctroll.ui.notifications.NotificationsViewModel;

import static java.sql.Types.NULL;

public class Upload extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    TextView sign_out;
    TextView nameTV;
    TextView emailTV;
    TextView idTV,galary,video,post;
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
    private String selectedPath;
    MediaController mediac;
    private NotificationsViewModel notificationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
      //  sign_out = root.findViewById(R.id.log_out);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nameTV = findViewById(R.id.name1);
        emailTV = findViewById(R.id.email1);
//        idTV = root.findViewById(R.id.id);
        photo = findViewById(R.id.photos2);
        write = findViewById(R.id.text1);
        galary = findViewById(R.id.selectimg);
        imgview = findViewById(R.id.img);
        caption = findViewById(R.id.text1);
        video = findViewById(R.id.vdio);
        post = findViewById(R.id.post);


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



        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(Userchatimage.this, String.valueOf(bitmap), Toast.LENGTH_SHORT).show();
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();


                if(filePath != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(Upload.this);
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
                                    progress = ProgressDialog.show(Upload.this, "Loading...",
                                            "Plz Wait",true);
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference namesRef = rootRef.child("POSTADMIN").push();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("messageText", write.getText().toString());
                                    map.put("photo", String.valueOf(personPhoto));
                                    map.put("messageUser", personName);
                                    map.put("email", personEmail);
                                    map.put("id", personId);
                                    map.put("photo1", image);
                                    String mGroupId = rootRef.push().getKey();
                                    map.put("idd", mGroupId);
                                    map.put("specialid",mGroupId+personId);
                                    int timeStamp = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                                    map.put("stamp",Integer.parseInt(String.valueOf(-1*timeStamp)));
                                    String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                                    map.put("messageTime", currentTime);
                                    namesRef.updateChildren(map);
                                    rootRef.child("POSTADMIN");
                                    rootRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            progress.dismiss();
                                            progressDialog.dismiss();
                                            imgview.setImageDrawable(null);
                                            Crouton.makeText(Upload.this,"Troll Uploaded For Admin Approval", Style.CONFIRM).show();
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
                                    Toast.makeText(Upload.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgview.setImageBitmap(bitmap);
                WatermarkImage watermarkImage = new WatermarkImage(bitmap)
                        .setImageAlpha(80)
                        .setPositionX(Math.random())
                        .setPositionY(Math.random())
                        .setRotation(15)
                        .setSize(0.1);

                WatermarkBuilder
                        .create(this, imgview)
                        .loadWatermarkImage(watermarkImage)
                        .setTileMode(true)
                        .getWatermark()
                        .setToImageView(imgview);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
