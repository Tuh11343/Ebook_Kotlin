package com.example.connectnodejs.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.adapter.BookViewNormalVerticalAdapter
import com.example.connectnodejs.databinding.FragmentFavoriteBinding
import com.example.connectnodejs.listener.IBookListener
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.FavoriteViewModel
import com.example.connectnodejs.viewmodels.MainViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper


class FragmentFavorite(
) : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
    }

    private fun setUpViewModel(){
        favoriteViewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        observe()
    }

    private fun setUpUI(){

    }

    private fun resetUI(){
        binding.bookAmount.text="0 Sách"
    }

    private fun observe() {
        observeBookList()
        observeAppState()
    }

    private fun observeAppState(){
        mainViewModel.appState.observe(viewLifecycleOwner){ state->
            if(state.peek()==MainViewModel.Companion.State.Favorite){
                resetUI()
                favoriteViewModel.findBookList(AppInstance.currentAccount!!.user_id!!)
            }
        }
    }

    private fun observeBookList() {
        favoriteViewModel.bookList.observe(viewLifecycleOwner) { bookList ->

            binding.bookList.adapter =
                BookViewNormalVerticalAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            val snapperWithCard = GravitySnapHelper(Gravity.START)
            snapperWithCard.attachToRecyclerView(binding.bookList)

            binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(favoriteViewModel.bookList.value!!.isNotEmpty()){
                        if(position==0){
                            favoriteViewModel.bookList.value!!.sortByDescending { it.id }
                        }else if(position==1){
                            favoriteViewModel.bookList.value!!.sortByDescending { it.rating }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Xử lý khi không có mục nào được chọn
                }
            }

            binding.bookAmount.text="${bookList.size} sách"
        }
    }


    private fun bookClickHandle(book: Book) {
        if (book.book_type == Book.BookType.PREMIUM) {
            if(AppInstance.currentAccount==null){
                Toast.makeText(
                    requireContext(),
                    "Bạn cần đăng nhập tài khoản để truy cập",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
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


