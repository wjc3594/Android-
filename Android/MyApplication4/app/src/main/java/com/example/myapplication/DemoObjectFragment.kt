package com.example.myapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

const val ARG_OBJECT = "object"

class DemoObjectFragment : Fragment() {

    companion object {
        fun newInstance() = DemoObjectFragment()
    }

    private lateinit var viewModel: DemoObjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(
            "wjc", "demo-object-oncreate-pos=${
                arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.getInt(
                    ARG_OBJECT
                )
            }"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val tv = view.findViewById<TextView>(R.id.text1)
            tv.text = getInt(ARG_OBJECT).toString()
            tv.setOnClickListener {
                Toast.makeText(activity,"点击",Toast.LENGTH_SHORT).show()
            }
            tv.setOnLongClickListener{
                Toast.makeText(activity,"长按",Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener true
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DemoObjectViewModel::class.java]
        // TODO: Use the ViewModel
    }

}