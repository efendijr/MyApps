package com.example.ahmad.myapps.api;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by ahmad on 01/02/2016.
 */
public class Config {

    public static final String PAYPAL_CLIENT_ID = "ATPZm7k-jA0PuYvl8h_i7OwIU6ZvowFVbMDnyMf2u8xKF6Rf-W7GH-U4nf7FxWR_ys8yucvrK1_vAW6L";
    public static final String PAYPAL_CLIENT_SECRET = "EGcHZFi2ChI-dz__yowD2NneU68RQOld9ukzSp3VtCrHAXe9inBO2Ea2pGGW9eaAaTuzmweDosvz1bBp";

    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String DEFAULT_MERCHANT = "Gats Shop";

    public static final String URL_PAYMENTS = "http://192.168.1.8/serviceslim/verifyPayment";
    public static final String URL_PRODUCTS = "http://192.168.1.8/serviceslim/v1/products";
    public static final String URL_MAKANAN = "http://192.168.1.8/serviceslim/listmakanan";
    public static final String URL_MINUMAN = "http://192.168.1.8/serviceslim/listminuman";

    public static final String URL_REGISTRASI = "http://192.168.1.8/serviceslim/anggota";
    public static final String URL_LOGIN = "http://192.168.1.8/serviceslim/anggota1";
}
