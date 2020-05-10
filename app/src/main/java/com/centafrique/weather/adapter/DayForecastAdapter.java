package com.centafrique.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.centafrique.weather.R;
import com.centafrique.weather.pojo.DailyForecastPojo;
import com.centafrique.weather.helper_class.AppUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DayForecastAdapter extends RecyclerView.Adapter<DayForecastAdapter.DayForecastHolder> {

    private Context context;
    private ArrayList<DailyForecastPojo> dailyForecastPojoArrayList ;

    public DayForecastAdapter(Context context, ArrayList<DailyForecastPojo> DailyForecastPojoArrayList) {
        this.context = context;
        this.dailyForecastPojoArrayList = DailyForecastPojoArrayList;
    }

    @NonNull
    @Override
    public DayForecastAdapter.DayForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_forecast, parent, false);
        DayForecastAdapter.DayForecastHolder holder = new DayForecastAdapter.DayForecastHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DayForecastAdapter.DayForecastHolder holder, int position) {

        String txtDay = dailyForecastPojoArrayList.get(position).getDt();
        Double txtMinTemp = dailyForecastPojoArrayList.get(position).getMinTemp();
        Double txtMaxTemp = dailyForecastPojoArrayList.get(position).getMaxTemp();

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");
        Date date = new Date();
        String currentDate = formatter.format(date);

        if (currentDate.equals(txtDay)){

            holder.imgBtn.setVisibility(View.VISIBLE);
            holder.imgBtn.setBackgroundResource(R.drawable.ic_action_today_new);

        }else {

            holder.imgBtn.setVisibility(View.INVISIBLE);
        }

        int txtWeatherCode = dailyForecastPojoArrayList.get(position).getId();

        holder.tvDayName.setText(txtDay);
        holder.tvMinTemperature.setText(String.format(Locale.getDefault(), "%.0f°", txtMinTemp));
        holder.tvMaxTemperature.setText(String.format(Locale.getDefault(), "%.0f°", txtMaxTemp));

        holder.animation_view.setAnimation(AppUtil.getWeatherAnimation(txtWeatherCode));
        holder.animation_view.playAnimation();

    }


    @Override
    public int getItemCount() {
        return dailyForecastPojoArrayList.size();
    }

    public static class DayForecastHolder extends RecyclerView.ViewHolder{

        private TextView tvDayName;
        private TextView tvMinTemperature, tvMaxTemperature;
        private LottieAnimationView animation_view;
        private ImageButton imgBtn;

        public DayForecastHolder(@NonNull View itemView) {
            super(itemView);

            animation_view = itemView.findViewById(R.id.animation_view);
            tvDayName = itemView.findViewById(R.id.tvDayName);

            tvMinTemperature = itemView.findViewById(R.id.tvMinTemperature);
            tvMaxTemperature = itemView.findViewById(R.id.tvMaxTemperature);

            imgBtn = itemView.findViewById(R.id.imgBtn);



        }





    }
}
