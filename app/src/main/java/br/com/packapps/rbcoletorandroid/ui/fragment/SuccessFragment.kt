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

class SuccessFragment : DialogFragment() {

    private var title: String? = null
    private var message: String? = null

    private var listener : OnSuccessFragmentListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(Const.TITLE)
            message = it.getString(Const.MESSAGE)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.success_fragment, container, false)
        var tvTitle = view.findViewById<TextView>(R.id.title_success_tv)
        var tvMessage = view.findViewById<TextView>(R.id.description_success_tv)
        tvTitle.text = title
        tvMessage.text = message

        view.findViewById<Button>(R.id.success_btn).setOnClickListener {
            dismiss()
            listener!!.onActionConfirmFromSuccessFragment()

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSuccessFragmentListener) {
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
            SuccessFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.TITLE, title)
                    putString(Const.MESSAGE, message)
                }
            }
    }



    interface OnSuccessFragmentListener {
        fun onActionConfirmFromSuccessFragment()
    }




}