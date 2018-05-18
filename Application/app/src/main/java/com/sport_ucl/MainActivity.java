package com.sport_ucl;

//cette classe permet de gerer le menu de login de l'application

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sport_ucl.dialog.DialogUserRegister;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    //********************************** Variables  **************************//

    private static EditText et_login;
    private static EditText et_password;
    private static View viewSignIn;
    private LinearLayout.LayoutParams default_layout_params;

    private Button btn_sign_in;
    private Button btn_register;
    private Button btn_facebook;

    //****************************** Variables de validité *******************//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isOnline()){
            Toast.makeText(getApplicationContext(),
                    "Veuillez vous connecter à internet",
                    Toast.LENGTH_LONG)
                    .show();
            finish();
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ajout recent
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        default_layout_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        viewSignIn = inflater.inflate(R.layout.activity_connect, null);
        addContentView(viewSignIn, default_layout_params);


        et_login = (EditText) findViewById(R.id.Login_et);
        et_password = (EditText) findViewById(R.id.Password_et);

        //************************* Facebook         ******************************//
        btn_facebook = (Button) findViewById(R.id.Facebook_btn);
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exécuter la connexion vers facebook

                // Facebook();
                //final MediaPlayer sound = MediaPlayer.create(MainActivity.this, R.raw.sound);
                //sound.start();
                // Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                // startActivity(intent);
            }
        });
        //************************* Fin Facebook     ******************************//
        //************************* Register         ******************************//
        btn_register = (Button) findViewById(R.id.Register_btn);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //renvoyer vers le formulaire d'enregistrement
                //Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                //startActivity(intent);
                //switchRegisterProfil();

                OpenDialogUserRegister();


            }
        });
        //************************* Fin Register     ******************************//
        //************************* Submit           ******************************//

        //todo break point

        btn_sign_in = (Button) findViewById(R.id.Sign_in_btn);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FireHelp fire = new FireHelp(MainActivity.this);
                fire.validatePassword(et_login.getText().toString(), et_password.getText().toString());
                fire.eventModifyAlert();
                DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
                dbAdapter.testGetGps();
            }

            ;

            //************************* Fin Submit       ******************************//

        });
    }


    public void OpenDialogUserRegister() {
        DialogUserRegister dialogUserRegister = new DialogUserRegister();
        dialogUserRegister.show(getSupportFragmentManager(), "exemple dialog user register");
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

}
