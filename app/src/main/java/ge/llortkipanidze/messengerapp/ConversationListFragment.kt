package ge.llortkipanidze.messengerapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.llortkipanidze.messengerapp.models.Conversation
import ge.llortkipanidze.messengerapp.models.ConversationListItemModel
import ge.llortkipanidze.messengerapp.models.Message
import ge.llortkipanidze.messengerapp.models.UserMetaData
import ge.llortkipanidze.messengerapp.pages.home.ConversationListAdapter
import java.util.*
import kotlin.math.log


class ConversationListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    var recylerViewAdapter = ConversationListAdapter(mutableListOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view:View = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        recyclerView = view?.findViewById(R.id.conversationsRecyclerView)!!
        recyclerView.adapter = recylerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        fetchData()


        return view

    }

    private fun fetchData(){

        val database = Firebase.database
        auth = Firebase.auth
        val ref = database.getReference("UserDataList").child(auth.currentUser!!.uid)
        ref.get().addOnCompleteListener {
            if (it.isSuccessful){
                val res : DataSnapshot? = it.result
                val userMetaData =res!!.getValue(UserMetaData::class.java)
                renderData(userMetaData!!)
            }
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
    }


    private fun renderData(userMetaData: UserMetaData){
        val conversationMap = userMetaData.conversaitoMapn ?: HashMap<String, Conversation>()
        val conversationsList = mutableListOf<ConversationListItemModel>()
        for((key, value) in conversationMap){
            val conversation = value.messageMap
            val lastMessage = conversation?.get(conversation.size - 1)
            val ref = Firebase.storage.reference.child("profilePictures/llort")
            ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                val conversationListItem = ConversationListItemModel(key, lastMessage!!.messageText!!,
                    Date(), bitmap
                )
                conversationsList.add(conversationListItem)
                recylerViewAdapter.list = conversationsList
                recylerViewAdapter.notifyDataSetChanged()
            }



        }




    }


}