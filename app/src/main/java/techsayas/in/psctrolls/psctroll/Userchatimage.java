package techsayas.in.psctrolls.psctroll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Userchatimage extends AppCompatActivity {
TextView nameTV,emailTV,post,galary;
    GoogleSignInClient mGoogleSignInClient;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
EditText write;
ImageView photo,imgview;
String personName,personEmail,personId;
Uri personPhoto;
    ProgressDialog progress;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchatimage);


        nameTV = findViewById(R.id.name3);
        emailTV = findViewById(R.id.email3);
//        idTV = root.findViewById(R.id.id);
        photo = findViewById(R.id.photos3);
        write = findViewById(R.id.write3);
        galary = findViewById(R.id.selectimg3);
        imgview = findViewById(R.id.img3);
        post = findViewById(R.id.post3);



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
            final ProgressDialog progressDialog = new ProgressDialog(Userchatimage.this);
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
                            progress = ProgressDialog.show(Userchatimage.this, "Loading...",
                                    "Plz Wait", true);
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference namesRef = rootRef.child("MSG").push();
                            Map<String, Object> map = new HashMap<>();
                            map.put("messageText", write.getText().toString());
                            map.put("photo", String.valueOf(personPhoto));
                            map.put("messageUser", personName);
                            map.put("email", personEmail);
                            map.put("id", personId);
                            map.put("photo1", image);
                            String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                            map.put("messageTime", currentTime);
                            namesRef.updateChildren(map);
                            rootRef.child("MSG");
                            rootRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
progress.dismiss();
                                    // Log.d(TAG, "This: "+dataSnapshot.getValue());
                                    //Toast.makeText(getActivity(),"gfdg",Toast.LENGTH_LONG).show();
//                                  Intent ab=new Intent(getApplicationContext(),Homepage.class);
//                                  startActivity(ab);
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
                            Toast.makeText(Userchatimage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
