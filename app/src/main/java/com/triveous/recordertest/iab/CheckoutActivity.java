package com.triveous.recordertest.iab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.triveous.recordertest.SkuUi;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by sohammondal on 10/12/14.
 */
public class CheckoutActivity extends Activity  {
    private ActivityCheckout activityCheckout;
    private static boolean firstLoad = true;
    private static List<String> mSkus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        start();
        super.onCreate(savedInstanceState);
    }

    @Override
    @DebugLog
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(activityCheckout!=null) {
            activityCheckout.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    @DebugLog
    private void start() {
        // clear our s
        if(mSkus != null) {
            if (firstLoad) {
                InAppBilling.clearSkus();
                firstLoad = false;
            }
            InAppBilling.init(getApplicationContext(), mSkus);
            activityCheckout = Checkout.forActivity(this, InAppBilling.getCheckout(getApplicationContext()));
            activityCheckout.start();
        }
    }

    @DebugLog
    private void stop() {
        if (activityCheckout != null) {
            activityCheckout.stop();
        }
    }

    public void loadInventory(final List<String> skus, final InAppBilling.InventoryLoadedListener inventoryLoadedListener) {
        mSkus = skus;
        start();
        InAppBilling.loadInventory(activityCheckout, inventoryLoadedListener);
    }

    public void purchase(final SkuUi skuUi, RequestListener<Purchase> requestListener) {
        if (activityCheckout != null && skuUi != null) {
            if (!skuUi.isPurchased()) {
                activityCheckout.createOneShotPurchaseFlow(requestListener);
                activityCheckout.whenReady(new Checkout.ListenerAdapter() {
                    @Override
                    public void onReady(BillingRequests requests) {
                        requests.purchase(skuUi.sku, null, activityCheckout.getPurchaseFlow());
                    }
                });
            }
        } else {
            // something has not been initialised
        }
    }
}
