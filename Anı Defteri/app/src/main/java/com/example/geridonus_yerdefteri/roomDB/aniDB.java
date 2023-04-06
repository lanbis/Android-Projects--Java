package com.example.geridonus_yerdefteri.roomDB;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.geridonus_yerdefteri.model.ani;
    @Database(entities = {ani.class}, version = 1)
    public abstract class aniDB extends RoomDatabase {
        public abstract aniDAO aniDAO();
    }
