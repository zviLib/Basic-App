package com.example.zvi.basicApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zvi.basicApp.client.Client;
import com.example.zvi.basicApp.client.CommandEnum;
import com.example.zvi.basicApp.client.InfoType;

import java.util.HashMap;

/**
 * 
 * @author Zvi Liebskind
 * An Activity that enables a sign up\in with the server
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button signin=(Button)findViewById(R.id.signin);
        final Button signup=(Button)findViewById(R.id.signup);

        // take credentials and confirm with the server
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // get the server's answer
                            final boolean signIn = signIn(((TextView)findViewById(R.id.username)).getText().toString(),((TextView)findViewById(R.id.password)).getText().toString().hashCode());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(signIn)
                                        ((TextView)findViewById(R.id.response)).setText("You have connected successfully");
                                        else
                                            ((TextView) findViewById(R.id.response)).setText("Cannot find user, please sign up");
                                    }
                                });
                        }
                    }).start();
            }
        });

        // register a new user
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View v = view;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String name = ((TextView)findViewById(R.id.username)).getText().toString();
                        // sign the user at the server
                        final boolean res = signUp(name,((TextView)findViewById(R.id.password)).getText().toString().hashCode());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(res)
                                    ((TextView)findViewById(R.id.response)).setText("You have registered successfully");
                                else
                                    ((TextView)findViewById(R.id.response)).setText("Username is not available");
                            }
                        });

                    }
                }).start();
            }
        });
    }

    /**
	 * check the credentails for a valid login
     * @param username - the entered user name
	 * @param password - hashcode of the entered password
     * @return whether the credentials are true
	 */
    public boolean signIn(String username,int password){
        Client client = new Client();
        HashMap<Integer,InfoType> map = new HashMap<>();
        map.put(0,new InfoType("loginUser"));
        map.put(1,new InfoType(username));
        map.put(2,new InfoType(password));
        String[][] rs = client.sendSelectCommand(map);
        return rs[1][0].equals("1");
    }

     /**
	 * register a new user at the server
     * @param username - the entered user name
	 * @param password - hashcode of the entered password
     * @return if the registration completed successfully
	 */
    public boolean signUp(String username,int password){
        
        // check if the username if available ///
        Client client = new Client();
        HashMap<Integer,InfoType> map = new HashMap<>();
        map.put(0,new InfoType("checkUsername"));
        map.put(1,new InfoType(username));
        String[][] rs = client.sendSelectCommand(map);
        if (rs[1][0].equals("1"))
            return false;
        ///////////////////////////////////////

        // register the user //////
        client = new Client();
        map = new HashMap<>();
        map.put(0,new InfoType("registerUser"));
        map.put(1,new InfoType(username));
        map.put(2,new InfoType(password));
        int ret = client.sendCommand(CommandEnum.sql,map);
        return ret!=0;
        ///////////////////////////
    }
}
