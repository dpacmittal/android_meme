package net.kylestew.AndroidLibs.RESTClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Observable;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

public class RESTCall extends Observable {

    // REST Actions
    public static final String ACTION_CREATE = "action_create";
    public static final String ACTION_UPDATE = "action_update";
    
    // HTTP Status Codes
    public static final int OK_200 = 200;
    public static final int CREATED_201 = 201;

    public enum RestVerb {
        GET, PUT, POST, DELETE;
    }

    public enum ContentType {
        JSON, XML;
    }

    private RestVerb restVerb;
    private ContentType contentType;
    private String url;
    private int expectedResult;
    private ArrayList<BasicHeader> headers;
    private RESTResponse restResponse;
    private ArrayList<ModelNameValue> params;

    public RESTCall() {
        restVerb = RestVerb.GET;
        contentType = ContentType.JSON;
        expectedResult = OK_200;
        headers = new ArrayList<BasicHeader>();
        params = new ArrayList<ModelNameValue>();
    }

    public void setRestVerb(RestVerb restVerb) {
        this.restVerb = restVerb;
    }

    public RestVerb getRestVerb() {
        return restVerb;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setExpectedResult(int expectedResult) {
        this.expectedResult = expectedResult;
    }

    public int getExpectedResult() {
        return expectedResult;
    }

    public void putHeader(String name, String value) {
        headers.add(new BasicHeader(name, value));
    }

    private <T extends HttpRequestBase> void addHeaders(T request) {
        if (contentType == ContentType.JSON) {
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
        } else {
            request.addHeader("Accept", "application/xml");
            request.addHeader("Content-Type", "application/xml");
        }
        for (BasicHeader header : headers) {
            request.addHeader(header);
        }
    }

    public void putParam(String name, String value) {
        params.add(new ModelNameValue(name, value));
    }

    public void putParam(String model, String name, String value) {
        params.add(new ModelNameValue(model, name, value));
    }

    public void putParam(String name, int value) {
        putParam(name, value + "");
    }

    public void putParam(String name, double value) {
        putParam(name, value + "");
    }

    public void putParam(String name, float value) {
        putParam(name, value + "");
    }

    public void putParam(String name, CharSequence value) {
        putParam(name, value.toString());
    }

    public void putParam(String name, Boolean value) {
        putParam(name, value.toString());
    }

    public void putParam(String model, String name, int value) {
        putParam(model, name, value + "");
    }

    public void putParam(String model, String name, double value) {
        putParam(model, name, value + "");
    }

    public void putParam(String model, String name, float value) {
        putParam(model, name, value + "");
    }

    public void putParam(String model, String name, CharSequence value) {
        putParam(model, name, value.toString());
    }

    public void putParam(String model, String name, Boolean value) {
        putParam(model, name, value.toString());
    }

    private String compileParams() throws JSONException {
        JSONObject data = new JSONObject();

        for (ModelNameValue param : params) {
            if (param.model == null) {
                data.put(param.name, param.value);
            } else {
                JSONObject modelData = data.optJSONObject(param.model);
                if (modelData == null) {
                    modelData = new JSONObject();
                    modelData.put(param.name, param.value);
                    data.put(param.model, modelData);
                } else {
                    modelData.put(param.name, param.value);
                }
            }
        }
        return data.toString();
    }

    public void setResponse(RESTResponse restResponse) {
        this.restResponse = restResponse;
        setChanged();
        notifyObservers(restResponse);
    }

    public RESTResponse getResponse() {
        return restResponse;
    }

    public HttpRequestBase getRequest() throws UnsupportedEncodingException, JSONException {
        HttpRequestBase request = null;
        switch (restVerb) {
            case GET:
                request = new HttpGet(url);
                addHeaders(request);
                break;
            case PUT:
                request = new HttpPut(url);
                addHeaders(request);
                ((HttpPut)request).setEntity(new StringEntity(compileParams()));
                break;
            case POST:
                request = new HttpPost(url);
                addHeaders(request);
                ((HttpPost)request).setEntity(new StringEntity(compileParams()));
                break;
            case DELETE:
                request = new HttpDelete(url);
                addHeaders(request);
                break;
        }
        return request;
    }

    private class ModelNameValue {
        public String model;
        public String name;
        public String value;

        public ModelNameValue(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public ModelNameValue(String model, String name, String value) {
            this.model = model;
            this.name = name;
            this.value = value;
        }
    }

}
