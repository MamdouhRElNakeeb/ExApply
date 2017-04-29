package ml.exapply.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ml.exapply.app.Objects.UserData;
import com.exapply.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mamdouhelnakeeb on 2/26/17.
 */

public class Register extends AppCompatActivity {

    EditText nameET, emailET, passwordET, mobileET;
    Spinner typeS;
    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;

    TextView linkToLogin;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);



        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        nameET = (EditText)findViewById(R.id.user_name);
        mobileET = (EditText)findViewById(R.id.user_mobile);
        passwordET = (EditText)findViewById(R.id.user_password);
        emailET = (EditText)findViewById(R.id.user_email);
        typeS = (Spinner) findViewById(R.id.user_type);
        signUpButton = (Button)findViewById(R.id.regBtn);

        linkToLogin = (TextView) findViewById(R.id.linkToLogin);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeS.setAdapter(adapter);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                final String email = emailET.getText().toString().trim();
                final String mobile = mobileET.getText().toString().trim();
                final String type = typeS.getSelectedItem().toString().trim();


                if (password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage(R.string.signup_error_message)
                            .setTitle(R.string.signup_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(new UserData(name, email, mobile, type));

                                        saveToSharedPrefs(name, mobile, type);

                                        Intent intent = new Intent(getBaseContext(), Home.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle(R.string.login_error_title)
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                }
            }
        });


        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveToSharedPrefs(String name, String mobile, String type){


        prefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
        editor = prefs.edit();

        editor.putString("login", "1");
        editor.putString("user_id", mFirebaseAuth.getCurrentUser().getUid());
        editor.putString("name", name);
        editor.putString("email", mFirebaseAuth.getCurrentUser().getEmail());
        editor.putString("mobile", mobile);
        editor.putString("type", type);
        editor.apply();

    }

}
