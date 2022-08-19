package com.example.myapplication;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;


public class HttpClientNative {
    private static final String TAG = "HttpTask";

    public static final int TIME_OUT = 10 * 1000;
    private static HttpClientNative sInstance;
    private OkHttpClient mClient;

    public HttpClientNative() {
        mClient = newClient();
    }

    public static HttpClientNative getInstance() {
        if (sInstance == null) {
            synchronized (HttpClientNative.class) {
                if (sInstance == null) {
                    sInstance = new HttpClientNative();
                }
            }
        }
        return sInstance;
    }


    public OkHttpClient getClient(){
        return mClient;
    }

    /**
     * 创建默认的OkHttpClient
     */
    private static OkHttpClient newClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);
//        if (AppEnvLite.GOOGLE_PLAY) {
//            builder.socketFactory(new DelegateSocketFactory());
//        }
//        builder.addInterceptor(new GzipRequestInterceptor());
//        if(!HttpConstant.DEBUG) {
        // 测试环境去掉证书校验
//            builder.sslSocketFactory(getSSLSocketFactory());
//        }
//        这里是为了测试能够配置ssl，开发中不需要配置
        return builder.build();
    }

    /** This interceptor compresses the HTTP request body. Many webservers can't handle this! */

    /**
     * 获取SSLSocketFactory
     */
//    private static SSLSocketFactory getSSLSocketFactory() {
//        try {
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null);
//            final int[] ids = new int[]{R.raw.huajiao1, R.raw.huajiao2, R.raw.huajiao3, R.raw.huajiao4};
//            for (int i = 0; i < ids.length; i++) {
//                keyStore.setCertificateEntry("cert" + i, loadCertificate(ids[i]));
//            }
//            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            factory.init(keyStore);
//            SSLContext ssl = SSLContext.getInstance("TLS");
//            ssl.init(null, factory.getTrustManagers(), null);
//            return ssl.getSocketFactory();
//        } catch (Exception e) {
//            return null;
//        }
//    }


    /**
     * 加载证书
     */
//    private static X509Certificate loadCertificate(int id) {
//        InputStream in = null;
//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            in = BaseApplication.getContext().getResources().openRawResource(id);
//            X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
//            return cert;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }


}