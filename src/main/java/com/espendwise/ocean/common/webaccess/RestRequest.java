package com.espendwise.ocean.common.webaccess;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RestRequest {

    private static Log logger = LogFactory.getLog(RestRequest.class);

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String UTF8 = "UTF-8";
    private static final String ACCEPT_HEADER_PARAM = "accept";

    private String url;

    public RestRequest(String hostAddress, String port, String path) {
        this.url = createConnectionUrl(hostAddress, port, path);
    }

    public RestRequest(String hostAddress, String path) {
        this.url = createConnectionUrl(hostAddress, null, path);
    }

    public <R> R doPut(Object o, ObjectTokenType<BasicResponseValue<R>> returnClass) {

        BasicResponseValue<R> response = doPutInternal(o, returnClass);


        if (response != null) {

            if (hasErrors(response)) {

                throw new WebAccessException(getUrl(),
                        response.getStatus(),
                        response.getErrors()
                );

            }

            return response.getObject();

        } else {

            throw new WebAccessResponseException(getUrl());

        }

    }

    public <R> R doGet(List<String[]> parameters,  ObjectTokenType<BasicResponseValue<R>> returnClass) {

        BasicResponseValue<R> response = doGetInternal(parameters, returnClass);

        if (response != null) {

            if (hasErrors(response)) {

                throw new WebAccessException(getUrl(),
                        response.getStatus(),
                        response.getErrors()
                );

            }

            return response.getObject();

        } else {

            throw new WebAccessResponseException(getUrl());

        }

    }


    public  <R> BasicResponseValue<R> doGetInternal(List<String[]> parameters,  ObjectTokenType<BasicResponseValue<R>> returnClass) {

        BasicResponseValue<R> response = null;

        try {

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            for (String[] p : parameters) {
                qparams.add(new BasicNameValuePair(p[0], p[1]));
            }

            HttpGet request = new HttpGet(getUrl(qparams));
            response = doRequest(request, returnClass);


        } catch (Exception e) {
            logger.error("doGetInternal()=> ERROR: " + e.getMessage(), e);
        }

        return response;
    }



    private <R> BasicResponseValue<R> doPutInternal(Object o,  ObjectTokenType<BasicResponseValue<R>> returnClass) {

        BasicResponseValue<R> response = null;

        try {

            HttpPut request = new HttpPut(getUrl());

            if (o != null) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

                String jsonString = gson.toJson(o);
                StringEntity s = new StringEntity(jsonString, UTF8);
                s.setContentEncoding(UTF8);
                s.setContentType(JSON_CONTENT_TYPE);
                request.setEntity(s);
            }

            if (returnClass != null) {
                request.addHeader(ACCEPT_HEADER_PARAM, JSON_CONTENT_TYPE);
            }

            response = doRequest(request, returnClass);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("doPutInternalt()=> ERROR: " + e.getMessage(), e);
        }

        return response;
    }

    private <R> BasicResponseValue<R> doRequest(HttpRequestBase request,  ObjectTokenType<BasicResponseValue<R>> returnClass) throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();

        logger.error("doRequest()=> request " + request);
        logger.error("doRequest()=> request.URI " + request.getURI());

        HttpResponse httPesponse = httpClient.execute(request);

        logger.error("doRequest()=> httPesponse " + httPesponse);

        if (httPesponse.getStatusLine().getStatusCode() == 200) {

            HttpEntity entity = httPesponse.getEntity();
            if (entity != null && returnClass != null) {
                return readHttpResponseObject(entity, returnClass);
            }
        }

        return null;


    }



    public String getUrl() {
        return url;
    }

    private static boolean hasErrors(BasicResponseValue response) {
        return response != null
                && response.getErrors() != null
                && !response.getErrors().isEmpty();
    }

    private String getUrl(List<NameValuePair> qparams) {
        if (!qparams.isEmpty()) {
            return getUrl() + "?" + URLEncodedUtils.format(qparams, UTF8);
        } else {
            return getUrl();
        }
    }

    private String createConnectionUrl(String hostAddress, String port, String path) {
        return "http://" + hostAddress + (port != null ? (":" + port) : "") + "/restws" + path;
    }

    private <R> BasicResponseValue<R> readHttpResponseObject(HttpEntity entity,  ObjectTokenType<BasicResponseValue<R>> returnType) {
       
        JsonReader reader = null;
       
        try {
         
            reader = new JsonReader(new InputStreamReader(entity.getContent(), UTF8));

            logger.error("readHttpResponseObject()=> reader: " + reader);

            BasicResponseValue<R> v =  new Gson().fromJson(reader,  returnType.getType());

            logger.error("readHttpResponseObject()=> v: " + v);

            return v;

        } catch (Exception e) {   // ignore
            e.printStackTrace();
            logger.error("readHttpResponseObject()=> ERROR: " + e.getMessage());
        } finally {
            closeJsonReader(reader);
        }
        return null;
    }

    private void closeJsonReader(JsonReader reader) {
        if (reader != null) {
            try { reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("closeJsonReader()=> ERROR: " + e.getMessage());
            }
        }
    }


}
