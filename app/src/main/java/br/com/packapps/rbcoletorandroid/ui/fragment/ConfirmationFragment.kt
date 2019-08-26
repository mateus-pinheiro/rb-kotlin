package br.com.packapps.rbcoletorandroid.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import br.com.packapps.rbcoletorandroid.R
import br.com.packapps.rbcoletorandroid.util.Const
import kotlinx.android.synthetic.main.fail_fragment.*

class ConfirmationFragment : DialogFragment() {


    private var title: String? = null
    private var message: String? = null

    private var listener : OnConfirmationFragmentListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(Const.TITLE)
            message = it.getString(Const.MESSAGE)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.send_confirmation_fragment, container, false)
        var tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        var tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        tvTitle.text = title
        tvMessage.text = message

        view.findViewById<Button>(R.id.yes_confirmation_btn).setOnClickListener {
            dismiss()
            listener!!.onActionConfirmFromConfirmationFragment()

        }

        view.findViewById<Button>(R.id.no_confirmation_btn).setOnClickListener {
            //Fazer post (envio de finalização para api da RB)
            dismiss()
            listener!!.onActionCancelFromConfirmationFragment()

        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnConfirmationFragmentListener) {
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
            ConfirmationFragment().apply {
                arguments = Bundle().apply {
                    putString(Const.TITLE, title)
                    putString(Const.MESSAGE, message)
                }
            }
    }



    interface OnConfirmationFragmentListener {
        fun onActionConfirmFromConfirmationFragment()
        fun onActionCancelFromConfirmationFragment()
    }


}