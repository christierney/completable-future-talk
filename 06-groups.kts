import java.util.*
import java.util.concurrent.*

val sr = SplittableRandom()

val cfs = (1..4).map { i ->
    CompletableFuture.runAsync {
        val delay = sr.split().nextLong(3000)
        Thread.sleep(delay)
        println("$i finished!")
    }
}

// This does not let you do anything directly with the results of the
// individual futures, but lets you wait until they are all complete.
CompletableFuture.allOf(*cfs.toTypedArray()).join()

/*

2 finished!
1 finished!
3 finished!
4 finished!

 */

val cfs2 = (1..4).map { i ->
    CompletableFuture.supplyAsync {
        val delay = sr.split().nextLong(3000)
        Thread.sleep(delay)
        "$i finished first!"
    }
}

// This does not let you supply a function argument, but you can use apply
// or any other chaining method to act on the result of the first one that
// completes.
CompletableFuture.anyOf(*cfs2.toTypedArray()).thenApply(::println).join()

/*

3 finished first!

 */
