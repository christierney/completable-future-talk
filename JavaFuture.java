import java.util.*;
import java.util.concurrent.*;

// A traditional Future has only synchronous interactions:
class JavaFuture {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future f = exec.submit(new Callable<String>() {
            public String call() throws Exception {
                Thread.sleep(1000);
                return "Future done at " + new Date();
            }
        });
        System.out.println("Before: " + new Date());
        System.out.println(f.get()); // This call will block...
        System.out.println("After: " + new Date()); // ...so this will print last.

        // Allow submitted tasks to finish, then exit.
        exec.shutdown();
    }
}
