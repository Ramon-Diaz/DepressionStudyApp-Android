package technology.mota.studentstressstudy;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int RESULT_OK = -1;

    private Button btnSelect, btnUpload;
    private TextView txtFilePath;

    private int REQ_CODE = 21;
    private String encodedFile;

    private File fileToUpload  = null;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    ProgressBar progressBar;

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
                                Toast.makeText(getActivity(),"Succesful Upload", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            @Override
                            public void onError(ANError error) {
                                Toast.makeText(getActivity(), error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), error.getErrorBody(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

        return v;
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

