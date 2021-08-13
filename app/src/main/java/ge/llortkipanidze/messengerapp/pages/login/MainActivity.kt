package ge.llortkipanidze.messengerapp.pages.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.llortkipanidze.messengerapp.R
import ge.llortkipanidze.messengerapp.models.Conversation
import ge.llortkipanidze.messengerapp.models.UserMetaData
import ge.llortkipanidze.messengerapp.pages.home.HomeActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var etNickname : EditText
    lateinit var etPassword : EditText
    lateinit var signInButton : Button
    lateinit var signUpButtonLogin: Button
    lateinit var etProfession : EditText
    lateinit var signUpButtonRegister: Button
    lateinit var notRegisteredTextView : TextView

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVariables()
    }

    private fun initVariables() {
        etNickname = findViewById(R.id.logInNicknameEt)
        etPassword = findViewById(R.id.logInPasswordEt)
        signInButton = findViewById(R.id.signInButton)
        signUpButtonLogin = findViewById(R.id.loginSignUpButton)
        etProfession = findViewById(R.id.whatIdoTextField)
        signUpButtonRegister = findViewById(R.id.registerSignUpButton)
        notRegisteredTextView = findViewById(R.id.notRegisteredTextView)

        auth = Firebase.auth
        if(auth.currentUser != null) {
            forwardToHomePage()
            return
        }
        signInButton.setOnClickListener {


            trySignIn()
        }

        signUpButtonLogin.setOnClickListener{


            transferToRegisterPage()
        }

        signUpButtonRegister.setOnClickListener {


            registerNewUser()
        }


    }

    private fun registerNewUser() {
        val userName = etNickname.text.toString()
        val email =  userName + EMAIL_SUFFIX
        val password = etPassword.text.toString();
        val profession = etProfession.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")

                    var user = auth.currentUser
                    val database = Firebase.database
                    val newUserReference = database.getReference("UserDataList")
                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }
                    user!!.updateProfile(profileUpdates)
                    newUserReference.child(user!!.uid).setValue(UserMetaData(profession, HashMap<String, Conversation>()))
                    forwardToHomePage()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }




    }

    private fun forwardToHomePage() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }



    private  fun trySignIn(){
        val email = etNickname.text.toString() + EMAIL_SUFFIX
        val password = etPassword.text.toString();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful){

                Toast.makeText(baseContext, "Success", Toast.LENGTH_LONG).show()
                forwardToHomePage()
            }
            else{
                Toast.makeText(baseContext, "Failure", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun transferToRegisterPage(){

        notRegisteredTextView.visibility = View.GONE
        signUpButtonLogin.visibility = View.GONE
        signInButton.visibility = View.GONE
        etProfession.visibility = View.VISIBLE
        signUpButtonRegister.visibility = View.VISIBLE


    }

    override fun onBackPressed() {
 //       super.onBackPressed()
        notRegisteredTextView.visibility = View.VISIBLE
        signUpButtonLogin.visibility = View.VISIBLE
        signInButton.visibility = View.VISIBLE
        etProfession.visibility = View.GONE
        signUpButtonRegister.visibility = View.GONE
    }

    companion object {
        private const val EMAIL_SUFFIX = "@dummy.com"
    }


}