package technology.mota.studentstressstudy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static technology.mota.studentstressstudy.MainActivity.APP_TAG;

public class SendFunctionality {
    public static String device_id;
    public static String username;
    public static String gender;
    public static String age;
    public static String weight;
    public static String height;
    public static String terms;
    public static String watch;
    public static boolean alreadyWating = false;

    public static JsonArrayList<HeartRateReader.HeartRateBinningData> heartRateData;
    public static JsonArrayList<HeartRateReader.HeartRateBinningData> rawHearRate;

    public static JsonArrayList<ExerciseReader.ExcerciseBinningData> exerciseData;
    public static JsonArrayList<SleepStageReader.SleepBinningData> sleepStageData;
    public static JsonArrayList<SleepReader.SleepBinningData> sleepData;
    public static JsonArrayList<StepReader.StepBinningData> stepData;
    public static JsonArrayList<TemperatureReader.TemperatureBinningData> temperatureData;
    public static JsonArrayList<BloodPressureReader.BloodPressureBinningData> bloodPressureData;


    public static void sendInformation(Context context) {
        boolean waiting = false;
        if (heartRateData == null) {
            Log.d(APP_TAG,"Waiting for HR");
            waiting = true;
        }
        if (rawHearRate == null) {
            Log.d(APP_TAG,"Waiting for RawHR");
            waiting = true;
        }
        if (exerciseData == null) {
            Log.d(APP_TAG,"Waiting for Excercise");
            waiting = true;
        }
        if (sleepData == null) {
            Log.d(APP_TAG,"Waiting for Sleep");
            waiting = true;
        }

        if (sleepStageData == null) {
            Log.d(APP_TAG,"Waiting for Sleep Stage");
            waiting = true;
        }

        if (stepData == null) {
            Log.d(APP_TAG,"Waiting for Step");
            waiting = true;
        }

        if (temperatureData == null) {
            Log.d(APP_TAG,"Waiting for Temperature");
            waiting = true;
        }

        if (bloodPressureData == null) {
            Log.d(APP_TAG,"Waiting for Blood");
            waiting = true;
        }

        if (waiting && !SendFunctionality.alreadyWating) {
            Toast.makeText(context, "Still waiting", Toast.LENGTH_SHORT).show();
            SendFunctionality.alreadyWating = true;
            return;
        }
        if (waiting && SendFunctionality.alreadyWating) {
            return;
        }

        Toast.makeText(context, "Building json", Toast.LENGTH_SHORT).show();
        JSONObject json = new JSONObject();
        try {
            json.put("hr_data", heartRateData.toJson());
            json.put("ss_data", sleepStageData.toJson());
            json.put("s_data", sleepData.toJson());
            json.put("exercise_data", exerciseData.toJson());
            json.put("id",device_id);
            json.put("t_data", temperatureData.toJson());
            json.put("bp_data", bloodPressureData.toJson());
            json.put("step_data", stepData.toJson());
            json.put("raw_hr",rawHearRate.toJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(APP_TAG, json.toString());
        Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post("https://mota.technology/StudentDepression/API/DATA/")
                .addJSONObjectBody(json)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            String error  = response.getString("error");
                            if ("false".equalsIgnoreCase(error)) {
                                Toast.makeText(context, "Data sent", Toast.LENGTH_SHORT).show();
                                alreadyWating = false;
                            } else {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "Data response not received", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void updateUserData (Context context, SharedPreferences pref) {
        if (device_id  == null
                || username == null
                ||  gender == null
                ||  age == null
                || weight == null
                || height == null
                || terms == null
                ||  gender.isEmpty()
                ||  age.isEmpty()
                || weight.isEmpty()
                || height.isEmpty()
                || terms.equals("F")
        ) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "Building json", Toast.LENGTH_SHORT).show();
        JSONObject json = new JSONObject();
        try {
            json.put("id", device_id);
            json.put("username", username);
            json.put("age", age);
            json.put("gender", gender);
            json.put("weight", weight);
            json.put("height", height);
            json.put("terms", terms);
            json.put("watch", watch);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(APP_TAG, json.toString());
        Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post("https://mota.technology/StudentDepression/API/UPDATE/")
                .addJSONObjectBody(json)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            device_id = response.getString("id");
                            username = response.getString("username");
                            gender = response.getString("gender");
                            age = response.getString("age");
                            weight = response.getString("weight");
                            height = response.getString("height");
                            terms = response.getString("terms");
                            watch = response.getString("watch");
                            if (device_id.isEmpty()) {
                                Toast.makeText(context, "No id", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("DEVICE_ID", device_id);
                            edit.putString("USERNAME", username);
                            edit.putString("GENDER", gender);
                            edit.putString("AGE", age);
                            edit.putString("WEIGHT", weight);
                            edit.putString("HEIGHT", height);
                            edit.putString("TERMS", terms);
                            edit.putString("WATCH", watch);
                            edit.commit();
                            Toast.makeText(context, "Data Updated: "+device_id , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "ID no response", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void createUserData (Context context, SharedPreferences pref) {
        if (username == null
                ||  gender == null
                ||  age == null
                || weight == null
                || height == null
                || terms == null
                || username.isEmpty()
                ||  gender.isEmpty()
                ||  age.isEmpty()
                || weight.isEmpty()
                || height.isEmpty()
                || terms.equals("F")
        ) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "Building json", Toast.LENGTH_SHORT).show();
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("age", age);
            json.put("gender", gender);
            json.put("weight", weight);
            json.put("height", height);
            json.put("terms", terms);
            json.put("watch", watch);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(APP_TAG, json.toString());
        Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post("https://mota.technology/StudentDepression/API/NEW/")
                .addJSONObjectBody(json)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            device_id = response.getString("id");
                            username = response.getString("username");
                            gender = response.getString("gender");
                            age = response.getString("age");
                            weight = response.getString("weight");
                            height = response.getString("height");
                            terms = response.getString("terms");
                            watch = response.getString("watch");
                            if (device_id.isEmpty()) {
                                Toast.makeText(context, "No id", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("DEVICE_ID", device_id);
                            edit.putString("USERNAME", username);
                            edit.putString("GENDER", gender);
                            edit.putString("AGE", age);
                            edit.putString("WEIGHT", weight);
                            edit.putString("HEIGHT", height);
                            edit.putString("TERMS", terms);
                            edit.putString("WATCH", watch);
                            edit.commit();

                            Toast.makeText(context, "ID CREATED: "+device_id , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "ID no response", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void sendLog(Context context, Integer firstText, Integer secondText, Integer thirdText, Integer fourthText, Integer fifthText, Integer sixthText, Integer seventhText, Integer eightText, Integer ninthText, Integer tenthText, Integer eleventhText, Integer twelfthText, Integer thirteenthText, Integer fourteenthText, Integer fifteenthText, Integer sixteenthText, Integer seventeenthText, Integer eighteenthText, Integer nineteenthText, Integer twentiethText, Integer twentyfirstText) {
        JSONObject json = new JSONObject();
        //JSONObject data = new JSONObject();
        try {
            // json.put("email", direccion_de_email) como ser guarda el device ID
            // json.put("password", password_variable)
            // en lugar de json.put seria data.put para todos los siguientes
            json.put("FIRST", firstText);
            json.put("SECOND", secondText);
            json.put("THIRD", thirdText);
            json.put("FOURTH", fourthText);
            json.put("FIFTH", fifthText);
            json.put("SIXTH", sixthText);
            json.put("SEVENTH", seventhText);
            json.put("EIGHT", eightText);
            json.put("NINTH", ninthText);
            json.put("TENTH", tenthText);
            json.put("ELEVENTH", eleventhText);
            json.put("TWELFTH", twelfthText);
            json.put("THIRTEENTH", thirteenthText);
            json.put("FOURTEENTH", fourteenthText);
            json.put("FIFTEENTH", fifteenthText);
            json.put("SIXTEENTH", sixteenthText);
            json.put("SEVENTEENTH", seventeenthText);
            json.put("EIGHTEENTH", eighteenthText);
            json.put("NINETEENTH", nineteenthText);
            json.put("TWENTIETH", twentiethText);
            json.put("TWENTYFIRST", twentyfirstText);

            json.put("DEVICE_ID", device_id);
            // aqui hacer : json.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(APP_TAG, json.toString());

        ////////// esto para imprimir que va a enviar
        Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();

        Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
        // https://142.244.110.140:8002?action=log
        AndroidNetworking.post("https://mota.technology/StudentDepression/API/LOG/")
                .addJSONObjectBody(json)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            String error  = response.getString("error");
                            if ("false".equalsIgnoreCase(error)) {
                                Toast.makeText(context, "Log sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "Data response not received", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public static void sendFile(Context context, String filepath) {
        File file = new File(filepath);
        AndroidNetworking.upload("https://mota.technology/StudentDepression/API/FILE/")
                // se envia el file
                .addMultipartFile("file", file)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            String error  = response.getString("error");
                            if ("false".equalsIgnoreCase(error)) {
                                Toast.makeText(context, "Sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(context, "Data response not received", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
