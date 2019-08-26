package br.com.packapps.rbcoletorandroid.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.util.Const

class FailFragment : DialogFragment() {

    private var title: String? = null
    private var message: String? = null

    private var listener : OnFailFragmentListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(Const.TITLE)
            message = it.getString(Const.MESSAGE)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fail_fragment, container, false)
        view.findViewById<TextView>(R.id.tvTitle).text = title?:""
        view.findViewById<TextView>(R.id.tvMessage).text = message?:""

        view.findViewById<Button>(R.id.fail_btn).setOnClickListener {
            dismiss()
            listener!!.onActionFromFailFragment()
        }


        return view

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFailFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        @JvmStatic
        fun newInstance(title: String, message : String) =
            FailFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.TITLE, title)
                    putString(Const.MESSAGE, message)
                }
            }
    }



    interface OnFailFragmentListener {
        fun onActionFromFailFragment()
    }


}