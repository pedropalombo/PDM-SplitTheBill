package br.edu.scl.ifsp.splitthebill.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Participant::class], version = 2)
abstract class ParticipantDaoRoom: RoomDatabase() {
    companion object Constants {
        const val PARTICIPANT_DATABASE_FILE = "participant_room"
    }
    abstract fun getParticipantDao(): ParticipantDao
}