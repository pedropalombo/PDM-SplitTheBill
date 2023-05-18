package br.edu.scl.ifsp.splitthebill.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.splitthebill.Constants
import br.edu.scl.ifsp.splitthebill.databinding.ActivityParticipantBinding
import br.edu.scl.ifsp.splitthebill.model.Participant
import kotlin.random.Random


class ParticipantActivity : AppCompatActivity() {

    //inflating Participants' views
    private val arb : ActivityParticipantBinding by lazy {
        ActivityParticipantBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(arb.root)

        supportActionBar?.subtitle = "Informações do Participante"   //changing header text

        //checks current SDK...
        val receivedParticipant = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra( Constants.EXTRA_PARTICIPANT, Participant::class.java)
        } else {
            intent.getParcelableExtra( Constants.EXTRA_PARTICIPANT)
        }

        //... and if a participant was sent over...
        receivedParticipant?.let { _receivedParticipant ->
            with(arb) {
                with(_receivedParticipant) {
                    //so it's info can be added to the Input Fields (EditText)
                    nameEt.setText(name)
                    priceEt.setText(itemPrice.toString())
                    itemEt.setText(itemPurchased)
                }
            }

            val viewParticipant = intent.getBooleanExtra(Constants.EXTRA_VIEW_PARTICIPANT, false)

            //changing state if participant's info exists\
            with(arb) {
                nameEt.isEnabled = !viewParticipant
                priceEt.isEnabled = !viewParticipant
                itemEt.isEnabled = !viewParticipant

                saveBt.visibility = if(viewParticipant) View.GONE else View.VISIBLE
            }
        }

        //when 'Save' button is clicked
        arb.saveBt.setOnClickListener {

            //creating new participant based on form submission
            val id = receivedParticipant?.let {  receivedParticipant.id  } ?: Random.nextInt()
            val participant = Participant(
                id = id,
                name = arb.nameEt.text.toString(),
                itemPurchased = arb.itemEt.text.toString(),
                itemPrice = arb.priceEt.text.toString().toFloat()
            )

            val resultIntent = Intent()    //creating intent to send participant info over to main

            //putting participant info in a bundle
            resultIntent.putExtra(
                Constants.EXTRA_PARTICIPANT, participant
            )

            setResult(RESULT_OK, resultIntent)  //setting result code as 'OK'
            finish()    //closing view when finished
        }
    }

}
