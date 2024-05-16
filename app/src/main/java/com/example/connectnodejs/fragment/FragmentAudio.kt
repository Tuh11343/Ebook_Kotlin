package com.example.connectnodejs.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.connectnodejs.MainActivity
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.FragmentAudioBinding
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.model.Lyric
import com.example.connectnodejs.service.MusicService
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.MainViewModel
import com.example.connectnodejs.viewmodels.AudioViewModel
import java.util.Scanner


class FragmentAudio(
) : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private var handler = Handler(Looper.getMainLooper())
    private var animatorSet = AnimatorSet()
    private var rotation: Float = 0.0f
    private lateinit var songViewModel: AudioViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activity: MainActivity
    private var musicService: MusicService? = null
    private lateinit var selectedBook:Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpUI()
    }

    private fun setUpViewModel(){
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        songViewModel = ViewModelProvider(requireActivity())[AudioViewModel::class.java]
        activity = requireActivity() as MainActivity
        musicService = activity.getMusicService()
        observe()
    }

    private fun setUpMusicService() {
        musicService!!.load(selectedBook.src_audio)
    }

    
    private fun setUpUI(){

        binding.btnPlay.setOnClickListener {
            btnPlayClick()
        }

        binding.btnRewind.setOnClickListener {
            btnRewindClick(2000)
        }

        binding.btnFastForward.setOnClickListener {
            btnFFClick(2000)
        }

        binding.btnRepeat.setOnClickListener {
            musicService!!.seekTo(0)
            updateCurrentTime()
        }

        binding.btnDown.setOnClickListener {
            btnDownClick()
        }

        musicService!!.btnPlayClick.observe(viewLifecycleOwner) {
            if (!musicService!!.mediaPlayer!!.isPlaying) {
                pauseAnimation()
                stopSeekBar()
                binding.btnPlay.setImageResource(R.drawable.icon_play)
            } else {
                startAnimation()
                updateSeekBar()
                binding.btnPlay.setImageResource(R.drawable.icon_pause)
            }
        }

        musicService!!.btnRewindClick.observe(viewLifecycleOwner) {
            btnRewindClick(2000)
        }

        musicService!!.btnFastForwardClick.observe(viewLifecycleOwner) {
            btnFFClick(2000)
        }

        musicService!!.seekBarSlide.observe(viewLifecycleOwner) {
            updateCurrentTime()
        }

        musicService!!.btnCloseClick.observe(viewLifecycleOwner){
            pauseAnimation()
            stopSeekBar()
            binding.btnPlay.setImageResource(R.drawable.icon_play)
            musicService!!.mediaPlayer!!.pause()
        }
    }

    private fun observe() {
        observeResetAudio()
        observeMediaLoadIsDone()
        observeBookAuthor()
    }

    private fun observeBookAuthor() {
        songViewModel.bookAuthorName.observe(viewLifecycleOwner) {
            AppInstance.bookAuthorName = it
            AppInstance.bookName = selectedBook!!.name
            AppInstance.bookImg = selectedBook!!.image
        }
    }

    private fun observeMediaLoadIsDone() {
        musicService!!.loadIsDone.observe(viewLifecycleOwner) {

            binding.seekBar.max = musicService!!.mediaPlayer!!.duration
            binding.totalTime.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            binding.currentTime.text = "00:00"

            loadLyrics(selectedBook?.lyric)
            setUpProgressBar()
        }
    }

    private fun observeResetAudio(){
        mainViewModel.resetAudio.observe(activity) {

            //--------------------------- Reset Media Player Component --------------//
            selectedBook=mainViewModel.getSelectedBook()
            binding.seekBar.max = 0
            binding.totalTime.text = "00:00"
            binding.currentTime.text = "00:00"

            Glide.with(requireContext())
                .load(selectedBook.image)
                .placeholder(R.drawable.song_circle)
                .error(R.drawable.song_circle)
                .centerCrop()
                .into(binding.bookImg)

            binding.bookName.text = selectedBook.name
            //--------------------------- ------------------------------------ --------------//

            songViewModel.findBookAuthorName(selectedBook.id)

            resetAnimation()
            setUpMusicService()

        }
    }

    private fun loadLyrics(lyric: String?) {

        /*var lyricsList = mutableListOf<Lyric>()
        val inputStream =
            resources.openRawResource(R.raw.test) // assuming your lyrics file is in the raw folder
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {

            val timeString: String = line.substring(line.indexOf("[") + 1, line.indexOf("]"))
            val text: String = line.substring(line.indexOf("]") + 1).trim()
            val time = parseTimeToMillis(timeString)

            lyricsList.add(Lyric(time, text))
            line = reader.readLine()
        }
        reader.close()*/

        var lyricsList = mutableListOf<Lyric>()
        val lyricsString = lyric ?: ""
        if (lyricsString.isNotBlank()) {
            val scanner = Scanner(lyricsString)
            while (scanner.hasNextLine()) {
                val line: String = scanner.nextLine()

                val timeString: String = line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                val text: String = line.substring(line.indexOf("]") + 1).trim()
                val time = parseTimeToMillis(timeString)

                lyricsList.add(Lyric(time, text))
            }
            scanner.close()

            songViewModel.updateLyricList(lyricsList)

            //Update total Lyrics
            val lyricsBuilder = StringBuilder()
            for (lyric in lyricsList) {
                lyricsBuilder.append("${lyric.content}\n")
            }

            songViewModel.updateTotalLyrics(lyricsBuilder.toString())
        } else {
            songViewModel.updateLyricList(mutableListOf())
            songViewModel.updateTotalLyrics("")
        }

    }


    private fun parseTimeToMillis(time: String): Long {
        val minutes = time.substring(0, 2).toLong()
        val seconds = time.substring(3, 5).toLong()
        val millis = time.substring(6, 8).toLong() * 10 // convert centiseconds to milliseconds
        return (minutes * 60 + seconds) * 1000 + millis
    }

    private fun startAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.bookImg, "rotation", rotation, rotation + 360)
        animator.duration = 10000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = Animation.INFINITE
        animatorSet.play(animator)
        animatorSet.start()
    }

    private fun pauseAnimation() {
        rotation = binding.bookImg.rotation
        animatorSet.cancel()
    }

    private fun resetAnimation() {
        binding.bookImg.rotation = 0f
        rotation = 0f
    }

    private fun updateSeekBar() {
        val updateSeekBar = object : Runnable {
            override fun run() {

                updateCurrentTime()

                val currentTime = musicService!!.mediaPlayer?.currentPosition?.toLong()
                val lyricList = songViewModel.lyricList.value!!
                if (lyricList.isEmpty()) {

                } else {
                    for (i in 0 until lyricList.size - 1) {
                        val currentKey = lyricList[i].time
                        val nextKey = lyricList[i + 1].time
                        if (nextKey != null) {
                            if (currentTime!! >= currentKey && currentTime < nextKey) {
                                if (songViewModel.currentLyric.value != lyricList[i].content) {
                                    var start = 0
                                    for ((index, lyric) in lyricList.withIndex()) {
                                        if (lyric.time < currentTime) {
                                            start += lyric.content.length + 1
                                            Log.i("Test", "Time+:${lyric.time}")
                                        } else {
                                            Log.i("Test", "StartProgress:${start}")
                                            if (index >= 1)
                                                start -= lyricList[index - 1].content.length + 1
                                            break
                                        }
                                    }
                                    songViewModel.updateStart(start)
                                    songViewModel.updateLyric(lyricList[i].content)

                                }
                            }
                        }
                    }
                }

                handler.postDelayed(this, 500) // Cập nhật mỗi giây
            }
        }
        handler.post(updateSeekBar)
    }

    private fun stopSeekBar() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun btnPlayClick() {
        try {
            if(musicService!!.loadIsDone.value==true){
                if (musicService!!.mediaPlayer!!.isPlaying) {
                    pauseAnimation()
                    stopSeekBar()
                    binding.btnPlay.setImageResource(R.drawable.icon_play)
                } else {

                    startAnimation()
                    updateSeekBar()
                    binding.btnPlay.setImageResource(R.drawable.icon_pause)
                }

                musicService!!.play()
            }else{
                Toast.makeText(requireContext(),"Trình phát chưa sẵn sàng",Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.i("ERROR", "Error:${e}")
        }
    }

    private fun btnRewindClick(time: Int) {
        musicService!!.rewind(2000)
        updateCurrentTime()
    }

    private fun btnFFClick(time: Int) {
        musicService!!.fastForward(2000)
        updateCurrentTime()
    }

    private fun updateCurrentTime() {
        binding.seekBar.progress = musicService!!.mediaPlayer!!.currentPosition
        binding.currentTime.text =
            formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
    }

    private fun formatDuration(durationInMillis: Long): String {
        val seconds = durationInMillis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun setUpProgressBar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                musicService!!.seekTo(seekBar!!.progress)
                updateCurrentTime()
            }
        })
    }

    private fun btnDownClick() {
        mainViewModel.returnLastState()
        mainViewModel.updateBottomBarVisibility(true)
        mainViewModel.updateSongControlVisibility(true)
    }

}


