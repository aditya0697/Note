package com.example.adityapatel.note;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BaseFragment extends Fragment {

    public ProgressDialog mProgressDialog;

    public void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if( mProgressDialog != null && mProgressDialog.isShowing()){

            mProgressDialog.dismiss();

        }
    }

    public void hideKeyboard(View view){
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
