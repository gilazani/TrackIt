package utils;

import android.content.Context;
import android.os.AsyncTask;

import com.example.trackit.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import callbacks.NotifySearchResultLocked;
import callbacks.OnSearchResultReturn;
import enums.MediaType;
import model.Image;
import model.Media;

public class MediaDataCollector {

    private Context context;

    private static MediaDataCollector mediaDataCollector = null;

    private OnSearchResultReturn onSearchResultReturn;
    private NotifySearchResultLocked notifySearchResultLocked;

    private MediaDataCollector(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (mediaDataCollector == null) {
            mediaDataCollector = new MediaDataCollector(context);
        }
    }

    public static MediaDataCollector getInstance() {
        return mediaDataCollector;
    }

    public void getSearchResult(String search) {
        search = search.replaceAll(" ", "%20");// replace space with %20

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://online-movie-database.p.rapidapi.com/auto-complete?q=" + search)
                .get()
                .addHeader("X-RapidAPI-Key", BuildConfig.XRapidAPIKey)
                .addHeader("X-RapidAPI-Host", "online-movie-database.p.rapidapi.com")
                .build();


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = client.newCall(request).execute();
                    String jsonSearchResult = response.body().string();
                    JSONObject json = new JSONObject(jsonSearchResult); // Your json string here
                    String arrResult = json.optString("d");
                    Type type = new TypeToken<Media[]>() {
                    }.getType();
                    Media[] media = new Gson().fromJson(arrResult, type);

                    onSearchResultReturn.updateSearchResult(media);
                } catch (Exception e) {
                }

//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
            }
        });
        t.start();
        try {
            t.join();
            notifySearchResultLocked.updateSearchAdapter();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void setCallback(OnSearchResultReturn onSearchResultReturn, NotifySearchResultLocked notifySearchResultLocked) {
        this.notifySearchResultLocked = notifySearchResultLocked;
        this.onSearchResultReturn = onSearchResultReturn;
    }
}
