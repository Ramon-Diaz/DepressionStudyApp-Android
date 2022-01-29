package technology.mota.studentstressstudy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class LogFragment extends Fragment implements View.OnClickListener {


    private Spinner FIRST;
    private Spinner SECOND;
    private Spinner THIRD;
    private Spinner FOURTH;
    private Spinner FIFTH;
    private Spinner SIXTH;
    private Spinner SEVENTH;

    private Spinner EIGHT;
    private Spinner NINTH;
    private Spinner TENTH;
    private Spinner ELEVENTH;
    private Spinner TWELFTH;
    private Spinner THIRTEENTH;
    private Spinner FOURTEENTH;

    private Spinner FIFTEENTH;
    private Spinner SIXTEENTH;
    private Spinner SEVENTEENTH;
    private Spinner EIGHTEENTH;
    private Spinner NINETEENTH;
    private Spinner TWENTIETH;
    private Spinner TWENTYFIRST;

    private Button SEND_LOG;


    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log, container, false);

        /////////////////////////////////////////////////
        //get the spinner from the xml.
        Spinner spinner1 = (Spinner) v.findViewById(R.id.spinner1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.dropdown_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);

        //spinner.getSelectedItem().toString()

        Spinner spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);

        Spinner spinner3 = (Spinner) v.findViewById(R.id.spinner3);
        spinner3.setAdapter(adapter);

        Spinner spinner4 = (Spinner) v.findViewById(R.id.spinner4);
        spinner4.setAdapter(adapter);

        Spinner spinner5 = (Spinner) v.findViewById(R.id.spinner5);
        spinner5.setAdapter(adapter);

        Spinner spinner6 = (Spinner) v.findViewById(R.id.spinner6);
        spinner6.setAdapter(adapter);

        Spinner spinner7 = (Spinner) v.findViewById(R.id.spinner7);
        spinner7.setAdapter(adapter);

        Spinner spinner8 = (Spinner) v.findViewById(R.id.spinner8);
        spinner8.setAdapter(adapter);

        Spinner spinner9 = (Spinner) v.findViewById(R.id.spinner9);
        spinner9.setAdapter(adapter);

        Spinner spinner10 = (Spinner) v.findViewById(R.id.spinner10);
        spinner10.setAdapter(adapter);

        Spinner spinner11 = (Spinner) v.findViewById(R.id.spinner11);
        spinner11.setAdapter(adapter);

        Spinner spinner12 = (Spinner) v.findViewById(R.id.spinner12);
        spinner12.setAdapter(adapter);

        Spinner spinner13 = (Spinner) v.findViewById(R.id.spinner13);
        spinner13.setAdapter(adapter);

        Spinner spinner14 = (Spinner) v.findViewById(R.id.spinner14);
        spinner14.setAdapter(adapter);

        Spinner spinner15 = (Spinner) v.findViewById(R.id.spinner15);
        spinner15.setAdapter(adapter);

        Spinner spinner16 = (Spinner) v.findViewById(R.id.spinner16);
        spinner16.setAdapter(adapter);

        Spinner spinner17 = (Spinner) v.findViewById(R.id.spinner17);
        spinner17.setAdapter(adapter);

        Spinner spinner18 = (Spinner) v.findViewById(R.id.spinner18);
        spinner18.setAdapter(adapter);

        Spinner spinner19 = (Spinner) v.findViewById(R.id.spinner19);
        spinner19.setAdapter(adapter);

        Spinner spinner20 = (Spinner) v.findViewById(R.id.spinner20);
        spinner20.setAdapter(adapter);

        Spinner spinner21 = (Spinner) v.findViewById(R.id.spinner21);
        spinner21.setAdapter(adapter);

        FIRST = spinner1;
        SECOND = spinner2;
        THIRD = spinner3;
        FOURTH = spinner4;
        FIFTH = spinner5;
        SIXTH = spinner6;
        SEVENTH = spinner7;
        EIGHT = spinner8;
        NINTH = spinner9;
        TENTH = spinner10;
        ELEVENTH = spinner11;
        TWELFTH = spinner12;
        THIRTEENTH = spinner13;
        FOURTEENTH = spinner14;
        FIFTEENTH = spinner15;
        SIXTEENTH = spinner16;
        SEVENTEENTH = spinner17;
        EIGHTEENTH = spinner18;
        NINETEENTH = spinner19;
        TWENTIETH = spinner20;
        TWENTYFIRST = spinner21;

        SEND_LOG = v.findViewById(R.id.sendLog);

        SEND_LOG.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        //RadioButton first = getView().findViewById(FIRST.getCheckedRadioButtonId());

        //RadioButton second = getView().findViewById(SECOND.getCheckedRadioButtonId());
        //RadioButton third = getView().findViewById(THIRD.getCheckedRadioButtonId());
        //RadioButton fourth = getView().findViewById(FOURTH.getCheckedRadioButtonId());
        //RadioButton fifth = getView().findViewById(FIFTH.getCheckedRadioButtonId());
        //RadioButton seventh = getView().findViewById(SEVENTH.getCheckedRadioButtonId());
        //RadioButton sixth = getView().findViewById(SIXTH.getCheckedRadioButtonId());

        Integer firstText = FIRST.getSelectedItemPosition();
        Integer secondText = SECOND.getSelectedItemPosition();
        Integer thirdText = THIRD.getSelectedItemPosition();
        Integer fourthText = FOURTH.getSelectedItemPosition();
        Integer fifthText = FIFTH.getSelectedItemPosition();
        Integer sixthText = SIXTH.getSelectedItemPosition();

        Integer seventhText = SEVENTH.getSelectedItemPosition();
        Integer eightText = EIGHT.getSelectedItemPosition();
        Integer ninthText = NINTH.getSelectedItemPosition();
        Integer tenthText = TENTH.getSelectedItemPosition();
        Integer eleventhText = ELEVENTH.getSelectedItemPosition();
        Integer twelfthText = TWELFTH.getSelectedItemPosition();
        Integer thirteenthText = THIRTEENTH.getSelectedItemPosition();
        Integer fourteenthText = FOURTEENTH.getSelectedItemPosition();
        Integer fifteenthText = FIFTEENTH.getSelectedItemPosition();
        Integer sixteenthText = SIXTEENTH.getSelectedItemPosition();
        Integer seventeenthText = SEVENTEENTH.getSelectedItemPosition();
        Integer eighteenthText = EIGHTEENTH.getSelectedItemPosition();
        Integer nineteenthText = NINETEENTH.getSelectedItemPosition();
        Integer twentiethText = TWENTIETH.getSelectedItemPosition();
        Integer twentyfirstText = TWENTYFIRST.getSelectedItemPosition();


        if (!SendFunctionality.device_id.isEmpty()) {
            SendFunctionality.sendLog(getContext(), firstText, secondText, thirdText, fourthText, fifthText, sixthText, seventhText, eightText, ninthText, tenthText, eleventhText, twelfthText, thirteenthText, fourteenthText, fifteenthText, sixteenthText, seventeenthText, eighteenthText, nineteenthText, twentiethText, twentyfirstText);
        } else {
            Toast.makeText(getContext(), "Fill profile data", Toast.LENGTH_SHORT).show();
        }

    }

}