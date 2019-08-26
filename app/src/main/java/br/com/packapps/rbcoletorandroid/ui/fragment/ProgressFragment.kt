package br.com.packapps.rbcoletorandroid.ui.fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.util.Const


class ProgressFragment : DialogFragment() {
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString(Const.MESSAGE)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_progress, container, false)

        if (message!= null)
            view.findViewById<TextView>(R.id.tvMessageProgress)

        return view
    }


    override fun isCancelable(): Boolean {
        return false
    }


    companion object {
        @JvmStatic
        fun newInstance(message: String?) =
            ProgressFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.MESSAGE, message)
                }
            }
    }
}
