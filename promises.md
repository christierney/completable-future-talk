Java 5 introduced Executors for doing asynchronous tasks, and the Future class
to represent the result of such a task. But you can’t do very much with a
future: you can cancel it, check if it has completed or been canceled, or block
waiting for the result.

You may be familiar with Promises from Javascript or another language. Promises
usually have additional features such as the ability to register callbacks or
to chain multiple asynchronous operations together. Java libraries such as
jdeferred and Akka has made these available in Java, but finally as of Java 8
there is a built-in solution: CompletableFuture. I’m going to walk you through
some of the features. CompletableFuture is hard to say, so I may refer to
“promises” instead sometimes.

First, here is a Future. Notice that we have to block in our main thread and
wait for it to complete to print the result.

By the way, I’m going to switch to writing examples in Kotlin, because I think
the syntax is nicer. Even if you don’t know Kotlin I think it will be pretty
clear what is going on, and I’m not using any Kotlin-specific classes---this is
all available in Java.

In the CompletableFuture version, our main thread registers an action to take
once the promise is resolved, but then carries on to print the next line
immediately.

If you want to quickly fire off a small async task, CF includes some static
methods that take a Runnable or Callable and return a promise that will resolve
when your task finishes.

Here you can see that there is more than one way to register a callback,
depending on whether I expect a value or not. thenAccept means I expect to get
a return value from the promise and do something with it; thenRun means I don’t
expect a value but want to do something after the promise completes. Both of
these methods actually return new promises themselves, so I’m just calling join
here so my script won’t exit before they complete.

Since these methods return promises, you can chain a whole series of actions
together. Besides accept and run, there is a third action, apply, which expects
an argument and also returns a value. All of these methods return a
CompletableFuture to allow further chaining; the difference is in whether the
callback you register takes an argument or not, and whether the returned future
will have a type like Int, or be Void.

You can also wait for multiple promises to resolve before running a single
callback. For example, we might want to simultaneously fetch video metadata
(like name) from one source and a list of renditions from another source. We
can then register a callback using thenCombine that will take the results of
both and put them together into one map.

thenCombine is the two-promise equivalent of thenApply (taking and returning a
value); there are also two-promise versions of accept and run.

You can create multiple promises and use the first one to complete.
acceptEither, applyToEither, and runAfterEither are all available.

If you have more than two promises, you can wait for any or all of them to
complete. allOf does not accept a function argument, but returns a new
CompletableFuture that completes when every promise in the list completes.
anyOf returns a new promise that completes when any one of the promises in the
list completes, and contains the return value of the winner.

What happens if something goes wrong? You can use exceptionally to provide a
function that will run if an exception occurs, allowing the chain of callbacks
to continue normally. Or you can use handle or whenComplete to handle either
the result or an exception with a single function. handle produces a result for
further computations; whenComplete runs an action but returns a Void future.

What if you want to run several tasks and use the first successful result?
Unfortunately, the way the API is written, if you use anyOf with multiple
futures and one of them completes exceptionally before any complete
successfully, you will see the exception. We have to write our own helper
function to get the behavior we want: take the first success, but if all of
them fail, return an exception.

