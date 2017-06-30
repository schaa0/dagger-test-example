package dagger.extension.example.scheduler;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class CurrentThreadExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }

    public static Scheduler scheduler() {
        return Schedulers.from(new CurrentThreadExecutor());
    }
}