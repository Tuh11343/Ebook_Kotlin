package com.example.connectnodejs.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.databinding.FragmentSignUpBinding
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.model.User
import com.example.connectnodejs.utils.RegexPattern
import com.example.connectnodejs.viewmodels.MainViewModel
import com.example.connectnodejs.viewmodels.SignUpViewModel
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class FragmentSignUp : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed
                Log.i(ContentValues.TAG, "Google sign in failed", e)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        signUpViewModel = ViewModelProvider(requireActivity())[SignUpViewModel::class.java]
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        // Tạo một ProgressBar mới
        createLoadingDialog()

        binding.btnClose.setOnClickListener{
            mainViewModel.returnLastState()
        }

        binding.btnGoogle.setOnClickListener{
            googleSignUp()
        }

        binding.btnSignUp.setOnClickListener {
            if (validate()) {
                signUpViewModel.findAccountByEmail(binding.gmail.text.toString())
                setDialog(true)

                val handler = Handler(Looper.getMainLooper())
                val runnable = Runnable {
                    handler.postDelayed({
                        signUpViewModel.findAccount.observe(viewLifecycleOwner) { account ->
                            setDialog(false)

                            if (account?.id != null) {
                                Toast.makeText(
                                    requireContext(),
                                    "Tài khoản đã tồn tại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                //Sign Up With NodeJS
                                createUser("",binding.phoneNumber.toString(),binding.fullName.toString(),false)

                                /*auth.createUserWithEmailAndPassword(
                                    binding.gmail.text.toString(),
                                    binding.password.text.toString(),
                                )
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.i(ContentValues.TAG, "Sign up success")
                                            *//*sendVerificationEmail()*//*

                                            //Create user and account
                                            createUser()

                                        } else {
                                            Log.i(
                                                ContentValues.TAG,
                                                "Sign up error:${task.exception}"
                                            )
                                        }
                                    }*/
                            }
                        }
                    }, 2000)
                }
                handler.post(runnable)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng kiểm tra dữ liệu điền vào!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createLoadingDialog() {
        val progressBar = ProgressBar(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = layoutParams

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setView(progressBar)
        dialog = builder.create()
    }

    private fun createAccount(userID: Int,subsscription_id:Int,email:String,is_verified:Boolean,password:String) {
        val account = Account()
        account.subscription_id = subsscription_id
        account.email = email
        account.user_id = userID
        account.is_verified = is_verified
        account.password = password

        signUpViewModel.createAccount(account)
    }

    private fun createUser(address:String,phoneNumber: String,name:String,googleAccount:Boolean) {
        val user = User()
        user.address = address
        user.phoneNumber = phoneNumber
        user.name = name
        signUpViewModel.createUser(user)
        signUpViewModel.updateGoogleAccount(googleAccount)
    }

    private fun createSubscription(
        subscriptionHistoryID: Int,
        duration: Int,
        pricePerMonth: Float,
        bookType: Book.BookType,
        limitBookMark: Int,
        type: String
    ) {
        val subscription = Subscription()
        subscription.type = type
        subscription.duration = duration
        subscription.subscription_history_id = subscriptionHistoryID
        subscription.price_per_month = pricePerMonth
        subscription.book_type = bookType
        subscription.limit_book_mark = limitBookMark

        signUpViewModel.createSubscription(subscription)
    }

    private fun createSubscriptionHistory(
        start: Date,
        end: Date,
        name: String,
        price: Float
    ) {
        val subscriptionHistory = SubscriptionHistory()
        subscriptionHistory.name = name
        subscriptionHistory.price = price
        subscriptionHistory.start = start
        subscriptionHistory.end = end

        signUpViewModel.createSubscriptionHistory(subscriptionHistory)

    }

    private fun observe() {
        observeCreatedUser()
        observeCreatedAccount()
        observeCreatedSubscription()
        observeCreatedSubscriptionHistory()
    }

    private fun observeCreatedUser() {
        signUpViewModel.createdUser.observe(viewLifecycleOwner) { user ->

            val start = Calendar.getInstance().time
            val end = Calendar.getInstance().time

            val name = "Normal"
            val price = 0F

            createSubscriptionHistory(start, end, name, price)
        }
    }

    private fun observeCreatedAccount() {
        signUpViewModel.createdAccount.observe(viewLifecycleOwner) { account ->
            goBack()
        }
    }

    private fun observeCreatedSubscriptionHistory() {
        signUpViewModel.createdSubscriptionHistory.observe(viewLifecycleOwner) { subscriptionHistory ->

            val duration = 0
            val pricePerMonth = 0f
            val bookType = Book.BookType.NORMAL
            val limitBookMark = 10
            val type = "Normal"
            createSubscription(
                subscriptionHistory.id!!,
                duration,
                pricePerMonth,
                bookType,
                limitBookMark,
                type
            )
        }
    }

    private fun observeCreatedSubscription() {
        signUpViewModel.createdSubscription.observe(viewLifecycleOwner) { subscription ->
            if(signUpViewModel.googleAccount.value==false){
                createAccount(signUpViewModel.createdUser.value!!.id!!,subscription.id!!,binding.gmail.text.toString(),true,binding.password.text.toString())
            }else{
                createAccount(signUpViewModel.createdUser.value!!.id!!,subscription.id!!,auth.currentUser!!.email!!,true,"123456")
            }

        }
    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }

    private fun goBack(){
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun validate(): Boolean {
        if(binding.fullName.text!!.isBlank()){
            Toast.makeText(requireContext(),"Hãy điền họ tên",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!RegexPattern.isValidEmail(binding.gmail.text!!.toString())){
            Toast.makeText(requireContext(),"Hãy nhập đúng định dạng gmail",Toast.LENGTH_SHORT).show()
            return false
        }
        if(!RegexPattern.isValidPassword(binding.password.text.toString())){
            Toast.makeText(requireContext(),"Mật khẩu cần ít nhất 6 ký tự",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun googleSignUp() {
        val signInIntent = mGoogleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    val isNewUser = task.result?.additionalUserInfo?.isNewUser
                    if (isNewUser == true) {
                        Log.i(ContentValues.TAG, "New user signed in")
                        createUser("",auth.currentUser!!.phoneNumber!!,auth.currentUser!!.displayName!!,true)
                        Toast.makeText(
                            requireContext(), "New user signed in.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Log.i(ContentValues.TAG, "Existing user signed in")
                        Toast.makeText(
                            requireContext(), "Existing user signed in.",
                            Toast.LENGTH_SHORT
                        ).show()
                        goBack()
                    }

                } else {
                    Log.i(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        requireContext(), "Đăng ký với tài khoản google thất bại",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }


}