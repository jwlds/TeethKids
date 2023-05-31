package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.R
import com.example.teethkids.databinding.HelpItemBinding
import com.example.teethkids.model.HelpOption

class ListHelpOptionAdapter(
    private val context: Context,
    private val onHelpOptionClicked: (HelpOption) -> Unit
) : RecyclerView.Adapter<ListHelpOptionAdapter.HelpOptionViewHolder>() {

    private val options = listOf(
        HelpOption(
            name = "Atualizar senha",
            description = "Senha atualizada em 01/02",
            icon = R.drawable.baseline_upgrade_24,
            color = "#59904F",
        ),
        HelpOption(
            name = "Recuperar senha",
            description = "Senha recuperada em 01/02",
            icon = R.drawable.outline_security_update_warning_24,
            color = "#904F4F",
        ),

        )

    class HelpOptionViewHolder(val binding: HelpItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(help: HelpOption, onOptionClicked: (HelpOption) -> Unit) {
            binding.helpOptionName.text = help.name
            binding.helpOptionDiscription.text = help.description

            val icon = ContextCompat.getDrawable(binding.root.context, help.icon)
            icon?.let {
                val color = Color.parseColor(help.color)
                DrawableCompat.setTint(it, color)
                binding.helpOptionIcon.setImageDrawable(it)
            }

            binding.root.setOnClickListener {
                onOptionClicked(help)
            }
        }
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: HelpOptionViewHolder, position: Int) {
        val help = options[position]
        holder.bind(help, onHelpOptionClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpOptionViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = HelpItemBinding.inflate(inflater, parent, false)

        return HelpOptionViewHolder(binding)
    }
}