package br.edu.scl.ifsp.splitthebill.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.splitthebill.R
import br.edu.scl.ifsp.splitthebill.databinding.TileReceiptBinding
import br.edu.scl.ifsp.splitthebill.model.Payment

class PaymentAdapter(
    context: Context,
    private val paymentList: MutableList<Payment>,
    private val totalCost : Float,
) : ArrayAdapter<Payment>(context, R.layout.tile_receipt, paymentList) {

    private lateinit var tileReceiptBinding: TileReceiptBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val payment = paymentList[position]   //getting participant based on position

        var tileReceiptView = convertView

        //checks if cell is null / can't be recycled
        if (tileReceiptView == null) {

            //inflates new cell
            tileReceiptBinding = TileReceiptBinding.inflate(
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                parent,
                false
            )

            tileReceiptView = tileReceiptBinding.root   //and add it to the View

            //creates holder and save the reference for the TextViews | better for performance
            val viewHolder = ViewHolder(
                tileReceiptBinding.nameTv,
                tileReceiptBinding.costTv,
                tileReceiptBinding.paymentTv
            )

            //saving ViewHolder's info to tag's view
            tileReceiptView.tag = viewHolder
        }

        //adding 'participant' info to cell..
        //.. through tags and ViewHolders
        with(tileReceiptView.tag as ViewHolder) {
            nameTv.text = payment.name
            costTv.text = ("R$${String.format("%.2f", payment.totalCost)}")
            paymentTv.text = ("R$${String.format("%.2f", payment.totalCost - (totalCost / paymentList.size))}")
        }

        return tileReceiptView
    }

    private data class ViewHolder(
        var nameTv: TextView,
        var costTv: TextView,
        var paymentTv: TextView
    )

}
