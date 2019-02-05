package com.sashaermolenko.fastorder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;
import com.sashaermolenko.fastorder.Adapters.DishRecyclerAdapter;
import com.sashaermolenko.fastorder.Items.DishItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DishActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private DishRecyclerAdapter adapter;
    private Context context;
//    public static ArrayList<DishItem> items_1 = new ArrayList<>();
//    public static ArrayList<DishItem> items_2 = new ArrayList<>();
//    public static ArrayList<DishItem> items_3 = new ArrayList<>();
//    public static ArrayList<DishItem> items_4 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        context = this;
//
//        if(MainActivity.flag == 0) {
//            items_1.add(new DishItem("Апельсиновый", "http://images.media-allrecipes.com/userphotos/960x960/3758394.jpg", 0, "50", "asd"));
//            items_1.add(new DishItem("Яблочный", "https://kedem.ru/photo/articles/2016/08/yablochnyj-sok-06.jpg", 1, "60", "asd"));
//            items_1.add(new DishItem("Мультифруктовый", "https://vashvkus.ru/system/ingredients/images/images/5378/90cf/7661/7305/8ec4/0500/full/mfru-1.jpg?1400410314", 1, "50", "asd"));
//            items_1.add(new DishItem("Вишневый", "https://bujinfo.am/wp-content/uploads/2018/07/bali-hyut.jpg", 3, "40", "asd"));
//
//            items_2.add(new DishItem("Маргарита", "https://koken.medialaancdn.be/sites/koken.vtm.be/files/styles/vmmaplayer_big/public/recipe/image/pizza_margharita_0.jpg", 0, "300", "asd"));
//            items_2.add(new DishItem("Гавайская", "http://kulinarnia.ru/wp-content/uploads/2016/01/pitstsa-gavayskaya-s-ananasami-recept.jpg", 1, "300", "asd"));
//            items_2.add(new DishItem("Баварская", "https://kor.ill.in.ua/m/610x385/1458096.jpg", 2, "300", "asd"));
//
//            items_3.add(new DishItem("Мокко", "http://v.img.com.ua/b/600x500/8/82/8e38975cc36fc67b81b006c7a6930828.jpg", 0, "300", "asd"));
//            items_3.add(new DishItem("Латте", "https://www.nespresso.com/ncp/res/uploads/recipes/1900px_Global_OL_ALL_Coffee%20Moment_Recipe_Latte%20Macchiato_2017_2019.jpg", 1, "300", "asd"));
//            items_3.add(new DishItem("Капучино", "http://coffeemap.com.ua/wp-content/uploads/2018/09/kapuchino-recept-i-tonkosti-prigotovlenija.png", 2, "300", "asd"));
//
//            items_4.add(new DishItem("Рибай", "https://easydine.ru/wp-content/uploads/2016/01/steyk-ribay-1.jpg", 0, "1800", "Стейк рибай - это высококачественная мраморная говядина, имеющая достаточно большое количество жировых прослоек."));
//            items_4.add(new DishItem("Томагавк", "https://t-bone.ua/wp-content/uploads/2017/08/tomagavk-stejk.jpg", 1, "1600", "Стейк Томагавк — это рибай на кости, у которого реберная кость сохранена почти полностью."));
//            items_4.add(new DishItem("Тибон", "http://2tarelki.ru/wp-content/uploads/2017/07/IMG_2675.jpg", 2, "1600", "Ти-Бон — это классический мужской стейк. Его покупают гурманы, тонко чувствующие разницу мраморного мяса и осознающие свои предпочтения. "));
//
//            MainActivity.flag = 1;
//        }
        recyclerView = (RecyclerView) findViewById(R.id.dishRecView);

        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        adapter = new DishRecyclerAdapter(context);
        recyclerView.setAdapter(adapter);

        Slidr.attach(this);
    }
}
