package com.example.proyekakhirlabpemrogramanmobile

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "outfits")
data class Outfit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary Key
    val name: String,                                 // Nama Outfit
    val description: String,                         // Deskripsi Outfit
    val imageResId: Int                              // ID resource untuk gambar
)