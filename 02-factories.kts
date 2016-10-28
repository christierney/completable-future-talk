import java.util.concurrent.CompletableFuture

// Runs the supplied callable for you and will automatically resolve
// the promise with a result when finished.
val cf = CompletableFuture.supplyAsync {
    Thread.sleep(500)
    42
}

// thenAccept runs a Consumer with the result of the future as its argument.
// The join() is so our script will not exit until the async task finishes.
cf.thenAccept(::println).join()

/*

42

 */

// Runs the supplied Runnable for you and will automatically resolve
// the promise when finished.
val cf2 = CompletableFuture.runAsync {
    Thread.sleep(500)
    println("done with my work")
}


// thenRun runs a Runnable after the future is completed.
// The join() is so our script will not exit until the async task finishes.
cf2.thenRun { println("async task finished") }.join()

/*

done with my work
async task finished

 */
