package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * HabitAdapter:
 * -------------
 * This adapter acts as a bridge between the Habit data (List<Habit>)
 * and the RecyclerView UI. It handles displaying each habit item,
 * updating its completion status, and navigating to its detail view.
 */
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context context;
    private List<Habit> habitList;
    private HabitDatabaseHelper dbHelper;

    /**
     * Constructor to initialize adapter with context and habit list
     */
    public HabitAdapter(Context context, List<Habit> habitList) {
        this.context = context;
        this.habitList = habitList;
        dbHelper = new HabitDatabaseHelper(context);
    }

    /**
     * Called when RecyclerView needs a new ViewHolder for a habit item.
     * Inflates the layout for a single habit row (item_habit.xml).
     */
    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    /**
     * Binds data from a Habit object to the corresponding views in the item layout.
     * Also sets up listeners for checkbox changes and item clicks.
     */
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        // Get current habit from the list
        Habit habit = habitList.get(position);

        // Bind habit name and frequency to TextViews
        holder.txtHabitName.setText(habit.getName());
        holder.txtHabitFrequency.setText(habit.getFrequency());

        // Set checkbox state based on whether habit is completed
        holder.chkCompleted.setChecked(habit.isCompleted());

        // Listener for checkbox toggle (mark as completed or not)
        holder.chkCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            habit.setCompleted(isChecked);
            // Update completion status in the database
            dbHelper.updateHabit(habit);
        });

        // Listener for clicking the whole item → opens HabitDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HabitDetailActivity.class);
            intent.putExtra("habit_id", habit.getId());  // Pass selected habit's ID
            context.startActivity(intent);
        });
    }

    /**
     * Returns the total number of habit items to display.
     */
    @Override
    public int getItemCount() {
        return habitList.size();
    }

    /**
     * ViewHolder class:
     * Holds references to UI components of each habit item to avoid repeated findViewById calls.
     */
    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView txtHabitName, txtHabitFrequency;
        CheckBox chkCompleted;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHabitName = itemView.findViewById(R.id.txtHabitName);
            txtHabitFrequency = itemView.findViewById(R.id.txtHabitFrequency);
            chkCompleted = itemView.findViewById(R.id.chkCompleted);
        }
    }
}
