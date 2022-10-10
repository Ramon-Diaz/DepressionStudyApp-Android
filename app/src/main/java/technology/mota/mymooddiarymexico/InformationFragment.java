package technology.mota.mymooddiarymexico;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;


public class InformationFragment extends Fragment {

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        TextView advice =  v.findViewById(R.id.advice);
       // SharedPreferences pref = getActivity().getSharedPreferences("StudentStressStudy", MODE_PRIVATE);
        //switch (pref.getInt("ADVICE",1)) {
        int random = new Random().nextInt(9) + 1;

        switch ( random ){
            case 2:
                advice.setText(R.string.advice_2);
                break;
            case 3:
                advice.setText(R.string.advice_3);
                break;
            case 4:
                advice.setText(R.string.advice_4);
                break;
            case 5:
                advice.setText(R.string.advice_5);
                break;
            case 6:
                advice.setText(R.string.advice_6);
                break;
            case 7:
                advice.setText(R.string.advice_7);
                break;
            case 8:
                advice.setText(R.string.advice_8);
                break;
            case 9:
                advice.setText(R.string.advice_9);
                break;
            case 10:
                advice.setText(R.string.advice_10);
                break;
            default:
                advice.setText(R.string.advice_1);
        }
        return v;
    }

}


