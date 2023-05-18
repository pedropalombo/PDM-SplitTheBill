package br.edu.scl.ifsp.splitthebill.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import br.edu.scl.ifsp.splitthebill.Constants
import br.edu.scl.ifsp.splitthebill.R
import br.edu.scl.ifsp.splitthebill.adapter.ParticipantAdapter
import br.edu.scl.ifsp.splitthebill.controller.ParticipantController
import br.edu.scl.ifsp.splitthebill.databinding.ActivityMainBinding
import br.edu.scl.ifsp.splitthebill.model.Participant

class MainActivity : AppCompatActivity() {
    //populating list
    private val participantList: MutableList<Participant> = mutableListOf()

    // -| adapter |-
    //showing participants
    private val participantAdapter: ParticipantAdapter by lazy {
        ParticipantAdapter(this, participantList)
    }

    // -| controller |-
    private val participantController: ParticipantController by lazy {
        ParticipantController(this, this::updateReceiptList)
    }

    private lateinit var parl: ActivityResultLauncher<Intent>

    // -| Inflaters |-
    // main app
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // menu activity
    // |-> adding the top buttons to Main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    // contextMenu
    // |-> onHold action for views
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }
    // -| / |-

    //on app launch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        participantController.fetchReceipts()   //getting participants from db
        amb.participantLv.adapter = participantAdapter //setting adapter

        //populating page
        parl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            this::onReceiveParticipant
        )

        registerForContextMenu(amb.participantLv)   //giving ContextMenu for views

        //showing participant info on click
        amb.participantLv.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val participant = participantList[position]
                onSelectParticipant(participant, true)
            }
    }

    //Activity Result Launcher logic
    private fun onReceiveParticipant(result: ActivityResult) {
        //Log.i("Result: ", result.data.toString())
        if (result.resultCode == RESULT_OK) {
            val participant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(Constants.EXTRA_PARTICIPANT, Participant::class.java)
            } else {
                result.data?.getParcelableExtra(Constants.EXTRA_PARTICIPANT)
            }

            //check if participant exists
            participant?.let { _participant ->
                val position = participantList.indexOfFirst { it.id == _participant.id }    //see if the ID already exists

                //if it already exists...
                if (position != -1) {
                    participantList[position] = _participant    //... adds participant where it was found on the list
                    participantController.editReceipt(_participant) //... edits the new info to the existing participant instance on db

                    Toast.makeText(this, "Participante editado!", Toast.LENGTH_SHORT).show()

                //... otherwise ...
                } else {
                    participantList.add(_participant)   //... adds to local list
                    participantController.insertReceipt(_participant)   //... and also to db

                    Toast.makeText(this, "Participante adicionado!", Toast.LENGTH_SHORT).show()
                }

                //Log.i("Participantes: ", participantList.toString())
                participantAdapter.notifyDataSetChanged()
            }
        }
    }

    //listener for when an item is selected on 'Menu' (header icons)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            //if "add participant" icon is clicked...
            R.id.addMi -> {
                val participantIntent = Intent(this, ParticipantActivity::class.java)
                parl.launch(participantIntent)

                true
            }

            //'' "calculator" icon is clicked
            R.id.calculatorMi -> {

                val paymentIntent = Intent(this, PaymentActivity::class.java)
                paymentIntent.putParcelableArrayListExtra(Constants.EXTRA_PARTICIPANT, ArrayList(participantList))

                parl.launch(paymentIntent)

                true
            }

            else -> false
        }
    }

    //updates views showed on Main page
    private fun updateReceiptList(_participantList: MutableList<Participant>) {
        participantList.clear()
        participantList.addAll(_participantList)
        participantAdapter.notifyDataSetChanged()
    }

    //when view is clicked
    private fun onSelectParticipant(participant: Participant, isViewMode: Boolean = false) {
        val participantIntent =
            Intent(this, ParticipantActivity::class.java)   //intent to send participant

        //creating bundle with the participant's info
        participantIntent.putExtra(
            Constants.EXTRA_PARTICIPANT,
            participant
        )
        participantIntent.putExtra(Constants.EXTRA_VIEW_PARTICIPANT, isViewMode)  //setting Activity as view-only

        parl.launch(participantIntent)
    }

    //when an item on ContextMenu (hold action) is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {

        //getting participant's position through ContextMenu's adapter (casting)
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        //val participant = participantList[position]
        return when (item.itemId) {

            //removing cell (participant)
            R.id.removeParticipantMi -> {
                participantController.removeReceipt(participantList[position])  //removing from db
                participantList.removeAt(position)  //removing from view
                participantAdapter.notifyDataSetChanged()

                Toast.makeText(this, "Participante removido!", Toast.LENGTH_SHORT).show()

                true
            }

            //editing participant's info
            R.id.editParticipantMi -> {
                val participant = participantList[position]     //creating participant to be sent
                onSelectParticipant(participant)
                true
            }

            else -> false
        }
    }

}