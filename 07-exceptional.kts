import java.util.concurrent.*

val exec = Executors.newSingleThreadExecutor()

val cf1 = CompletableFuture<Int>()
val cf2 = CompletableFuture<Int>()

exec.submit {
    cf1.completeExceptionally(RuntimeException("and so, I die!"))
    cf2.completeExceptionally(RuntimeException("and so, I die!"))
}

// if an exception occurs, substitute a default value for the next stage
// to use.
cf1.exceptionally { 42 }.thenApply(::println)

// whenComplete and handle take a single callback that receives either a result
// or an exception.
cf2.whenComplete { i, ex ->
    if (i != null) println(i)
    else println("Unexpected exception: $ex")
}


/*

42
Unexpected exception: java.lang.RuntimeException: and so, I die!

 */

// Allow submitted tasks to finish, then exit.
exec.shutdown()
