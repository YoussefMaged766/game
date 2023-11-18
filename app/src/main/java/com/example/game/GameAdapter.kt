package com.example.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.game.databinding.GameItemBinding
import com.example.game.db.GameEntity

class GameAdapter : ListAdapter<GameEntity, GameAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<GameEntity>() {
        override fun areItemsTheSame(
            oldItem: GameEntity,
            newItem: GameEntity
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GameEntity,
            newItem: GameEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    class viewholder(var binding: GameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GameEntity) {
            binding.imageView1.setImageResource(data.img1)
            binding.imageView2.setImageResource(data.img2)
            binding.imageView3.setImageResource(data.img3)
            binding.txtTotalBalance.text = "${binding.root.context.getString(R.string.totalAssets)} ${data.currentAssets}$"
            binding.txtBet.text = "${binding.root.context.getString(R.string.bet)} ${data.currentBet}$"
            if (data.isWin) {
                binding.txtGainOrLoss.text = "${binding.root.context.getString(R.string.gain)} ${data.gain}$"
                binding.linearGameItem.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.win
                    )
                )
            } else {
                binding.txtGainOrLoss.text = "${binding.root.context.getString(R.string.loss)} -${data.lose}$"
                binding.linearGameItem.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            GameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))
    }
}