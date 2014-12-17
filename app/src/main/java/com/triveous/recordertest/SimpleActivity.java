package com.triveous.recordertest;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.triveous.recordertest.iab.CheckoutActivity;
import com.triveous.recordertest.iab.InAppBilling;

import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import java.util.ArrayList;

import hugo.weaving.DebugLog;

import static java.util.Arrays.asList;

/**
 * Created by sohammondal on 17/12/14.
 */
public class SimpleActivity extends CheckoutActivity {
    public ArrayList<SkuUi> skuArrayList;
    private CustomAdapter adapter;
    public ListView listView;

    @Override
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
                        @DebugLog
                        public void onSuccess(Purchase purchase) {
                            Toast.makeText(SimpleActivity.this, "Successfully bought " + skuUi.sku.title, Toast.LENGTH_LONG).show();
                            loadInventory(asList("com.triveous.skyrotest1", "com.triveous.skyrotest2", "com.triveous.skyrotest3", "android.test.purchased"),
                                    new InAppBilling.InventoryLoadedListener() {
                                        @Override
                                        @DebugLog
                                        public void OnInventoryLoaded(ArrayList<SkuUi> returnedSkuArrayList) {
                                            skuArrayList = returnedSkuArrayList;
                                            adapter = new CustomAdapter(SimpleActivity.this, skuArrayList);
                                            listView.setAdapter(adapter);
                                        }
                                    });
                        }

                        @Override
                        public void onError(int i, Exception e) {
                            Toast.makeText(SimpleActivity.this, "Could not buy " + skuUi.sku.title, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(SimpleActivity.this, "Nothing to buy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadInventory(asList("com.triveous.skyrotest1", "com.triveous.skyrotest2", "com.triveous.skyrotest3", "android.test.purchased"),
                new InAppBilling.InventoryLoadedListener() {
                    @Override
                    public void OnInventoryLoaded(ArrayList<SkuUi> returnedSkuArrayList) {
                        skuArrayList = returnedSkuArrayList;
                        adapter = new CustomAdapter(SimpleActivity.this, skuArrayList);
                        listView.setAdapter(adapter);
                    }
                });


    }
}
