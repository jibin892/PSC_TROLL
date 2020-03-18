package techsayas.in.psctrolls.psctroll.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import techsayas.in.psctrolls.psctroll.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainsFragment extends Fragment {





    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);


        return root;
    }
}