package com.example.calendar.ComposerList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.calendar.MyApplication;
import com.example.calendar.R;
import com.example.calendar.ScrollingActivity;
import com.ibm.icu.text.RuleBasedNumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Composer> bornComposers, deadComposers;
    public LocalDate selectedDay;
    public MyApplication application;

    public ListAdapter(List<Composer> bornComposers, List<Composer> deadComposers, MyApplication application) {
        this.bornComposers = bornComposers;
        this.deadComposers = deadComposers;
        this.application = application;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView nameView, yearView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textComposer);
            yearView = itemView.findViewById(R.id.textTime);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_composer, parent, false);
        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder1 = (MyViewHolder)holder;

        Composer composer;
        String status;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(Locale.UK, RuleBasedNumberFormat.ORDINAL);

        if (position < bornComposers.size()) {
            composer = bornComposers.get(position);


        }
        else {
            composer = deadComposers.get(position - bornComposers.size());

        }

        if (composer.getName().endsWith("s"))
            status = "' ";
        else status = "'s ";
        if (composer.isBirthday(selectedDay) != null) {
            status = nf.format(composer.isBirthday(selectedDay), "%ordinal")
                    + "Birthday";
            if (composer.isDeath(selectedDay) != null) {
                status += " and " + nf.format(composer.isBirthday(selectedDay)) + " Death";
            }
        }
        else /* if (composer.isDeath(selectedDay) != null) */ {
            status = nf.format(composer.isBirthday(selectedDay)) + " Death";
        }


        status += " Anniversary";
        holder1.nameView.setText(composer.getName() + status);
        holder1.yearView.setText(composer.getYears());
        holder1.view.setOnClickListener(v -> {
            application.composer = composer;
            v.getContext().startActivity(new Intent(v.getContext(), ScrollingActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return bornComposers.size() + deadComposers.size();
    }
}
