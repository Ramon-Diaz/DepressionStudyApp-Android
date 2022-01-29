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
import java.util.List;

import static technology.mota.studentstressstudy.MainActivity.APP_TAG;
import static technology.mota.studentstressstudy.Time.THREE_DAY;
import static technology.mota.studentstressstudy.Time.THREE_DAY_START_UTC_TIME;

public class SleepStageReader {


    private final HealthDataResolver mResolver;
    private final Context context;

    public SleepStageReader (HealthDataStore store, Context context) {
        this.mResolver = new HealthDataResolver(store, null);
        this.context = context;
    }
    public void readSleepStage() {
        HealthDataResolver.ReadRequest request = new HealthDataResolver.ReadRequest.Builder()
                .setDataType(HealthConstants.SleepStage.HEALTH_DATA_TYPE)
                .setLocalTimeRange(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.TIME_OFFSET,
                        THREE_DAY_START_UTC_TIME, THREE_DAY_START_UTC_TIME + THREE_DAY)
                .build();

        try {
            mResolver.read(request).setResultListener(result -> {
                JsonArrayList<SleepBinningData> binningDataList = new JsonArrayList<SleepBinningData>();
                try {
                    Iterator<HealthData> iterator = result.iterator();
                    while (iterator.hasNext()) {
                        HealthData data = iterator.next();
                        binningDataList.add(new SleepBinningData(
                                data.getInt("stage"),
                                data.getLong("start_time"),
                                data.getLong("end_time")
                        ));
                    }
                } finally {
                    result.close();
                }
                SendFunctionality.sleepStageData = binningDataList;
                SendFunctionality.sendInformation(context);
            });
        } catch (Exception e) {
            Log.e(APP_TAG, "Getting heart rate fails.", e);
        }
    }


    public static class SleepBinningData extends JSONObject {
        public String stage = "";
        public long start_time = 0;
        public long end_time = 0;

        public SleepBinningData(int stage, long start, long end){
            switch (stage) {
                case HealthConstants.SleepStage.STAGE_AWAKE:
                    this.stage = "AWAKE";
                    break;
                case HealthConstants.SleepStage.STAGE_LIGHT:
                    this.stage = "LIGHT";
                    break;
                case HealthConstants.SleepStage.STAGE_DEEP:
                    this.stage = "DEEP";
                    break;
                case HealthConstants.SleepStage.STAGE_REM:
                    this.stage = "REM";
                    break;
            }
            this.start_time = start;
            this.end_time = end;
            try {
                this.put("stage", this.stage);
                this.put("start", start);
                this.put("end", end);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "{\nstage: "+stage+",\nstart: "+start_time+",\nend: "+end_time+"\n}";
        }
    }
}