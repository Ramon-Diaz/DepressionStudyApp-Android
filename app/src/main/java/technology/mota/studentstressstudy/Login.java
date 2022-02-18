package technology.mota.studentstressstudy;

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

                AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=login")
                        .addBodyParameter("email", alias)
                        .addBodyParameter("password", password)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Login.this, response.toString(), Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_ALIAS, alias);
                                editor.putString(KEY_PASSWORD, password);
                                editor.apply();
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
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle(view.getContext().getString(R.string.reset_password));
                passwordResetDialog.setMessage(view.getContext().getString(R.string.email_prompt_reset));

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