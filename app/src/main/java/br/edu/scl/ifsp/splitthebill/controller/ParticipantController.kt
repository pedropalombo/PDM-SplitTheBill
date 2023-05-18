package br.edu.scl.ifsp.splitthebill.controller

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import br.edu.scl.ifsp.splitthebill.model.Participant
import br.edu.scl.ifsp.splitthebill.model.ParticipantDao
import br.edu.scl.ifsp.splitthebill.model.ParticipantDaoRoom
import br.edu.scl.ifsp.splitthebill.model.ParticipantDaoRoom.Constants.PARTICIPANT_DATABASE_FILE

class ParticipantController(
    private val activity: AppCompatActivity,
    private val onReceiveReceipts: (list: MutableList<Participant>) -> Unit
) {

    private val participantDaoImpl: ParticipantDao = Room.databaseBuilder(
        activity, ParticipantDaoRoom::class.java, PARTICIPANT_DATABASE_FILE
    ).build().getParticipantDao()


    //inserting participant
    fun insertReceipt(participant: Participant) {
        Thread {
            participantDaoImpl.createParticipant(participant)
        }.start()
    }

    //retrieving single participant via id
    fun fetchReceipt(id: Int) = participantDaoImpl.retrieveParticipant(id)

    //retrieving all participants on db
    fun fetchReceipts() {
        Thread {
            val list = participantDaoImpl.retrieveParticipants()
            activity.runOnUiThread {
                onReceiveReceipts(list)
            }
        }.start()
    }

    //edits given participant
    fun editReceipt(participant: Participant) {
        Thread {
            participantDaoImpl.updateParticipant(participant)
        }.start()
    }

    //removes given participant
    fun removeReceipt(participant: Participant) {
        Thread {
            participantDaoImpl.deleteParticipant(participant)
        }.start()
    }
}