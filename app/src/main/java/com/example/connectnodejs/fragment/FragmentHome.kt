package com.example.connectnodejs.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.connectnodejs.adapter.BookViewNormalAdapter
import com.example.connectnodejs.adapter.BookWithBigCardAdapter
import com.example.connectnodejs.adapter.BookWithMultiAdapter
import com.example.connectnodejs.adapter.ContinueBookAdapter
import com.example.connectnodejs.adapter.HomeAuthorAdapter
import com.example.connectnodejs.databinding.FragmentHomeBinding
import com.example.connectnodejs.listener.IAuthorListener
import com.example.connectnodejs.listener.IBookListener
import com.example.connectnodejs.listener.IMultiBookListener
import com.example.connectnodejs.model.Author
import com.example.connectnodejs.model.Book
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.HomeViewModel
import com.example.connectnodejs.viewmodels.MainViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper


class FragmentHome() : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpUI()
    }

    private fun setUpViewModel(){
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        observe()
    }

    private fun setUpUI(){
        homeViewModel.findAllBookList(12, 0)
        homeViewModel.findAllAuthorList(12, 0)
        homeViewModel.findNormalBook(12, 0)
        homeViewModel.findPremiumBook(10, 0)
        homeViewModel.findTopRatingBook(10,0)
    }

    private fun observe() {
        observeAppState()
        observeBigCardBookList()
        observeAuthorList()
    }

    private fun observeAppState(){
        mainViewModel.appState.observe(viewLifecycleOwner){ state->
//            setUpUI()
        }
    }

    private fun observeBigCardBookList() {
        homeViewModel.bigCardBookList.observe(viewLifecycleOwner) { bookList ->
            binding.bigCardBookList.adapter =
                BookWithBigCardAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            binding.bigCardBookList.setIntervalRatio(0.8f)
            binding.bigCardBookList.setInfinite(true)

            //------------------------------------------------------------------------------------//

            binding.multiBookList.adapter =
                BookViewNormalAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            val snapperWithCard = GravitySnapHelper(Gravity.START)
            snapperWithCard.attachToRecyclerView(binding.multiBookList)

        }

        //------------------------------------------------------------------------------------//
        homeViewModel.normalBookList.observe(viewLifecycleOwner){ bookList->

            binding.freeBookList.adapter =
                BookWithMultiAdapter(bookList, object : IMultiBookListener {
                    override fun onFirstBookClick(book: Book) {
                        bookClickHandle(book)
                    }

                    override fun onSecondBookClick(book: Book) {
                        bookClickHandle(book)
                    }


                })
            val snapper = GravitySnapHelper(Gravity.START)
            snapper.attachToRecyclerView(binding.freeBookList)
        }

        //------------------------------------------------------------------------------------//
        homeViewModel.premiumBookList.observe(viewLifecycleOwner) { bookList ->
            binding.premiumBookList.adapter =
                BookViewNormalAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            val snapperWithCard = GravitySnapHelper(Gravity.START)
            snapperWithCard.attachToRecyclerView(binding.premiumBookList)
        }

        //------------------------------------------------------------------------------------//
        homeViewModel.topRatingBookList.observe(viewLifecycleOwner){bookList->

            binding.topRatingBookList.adapter =
                ContinueBookAdapter(bookList, object : IBookListener {
                    override fun onBookClick(book: Book) {
                        //Check if book premium
                        bookClickHandle(book)
                    }

                })
            val snapperContinueBook = GravitySnapHelper(Gravity.START)
            snapperContinueBook.attachToRecyclerView(binding.topRatingBookList)

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

    private fun observeAuthorList() {
        homeViewModel.authorList.observe(viewLifecycleOwner) { authorList ->

            binding.authorList.adapter = HomeAuthorAdapter(authorList, object : IAuthorListener {
                override fun onAuthorClick(author: Author) {
                    mainViewModel.addSelectedAuthor(author)
                    mainViewModel.addState(MainViewModel.Companion.State.Author)
                }

            })
            val authorSnapper = GravitySnapHelper(Gravity.START)
            authorSnapper.attachToRecyclerView(binding.authorList)

        }
    }

}