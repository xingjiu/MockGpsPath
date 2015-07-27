package ledongli.cn.mockgpspath.thread;

import android.os.Handler;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ledongli.cn.mockgpspath.common.GlobalConfig;

/**
 * Created by wangyida on 15-3-31.
 */
public class ThreadPool {
    private static final boolean IS_DEBUG = false;

    private static final int CPU_CORES = 3;

    private static ScheduledExecutorService sScheduledPool = Executors.newScheduledThreadPool(CPU_CORES);

    private static ScheduledExecutorService sSingleExecutor = Executors.newSingleThreadScheduledExecutor();

    private static Handler sUiHandler = null;


    public static Future<?> runOnPool(Runnable r) {
        return sScheduledPool.submit(r);
    }

    public static void runOnUi(Runnable r) {
        if(sUiHandler == null) {
            sUiHandler = new Handler(GlobalConfig.getAppContext().getMainLooper());
        }
        sUiHandler.post(r);
    }

    public static void postOnUiDelayed(Runnable r, long delay) {
        if(sUiHandler == null) {
            sUiHandler = new Handler(GlobalConfig.getAppContext().getMainLooper());
        }
        sUiHandler.postDelayed(r, delay);
    }

    public static void runOnWorker(Runnable r) {
        sSingleExecutor.submit(r);
    }

    public static void postOnWorkerDelayed(Runnable r, int delay) {
        sSingleExecutor.schedule(r, delay, TimeUnit.MILLISECONDS);
    }

    public static void postOnPoolDelayed(Runnable r, int delay) {
         sScheduledPool.schedule(r, delay, TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        sScheduledPool.shutdown();
        sSingleExecutor.shutdown();
    }
}

