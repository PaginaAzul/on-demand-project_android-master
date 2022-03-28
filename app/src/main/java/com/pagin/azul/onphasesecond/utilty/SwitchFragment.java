package com.pagin.azul.onphasesecond.utilty;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.pagin.azul.R;


/**
 * Created by akamahesh on 11/5/17.
 */

public class SwitchFragment {

    /**
     * Change the current displayed fragment by a new one.
     * - if the fragmebt is in backstack, it will pop it
     * - if the fragment is already displayed (trying to change the fragment with the same), it will not do anything
     *
     * @param fragment        the new fragment display
     * @param saveInBackstack if we want the fragment to be in backstack
     * @param animate         if we want a nice animation or not
     */
    public static void changeFragment(FragmentManager fragmentManager, Fragment fragment, boolean saveInBackstack, boolean animate) {
        String backStateName = fragment.getClass().getName();
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (animate) {
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            transaction.replace(R.id.mainFrame,fragment,backStateName);
            if (saveInBackstack) {
                transaction.addToBackStack(backStateName);
            }

            transaction.commitAllowingStateLoss();


        } catch (IllegalStateException e) {
            //Toaster.toast(e.getMessage());
        }
    }






}
