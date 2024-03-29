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

public class TemperatureReader {
    private final HealthDataResolver mResolver;
    private final Context context;

    public TemperatureReader(HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }

    public void readTemperature() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.BodyTemperature.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {
            mResolver.read(request).setResultListener(result -> {
                JsonArrayList<TemperatureReader.TemperatureBinningData> binningDataList = new JsonArrayList<TemperatureReader.TemperatureBinningData>();
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new TemperatureReader.TemperatureBinningData(
                                data.getLong("time_offset"),
                                data.getLong("start_time"),
                                data.getLong("end_time"),
                                data.getFloat("temperature")
                        ));
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.temperatureData = binningDataList;
                SendFunctionality.sendInformation(context);
            });
        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }
    }


    public static class TemperatureBinningData extends JSONObject {
        public long time_offset = 0;
        public long start_time = 0;
        public long end_time = 0;
        public float temperature = 0;

        public TemperatureBinningData(long offset, long start, long end, float temperature) {
            this.time_offset = offset;
            this.start_time = start;
            this.end_time = end;
            this.temperature = temperature;
            try {
                this.put("offset", offset);
                this.put("start", start);
                this.put("end", end);
                this.put("temperature", temperature);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\noffset: " + time_offset + ",\nstart: " + start_time + ",\nend: " + end_time + ",\ntemperature: " + temperature + "\n}";
        }
    }
}
