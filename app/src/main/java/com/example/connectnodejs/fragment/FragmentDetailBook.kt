package com.example.connectnodejs.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.adapter.BookDetailGenreAdapter
import com.example.connectnodejs.adapter.BookViewNormalAdapter
import com.example.connectnodejs.adapter.HomeAuthorAdapter
import com.example.connectnodejs.databinding.FragmentDetailBookBinding
import com.example.connectnodejs.listener.IAuthorListener
import com.example.connectnodejs.listener.IBookListener
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.BookDetailViewModel
import com.example.connectnodejs.viewmodels.MainViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper


class FragmentDetailBook(
) : Fragment() {

    private lateinit var binding: FragmentDetailBookBinding
    private lateinit var bookDetailViewModel: BookDetailViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var selectedBook:Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpUI()
    }
    
    private fun setUpViewModel(){
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        bookDetailViewModel = ViewModelProvider(requireActivity())[BookDetailViewModel::class.java]
        
        observe()
    }
    
    private fun setUpUI(){

        binding.btnFavorite.setOnClickListener {

            if (AppInstance.currentAccount == null) {
                Toast.makeText(
                    requireContext(),
                    "Bạn cần đăng nhập để sử dụng tính năng này",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                bookDetailViewModel.favoriteClick(
                    AppInstance.currentAccount!!.user_id,
                    selectedBook.id
                )
            }
        }

        binding.btnBack.setOnClickListener {
            mainViewModel.returnLastState()
        }

        binding.playClickView.setOnClickListener {
            mainViewModel.resetAudio()
            mainViewModel.addState(MainViewModel.Companion.State.ReadBook)
        }

    }

    private fun resetUI(){
        binding.scrollView.scrollTo(0,0)
        selectedBook=mainViewModel.getSelectedBook()

        binding.bookName.text = selectedBook.name
        binding.bookDescription.text = selectedBook.description ?: "Không có thông tin về sách"
        Glide.with(requireContext())
            .load(selectedBook.image)
            .placeholder(R.drawable.song_circle)
            .error(R.drawable.song_circle)
            .into(binding.bookImg)

        bookDetailViewModel.findAuthorsByBookID(selectedBook.id, 10, 0)
        bookDetailViewModel.findGenresByBookID(selectedBook.id, 100, 0)
    }

    private fun observe() {
        observeAuthors()
        observeGenres()
        observeAuthorBookList()
        observeFavoriteClick()
        observeAppState()
    }

    private fun observeAppState(){
        mainViewModel.appState.observe(viewLifecycleOwner){ state->
            if(state.peek()==MainViewModel.Companion.State.DetailBook){
                resetUI()
            }
        }
    }
    
    private fun observeAuthors() {
        bookDetailViewModel.authorList.observe(viewLifecycleOwner) { authorList ->
            binding.authorName.text = authorList.joinToString(separator = ",") { it.name }

            binding.authorList.adapter = HomeAuthorAdapter(authorList,object: IAuthorListener {
                override fun onAuthorClick(author: Author) {
                    mainViewModel.addSelectedAuthor(author)
                    mainViewModel.addState(MainViewModel.Companion.State.Author)
                }

            })
            val authorSnapper = GravitySnapHelper(Gravity.START)
            authorSnapper.attachToRecyclerView(binding.authorList)

        }
    }

    private fun observeGenres() {
        bookDetailViewModel.genreList.observe(viewLifecycleOwner) { genreList ->
            binding.genreList.adapter = BookDetailGenreAdapter(genreList)
            val genreListSnapper = GravitySnapHelper(Gravity.START)
            genreListSnapper.attachToRecyclerView(binding.genreList)
        }
    }

    private fun observeAuthorBookList() {
        bookDetailViewModel.authorBookList.observe(viewLifecycleOwner) { bookList ->
            binding.authorBookList.adapter =
                BookViewNormalAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })

            val authorBookListSnapper = GravitySnapHelper(Gravity.START)
            authorBookListSnapper.attachToRecyclerView(binding.authorBookList)
        }
    }

    private fun observeFavoriteClick() {
        bookDetailViewModel.favoriteClick.observe(viewLifecycleOwner) { boolean ->

            if(boolean){
                Toast.makeText(requireContext(),"Thêm vào danh sách yêu thích thành công",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Đã xóa khỏi danh sách yêu thích",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bookClickHandle(book: Book) {
        if (book.book_type == Book.BookType.PREMIUM) {
            val currentAccountSubscription = AppInstance.currentSubscription
            if (currentAccountSubscription!!.book_type == Book.BookType.PREMIUM) {
                bookClick(book)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Bạn cần nâng cấp tài khoản để truy cập",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else if (book.book_type == Book.BookType.NORMAL) {
            bookClick(book)
        }
    }

    private fun bookClick(book: Book) {
        mainViewModel.addSelectedBook(book)
        mainViewModel.addState(MainViewModel.Companion.State.DetailBook)
    }

}


