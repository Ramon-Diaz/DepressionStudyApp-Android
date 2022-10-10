package technology.mota.mymooddiarymexico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Random;

public class RecordQuestionFragment extends Fragment {

    public RecordQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recording_custom, container, false);
        TextView question =  v.findViewById(R.id.freeRecordingDesc);

        int random = new Random().nextInt(9) + 1;

        switch ( random ) {
            case 2:
                question.setText(R.string.advice_2);
                break;
            case 3:
                question.setText(R.string.advice_3);
                break;
            case 4:
                question.setText(R.string.advice_4);
                break;
            case 5:
                question.setText(R.string.advice_5);
                break;
            case 6:
                question.setText(R.string.advice_6);
                break;
            case 7:
                question.setText(R.string.advice_7);
                break;
            case 8:
                question.setText(R.string.advice_8);
                break;
            case 9:
                question.setText(R.string.advice_9);
                break;
            case 10:
                question.setText(R.string.advice_10);
                break;
            default:
                question.setText(R.string.advice_1);
        }
        return v;
    }
}
