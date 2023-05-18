package br.edu.scl.ifsp.splitthebill.model

import androidx.room.*

@Dao
interface ParticipantDao {
    @Insert
    fun createParticipant(participant: Participant)
    @Query("SELECT * FROM Participant WHERE id = :id")
    fun retrieveParticipant(id: Int): Participant?
    @Query("SELECT * FROM Participant")
    fun retrieveParticipants(): MutableList<Participant>
    @Update
    fun updateParticipant(participant: Participant): Int
    @Delete
    fun deleteParticipant(participant: Participant): Int
}