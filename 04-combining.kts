import java.util.*
import java.util.concurrent.*

val exec = Executors.newSingleThreadExecutor()
val cf1 = CompletableFuture<HashMap<String, Any>>()
exec.submit {
    Thread.sleep(3000)
    // This future will complete with a HashMap.
    cf1.complete(hashMapOf("name" to "Panda cat doge",
                           "tags" to listOf("funny!")))
}

val cf2 = CompletableFuture<List<HashMap<String, String>>>()
exec.submit {
    Thread.sleep(1000)
    // This future will complete with a List of HashMaps.
    cf2.complete(listOf(hashMapOf("src" to "example.com/lowres.mp4"),
                        hashMapOf("src" to "example.com/hires.mp4")))
}

// This BiFunction takes the results of cf1 and cf2 as arguments.
cf1.thenCombine(cf2, { metadata, srcs ->
    // Return a HashMap with all the data from both sources.
    metadata + hashMapOf("renditions" to srcs)
}).thenAccept(::println)

/*

{name=Panda cat doge, tags=[funny!], renditions=[{src=example.com/lowres.mp4}, {src=example.com/hires.mp4}]}

 */

// Allow submitted tasks to finish, then exit.
exec.shutdown()
