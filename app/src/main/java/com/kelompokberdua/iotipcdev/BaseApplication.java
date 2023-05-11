package com.kelompokberdua.iotipcdev;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kelompokberdua.iotipcdev.feature.LoginActivity;
import com.kelompokberdua.iotipcdev.feature.RegisterActivity;
import com.kelompokberdua.iotipcdev.util.CameraUtils;
import com.thingclips.smart.api.MicroContext;
import com.thingclips.smart.api.router.UrlBuilder;
import com.thingclips.smart.api.service.RedirectService;
import com.thingclips.smart.api.service.RouteEventListener;
import com.thingclips.smart.api.service.ServiceEventListener;
import com.thingclips.smart.home.sdk.ThingHomeSdk;
import com.thingclips.smart.optimus.sdk.ThingOptimusSdk;
import com.thingclips.smart.sdk.api.INeedLoginListener;
import com.thingclips.smart.wrapper.api.ThingWrapper;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThingHomeSdk.init(this, "9qwhgqgqduqwxprgeaya", "4afmvwj95kphj57wvkafh3rh7fyv5su3");
        ThingHomeSdk.setDebugMode(true);
        ZXingLibrary.initDisplayOpinion(this);
        CameraUtils.init(this);

        /** Check is session expired */
        ThingHomeSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Toast.makeText(context, "Session Expired", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BaseApplication.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Initializes the BizBundle.
//        Fresco.initialize(this);
        ThingWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                // The callback of an unimplemented route.
                // An unresponsive touch indicates an unimplemented route. To implement the route, set `urlBuilder.target` to the required route and set routing parameters in `urlBuilder.params`.
                Log.e("router not implement", urlBuilder.target + urlBuilder.params.toString());
                Toast.makeText(getBaseContext(), "router not implement "+urlBuilder.target + urlBuilder.params.toString(), Toast.LENGTH_SHORT).show();
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                // The callback of an unimplemented service.
                Log.e("service not implement", serviceName);
            }
        });
        ThingOptimusSdk.init(this);

        // Registers the home service. This service is not required for the Mall UI BizBundle.
//        ThingWrapper.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());
        // Intercepts an existing route and navigates to a custom page based on specific parameters.
        RedirectService service = MicroContext.getServiceManager().findServiceByInterface(RedirectService.class.getName());
        service.registerUrlInterceptor(new RedirectService.UrlInterceptor() {
            @Override
            public void forUrlBuilder(UrlBuilder urlBuilder, RedirectService.InterceptorCallback interceptorCallback) {
                //Such as:
                // Intercept the event of tapping the top-right corner of the panel to navigate to a custom page specified by the parameters of `urlBuilder`.
                if (urlBuilder.target.equals("panelAction") && urlBuilder.params.getString("action").equals("gotoPanelMore")) {
                    interceptorCallback.interceptor("interceptor");
                    Log.e("interceptor", urlBuilder.params.toString());
                } else {
                    interceptorCallback.onContinue(urlBuilder);
                }
            }
        });
    }
//
//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
