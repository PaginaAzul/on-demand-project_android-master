package com.pagin.azul.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagin.azul.R;
import com.pagin.azul.activities.HomeMainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pagin.azul.Constant.Constants.kFrom;
import static com.pagin.azul.Constant.Constants.kFromCategory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NormalMyOrderDashboardFragment extends Fragment {

    @BindView(R.id.tv_deliverman)
    TextView deliverman;
    @BindView(R.id.tv_professional)
    TextView tvProfessional;
    @BindView(R.id.tv_pending)
    TextView tvPending;
    @BindView(R.id.tv_active)
    TextView tvActive;
    @BindView(R.id.tv_past)
    TextView tvPast;
    //private String from="delivery";
    private String from="professional";
    private String fromCategory="pending";
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setLanguage();
        View view = inflater.inflate(R.layout.fragment_normal_my_order_dashboard, container, false);
        ButterKnife.bind(this, view);

        deliverman.setTextColor(getResources().getColor(R.color.white));
        tvProfessional.setTextColor(getResources().getColor(R.color.colortext));
        bundle=new Bundle();
        NUPendingDeliverPersongFrag fragment = new NUPendingDeliverPersongFrag();
        NUActiveDeliveryPersonFrag fragmentActive = new NUActiveDeliveryPersonFrag();

        if(getArguments()!=null){
            from=getArguments().getString("from");
            fromCategory=getArguments().getString("fromCategory");


            if(from.equalsIgnoreCase("Delivery")){

                deliverman.setTextColor(getResources().getColor(R.color.white));
                tvProfessional.setTextColor(getResources().getColor(R.color.colortext));
                from="delivery";

                if(fromCategory.equalsIgnoreCase("Past")) {

                    tvPending.setTextColor(getResources().getColor(R.color.colortext));
                    tvActive.setTextColor(getResources().getColor(R.color.colortext));
                    tvPast.setTextColor(getResources().getColor(R.color.white));
                    fromCategory="past";
                    NUPastDeliveryPersonFrag  fragmentss = new NUPastDeliveryPersonFrag();
                    bundle.putString(kFrom,from);
                    bundle.putString(kFromCategory,fromCategory);
                    replaceFrag(fragmentss,"certificationFrag",bundle);

                }else if(fromCategory.equalsIgnoreCase("Pending")) {

                    tvPending.setTextColor(getResources().getColor(R.color.white));
                    tvActive.setTextColor(getResources().getColor(R.color.colortext));
                    tvPast.setTextColor(getResources().getColor(R.color.colortext));
                    fromCategory="pending";
                    fragment = new NUPendingDeliverPersongFrag();
                    bundle.putString(kFrom,from);
                    bundle.putString(kFromCategory,fromCategory);
                    replaceFrag(fragment,fragment.getTag(),bundle);

                }else {

                    tvPending.setTextColor(getResources().getColor(R.color.colortext));
                    tvActive.setTextColor(getResources().getColor(R.color.white));
                    tvPast.setTextColor(getResources().getColor(R.color.colortext));
                    fromCategory = "active";
                    NUActiveDeliveryPersonFrag fragment1 = new NUActiveDeliveryPersonFrag();
                    bundle.putString(kFrom, from);
                    bundle.putString(kFromCategory, fromCategory);
                    replaceFrag(fragment1, "certificationFrag", bundle);
                }


            }else if(from.equalsIgnoreCase("Professional")){

                tvProfessional.setTextColor(getResources().getColor(R.color.white));
                deliverman.setTextColor(getResources().getColor(R.color.colortext));
                from="professional";

                if(fromCategory.equalsIgnoreCase("Active")) {

                    tvPending.setTextColor(getResources().getColor(R.color.colortext));
                    tvActive.setTextColor(getResources().getColor(R.color.white));
                    tvPast.setTextColor(getResources().getColor(R.color.colortext));
                    fromCategory = "Active";

                    fragmentActive = new NUActiveDeliveryPersonFrag();
                    bundle.putString(kFrom, from);
                    bundle.putString(kFromCategory, fromCategory);
                    replaceFrag(fragmentActive, fragmentActive.getTag(), bundle);


                }else if(fromCategory.equalsIgnoreCase("Past")){
                    tvPending.setTextColor(getResources().getColor(R.color.colortext));
                    tvActive.setTextColor(getResources().getColor(R.color.colortext));
                    tvPast.setTextColor(getResources().getColor(R.color.white));
                    fromCategory="past";
                    NUPastDeliveryPersonFrag  fragmentss = new NUPastDeliveryPersonFrag();
                    bundle.putString(kFrom,from);
                    bundle.putString(kFromCategory,fromCategory);
                    replaceFrag(fragmentss,"certificationFrag",bundle);


                }else {

                    tvPending.setTextColor(getResources().getColor(R.color.white));
                    tvActive.setTextColor(getResources().getColor(R.color.colortext));
                    tvPast.setTextColor(getResources().getColor(R.color.colortext));
                    fromCategory="pending";

                    fragment = new NUPendingDeliverPersongFrag();
                    bundle.putString(kFrom,from);
                    bundle.putString(kFromCategory,fromCategory);
                    replaceFrag(fragment,fragment.getTag(),bundle);

                }

            }

        }else {
            if (fragment != null) {
                //bundle.putString(kFrom,"delivery");
                bundle.putString(kFrom,"professional");
                bundle.putString(kFromCategory,"pending");
                replaceFrag(fragment,"certificationFrag",bundle);
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));

            } else {
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,"certificationFrag",bundle);
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
            }
        }

        return view;

    }
    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
    @OnClick({R.id.tv_deliverman, R.id.tv_professional, R.id.tv_pending, R.id.tv_active, R.id.tv_past})
    void onClick(View v) {
        Fragment fragment=null;
        switch (v.getId()) {
            case R.id.tv_deliverman:
                deliverman.setTextColor(getResources().getColor(R.color.white));
                tvProfessional.setTextColor(getResources().getColor(R.color.colortext));
                from="delivery";

                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.colortext));
                tvPast.setTextColor(getResources().getColor(R.color.colortext));
                fromCategory="pending";
                fragment = new NUPendingDeliverPersongFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,fragment.getTag(),bundle);
                break;
            case R.id.tv_professional:
                tvProfessional.setTextColor(getResources().getColor(R.color.white));
                deliverman.setTextColor(getResources().getColor(R.color.colortext));
                from="professional";

                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.colortext));
                tvPast.setTextColor(getResources().getColor(R.color.colortext));
                fromCategory="pending";
                fragment = new NUPendingDeliverPersongFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,fragment.getTag(),bundle);
                break;
            case R.id.tv_pending:
                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvActive.setTextColor(getResources().getColor(R.color.colortext));
                tvPast.setTextColor(getResources().getColor(R.color.colortext));
                fromCategory="pending";
                fragment = new NUPendingDeliverPersongFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,fragment.getTag(),bundle);
                break;
            case R.id.tv_active:
                tvPending.setTextColor(getResources().getColor(R.color.colortext));
                tvActive.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.colortext));
                fromCategory="active";
                 fragment = new NUActiveDeliveryPersonFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,"certificationFrag",bundle);
                break;
            case R.id.tv_past:
                tvPending.setTextColor(getResources().getColor(R.color.colortext));
                tvActive.setTextColor(getResources().getColor(R.color.colortext));
                tvPast.setTextColor(getResources().getColor(R.color.white));
                fromCategory="past";
               fragment = new NUPastDeliveryPersonFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,"certificationFrag",bundle);
                break;
            default:
                fragment = new NUPendingDeliverPersongFrag();
                bundle.putString(kFrom,from);
                bundle.putString(kFromCategory,fromCategory);
                replaceFrag(fragment,"certificationFrag",bundle);
                tvActive.setTextColor(getResources().getColor(R.color.blacklight));
                tvPending.setTextColor(getResources().getColor(R.color.white));
                tvPast.setTextColor(getResources().getColor(R.color.blacklight));
                fromCategory="pending";
                break;
        }
    }


    private void firstView(){
        deliverman.setTextColor(getResources().getColor(R.color.white));
        tvProfessional.setTextColor(getResources().getColor(R.color.colortext));
    }

    private void replaceFrag(Fragment frag, String nameTag, Bundle bundle) {
        if (bundle != null)
            frag.setArguments(bundle);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.childContainer, frag, nameTag)
                .addToBackStack(nameTag)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            ((HomeMainActivity)getActivity()).tv_title.setVisibility(View.VISIBLE);
            ((HomeMainActivity)getActivity()).tv_title.setText(R.string.nav_my_order);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*private void setLanguage() {
        // check language from data base
        if(SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.IS_LAN_SAVE).equalsIgnoreCase("true")){
            if(SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.LANGUAGE).equalsIgnoreCase(Constants.kEnglish)){
                LocaleHelper.setLocale(getActivity(),"en");
            }else {
                LocaleHelper.setLocale(getActivity(), "pt");
            }
        }else {
            LocaleHelper.setLocale(getActivity(),"pt");
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.LANGUAGE, Constants.kPortuguese);
            SharedPreferenceWriter.getInstance(getActivity()).writeStringValue(SharedPreferenceKey.IS_LAN_SAVE,"true");
        }
    }*/



}
