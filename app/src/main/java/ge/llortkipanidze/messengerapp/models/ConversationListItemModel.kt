package ge.llortkipanidze.messengerapp.models

import android.graphics.Bitmap
import java.util.*

data class ConversationListItemModel(val userName: String, val lastMessage: String, val sendTime: String, val bitmap: Bitmap)