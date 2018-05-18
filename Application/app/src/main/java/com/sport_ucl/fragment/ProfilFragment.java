package com.sport_ucl.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import com.sport_ucl.User;
import com.sport_ucl.UserSingleton;

import junit.framework.Test;

/**
 * Created by valentin on 13/03/18.
 */

public class ProfilFragment extends Fragment {

    private TextView UserLogin;
    private TextView UserPassword;
    private UserSingleton User;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_profil, container, false);

        UserLogin = myFragmentView.findViewById(R.id.tvUserLogin);
        UserPassword = myFragmentView.findViewById(R.id.tvUserPassword);

       /* Long UserId = (User.getINSTANCE().getUser().getId());
        String UserBd = User.getINSTANCE().getUser().getBirthday();
        String UserCount = User.getINSTANCE().getUser().getCountry();
        String UserEmail = User.getINSTANCE().getUser().getEmail();
        String UserFirstName = User.getINSTANCE().getUser().getFirstName();
        String UserGenre = User.getINSTANCE().getUser().getGenre();
        String UserLastName = User.getINSTANCE().getUser().getLastName();

        Log.d("VAL", "User ID: " + UserId.toString());
        Log.d("VAL", "User Bd: " + UserBd);
        Log.d("VAL", "User Count: " + UserCount);
        Log.d("VAL", "User Email: " + UserEmail);
        Log.d("VAL", "User First name: " + UserFirstName);
        Log.d("VAL", "User genre: " + UserGenre);
        Log.d("VAL", "User Last name: " + UserLastName);

        //UserLogin.setText(User.getUser().getFirstName().toString());
        //UserPassword.setText(User.getUser().getLastName().toString());


*/

        return myFragmentView;
    }
}
