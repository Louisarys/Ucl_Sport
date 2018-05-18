package com.sport_ucl.fragment;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.sport_ucl.DisciplineToken;
import com.sport_ucl.EvenementListAdapter;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import com.sport_ucl.dialog.DialogFilterPlanning;
import com.sport_ucl.dialog.DialogSportPlanning;
import com.sport_ucl.fragment.HomeFragment;
import com.sport_ucl.fragment.NotificationFragment;
import com.sport_ucl.fragment.PlanningFragment;
import com.sport_ucl.fragment.ProfilFragment;
import com.sport_ucl.fragment.WolvesFragment;
import com.sport_ucl.module.Planning;

import java.util.ArrayList;
import java.util.HashSet;

public class menu extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{


    public ListView mListViewFrag;
    public ArrayList<DisciplineToken> eventListFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    private boolean loadFragment(android.support.v4.app.Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        android.support.v4.app.Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_planning:
                fragment = new PlanningFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new NotificationFragment();
                break;
            case  R.id.navigation_profil:
                fragment = new ProfilFragment();
                break;
            case  R.id.navigation_wolves:
                fragment = new WolvesFragment();
                break;
        }
        return loadFragment(fragment);
    }


    public void test() {
        mListViewFrag = (ListView) findViewById(R.id.listViewPlanning);
        eventListFrag = PlanningSingleton.getINSTANCE().getTokenList(menu.this);
        final EvenementListAdapter adapter = new EvenementListAdapter(this,R.layout.adapter_view_layout, eventListFrag);
        mListViewFrag.setAdapter(adapter);
    }

    public void browser(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
        startActivity(browserIntent);
    }


    public void wolvesMenuFacebookBtnClick(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/uclsports/"));
        startActivity(browserIntent);
    }
    public void wolvesMenuYoutubeBtnClick(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCJI8JZky6BS3VkgC24xgXfA"));
        startActivity(browserIntent);
    }
    public void wolvesMenuWebBtnClick(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://uclouvain.be/fr/etudier/sport"));
        startActivity(browserIntent);
    }
    public void wolvesMenuPartenaireBtnClick(View view){
        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/uclsports/"));
        //startActivity(browserIntent);
    }


}
