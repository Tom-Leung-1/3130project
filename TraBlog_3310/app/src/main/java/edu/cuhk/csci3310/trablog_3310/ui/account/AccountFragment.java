package edu.cuhk.csci3310.trablog_3310.ui.account;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import edu.cuhk.csci3310.trablog_3310.R;

public class AccountFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    AnimationDrawable gradientAnimation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_setting, container, false);
        return root;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NestedScrollView myLayout = (NestedScrollView) getView().findViewById(R.id.change_account);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(10);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();

        final Button changePwBtn = getActivity().findViewById(R.id.change_pw_btn);
        changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Password has been changed!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}