package teamawesome.cs180frontend.Misc;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public final class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
    }
}
