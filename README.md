# akka-examples
Java examples using the Akka framework.

Examples are run from the src/test/java directory

**org.gmjm.akka.HelloWorldTest**

>A basic actor system that appends a greeting onto received messages and prints them to std out.

**org.gmjm.akka.webcrawler.UrlCarwlUtilTest**

>An advanced actor system.  The system crawls the web recursively.  Links will be followed to the specified depth, and output appended to files.  The FileWritingManager and Actor show an example of how to write to multiple files concurrently.  The Crawl object shows how messages can do different things based on their object hierarchy.

**org.gmjm.akka.io.IoTest**

>This is a rough implementation of a telnet server backed by an ActorSystem.

[High level overview of the Web Crawler example](https://drive.google.com/file/d/0B_qxYel84DH2X3BuVFV3ZUZUTEk/view?usp=sharing)

## Futures

Akka Futures are used to parallelize the multiplication of large matrices.  Check out the following classes to see them in action:

**src/test/java/com.gmjm.akka.futures.FuturesTest**

>This test harness allows you to load a large dynamically generated matrix equation into memory.
There are two tests, one that is single threaded, and one that uses Futures.  Tweak the matrix parameters and threads in the execution context to get a feel for how powerful the futures can be when they are organized correctly.  

**src/main/java/com.gmjm.akka.futures.FutureMatrixReduction**

>This class is where the magic is done.  A recursive function takes a list of Matrix objects, and creates a tree of Futures.  The Tree will continue to reduce the tree in parallel until the final Matrix is obtained.  Since the futureReduce() method uses the akka Futures.sequence(toReduce, ec) method, the future results will be mapped in the sequence, rather than be processed by when they were completed.  While this doesn't allow for maximum throuput, it is the only way to achieve the correct results.  If the sequence method was not used, the end result would be unpredictable due to the fact that matrix multiplication is not communicative (A*B != B*A).
