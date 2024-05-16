package com.example.connectnodejs.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.databinding.FragmentLyricsBinding
import com.example.connectnodejs.viewmodels.AudioViewModel


class FragmentLyric : Fragment() {

    private lateinit var binding: FragmentLyricsBinding
    private lateinit var songViewModel: AudioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLyricsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songViewModel = ViewModelProvider(requireActivity())[AudioViewModel::class.java]
        observeTotalLyrics()
        observeCurrentLyrics()
    }

    private fun observeTotalLyrics() {
        songViewModel.totalLyrics.observe(viewLifecycleOwner) { totalLyrics ->
            Log.i("Test", "Cập nhật toàn bộ lời bài hát")
            binding.lyricsTextView.text = totalLyrics.toString()
        }
    }

    private fun observeCurrentLyrics() {
        songViewModel.currentLyric.observe(viewLifecycleOwner) { currentLyric ->

            if(currentLyric.isBlank()){
                binding.lyricsTextView.text="Hiện tại không có lời cho sách này !!!"
            }else{
                try {
                    binding.lyricsTextView.text = songViewModel.totalLyrics.value

                    val start = songViewModel.start.value!!
                    val end = start + currentLyric.length
                    val spannableLyrics = SpannableString(songViewModel.totalLyrics.value)
                    spannableLyrics.setSpan(
                        ForegroundColorSpan(Color.RED),
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    binding.lyricsTextView.text = spannableLyrics

                    // Tính toán vị trí cuộn
                    val scrollY = binding.lyricsTextView.layout.getLineTop(
                        binding.lyricsTextView.layout.getLineForOffset(end)
                    ) - (binding.scrollView.height - binding.lyricsTextView.lineHeight) / 2
                    // Cuộn đến vị trí
                    binding.scrollView.smoothScrollTo(0, scrollY)
                } catch (e: Exception) {
                    Log.i("ERROR", "Loi:${e}")
                }
            }
        }
    }

}