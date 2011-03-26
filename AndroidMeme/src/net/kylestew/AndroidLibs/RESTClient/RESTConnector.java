package net.kylestew.AndroidLibs.RESTClient;

import java.io.Serializable;
import java.util.LinkedList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class RESTConnector {

    public static final String REST_RESPONSE = "rest_response";
    private static LinkedList<RESTJob> callQueue = new LinkedList<RESTJob>();

    static {
        new Thread(new NetworkConnector()).start();
    }

    public void execute(final RESTCall restCall) {
        synchronized (callQueue) {
            RESTJob job = new RESTJob(restCall, new Handler(new Callback() {

                @Override
                public boolean handleMessage(Message message) {
                    synchronized (callQueue) {
                        RESTJob job = (RESTJob)message.getData().getSerializable(REST_RESPONSE);
                        job.restCall.setResponse(job.response);
                    }
                    return true;
                }
            }));
            callQueue.add(job);
            callQueue.notify();
        }
    }

    // Processes REST calls one at a time in order
    private static class NetworkConnector implements Runnable {

        private RESTJob job;

        @Override
        public void run() {
            while (true) {
                synchronized (callQueue) {
                    // Wait for a REST job to show up
                    while (callQueue.isEmpty()) {
                        try {
                            callQueue.wait();
                        } catch (InterruptedException e) {
                            // Ignored
                        }
                    }

                    // Run the REST job
                    job = callQueue.getFirst();
                    job.response = execute(job.restCall);
                    sendMessage(job);
                    callQueue.remove(job);
                }
            }
        }

        // Synchronized - only one REST call at a time
        private synchronized RESTResponse execute(RESTCall restCall) {
            RESTResponse restResponse = new RESTResponse();

            // Don't follow redirects (this may be a problem later, don't forget about this code)
            HttpParams params = new BasicHttpParams();
            HttpClientParams.setRedirecting(params, false);
            HttpClient client = new DefaultHttpClient(params);
            try {
                HttpResponse response = client.execute(restCall.getRequest());
                restResponse = new RESTResponse(response, restCall.getExpectedResult());
            } catch (Exception e) {
                restResponse.setException(e);
            }
            return restResponse;
        }

        private void sendMessage(RESTJob job) {
            Bundle data = new Bundle();
            data.putSerializable(REST_RESPONSE, job);
            Message message = Message.obtain();
            message.setData(data);
            job.handler.sendMessage(message);
        }

    }

    private class RESTJob implements Serializable {
        private static final long serialVersionUID = 9166364716714211420L;

        public RESTCall restCall;
        public Handler handler;
        public RESTResponse response;

        public RESTJob(RESTCall restCall, Handler handler) {
            this.restCall = restCall;
            this.handler = handler;
        }

    }

}
