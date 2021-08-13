package ge.llortkipanidze.messengerapp.models

data class UserMetaData(val profession : String? =null, val conversationMap : Map<String, Conversation>? = null)
