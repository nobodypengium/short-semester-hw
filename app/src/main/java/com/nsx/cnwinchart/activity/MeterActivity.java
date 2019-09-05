package com.nsx.cnwinchart.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.nsx.cnwinchart.R;
import com.nsx.cnwinchart.dashboardview.view.DashboardView;
import com.nsx.cnwinchart.manager.CombinedChartManager;
import com.nsx.cnwinchart.manager.DynamicLineChartManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MeterActivity extends AppCompatActivity  {
    DashboardView dashboardViewT,dashboardViewC;
    //    private DynamicLineChartManager dynamicLineChartManager2;
    private CombinedChart mCombinedChart1;
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合

    int t = 0;
    int c = 0;
    float tf = 0;
    float cf = 0;
    SimpleDateFormat df = new SimpleDateFormat("mm:ss");//设置日期格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        mCombinedChart1 = (CombinedChart) findViewById(R.id.chart1);
        dashboardViewT = (DashboardView) findViewById(R.id.dashboardViewT);
        dashboardViewC = (DashboardView) findViewById(R.id.dashboardViewC);
        LineChart mChart2 = (LineChart) findViewById(R.id.dynamic_chart2);

        names.add("温度");
        names.add("CO2");

        //折线颜色
        colour.add(Color.CYAN);
        colour.add(Color.GREEN);

        dashboardViewT.setTikeStrArray(new String[]{"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"});
        dashboardViewT.setText("当前温度");
        dashboardViewT.setUnit("0℃");
        dashboardViewT.setStartNum(20);
        dashboardViewT.setMaxNum(30);
        dashboardViewT.setTextColor(Color.WHITE);

        dashboardViewC.setTikeStrArray(new String[]{"350", "355", "360", "365", "370", "375", "380", "385", "390", "395", "400"});
        dashboardViewC.setText("当前CO2");
        dashboardViewC.setUnit("ppm");
        dashboardViewC.setStartNum(350);
        dashboardViewC.setMaxNum(400);
        dashboardViewC.setTextColor(Color.WHITE);

        List<String> xData = new ArrayList<>();
        List<List<Float>> yBarDatas = new ArrayList<>();
        List<List<Float>> yLineDatas = new ArrayList<>();

        List<Float> yData = new ArrayList<>();
        List<Float> yData2 = new ArrayList<>();

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#009966"));
        colors.add(Color.parseColor("#009999"));

        final TextView textView = findViewById(R.id.text);
        Button login = (Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path="http://10.128.234.101:8080/mainpage/position";
                        try {
                            try{
                                URL url = new URL(path); //新建url并实例化
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");//获取服务器数据
                                connection.setReadTimeout(8000);//设置读取超时的毫秒数
                                connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                                InputStream in = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                                String arr[] = result.split(" ");
                                textView.setText( result + arr[0] + arr[1]);

//                                t = (int)Float.parseFloat(arr[0]);
//                                c = (int)Float.parseFloat(arr[1]);
//                                tf = Float.parseFloat(arr[0]);
//                                cf = Float.parseFloat(arr[1]);

                                t = (int) (Math.random()*(30-20)+20);
                                c = (int) (Math.random()*(400-350)+350);
                                cf = c;
                                tf = t;

                                Log.wtf("af",result);
                            }catch (MalformedURLException e){}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                yData.add(cf);
                yData2.add(tf);
                xData.add(df.format(new Date()));
                yBarDatas.add(yData);
                yLineDatas.add(yData2);
                CombinedChartManager combineChartManager1 = new CombinedChartManager(mCombinedChart1);
                combineChartManager1.showCombinedChart(xData, yLineDatas.get(0),yBarDatas.get(0),
                        "温度", "CO2", colors.get(0), colors.get(1));
                dashboardViewT.setPercent(t);
                dashboardViewC.setPercent(c);
            }
        });
    }

}
