package technology.mota.mymooddiarymexico;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    // The state of connection
//    private HealthDataStore mStore;
//    private HealthConnectionErrorResult mConnError;
    public static final String APP_TAG = "Student_Stress_Study";
//    private HeartRateReader mReporterHR;
//    private SleepStageReader mReporterSleepStage;
//    private ExerciseReader mReporterEx;
//    private StepReader  mReporterStep;
//    private SleepReader mReporterSleep;
//    private BloodPressureReader mReporterBP;
//    private TemperatureReader mReporterT;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "StudentStressStudy";
    public static final String KEY_ALIAS = "alias";
    public static final String KEY_PASSWORD = "password";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction;
            switch (item.getItemId()) {
                case R.id.navigation_log:
                    Fragment log = fragmentManager.findFragmentByTag("log");
                    if (log == null) {
                        log = new LogFragment();
                    }
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, log, "log");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_home:
                    Fragment information = fragmentManager.findFragmentByTag("information");
                    if (information == null) {
                        information = new InformationFragment();
                    }
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, information, "information");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    Fragment profile = fragmentManager.findFragmentByTag("profile");
                    if (profile == null) {
                        profile = new ProfileFragment();
                    }
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, profile, "profile");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_voice:
                    Fragment record = fragmentManager.findFragmentByTag("record");
                    if (record == null) {
                        record = new RecordingOptionFragment();
                    }
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, record, "record");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                case R.id.navigation_upload:
                    Fragment upload = fragmentManager.findFragmentByTag("upload");
                    if (upload == null) {
                        upload = new UploadFragment();
                    }
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment, upload, "upload");
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets the navigation menu
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String alias = sharedPreferences.getString(KEY_ALIAS, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        //Toast.makeText(MainActivity.this, "User: " + alias ,Toast.LENGTH_SHORT).show();

        // Create a HealthDataStore instance and set its listener
//        mStore = new HealthDataStore(this, mConnectionListener);
//
//        // Request the connection to the health data store
//        mStore.connectService();
//        mReporterHR = new HeartRateReader(mStore, getApplicationContext());
//        mReporterSleepStage = new SleepStageReader(mStore, getApplicationContext());
//        mReporterSleep = new SleepReader(mStore, getApplicationContext());
//        mReporterStep = new StepReader(mStore, getApplicationContext());
//        mReporterEx = new ExerciseReader(mStore, getApplicationContext());
//        mReporterBP = new BloodPressureReader(mStore, getApplicationContext());
//        mReporterT = new TemperatureReader(mStore, getApplicationContext());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        Fragment information = new InformationFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, information, "information");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
        transaction.addToBackStack(null);
        transaction.commit();

//        PackageManager pm = this.getPackageManager();
//        ComponentName receiver = new ComponentName(this, BootReceiver.class);
//        Intent alarmIntent = new Intent(this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        //region Enable Daily Notifications
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 19);
//        calendar.set(Calendar.MINUTE, 1);
//        calendar.set(Calendar.SECOND, 1);
//        // if notification time is before selected time, send notification the next day
//        if (calendar.before(Calendar.getInstance())) {
//            calendar.add(Calendar.DATE, 1);
//        }
//        if (manager != null) {
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY, pendingIntent);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//            }
//        }
//        //To enable Boot Receiver class
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

//        // get data saved in device
//        SharedPreferences pref = getSharedPreferences("StudentStressStudy", MODE_PRIVATE);
//        SendFunctionality.device_id = pref.getString("DEVICE_ID", "");
//        SendFunctionality.username = pref.getString("USERNAME", "");
//        SendFunctionality.gender = pref.getString("GENDER", "");
//        SendFunctionality.age = pref.getString("AGE", "");
//        SendFunctionality.weight = pref.getString("WEIGHT", "");
//        SendFunctionality.height = pref.getString("HEIGHT", "");
//        SendFunctionality.terms = pref.getString("TERMS", "");
//        SendFunctionality.watch = pref.getString("WATCH", "");
//        SharedPreferences.Editor edit = pref.edit();
//        edit.putInt("ADVICE", (int) Math.ceil(Math.random() * 9) + 1);
//        edit.commit();
    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.bLogout:
//
//                startActivity(new Intent(getApplicationContext(), Login.class));
//                break;
//        }
//    }

