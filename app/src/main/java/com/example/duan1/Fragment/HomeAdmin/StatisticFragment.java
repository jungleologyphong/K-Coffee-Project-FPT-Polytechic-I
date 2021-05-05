package com.example.duan1.Fragment.HomeAdmin;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan1.ActivityAdmin.HomeAdminActivity;
import com.example.duan1.DAO.StatisticDAO;
import com.example.duan1.Model.ChartItem;
import com.example.duan1.Model.Invoice;
import com.example.duan1.Model.InvoiceDetail;
import com.example.duan1.Model.Product;
import com.example.duan1.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StatisticFragment extends Fragment implements OnChartValueSelectedListener, StatisticDAO.StatisticInterface {
    EditText edtFrom, edtTo;
    Button btnShow;
    PieChart chart;
    ArrayList<PieEntry> pieEntries;
    ArrayList<ChartItem> chartItems;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DecimalFormat df = new DecimalFormat("#,###,###");
    DatabaseReference databaseReference;
    DatePickerDialog datePickerDialog;
    long foodTotal, drinkTotal, total, deliveryFeeTotal, serviceFeeTotal, discountTotal, subTotal;
    int drinkCount, foodCount;
    //
    TextView tvDrinkTotal, tvFoodTotal, tvTotal, tvDeliveryFeeTotal, tvServiceFeeTotal, tvSubTotal, tvDiscountTotal;
    //
    ArrayList<Invoice> invoices;
    ArrayList<InvoiceDetail> invoiceDetails;
    //
    StatisticDAO statisticDAO;
    //
    TextView tvCancelledProductCount, tvOrderedProductCount, tvDeliveredProductCount, tvProductCount, tvAllTotal, tvOrderedTotal, tvCancelledTotal, tvDeliveredTotal;

    public StatisticFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        edtFrom = view.findViewById(R.id.edtFrom);
        edtTo = view.findViewById(R.id.edtTo);
        btnShow = view.findViewById(R.id.btnShow);
        chart = view.findViewById(R.id.chart);


        tvDrinkTotal = view.findViewById(R.id.tvDrinkTotal);
        tvFoodTotal = view.findViewById(R.id.tvFoodTotal);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvDeliveryFeeTotal = view.findViewById(R.id.tvDeliveryFeeTotal);
        tvServiceFeeTotal = view.findViewById(R.id.tvServiceFeeTotal);
        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvDiscountTotal = view.findViewById(R.id.tvDiscountTotal);


        statisticDAO = new StatisticDAO(this);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDatePicker();
        setupPieChart();
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    processData();
                }
            }
        });
    }

    private boolean validate() {
        String dateFrom = edtFrom.getText().toString();
        String dateTo = edtTo.getText().toString();

        if (dateFrom.isEmpty() || dateTo.isEmpty()) {
            Toast.makeText(getContext(), "Không được để trống ngày", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            dateFrom = dateFrom.concat(" 00:00:01");
            dateTo = dateTo.concat(" 23:59:59");
            long dateTimeFrom = 0;
            try {
                dateTimeFrom = simpleDateFormat.parse(dateFrom).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dateTimeTo = 0;
            try {
                dateTimeTo = simpleDateFormat.parse(dateTo).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //
            if (dateTimeFrom > dateTimeTo) {
                Log.e("From to", dateTimeFrom + " " + dateTimeTo);
                Toast.makeText(getContext(), "Ngày kết thúc phải bằng hoặc lớn hơn ngày bắt đầu", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void setupDatePicker() {
        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                String selectedDay = edtFrom.getText().toString();
                String[] splitArr = selectedDay.split("/");
                String currentDate = d + "/" + m + "/" + y;
                if (!selectedDay.equalsIgnoreCase(currentDate) && !selectedDay.isEmpty()) {
                    d = Integer.parseInt(splitArr[0]);
                    m = Integer.parseInt(splitArr[1]) - 1;
                    y = Integer.parseInt(splitArr[2]);
                }
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = String.valueOf(month);
                                String formattedDayOfMonth = String.valueOf(dayOfMonth);
                                if (month < 10) {
                                    formattedMonth = "0" + formattedMonth;
                                }
                                if (dayOfMonth < 10) {
                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                edtFrom.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                            }
                        }, y, m, d);

                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Xác nhận", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Hủy", datePickerDialog);
                datePickerDialog.show();

            }
        });
        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                int m = calendar.get(Calendar.MONTH);
                int y = calendar.get(Calendar.YEAR);
                String selectedDay = edtTo.getText().toString();
                String[] splitArr = selectedDay.split("/");
                String currentDate = d + "/" + m + "/" + y;
                if (!selectedDay.equalsIgnoreCase(currentDate) && !selectedDay.isEmpty()) {
                    d = Integer.parseInt(splitArr[0]);
                    m = Integer.parseInt(splitArr[1]) - 1;
                    y = Integer.parseInt(splitArr[2]);
                }
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = String.valueOf(month);
                                String formattedDayOfMonth = String.valueOf(dayOfMonth);
                                if (month < 10) {
                                    formattedMonth = "0" + formattedMonth;
                                }
                                if (dayOfMonth < 10) {
                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                edtTo.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                            }
                        }, y, m, d);

                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Xác nhận", datePickerDialog);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Hủy", datePickerDialog);
                datePickerDialog.show();

            }
        });
    }

    private void processData() {
        clearData();

        invoices = new ArrayList<>();
        invoiceDetails = new ArrayList<>();

        String dateFrom = edtFrom.getText().toString();
        String dateTo = edtTo.getText().toString();
        dateFrom = dateFrom.concat(" 00:00:01");
        dateTo = dateTo.concat(" 23:59:59");
        long dateTimeFrom = 0;
        try {
            dateTimeFrom = simpleDateFormat.parse(dateFrom).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dateTimeTo = 0;
        try {
            dateTimeTo = simpleDateFormat.parse(dateTo).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("Time", dateTimeFrom + " - " + dateTimeTo + "\n" + dateFrom + " - " + dateTo);
        //
        getData(dateTimeFrom, dateTimeTo);
        statisticDAO.getAllTotal(dateTimeFrom, dateTimeTo);
    }

    private void getData(long dateTimeFrom, long dateTimeTo) {
        databaseReference.child("Invoice").orderByChild("createdAt").startAt(dateTimeFrom).endAt(dateTimeTo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            final Invoice invoice = item.getValue(Invoice.class);
                            if (invoice != null) {
                                invoice.setId(item.getKey());
                                if (invoice.getStatus().equalsIgnoreCase("delivered")) {
                                    total += invoice.getTotal();
                                    subTotal += invoice.getSubTotal();
                                    deliveryFeeTotal += invoice.getDeliveryFee();
                                    serviceFeeTotal += invoice.getServiceFee();
                                    databaseReference.child("InvoiceDetail").orderByChild("invoiceId").equalTo(invoice.getId())
                                            .addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    final InvoiceDetail invoiceDetail = snapshot.getValue(InvoiceDetail.class);
                                                    if (invoiceDetail != null) {
                                                        invoiceDetail.setId(snapshot.getKey());
                                                        databaseReference.child("Product").child(invoiceDetail.getProductId())
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        Product product = snapshot.getValue(Product.class);
                                                                        if (product != null) {
                                                                            product.setId(snapshot.getKey());
                                                                            if (product.getType().equalsIgnoreCase("Drink")) {
                                                                                drinkCount += invoiceDetail.getQuantity();
                                                                                drinkTotal += invoiceDetail.getQuantity() * product.getPrice();
                                                                                setData();
                                                                            } else if (product.getType().equalsIgnoreCase("Food")) {
                                                                                foodCount += invoiceDetail.getQuantity();
                                                                                foodTotal += invoiceDetail.getQuantity() * product.getPrice();
                                                                                setData();
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                }

                                                @Override
                                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                }

                                                @Override
                                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                }
                            }
                        }
                        setData();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void setupPieChart() {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5f, 10f, 5f, 5f);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setHoleColor(getResources().getColor(R.color.White));
        chart.setTransparentCircleColor(R.color.White);
        chart.setTransparentCircleAlpha(0);
        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(0f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0f);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setOnChartValueSelectedListener(this);
        chart.setUsePercentValues(true);

        chart.setEntryLabelColor(R.color.Black);
        chart.setCenterTextColor(R.color.Black);
        chart.setCenterTextSize(20);
        chart.setEntryLabelTextSize(20);

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
        legend.setTextSize(15);
    }

    private void clearData() {/*
        foodCount = 0;
        drinkCount = 0;
        foodTotal = 0;
        drinkTotal = 0;
        total = 0;
        deliveryFeeTotal = 0;
        serviceFeeTotal = 0;
        subTotal = 0;
        setData();*/
    }

    private void setData() {/*
        tvFoodTotal.setText(df.format(foodTotal));
        tvDrinkTotal.setText(df.format(drinkTotal));
        tvTotal.setText(df.format(total));
        tvDeliveryFee.setText(df.format(deliveryFeeTotal));
        tvServiceFee.setText(df.format(serviceFeeTotal));
        tvSubTotal.setText(df.format(subTotal));
        setPiechartData(drinkTotal, foodTotal, drinkCount, foodCount);*/
    }

    private void setPiechartData(long drinkTotal, long foodTotal, int drinkCount,
                                 int foodCount) {

        pieEntries = new ArrayList<>();
        chartItems = new ArrayList<>();
        if (foodTotal != 0) {
            chartItems.add(new ChartItem("Đồ ăn", foodCount, foodTotal));
        }
        if (drinkTotal != 0) {
            chartItems.add(new ChartItem("Nước uống", drinkCount, drinkTotal));
        }
        for (int i = 0; i < chartItems.size(); i++) {
            pieEntries.add(new PieEntry(chartItems.get(i).getTotal(), chartItems.get(i).getLabel()));
        }

        if (foodTotal == 0 && drinkTotal == 0) {
            chart.setVisibility(View.GONE);
        } else {
            chart.setVisibility(View.VISIBLE);
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Loại sản phẩm");

            pieDataSet.setDrawIcons(false);

            pieDataSet.setSliceSpace(3f);
            pieDataSet.setIconsOffset(new MPPointF(0f, 40f));
            pieDataSet.setSelectionShift(5f);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(getResources().getColor(R.color.OrangeRed));
            colors.add(getResources().getColor(R.color.Aqua));
            colors.add(getResources().getColor(R.color.Purple));
            colors.add(getResources().getColor(R.color.DodgerBlue));
            colors.add(getResources().getColor(R.color.Orange));
            colors.add(getResources().getColor(R.color.Blue));
            colors.add(getResources().getColor(R.color.Lime));
            colors.add(getResources().getColor(R.color.Yellow));
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(15f);
            pieData.setValueTextColor(R.color.Black);

            chart.setData(pieData);
            chart.highlightValue(null);
            chart.invalidate();
            //chart.setOnChartValueSelectedListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeAdminActivity) getActivity()).getSupportActionBar().setTitle("Thống kê");
        ((HomeAdminActivity) getActivity()).changeBackButton();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        //showDetailDialog(h);
    }

    @Override
    public void onNothingSelected() {

    }

    public void showDetailDialog(final Highlight highlight) {
        int position = (int) highlight.getX();
        BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        bsd.setContentView(R.layout.dialog_chart_detail);

        TextView tvType = bsd.findViewById(R.id.tvType);
        TextView tvCount = bsd.findViewById(R.id.tvCount);
        TextView tvTotal = bsd.findViewById(R.id.tvTotal);

        ChartItem chartItem = chartItems.get(position);
        tvType.setText(String.valueOf(chartItem.getLabel()));
        tvTotal.setText(df.format(chartItem.getTotal()));
        tvCount.setText(String.valueOf(chartItem.getCount()));

        bsd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                chart.highlightValue(null);
            }
        });
        bsd.show();

    }

    @Override
    public void getAllTotal(long[] total, long[] subTotal, long[] discount, long[] deliveryFee, long[] serviceFee, long[] invoiceCount) {
        Log.e("Thông số", "****************************************");
        Log.e("Giá tri", total[0] + " / " + total[1] + " / " + total[2] + " / " + total[3]);
        Log.e("Tổng", subTotal[0] + " / " + subTotal[1] + " / " + subTotal[2] + " / " + subTotal[3]);
        Log.e("Giảm giá", discount[0] + " / " + discount[1] + " / " + discount[2] + " / " + discount[3]);
        Log.e("Phí giao hàng", deliveryFee[0] + " / " + deliveryFee[1] + " / " + deliveryFee[2] + " / " + deliveryFee[3]);
        Log.e("Phí dịch vụ", serviceFee[0] + " / " + serviceFee[1] + " / " + serviceFee[2] + " / " + serviceFee[3]);
        Log.e("Số lượng hóa đơn", invoiceCount[0] + " / " + invoiceCount[1] + " / " + invoiceCount[2] + " / " + invoiceCount[3]);
        tvTotal.setText(String.valueOf(total[3]));
        tvDiscountTotal.setText(String.valueOf(discount[3]));
        tvDeliveryFeeTotal.setText(String.valueOf(deliveryFee[3]));
        tvServiceFeeTotal.setText(String.valueOf(serviceFee[3]));
        tvSubTotal.setText(String.valueOf(subTotal[3]));
    }

    @Override
    public void getProductCount(long[] productCount, long[] cancelledProductCount, long[] orderedProductCount, long[] deliveredProductCount) {
        Log.e("Số lượng sản phẩm", "****************************************");
        Log.e("Tổng", productCount[0] + "/" + productCount[1] + "/" + productCount[2]);
        Log.e("Đã hủy", cancelledProductCount[0] + "/" + cancelledProductCount[1] + "/" + cancelledProductCount[2]);
        Log.e("Đã đặt", orderedProductCount[0] + "/" + orderedProductCount[1] + "/" + orderedProductCount[2]);
        Log.e("Đã giao", deliveredProductCount[0] + "/" + deliveredProductCount[1] + "/" + deliveredProductCount[2]);
    }

    @Override
    public void getProductTotal(long[] allTotal, long[] cancelledTotal, long[] orderedTotal, long[] deliveredTotal) {
        Log.e("Tổng giá trị", "****************************************");
        Log.e("Tất cả", allTotal[0] + "/" + allTotal[1] + "/" + allTotal[2]);
        Log.e("Đã hủy", cancelledTotal[0] + "/" + cancelledTotal[1] + "/" + cancelledTotal[2]);
        Log.e("Đã đặt", orderedTotal[0] + "/" + orderedTotal[1] + "/" + orderedTotal[2]);
        Log.e("Đã giao", deliveredTotal[0] + "/" + deliveredTotal[1] + "/" + deliveredTotal[2]);
        tvFoodTotal.setText(String.valueOf(deliveredTotal[2]));
        tvDrinkTotal.setText(String.valueOf(deliveredTotal[1]));
        setPiechartData(allTotal[1], allTotal[2], 1, 1);
    }

}