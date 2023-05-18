package br.edu.scl.ifsp.splitthebill.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity
data class Participant (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @NotNull var name: String,
    @NotNull var itemPurchased: String,
    @NotNull var itemPrice: Float
) : Parcelable
