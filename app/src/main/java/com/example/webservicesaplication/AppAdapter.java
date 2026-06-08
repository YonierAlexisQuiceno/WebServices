package com.example.webservicesaplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = "AppAdapter";
    private String url = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private RequestQueue requestQueue;
    private List<AppModel> appList;
    private Context context;
    private ProgressBar progressBar;
    private TextView textError;

    public AppAdapter(@NonNull Context context, ProgressBar progressBar, TextView textError) {
        super(context, R.layout.adapter_app, new ArrayList<>());
        this.context = context;
        this.appList = new ArrayList<>();
        this.requestQueue = Volley.newRequestQueue(context);
        this.progressBar = progressBar;
        this.textError = textError;

        // Mostrar ProgressBar mientras se cargan los datos
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response);
                        notifyDataSetChanged();
                        // Ocultar ProgressBar al cargar exitosamente
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores: mostrar mensaje al usuario
                Log.e(TAG, "Error al cargar datos: " + error.getMessage());
                Toast.makeText(context, "Error al cargar los datos. Verifique su conexión.", Toast.LENGTH_LONG).show();
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if (textError != null) {
                    textError.setVisibility(View.VISIBLE);
                }
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

                // Manejo seguro del campo rights (puede no existir en algunas entries)
                String rights = "";
                if (obj.has("rights")) {
                    rights = obj.getJSONObject("rights").getString("label");
                }

                JSONArray images = obj.getJSONArray("im:image");
                String image = images.getJSONObject(2).getString("label");

                String category = obj.getJSONObject("category").getJSONObject("attributes").getString("label");

                AppModel appModel = new AppModel(name, image, summary, rights, category);
                appList.add(appModel);
                add(appModel);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando JSON: " + e.getMessage());
            Toast.makeText(context, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Patrón ViewHolder para mejorar rendimiento del scroll
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_app, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textRights = convertView.findViewById(R.id.textRights);
            holder.textSummary = convertView.findViewById(R.id.textSummary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppModel currentItem = getItem(position);

        if (currentItem != null) {
            holder.textName.setText(currentItem.getName());
            holder.textRights.setText(currentItem.getRights());
            holder.textSummary.setText(currentItem.getSummary());

            // Imagen por defecto mientras carga
            holder.imageView.setImageResource(R.drawable.phone);

            ImageRequest imageRequest = new ImageRequest(currentItem.getImage(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.imageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error cargando imagen: " + error.getMessage());
                            // Mantener imagen por defecto si falla
                        }
                    });
            requestQueue.add(imageRequest);
        }

        return convertView;
    }

    // ViewHolder para mejorar rendimiento del ListView
    private static class ViewHolder {
        ImageView imageView;
        TextView textName;
        TextView textRights;
        TextView textSummary;
    }
}
