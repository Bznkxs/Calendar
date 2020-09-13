package com.example.calendar.ComposerList;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.calendar.Music.Music;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Composer {
    private final String name;
    private final LocalDate birth;
    private final LocalDate death;
    private final List<Music> musicList;

    public Composer(String name, LocalDate birth, LocalDate death, List<Music> musicList) {
        this.name = name;
        this.birth = birth;
        this.death = death;
        this.musicList = musicList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDate convertToDate(String date) {
        DateTimeFormatter[] dateTimeFormatters = {
                DateTimeFormatter.ofPattern("yy-MM-dd"),
                DateTimeFormatter.ofPattern("dd MMM, yyyy"),
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("M.dd yyyy"),
                DateTimeFormatter.ofPattern("MMM d, yyyy"),
                DateTimeFormatter.ofPattern("d MMM, yyyy")
        };
        for (DateTimeFormatter f : dateTimeFormatters) {
            try {
                return LocalDate.parse(date, f);
            } catch (Exception ignored) {
            }
        }
        return null; // failure
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Composer createComposerFromString(String stringDescription, List<Music> musicList) {
        String[] strings = stringDescription.split("[@-]");
        if (strings.length < 3) {
            return null;
        }
        return new Composer(
            strings[0],
            convertToDate(strings[1]),
            convertToDate(strings[2]),
            musicList
        );
    }

    /**
     *
     * @param date
     * @return null if is not birthday
     * int age
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer isBirthday(LocalDate date) {
        if (birth.withYear(0).equals(date.withYear(0))) {
            return Period.between(birth, date).getYears();
        }
        return null;
    }

    /**
     *
     * @param date
     * @return null if is not death
     * int age
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer isDeath(LocalDate date) {
        if (death.withYear(0).equals(date.withYear(0))) {
            return Period.between(death, date).getYears();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public LocalDate getDeath() {
        return death;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getYears(DateTimeFormatter formatter) {

        return (birth.format(formatter) + " - " + death.format(formatter));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getYears() {
        return getYears(DateTimeFormatter.ofPattern("yy-MM-dd"));
    }

    public List<Music> getMusicList() {
        return musicList;
    }
}
