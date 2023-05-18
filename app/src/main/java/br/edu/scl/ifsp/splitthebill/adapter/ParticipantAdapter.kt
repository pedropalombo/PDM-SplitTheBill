package br.edu.scl.ifsp.splitthebill.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.splitthebill.R
import br.edu.scl.ifsp.splitthebill.databinding.TileParticipantBinding
import br.edu.scl.ifsp.splitthebill.model.Participant

//adapter for cell inflation made by hand
class ParticipantAdapter(
    context: Context,
    private val participantList: MutableList<Participant>
) : ArrayAdapter<Participant>(context, R.layout.tile_participant, participantList) {

    private lateinit var tileParticipantBinding: TileParticipantBinding //inflater

    //shows cells on screen
    // \-> position: where on array it is | convertView: gets inflated cells and recycles them | parent: main view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val participant: Participant =
            participantList[position]   //getting participant based on position

        var tileParticipantView = convertView

        //checks if cell is null / can't be recycled
        if (tileParticipantView == null) {

            //inflates new cell
            tileParticipantBinding = TileParticipantBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )

            tileParticipantView = tileParticipantBinding.root   //and add it to the View

            //creates holder and save the reference for the TextViews | better for performance
            val tileParticipantViewHolder = TileParticipantViewHolder(
                tileParticipantBinding.nameTv,
                tileParticipantBinding.priceTv
            )

            //saving ViewHolder to cell
            //tileParticipantView.tag = tileParticipantViewHolder
            tileParticipantView.tag = tileParticipantViewHolder
        }

        //adding 'participant' info to cell..
        //.. through tags and ViewHolders
        with(tileParticipantView.tag as TileParticipantViewHolder) {
            nameTv.text = participant.name
            priceTv.text = ("R$${String.format("%.2f", participant.itemPrice)}")
        }

        return tileParticipantView
    }

    private data class TileParticipantViewHolder(
        var nameTv: TextView,
        var priceTv: TextView
    )

}