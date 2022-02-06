package technology.mota.studentstressstudy;


import android.content.SharedPreferences;
import android.os.Bundle;

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

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText AGE;
    private Spinner GENDER;
    private Spinner LANGUAGE;

    private Button UPDATE;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

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
        UPDATE.setOnClickListener(this);


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
        return v;
    }

    @Override
    public void onClick(View view) {

        String ageText = AGE.getText().toString();
        int genderText = GENDER.getSelectedItemPosition();
        int languageText = LANGUAGE.getSelectedItemPosition();

        // get the user data if it is not on the device
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("AGE", ageText);
        editor.putInt("GENDER", genderText);
        editor.putInt("LANGUAGE", languageText);
        editor.apply();

        Toast.makeText(getActivity(),"Updated!", Toast.LENGTH_SHORT).show();

    }
}
