package br.edu.scl.ifsp.splitthebill

//used for 'Companion Objects' | maintaining constants | sealed -> protected for abstract classes
// OBS: not a view
sealed class Constants {
    companion object {
        const val EXTRA_PARTICIPANT = "Participant"
        const val EXTRA_VIEW_PARTICIPANT = "ViewParticipant"
    }
}