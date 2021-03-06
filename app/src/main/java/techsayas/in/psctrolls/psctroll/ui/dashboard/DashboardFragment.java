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
import android.widget.Button;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import techsayas.in.psctrolls.psctroll.Bookmark;
import techsayas.in.psctrolls.psctroll.Bookmarkview;
import techsayas.in.psctrolls.psctroll.Comment;
import techsayas.in.psctrolls.psctroll.Homeview;
import techsayas.in.psctrolls.psctroll.Mypost;
import techsayas.in.psctrolls.psctroll.PhotoFullPopupWindow;
import techsayas.in.psctrolls.psctroll.Profileremove;
import techsayas.in.psctrolls.psctroll.Profileview;
import techsayas.in.psctrolls.psctroll.Psc_notification;
import techsayas.in.psctrolls.psctroll.Quiz;
import techsayas.in.psctrolls.psctroll.R;
import techsayas.in.psctrolls.psctroll.Youtube_main;
import techsayas.in.psctrolls.psctroll.ui.message.MessageViewModel;

import static android.widget.Toast.LENGTH_LONG;


public class DashboardFragment extends Fragment implements View.OnClickListener {

    private BottomSheetDialog bottomSheetDialog;
    View root;
ImageView tvNext;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.dii, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        View shareView = bottomSheetDialogView.findViewById(R.id.quiz1);
        View getLinkView = bottomSheetDialogView.findViewById(R.id.pscvideo);
        View editNameView = bottomSheetDialogView.findViewById(R.id.bokkmarkpage1);
        View deleteView = bottomSheetDialogView.findViewById(R.id.delete);
        shareView.setOnClickListener(this);
        getLinkView.setOnClickListener(this);
        editNameView.setOnClickListener(this);
        deleteView.setOnClickListener(this);
        tvNext=root.findViewById(R.id.tvNext);

        tvNext.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.tvNext:
                bottomSheetDialog.show();
                break;
            case R.id.pscvideo:
startActivity(new Intent(getActivity(), Youtube_main.class));
                break;
            case R.id.quiz1:
                startActivity(new Intent(getActivity(), Quiz.class));

                break;
            case R.id.name:

                //  Toast.makeText(getActivity(), "Edit name", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:


               // Toast.makeText(getActivity(), "Delete collection", Toast.LENGTH_SHORT).show();
                break;
            case R.id.facebook:


                break;
            case R.id.instagaram:


                break;
            case R.id.google:


                break;
        }
    }
}
























