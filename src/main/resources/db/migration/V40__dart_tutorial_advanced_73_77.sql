-- Section 12: Asynchronous Programming (Lessons 73–77, Advanced)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Futures in Dart', 'dart-futures',
    $dart_73$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Future?</a></li><li><a href="#create">Creating Futures</a></li><li><a href="#then">then / catchError / whenComplete</a></li><li><a href="#chaining">Chaining Futures</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Future?</h2>
<p>A <code>Future&lt;T&gt;</code> represents a value that will be available at some point in the future — the result of an asynchronous computation. It is similar to a Promise in JavaScript. Futures exist in one of three states:</p>
<ul><li><strong>Uncompleted</strong> — still computing</li><li><strong>Completed with a value</strong> — success</li><li><strong>Completed with an error</strong> — failure</li></ul>
<h2 id="create">Creating Futures</h2>
<pre><code>import 'dart:async';

// Future.value — immediately completed
Future&lt;int&gt; getValue() =&gt; Future.value(42);

// Future.error — immediately failed
Future&lt;int&gt; getError() =&gt; Future.error(Exception('Oops'));

// Future.delayed — completes after a delay
Future&lt;String&gt; fetchData() =&gt; Future.delayed(
  Duration(seconds: 2),
  () =&gt; 'Data loaded',
);

// Using a Completer for manual control
Future&lt;String&gt; manualFuture() {
  final completer = Completer&lt;String&gt;();
  Future.delayed(Duration(seconds: 1), () {
    completer.complete('Done!');
    // or completer.completeError(Exception('Failed'));
  });
  return completer.future;
}</code></pre>
<h2 id="then">then / catchError / whenComplete</h2>
<pre><code>fetchData()
    .then((data) =&gt; print('Got: $data'))
    .catchError((e) =&gt; print('Error: $e'))
    .whenComplete(() =&gt; print('Finished'));

// Chaining transforms with then:
Future.value('  hello world  ')
    .then((s) =&gt; s.trim())
    .then((s) =&gt; s.split(' '))
    .then((words) =&gt; words.map((w) =&gt;
        w[0].toUpperCase() + w.substring(1)).join(' '))
    .then(print);  // Hello World</code></pre>
<h2 id="chaining">Chaining Futures</h2>
<pre><code>Future&lt;int&gt; step1() =&gt; Future.delayed(Duration(milliseconds: 100), () =&gt; 10);
Future&lt;int&gt; step2(int v) =&gt; Future.delayed(Duration(milliseconds: 100), () =&gt; v * 2);
Future&lt;String&gt; step3(int v) =&gt; Future.value('Result: $v');

void main() {
  step1()
      .then(step2)
      .then(step3)
      .then(print)
      .catchError((e) =&gt; print('Error: $e'));
  // Result: 20
}</code></pre>
<h2 id="example">Wait for Multiple Futures</h2>
<pre><code>Future&lt;void&gt; main() async {
  // Run in parallel, wait for all:
  var results = await Future.wait([
    Future.delayed(Duration(seconds: 1), () =&gt; 'First'),
    Future.delayed(Duration(seconds: 2), () =&gt; 'Second'),
    Future.delayed(Duration(milliseconds: 500), () =&gt; 'Third'),
  ]);
  print(results);  // [First, Second, Third] — takes ~2s total, not 3.5s

  // First to complete:
  var first = await Future.any([
    Future.delayed(Duration(seconds: 3), () =&gt; 'Slow'),
    Future.delayed(Duration(milliseconds: 100), () =&gt; 'Fast'),
  ]);
  print(first);  // Fast
}</code></pre>
<div class="note-box"><strong>Note:</strong> <code>Future.wait</code> runs futures in parallel and collects all results. <code>Future.any</code> returns the first to complete. If any future in <code>Future.wait</code> fails, the entire wait fails.</div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does Future.wait do?</strong></p>
<ul><li>A) Waits for the slowest future and discards others</li><li>B) Runs all futures in parallel and collects results ✅</li><li>C) Runs futures sequentially</li><li>D) Returns the first completed future</li></ul>
<p><strong>2. Which method always runs after a Future, regardless of success or failure?</strong></p>
<ul><li>A) then</li><li>B) catchError</li><li>C) whenComplete ✅</li><li>D) onError</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p><code>Future&lt;T&gt;</code> represents async results. Chain with <code>.then()</code>, handle errors with <code>.catchError()</code>, and cleanup with <code>.whenComplete()</code>. Use <code>Future.wait</code> to run multiple futures in parallel. Use <code>async/await</code> (next lesson) for cleaner syntax over long chains.</p>
    $dart_73$,
    'Learn Dart Futures — the building blocks of async programming. Creating, chaining, and combining Futures with then, catchError, Future.wait and Future.any.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,async', NOW(), 73, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Async and Await in Dart', 'dart-async-await',
    $dart_74$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are async/await?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#error">Error Handling</a></li><li><a href="#parallel">Parallel vs Sequential</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are async/await?</h2>
