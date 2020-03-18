package techsayas.in.psctrolls.psctroll.ui.profile;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.L;
import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseError;
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
import com.shrikanthravi.library.NightModeButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import techsayas.in.psctrolls.psctroll.Comment;
import techsayas.in.psctrolls.psctroll.Messageimagesent;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.Profileview;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Upload;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;
import techsayas.in.psctrolls.psctroll.ui.notifications.NotificationsViewModel;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment implements View.OnClickListener {
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
    RelativeLayout relativeLayout;
    String personId;
    String ser;
    private BottomSheetDialog bottomSheetDialog;
    NightModeButton nightModeButton;
    private String selectedPath;
    MediaController mediac;
    private NotificationsViewModel notificationsViewModel;
    private ProfileViewModel profileViewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
 root = inflater.inflate(R.layout.fragment_profile, container, false);

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.chosepicpost, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        View camera  = bottomSheetDialogView.findViewById(R.id.chosecamarass1);
        View galarys = bottomSheetDialogView.findViewById(R.id.chosegal);
        camera.setOnClickListener(this);
        galarys.setOnClickListener(this);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        nameTV =root.findViewById(R.id.name1);
        emailTV = root.findViewById(R.id.email1);
//        idTV = root.findViewById(R.id.id);
        photo = root.findViewById(R.id.photos2);
       // write =root. findViewById(R.id.text1);
        galary =root. findViewById(R.id.selectimg);
        galary.setOnClickListener(this);

        imgview =root. findViewById(R.id.img);
        caption =root. findViewById(R.id.text1);
        video = root.findViewById(R.id.vdio);
        post = root.findViewById(R.id.post);


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

            nameTV.setText(personName);
            emailTV.setText(personEmail);
//            idTV.setText("ID: "+personGivenName);
            // Picasso.get().load(personPhoto).into(photo1);
            Picasso.get().load(personPhoto).into(photo);


        }



//        galary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);
//
//            }
//        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caption.getText().toString().isEmpty()){

                    Crouton.makeText(getActivity(),"Filed is Empty", Style.ALERT).show();

                }
else {
                    //Toast.makeText(Userchatimage.this, String.valueOf(bitmap), Toast.LENGTH_SHORT).show();
                    storage = FirebaseStorage.getInstance();
                    storageReference = storage.getReference();


                    if (filePath != null) {
                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                                        progress = ProgressDialog.show(getActivity(), "Loading...",
                                                "Plz Wait", true);
                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference namesRef = rootRef.child("POSTADMIN").push();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("messageText", caption.getText().toString());
                                        map.put("photo", String.valueOf(personPhoto));
                                        map.put("messageUser", personName);
                                        map.put("email", personEmail);
                                        map.put("id", personId);
                                        map.put("photo1", image);
                                        String mGroupId = rootRef.push().getKey();
                                        map.put("idd", mGroupId);
                                        map.put("specialid", mGroupId + personId);
                                        int timeStamp = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                                        map.put("stamp", Integer.parseInt(String.valueOf(-1 * timeStamp)));
                                        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                                        map.put("messageTime", currentTime);
                                        namesRef.updateChildren(map);
                                        rootRef.child("POSTADMIN");
                                        caption.getText().clear();
                                        rootRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                progress.dismiss();
                                                progressDialog.dismiss();
                                                imgview.setImageDrawable(null);
                                                Crouton.makeText(getActivity(), "Troll Uploaded For Admin Approval", Style.CONFIRM).show();
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
                                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    else {
                        Crouton.makeText(getActivity(), "Select An Image ", Style.ALERT).show();
                    }
                }

            }
        });
        return root;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                  /* filePath = imageReturnedIntent.getData();
                   File finalFile = new File(getRealPathFromURI(filePath));*/


                    bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    filePath=  getImageUri(getActivity(), bitmap);
                 //   Toast.makeText(Messageimagesent.this,String.valueOf(filePath), Toast.LENGTH_LONG).show();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    imgview.getLayoutParams().height = 900;
                    imgview.getLayoutParams().width = 1200;
                    imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgview.setImageBitmap(bitmap);

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    filePath = imageReturnedIntent.getData();
                    //Toast.makeText(getActivity(),String.valueOf(filePath), Toast.LENGTH_LONG).show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
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
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.selectimg:
                bottomSheetDialog.show();
                break;

            case R.id.chosecamarass1:
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 0);
                }


                break;

            case R.id.chosegal:


                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);


                break;

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}









