package com.example.connectnodejs.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.FragmentSignInBinding
import com.example.connectnodejs.model.Account
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Subscription
import com.example.connectnodejs.model.SubscriptionHistory
import com.example.connectnodejs.model.User
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.utils.RegexPattern
import com.example.connectnodejs.viewmodels.MainViewModel
import com.example.connectnodejs.viewmodels.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date

class FragmentSignIn : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var signInViewModel: SignInViewModel

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog

    private lateinit var mGoogleSignInClient: GoogleSignInClient
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        mGoogleSignInClient.revokeAccess()
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        signInViewModel = ViewModelProvider(requireActivity())[SignInViewModel::class.java]
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe()

        binding.btnSignIn.setOnClickListener {
            if (validate()) {
                signIn(binding.gmail.text.toString(), binding.password.text.toString())
            }
        }

        binding.btnGoogle.setOnClickListener {
            googleSignIn()
        }

        binding.btnClose.setOnClickListener {
            mainViewModel.returnLastState()
        }
    }


    private fun signIn(email: String, password: String) {

        signInViewModel.signIn(email, password)

        /*auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {


                    // Lưu id vào trong cục bộ
                    val editor = sharedPreferences.edit()
                    editor.putInt(
                        AppInstance.ACCOUNT_ID_KEY.toString(),
                        AppInstance.currentAccount!!.id!!
                    )
                    editor.apply()
                    Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT)
                        .show()

                    mainViewModel

                } else {
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Đăng nhập thất bại",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }*/
    }

    private fun observe() {
        observeCreatedUser()
        observeCreatedAccount()
        observeCreatedSubscription()
        observeCreatedSubscriptionHistory()
        observeSignInAccount()
        observeAccountSubscription()
    }

    private fun validate(): Boolean {

        if (!RegexPattern.isValidEmail(binding.gmail.text!!.toString())) {
            Toast.makeText(requireContext(), "Hãy nhập đúng định dạng gmail", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (!RegexPattern.isValidPassword(binding.password.text.toString())) {
            Toast.makeText(requireContext(), "Mật khẩu cần ít nhất 6 ký tự", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }

    private fun googleSignIn() {
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
                        createUser("", "", auth.currentUser!!.displayName!!, true)
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

                        signInViewModel.findAccountByEmail(auth.currentUser!!.email!!)
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

    private fun createAccount(
        userID: Int,
        subsscription_id: Int,
        email: String,
        is_verified: Boolean,
        password: String
    ) {
        val account = Account()
        account.subscription_id = subsscription_id
        account.email = email
        account.user_id = userID
        account.is_verified = is_verified
        account.password = password

        signInViewModel.createAccount(account)
    }

    private fun createUser(
        address: String,
        phoneNumber: String,
        name: String,
        googleAccount: Boolean
    ) {
        val user = User()
        user.address = address
        user.phoneNumber = phoneNumber
        user.name = name
        signInViewModel.createUser(user)
        signInViewModel.updateGoogleAccount(googleAccount)
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

        signInViewModel.createSubscription(subscription)
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

        signInViewModel.createSubscriptionHistory(subscriptionHistory)

    }

    private fun observeCreatedUser() {
        signInViewModel.createdUser.observe(viewLifecycleOwner) { user ->

            val start = Calendar.getInstance().time
            val end = Calendar.getInstance().time

            val name = "Normal"
            val price = 0F

            createSubscriptionHistory(start, end, name, price)
        }
    }

    private fun observeCreatedAccount() {
        signInViewModel.createdAccount.observe(viewLifecycleOwner) { account ->
            val editor = sharedPreferences.edit()
            editor.putInt(
                AppInstance.ACCOUNT_ID_KEY.toString(),
                account.id!!
            )
            editor.apply()
            Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT)
                .show()

            AppInstance.currentAccount = account
            mainViewModel.updateBottomBarTab(0)
        }
    }

    private fun observeCreatedSubscriptionHistory() {
        signInViewModel.createdSubscriptionHistory.observe(viewLifecycleOwner) { subscriptionHistory ->

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
        signInViewModel.createdSubscription.observe(viewLifecycleOwner) { subscription ->

            AppInstance.currentSubscription=subscription

            if (signInViewModel.googleAccount.value == false) {
                createAccount(
                    signInViewModel.createdUser.value!!.id!!,
                    subscription.id!!,
                    binding.gmail.text.toString(),
                    true,
                    binding.password.text.toString()
                )

                //Luu account google hay la gmail
                val editor = sharedPreferences.edit()
                editor.putBoolean(
                    AppInstance.IS_GOOGLE_ACCOUNT.toString(),
                    false
                )
                editor.apply()


            } else {
                createAccount(
                    signInViewModel.createdUser.value!!.id!!,
                    subscription.id!!,
                    auth.currentUser!!.email!!,
                    true,
                    "123456"
                )

                val editor = sharedPreferences.edit()
                editor.putBoolean(
                    AppInstance.IS_GOOGLE_ACCOUNT.toString(),
                    true
                )
                editor.apply()
            }


        }
    }

    private fun observeSignInAccount() {
        signInViewModel.signInAccount.observe(viewLifecycleOwner) { account ->

            if (account == null) {
                Toast.makeText(
                    requireContext(),
                    "Thông tin đăng nhập không đúng",
                    Toast.LENGTH_SHORT
                )
            } else {
                // Lưu id vào trong cục bộ
                val editor = sharedPreferences.edit()
                editor.putInt(
                    AppInstance.ACCOUNT_ID_KEY.toString(),
                    account.id!!
                )
                editor.apply()

                AppInstance.currentAccount = account
                signInViewModel.findAccountSubscription(account.id!!)
            }

        }
    }

    private fun observeAccountSubscription() {
        signInViewModel.accountSubscription.observe(viewLifecycleOwner) { subscription ->
            Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT)
                .show()
            AppInstance.currentSubscription=subscription

            val editor = sharedPreferences.edit()
            editor.putBoolean(
                AppInstance.IS_GOOGLE_ACCOUNT.toString(),
                true
            )
            editor.apply()

            //Go Back To Home
            mainViewModel.updateBottomBarTab(0)
        }
    }

    private fun goBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }


}