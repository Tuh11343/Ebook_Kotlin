package com.example.connectnodejs

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.connectnodejs.adapter.MainPageAdapter
import com.example.connectnodejs.databinding.MainBinding
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.service.MusicService
import com.example.connectnodejs.utils.AlarmReceiver
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.MainViewModel
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainBinding
    private lateinit var mainPageAdapter: MainPageAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var musicService: MusicService? = null
    private var isBound = false

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: MusicService.LocalBinder = service as MusicService.LocalBinder
            musicService = binder.getService()
            Log.i("Nothing", "Bat dau ket noi")
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i("Nothing", "Ket noi that bai")
        }
    }

    fun getMusicService(): MusicService? {
        return musicService
    }

    override fun onStart() {
        super.onStart()
        if (!isBound) {
            val serviceIntent = Intent(this, MusicService::class.java)
            bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
            isBound = true
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null && intent.hasExtra("data")) {
            val data = intent.getStringExtra("data")
            Log.d("Nothing", "$data")

            mainViewModel.updateBottomBarVisibility(false)
            mainViewModel.updateState(MainViewModel.Companion.State.ReadBook)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("Nothing", "Du lieu:${Calendar.getInstance().time}")

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        setUpViewModel()
        setUpUI()
        createNotificationChannel()
        setUpAlarm()
        loadSignInAccount()
        onBackPressedHandle()
    }

    private fun setUpUI() {
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songControlSwipeDown()
        setUpViewPager()
        topAppBarItemClick()
        bottomBarOnTabSelect()

        //Di den trang chu
        mainViewModel.addState(MainViewModel.Companion.State.Home)
    }

    private fun setUpViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.createAppState()
        observe()
    }

    private fun setUpViewPager() {
        mainPageAdapter = MainPageAdapter(this, binding.viewPager)

        binding.viewPager.adapter = mainPageAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == mainPageAdapter.toFragmentSubscription()) {
                    binding.viewPager.adapter?.notifyDataSetChanged()
                }
            }
        })
        binding.viewPager.isUserInputEnabled = false
    }

    private fun observe() {
        observeAppState()
        observeBottomBarVisibility()
        observeSongControlVisibility()
        observeAppBarVisibility()
        observeBottomBarTab()
        observeCurrentAccountSubscriptionHistory()
        observeCurrentAccountSubscription()
        observeAppBarVisibility()
        observeCurrentAccount()
    }

    private fun observeAppState() {
        mainViewModel.appState.observe(this) { state ->

            when (state.peek()) {
                MainViewModel.Companion.State.ReadBook -> {
                    goToFragmentReadBook()
                }

                MainViewModel.Companion.State.DetailBook -> {
                    goToFragmentDetailBook()
                }

                MainViewModel.Companion.State.Home -> {
                    goToFragmentHome()
                }

                MainViewModel.Companion.State.Search -> {
                    goToFragmentSearch()
                }

                MainViewModel.Companion.State.User -> {
                    goToFragmentUser()
                }

                MainViewModel.Companion.State.SignIn -> {


                    goToFragmentSignIn()
                }

                MainViewModel.Companion.State.SignUp -> {


                    goToFragmentSignUp()
                }

                MainViewModel.Companion.State.Author -> {


                    goToFragmentAuthor()
                }

                MainViewModel.Companion.State.Subscription -> {
                    goToFragmentSubscription()
                }

                MainViewModel.Companion.State.Favorite -> {


                    goToFragmentFavorite()

                }

                MainViewModel.Companion.State.UserSignIn -> {
                    goToFragmentUserLogin()
                }

            }
        }

    }

    private fun goToFragmentDetailBook() {

        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentDetailBook(), false)
    }

    private fun goToFragmentHome() {
        mainViewModel.updateAppBarVisibility(true)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toHomeFragment(), false)
    }

    private fun goToFragmentSearch() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentSearch(), false)
    }

    private fun goToFragmentUser() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentUser(), false)
    }

    private fun goToFragmentUserLogin() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentUserLogin(), false)
    }

    private fun goToFragmentReadBook() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(false)
        mainViewModel.updateSongControlVisibility(false)

        binding.viewPager.setCurrentItem(mainPageAdapter.toReadBookFragment(), false)
    }

    private fun goToFragmentAuthor() {

        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentAuthor(), false)
    }

    private fun goToFragmentSignIn() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentSignIn(), false)
    }

    private fun goToFragmentSignUp() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentSignUp(), false)
    }

    private fun goToFragmentSubscription() {
        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentSubscription(), false)
    }

    private fun goToFragmentFavorite() {
        mainViewModel.updateAppBarVisibility(false)
        mainViewModel.updateBottomBarVisibility(true)

        binding.viewPager.setCurrentItem(mainPageAdapter.toFragmentFavorite(), false)
    }

    private fun topAppBarItemClick() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.topMenu_search -> {
                    binding.bottomBar.selectTabAt(1)
                    true
                }

                R.id.user -> {
                    binding.bottomBar.selectTabAt(3)
                    true
                }

                else -> {
                    Log.i("ERROR", "Item ID:${menuItem.itemId}")
                    false
                }
            }
        }
    }

    private fun bottomBarOnTabSelect() {
        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newIndex) {
                    0 -> {
                        mainViewModel.resetAppState(MainViewModel.Companion.State.Home)
                    }

                    1 -> {
                        mainViewModel.resetAppState(MainViewModel.Companion.State.Search)
                    }

                    2 -> {
                        if (AppInstance.currentAccount != null) {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.Favorite)
                        } else {
                            binding.bottomBar.selectTabAt(3)
                        }
                    }

                    3 -> {
                        if (AppInstance.currentAccount != null) {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.UserSignIn)
                        } else {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.User)
                        }
                    }
                }

            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                when (index) {
                    0 -> {
                        mainViewModel.resetAppState(MainViewModel.Companion.State.Home)
                    }

                    1 -> {
                        mainViewModel.resetAppState(MainViewModel.Companion.State.Search)
                    }

                    2 -> {
                        if (AppInstance.currentAccount != null) {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.Favorite)
                        } else {
                            binding.bottomBar.selectTabAt(3)
                        }
                    }

                    3 -> {
                        if (AppInstance.currentAccount != null) {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.UserSignIn)
                        } else {
                            mainViewModel.resetAppState(MainViewModel.Companion.State.User)
                        }
                    }
                }
            }
        })

    }

    private fun observeAppBarVisibility() {
        mainViewModel.appBarVisibility.observe(this) {
            when (it) {
                true -> {
                    binding.appBar.visibility = View.VISIBLE
                }

                false -> {
                    binding.appBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeBottomBarVisibility() {
        mainViewModel.bottomBarVisibility.observe(this) {
            when (it) {
                true -> {
                    binding.bottomBar.visibility = View.VISIBLE
                }

                false -> {
                    binding.bottomBar.visibility = View.GONE
                }
            }
        }
    }

    private fun observeBottomBarTab() {
        mainViewModel.bottomBarTab.observe(this) {
            binding.bottomBar.selectTabAt(it)
        }
    }

    private fun observeMusicBtnPlayClick(){
        musicService?.btnPlayClick?.observe(this){
            if (musicService?.mediaPlayer?.isPlaying == true) {
                binding.btnPlay.setImageResource(R.drawable.icon_pause)
            } else {
                binding.btnPlay.setImageResource(R.drawable.icon_play)
            }
        }
    }

    private fun observeSongControlVisibility() {
        mainViewModel.songControlVisibility.observe(this) {
            observeMusicBtnPlayClick()
            when (it) {
                true -> {
                    if (musicService?.isNotificationCreated == true) {

                        var book:Book?=null
                        if(mainViewModel.getSelectedBook()!=null)
                            book=mainViewModel.getSelectedBook()!!

                        binding.songControl.visibility = View.VISIBLE

                        binding.btnPlay.setOnClickListener {
                            musicService?.btnPlayClick?.postValue(Unit)
                            musicService?.play()
                            if (musicService?.mediaPlayer?.isPlaying == true) {
                                binding.btnPlay.setImageResource(R.drawable.icon_pause)
                            } else {
                                binding.btnPlay.setImageResource(R.drawable.icon_play)
                            }
                        }

                        if(book!=null){
                            Glide.with(this)
                                .load(book.image)
                                .placeholder(R.drawable.song_circle)
                                .error(R.drawable.song_circle)
                                .into(binding.songImg)
                            binding.bookName.text = book.name
                        }


                        if (musicService?.mediaPlayer?.isPlaying == true) {
                            binding.btnPlay.setImageResource(R.drawable.icon_pause)
                        } else {
                            binding.btnPlay.setImageResource(R.drawable.icon_play)
                        }

                        musicService!!.btnCloseClick!!.observe(this) {
                            if (musicService?.mediaPlayer?.isPlaying == true) {
                                binding.btnPlay.setImageResource(R.drawable.icon_pause)
                            } else {
                                binding.btnPlay.setImageResource(R.drawable.icon_play)
                            }
                            binding.btnPlay.setImageResource(R.drawable.icon_play)
                        }
                    }
                }

                false -> {
                    binding.songControl.visibility = View.GONE
                }
            }
        }
    }

    private fun observeCurrentAccount() {
        mainViewModel.currentAccount.observe(this) { account ->
            Toast.makeText(this@MainActivity, "Đã đăng nhập", Toast.LENGTH_SHORT).show()
            AppInstance.currentAccount = account
        }
    }

    private fun observeCurrentAccountSubscription() {
        mainViewModel.currentAccountSubscription.observe(this) { subscription ->

            mainViewModel.findAccountSubscriptionHistory(subscription.id!!)
            AppInstance.currentSubscription = subscription

        }
    }

    private fun observeCurrentAccountSubscriptionHistory() {
        mainViewModel.currentAccountSubscriptionHistory.observe(this) { subscriptionHistory ->
            val subscription = AppInstance.currentSubscription
            //Check coi hết date chưa
            if (subscription!!.book_type == Book.BookType.PREMIUM) {
                if (subscriptionHistory!!.end.before(Calendar.getInstance().time)) {
                    //Hết hạn premium

                    subscription.book_type = Book.BookType.NORMAL
                    subscription.limit_book_mark = 10
                    subscription.price_per_month = 0f
                    subscription.duration = 0
                    subscription.type = "Normal"
                    mainViewModel.updateSubscription(subscription)

                    subscriptionHistory!!.name = "Normal"
                    subscriptionHistory!!.price = 0f
                    subscriptionHistory!!.end = Calendar.getInstance().time
                    subscriptionHistory!!.start = Calendar.getInstance().time
                    mainViewModel.updateSubscriptionHistory(subscriptionHistory)
                }
            }
        }
    }

    /*private fun showBottomBar(it: Boolean) {
        if (!it) {
            val animateHide = ObjectAnimator.ofFloat(
                binding.bottomBar, "translationY",
                0f, binding.bottomBar.height.toFloat()
            ).apply {
                this.duration = duration
            }

            animateHide.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    binding.bottomBar.visibility=View.GONE
                }
            })

            animateHide.start()
            binding.bottomBar.visibility = View.GONE
        } else {
            binding.bottomBar.visibility = View.VISIBLE
            val animateShow = ObjectAnimator.ofFloat(
                binding.bottomBar, "translationY",
                binding.bottomBar.height.toFloat(), 0f
            ).apply {
                this.duration = duration
            }

            animateShow.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                }
            })

            animateShow.start()
        }
    }*/

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Xác nhận")
        builder.setMessage("Bạn có muốn thoát ứng dụng?")
        builder.setPositiveButton("Có") { _, _ ->
            finish()
        }
        builder.setNegativeButton("Không") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun loadSignInAccount() {
        val currentAccountID = sharedPreferences.getInt(AppInstance.ACCOUNT_ID_KEY.toString(), -1)

        if (currentAccountID != -1) {
            mainViewModel.findAccountByID(currentAccountID)
            mainViewModel.findAccountSubscription(currentAccountID)

        } else {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun songControlSwipeDown() {
        val gestureDetector =
            GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {
                    return false
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    val distanceY = e2.y - e1!!.y
                    if (distanceY > 100) {
                        val animateHide = ObjectAnimator.ofFloat(
                            binding.songControl, "translationY",
                            0f, binding.songControl.height.toFloat()
                        ).apply {
                            this.duration = duration
                        }
                        animateHide.start()
                        animateHide.doOnEnd {
                            binding.songControl.visibility = View.GONE
                            binding.songControl.translationY = 0f
                            musicService?.mediaPlayer?.pause()
                            musicService?.btnPlayClick?.postValue(Unit)
                            musicService?.cancelNotification()
                        }
                    }
                    return false
                }
            })

        binding.songControl.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        binding.songControl.setOnClickListener {
            mainViewModel.addState(MainViewModel.Companion.State.ReadBook)
        }

    }


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val description = "This is Alarm Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AlarmReceiver.CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmManager.cancel(pendingIntent)
    }

    private fun setUpAlarm() {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // Nếu thời gian đã qua, đặt cho ngày tiếp theo
        if (System.currentTimeMillis() > calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

//        For Test Only
        val calendarTest = Calendar.getInstance()
        calendarTest.add(Calendar.SECOND, 5)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendarTest.timeInMillis,
            10000,
            pendingIntent
        )

        val intervalMillis = 24 * 60 * 60 * 1000 // 24 giờ
        try {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

        } catch (er: SecurityException) {
            Log.e("ERROR", "Error from setUpAlarm")
        }


        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val setTime = dateFormat.format(calendar.time)
        Log.i("DEBUG", "Thời gian set: $setTime")


    }

    private fun onBackPressedHandle() {
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val currentState = mainViewModel.appState.value!!.peek()
                if (currentState == MainViewModel.Companion.State.Home ||
                    currentState == MainViewModel.Companion.State.Search ||
                    currentState == MainViewModel.Companion.State.Favorite ||
                    currentState == MainViewModel.Companion.State.User ||
                    currentState == MainViewModel.Companion.State.Subscription
                ) {
                    showExitConfirmationDialog()
                }else if(currentState==MainViewModel.Companion.State.ReadBook){
                    mainViewModel.updateSongControlVisibility(true)
                    mainViewModel.returnLastState()
                }
                else{
                    mainViewModel.returnLastState()
                }
            }
        })
    }

}






