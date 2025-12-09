package com.nhom.weather_hub.config;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class SSLUtils {

    public static SSLSocketFactory getInsecureSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(
                                X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(
                                X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            return sc.getSocketFactory();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL Socket Factory", e);
        }
    }
}
