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
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.adapter.BookViewNormalAdapter
import com.example.connectnodejs.databinding.FragmentAuthorBinding
import com.example.connectnodejs.listener.IBookListener
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.AuthorViewModel
import com.example.connectnodejs.viewmodels.MainViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper


class FragmentAuthor(
) : Fragment() {

    private lateinit var binding: FragmentAuthorBinding
    private lateinit var authorViewModel: AuthorViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var selectedAuthor: Author

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpUI()
    }

    private fun setUpViewModel(){
        authorViewModel = ViewModelProvider(requireActivity())[AuthorViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        observe() 
    }

    private fun setUpUI(){
        binding.btnBack.setOnClickListener{
            mainViewModel.returnLastState()
        }
    }

    private fun resetUI(){
        selectedAuthor=mainViewModel.getSelectedAuthor()
        authorViewModel.findBookList(selectedAuthor.id!!)

        Glide.with(requireContext())
            .load(selectedAuthor.image)
            .placeholder(R.drawable.song_circle)
            .error(R.drawable.song_circle)
            .into(binding.authorImg)

        binding.authorName.text=selectedAuthor.name
        binding.authorDescription.text=selectedAuthor.description
    }
   

    private fun observe() {
        observeAppState()
        observeBookList()
    }

    private fun observeAppState(){
        mainViewModel.appState.observe(viewLifecycleOwner){ state->
            if(state.peek()==MainViewModel.Companion.State.Author){
                resetUI()
            }
        }
    }
    
    private fun observeBookList() {
        authorViewModel.bookList.observe(viewLifecycleOwner) { bookList ->

            binding.bookAmount.text="Số lượng sách: ${bookList.size}"

            binding.bookList.adapter =
                BookViewNormalAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            val snapperWithCard = GravitySnapHelper(Gravity.START)
            snapperWithCard.attachToRecyclerView(binding.bookList)

            binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if(authorViewModel.bookList.value!!.isNotEmpty()){
                        if(position==0){
                            authorViewModel.bookList.value!!.sortByDescending { it.id }
                        }else if(position==1){
                            authorViewModel.bookList.value!!.sortByDescending { it.rating }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Xử lý khi không có mục nào được chọn
                }
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


