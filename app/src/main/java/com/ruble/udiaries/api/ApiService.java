package com.ruble.udiaries.api;

import com.ruble.udiaries.model.Note;
import com.ruble.udiaries.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("users")
    Call<List<User>> login(
        @Query("username") String username,
        @Query("password") String password
    );

    @POST("users")
    Call<User> register(@Body User user);

    @GET("notes")
    Call<List<Note>> getNotes(@Query("userId") int userId);

    @GET("notes/{id}")
    Call<Note> getNote(@Path("id") int id);

    @POST("notes")
    Call<Note> createNote(@Body Note note);

    @DELETE("notes/{id}")
    Call<Void> deleteNote(@Path("id") int id);
} 