//    @Override
//    public void onDestroy() {
//        mStore.disconnectService();
//        super.onDestroy();
//    }
//    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
//        @Override
//        public void onConnected() {
//            Log.d(APP_TAG, "onConnected");
//            if (isPermissionAcquired()) {
//                //TODO: SMTH
//
//            } else {
//                requestPermission();
//            }
//        }
//
//        @Override
//        public void onConnectionFailed(HealthConnectionErrorResult error) {
//            Log.d(APP_TAG, "onConnectionFailed");
//            showConnectionFailureDialog(error);
//        }
//
//        @Override
//        public void onDisconnected() {
//            Log.d(APP_TAG, "onDisconnected");
//            if (!isFinishing()) {
//                mStore.connectService();
//            }
//        }
//    };
//
//    private final HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult> mPermissionListener =
//            new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {
//
//                @Override
//                public void onResult(HealthPermissionManager.PermissionResult result) {
//                    Map<PermissionKey, Boolean> resultMap = result.getResultMap();
//                    // Show a permission alarm and clear step count if permissions are not acquired
//                    if (resultMap.values().contains(Boolean.FALSE)) {
//                        showPermissionAlarmDialog();
//                    } else {
//                        //TODO: SMTH
//                    }
//                }
//            };

//    private void showPermissionAlarmDialog() {
//        if (isFinishing()) {
//            return;
//        }
//        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//        alert.setTitle(R.string.notice).setMessage(R.string.msg_perm_acquired).setPositiveButton(R.string.ok, null).show();
//    }

//    private void showConnectionFailureDialog(final HealthConnectionErrorResult error) {
//        if (isFinishing()) {
//            return;
//        }
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//        if (error.hasResolution()) {
//            switch (error.getErrorCode()) {
//                case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
//                    alert.setMessage(R.string.msg_req_install);
//                    break;
//                case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
//                    alert.setMessage(R.string.msg_req_upgrade);
//                    break;
//                case HealthConnectionErrorResult.PLATFORM_DISABLED:
//                    alert.setMessage(R.string.msg_req_enable);
//                    break;
//                case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
//                    alert.setMessage(R.string.msg_req_agree);
//                    break;
//                default:
//                    alert.setMessage(R.string.msg_req_available);
//                    break;
//            }
//        } else {
//            alert.setMessage(R.string.msg_conn_not_available);
//        }
//
//        alert.setPositiveButton(R.string.ok, (dialog, id) -> {
//            if (error.hasResolution()) {
//                error.resolve(MainActivity.this);
//            }
//        });
//
//        if (error.hasResolution()) {
//            alert.setNegativeButton(R.string.cancel, null);
//        }
//
//        alert.show();
//    }
//
//    private boolean isPermissionAcquired() {
//        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
//        try {
//            // Check whether the permissions that this application needs are acquired
//            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(generatePermissionKeySet());
//            return !resultMap.values().contains(Boolean.FALSE);
//        } catch (Exception e) {
//            Log.e(APP_TAG, "Permission request fails.", e);
//        }
//        return false;
//    }
//
//    private void requestPermission() {
//        HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);
//        try {
//            // Show user permission UI for allowing user to change options
//            pmsManager.requestPermissions(generatePermissionKeySet(), MainActivity.this)
//                    .setResultListener(mPermissionListener);
//        } catch (Exception e) {
//            Log.e(APP_TAG, "Permission setting fails.", e);
//        }
//    }
//
//    private Set<PermissionKey> generatePermissionKeySet() {
//        Set<PermissionKey> pmsKeySet = new HashSet<>();
//        pmsKeySet.add(new PermissionKey(HealthConstants.Exercise.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.BodyTemperature.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.HeartRate.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.Sleep.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.SleepStage.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        pmsKeySet.add(new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, HealthPermissionManager.PermissionType.READ));
//        return pmsKeySet;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            //requestPermission();
            startActivity(new Intent(getApplicationContext(),
                    Login.class));
            //Login.isLogout = TRUE;
        }
        return true;
    }
//
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }
//
//    public void sendDataUtil(){
//        if (SendFunctionality.device_id.isEmpty()) {
//            Toast.makeText(this, "No id", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Toast.makeText(this, "Getting data ready...", Toast.LENGTH_SHORT).show();
//
//        mReporterHR.readHeartRate();
//        mReporterSleep.readSleep();
//        mReporterEx.readExcercise();
//        mReporterSleepStage.readSleepStage();
//        mReporterStep.readStep();
//        mReporterT.readTemperature();
//        mReporterBP.readPressure();
//    }
//    public void sendData(View v) {
//        Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();
//        SharedPreferences pref = getSharedPreferences("StudentStressStudy", MODE_PRIVATE);
//        SendFunctionality.device_id = pref.getString("DEVICE_ID", "");
//        if (SendFunctionality.device_id.isEmpty()) {
//            Toast.makeText(this, "Debes crear un perfil primero.", Toast.LENGTH_LONG).show();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction;
//            Fragment profile = new ProfileFragment();
//            transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.fragment, profile, "profile");
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //setting animation for fragment transaction
//            transaction.addToBackStack(null);
//            transaction.commit();
//
//        } else {
//            sendDataUtil();
//        }
//
//    }

}
