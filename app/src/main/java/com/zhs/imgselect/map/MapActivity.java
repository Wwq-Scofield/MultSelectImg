package com.zhs.imgselect.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.SubPoiItem;
import com.zhs.app.imgselect.R;
import com.zhs.imgselect.map.adapter.LocateRecyclerAdapter;
import com.zhs.imgselect.map.bean.LocationInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class MapActivity extends AppCompatActivity implements AMapLocationListener, PoiSearch.OnPoiSearchListener {
    public AMapLocationClient mlocationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private List<LocationInfo> mList;
    private LocateRecyclerAdapter mAdapter;
    private String address;
    private TextView tvFinish;
    private String city;
    private RecyclerView mLocateRecycler;
    private String mCurrentAddress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mList = new ArrayList<>();

        mLocateRecycler = (RecyclerView) findViewById(R.id.locate_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mLocateRecycler.setLayoutManager(layoutManager);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("address", city+"."+mCurrentAddress);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mAdapter = new LocateRecyclerAdapter(this);
        mLocateRecycler.setAdapter(mAdapter);
        mLocateRecycler.getItemAnimator().setChangeDuration(0);
        mAdapter.setOnItemClickListener(new LocateRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mAdapter!=null){
                    if(position<0){
                        return;
                    }
                    mAdapter.setSelect(position);
                    if(position==0){
                        mCurrentAddress="";
                    }else if(position==1){
                        Toast.makeText(MapActivity.this, "请选择详细地址 ", Toast.LENGTH_SHORT).show();
                    }else{
                        mCurrentAddress=mList.get(position).getTitle();
                    }
                }else{

                }
            }
        });
        initLocate();
    }

    private void initLocate() {
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double latitude = amapLocation.getLatitude();//获取纬度
                double longitude = amapLocation.getLongitude();//获取经度
                float accuracy = amapLocation.getAccuracy();//获取精度信息
                city = amapLocation.getCity();
                Log.d("wwq", amapLocation.getAddress()+"");
                mList.clear();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                PoiSearch.Query query = new PoiSearch.Query("", address, "");
                query.setPageSize(20);
                PoiSearch search = new PoiSearch(this, query);
                search.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude, longitude), 10000));
                search.setOnPoiSearchListener(this);
                search.searchPOIAsyn();

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int i) {
        PoiSearch.Query query = result.getQuery();
        ArrayList<PoiItem> pois = result.getPois();
        for (PoiItem poi : pois) {
            String name = poi.getCityName();
            String snippet = poi.getSnippet();
            String businessArea = poi.getBusinessArea();
            String shopID = poi.getShopID();
            String title = poi.getTitle();
            List<SubPoiItem> subPois = poi.getSubPois();
            String snippet1 = poi.getSnippet();
            LocationInfo info = new LocationInfo();
            info.setAddress(snippet);
            info.setTitle(title);
            info.setCity(name);
            info.setSelect(false);
            mList.add(info);
            Log.d("haha", "name" + name + "  snippet:" + snippet + " businessArea: " + businessArea + " shopID: " + shopID + " title: " + title + " snippet1:" + snippet1 + "\n");
        }
        Log.d("wwq","onPoiSearched...");
        LocationInfo info = new LocationInfo();
        info.setSelect(true);
        mList.add(0, info);
        LocationInfo info1 = new LocationInfo();
        info1.setCity(city);
        info1.setSelect(false);
        mList.add(1, info1);
        mAdapter.setDatas(mList);
        mAdapter.notifyDataSetChanged();
//        mlocationClient.stopLocation();

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
