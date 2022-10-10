package technology.mota.mymooddiarymexico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

public class Register extends AppCompatActivity {

    Button bRegister;
    EditText etAlias, etPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAlias = (EditText) findViewById(R.id.etAlias);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        progressBar = findViewById(R.id.progressBar);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alias = etAlias.getText().toString();
                String password = etPassword.getText().toString();

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

                AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=register")
                        .addBodyParameter("email", alias)
                        .addBodyParameter("password", password)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Register.this, response.toString(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Login.class));
                            }
                            @Override
                            public void onError(ANError error) {
                             //   Toast.makeText(Register.this, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(Register.this, error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}