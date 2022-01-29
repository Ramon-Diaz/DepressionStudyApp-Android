package technology.mota.studentstressstudy;

import android.content.Context;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static technology.mota.studentstressstudy.MainActivity.APP_TAG;
import static technology.mota.studentstressstudy.Time.THREE_DAY;
import static technology.mota.studentstressstudy.Time.THREE_DAY_START_UTC_TIME;

public class BloodPressureReader {
    private final HealthDataResolver mResolver;
    private final Context context;

    public BloodPressureReader(HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }

    public void readPressure() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.BloodPressure.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {
            mResolver.read(request).setResultListener(result -> {
                JsonArrayList<BloodPressureReader.BloodPressureBinningData> binningDataList = new JsonArrayList<BloodPressureReader.BloodPressureBinningData>();
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new BloodPressureReader.BloodPressureBinningData(
                                data.getLong("time_offset"),
                                data.getLong("start_time"),
                                data.getLong("end_time"),
                                data.getFloat("diastolic"),
                                data.getFloat("systolic"),
                                data.getFloat("mean"),
                                data.getInt("pulse")
                        ));
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.bloodPressureData = binningDataList;
                SendFunctionality.sendInformation(context);
            });
        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }
    }


    public static class BloodPressureBinningData extends JSONObject {
        public long time_offset = 0;
        public long start_time = 0;
        public long end_time = 0;
        public float diastolic = 0;
        public float systolic = 0;
        public float mean = 0;
        public int pulse = 0;

        public BloodPressureBinningData(long offset, long start, long end,float diastolic,float systolic, float mean, int pulse) {
            this.time_offset = offset;
            this.start_time = start;
            this.end_time = end;
            this.diastolic = diastolic;
            this.systolic = systolic;
            this.mean = mean;
            this.pulse = pulse;

            try {
                this.put("offset", offset);
                this.put("start", start);
                this.put("end", end);
                this.put("diastolic", diastolic);
                this.put("systolic", systolic);
                this.put("mean", mean);
                this.put("pulse", pulse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\noffset: " + time_offset + ",\nstart: " + start_time + ",\nend: " + end_time + ",\ndiastolic: " + diastolic + ",\nsystolic: " + systolic + ",\nmean: " + mean + ",\npulse: " + pulse + "\n}";
        }
    }
}
