package net.kylestew.AndroidMeme;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    
    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<ProgressBar> progressBarReference;
    
    public BitmapDownloaderTask(ImageView imageView, ProgressBar progressBar) {
        this.imageViewReference = new WeakReference<ImageView>(imageView);
        this.progressBarReference = new WeakReference<ProgressBar>(progressBar);
    }
    
    @Override
    protected Bitmap doInBackground(String... params)
    {
        String url = params[0];        
        HttpClient client = new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) { 
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 
                return null;
            }
            
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent(); 
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();  
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (isCancelled()) {
            bitmap = null;
        }
        
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            ProgressBar progressBar = progressBarReference.get();
            if (imageView != null)  {
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        }
    }
    
}
