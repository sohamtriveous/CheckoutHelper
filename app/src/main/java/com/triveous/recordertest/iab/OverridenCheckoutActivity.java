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
public abstract class OverridenCheckoutActivity extends Activity implements InAppBilling.InventoryLoadedListener {
    private ActivityCheckout activityCheckout;
    private static boolean firstLoad = true;

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


    /**
     * Needs to be overriden by the user to return the skus of the items that need to be loaded
     * @return
     */
    public abstract List<String> getSkus();

    @DebugLog
    private void start() {
        // clear our s
        if(firstLoad) {
            InAppBilling.clearSkus();
            firstLoad = false;
        }
        InAppBilling.init(getApplicationContext(), getSkus());
        activityCheckout = Checkout.forActivity(this, InAppBilling.getCheckout(getApplicationContext()));
        activityCheckout.start();
    }

    @DebugLog
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
