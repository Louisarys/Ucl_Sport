package com.sport_ucl.module;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport_ucl.R;
import java.util.ArrayList;


/**
 * Created by Thomas on 1/05/2018.
 */

//Class to show and select a chat to join

public class ListDiscussion extends AppCompatActivity {

    private FloatingActionButton btn_ok;
    private AutoCompleteTextView input_name;
    public ArrayList<String> arr;
    public ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_discussion);

        //Get elements from layout
        btn_ok=findViewById(R.id.listDiscussionOK);
        input_name=findViewById(R.id.listDiscussionInput);

        //Click ok
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Veuillez entrer un chat",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else{
                    Intent intentDiscussion = new Intent(ListDiscussion.this, Discussion.class);
                    intentDiscussion.putExtra("chatName", input_name.getText().toString());
                    input_name.setText("");
                    startActivity(intentDiscussion);
                }

            }
        });

        //Display rooms and update it

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        arr = new ArrayList<>();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr.clear();
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {
                    arr.add(zoneSnapshot.getKey());
                }
                //Makes the ListView realtime
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // If error, log a message
                Log.w("err ListDiscussion", "onCancelled: ",databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(listener);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arr);

        ListView listView = (ListView) findViewById(R.id.list_of_chats);
        listView.setAdapter(adapter);

        //Add AutoCompletion
        input_name.setAdapter(new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,arr));
        input_name.setThreshold(1);
        input_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                input_name.setText(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Click on a chat
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chatName=adapterView.getItemAtPosition(i).toString();
                Intent intentDiscussion = new Intent(ListDiscussion.this, Discussion.class);
                intentDiscussion.putExtra("chatName", chatName);
                startActivity(intentDiscussion);
            }
        });
    }
}
