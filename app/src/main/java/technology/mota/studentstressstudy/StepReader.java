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

public class StepReader {
    private final HealthDataResolver mResolver;
    private final Context context;

    public StepReader (HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }
    public void readStep() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {
            mResolver.read(request).setResultListener(result -> {
                JsonArrayList<StepReader.StepBinningData> binningDataList = new JsonArrayList<StepReader.StepBinningData>();
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new StepReader.StepBinningData(
                                data.getLong("time_offset"),
                                data.getLong("start_time"),
                                data.getLong("end_time"),
                                data.getInt("count")
                        ));
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.stepData = binningDataList;
                SendFunctionality.sendInformation(context);
            });
        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }
    }


    public static class StepBinningData extends JSONObject {
        public long time_offset = 0;
        public long start_time = 0;
        public long end_time = 0;
        public int count = 0;

        public StepBinningData(long offset, long start, long end, int count){
            this.time_offset = offset;
            this.start_time = start;
            this.end_time = end;
            this.count = count;
            try {
                this.put("offset", offset);
                this.put("start", start);
                this.put("end", end);
                this.put("count", count);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\noffset: "+time_offset+",\nstart: "+start_time+",\nend: "+end_time+",\ncount: "+count+"\n}";
        }
    }
}