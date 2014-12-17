package com.triveous.recordertest.iab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.triveous.recordertest.SkuUi;
import com.triveous.recordertest.utils.LogUtils;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.Cache;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Products;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.Sku;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import hugo.weaving.DebugLog;

import static org.solovyev.android.checkout.ProductTypes.IN_APP;

/**
 * Created by sohammondal on 10/12/14.
 */
public class InAppBilling {
    private static Billing billing = null;
    private static Checkout checkout = null;

    private static Inventory inventory;
    private static ArrayList<SkuUi> skuArrayList;
    private static List<String> skus;

    @DebugLog
    public static Billing getBilling(final Context context) {
        if (billing == null) {
            if (context != null) {
                billing = new Billing(context, new Billing.Configuration() {
                    @Override
                    @DebugLog
                    public String getPublicKey() {
                        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgj+" +
                                "zqwrIRxLE6b0ozA/8HI2+LbIX7RY/+fRp6O1D3WxyqQoiN6VdGMlQk5bHKUUk3VPXpmroh26BNQmty" +
                                "fzMYC0X/U4jYaKZceguLRw1++ukW2wE0Ui8Lyu4LuLvRXmWjsTPLBVg8af7JNbqI/52OWSnlY9zmP9f18" +
                                "hIfCZS8AR40yh91X5Ef4jxGCwYAbfiy2CIX1sY5f5nQphaGST1dO1Z6NtE/M0m97LAv57nE1vg3R8l7T9W+WT" +
                                "mfLBPqxJttkqj3f/jRyT6TV+1q7+ek5VJ4aXSe+RF1wZOLqpJEG1AIXWMEsP2kW13N9mWtglzyepqRj67XzSTQ" +
                                "LWidvWkMQIDAQAB";
                    }

                    @Nullable
                    @Override
                    @DebugLog
                    public Cache getCache() {
                        return null;
                    }

                    // only when v3 is not supported
                    @Override
                    @DebugLog
                    public Inventory getFallbackInventory(Checkout checkout, Executor executor) {
                        return null;
                    }
                });
            } else {
                return null;
            }

        }
        if (billing != null) {
            billing.connect();
        }
        Log.w("InAppBilling", "Billing is " + billing);

        return billing;
    }

    @DebugLog
    public static Checkout getCheckout(final Context context) {
        if (checkout == null) {
            if (getBilling(context) != null) {
                checkout = Checkout.forApplication(getBilling(context), Products.create().add(IN_APP, skus));
            }
        }
        return checkout;
    }

    public static void clear() {
        checkout = null;
        inventory = null;
        skuArrayList = null;
        InAppBilling.skus = null;
    }

    public static void clearSkus() {
        InAppBilling.skus = null;
    }

    @DebugLog
    public static void init(final Context context, List<String> skus) {
        if (checkout == null || InAppBilling.skus == null) {
            if (InAppBilling.skus == null) {
                InAppBilling.skus = skus;
                checkout = null;
            }
            inventory = null;
            skuArrayList = null;
            getCheckout(context);
        }
    }

    @DebugLog
    public static void start(final Context context, final ActivityCheckout activityCheckout, final InventoryLoadedListener inventoryLoadedListener) {
        Checkout checkout = getCheckout(context);
        if (checkout != null) {
            loadInventory(activityCheckout, inventoryLoadedListener);
        }
    }

    @DebugLog
    public static void loadInventory(final ActivityCheckout activityCheckout, final InventoryLoadedListener inventoryLoadedListener) {
        inventory = activityCheckout.loadInventory();
        skuArrayList = new ArrayList<SkuUi>();

        inventory.whenLoaded(new Inventory.Listener() {
            @Override
            @DebugLog
            public void onLoaded(Inventory.Products products) {
                final Inventory.Product product = products.get(IN_APP);
                if (product.supported) {
                    skuArrayList.clear();
                    for (Sku sku : product.getSkus()) {
                        final Purchase purchase = product.getPurchaseInState(sku, Purchase.State.PURCHASED);

                        LogUtils.printLogD("Status: " + sku.title);
                        if (purchase != null) {
                            LogUtils.printLogD("Token: " + purchase.token);
                        }
                        skuArrayList.add(SkuUi.create(sku, purchase != null ? purchase.token : null));
                    }
                    inventoryLoadedListener.OnInventoryLoaded(skuArrayList);
                }
            }
        });
    }

    public static interface InventoryLoadedListener {
        public void OnInventoryLoaded(ArrayList<SkuUi> skuArrayList);
    }
}
