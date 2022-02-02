package technology.mota.studentstressstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button bLogin;
    EditText etAlias, etPassword;
    TextView tvRegisterLink;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    TextView forgotPassword;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // look for the content view which is currently displayed in xml and will assign it to the variable
        etAlias = (EditText) findViewById(R.id.etAlias);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.blogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        //fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogin);
        forgotPassword = findViewById(R.id.forgotPassword);

        // listen for a click
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alias = etAlias.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(TextUtils.isEmpty(alias)){
                    etAlias.setError(view.getContext().getString(R.string.alias_error));
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError(view.getContext().getString(R.string.password_error));
                    return;
                }
                if(password.length()< 6){
                    etPassword.setError(view.getContext().getString(R.string.password_error_2));
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

                String name = sharedPreferences.getString(KEY_ALIAS, null);
                String pass = sharedPreferences.getString(KEY_PASSWORD, null);

                if(name.equals(alias) && pass.equals(password)){
                    Toast.makeText(Login.this, R.string.welcome,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                } else{
                    etAlias.setError("Check alias or password!");
                    Toast.makeText(Login.this, "Alias or password is not correct!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                // authenticate the user
//                fAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Login.this, R.string.welcome,Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        } else {
//                            Toast.makeText(Login.this, R.string.welcome + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//
//                        }
//                    }
//                });
            }
        });


        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

                String name = sharedPreferences.getString(KEY_ALIAS, null);
                if(name != null){
                    Toast.makeText(Login.this, "You are already registered", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getApplicationContext(), Register.class));

                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  EditText resetMail = new EditText((view.getContext()));
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle(view.getContext().getString(R.string.reset_password));
                passwordResetDialog.setMessage(view.getContext().getString(R.string.email_prompt_reset));
               // passwordResetDialog.setView(resetMail);

//                passwordResetDialog.setPositiveButton(view.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        // extract the email and send reset link
//                        String email = resetMail.getText().toString();
//                        fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(Login.this, view.getContext().getString(R.string.reset_link_prompt), Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(Login.this, view.getContext().getString(R.string.reset_link_prompt_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//                });
//
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();
            }
        });

    }
}