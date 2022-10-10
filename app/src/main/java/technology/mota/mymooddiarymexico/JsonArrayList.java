package technology.mota.mymooddiarymexico;

import org.json.JSONArray;

import java.util.ArrayList;

public class JsonArrayList<T> {
    private ArrayList<T> arr;
    private JSONArray arrJson;
    public JsonArrayList() {
        arr = new ArrayList<>();
        arrJson = new JSONArray();
    }
    public void add(T value) {
        arr.add(value);
        arrJson.put(value);
    }
    public JSONArray toJson() {
        return arrJson;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int x = 0; x < arr.size(); x++) {

            if (x > 0) {
                sb.append(",\n");
            }
            sb.append(arr.get(x).toString());
        }
        sb.append("\n]");

        return sb.toString();
    }
}
