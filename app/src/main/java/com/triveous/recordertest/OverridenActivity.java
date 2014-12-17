package com.triveous.recordertest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.triveous.recordertest.iab.OverridenCheckoutActivity;

import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

import static java.util.Arrays.asList;

/**
 * Created by sohammondal on 17/12/14.
 */
public class OverridenActivity extends OverridenCheckoutActivity {
    public ArrayList<SkuUi> skuArrayList;
    private CustomAdapter adapter;
    public ListView listView;

    @Override
    public void OnInventoryLoaded(final ArrayList<SkuUi> skuArrayList) {
        this.skuArrayList = skuArrayList;
        adapter = new CustomAdapter(this, this.skuArrayList);
        listView.setAdapter(adapter);
    }

    @Override
    public List<String> getSkus() {
        return asList("com.triveous.skyrotest1", "com.triveous.skyrotest2", "com.triveous.skyrotest3", "android.test.purchased");
    }

    @Override
    @DebugLog
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (skuArrayList != null) {
                    final SkuUi skuUi = skuArrayList.get(position);
                    purchase(skuUi, new RequestListener<Purchase>() {
                        @Override
                        public void onSuccess(Purchase purchase) {
                            Toast.makeText(OverridenActivity.this, "Successfully bought " + skuUi.sku.title, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(int i, Exception e) {
                            Toast.makeText(OverridenActivity.this, "Could not buy " + skuUi.sku.title, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(OverridenActivity.this, "Nothing to buy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadInventory();
    }
}
