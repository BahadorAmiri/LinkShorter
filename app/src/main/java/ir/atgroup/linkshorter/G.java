package ir.atgroup.linkshorter;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.tapsell.sdk.Tapsell;

public class G extends Application {

    public static String TAPSELL_KEY = "gntbsbtefrrftbrjtgdmjhpdknfbigbmqkgmmlhhgghpedojqnrsiijdeaflntsogmolap";
    public static String TAPSELL_ZONE_ID = "610348422a39551abb9986d0";

    public static String GMAIL_SENDER = "bahadoramiri.report@gmail.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Tapsell.initialize(this, TAPSELL_KEY);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Dana-Light.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

}
