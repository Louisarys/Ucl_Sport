package com.sport_ucl;

/* Cette classe permet la gestion du menu principal de notre application */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sport_ucl.fragment.menu;
import com.sport_ucl.module.Amis;
import com.sport_ucl.Administrator.Administrateur;
import com.sport_ucl.module.Discussion;
import com.sport_ucl.module.ListDiscussion;
import com.sport_ucl.module.Planning;
import com.sport_ucl.module.RendezVous;
import com.sport_ucl.module.SportFavoris;


public class home extends AppCompatActivity {

    //*************************** nav_header data ********************************//
    private TextView tv_NameUser  ;
    private TextView  tv_EmailUser ;
    private ImageView iv_imageUser ;
    //************************ Fin nav_header data *******************************//

    //*************************** nav_header data ********************************//

    private ImageButton btn_Menu_planning      ;
    private ImageButton btn_Menu_sport_favori  ;
    private ImageButton btn_Menu_position      ;
    private ImageButton btn_Menu_amis          ;
    private ImageButton btn_Menu_administrator ;
    private ImageButton btn_Menu_discussion    ;
    private LinearLayout admin;

    //Todo Test de valentin
    private Button btn_test_val;

    //************************ Fin nav_header data *******************************//


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //************************ Fin nav_header data *******************************//

        //**************************** Gestion des click sur Module ********************************//

        btn_Menu_planning = (ImageButton) findViewById(R.id.Menu_planning_btn);
        btn_Menu_sport_favori = (ImageButton) findViewById(R.id.Menu_sport_favori_btn);
        btn_Menu_position = (ImageButton) findViewById(R.id.Menu_position_btn);
        btn_Menu_amis = (ImageButton) findViewById(R.id.Menu_amis_btn);
        btn_Menu_administrator = (ImageButton) findViewById(R.id.Menu_rdv_btn);
        btn_Menu_discussion = (ImageButton) findViewById(R.id.Menu_discussion_btn);
        admin = (LinearLayout) findViewById(R.id.admin_layout);


        if (!UserSingleton.getINSTANCE().getUser().getAdmin()){
            btn_Menu_administrator.setVisibility(View.GONE);
            admin.setVisibility(View.GONE);
        }

        //Todo Test de valentin
        btn_test_val = (Button) findViewById(R.id.test_val_test_btn);

        //*************************  FinGestion des click sur Module *******************************//

        //**************************** Gestion des click sur Module ********************************//

        btn_Menu_planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPlanning = new Intent(home.this, Planning.class);
                startActivity(intentPlanning);
            }
        });
        btn_Menu_sport_favori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSport = new Intent(home.this, SportFavoris.class);
                startActivity(intentSport);
            }
        });
        btn_Menu_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGps = new Intent(home.this, Gps.class);
                startActivity(intentGps);


            }
        });
        btn_Menu_amis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAmis = new Intent(home.this, Amis.class);
                startActivity(intentAmis);
            }
        });
        btn_Menu_administrator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdministrator = new Intent(home.this, Administrateur.class);
                startActivity(intentAdministrator);
            }
        });
        btn_Menu_discussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDiscussion = new Intent(home.this, ListDiscussion.class);
                startActivity(intentDiscussion);
            }
        });

        //TODO test de valentin
        btn_test_val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDiscussion = new Intent(home.this, menu.class);
                startActivity(intentDiscussion);
            }
        });

    }}
