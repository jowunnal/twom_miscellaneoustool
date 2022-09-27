package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.BookTypeAdapter
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.BookTypeBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookType : Fragment() {
    var _binding:BookTypeBinding ?=null
    val binding get() = _binding!!
    lateinit var adapter:BookTypeAdapter
    val model:BookViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= BookTypeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookTypeRecyclerView.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        adapter= BookTypeAdapter(requireActivity())
        binding.bookTypeRecyclerView.adapter=adapter
        var list=ArrayList<String>().apply {
            add("miscellaneous")
            add("weapons")
            add("costumes")
        }
        adapter.setItem(list)
        adapter.setOnItemClickListener(object:OnItemClickListener{
            override fun OnHomeItemClick(v: View, pos: Int) {
                model.dataItemType=adapter.getItem(pos)
                Navigation.findNavController(view).navigate(R.id.action_bookType_to_bookMain)
            }
        })
    }
}