package com.yernazar.pidapplication.data.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Route (
        @PrimaryKey
        @SerializedName("id")
        val uid: String,
        @SerializedName("longName")
        val longName: String,
        @SerializedName("shortName")
        val shortName: String,
        @SerializedName("desc")
        val desc: String? = "",
        @SerializedName("agency")
        val agency: String,
        @SerializedName("color")
        val color: String,
        @SerializedName("textColor")
        val textColor: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("night")
        val isNight: Boolean
        )