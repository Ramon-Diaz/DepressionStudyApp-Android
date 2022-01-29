package technology.mota.studentstressstudy;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;


public class NotificationFragment extends Fragment {

    TextView notifications;
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        notifications =  v.findViewById(R.id.notifications);
        getNotifications();
        return v;
    }

    public void getNotifications() {
        //Toast.makeText(getContext(), "Recibiendo notificaciones", Toast.LENGTH_SHORT).show();
        AndroidNetworking.post("https://mota.technology/StudentDepression/API/NOTIFICATIONS/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            notifications.setMovementMethod(LinkMovementMethod.getInstance());
                            notifications.setText(Html.fromHtml(response.getString("notifications")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getContext(), "Notificaciones recibidas", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(ANError error) {
                        //Toast.makeText(getContext(), "Algo salió mal al intentar obtener las notificaciones", Toast.LENGTH_LONG).show();
                    }
                });
    }



}