<p><code>async</code> and <code>await</code> are syntactic sugar over Futures that make asynchronous code look and behave like synchronous code. An <code>async</code> function always returns a <code>Future</code>, even if you return a plain value.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>// Mark function as async:
Future&lt;String&gt; fetchUser(int id) async {
  // await pauses execution until the Future completes:
  await Future.delayed(Duration(seconds: 1));
  return 'User $id';
}

void main() async {
  print('Fetching...');
  String user = await fetchUser(42);
  print(user);  // User 42
  print('Done');
}
// Output: Fetching... → (1 second) → User 42 → Done</code></pre>
<h2 id="error">Error Handling with try-catch</h2>
<pre><code>Future&lt;String&gt; riskyFetch() async {
  await Future.delayed(Duration(milliseconds: 200));
  throw Exception('Server error');
}

Future&lt;void&gt; main() async {
  try {
    var result = await riskyFetch();
    print(result);
  } catch (e) {
    print('Caught: $e');  // Caught: Exception: Server error
  } finally {
    print('Cleanup');
  }
}</code></pre>
<h2 id="parallel">Parallel vs Sequential</h2>
<pre><code>Future&lt;String&gt; task(String name, int seconds) async {
  await Future.delayed(Duration(seconds: seconds));
  return '$name done';
}

Future&lt;void&gt; sequential() async {
  var t = DateTime.now();
  var a = await task('A', 2);  // waits 2s
  var b = await task('B', 2);  // waits another 2s
  print([a, b]);
  print('Sequential: $${DateTime.now().difference(t).inSeconds}s');  // ~4s
}

Future&lt;void&gt; parallel() async {
  var t = DateTime.now();
  // Start both immediately:
  var futureA = task('A', 2);
  var futureB = task('B', 2);
  var results = await Future.wait([futureA, futureB]);
  print(results);
  print('Parallel: $${DateTime.now().difference(t).inSeconds}s');  // ~2s
}</code></pre>
<h2 id="example">Full Example: HTTP-like API Calls</h2>
<pre><code>class ApiClient {
  Future&lt;Map&lt;String, dynamic&gt;&gt; getUser(int id) async {
    await Future.delayed(Duration(milliseconds: 300));
    return {'id': id, 'name': 'User $id', 'email': 'user$id@example.com'};
  }

  Future&lt;List&lt;String&gt;&gt; getPosts(int userId) async {
    await Future.delayed(Duration(milliseconds: 200));
    return List.generate(3, (i) =&gt; 'Post $${i + 1} by user $userId');
  }
}

Future&lt;void&gt; main() async {
  var client = ApiClient();

  // Get user, then their posts
  var user = await client.getUser(1);
  print('User: $${user['name']}');

  var posts = await client.getPosts(user['id']);
  for (var post in posts) {
    print('  - $post');
  }

  // Or get multiple users in parallel:
  var users = await Future.wait(
    [1, 2, 3].map(client.getUser),
  );
  print('Loaded $${users.length} users');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>User: User 1
  - Post 1 by user 1
  - Post 2 by user 1
  - Post 3 by user 1
Loaded 3 users</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does an async function always return?</strong></p>
<ul><li>A) void</li><li>B) dynamic</li><li>C) A Future ✅</li><li>D) A Stream</li></ul>
<p><strong>2. How do you run two async tasks in parallel?</strong></p>
<ul><li>A) await task1(); await task2();</li><li>B) var f1 = task1(); var f2 = task2(); await Future.wait([f1, f2]); ✅</li><li>C) await [task1(), task2()]</li><li>D) Future.parallel([task1, task2])</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Mark functions <code>async</code> and use <code>await</code> to pause until a Future completes. Async functions always return <code>Future&lt;T&gt;</code>. Use <code>try-catch</code> for error handling. For parallel execution, start all futures first then <code>await Future.wait()</code> — don't <code>await</code> them one by one.</p>
    $dart_74$,
    'Master Dart async/await — write clean asynchronous code, handle errors with try-catch, and understand the difference between sequential and parallel async execution.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,async', NOW(), 74, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Streams in Dart', 'dart-streams',
    $dart_75$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Stream?</a></li><li><a href="#types">Stream Types</a></li><li><a href="#create">Creating Streams</a></li><li><a href="#listen">Listening to Streams</a></li><li><a href="#transform">Transforming Streams</a></li><li><a href="#async-for">async for-in</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Stream?</h2>
