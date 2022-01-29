package technology.mota.studentstressstudy;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private EditText ID;
    private EditText NAME;
    private EditText AGE;
    private EditText WATCH;
    private EditText WEIGHT;
    private EditText HEIGHT;
    private RadioButton FEMALE;
    private RadioButton MALE;
    private RadioButton OTHER;
    private RadioGroup GENDER;
    private CheckBox TERMS;

    private Button UPDATE;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        NAME = v.findViewById(R.id.username);
        ID = v.findViewById(R.id.deviceId);
        AGE = v.findViewById(R.id.age);
        WEIGHT = v.findViewById(R.id.weight);
        HEIGHT = v.findViewById(R.id.height);
        MALE = v.findViewById(R.id.male);
        OTHER = v.findViewById(R.id.other);
        TERMS = v.findViewById(R.id.terms);
        FEMALE = v.findViewById(R.id.female);
        GENDER = v.findViewById(R.id.gender);
        WATCH = v.findViewById(R.id.watch);
        UPDATE = v.findViewById(R.id.update);

        UPDATE.setOnClickListener(this);

        NAME.setText(SendFunctionality.username);
        ID.setText(SendFunctionality.device_id);
        AGE.setText(SendFunctionality.age);
        WEIGHT.setText(SendFunctionality.weight);
        HEIGHT.setText(SendFunctionality.height);
        WATCH.setText(SendFunctionality.watch);
        if ("F".equals(SendFunctionality.gender)) {
            MALE.setChecked(false);
            OTHER.setChecked(false);
            FEMALE.setChecked(true);
        } else if ("M".equals(SendFunctionality.gender)) {
            FEMALE.setChecked(false);
            OTHER.setChecked(false);
            MALE.setChecked(true);
        }
        else if ("O".equals(SendFunctionality.gender)) {
            MALE.setChecked(false);
            FEMALE.setChecked(false);
            OTHER.setChecked(true);
        }
        if ("T".equals(SendFunctionality.terms)) {
            TERMS.setChecked(true);
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        RadioButton radio = (RadioButton) getView().findViewById(GENDER.getCheckedRadioButtonId());

        SharedPreferences pref = getActivity().getSharedPreferences("StudentStressStudy", MODE_PRIVATE);

        String genderText = "";
        if (radio != null) {
            genderText = radio.getText().toString();
        }
        String gender;
        switch (genderText) {
            case "Femenino":
            case "Femme":
            case "Female":
                gender = "F";
                break;
            case "Male":
            case "Masculino":
            case "Homme":
                gender = "M";
                break;
            case "Other":
            case "Autre":
            case "Otro":
                gender = "O";
                break;
            default:
                gender = "";
        }
        String name = NAME.getText().toString();
        String age = AGE.getText().toString();
        String id = ID.getText().toString();
        String weight = WEIGHT.getText().toString();
        String height = HEIGHT.getText().toString();
        String watch = WATCH.getText().toString();
        String terms = TERMS.isChecked() ? "T" : "F";

        SendFunctionality.username = name;
        SendFunctionality.age = age;
        SendFunctionality.weight = weight;
        SendFunctionality.height = height;
        SendFunctionality.terms = terms;
        SendFunctionality.device_id = id;
        SendFunctionality.gender = gender;
        SendFunctionality.watch = watch;

        if (SendFunctionality.device_id.isEmpty()) {
            SendFunctionality.createUserData(getContext(),pref);
        } else {
            SendFunctionality.updateUserData(getContext(),pref);
        }

    }
}
