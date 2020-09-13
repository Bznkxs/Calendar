package com.example.calendar;

import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.calendar.ComposerList.Composer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

public class ComposerViewModel extends ViewModel {
    MutableLiveData<List<Composer>> composerData;
    public Context context;
    boolean loading = false;

    public  ComposerViewModel() {
        composerData = new MutableLiveData<>();
        composerData.setValue(new ArrayList<>());
    }

    public LiveData<List<Composer>> getComposerData() {
        if (!loading) {
            loadComposers();
        }
        return composerData;
    }

    public void loadComposers() {
        assert context != null;
        loading = true;
        Thread thread = new Thread(new Runnable() {
            final String urlText = "https://raw.githubusercontent.com/Bznkxs/Calendar/master/Data/data.txt";
            final String localText = "data.txt";
            final List<Composer> composerList = new ArrayList<>();
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                // strictly follow online file
                // store in local file

                // first load local file
                loadFromLocalFile();
                composerData.postValue(composerList);

                // then refresh local file
                try (ReadableByteChannel rbc = Channels.newChannel(new URL(urlText).openStream())) {
                    try (FileOutputStream fos = context.openFileOutput(localText,Context.MODE_PRIVATE)) {
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "failed IO", Toast.LENGTH_SHORT).show();
                }

                HttpURLConnection conn;
                try {
                    conn = (HttpURLConnection)new URL(urlText).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(60000);
                    conn.setReadTimeout(30000);
                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    int ch;
                    while ((ch = rd.read()) != -1) {
                        sb.append((char) ch);
                    }
                    String res = sb.toString();
                    try (FileOutputStream fos = context.openFileOutput(localText,Context.MODE_PRIVATE)) {
                        fos.write(res.getBytes());
                    }
                    conn.disconnect();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // finally load local file again
                loadFromLocalFile();
                composerData.postValue(composerList);
                loading = false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            private void loadFromLocalFile() {
                try (FileInputStream in = context.openFileInput(localText)){
                    String tot = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
                    String[] arr = tot.split("\n");
                    for (String i : arr) {
                        Composer composer = Composer.createComposerFromString(i, null);
                        if (composer != null)
                            composerList.add(composer);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}

