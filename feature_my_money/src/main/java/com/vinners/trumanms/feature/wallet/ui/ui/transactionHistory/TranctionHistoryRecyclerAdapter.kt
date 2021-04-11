package com.vinners.trumanms.feature.wallet.ui.ui.transactionHistory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.money.Transaction
import com.vinners.trumanms.feature.wallet.R
import java.util.*

class TranctionHistoryRecyclerAdapter :
    RecyclerView.Adapter<TranctionHistoryRecyclerAdapter.TransactionViewHolder>() {
    private var transactionList = listOf<Transaction>()
    private var context : Context? = null

    fun updateList(transactionList: List<Transaction>) {
        this.transactionList = transactionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_recycler_items, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.onBind(transactionList[position])
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amount = itemView.findViewById<TextView>(R.id.amountEt)
       private val day = itemView.findViewById<TextView>(R.id.dayEt)
        private val month = itemView.findViewById<TextView>(R.id.monthEt)
        private val year = itemView.findViewById<TextView>(R.id.yearEt)
        private val remarks = itemView.findViewById<TextView>(R.id.remarksEt)
        private val type = itemView.findViewById<TextView>(R.id.typeEt)
        private val clientInfo = itemView.findViewById<TextView>(R.id.clientEt)
        private val category = itemView.findViewById<TextView>(R.id.categoryEt)
        private val status = itemView.findViewById<TextView>(R.id.isRedeemed)


        fun onBind(transaction: Transaction) {
            category.text = transaction.category
            type.text = transaction.type
            remarks.text = transaction.remarks
            if (transaction.type.equals("credit",true)) {
                amount.setTextColor(ContextCompat.getColor(context!!, R.color.button_back))
                amount.text = "+₹${transaction.amount}"
            }else if (transaction.type.equals("debit",true)){
                amount.setTextColor(ContextCompat.getColor(context!!, R.color.world_rugby_red))
                amount.text = "-₹${transaction.amount}"
            }else
                amount.text = "₹0"
            if (transaction.date.isNullOrEmpty().not()){
                year.text = DateTimeHelper.getYearFromString(transaction.date!!)
                month.text = DateTimeHelper.getMonthFromString(transaction.date!!)
                day.text = DateTimeHelper.getDayFromString(transaction.date!!)
            }
            if(transaction.category.equals("Referral",true)){
                clientInfo.setVisibilityVisible()
                clientInfo.text =
                    """${transaction.firstName} ${transaction.lastName}, ${transaction.mobile}"""
            }else if (transaction.category.equals("task",true)) {
                clientInfo.setVisibilityVisible()
                clientInfo.text = transaction.jobNo
            }else
                clientInfo.setVisibilityGone()

            if (transaction.isReedemable)
                status.text = "Approved"
            else
                status.text = "Pending"
        }
    }
}