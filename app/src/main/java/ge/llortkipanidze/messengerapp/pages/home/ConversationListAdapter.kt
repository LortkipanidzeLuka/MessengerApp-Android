package ge.llortkipanidze.messengerapp.pages.home

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import ge.llortkipanidze.messengerapp.R
import ge.llortkipanidze.messengerapp.models.ConversationListItemModel
import ge.llortkipanidze.messengerapp.models.Message

class ConversationListAdapter(var list: List<ConversationListItemModel>):
    RecyclerView.Adapter<ConversationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val item = list[position]
        holder.initView(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class ConversationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val profilePicImageView = itemView.findViewById<ImageView>(R.id.conversationListItemProfilePicImageView)
    val displayNameTextView = itemView.findViewById<TextView>(R.id.conversationListItemDisplayNameTextView)
    val messageTextView = itemView.findViewById<TextView>(R.id.conversationListItemMessageTextView)
    val timeTextView = itemView.findViewById<TextView>(R.id.conversationListItemTimeTextView)

    fun initView(conversationListItemModel: ConversationListItemModel){

        profilePicImageView.setImageBitmap(conversationListItemModel.bitmap)
        displayNameTextView.text = conversationListItemModel.userName
        messageTextView.text = conversationListItemModel.lastMessage
        timeTextView.text = conversationListItemModel.sendTime.toString()

    }



}