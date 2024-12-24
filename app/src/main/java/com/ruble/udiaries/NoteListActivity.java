package com.ruble.udiaries;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruble.udiaries.adapter.NoteAdapter;
import com.ruble.udiaries.api.RetrofitClient;
import com.ruble.udiaries.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private FloatingActionButton fabAdd;
    private int userId; // 从登录页面传递过来

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        setupRecyclerView();
        setupFab();
        loadNotes();
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(NoteListActivity.this, NoteEditActivity.class);
                intent.putExtra("noteId", note.getId());
                startActivity(intent);
            }

            @Override
            public void onNoteLongClick(Note note) {
                showDeleteDialog(note);
            }
        });
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, NoteEditActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private void loadNotes() {
        RetrofitClient.getInstance()
            .getApi()
            .getNotes(userId)
            .enqueue(new Callback<List<Note>>() {
                @Override
                public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        adapter.setNotes(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Note>> call, Throwable t) {
                    Toast.makeText(NoteListActivity.this, 
                        "加载笔记失败: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void showDeleteDialog(Note note) {
        new AlertDialog.Builder(this)
            .setTitle("删除笔记")
            .setMessage("确定要删除这条笔记吗？")
            .setPositiveButton("删除", (dialog, which) -> deleteNote(note))
            .setNegativeButton("取消", null)
            .show();
    }

    private void deleteNote(Note note) {
        RetrofitClient.getInstance()
            .getApi()
            .deleteNote(note.getId())
            .enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NoteListActivity.this, 
                            "删除成功", 
                            Toast.LENGTH_SHORT).show();
                        loadNotes(); // 重新加载笔记列表
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(NoteListActivity.this, 
                        "删除失败: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // 每次返回页面时重新加载笔记
    }
} 