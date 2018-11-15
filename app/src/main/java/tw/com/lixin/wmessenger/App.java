package tw.com.lixin.wmessenger;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

import tw.com.atromoby.utils.RegisterApplication;

public class App extends RegisterApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setLocaleTW (Context context){
        Locale locale = new Locale("zh", "rTW");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    public static void setLocaleEN (Context context){
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

}
