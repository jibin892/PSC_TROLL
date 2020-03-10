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

import cn.pedant.SweetAlert.SweetAlertDialog;
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
        write = findViewById(R.id.text);
        galary = findViewById(R.id.selectimg);
        imgview = findViewById(R.id.img);
        caption = findViewById(R.id.text1);
        video = findViewById(R.id.vdio);
        post = findViewById(R.id.post);

        caption.setVisibility(View.INVISIBLE);
     //   imgview.setVisibility(View.INVISIBLE);

//


        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
            }
        });
        //Drawable oldDrawable = imgview.getDrawable();

//        else{
//
//
//
//        }

      //  caption.setVisibility(View.GONE);
       // caption.setVisibility(ImageView.GONE);

//        Animation center_reveal_anim = AnimationUtils.loadAnimation(getActivity(),R.anim.center_reveal_anim);
//        facebbok.startAnimation(center_reveal_anim);
//        google.startAnimation(center_reveal_anim);
//        instagram.startAnimation(center_reveal_anim);

        galary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent inten = new Intent();
//                inten.setType("image/*");
//                inten.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(inten, "Select Picture"), 1);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//                if (imgview.getDrawable() == null) {
//                    write.setVisibility(View.VISIBLE);
//                    caption.setVisibility(View.GONE);
//                } else {
//                    caption.setVisibility(View.VISIBLE);
//
//                }

            }


//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,IMAGE_PICK_CODE);

        });



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

lc();



            }
        });


//        if (imgview.getDrawable()==null){
//            caption.setVisibility(View.GONE);
//Toast.makeText(getApplicationContext(),"dsgfg",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Toast.makeText(getApplicationContext(),"dsgfg",Toast.LENGTH_LONG).show();
//
//            caption.setVisibility(View.VISIBLE);
//        } VideoView videov;


//         Configure sign-in to request the user's ID, email address, and basic
//         profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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

//        sign_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });
//
    }



    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Upload.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Successfully signed out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));

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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void lc() {
//        progress = ProgressDialog.show(Upload.this, "Loading...",
//                "Plz Wait", true);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference namesRef = rootRef.child("POST").push();
        Map<String, Object> map = new HashMap<>();
        map.put("post", write.getText().toString());
        map.put("name", String.valueOf(personPhoto));
        map.put("email", personName);
        map.put("photo", personEmail);
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        map.put("time", currentTime);
        namesRef.updateChildren(map);
        rootRef.child("POST").child(personId);


    }
}


