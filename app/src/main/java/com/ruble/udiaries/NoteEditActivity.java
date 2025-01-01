package com.ruble.udiaries;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ruble.udiaries.api.RetrofitClient;
import com.ruble.udiaries.model.Note;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteEditActivity extends AppCompatActivity {
    private TextInputEditText titleInput;
    private TextInputEditText contentInput;
    private MaterialButton saveButton;
    private String noteId = null;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        saveButton = findViewById(R.id.saveButton);

        noteId = getIntent().getStringExtra("noteId");
        userId = getIntent().getIntExtra("userId", -1);

        if (noteId != null) {
            loadNote();
        }

        saveButton.setOnClickListener(v -> saveNote());
    }

    private void loadNote() {
        RetrofitClient.getInstance()
            .getApi()
            .getNote(noteId)
            .enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Note note = response.body();
                        titleInput.setText(note.getTitle());
                        contentInput.setText(note.getContent());
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    Toast.makeText(NoteEditActivity.this, 
                        "加载笔记失败: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void saveNote() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        if (title.isEmpty()) {
            titleInput.setError("请输入标题");
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUserId(userId);
        note.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", 
            Locale.getDefault()).format(new Date()));

        if (noteId != null) {
            note.setId(noteId);
            updateNote(note);
        } else {
            createNote(note);
        }
    }

    private void createNote(Note note) {
        RetrofitClient.getInstance()
            .getApi()
            .createNote(note)
            .enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    Log.d("NoteEditActivity", "Response code: " + response.code());
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("NoteEditActivity", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(NoteEditActivity.this, 
                            "保存成功", 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NoteEditActivity.this, 
                            "保存失败: " + response.code(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    Toast.makeText(NoteEditActivity.this, 
                        "保存失败: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateNote(Note note) {
        note.setId(noteId);
        note.setUserId(userId);
        note.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", 
            Locale.getDefault()).format(new Date()));

        RetrofitClient.getInstance()
            .getApi()
            .updateNote(noteId, note)
            .enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    Log.d("NoteEditActivity", "Update URL: " + call.request().url());
                    Log.d("NoteEditActivity", "Update Response code: " + response.code());
                    if (!response.isSuccessful()) {
                        try {
                            String errorBody = response.errorBody() != null ? 
                                response.errorBody().string() : "Unknown error";
                            Log.e("NoteEditActivity", "Update Error body: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(NoteEditActivity.this, 
                            "更新成功", 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(NoteEditActivity.this, 
                            "更新失败: " + response.code(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    Log.e("NoteEditActivity", "Update failed for URL: " + 
                        call.request().url(), t);
                    Toast.makeText(NoteEditActivity.this, 
                        "更新失败: " + t.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            });
    }
} 