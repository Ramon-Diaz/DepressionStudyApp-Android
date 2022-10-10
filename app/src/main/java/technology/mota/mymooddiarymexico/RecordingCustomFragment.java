package technology.mota.mymooddiarymexico;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RecordingCustomFragment extends Fragment implements View.OnClickListener {
    private ImageButton recordBtn;
    private TextView filenameText;

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private boolean isRecording = false;

    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;
    private File fileToPlay  = null;

    private Chronometer timer;

    private ImageButton playBtn;
    private Button sendBtn;
    private Button removeBtn;

    private TextView playerHeader;
    private TextView playerFilename;

    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    private String recordPath = null;

    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    private CoordinatorLayout coordinatorLayout;

    public RecordingCustomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_recording_custom, container, false);

        coordinatorLayout = v.findViewById(R.id.coordinator_layout_message_record_question);

        recordBtn = v.findViewById(R.id.record_btn);
        timer = v.findViewById(R.id.record_timer);
        filenameText = v.findViewById(R.id.record_filename);
        playerSheet = v.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);

        playBtn = v.findViewById(R.id.player_play_btn);
        playerHeader = v.findViewById(R.id.player_header_title);
        playerFilename = v.findViewById(R.id.player_filename);
        removeBtn = v.findViewById(R.id.removeFile);
        sendBtn = v.findViewById(R.id.sendFile);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String alias = sharedPreferences.getString(KEY_ALIAS, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);


        playerSeekbar = v.findViewById(R.id.player_seekbar);
        /* Setting up on click listener
           - Class must implement 'View.OnClickListener' and override 'onClick' method
         */
        TextView question =  v.findViewById(R.id.freeRecordingDesc);
        int random = new Random().nextInt(21);

        switch ( random ) {
            case 1:
                question.setText(R.string.question_1);
                break;
            case 2:
                question.setText(R.string.question_2);
                break;
            case 3:
                question.setText(R.string.question_3);
                break;
            case 4:
                question.setText(R.string.question_4);
                break;
            case 5:
                question.setText(R.string.question_5);
                break;
            case 6:
                question.setText(R.string.question_6);
                break;
            case 7:
                question.setText(R.string.question_7);
                break;
            case 8:
                question.setText(R.string.question_8);
                break;
            case 9:
                question.setText(R.string.question_9);
                break;
            case 10:
                question.setText(R.string.question_10);
                break;
            case 11:
                question.setText(R.string.question_11);
                break;
            case 12:
                question.setText(R.string.question_12);
                break;
            case 13:
                question.setText(R.string.question_13);
                break;
            case 14:
                question.setText(R.string.question_14);
                break;
            case 15:
                question.setText(R.string.question_15);
                break;
            case 16:
                question.setText(R.string.question_16);
                break;
            case 17:
                question.setText(R.string.question_17);
                break;
            case 18:
                question.setText(R.string.question_18);
                break;
            case 19:
                question.setText(R.string.question_19);
                break;
            case 20:
                question.setText(R.string.question_20);
                break;
            case 21:
                question.setText(R.string.question_21);
                break;
        }


        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We cant do anything here for this app
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    pauseAudio();
                } else {
                    if(fileToPlay != null){
                        resumeAudio();
                    }
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                AndroidNetworking.upload("https://hypatia.cs.ualberta.ca/depression/index.php?action=voice")
                        // to send the audio file
                        .addMultipartParameter("email", alias)
                        .addMultipartParameter("password", password)
                        .addMultipartFile("data", fileToPlay)
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
                                //Toast.makeText(getActivity(),"Succesful Upload", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                showSnackbar();
                                sendBtn.setEnabled(false);

                            }
                            @Override
                            public void onError(ANError error) {
                                //Toast.makeText(getActivity(), error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(getActivity(), error.getErrorBody(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fileToPlay.delete();
                stopAudio();
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });

        recordBtn.setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View view) {

        if(isRecording) {
            //Stop Recording
            stopRecording();

            // Change button image and set Recording state to false
            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
            isRecording = false;
        } else {
            //Check permission to record audio
            if(checkPermissions()) {
                //Start Recording
                startRecording();

                // Change button image and set Recording state to false
                recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                isRecording = true;
            }
        }

    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio() {
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        isPlaying = true;

        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void stopAudio() {
        //Stop The Audio
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_play_btn, null));
        playerHeader.setText(R.string.stopped);
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.player_pause_btn, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText(R.string.playing);
        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText(R.string.ended);
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }


    private void stopRecording() {
        //Stop Timer, very obvious
        timer.stop();

        //Change text on page to file saved
        filenameText.setText("File created: " + recordFile);

        //Stop media recorder and set it to null for further use to record new audio
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        fileToPlay = new File(recordPath+"/" + recordFile);
        playAudio(fileToPlay);
    }

    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //Get app external directory path
        recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        //Get current date and time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
        recordFile = "Recording_question" + "_" + formatter.format(now) + ".3gp";

        filenameText.setText("Recording...");

        //Setup Media Recorder for recording
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Start Recording
        mediaRecorder.start();
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording){
            stopRecording();
        }
    }

    public void showSnackbar(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Successful Upload", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}