<p>A <code>Stream&lt;T&gt;</code> is a sequence of asynchronous events over time — like a pipe delivering values one by one. Where a <code>Future</code> delivers a single result, a <code>Stream</code> delivers zero or more results over time. Examples: user clicks, file bytes, WebSocket messages, real-time sensor data.</p>
<h2 id="types">Stream Types</h2>
<ul>
<li><strong>Single-subscription</strong> (default) — can only be listened to once; like reading a file</li>
<li><strong>Broadcast</strong> — multiple listeners at once; like a radio broadcast</li>
</ul>
<h2 id="create">Creating Streams</h2>
<pre><code>// From an iterable:
Stream&lt;int&gt; counter = Stream.fromIterable([1, 2, 3, 4, 5]);

// Periodic stream:
Stream&lt;int&gt; ticker = Stream.periodic(
  Duration(seconds: 1),
  (count) =&gt; count,
).take(5);  // stop after 5 events

// Async generator function:
Stream&lt;int&gt; countdown(int from) async* {
  for (int i = from; i &gt;= 0; i--) {
    await Future.delayed(Duration(milliseconds: 500));
    yield i;
  }
}

// From a Future:
Stream&lt;String&gt; fromFuture = Stream.fromFuture(
  Future.delayed(Duration(seconds: 1), () =&gt; 'Done')
);</code></pre>
<h2 id="listen">Listening to Streams</h2>
<pre><code>var stream = Stream.fromIterable([1, 2, 3, 4, 5]);
stream.listen(
  (data) =&gt; print('Data: $data'),
  onError: (e) =&gt; print('Error: $e'),
  onDone: () =&gt; print('Stream closed'),
);

// Full async generator example:
Future&lt;void&gt; main() async {
  await for (var n in countdown(5)) {
    print(n);
  }
  // 5, 4, 3, 2, 1, 0
}</code></pre>
<h2 id="transform">Transforming Streams</h2>
<pre><code>Stream&lt;int&gt; numbers = Stream.fromIterable(
  List.generate(10, (i) =&gt; i + 1)
);

numbers
  .where((n) =&gt; n.isEven)          // keep even numbers
  .map((n) =&gt; n * n)               // square them
  .take(3)                          // first 3 only
  .listen(print);                   // 4, 16, 36</code></pre>
<h2 id="async-for">async for-in</h2>
<pre><code>Stream&lt;String&gt; messageStream() async* {
  var messages = ['Hello', 'World', 'From', 'Dart', 'Streams'];
  for (var msg in messages) {
    await Future.delayed(Duration(milliseconds: 200));
    yield msg;
  }
}

Future&lt;void&gt; main() async {
  await for (var message in messageStream()) {
    print(message);
  }
  print('All messages received');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Hello
World
From
Dart
Streams
All messages received</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What keyword turns a function into a stream generator?</strong></p>
<ul><li>A) async</li><li>B) yield</li><li>C) async* with yield ✅</li><li>D) stream</li></ul>
<p><strong>2. What is the difference between Stream and Future?</strong></p>
<ul><li>A) No difference</li><li>B) Future delivers one value; Stream delivers multiple values over time ✅</li><li>C) Stream is synchronous; Future is async</li><li>D) Future can have multiple listeners; Stream cannot</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>A <code>Stream&lt;T&gt;</code> delivers multiple values over time. Create streams with <code>async*</code> generators using <code>yield</code>, or with factory constructors like <code>Stream.fromIterable</code> and <code>Stream.periodic</code>. Listen with <code>.listen()</code> or <code>await for</code>. Transform with <code>.map()</code>, <code>.where()</code>, <code>.take()</code> and other stream methods.</p>
    $dart_75$,
    'Learn Dart Streams — handle sequences of async events with async* generators, Stream.fromIterable, transformations with map/where/take, and await for-in loops.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,async', NOW(), 75, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Stream Controllers in Dart', 'dart-stream-controllers',
    $dart_76$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a StreamController?</a></li><li><a href="#basic">Basic Usage</a></li><li><a href="#broadcast">Broadcast Controllers</a></li><li><a href="#pattern">Event Bus Pattern</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a StreamController?</h2>
