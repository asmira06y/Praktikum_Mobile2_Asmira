
package id.ac.polbeng.asmira.kabkotaretrofitapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ac.polbeng.asmira.kabkotaretrofitapp.databinding.KabkotaItemBinding
import id.ac.polbeng.asmira.kabkotaretrofitapp.model.KabKotaItem

class KabKotaAdapter :
    ListAdapter<KabKotaItem, KabKotaAdapter.ViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickCallback {
        fun onItemClicked(data: KabKotaItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(
        callback: OnItemClickCallback
    ) {
        onItemClickCallback = callback
    }

    inner class ViewHolder(
        private val binding: KabkotaItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KabKotaItem) {
            binding.tvKabKota.text = item.kabupatenKota
            binding.tvPusatPemerintahan.text =
                "Pusat Pemerintahan: ${item.pusatPemerintahan}"

            binding.root.setOnClickListener {
                if (::onItemClickCallback.isInitialized) {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = KabkotaItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<KabKotaItem>() {
                override fun areItemsTheSame(
                    oldItem: KabKotaItem,
                    newItem: KabKotaItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: KabKotaItem,
                    newItem: KabKotaItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
