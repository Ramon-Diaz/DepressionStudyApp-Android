package technology.mota.mymooddiarymexico;

import android.content.Context;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static technology.mota.mymooddiarymexico.MainActivity.APP_TAG;
import static technology.mota.mymooddiarymexico.Time.THREE_DAY;
import static technology.mota.mymooddiarymexico.Time.THREE_DAY_START_UTC_TIME;


public class ExerciseReader {

    private final HealthDataResolver mResolver;
    private final Context context;


    public ExerciseReader(HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }
    public String readExcercise() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.Exercise.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {
            JsonArrayList<ExcerciseBinningData> binningDataList = new JsonArrayList<ExcerciseBinningData>();
            mResolver.read(request).setResultListener(result -> {
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new ExcerciseBinningData(
                                data.getFloat("mean_heart_rate"),
                                data.getFloat("min_heart_rate"),
                                data.getFloat("max_heart_rate"),
                                data.getFloat("mean_speed"),
                                data.getFloat("distance"),
                                data.getFloat("calorie"),
                                data.getInt("duration"),
                                data.getLong("start_time"),
                                data.getLong("end_time")
                        ));
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.exerciseData = binningDataList;
                SendFunctionality.sendInformation(context);

            });
            return binningDataList.toString();
        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }

        return "";
    }


    public static class ExcerciseBinningData extends JSONObject {
        public float heart_rate_mean = 0f;
        public float heart_rate_min = 0f;
        public float heart_rate_max = 0f;
        public float speed = 0f;
        public float distance = 0f;
        public float calorie = 0f;
        public int duration = 0;
        public long start_time = 0;
        public long end_time = 0;

        public ExcerciseBinningData(float hr, float hr_min, float hr_max, float speed, float distance, float calorie, int duration, long start, long end){
            this.heart_rate_mean = hr;
            this.heart_rate_max = hr_max;
            this.heart_rate_min = hr_min;
            this.start_time = start;
            this.end_time = end;
            this.duration = duration;
            this.speed = speed;
            this.distance = distance;
            this.calorie = calorie;
            try {
                this.put("hr", hr);
                this.put("hr_min", hr_min);
                this.put("hr_max", hr_max);
                this.put("duration", duration);
                this.put("speed", speed);
                this.put("distance", distance);
                this.put("calorie", calorie);
                this.put("start", start);
                this.put("end", end);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\nhr: "+heart_rate_mean+",\nhr_min: "+heart_rate_min+",\nhr_max: "+heart_rate_max+",\nduration: "+duration+",\nspeed: "+speed+",\ndistance: "+distance+",\ncalorie: "+calorie+",\nstart: "+start_time+",\nend: "+end_time+"\n}";
        }
    }


}