<p>A <code>StreamController&lt;T&gt;</code> gives you fine-grained control over a stream — you decide when to add events, errors, and when to close the stream. The controller exposes a <code>sink</code> for adding events and a <code>stream</code> for listening.</p>
<h2 id="basic">Basic Usage</h2>
<pre><code>import 'dart:async';

void main() async {
  var controller = StreamController&lt;int&gt;();

  // Add events to the sink
  controller.sink.add(1);
  controller.sink.add(2);
  controller.sink.add(3);
  controller.sink.close();  // close when done

  // Listen to the stream
  await for (var value in controller.stream) {
    print(value);  // 1, 2, 3
  }

  // Always close the controller to free resources
  await controller.close();
}</code></pre>
<h2 id="broadcast">Broadcast Controllers</h2>
<pre><code>// Regular: only one listener
// Broadcast: multiple listeners
var broadcast = StreamController&lt;String&gt;.broadcast();

broadcast.stream.listen((msg) =&gt; print('Listener 1: $msg'));
broadcast.stream.listen((msg) =&gt; print('Listener 2: $${msg.toUpperCase()}'));

broadcast.sink.add('hello');
broadcast.sink.add('world');
await broadcast.close();
// Listener 1: hello
// Listener 2: HELLO
// Listener 1: world
// Listener 2: WORLD</code></pre>
<h2 id="pattern">Event Bus Pattern</h2>
<pre><code>class EventBus {
  final _controller = StreamController&lt;Map&lt;String, dynamic&gt;&gt;.broadcast();

  Stream&lt;Map&lt;String, dynamic&gt;&gt; get stream =&gt; _controller.stream;

  // Filter stream to specific event type
  Stream&lt;Map&lt;String, dynamic&gt;&gt; on(String eventType) =&gt;
      stream.where((e) =&gt; e['type'] == eventType);

  void emit(String type, Map&lt;String, dynamic&gt; data) {
    _controller.add({'type': type, ...data});
  }

  Future&lt;void&gt; close() =&gt; _controller.close();
}

void main() async {
  var bus = EventBus();

  bus.on('login').listen((e) =&gt; print('User logged in: $${e['user']}'));
  bus.on('logout').listen((e) =&gt; print('User logged out: $${e['user']}'));
  bus.stream.listen((e) =&gt; print('All events: $${e['type']}'));

  bus.emit('login', {'user': 'alice'});
  bus.emit('login', {'user': 'bob'});
  bus.emit('logout', {'user': 'alice'});

  await Future.delayed(Duration(milliseconds: 50));
  await bus.close();
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>All events: login
User logged in: alice
All events: login
User logged in: bob
All events: logout
User logged out: alice</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is the difference between a regular and broadcast StreamController?</strong></p>
<ul><li>A) No difference</li><li>B) Broadcast allows multiple listeners; regular allows only one ✅</li><li>C) Regular is async; broadcast is sync</li><li>D) Broadcast is faster</li></ul>
<p><strong>2. What should you always do with a StreamController when done?</strong></p>
<ul><li>A) Restart it</li><li>B) Call controller.close() to free resources ✅</li><li>C) Call controller.cancel()</li><li>D) Call controller.drain()</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p><code>StreamController&lt;T&gt;</code> gives manual control over stream events. Add events via <code>controller.sink.add()</code>, add errors via <code>controller.sink.addError()</code>, and close with <code>controller.sink.close()</code>. Use <code>StreamController.broadcast()</code> for multiple listeners. Always close controllers to avoid memory leaks.</p>
    $dart_76$,
    'Learn Dart StreamController — manually control stream events with sink.add(), broadcast controllers for multiple listeners, and build an event bus pattern.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,async', NOW(), 76, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Isolates in Dart', 'dart-isolates',
    $dart_77$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Isolates?</a></li><li><a href="#why">Why Isolates?</a></li><li><a href="#spawn">Spawning an Isolate</a></li><li><a href="#compute">Isolate.run</a></li><li><a href="#communication">Two-way Communication</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Isolates?</h2>
