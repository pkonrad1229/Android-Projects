package com.example.myparse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    boolean loginTrue = true;
    EditText username;
    EditText password;
    Button button;
    TextView textView;
    ImageView imageView;
    ConstraintLayout constraintLayout;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            buttonPressed(v);
        }
        return false;
    }

    public void buttonPressed(View view) {
        if (username.getText().toString()=="" || password.getText().toString()=="") {
            Toast.makeText(MainActivity.this, "Username and password required!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (loginTrue == true) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "logged in correctly", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "signed up user :" + user.getUsername(), Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textView){
            if (loginTrue==true) {
                button.setText("Sign up");
                textView.setText("or, Login");
                loginTrue = false;
            } else {
                button.setText("Login");
                textView.setText("or, Sign up");
                loginTrue = true;
            }
        } else if (view.getId() == R.id.backgroundLayout || view.getId() == R.id.backgroundLayout){
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.backgroundLayout);
        setTitle("Instagram");

        textView.setOnClickListener(this);

        password.setOnKeyListener(this);
        imageView.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);



        /*ParseObject score = new ParseObject("Score");
        score.put("username", "sean");
        score.put("score", 65);
        score.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // OK
                    Log.i("success", "We saved a score");
                } else {
                    e.printStackTrace();
                }
            }
        });




        ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

       query.whereGreaterThan("score", 50);
       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
               if (e==null && objects.size()>0) {
                   for (ParseObject object : objects) {
                       object.put("score", object.getInt("score")+20 );
                       object.saveInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               if (e==null) {
                                   Log.i("success", "We saved a score");
                               }
                           }
                       });
                   }
               }
           }
       });


        ParseUser user = new ParseUser();
        user.setUsername("nick");
        user.setPassword("password");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    Log.i("user","created");
                } else {
                    e.printStackTrace();
                }
            }
        });



        ParseUser.logInInBackground("nick", "passsword", new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user!=null) {
                    Log.i("user","logged in!");
                } else {
                    e.printStackTrace();
                }
            }
        });


        ParseUser.logOut();
        if (ParseUser.getCurrentUser() != null) {
            Log.i("Signed in", ParseUser.getCurrentUser().getUsername());
        } else {
            Log.i("not signed in", "not signed");
        }


        ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e==null && users.size()>0) {
                    for (ParseUser user : users) {
                        Log.i("name", user.getUsername());
                        //users.add(object.getString("username"));
                    }
                } else {
                    Log.i("ERROR", "problem with users");
                }

            }
        });

         */
        if (ParseUser.getCurrentUser() != null) {
            showUserList();
        }








        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}