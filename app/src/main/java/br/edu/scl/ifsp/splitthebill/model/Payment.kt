package br.edu.scl.ifsp.splitthebill.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
data class Payment (
    var name: String,
    var totalCost: Float
) : Parcelable
