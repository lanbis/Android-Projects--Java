package com.example.geridonus_yerdefteri.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class ani implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name="isim")
    public String isim;
    @ColumnInfo(name="enlem")
    public Double enlem;
    @ColumnInfo(name="boylam")
    public Double boylam;

    public ani(String isim, Double enlem, Double boylam) {
        this.isim = isim;
        this.enlem = enlem;
        this.boylam = boylam;
    }


}
