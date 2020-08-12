package com.andrius.notesappdemo.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrius.notesappdemo.R
import com.andrius.notesappdemo.ui.IMenuListener
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment: Fragment() {

    lateinit var menuCallback: IMenuListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        menuCallback = context as IMenuListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        menuCallback.hideMenuItems()
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().setTitle(R.string.about_text)

        btn_github.setOnClickListener {
            openWebsite("https://github.com/CPlusPlusCompiler?tab=repositories")
        }

        btn_linkedin.setOnClickListener {
            openWebsite("https://www.linkedin.com/in/andrius-dara%C5%A1kevi%C4%8Dius-69a935130/")
        }

        btn_portfolio.setOnClickListener {

        }
    }

    private fun openWebsite(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun onDetach() {
        menuCallback.showMenuItems()
        super.onDetach()
    }

}