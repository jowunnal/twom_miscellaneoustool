package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jinproject.twomillustratedbook.Adapter.HomeAdapter
import com.jinproject.twomillustratedbook.Item.HomeItem
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.HomeBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : Fragment() {
    var _binding : HomeBinding ?= null
    val binding get() = _binding!!
    lateinit var navController : NavController
    lateinit var adapter : HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding= HomeBinding.inflate(inflater,container,false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        initRecyclerView()
        adapter.setItemClickListener(object: OnItemClickListener{
            override fun OnHomeItemClick(v: View, pos: Int) {
                when(pos){
                    0->navController.navigate(R.id.action_homeFragment_to_bookType)
                    1->navController.navigate(R.id.action_homeFragment_to_dropListMaps)
                    2->navController.navigate(R.id.action_homeFragment_to_alarm)
                    3->navController.navigate(R.id.action_homeFragment_to_myInfo)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView(){
        binding.homeContainer.layoutManager=GridLayoutManager(activity,2)
        adapter=HomeAdapter()
        binding.homeContainer.adapter=adapter
        val items = ArrayList<HomeItem>()
        items.add(HomeItem(R.drawable.book_icon,"도감"))
        items.add(HomeItem(R.drawable.drop_icon,"드랍탬"))
        items.add(HomeItem(R.drawable.alarm_icon2,"알람"))
        items.add(HomeItem(R.drawable.note_icon,"패치노트&사용법"))
        adapter.addItems(items)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}