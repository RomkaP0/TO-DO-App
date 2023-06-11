package com.romka_po.to_doapp.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.romka_po.to_doapp.R
import com.romka_po.to_doapp.databinding.FragmentTodoListBinding
import com.romka_po.to_doapp.model.TodoListAdapter

class TodoListFragment : Fragment() {
    lateinit var rvAdapter: TodoListAdapter

    private lateinit var binding: FragmentTodoListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTodoListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(binding.todoRecyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        rvAdapter = TodoListAdapter()
        with(recyclerView) {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}