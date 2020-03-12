package techsayas.in.psctrolls.psctroll.request;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import techsayas.in.psctrolls.psctroll.R;

public class Trollrequest extends Fragment {

    private TrollrequestViewModel mViewModel;

View request;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        request= inflater.inflate(R.layout.trollrequest_fragment, container, false);
        return  request;
    }


}
