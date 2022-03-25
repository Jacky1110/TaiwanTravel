//package com.jotangi.nickyen.BannerList;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.StoreAreaFragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.daimajia.slider.library.SliderLayout;
//import com.daimajia.slider.library.SliderTypes.TextSliderView;
//import com.jotangi.nickyen.R;
//
///**
// * A simple {@link StoreAreaFragment} subclass.
// * Use the {@link HomeSliderFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class  HomeSliderFragment extends StoreAreaFragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private SliderLayout mSliderLayout;
//
//    public HomeSliderFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeSliderFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeSliderFragment newInstance(String param1, String param2) {
//        HomeSliderFragment fragment = new HomeSliderFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        View view = inflater.inflate(R.layout.fragment_home_slider, container, false);
//        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
//
//        initSlider();
//        return view;
//
//    }
//
//    private void initSlider(){
//        TextSliderView textSliderView = new TextSliderView(this.getActivity());
//        textSliderView
//                .image(R.drawable.test_carousel);
//
//        TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
//        textSliderView2
//                .image(R.drawable.test_carousel);
//
//        TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
//        textSliderView3
//                .image(R.drawable.test_carousel);
//
//        mSliderLayout.addSlider(textSliderView);
//        mSliderLayout.addSlider(textSliderView2);
//        mSliderLayout.addSlider(textSliderView3);
//
//        mSliderLayout.setDuration(3000);
//
//    }
//
//}