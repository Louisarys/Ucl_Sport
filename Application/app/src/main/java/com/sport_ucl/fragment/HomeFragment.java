package com.sport_ucl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sport_ucl.R;

/**
 * Created by valentin on 13/03/18.
 */


public class HomeFragment  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View myFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        Button google = (Button) myFragmentView.findViewById(R.id.btnHomeOpenGoogle);
        google.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(browserIntent);
            }
        }) ;



        return myFragmentView;
    }
}
