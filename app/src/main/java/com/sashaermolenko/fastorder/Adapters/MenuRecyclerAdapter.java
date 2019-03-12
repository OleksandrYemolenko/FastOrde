package com.sashaermolenko.fastorder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.sashaermolenko.fastorder.DishActivity;
import com.sashaermolenko.fastorder.Handler;
import com.sashaermolenko.fastorder.Items.MenuItem;
import com.sashaermolenko.fastorder.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.RecyclerViewHolder>{

    private Context context;
    public static int categoty;
    private ArrayList<MenuItem> items = new ArrayList<>();

    public MenuRecyclerAdapter(Context context) {
        this.context = context;
        items.add(new MenuItem("Соки", "https://healthynibblesandbits.com/wp-content/uploads/2016/11/How-to-Cut-a-Pomegranate-FF.jpg", 0));
        items.add(new MenuItem("Пицца", "https://www.slivki.by/znijki-media/w522_322/default/1009921/146.jpg", 1));
        items.add(new MenuItem("Кофе", "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/701/coffee-mug-1493946797.jpg", 2));
        items.add(new MenuItem( "Стейки", "https://cdn.lifehacker.ru/wp-content/uploads/2018/05/20-laJfxakov-dlya-prigotovleniya-deJstvitelno-bozhestvennogo-steJka_1526682003-1140x570.jpg", 3));

        new AsyncRequest().execute();
    }

    public void addAll(List<MenuItem> items) {
        int pos = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(pos, this.items.size());
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menuitem, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final MenuItem menuItem = items.get(position);

        holder.bind(menuItem);

        holder.itemView.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DishActivity.class);
                categoty = menuItem.getId();
                i.putExtra("id", categoty);
                context.startActivity(i);
                //MainActivity.ChangeAct(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            title.setTypeface(Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf"));

            image = (ImageView) itemView.findViewById(R.id.imgMenu);
        }

        public void bind(final MenuItem recyclerItem) {

            title.setText(recyclerItem.getName());
           // image.setImageResource(R.drawable.ic_dashboard_black_24dp);
            Picasso.with(context.getApplicationContext()).load(recyclerItem.getURL()).into(image);
        }
    }

    class AsyncRequest extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(Handler.createLink("menu.getCategories"));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String content = "", line = "";
                while((line = bf.readLine()) != null) {
                    content += line;
                }

                con.disconnect();
                return content;
            } catch (MalformedURLException e) {
                System.out.println(e);
            } catch (IOException e) {
                System.out.println(e);
            }

            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            ArrayList<MenuItem> items = new ArrayList<>();
            try {
                //System.out.println("REQUEST   ==== " + s);
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("response");
                for(int i = 0; i < arr.length(); ++i) {
                    String name = (String)arr.getJSONObject(i).get("category_name");
                    String photo = (String)arr.getJSONObject(i).get("category_photo");
                    int id = Integer.parseInt((String)arr.getJSONObject(i).get("category_id"));
                    items.add(new MenuItem(name, Handler.link + photo, id));
                }
            } catch (JSONException e) {
                System.out.println(e);
            }

            addAll(items);
        }
    }

}
