package com.teamnoyes.locationsearchmap.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultEntity(
    val fullAddress: String,
    val buildingName: String,
    val locationLatLng: LocationLatLngEntity
):Parcelable
