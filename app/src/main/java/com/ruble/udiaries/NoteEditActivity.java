package com.ruble.udiaries;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ruble.udiaries.api.RetrofitClient;
import com.ruble.udiaries.model.Note;

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
    private int noteId = -1;
    private int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        saveButton = findViewById(R.id.saveButton);

        noteId = getIntent().getIntExtra("noteId", -1);
        userId = getIntent().getIntExtra("userId", -1);

        if (noteId != -1) {
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

        RetrofitClient.getInstance()
            .getApi()
            .createNote(note)
            .enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(NoteEditActivity.this, 
                            "保存成功", 
                            Toast.LENGTH_SHORT).show();
                        finish();
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
} 