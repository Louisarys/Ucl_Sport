package com.sport_ucl.module;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.sport_ucl.ChatMessage;
import com.sport_ucl.R;
import com.sport_ucl.UserSingleton;

//Display chat
public class Discussion extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;
    private String chatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatName=getIntent().getExtras().getString("chatName");

        displayChatMessages();
    }

    private void displayChatMessages() {
        //Change title
        TextView titleDiscussion = (TextView) findViewById(R.id.titleChat) ;
        titleDiscussion.setText(chatName);

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        //Retrieve "chat" in Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference().child("chats").child(chatName).limitToLast(30);

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                // Get inputs
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

        //Post a message
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                if(input.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Veuillez entrer un message",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else{
                    // Push input to firebase database
                    FirebaseDatabase.getInstance()
                            .getReference().child("chats").child(chatName)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    UserSingleton.getINSTANCE().getUser().getFirstName())
                            );

                    // Clear the input
                    input.setText("");
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
