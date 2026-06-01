package com.example.webservicesaplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends ArrayAdapter<AppModel> {
    private String url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private RequestQueue requestQueue;
    private List<AppModel> appList;
    private Context context;

    public AppAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_app, new ArrayList<>());
        this.context = context;
        this.appList = new ArrayList<>();
        this.requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response);
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });
        requestQueue.add(request);
    }

    private void parseJSON(JSONObject response) {
        try {
            JSONObject feed = response.getJSONObject("feed");
            JSONArray entry = feed.getJSONArray("entry");

            for (int i = 0; i < entry.length(); i++) {
                JSONObject obj = entry.getJSONObject(i);

                String name = obj.getJSONObject("im:name").getString("label");
                String summary = obj.getJSONObject("summary").getString("label");
                String rights = obj.getJSONObject("rights").getString("label");

                JSONArray images = obj.getJSONArray("im:image");
                String image = images.getJSONObject(2).getString("label");

                String category = obj.getJSONObject("category").getJSONObject("attributes").getString("label");

                AppModel appModel = new AppModel(name, image, summary, rights, category);
                appList.add(appModel);
                add(appModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_app, parent, false);
        }

        AppModel currentItem = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.image);
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textRights = convertView.findViewById(R.id.textRights);
        TextView textSummary = convertView.findViewById(R.id.textSummary);

        if (currentItem != null) {
            textName.setText(currentItem.getName());
            textRights.setText(currentItem.getRights());
            textSummary.setText(currentItem.getSummary());

            ImageRequest imageRequest = new ImageRequest(currentItem.getImage(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });
            requestQueue.add(imageRequest);
        }

        return convertView;
    }
}
