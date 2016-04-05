package com.example.ahmad.myapps.api;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

/**
 * Created by ahmad on 01/02/2016.
 */
public class Config {

    public static final String PAYPAL_CLIENT_ID = "AZcVSPKaITMwRNC9UZU_eSgexmC7SXdCwM4uLCiz0XAbExi2h4k2Z1X983X6PJmQKEZPpxQfOWrvfZWE";
    public static final String PAYPAL_CLIENT_SECRET = "ELrXLF8-3MYyYCbnFoHIfS8ArWkUCnnPZ7EyYR6Pl2NRhedkH4OS62l1PyvD165yY642w-gL0EhsEY7k";

    public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String DEFAULT_MERCHANT = "Gats Shop";

    public static final String URL_PAYMENTS = "http://192.168.1.10/serviceslim/v1/verifyPayment";
    public static final String URL_PRODUCTS = "http://192.168.1.10/serviceslim/v1/products";
    public static final String URL_MAKANAN = "http://192.168.1.10/serviceslim/listmakanan";
    public static final String URL_MINUMAN = "http://192.168.1.10/serviceslim/listminuman";
    public static final String URL_FOOD = "http://192.168.1.10/serviceslim/food";

    public static final String URL_REGISTRASI = "http://192.168.1.10/serviceslim/anggota";
    public static final String URL_LOGIN = "http://192.168.1.10/serviceslim/anggota1";
}
