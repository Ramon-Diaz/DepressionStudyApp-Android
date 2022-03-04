package technology.mota.studentstressstudy;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogFragment extends Fragment {

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

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    private CoordinatorLayout coordinatorLayout;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log, container, false);

        coordinatorLayout = v.findViewById(R.id.coordinator_layout_log);
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

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String alias = sharedPreferences.getString(KEY_ALIAS, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        SEND_LOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(),"Sending...", Toast.LENGTH_SHORT).show();

                int firstText = FIRST.getSelectedItemPosition();
                int secondText = SECOND.getSelectedItemPosition();
                int thirdText = THIRD.getSelectedItemPosition();
                int fourthText = FOURTH.getSelectedItemPosition();
                int fifthText = FIFTH.getSelectedItemPosition();
                int sixthText = SIXTH.getSelectedItemPosition();
                int seventhText = SEVENTH.getSelectedItemPosition();
                int eightText = EIGHT.getSelectedItemPosition();
                int ninthText = NINTH.getSelectedItemPosition();
                int tenthText = TENTH.getSelectedItemPosition();
                int eleventhText = ELEVENTH.getSelectedItemPosition();
                int twelfthText = TWELFTH.getSelectedItemPosition();
                int thirteenthText = THIRTEENTH.getSelectedItemPosition();
                int fourteenthText = FOURTEENTH.getSelectedItemPosition();
                int fifteenthText = FIFTEENTH.getSelectedItemPosition();
                int sixteenthText = SIXTEENTH.getSelectedItemPosition();
                int seventeenthText = SEVENTEENTH.getSelectedItemPosition();
                int eighteenthText = EIGHTEENTH.getSelectedItemPosition();
                int nineteenthText = NINETEENTH.getSelectedItemPosition();
                int twentiethText = TWENTIETH.getSelectedItemPosition();
                int twentyfirstText = TWENTYFIRST.getSelectedItemPosition();

                if (firstText == 0 || secondText == 0 || thirdText == 0 || fourthText == 0 || fifthText == 0 || sixthText == 0 || seventhText == 0 || eightText == 0 || ninthText == 0 || tenthText == 0 ||
                        eleventhText == 0 || twelfthText == 0 || thirteenthText == 0 || fourteenthText == 0 || fifteenthText == 0 || sixteenthText == 0 || seventeenthText == 0 || eighteenthText == 0 || nineteenthText == 0 || twentiethText == 0 ||
                        twentyfirstText == 0) {

                    Toast.makeText(getContext(), "All fields must completed", Toast.LENGTH_SHORT).show();

                } else {

                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    JSONObject data = new JSONObject();
                    try{
                        data.put("date", date);
                        data.put("first", firstText);
                        data.put("second", secondText);
                        data.put("third", thirdText);
                        data.put("fourth", fourthText);
                        data.put("fifth", fifthText);
                        data.put("sixth", sixthText);
                        data.put("seventh", seventhText);
                        data.put("eigth", eightText);
                        data.put("ninth", ninthText);
                        data.put("tenth", tenthText);
                        data.put("eleventh", eleventhText);
                        data.put("twelfth", twelfthText);
                        data.put("thirteenth", thirteenthText);
                        data.put("fourteenth", fourteenthText);
                        data.put("fifteenth", fifteenthText);
                        data.put("sixteenth", sixteenthText);
                        data.put("seventeenth", seventeenthText);
                        data.put("eighteenth", eighteenthText);
                        data.put("nineteenth", nineteenthText);
                        data.put("twentieth", twentiethText);
                        data.put("twentyfirst", twentyfirstText);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    // send the data
                    AndroidNetworking.post("https://hypatia.cs.ualberta.ca/depression/index.php?action=log")
                            .addBodyParameter("email", alias)
                            .addBodyParameter("password", password)
                            .addBodyParameter("data", data.toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    showSnackbar();
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
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Uploaded", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}