<p>In Dart, all code runs in an <strong>isolate</strong> — an independent worker with its own memory heap. Unlike threads in other languages, isolates don't share memory; they communicate only by passing messages (copies of data) through <code>SendPort</code> and <code>ReceivePort</code>. This eliminates race conditions and makes concurrency safe.</p>
<h2 id="why">Why Isolates?</h2>
<p>Dart's event loop is single-threaded. CPU-heavy work (image processing, encryption, parsing large JSON) blocks the event loop and freezes your UI. Offload such work to a separate isolate to keep your main isolate responsive.</p>
<h2 id="spawn">Spawning an Isolate</h2>
<pre><code>import 'dart:isolate';

void heavyWork(SendPort sendPort) {
  // This runs in a separate isolate
  int sum = 0;
  for (int i = 0; i &lt; 1000000000; i++) sum += i;
  sendPort.send(sum);
}

Future&lt;void&gt; main() async {
  var receivePort = ReceivePort();
  await Isolate.spawn(heavyWork, receivePort.sendPort);

  var result = await receivePort.first;
  print('Sum: $result');
  receivePort.close();
}</code></pre>
<h2 id="compute">Isolate.run (Dart 2.19+)</h2>
<p><code>Isolate.run()</code> is the simplest API — spawn, run, get result, and the isolate is automatically cleaned up:</p>
<pre><code>import 'dart:isolate';

List&lt;int&gt; findPrimes(int limit) {
  var primes = &lt;int&gt;[];
  for (int n = 2; n &lt;= limit; n++) {
    bool isPrime = true;
    for (int i = 2; i * i &lt;= n; i++) {
      if (n % i == 0) { isPrime = false; break; }
    }
    if (isPrime) primes.add(n);
  }
  return primes;
}

Future&lt;void&gt; main() async {
  print('Finding primes...');
  var primes = await Isolate.run(() =&gt; findPrimes(100000));
  print('Found $${primes.length} primes');
  print('First 10: $${primes.take(10).toList()}');
}</code></pre>
<h2 id="communication">Two-way Communication</h2>
<pre><code>import 'dart:isolate';

void worker(SendPort mainSendPort) async {
  var workerPort = ReceivePort();
  mainSendPort.send(workerPort.sendPort);  // send back our port

  await for (var message in workerPort) {
    if (message == 'STOP') break;
    var result = message * 2;  // process message
    mainSendPort.send(result);
  }
}

Future&lt;void&gt; main() async {
  var mainPort = ReceivePort();
  await Isolate.spawn(worker, mainPort.sendPort);

  var messages = mainPort.asBroadcastStream();
  SendPort workerSendPort = await messages.first;

  for (var n in [1, 2, 3, 4, 5]) {
    workerSendPort.send(n);
    var result = await messages.first;
    print('$n * 2 = $result');
  }

  workerSendPort.send('STOP');
  mainPort.close();
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>1 * 2 = 2
2 * 2 = 4
3 * 2 = 6
4 * 2 = 8
5 * 2 = 10</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do isolates communicate in Dart?</strong></p>
<ul><li>A) Shared memory variables</li><li>B) Global state</li><li>C) By passing message copies through SendPort/ReceivePort ✅</li><li>D) Through synchronous function calls</li></ul>
<p><strong>2. What is the simplest way to run code in an isolate (Dart 2.19+)?</strong></p>
<ul><li>A) Isolate.spawn</li><li>B) Isolate.run ✅</li><li>C) Thread.start</li><li>D) compute()</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Isolates are Dart's concurrency model — independent workers with no shared memory, communicating via message passing. Use <code>Isolate.run()</code> for one-off tasks (simplest), <code>Isolate.spawn()</code> for long-running workers. Pass data via <code>SendPort</code>/<code>ReceivePort</code>. Use isolates for CPU-intensive work to keep the main event loop responsive.</p>
    $dart_77$,
    'Learn Dart Isolates — Dart concurrency model with no shared memory, SendPort and ReceivePort messaging, Isolate.run for simple tasks, and two-way communication patterns.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,async', NOW(), 77, 'PUBLISHED', 'ACT', 'system'
);
