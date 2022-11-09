package technology.mota.mymooddiarymexico;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
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

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Environment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RecordFragment extends Fragment implements View.OnClickListener {

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


    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "MyMoodDiaryRecordings";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    short[] audioData;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    // private boolean isRecording = false;


    private CoordinatorLayout coordinatorLayout;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_record, container, false);

        coordinatorLayout = v.findViewById(R.id.coordinator_layout_message_record_text);

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


        bufferSize = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        audioData = new short [bufferSize]; //short array that pcm data is put into.

        playerSeekbar = v.findViewById(R.id.player_seekbar);
        /* Setting up on click listener
           - Class must implement 'View.OnClickListener' and override 'onClick' method
         */

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
                // SendFunctionality.sendFile(getContext(), fileToPlay.getAbsolutePath());
                File fileToUpload = new File(getFilename(recordFile));
                AndroidNetworking.upload("https://hypatia.cs.ualberta.ca/depression/index.php?action=voice")
                        // to send the audio file
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
                File fileToRemove = new File(getFilename(recordFile));
                fileToRemove.delete();

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
            if(checkPermissions() && checkStoragePermission()) {
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

    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        //Get app external directory path
        //    recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file

        filenameText.setText("Recording...");

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");
        recordingThread.start();
    }

    //Setup Media Recorder for recording
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setAudioChannels(1);
//        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
//        mediaRecorder.setAudioEncodingBitRate(16*44100);
//        mediaRecorder.setAudioSamplingRate(44100);
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //Start Recording
//        mediaRecorder.start();
//    }

    private void stopRecording() {
        //Stop Timer, very obvious
        timer.stop();

        //Get current date and time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        Date now = new Date();

        recordFile = "Recording_text" + "_" + formatter.format(now) + AUDIO_RECORDER_FILE_EXT_WAV;

        //Change text on page to file saved
        filenameText.setText("File Created: " + getFilename(recordFile));

        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(),getFilename(recordFile));
        deleteTempFile();

        //Stop media recorder and set it to null for further use to record new audio
    //    mediaRecorder.stop();
    //    mediaRecorder.release();
    //    mediaRecorder = null;
    //    fileToPlay = new File(recordPath+"/" + recordFile);
        File fileToPlay = new File(getFilename(recordFile));
    //    fileToPlay = new File(getTempFilename());
        playAudio(fileToPlay);
    }

    private String getTempFilename(){
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(getContext().getExternalFilesDir(null) ,AUDIO_RECORDER_FOLDER);
        }
        else{
            dir = new File(Environment.getExternalStorageDirectory().getPath(),AUDIO_RECORDER_FOLDER);
        }
        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }

        File tempFile = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            tempFile = new File(getContext().getExternalFilesDir(null) ,AUDIO_RECORDER_TEMP_FILE);
        }
        else{
            tempFile = new File(Environment.getExternalStorageDirectory().getPath(),AUDIO_RECORDER_TEMP_FILE);
        }

        if(tempFile.exists())
            tempFile.delete();

        return (dir.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private String getFilename(String recordFile){
    //    String filepath = Environment.getExternalStorageDirectory().getPath();
    //    File file = new File(filepath,AUDIO_RECORDER_FOLDER);
        File file = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            file = new File(getContext().getExternalFilesDir(null) ,AUDIO_RECORDER_FOLDER);
        }
        else{
            file = new File(Environment.getExternalStorageDirectory().getPath(),AUDIO_RECORDER_FOLDER);
        }

        if(!file.exists()){
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + recordFile);
    }

    private void writeAudioDataToFile(){
        byte data[] = new byte[bufferSize];

        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if(null != os){
            while(isRecording){
                read = recorder.read(data, 0, bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            // AppLog.logString("File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (1 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
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

    private boolean checkStoragePermission() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
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