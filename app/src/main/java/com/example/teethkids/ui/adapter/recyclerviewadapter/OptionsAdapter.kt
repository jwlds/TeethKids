package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.R
import com.example.teethkids.databinding.OptionItemBinding
import com.example.teethkids.model.Option


class OptionsAdapter(
    private val context: Context,
    private val onOptionClicked: (Option) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {

    private val options = listOf(
        Option(
            name = "Meus Dados",
            description = "Perfil, nome, data de nascimento, telefone, CRO",
            icon = R.drawable.account_circle
        ),
        Option(
            name = "Mini Currículo",
            description = "Mini currículo cadastrado",
            icon = R.drawable.baseline_menu_book_24
        ),
        Option(
            name = "Endereços",
            description = "Endereços registrados",
            icon = R.drawable.outline_add_home_work_24
        ),
        Option(
            name = "Avaliações",
            description = "Avaliações enviadas",
            icon = R.drawable.outline_reviews_24
        ),
        Option(
            name = "Ajuda",
            description = "Ajuda sobre o App",
            icon = R.drawable.baseline_help_outline_24
        ),
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