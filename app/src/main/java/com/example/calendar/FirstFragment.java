package com.example.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.calendar.ComposerList.Composer;
import com.example.calendar.ComposerList.ListAdapter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FirstFragment extends Fragment {

    ComposerViewModel viewModel;
    ListAdapter listAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_first, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(ComposerViewModel.class);
        viewModel.context = getContext();

        CalendarView calendarView = view.findViewById(R.id.calendarView);

        listAdapter = new ListAdapter(new LinkedList<>(), new LinkedList<>(), (MyApplication) getActivity().getApplication());

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            onDataChanged(LocalDate.of(year, month, dayOfMonth));
        });

        viewModel.getComposerData().observe(getViewLifecycleOwner(), composers -> {
            onDataChanged(new Date(calendarView.getDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        });

    }

    void onDataChanged(LocalDate date) {

        List<Composer> composers = viewModel.getComposerData().getValue();
        assert composers != null;
        List<Composer> bornComposers = new ArrayList<>();
        List<Composer> deadComposers = new ArrayList<>();
        for (Composer composer : composers) {
            if (composer.getBirth().equals(date)) {
                bornComposers.add(composer);
            }
            else if (composer.getDeath().equals(date)) {
                deadComposers.add(composer);
            }
        }
        listAdapter.deadComposers = deadComposers;
        listAdapter.bornComposers = bornComposers;
        listAdapter.notifyDataSetChanged();
    }
}
