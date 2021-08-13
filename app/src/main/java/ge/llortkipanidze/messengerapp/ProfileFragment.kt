package ge.llortkipanidze.messengerapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

import ge.llortkipanidze.messengerapp.pages.login.MainActivity


class ProfileFragment : Fragment() {

   lateinit var profilePictureImageView: ImageView
   lateinit var userNameEt : EditText
   lateinit var professionEt : EditText
   lateinit var updateButton : Button
   lateinit var signOutButton: Button
   lateinit var user : FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(view)

        return view

    }

    fun initViews(view:View){
        profilePictureImageView = view.findViewById(R.id.profilePageProfilePicture)
        userNameEt = view.findViewById(R.id.profilePageUsernameEt)
        professionEt = view.findViewById(R.id.profilePageProfessionEt)
        updateButton = view.findViewById(R.id.updateButton)
        signOutButton = view.findViewById(R.id.signOutButton)

        val auth = Firebase.auth
        user = auth.currentUser!!

        userNameEt.setText(user!!.displayName)
        setProfessionInitValue()

        signOutButton.setOnClickListener{
           logOut(auth)
        }

        updateButton.setOnClickListener {
            updateInfo()
        }

        profilePictureImageView.setOnClickListener {
            uploadImage()
        }


    }

    private fun uploadImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    fun setProfessionInitValue(){
        val database = Firebase.database
        val ref = database.getReference("UserDataList").child(user.uid).child("profession")

        ref.get().addOnCompleteListener {
            if(it.isSuccessful){
                professionEt.setText(it.result?.value.toString())
            }
        }
    }

    private fun logOut(auth: FirebaseAuth){
        auth.signOut()
        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updateInfo(){

        val newUserName = userNameEt.text.toString()

        val update = userProfileChangeRequest {
            displayName = newUserName
        }
        user.updateProfile(update)

        user.updateEmail("$newUserName@dummy.com")

        val database = Firebase.database
        val ref = database.getReference("UserDataList").child(user.uid)
        ref.child("profession").setValue(professionEt.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            profilePictureImageView.setImageURI(data.data)
            val imageStorage = Firebase.storage
            val ref = imageStorage.getReference("profilePictures/${user.displayName}")

            ref.putFile(data.data!!)

        }
    }

    private fun getProfilePicture(){
        val ref = Firebase.storage.reference.child("profilePictures/${user.displayName}")
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            profilePictureImageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfilePicture()
    }



}