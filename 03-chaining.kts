import java.util.concurrent.*

// Here we create a future that we are responsible for completing ourselves.
val cf = CompletableFuture<Int>()
val exec = Executors.newSingleThreadExecutor()
exec.submit {
    Thread.sleep(1000)
    cf.complete(21)
}

println("starting...")
// thenApply runs a Function, which takes an argument and returns a value.
cf.thenApply { x ->
    x * 2
}.thenAccept { x -> // apply = argument but no return value
    println(x)
}.thenRun {         // run = no argument or return value
    println("finished!")
}

// All of these still return a CompletableFuture. The difference is in whether
// the cf will have a specific type like <Int> (apply + accept)
// or be type <Void> (run).

/*

starting...
42
finished!

 */

// Allow submitted tasks to finish, then exit.
exec.shutdown()
