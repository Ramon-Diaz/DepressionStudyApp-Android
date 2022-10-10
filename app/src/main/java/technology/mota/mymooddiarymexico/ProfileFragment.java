package technology.mota.mymooddiarymexico;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private EditText AGE;
    private Spinner GENDER;
    private Spinner LANGUAGE;

    private Button UPDATE;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    private CoordinatorLayout coordinatorLayout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        coordinatorLayout = v.findViewById(R.id.coordinator_layout_profile);

        AGE = v.findViewById(R.id.age);

        //get the spinner from the xml.
        Spinner spinner_gender = (Spinner) v.findViewById(R.id.spinner_gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(v.getContext(),
                R.array.dropdown_gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_gender.setAdapter(adapter_gender);

        ArrayAdapter<CharSequence> adapter_language = ArrayAdapter.createFromResource(v.getContext(),
                R.array.dropdown_language, android.R.layout.simple_spinner_item);
        adapter_language.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_language = (Spinner) v.findViewById(R.id.spinner_language);
        spinner_language.setAdapter(adapter_language);

        GENDER = spinner_gender;
        LANGUAGE = spinner_language;

        UPDATE = v.findViewById(R.id.update);
       // UPDATE.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String alias = sharedPreferences.getString(KEY_ALIAS, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);
        String age = sharedPreferences.getString("AGE", null);
        int gender = sharedPreferences.getInt("GENDER", -1);
        int language = sharedPreferences.getInt("LANGUAGE", -1);

        if(age != null){
            AGE.setText(age);
        }
        if(gender != -1){
            GENDER.setSelection(gender);
        }
        if(language != -1){
            LANGUAGE.setSelection(language);
        }

        UPDATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the written text
                String ageText = AGE.getText().toString();
                int genderInt= GENDER.getSelectedItemPosition();
                int languageInt = LANGUAGE.getSelectedItemPosition();
                String genderText = GENDER.getSelectedItem().toString();
                String languageText = LANGUAGE.getSelectedItem().toString();
                // save it in a json object to later convert it to string

                if(ageText == null || genderInt == 0 || languageInt == 0){
                    Toast.makeText(getActivity(), "All fields must be completed", Toast.LENGTH_SHORT).show();
                } else{

                    JSONObject data = new JSONObject();
                    try{
                        data.put("age", ageText);
                        data.put("gender", genderText);
                        data.put("language", languageText);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    // send the data
                    AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=info")
                            .addBodyParameter("email", alias)
                            .addBodyParameter("password", password)
                            .addBodyParameter("data", data.toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {

                                    // get the user data if it is not on the device
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("AGE", ageText);
                                    editor.putInt("GENDER", genderInt);
                                    editor.putInt("LANGUAGE", languageInt);
                                    editor.apply();

                                    showSnackbar();

                                    UPDATE.setEnabled(false);

                                }
                                @Override
                                public void onError(ANError error) {
                                    Toast.makeText(getActivity(), error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), error.getErrorBody(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
        return v;
    }

    public void showSnackbar(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Updated", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
