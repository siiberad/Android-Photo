package com.siiberad.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : Fragment() {

    lateinit var vm: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (activity != null) {
            vm = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        }
        (activity as MainActivity?)?.showFAB()
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getData().observe(viewLifecycleOwner, {
            val pathAdapter = PathAdapter(it)
            pathAdapter.mOnItemClickListener = object : PathAdapter.OnItemClickListener {
                override fun onClick(path: String) {
                    vm.pathUri.postValue(path)
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }

            }
            rv_photo.apply {
                layoutManager = GridLayoutManager(context, 3)
                pathAdapter.notifyDataSetChanged()
                adapter = pathAdapter
            }
        })

    }
}