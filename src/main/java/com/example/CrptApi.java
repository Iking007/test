package com.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public class CrptApi implements Runnable{
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private int sizeRequest = 0;


    public CrptApi(TimeUnit timeUnit, int requestLimit){ // Конструктор
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        new Thread(this).start();          
    }

    public void request(JSONObject json) throws IOException, InterruptedException{ //Запрос
        if ( sizeRequest >= requestLimit) {
            System.out.println("Запрос заблокирован");    
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        sizeRequest++;
    }

    @Override
    public void run() {
        CountDownLatch countDownLatch = new CountDownLatch(requestLimit);
        try {
            while (true) {
                countDownLatch.await(1, this.timeUnit);
                this.sizeRequest = 0;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        JSONObject json = new JSONObject();
        JSONObject jsonParticipantInn = new JSONObject();
        Date date = new Date(120, 1, 23);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jsonParticipantInn.put("participantInn", "string");
        json.put("description", jsonParticipantInn);
        json.put("doc_id", "string");
        json.put("doc_status", "string");
        json.put("doc_typ", "LP_INTRODUCE_GOODS");
        json.put("importRequest", true);
        json.put("owner_inn", "string");
        json.put("participant_inn", "string");
        json.put("producer_inn", "string");
        json.put("production_date", sdf.format(date));
        json.put("production_type", "string");
        List<Product> products = new ArrayList<Product>();
        Product product = new Product("string",
        sdf.format(date),
         "string", 
         "string", 
         "string",
         sdf.format(date),
           "string",
           "string",
           "string");
        products.add(product);
        json.put("products", products.toArray());
        json.put("reg_date", sdf.format(date));
        json.put("reg_number", "string");

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);
        while (true) {
            Thread.sleep(150);
            crptApi.request(json);
        }
        
    }

    public static class Product {
        String certificate_document, certificate_document_date, certificate_document_number,
            owner_inn, producer_inn, production_date, tnved_code, uit_code, uitu_code;
        public Product(String certificate_document, 
            String certificate_document_date, 
            String certificate_document_number,
            String owner_inn, 
            String producer_inn, 
            String production_date, 
            String tnved_code, 
            String uit_code, 
            String uitu_code){
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.certificate_document_number = certificate_document_number;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }
        public String getCertificate_document() {
            return certificate_document;
        }
        public String getCertificate_document_number() {
            return certificate_document_number;
        }
        public String getCertificate_document_date() {
            return certificate_document_date;
        }
        public String getOwner_inn() {
            return owner_inn;
        }
        public String getProducer_inn() {
            return producer_inn;
        }
        public String getProduction_date() {
            return production_date;
        }
        public String getTnved_code() {
            return tnved_code;
        }
        public String getUit_code() {
            return uit_code;
        }
        public String getUitu_code(){
            return this.uitu_code;
        }
        
    }
    
}

