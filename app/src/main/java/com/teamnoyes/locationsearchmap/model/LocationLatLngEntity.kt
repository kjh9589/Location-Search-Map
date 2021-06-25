package com.teamnoyes.locationsearchmap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationLatLngEntity(
    val latitude: Float,
    val longitude: Float
):Parcelable
