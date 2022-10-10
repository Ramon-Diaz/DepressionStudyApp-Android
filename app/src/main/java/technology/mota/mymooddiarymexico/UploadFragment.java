package technology.mota.mymooddiarymexico;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class UploadFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;

    private Button btnSelect, btnUpload;
    private TextView txtFilePath;

    private int REQ_CODE = 21;

    private File fileToUpload  = null;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    ProgressBar progressBar;

    private CoordinatorLayout coordinatorLayout;

    TextView detailsTextGarmin;
    LinearLayout linearLayoutGarmin;
    TextView detailsTextFitbit;
    LinearLayout linearLayoutFitbit;
    TextView detailsTextSamsung;
    LinearLayout linearLayoutSamsung;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_upload, container, false);

        txtFilePath = v.findViewById(R.id.uploadText);
        btnSelect = v.findViewById(R.id.btnSelect);
        btnUpload = v.findViewById(R.id.btnUpload);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String alias = sharedPreferences.getString(KEY_ALIAS, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        progressBar = v.findViewById(R.id.progressBar);

        coordinatorLayout = v.findViewById(R.id.coordinator_layout_upload);

        detailsTextGarmin = v.findViewById(R.id.garmin_text_1);
        linearLayoutGarmin = v.findViewById(R.id.linear_layout_garmin);
        linearLayoutGarmin.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        View garminButton = v.findViewById(R.id.linear_layout_garmin);

        garminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailsTextGarmin.getVisibility() == View.GONE){
                    detailsTextGarmin.setVisibility(View.VISIBLE);
                    TextView linkTextViewGarmin = view.findViewById(R.id.garmin_text_1);
                    linkTextViewGarmin.setMovementMethod(LinkMovementMethod.getInstance());
                } else{
                    detailsTextGarmin.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(linearLayoutGarmin, new AutoTransition());
            }
        });

        detailsTextFitbit = v.findViewById(R.id.fitbit_text_1);
        linearLayoutFitbit = v.findViewById(R.id.linear_layout_fitbit);
        linearLayoutFitbit.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        View fitbitButton = v.findViewById(R.id.linear_layout_fitbit);

        fitbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailsTextFitbit.getVisibility() == View.GONE){
                    detailsTextFitbit.setVisibility(View.VISIBLE);
                    TextView linkTextViewFitbit = view.findViewById(R.id.fitbit_text_1);
                    linkTextViewFitbit.setMovementMethod(LinkMovementMethod.getInstance());
                } else{
                    detailsTextFitbit.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(linearLayoutFitbit, new AutoTransition());
            }
        });

        detailsTextSamsung = v.findViewById(R.id.samsung_text_1);
        linearLayoutSamsung = v.findViewById(R.id.linear_layout_samsung);
        linearLayoutSamsung.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        View samsungButton = v.findViewById(R.id.linear_layout_samsung);

        samsungButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailsTextSamsung.getVisibility() == View.GONE){
                    detailsTextSamsung.setVisibility(View.VISIBLE);
                    TextView linkTextViewSamsung = view.findViewById(R.id.samsung_text_1);
                    linkTextViewSamsung.setMovementMethod(LinkMovementMethod.getInstance());
                } else{
                    detailsTextSamsung.setVisibility(View.GONE);
                }
                TransitionManager.beginDelayedTransition(linearLayoutSamsung, new AutoTransition());
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, REQ_CODE);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                AndroidNetworking.upload("https://hypatia.cs.ualberta.ca/depression/index.php?action=voice")
                        // to send the file
                        .addMultipartParameter("email", alias)
                        .addMultipartParameter("password", password)
                        .addMultipartFile("data", fileToUpload)
                        .setPriority(Priority.HIGH)
                        .build()
                        .setUploadProgressListener(new UploadProgressListener() {
                            @Override
                            public void onProgress(long bytesUploaded, long totalBytes) {
                                // do anything with progress
                            }
                        })
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                showSnackbar();
                                btnUpload.setEnabled(false);
                            }
                            @Override
                            public void onError(ANError error) {
                                Toast.makeText(getActivity(), error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

        return v;
    }

    public void expandGarmin (View view){

    }

    public void showSnackbar(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Successful Upload", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();

            String mSelectedPath = PathUtils.getPathFromUri(getContext(), path);
            txtFilePath.setText(mSelectedPath);
            fileToUpload = new File(mSelectedPath);
        }
    }

    private boolean checkPermission() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(), "Permission Successfull", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }


}

