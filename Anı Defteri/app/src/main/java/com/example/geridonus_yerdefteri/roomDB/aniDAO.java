package com.example.geridonus_yerdefteri.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.geridonus_yerdefteri.model.ani;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface aniDAO {

    @Query("SELECT * FROM ani")
    Flowable<List<ani>> hepsiniGetir();

    @Insert
    Completable insert(ani ani);

    @Delete
    Completable delete(ani ani);
}
