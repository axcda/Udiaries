package com.ruble.udiaries;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.ruble.udiaries.api.RetrofitClient;
import com.ruble.udiaries.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        // 登录按钮点击事件
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (validateInput(username, password)) {
                performLogin(username, password);
            }
        });

        // 注册链接点击事件
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            usernameInput.setError("请输入用户名");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("请输入密码");
            return false;
        }
        return true;
    }

    private void performLogin(String username, String password) {
        RetrofitClient.getInstance()
            .getApi()
            .login(username, password)
            .enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        User user = response.body().get(0);
                        Log.d("LoginActivity", "User ID: " + user.getId());
                        Log.d("LoginActivity", "Username: " + user.getUsername());
                        
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        
                        Intent intent = new Intent(LoginActivity.this, NoteListActivity.class);
                        intent.putExtra("userId", user.getId());
                        startActivity(intent);
                        finish();
                    } else {
                        if (response.body() == null) {
                            Log.e("LoginActivity", "Response body is null");
                        } else if (response.body().isEmpty()) {
                            Log.e("LoginActivity", "Response body is empty");
                        }
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
} 