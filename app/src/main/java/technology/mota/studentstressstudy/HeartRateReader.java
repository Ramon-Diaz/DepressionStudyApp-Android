package technology.mota.studentstressstudy;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthData;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthDataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import static technology.mota.studentstressstudy.MainActivity.APP_TAG;
import static technology.mota.studentstressstudy.Time.THREE_DAY;
import static technology.mota.studentstressstudy.Time.THREE_DAY_START_UTC_TIME;

public class HeartRateReader {

    private final HealthDataResolver mResolver;
    private final Context context;


    public HeartRateReader (HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }
    public void readHeartRate() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {

            mResolver.read(request).setResultListener(result -> {
                JsonArrayList<HeartRateBinningData> binningDataList = new JsonArrayList<HeartRateBinningData>();
                JsonArrayList<HeartRateBinningData> binningData = new JsonArrayList<HeartRateBinningData>();
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new HeartRateBinningData(
                                data.getFloat("heart_rate"),
                                data.getFloat("min"),
                                data.getFloat("max"),
                                data.getInt("heart_beat_count"),
                                data.getLong("start_time"),
                                data.getLong("end_time")
                                ));
                        try{
                        byte[] binning = data.getBlob("binning_data");
                        if (binning != null ){
                            List<RawHR> raw= this.getLiveData(binning);
                            for (RawHR d: raw) {
                                binningData.add(new HeartRateBinningData(
                                        d.heart_rate,
                                        d.heart_rate_min,
                                        d.heart_rate_max,
                                        0,
                                        d.start_time,
                                        d.end_time
                                ));
                            }
                        }} catch(Exception e){

                        }
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.heartRateData = binningDataList;
                SendFunctionality.rawHearRate = binningData;
                SendFunctionality.sendInformation(context);
            });

        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }
    }

    public class RawHR {
        long start_time = 0L;
        float heart_rate = 0.f;
        float heart_rate_max = 0.f;
        float heart_rate_min = 0.f;
        long end_time = 0L;
    }
    public List<RawHR> getLiveData(byte[] zip) {
        // decompress ZIP
        List<RawHR> liveDataList = HealthDataUtil.getStructuredDataList(zip, RawHR.class);
        return liveDataList;
    }
    public static class HeartRateBinningData extends JSONObject {
        public float heart_rate = 0f;
        public float heart_rate_min = 0f;
        public float heart_rate_max = 0f;
        public int heart_beat_count = 0;
        public long start_time = 0;
        public long end_time = 0;

        public HeartRateBinningData(float hr, float hr_min, float hr_max, int hbc, long start, long end){
            this.heart_rate = hr;
            this.heart_rate_max = hr_max;
            this.heart_rate_min = hr_min;
            this.start_time = start;
            this.end_time = end;
            this.heart_beat_count = hbc;
            try {
                this.put("hr", hr);
                this.put("hr_min", hr_min);
                this.put("hr_max", hr_max);
                this.put("count", hbc);
                this.put("start", start);
                this.put("end", end);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\nhr: "+heart_rate+",\nhr_min: "+heart_rate_min+",\nhr_max: "+heart_rate_max+",\nhr_count: "+heart_beat_count+",\nstart: "+start_time+",\nend: "+end_time+"\n}";
        }
    }


}
