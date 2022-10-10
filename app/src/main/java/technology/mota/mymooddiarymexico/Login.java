package technology.mota.mymooddiarymexico;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class Login extends AppCompatActivity {

    Button bLogin;
    EditText etAlias, etPassword;
    TextView tvRegisterLink;
    ProgressBar progressBar;
    TextView forgotPassword;

    SharedPreferences sharedPreferences;
//    SharedPreferences sharedPreferences_login;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

//    static boolean isLogout = FALSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // look for the content view which is currently displayed in xml and will assign it to the variable
        etAlias = (EditText) findViewById(R.id.etAlias);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.blogin);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        progressBar = findViewById(R.id.progressBarLogin);
        forgotPassword = findViewById(R.id.forgotPassword);

//        sharedPreferences_login = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//
//        String alias = sharedPreferences_login.getString(KEY_ALIAS, null);
//        String password = sharedPreferences_login.getString(KEY_PASSWORD, null);
//
//        if(alias.length() > 1 && password.length() > 1 && isLogout == FALSE){
//            progressBar.setVisibility(View.VISIBLE);
//            AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=login")
//                    .addBodyParameter("email", alias)
//                    .addBodyParameter("password", password)
//                    .setPriority(Priority.MEDIUM)
//                    .build()
//                    .getAsString(new StringRequestListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            progressBar.setVisibility(View.GONE);
//                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        }
//                        @Override
//                        public void onError(ANError error) {
//                            //   Toast.makeText(Register.this, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(Login.this, error.getErrorBody(), Toast.LENGTH_LONG).show();
//
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });
//        }

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

                AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=login")
                        .addBodyParameter("email", alias)
                        .addBodyParameter("password", password)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(Login.this, response.toString(), Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_ALIAS, alias);
                                editor.putString(KEY_PASSWORD, password);
                                editor.apply();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                            @Override
                            public void onError(ANError error) {
                                //   Toast.makeText(Register.this, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(Login.this, error.getErrorBody(), Toast.LENGTH_LONG).show();

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetAlias = new EditText(view.getContext());

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle(view.getContext().getString(R.string.reset_password));
                passwordResetDialog.setMessage(view.getContext().getString(R.string.email_prompt_reset));
                passwordResetDialog.setView(resetAlias);

                passwordResetDialog.setPositiveButton(view.getContext().getString((R.string.send)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String alias_ = resetAlias.getText().toString();
                        if(TextUtils.isEmpty(alias_)){
                            Toast.makeText(view.getContext(), view.getContext().getString(R.string.alias_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // send an email to diazramo@ualberta.ca to send the email
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:?subject=" + "Password Reset Depression Study App"+ "&body=" + "Hi Ramon, can you reset my password, my alias is: "+alias_ + "&to=" + "diazramo@ualberta.ca"));


                        if (emailIntent.resolveActivity(getPackageManager()) != null){
                            startActivity(emailIntent);
                        } else {
                            Toast.makeText(view.getContext(), "There is no application that supports this action", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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