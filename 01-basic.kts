import java.util.*
import java.util.concurrent.*

// A traditional Future has only synchronous interactions:
val exec = Executors.newSingleThreadExecutor()
val f = exec.submit(Callable<String> {
    Thread.sleep(1000)
    "Future done at ${Date()}!"
})
println("Before: ${Date()}")
println(f.get()) // This call will block...
println("After: ${Date()}") // ...so this will print last.

/*

Before: Fri Oct 28 10:53:10 EDT 2016
Future done at Fri Oct 28 10:53:11 EDT 2016!
After: Fri Oct 28 10:53:11 EDT 2016

 */

// A CompletableFuture (promise) allows you to register
// non-blocking callbacks:
val cf = CompletableFuture<String>()
exec.submit {
    Thread.sleep(1000)
    cf.complete("CompletableFuture done at ${Date()}!")
}
println("Before: ${Date()}")
cf.thenAccept(::println) // This will not wait for the Future to complete...
println("After: ${Date()}") // ...so this will print first.

/*

Before: Fri Oct 28 10:55:19 EDT 2016
After: Fri Oct 28 10:55:19 EDT 2016
CompletableFuture done at Fri Oct 28 10:55:20 EDT 2016!

 */

// Allow submitted tasks to finish, then exit.
exec.shutdown()
