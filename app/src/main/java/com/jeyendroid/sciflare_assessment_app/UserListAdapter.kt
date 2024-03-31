package com.jeyendroid.sciflare_assessment_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeyendroid.sciflare_assessment_app.databinding.ListItemUserBinding

class UserListAdapter(private val userList : List<UserDataModel>,
                      private val listener : OnPerformAction) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.list_item_user, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listener)
    }

    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(private val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener : OnPerformAction) {
            binding.tvUserName.text = userList[adapterPosition].Name
            binding.tvUserNumber.text = userList[adapterPosition].Mobile
            binding.tvUserEmail.text = userList[adapterPosition].Email
            binding.tvUserGender.text = userList[adapterPosition].Gender

            binding.ivEdit.setOnClickListener { listener.onEdit(userList[adapterPosition]) }
            binding.ivDelete.setOnClickListener { listener.onDelete(userList[adapterPosition]) }
        }
    }

    interface OnPerformAction {
        fun onEdit(userDataModel: UserDataModel)
        fun onDelete(userDataModel: UserDataModel)
    }
}