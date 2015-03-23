package me.jordancarlson.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String BUTTON = "button";
    private static final String BODY = "body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "cb418599d67f172b250a72119e8a601d";
        double latitude = 37.8267;
        double longitude = -122.423;
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            alertUserAboutNetworkError();
        }
        Log.d(TAG, "Main UI ");

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getString(R.string.error_title));
        bundle.putString(BODY, getString(R.string.error_body));
        bundle.putString(BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }
    private void alertUserAboutNetworkError() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getString(R.string.error_title));
        bundle.putString(BODY, getString(R.string.network_unavailable));
        bundle.putString(BUTTON, getString(R.string.ok));

        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
