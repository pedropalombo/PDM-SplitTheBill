package br.edu.scl.ifsp.splitthebill.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.scl.ifsp.splitthebill.Constants
import br.edu.scl.ifsp.splitthebill.R
import br.edu.scl.ifsp.splitthebill.adapter.PaymentAdapter
import br.edu.scl.ifsp.splitthebill.databinding.ActivityPaymentBinding
import br.edu.scl.ifsp.splitthebill.databinding.TileReceiptFooterBinding
import br.edu.scl.ifsp.splitthebill.model.Participant
import br.edu.scl.ifsp.splitthebill.model.Payment

class PaymentActivity : AppCompatActivity() {

    //total spent by participants
    private var sumTotal: Float = 0f

    //inflater for view's elements
    private val apb: ActivityPaymentBinding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    //footer with total cost + price to be paid by each participant
    private val footerBinding: TileReceiptFooterBinding by lazy {
        TileReceiptFooterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        //checks current SDK...
        val receivedParticipant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(Constants.EXTRA_PARTICIPANT, Participant::class.java)
        } else {
            intent.getParcelableArrayListExtra(Constants.EXTRA_PARTICIPANT)
        }

        //Log.i("Received Participant: ", receivedParticipant.toString())

        val paymentList = getPaymentList(receivedParticipant ?: mutableListOf())

        apb.listaParticipantesLv.adapter = PaymentAdapter(this, paymentList, sumTotal)  //populating listview on payment page

        apb.listaParticipantesLv.addFooterView(footerBinding.root)

        //populating footer section
        with(footerBinding) {
            valorTotalTv.text = getString(R.string.total_text, String.format("%.2f", sumTotal))
            porPessoaTv.text =
                getString(R.string.per_person_text, String.format("%.2f", (sumTotal / paymentList.size)))
        }

    }

    //gets non-duplicated participant list for calculation purposes
    private fun getPaymentList(participantList: MutableList<Participant>): MutableList<Payment> {
       val filteredList = mutableListOf<Payment>()  //filtered list for non-duplicates

        //parsing through participants
        participantList.forEach { _participant ->

            //saving position if there's already a participant with the same name on filteredList
            val position = filteredList.indexOfFirst {
                it.name == _participant.name
            }

            sumTotal += _participant.itemPrice  //adding the participant's money used on the total expenses

            //checks if participant actually existed on filteredList...
            if (position != -1) {
                filteredList[position].totalCost += _participant.itemPrice  //... and add the amount from the participant to filteredList's instance of the participant with the same name

            //... otherwise ...
            } else {
                //... adds the current participant to the filtered list
                filteredList.add(Payment(
                    name = _participant.name,
                    totalCost = _participant.itemPrice
                ))
            }
        }

        //returns the filtered list to be used on payment page
        return filteredList
    }

}