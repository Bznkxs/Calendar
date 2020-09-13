package com.example.calendar;

import android.content.Context;
import android.os.Build;
import android.renderscript.ScriptGroup;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.calendar.ComposerList.Composer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComposerViewModel extends ViewModel {
    MutableLiveData<List<Composer>> composerData;
    public Context context;

    public  ComposerViewModel() {
        composerData = new MutableLiveData<>();
        composerData.setValue(new ArrayList<>());
    }

    public LiveData<List<Composer>> getComposerData() {
        loadComposers();
        return composerData;
    }

    public void loadComposers() {
        assert context != null;
        Thread thread = new Thread(new Runnable() {
            final String urlText = "https://github.com/Bznkxs/Calendar/blob/master/Data/data.txt";
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
                    try (FileOutputStream fos = new FileOutputStream(localText)) {
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // finally load local file again
                loadFromLocalFile();
                composerData.postValue(composerList);
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
