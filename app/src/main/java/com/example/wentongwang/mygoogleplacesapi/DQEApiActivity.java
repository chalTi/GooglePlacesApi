package com.example.wentongwang.mygoogleplacesapi;

import android.app.Activity;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wentongwang on 06/06/2017.
 */

public class DQEApiActivity extends Activity {

    private DQEService mService;
    private EditText codePostal, city, phone, email;
    private AutoCompleteTextView voie, num;
    private Button phoneCheck, emailCheck;

    private AutoCompleteAdapter voieAdapter;
    private NumAutoCompleteAdapter numAdapter;
    private DQEResult currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dqe);

        codePostal = (EditText) findViewById(R.id.code_postal);
        city = (EditText) findViewById(R.id.city);
        voie = (AutoCompleteTextView) findViewById(R.id.voie);
        num = (AutoCompleteTextView) findViewById(R.id.num);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        emailCheck = (Button) findViewById(R.id.email_check);
        phoneCheck = (Button) findViewById(R.id.phone_check);

        initAdapter();
        initRetrofit();
        initListeners();
    }

    private void initAdapter() {
        voieAdapter = new AutoCompleteAdapter(this, android.R.layout.simple_list_item_1);
        voie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPlace = voieAdapter.getCompleteItem(position);


                Map<String, String> params = new HashMap<>();
                params.put("IDVoie", currentPlace.getIDVoie());
                params.put("IDLocalite", currentPlace.getIDLocalite());
                params.put("CodePostal", currentPlace.getCodePostal());


                Observable<Map<String, DQEResult>> observable = mService.getNumInAddress(getParams(params));
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, DQEResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Map<String, DQEResult> stringDQEResultMap) {

                                DQEResult result = stringDQEResultMap.get("1");
                                if (result != null) {
                                    numAdapter.setmResultList(Arrays.asList(result.getListeNumero().split(";")));
                                }
                            }
                        });

            }
        });

        numAdapter = new NumAutoCompleteAdapter(this, android.R.layout.simple_list_item_1);


    }

    private void initListeners() {
        codePostal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 3) {
                    return;
                }


                Map<String, String> params = new HashMap<>();
                params.put("CodePostal", s.toString());
                params.put("Alpha", "false");
                params.put("Instance", "0");


                Observable<Map<String, DQEResult>> observable = mService.getCityWithCodePostal(getParams(params));
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, DQEResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Map<String, DQEResult> stringDQEResultMap) {
                                DQEResult result = stringDQEResultMap.get("1");
                                if (result != null) {
                                    city.setText(result.getLocalite());
                                    currentPlace = result;
                                }
                            }
                        });
            }
        });

        voie.setAdapter(voieAdapter);
        voie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 3) {
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("IDLocalite", currentPlace.getIDLocalite());
                params.put("Adresse", s.toString());
                params.put("Instance", "0");


                Observable<Map<String, DQEResult>> observable = mService.getAddressInCity(getParams(params));
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, DQEResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Map<String, DQEResult> stringDQEResultMap) {
                                List<DQEResult> results = new ArrayList<>();

                                for (DQEResult dqeResult : stringDQEResultMap.values()) {
                                    results.add(dqeResult);
                                }

                                voieAdapter.setmResultList(results);

                            }
                        });
            }
        });

        num.setAdapter(numAdapter);
        num.setThreshold(1);


        phoneCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("Tel", phone.getText().toString());
                params.put("Format", "0");


                Observable<Map<String, PhoneCheckResponse>> observable = mService.checkPhoneNumber(getParams(params));
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, PhoneCheckResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Map<String, PhoneCheckResponse> stringDQEResultMap) {
                                PhoneCheckResponse response = stringDQEResultMap.get("1");
                                if (response.getIdError() == 0) {
                                    phone.setError("le téléphone est incorrect");
                                }

                            }
                        });
            }
        });

        emailCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("Email", email.getText().toString());
                params.put("Licence", "CYPLW0PAEL");


                Observable<Map<String, EmailCheckResponse>> observable = mService.checkEmail(params);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Map<String, EmailCheckResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Map<String, EmailCheckResponse> stringDQEResultMap) {
                                EmailCheckResponse response = stringDQEResultMap.get("1");
                                if (!(response.getIdError().equals("00") || response.getIdError().equals("01"))) {
                                    email.setError("l'email est incorrect");
                                }

                            }
                        });
            }
        });
    }

    private void initRetrofit() {

        OkHttpClient httpClient;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://prod2.dqe-software.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        mService = retrofit.create(DQEService.class);
    }

    private Map<String, String> getParams(Map<String, String> params) {
        params.put("Licence", "CYPLW0PAEL");
        params.put("Pays", "FRA");
        return params;
    }
}
