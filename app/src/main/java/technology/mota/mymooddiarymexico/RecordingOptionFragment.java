package technology.mota.mymooddiarymexico;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RecordingOptionFragment extends Fragment {


    public RecordingOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_recording_option, container, false);
        Button customBtn = v.findViewById(R.id.customBtn);
        Button textBtn = v.findViewById(R.id.textBtn);

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder noiseAlertDialog = new AlertDialog.Builder(v.getContext());
                noiseAlertDialog.setTitle("Important!");
                noiseAlertDialog.setMessage("Before recording yourself please make sure you are in a space without distractions or noise");

                noiseAlertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction;
                        Fragment recordF = fragmentManager.findFragmentByTag("recordF");
                        if (recordF == null) {
                            recordF = new RecordingCustomFragment();
                        }
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment, recordF, "recordF");
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
                noiseAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog
                    }
                });

                noiseAlertDialog.create().show();

            }
        });
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder noiseAlertDialog = new AlertDialog.Builder(v.getContext());
                noiseAlertDialog.setTitle("Important!");
                noiseAlertDialog.setMessage("Before recording yourself please make sure you are in a space without distractions or noise");

                noiseAlertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction;
                        Fragment textF = fragmentManager.findFragmentByTag("textF");
                        if (textF == null) {
                            textF = new RecordFragment();
                        }
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment, textF, "textF");
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });

                noiseAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close the dialog
                    }
                });

                noiseAlertDialog.create().show();

            }
        });
        return v;
    }
}