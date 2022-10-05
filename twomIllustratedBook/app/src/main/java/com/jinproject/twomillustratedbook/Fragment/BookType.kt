package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.BookTypeAdapter
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.BookTypeBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.viewModel.DropListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookType : BindFragment<BookTypeBinding>(R.layout.book_type,true) {
    val bookViewModel: DropListViewModel by activityViewModels()
    val adapter:BookTypeAdapter by lazy{BookTypeAdapter(requireActivity())}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookTypeRecyclerView.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.bookTypeRecyclerView.adapter=adapter
        binding.dropListViewModel=bookViewModel

        var list=ArrayList<String>().apply {
            add("miscellaneous")
            add("weapons")
            add("Armors")
            add("costumes")
            add("skill")
        }
        adapter.setItem(list)
        adapter.setOnItemClickListener(object:OnItemClickListener{
            override fun OnHomeItemClick(v: View, pos: Int) {
                bookViewModel.dataItemType=adapter.getItem(pos)
                Navigation.findNavController(view).navigate(R.id.action_bookType_to_bookMain)
            }
        })

    }
}