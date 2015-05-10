package com.example.bellng.trackall;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bellng on 10/05/2015.
 */
public class ItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<ListItem> items;

    public ItemAdapter(Context context, ArrayList<ListItem> items){
        this.context = context;
        this.items = items;
    }

    public int getCount(){
        return items.size();
    }

    public ListItem getItem(int i){
        return items.get(i);
    }

    public long getItemId(int i){
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup){
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        TextView title = (TextView) view.findViewById(R.id.titleLabel);
        TextView description = (TextView) view.findViewById(R.id.descLabel);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);

        ListItem item = items.get(i);

        title.setText(item.getTitle());
        description.setText(item.getDescription());
        if(image.getDrawable() == null) new ImageLoadTask(item.getImageURL(), image).execute();

        return view;
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
