package com.example.polyapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, OnMapReadyCallback {
    GoogleMap gMap;
    CheckBox checkBox;
    SeekBar seekRed, seekGreen, seekBlue;
    Button btDraw, btClear;
    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();
    int red = 0, green = 0, blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkBox = findViewById(R.id.check_box);
        seekRed = findViewById(R.id.seek_red);
        seekGreen = findViewById(R.id.seek_green);
        seekBlue = findViewById(R.id.seek_blue);
        btDraw = findViewById(R.id.bt_draw);
        btClear = findViewById(R.id.bt_clear);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    if (polygon == null) return;
                    ;

                    polygon.setFillColor(Color.rgb(red, green, blue));
                } else {

                    polygon.setFillColor(Color.TRANSPARENT);
                }
            }
        });

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (polygon != null) polygon.remove();

                PolygonOptions polygonOptions = new PolygonOptions().addAll(latLngList)
                        .clickable(true);
                polygon = gMap.addPolygon(polygonOptions);

                polygon.setStrokeColor(Color.rgb(red, green, blue));
                if (checkBox.isChecked())

                    polygon.setFillColor(Color.rgb(red, green, blue));
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (polygon != null) polygon.remove();
                for (Marker marker : markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                checkBox.setChecked(false);
                seekRed.setProgress(0);
                seekGreen.setProgress(0);
                seekBlue.setProgress(0);
            }
        });
        seekRed.setOnSeekBarChangeListener(this);
        seekGreen.setOnSeekBarChangeListener(this);
        seekBlue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//Create MarkerOptions
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//Create Marker
                Marker marker = gMap.addMarker(markerOptions);
//Add LanLng and Marker
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int id = seekBar.getId();

        if (id == R.id.seek_red) {
            red = i;
        } else if (id == R.id.seek_green) {
            green = i;
        } else if (id == R.id.seek_blue) {
            blue = i;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}