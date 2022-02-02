package technology.mota.studentstressstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    Button bRegister;
    EditText etAlias, etPassword;
    //FirebaseAuth fAuth;
    ProgressBar progressBar;
    //FirebaseFirestore fStore;
    //String userID;

    // Shared preferences
    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etAlias = (EditText) findViewById(R.id.etAlias);
        //etAge = (EditText) findViewById(R.id.etAge);
        //etGender = (EditText) findViewById(R.id.etGender);
        //etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        bRegister = (Button) findViewById(R.id.bRegister);

        //fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        // if current user is active
        //if(fAuth.getCurrentUser() != null){
        //    startActivity(new Intent(getApplicationContext(),MainActivity.class));
        //    finish();
        //}

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String name = sharedPreferences.getString(KEY_ALIAS, null);
        if (name!= null){
            Toast.makeText(Register.this, "You are already registered",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alias = etAlias.getText().toString();
            //    int age = Integer.parseInt(etAge.getText().toString());
            //    String gender = etGender.getText().toString();
             //   String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if(TextUtils.isEmpty(alias)){
                    etAlias.setError(view.getContext().getString(R.string.alias_error));
                    return;
                }
//                if(TextUtils.isEmpty(gender)){
//                    etGender.setError(view.getContext().getString(R.string.gender_error));
//                    return;
//                }
//                if(TextUtils.isEmpty(username)){
//                    etUsername.setError(view.getContext().getString(R.string.email_error));
//                    return;
//                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError(view.getContext().getString(R.string.password_error));
                    return;
                }
                if(password.length()< 6){
                    etPassword.setError(view.getContext().getString(R.string.password_error_2));
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_ALIAS, alias);
                editor.putString(KEY_PASSWORD, password);
                editor.apply();

                Toast.makeText(Register.this, R.string.user_created,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login.class));

//                fAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Register.this, R.string.user_created,Toast.LENGTH_SHORT).show();
//                            userID = fAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fStore.collection("users").document(userID);
//                            Map<String,Object> user = new HashMap<>();
//                            user.put("alias",alias);
//                            user.put("age", String.valueOf(age));
//                            user.put("gender",gender);
//                            user.put("username",username);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d(TAG, "onSuccess: user profile is created for "+ userID);
//                                }
//                            });
//                            startActivity(new Intent(getApplicationContext(),Login.class));
//                        } else {
//                            Toast.makeText(Register.this, R.string.error + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });

            }
        });


    }


}