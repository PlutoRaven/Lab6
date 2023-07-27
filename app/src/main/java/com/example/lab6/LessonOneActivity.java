package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.lab6.databinding.ActivityLessonOneBinding;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LessonOneActivity extends AppCompatActivity {
    private static final String NAMESPACE = "https://www.w3schools.com/xml/";
    private static final String URL = "https://www.w3schools.com/xml/tempconvert.asmx";
    private static final String METHOD_NAME_F_TO_C = "FahrenheitToCelsius";
    private static final String METHOD_NAME_C_TO_F = "CelsiusToFahrenheit";
    private static final String SOAP_ACTION_F_TO_C = "https://www.w3schools.com/xml/FahrenheitToCelsius";
    private static final String SOAP_ACTION_C_TO_F = "https://www.w3schools.com/xml/CelsiusToFahrenheit";


    private ActivityLessonOneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.convertFtoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temperature = binding.temperature.getText().toString();
                new FahrenheitToCelsiusTask().execute(temperature);
            }
        });

        binding.convertCtoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temperature = binding.temperature.getText().toString();
                new CelsiusToFahrenheitTask().execute(temperature);
            }
        });

    }

    private class FahrenheitToCelsiusTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String temperature = params[0];
            String result;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_F_TO_C);
                request.addProperty("Fahrenheit", temperature);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);
                transportSE.call(SOAP_ACTION_F_TO_C, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = response.toString();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.result.setText(result + " °C");
        }
    }

    private class CelsiusToFahrenheitTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String temperature = params[0];
            String result;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_C_TO_F);
                request.addProperty("Celsius", temperature);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);
                transportSE.call(SOAP_ACTION_C_TO_F, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                result = response.toString();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            binding.result.setText(result + " °F");
        }
    }
}