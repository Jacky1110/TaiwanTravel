package com.jotangi.nickyen.shop;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jotangi.nickyen.AppUtility;
import com.jotangi.nickyen.GridSpacingItemDecoration;
import com.jotangi.nickyen.MainActivity;
import com.jotangi.nickyen.PicassoTrustAll;
import com.jotangi.nickyen.R;
import com.jotangi.nickyen.api.ApiConnection;
import com.jotangi.nickyen.api.ApiConstant;
import com.jotangi.nickyen.model.ShopBean;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.jotangi.nickyen.shop.ShopNewActivity.pages;
import static com.jotangi.nickyen.shop.ShopNewActivity.status;

public class LivelihoodFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
{
    private GoogleMap mMap;
    private MapView mapView;
    private ProgressBar progressBar;

    private ViewPager viewPager;
    private ShopViewPagerAdapter shopViewPagerAdapter;

    private ArrayList<Marker> markers;
    private LatLngBounds.Builder builder;
    private int currentStoreIndex = 1;

    private RecyclerView recyclerView;

    private List<ShopBean> shopList = new ArrayList<>();
    private ShopListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_livelihood, container, false);

        viewPager = v.findViewById(R.id.shop_viewPager);

        recyclerView = v.findViewById(R.id.recycler);

        pages = 3;
        //地圖初始化
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        builder = new LatLngBounds.Builder();

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadData("", "4");
    }

    //初始化資料用的
    private void loadData(String area, String storeType)
    {
        String la, lo;
        if (MainActivity.myLocation == null)
        {
            la = "";
            lo = "";
        } else
        {
            la = String.valueOf(MainActivity.myLocation.getLatitude());
            lo = String.valueOf(MainActivity.myLocation.getLongitude());
        }
        ApiConnection.getShopList(la, lo, area, storeType, new ApiConnection.OnConnectResultListener()
        {
            @Override
            public void onSuccess(String jsonString)
            {
                Type type = new TypeToken<ArrayList<ShopBean>>()
                {
                }.getType();
                shopList = new Gson().fromJson(jsonString, type);

//                Collections.sort(shopList, new Comparator<ShopBean>()
//                {
//                    @Override
//                    public int compare(ShopBean o1, ShopBean o2)
//                    {
//                        return o2.getDistance().compareTo(o1.getDistance());
//                    }
//                });
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        initView();
                    }
                });

            }

            @Override
            public void onFailure(String message)
            {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        viewPager.setAdapter(null);
                        recyclerView.setAdapter(null);
                    }
                });
            }
        });
    }

    private void initView()
    {
        shopViewPagerAdapter = new ShopViewPagerAdapter(getActivity(), shopList);
        viewPager.setAdapter(shopViewPagerAdapter);
        if (shopList.size() > 1)
        {
            viewPager.setCurrentItem(1);
        } else if (shopList.size() == 1)
        {
            viewPager.setCurrentItem(0);
        }

        final int paddingPx = 300;
        final float MIN_SCALE = 0.8f;
        final float MAX_SCALE = 1f;
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPadding(paddingPx, 0, paddingPx, 0);
//        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 50, false));
        adapter = new ShopListAdapter(shopList);
        recyclerView.setAdapter(adapter);

        ViewPager.PageTransformer transformer = (page, position) -> {
            float pagerWidthPx = ((ViewPager) page.getParent()).getWidth();
            float pageWidthPx = pagerWidthPx - 2 * paddingPx;

            float maxVisiblePages = pagerWidthPx / pageWidthPx;
            float center = maxVisiblePages / 2f;

            float scale;
            if (position + 0.5f < center - 0.5f || position > center)
            {
                scale = MIN_SCALE;
            } else
            {
                float coef;
                if (position + 0.5f < center)
                {
                    coef = (position + 1 - center) / 0.5f;
                } else
                {
                    coef = (center - position) / 0.5f;
                }
                scale = coef * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
            }
            page.setScaleX(scale);
            page.setScaleY(scale);
        };
        viewPager.setPageTransformer(false, transformer);

        if (status)
        {
            //地圖模式
            viewPager.setVisibility(View.VISIBLE);
            mapView.setVisibility(View.VISIBLE);
//            btnList.setImageResource(R.drawable.ic_list);

            recyclerView.setVisibility(View.GONE);
        } else
        {
            //列表模式
            viewPager.setVisibility(View.GONE);
            mapView.setVisibility(View.GONE);
//            btnList.setImageResource(R.drawable.ic_location_white);

            recyclerView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        if (marker.getTag() != null)
        {
            int index = (int) marker.getTag();

            if (index < 0)
            {
                return false;
            }
            viewPager.setCurrentItem(index, true);
            currentStoreIndex = index;
            Log.d("安安", "onMarkerClick: " + currentStoreIndex);
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
//        progressBar.setVisibility(View.GONE);
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        CameraPosition cameraPosition;
        if (MainActivity.myLocation != null)
        {
            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(MainActivity.myLocation.getLatitude(), MainActivity.myLocation.getLongitude()))
                    .zoom(16)
                    .build();
        } else
        {
            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(24.989075575968332, 121.31450436987083))
                    .zoom(16)
                    .build();
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
        {
            @Override
            public void onMapLoaded()
            {
                layoutResult();
            }
        });
    }

    private void layoutResult()
    {
        //移除已標示Marker
        mMap.clear();

        //ping marker
        markers = new ArrayList<>();
        for (int i = 0; i < shopList.size(); i++)
        {
            ShopBean item = shopList.get(i);

            String snippet = item.getStoreAddress();
            LatLng store = new LatLng(Double.parseDouble(item.getStoreLatitude()), Double.parseDouble(item.getStoreLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(store)
                    .title(item.getStoreName())
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_mark)))
            );
            marker.setTag(i);
            builder.include(marker.getPosition());

            markers.add(marker);
        }

        if (markers.size() < 1)
        {
            return;
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                currentStoreIndex = position;
                viewPager.setCurrentItem(currentStoreIndex, true);
                LatLng position2 = markers.get(currentStoreIndex).getPosition();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position2.latitude, position2.longitude), 16));
                markers.get(currentStoreIndex).showInfoWindow();
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder>
    {
        private List<ShopBean> list;

        public ShopListAdapter(final List<ShopBean> list)
        {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private View v;
            private ImageView imgContent;
            private TextView txtStoreName, txtPhone, txtDistance;

            public ViewHolder(@NonNull @NotNull View itemView)
            {
                super(itemView);

                v = itemView;

                imgContent = v.findViewById(R.id.iv_content);
                txtStoreName = v.findViewById(R.id.tv_store_name);
                txtPhone = v.findViewById(R.id.tv_info_store_tel);
                txtDistance = v.findViewById(R.id.tv_distance);
            }
        }

        @NonNull
        @NotNull
        @Override
        public ShopListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
        {
            return new ShopListAdapter.ViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ShopListAdapter.ViewHolder holder, int i)
        {
            holder.txtStoreName.setText(list.get(i).getStoreName());
            holder.txtPhone.setText(list.get(i).getStorePhone());
            PicassoTrustAll.getInstance(getActivity()).load(ApiConstant.API_IMAGE + list.get(i).getStorePicture()).into(holder.imgContent);

            holder.txtDistance.setText(AppUtility.mToKm(list.get(i).getDistance()));

            holder.v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    ProductFragment productFragment = ProductFragment.newInstance(list.get(i), "");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopNewLayout, productFragment, null).addToBackStack(null).commit();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return list.size();
        }
    }

    public class ShopViewPagerAdapter extends PagerAdapter
    {
        private Context context;
        private List<ShopBean> shopList;

        public ShopViewPagerAdapter(final Context context, final List<ShopBean> shopList)
        {
            this.context = context;
            this.shopList = shopList;
        }

        @Override
        public int getCount()
        {
            return shopList.size();
        }

        // 來判斷顯示的是否是同一張圖片，這裡我们將兩個參數相比較返回即可
        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object)
        {
            return view.equals(object);
        }

        // 當要顯示的圖片可以進行緩存的時候，會調用這個方法進行顯示圖片的初始化，我们將要顯示的ImageView加入到ViewGroup中，然後作為返回值返回即可
        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.slide_shop_item, container, false);

            RoundedImageView roundedImageView = view.findViewById(R.id.imageSlide);
            TextView txtTitle = view.findViewById(R.id.tv_title);
            TextView txtContent = view.findViewById(R.id.tv_content);
            TextView txtDescript = view.findViewById(R.id.tv_descript);
            RoundedImageView background = view.findViewById(R.id.background);
            background.getBackground().setAlpha(100);//0~255透明度值

            PicassoTrustAll.getInstance(getActivity()).load(ApiConstant.API_IMAGE + shopList.get(position).getStorePicture()).into(roundedImageView);
            txtTitle.setText(shopList.get(position).getStoreName());
            txtDescript.setText(shopList.get(position).getStoreNews());

            int AAA = Integer.parseInt(shopList.get(position).getDistance());

            if (AAA < 1000)
            {
                txtContent.setText(shopList.get(position).getDistance() + "m");

            } else
            {
                double BBB = AAA / 100;
                BigDecimal bigDecimal = new BigDecimal(BBB);
                double f1 = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue() / 10;
                txtContent.setText(f1 + "Km");
            }

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ProductFragment productFragment = ProductFragment.newInstance(shopList.get(position), "");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.shopNewLayout, productFragment, null).addToBackStack(null).commit();
                }
            });
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object)
        {
            container.removeView((View) object);
        }
    }
}