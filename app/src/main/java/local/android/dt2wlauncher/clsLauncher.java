package local.android.dt2wlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class clsLauncher extends Activity {

    private Thread.UncaughtExceptionHandler m_androidDefaultUEH;
    private Thread.UncaughtExceptionHandler m_appUEH= new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            final Throwable exth = ex;
            new Thread() {
                @Override
                public void run() {

                    String msg;
                    if (exth.getCause() instanceof android.content.ActivityNotFoundException) {
                        msg = "Dt2w.apk not installed, install app then run Dt2wLauncher.";

                    } else {
                        msg = exth.getMessage();

                    }
                    Log.d("Dt2wLaucher", msg);
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    //commented: restart activity...
                    //System.exit(0);
                    Looper.loop();
                }
            }.start();

            try
            {
                Thread.sleep(8000); // Let the Toast display before app will get shutdown
            }
            catch (InterruptedException e)
            {
                // Ignored.
            }

            //commented: run default exception handler now...
            //m_androidDefaultUEH.uncaughtException(thread, appex);

            finish();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* UNCAUGHT EXCEPTIONS */
        m_androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(m_appUEH);
        /* END UNCAUGHT EXCEPTIONS */
        Intent intent = new Intent().addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction("com.huawei.action.MOTION_SETTINGS");

        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
