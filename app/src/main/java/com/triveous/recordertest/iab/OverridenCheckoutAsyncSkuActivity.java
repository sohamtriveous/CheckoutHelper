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
public abstract class OverridenCheckoutAsyncSkuActivity extends Activity implements InAppBilling.InventoryLoadedListener {
    private ActivityCheckout activityCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityCheckout != null) {
            activityCheckout.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    /**
     * Here you need to manually start the checkout process by initiating a start
     * @param skus
     */
    private void start(List<String> skus) {
        InAppBilling.init(getApplicationContext(), skus);
        activityCheckout = Checkout.forActivity(this, InAppBilling.getCheckout(getApplicationContext()));
        activityCheckout.start();
    }

    private void startAndLoadInventory(List<String> skus) {
        start(skus);
        loadInventory();
    }

    private void stop() {
        if (activityCheckout != null) {
            activityCheckout.stop();
        }
    }

    public void loadInventory() {
        InAppBilling.loadInventory(activityCheckout, this);
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
