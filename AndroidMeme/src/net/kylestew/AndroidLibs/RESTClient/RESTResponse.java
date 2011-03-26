package net.kylestew.AndroidLibs.RESTClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RESTResponse implements Serializable {
	private static final long serialVersionUID = 1173746324329536516L;

	private Exception exception;
	private HttpResponse response;
	private boolean error;
	private JSONArray jsonArray;
	private String body;
	private int statusCode;

	public RESTResponse() {
		// Response only contains an error
		this.error = true;
	}

	public RESTResponse(HttpResponse response, int expectedResult) {
		this.response = response;
		this.error = false;
		this.statusCode = response.getStatusLine().getStatusCode();

		try {
			// Extract data from REST response
			InputStream content = response.getEntity().getContent();
			body = convertStreamToString(content);
			content.close();
			
			// Did we receive the expected result?
			if (response.getStatusLine().getStatusCode() != expectedResult)
			    error = true;

			// Try to convert to a JSON array first
			jsonArray = new JSONArray(body);
		} catch (IllegalStateException e) {
			setException(e);
		} catch (IOException e) {
			setException(e);
		} catch (JSONException e) {
			// Try to convert to a JSON object and wrap that in an array
			try {
				jsonArray = new JSONArray().put(new JSONObject(body));
			} catch (JSONException e1) {
				// Give up, but don't error
				// Some server responses only send a status line
				jsonArray = null;
			}
		}
	}

	public void setException(Exception exception) {
		this.exception = exception;
		this.error = true;
	}

	public Exception getException() {
		return exception;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public boolean hasError() {
		return error;
	}
	
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	
	public String getBody() {
		return body;
	}
	
	public int getStatusCode() {
	    return statusCode;
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means there's no
		 * more data to read. Each line will appended to a StringBuilder and returned as
		 * String.
		 */
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

}
