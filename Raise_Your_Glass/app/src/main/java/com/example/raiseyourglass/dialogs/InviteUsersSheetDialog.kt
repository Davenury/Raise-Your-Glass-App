package com.example.raiseyourglass.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.raiseyourglass.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InviteUsersSheetDialog : BottomSheetDialogFragment() {
    companion object{
        const val TAG = "InviteUsersBottomSheetDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.invite_users_bottom_sheet,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //all of these shits that need to be here
    }
}