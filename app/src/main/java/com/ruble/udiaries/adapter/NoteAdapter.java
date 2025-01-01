package com.ruble.udiaries.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruble.udiaries.R;
import com.ruble.udiaries.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes = new ArrayList<>();
    private List<Note> filteredNotes = new ArrayList<>();
    private OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
        void onNoteLongClick(Note note);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        this.filteredNotes = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredNotes.clear();
        if (query.isEmpty()) {
            filteredNotes.addAll(notes);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Note note : notes) {
                if (note.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    note.getContent().toLowerCase().contains(lowerCaseQuery)) {
                    filteredNotes.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = filteredNotes.get(position);
        holder.titleText.setText(note.getTitle());
        holder.contentPreview.setText(note.getContent());
        holder.dateText.setText(note.getCreatedAt());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoteClick(note);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onNoteLongClick(note);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return filteredNotes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView contentPreview;
        TextView dateText;

        NoteViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            contentPreview = itemView.findViewById(R.id.contentPreview);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
} 