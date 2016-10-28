import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

// Use a larger thread pool to speed things up.
val exec = Executors.newFixedThreadPool(40)

// Simulate performing an asynchronous task that takes a set amount of time
// and either succeeds (returning a String) or throws an exception.
fun task(latency: Long, success: Boolean) = CompletableFuture.supplyAsync {
    Thread.sleep(latency)
    if (success) "success ($latency)"
    else throw RuntimeException("failure ($latency)")
}

// Helper that returns a CompletableFuture that will complete with the first
// successful result from a list of futures, or complete exceptionally if
// they all fail.
fun <T> successfulOf(cfs: Collection<CompletableFuture<T>>) : CompletableFuture<T> {
    // Create a new future that will hold the first success or ultimate failure.
    val cf = CompletableFuture<T>()

    // Complete our new future with the result of any of the list of futures
    // that finishes successfully.
    cfs.forEach { it.thenAccept { r -> cf.complete(r) } }

    // In case none of them complete successfully, wait for all of them to
    // complete, then complete our future with an exception if and only if
    // all of them failed.
    CompletableFuture.allOf(*cfs.toTypedArray()).whenComplete { r,e ->
        if (cfs.all { it.isCompletedExceptionally }) cf.completeExceptionally(RuntimeException("all failed!"))
    }

    return cf
}

successfulOf(listOf(
        task(100, true), // this should win
        task(200, false),
        task(300, false),
        task(400, false)
)).thenApply(::println).join()


successfulOf(listOf(
        task(100, false),
        task(200, true), // this should win
        task(300, true),
        task(400, false)
)).thenApply(::println).join()

successfulOf(listOf(
        task(100, false),
        task(200, false),
        task(300, false),
        task(400, true) // this should win
)).thenApply(::println).join()

successfulOf(listOf(
        task(100, false),
        task(200, false),
        task(300, false),
        task(400, false) // all fail; should get an exception
)).thenApply(::println).join()


/*

success (100)
success (200)
success (400)
java.util.concurrent.CompletionException: java.lang.RuntimeException: all failed!

*/
