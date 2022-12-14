package com.nikunj.randomusers.presentation.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikunj.randomusers.R
import com.nikunj.randomusers.domain.models.User
import com.nikunj.randomusers.presentation.ui.common.FavoriteUserItemInteractions
import com.nikunj.randomusers.presentation.ui.common.UserDiffCallback
import com.nikunj.randomusers.presentation.ui.common.UserItemInteractions
import com.nikunj.randomusers.presentation.ui.common.setUserImage
import kotlinx.android.synthetic.main.item_user.view.*
import timber.log.Timber

class FavoriteUsersAdapter(private val favoriteUserItemInteractions: FavoriteUserItemInteractions) :
    ListAdapter<User, FavoriteUsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserViewHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Timber.d("Binding view holder at position $position")
        val user = getItem(position)
        user?.let { holder.bind(user, favoriteUserItemInteractions) }
    }

    fun swipeItem(position: Int) {
        favoriteUserItemInteractions.removeUserFromFavorites(getItem(position))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User, userItemInteractions: UserItemInteractions) {
            itemView.user_name.text = user.name
            itemView.user_email.text = user.email


            setUserImage(
                itemView.user_image,
                user.gender,
                itemView.context.getColor(R.color.colorSecondary),
                6f,
                user.largePicture
            )

            itemView.setOnClickListener { itemView ->
                Timber.i("User ${user.id} clicked in position $adapterPosition")
                itemView.user_image.transitionName = itemView.context
                    .getString(R.string.user_image_transition, adapterPosition)
                userItemInteractions.navigateToUserDetail(user, itemView.user_image, adapterPosition)
            }
        }
    }
}