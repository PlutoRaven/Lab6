package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;


import com.example.lab6.databinding.ActivityLessonTwoBinding;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class LessonTwoActivity extends AppCompatActivity {
    private static final String NAMESPACE = "https://webservicex.net/";
    private static final String URL = "https://www.webservicex.net/CurrencyConvertor.asmx";
    private static final String METHOD_NAME = "ConversionRate";
    private static final String SOAP_ACTION = "https://www.webserviceX.NET/ConversionRate";

    private List<String> currencies;
    private ActivityLessonTwoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("VND");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCurrency.setAdapter(adapter);

        binding.buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = binding.editTextAmount.getText().toString();
                String fromCurrency = binding.spinnerCurrency.getSelectedItem().toString();
                String toCurrency = fromCurrency.equals("USD") ? "VND" : "USD";
                new CurrencyConversionTask().execute(amount, fromCurrency, toCurrency);
            }
        });
    }

    private class CurrencyConversionTask extends AsyncTask<String, Void, Double> {
        @Override
        protected Double doInBackground(String... params) {
            String amount = params[0];
            String fromCurrency = params[1];
            String toCurrency = params[2];

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                PropertyInfo amountProp = new PropertyInfo();
                amountProp.setName("FromCurrency");
                amountProp.setValue(fromCurrency);
                amountProp.setType(String.class);
                request.addProperty(amountProp);

                PropertyInfo fromProp = new PropertyInfo();
                fromProp.setName("ToCurrency");
                fromProp.setValue(toCurrency);
                fromProp.setType(String.class);
                request.addProperty(fromProp);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);
                transportSE.call(SOAP_ACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                double rate = Double.parseDouble(response.toString());
                double amountDouble = Double.parseDouble(amount);
                return rate * amountDouble;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double result) {
            super.onPostExecute(result);
            if (result != null) {
                binding.textViewResult.setText(String.format("%.2f", result));
            } else {
                binding.textViewResult.setText("Error occurred ");
            }
        }
    }
}