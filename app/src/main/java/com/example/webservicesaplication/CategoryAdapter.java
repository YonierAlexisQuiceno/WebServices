package com.example.webservicesaplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<AppModel> {
    private String url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private RequestQueue requestQueue;
    private List<AppModel> appList;
    private Context context;

    public CategoryAdapter(@NonNull Context context) {
        super(context, R.layout.adapter_category, new ArrayList<>());
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_category, parent, false);
        }

        AppModel currentItem = getItem(position);

        TextView textCategory = convertView.findViewById(R.id.textCategory);
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textSummary = convertView.findViewById(R.id.textSummary);

        if (currentItem != null) {
            textCategory.setText(currentItem.getCategory());
            textName.setText(currentItem.getName());
            textSummary.setText(currentItem.getSummary());
        }

        return convertView;
    }
}
