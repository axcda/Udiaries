package com.ruble.udiaries;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ruble.udiaries.api.RetrofitClient;
import com.ruble.udiaries.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private MaterialButton registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化视图
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);

        // 注册按钮点击事件
        registerButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (validateInput(username, password, confirmPassword)) {
                performRegister(username, password);
            }
        });

        // 登录链接点击事件
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInput(String username, String password, String confirmPassword) {
        if (username.isEmpty()) {
            usernameInput.setError("请输入用户名");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("请输入密码");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("请确认密码");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("两次输入的密码不一致");
            return false;
        }
        return true;
    }

    private void performRegister(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .format(new Date()));

        RetrofitClient.getInstance()
            .getApi()
            .register(user)
            .enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish(); // 返回登录页
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
} 