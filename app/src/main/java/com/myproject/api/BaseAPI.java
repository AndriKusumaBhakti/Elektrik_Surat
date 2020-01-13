package com.myproject.api;

import android.content.Context;

import com.jakewharton.retrofit.Ok3Client;
import com.myproject.R;
import com.myproject.aplication.APP;
import com.myproject.aplication.Config;
import com.myproject.aplication.Preference;
import com.myproject.model.response.APIBaseResponse;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;

public class BaseAPI {

    public static RestAdapter getMTAPIV9(final Context ctx){

        Builder b = new Builder();
        b.readTimeout(30, TimeUnit.SECONDS);
        b.writeTimeout(30, TimeUnit.SECONDS);
        b.connectTimeout(30, TimeUnit.SECONDS);

        OkHttpClient client = b.build();
        client = trustAllSslClient(client);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getAPIUrl())
                .setClient(new Ok3Client(client))
                .setLog(new AndroidLog(Config.APP_NAME))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        String tokens = APP.getStringPref(ctx, Preference.TOKEN);
                        request.addHeader("Accept", "application/json;versions=1");
                        if (tokens.isEmpty() == false) {
                            request.addHeader("Token", tokens);
                        }
                    }
                }).setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError cause) {
                        String errorDescription;
                        if (cause.getKind().equals(RetrofitError.Kind.NETWORK)) {
                            errorDescription = ctx.getResources().getString(R.string.error_connection_offline);
                        } else {
                            if (cause.getResponse() == null) {
                                errorDescription = ctx.getResources().getString(R.string.error_fetching_data);
                            } else {
                                try {
                                    APIBaseResponse body = (APIBaseResponse) cause.getBodyAs(APIBaseResponse.class);
                                    errorDescription = body.getMessage();
                                    APP.logError(errorDescription);
                                } catch (Exception ex) {
                                    APP.logError(cause.getMessage());
                                    errorDescription = ctx.getResources().getString(R.string.error_fetching_data);
                                }
                            }
                        }
                        return new Exception(errorDescription);
                    }
                })
                .build();

        if (Config.isDebuging) {
            restAdapter.setLogLevel((RestAdapter.LogLevel.FULL));
        }
        else{
            restAdapter.setLogLevel((RestAdapter.LogLevel.NONE));
        }

        return restAdapter;
    }

    private static final TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    private static final SSLContext trustAllSslContext;
    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    private static OkHttpClient trustAllSslClient(OkHttpClient client) {
        Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }
}
