package org.example;

import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Main {
    public static void main(String[] args) {
        String message="Ciaooooooo mesasggio iniviato in automaticoooo";
        String webhookUrl = "https://discord.com/api/webhooks/1207700321149919272/8ekgJghdsRYupeA1kpzoJVs3e2vFnwxvIco_bRpBzxS0UlZ50PuNngzSBT69CBP75mIy";
        OkHttpClient client = createCustomOkHttpClient();


        // Costruzione del corpo della richiesta HTTP
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{\"content\":\"" + message + "\"}");

        // Creazione della richiesta HTTP POST
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        // Invio della richiesta HTTP
        try {
            Response response = client.newCall(request).execute();
            System.out.println("Notifica inviata con successo a Discord. Codice di risposta: " + response.code());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Si è verificato un errore durante l'invio della notifica a Discord.");
        }
    }
    public static OkHttpClient createCustomOkHttpClient(){
        //creo l'istaza del trust manager personalizzato
        X509TrustManager customTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        //Crazione SSLContext inizializzando con il trust manager personalizzato
        SSLContext sslContext;
        try{
            //configurazione dell'sslContext per utilizzare il protocollo TLS (Transport Layer Security)
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{customTrustManager}, new java.security.SecureRandom());
        }catch (Exception e){
            throw new RuntimeException("Errore durante la configurazione del SSLContext.", e);
        }
        //creazione OkHttpClient.Builder e configurazione del custom trust manager
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(),customTrustManager)
                .hostnameVerifier((hostname, session) -> true);
        return builder.build();
    }
}
