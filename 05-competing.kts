import java.util.*
import java.util.concurrent.*


// Use multiple threads so that either future has a chance to complete first.
val exec = Executors.newFixedThreadPool(2)
val sr = SplittableRandom()
val cf1 = CompletableFuture<String>()
val cf2 = CompletableFuture<String>()

// Complete each future after a random amount of time.
listOf(cf1, cf2).forEachIndexed { i, cf ->
    exec.submit {
        val delay = sr.split().nextLong(1000)
        Thread.sleep(delay)
        cf.complete("$i finished first!")
    }
}

// Println the result of whichever future completes first.
cf1.acceptEither(cf2, ::println)

/*

> for i in {1..5}; do kotlinc -script 05-competing.kts ; done
0 finished first!
0 finished first!
0 finished first!
1 finished first!
1 finished first!

 */

// Allow submitted tasks to finish, then exit.
exec.shutdown()
