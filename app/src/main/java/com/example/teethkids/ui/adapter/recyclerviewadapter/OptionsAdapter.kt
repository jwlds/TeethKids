package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.R
import com.example.teethkids.databinding.OptionItemBinding
import com.example.teethkids.model.Option

//class OptionsAdapter(
//private val context: Context
//):
//RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {
//
//    private val options = listOf(
//        Option(
//            name = "Conversas",
//            description = "Meus histórico de conversas",
//            icon = R.drawable.baseline_message_24),
//        Option(
//            name = "Meu dados ",
//            description = "Minha informações da conta",
//            icon = R.drawable.baseline_person_24),
//        Option(
//            name = "Endereços ",
//            description = "Meus endereços de atendimento",
//            icon = R.drawable.baseline_pin_drop_24) ,
//        Option(
//            name = "Ajuda",
//        description = "Podemos ajudar voce",
//        icon = R.drawable.baseline_message_24),
//    )
//
//
//    class OptionViewHolder(val binding: OptionItemBinding):
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(option: Option) {
//            val optionSelected:(Option,Int) -> Unit
//
//            binding.optionItemName.text = option.name
//            binding.optionItemDescription.text = option.description
//            binding.optionItemIcon.setImageResource(option.icon)
//        }
//    }
//
//    override fun getItemCount(): Int = options.size
//
//    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
//        val option = options[position]
//        holder.bind(option)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
//        val inflater = LayoutInflater.from(context)
//        val binding = OptionItemBinding.inflate(inflater, parent, false)
//
//        return OptionViewHolder(binding)
//    }
//
//
//}

class OptionsAdapter(
    private val context: Context,
    private val onOptionClicked: (Option) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    private val options = listOf(
        Option(
            name = "Conversas",
            description = "Meus histórico de conversas",
            icon = R.drawable.baseline_message_24
        ),
        Option(
            name = "Meus dados",
            description = "Minha informações da conta",
            icon = R.drawable.baseline_person_24
        ),
        Option(
            name = "Endereços",
            description = "Meus endereços de atendimento",
            icon = R.drawable.baseline_pin_drop_24
        ),
        Option(
            name = "Ajuda",
            description = "Podemos ajudar você",
            icon = R.drawable.baseline_message_24
        )
    )

    class OptionViewHolder(val binding: OptionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: Option, onOptionClicked: (Option) -> Unit) {
            binding.optionItemName.text = option.name
            binding.optionItemDescription.text = option.description
            binding.optionItemIcon.setImageResource(option.icon)

            binding.root.setOnClickListener {
                onOptionClicked(option)
            }
        }
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option, onOptionClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = OptionItemBinding.inflate(inflater, parent, false)

        return OptionViewHolder(binding)
    }
}