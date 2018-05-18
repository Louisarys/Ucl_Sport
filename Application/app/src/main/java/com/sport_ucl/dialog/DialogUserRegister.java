package com.sport_ucl.dialog;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.FireHelp;
import com.sport_ucl.MainActivity;
import com.sport_ucl.R;
import com.sport_ucl.User;
import com.sport_ucl.UserSingleton;
import com.sport_ucl.module.Planning;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by valentin on 11/03/18.
 */

/**
 * Boite de dialogue qui s'ouvre lorsqu'on selectionne s'enregister
 * lors de la connexion de l'utilisateur.
 */

public class DialogUserRegister extends AppCompatDialogFragment {

    private Button btnUserDateDeNaissance;
    private EditText login;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private EditText email;

    //********************************  BirthDay Variable   **************************************//

    private static final String TAG = "DialogUserRegister";
    private TextView mDisplayBirthDay; //déclare un champ birthday de type TextView
    private DatePickerDialog.OnDateSetListener mDateSetListener; //declare la fin de selection
    private int dateActuelle ;
    private int dateEnterInt ;
    private String date ;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register_user, null);

        btnUserDateDeNaissance = view.findViewById(R.id.btnUserDateDeNaissance);
        login = view.findViewById(R.id.etUserLogin);
        password = view.findViewById(R.id.etUserPassword);
        first_name = view.findViewById(R.id.etUserPrenom);
        last_name = view.findViewById(R.id.etUserName);
        email = view.findViewById(R.id.etUserMail);


        btnUserDateDeNaissance.setOnClickListener(new View.OnClickListener() { // méthode d'écoute sur le click
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(); //recupération jj, mm, yyyy actuel
                int year  = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day   = cal.get(Calendar.DAY_OF_MONTH);

                dateActuelle = year + month + day ;

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),                        // Context
                        mDateSetListener,                                 // l'écoute
                        year, month, day                                  // Parametres
                );

                // Application sur la boite de dialogue actuel
                dialog.show();

            }
        });




        builder.setView(view)
                .setTitle("Inscription")
                .setPositiveButton("S'enregister", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (!login.getText().toString().equals("") && !password.getText().toString().equals("")){
                            UserSingleton.getINSTANCE().setUser(new User(first_name.getText().toString(),
                                    last_name.getText().toString(), null, null,
                                    email.getText().toString(), null, -1, false));
                            FireHelp fire = new FireHelp(getContext());
                            fire.validateLogin(login.getText().toString(), password.getText().toString());
                        }
                        else{
                            makeLoginToast();
                        }

                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });





        return builder.create();
    }

    public void makeLoginToast(){
        if (login.getText().toString().equals("") && password.getText().toString().equals("")){
            Toast.makeText(getContext(), "veuillez rentrer login et un mot de passe", Toast.LENGTH_SHORT).show();
        }
        else {
            if (login.getText().toString().equals("")){
                Toast.makeText(getContext(), "veuillez rentrer un login", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "veuillez rentrer un mot de passe", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
