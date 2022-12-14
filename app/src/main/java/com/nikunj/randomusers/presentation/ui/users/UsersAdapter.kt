package com.nikunj.randomusers.presentation.ui.users

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikunj.randomusers.R
import com.nikunj.randomusers.domain.models.User
import com.nikunj.randomusers.presentation.ui.common.UserDiffCallback
import com.nikunj.randomusers.presentation.ui.common.UserItemInteractions
import com.nikunj.randomusers.presentation.ui.common.setUserImage
import kotlinx.android.synthetic.main.item_user.view.*
import timber.log.Timber

class UsersAdapter(private val userItemInteractions: UserItemInteractions) :
    PagedListAdapter<User, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Timber.d("Binding view holder at position $position")
        val user = getItem(position)
        user?.let { holder.bind(user, userItemInteractions) }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, userItemInteractions: UserItemInteractions) {
            itemView.user_name.text = user.name
            itemView.user_email.text = user.email

            if(user.favorite)
                setUserImage(user, itemView, itemView.context.getColor(R.color.colorSecondary), 6f)
            else
                setUserImage(user, itemView)

            itemView.setOnClickListener { itemView ->
                Timber.i("User ${user.id} clicked in position $adapterPosition")
                itemView.user_image.transitionName = itemView.context
                    .getString(R.string.user_image_transition, adapterPosition)
                userItemInteractions.navigateToUserDetail(user, itemView.user_image, adapterPosition)
            }
        }

        private fun setUserImage(user: User, itemView: View, borderColor: Int = Color.GRAY, borderSize: Float = 3f) {
            setUserImage(
                itemView.user_image,
                user.gender,
                borderColor,
                borderSize,
                user.largePicture
            )
        }
    }
}