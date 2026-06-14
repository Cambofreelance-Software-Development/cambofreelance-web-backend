-- Exception Handling cont. (64-66) + Advanced Dart (68, 70-71)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Try-Catch in Dart', 'dart-try-catch',
    $dart_64$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#intro">Introduction</a></li><li><a href="#multiple">Multiple Catch Blocks</a></li><li><a href="#stack-trace">Stack Trace</a></li><li><a href="#rethrow">Rethrow</a></li><li><a href="#example">Comprehensive Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="intro">Introduction</h2>
<p>The <code>try-catch</code> construct protects code that might throw. Code in the <code>try</code> block runs normally; if an exception is thrown, execution immediately jumps to a matching <code>catch</code> or <code>on</code> clause.</p>
<pre><code>try {
  // Risky code
  var result = int.parse('not-a-number');
} on FormatException catch (e) {
  print('Could not parse: $${e.message}');
}</code></pre>
<h2 id="multiple">Multiple Catch Blocks</h2>
<pre><code>void processData(String input, int index) {
  try {
    var number = int.parse(input);
    var list = [10, 20, 30];
    print('Result: $${list[index] ~/ number}');
  } on FormatException catch (e) {
    print('Bad format: $${e.message}');
  } on RangeError catch (e) {
    print('Index out of range: $${e.message}');
  } on IntegerDivisionByZeroException {
    print('Cannot divide by zero!');
  } catch (e) {
    print('Unexpected error: $e');
  }
}

void main() {
  processData('2', 1);         // Result: 10
  processData('abc', 0);       // Bad format: ...
  processData('5', 10);        // Index out of range: ...
  processData('0', 0);         // Cannot divide by zero!
}</code></pre>
<h2 id="stack-trace">Stack Trace</h2>
<p>The second parameter in <code>catch (e, stackTrace)</code> captures the call stack at the point of the error — useful for debugging:</p>
<pre><code>void riskyOperation() {
  throw StateError('Something went wrong');
}

void main() {
  try {
    riskyOperation();
  } catch (e, stackTrace) {
    print('Error: $e');
    print('Stack:\n$stackTrace');
  }
}</code></pre>
<h2 id="rethrow">Rethrow</h2>
<p>Use <code>rethrow</code> to re-throw the current exception (preserving the original stack trace), useful for logging before propagating:</p>
<pre><code>void process() {
  try {
    throw FormatException('Invalid data');
  } catch (e) {
    print('Logging error: $e');
    rethrow;  // re-throws with original stack trace
  }
}

void main() {
  try {
    process();
  } catch (e) {
    print('Handled at top level: $e');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Logging error: FormatException: Invalid data
Handled at top level: FormatException: Invalid data</pre></div>
<h2 id="example">Comprehensive Example</h2>
<pre><code>class NetworkException implements Exception {
  final int statusCode;
  final String message;
  NetworkException(this.statusCode, this.message);
  @override String toString() => 'NetworkException($statusCode): $message';
}

Future&lt;String&gt; fetchData(String url) async {
  if (url.isEmpty) throw ArgumentError('URL cannot be empty');
  if (!url.startsWith('https')) throw NetworkException(403, 'HTTPS required');
  return 'Data from $url';
}

void main() async {
  final urls = ['', 'http://example.com', 'https://example.com'];
  for (var url in urls) {
    try {
      var data = await fetchData(url);
      print('Success: $data');
    } on ArgumentError catch (e) {
      print('Argument error: $${e.message}');
    } on NetworkException catch (e) {
      print('Network error: $e');
    } catch (e, st) {
      print('Unknown: $e\n$st');
    }
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is the difference between catch (e) and on ExceptionType catch (e)?</strong></p>
<ul><li>A) No difference</li><li>B) on catches a specific type; catch catches all ✅</li><li>C) catch is for async code only</li><li>D) on is deprecated</li></ul>
<p><strong>2. What does rethrow preserve that throw e does not?</strong></p>
<ul><li>A) The error message</li><li>B) The original stack trace ✅</li><li>C) The exception type</li><li>D) The catch block</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>on ExceptionType catch (e)</code> to catch specific exceptions and bare <code>catch (e, stackTrace)</code> for a fallback. Use <code>rethrow</code> to propagate an exception while preserving the stack trace. Order <code>on</code> clauses from most specific to least specific.</p>
    $dart_64$,
    'Deep dive into Dart try-catch — multiple catch blocks, stack trace capture, the rethrow keyword, and building robust error-handling pipelines.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,exceptions', NOW(), 64, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Finally Block in Dart', 'dart-finally',
    $dart_65$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#intro">Introduction</a></li><li><a href="#guarantee">Execution Guarantee</a></li><li><a href="#with-return">Finally with Return</a></li><li><a href="#resource">Resource Cleanup</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="intro">Introduction</h2>
<p>The <code>finally</code> block always executes after <code>try</code> and <code>catch</code>, regardless of whether an exception was thrown or caught. It is ideal for cleanup code — closing connections, releasing resources, logging completion.</p>
<pre><code>try {
  // risky code
} catch (e) {
  // handle error
} finally {
  // ALWAYS runs
}</code></pre>
<h2 id="guarantee">Execution Guarantee</h2>
<pre><code>void demonstrate(bool throwError) {
  print('Start');
  try {
    if (throwError) throw Exception('Oops!');
    print('No error occurred');
  } catch (e) {
    print('Caught: $e');
  } finally {
    print('Finally always runs');
  }
  print('After try-catch-finally');
}

void main() {
  demonstrate(false);
  print('---');
  demonstrate(true);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Start
No error occurred
Finally always runs
After try-catch-finally
---
Start
Caught: Exception: Oops!
Finally always runs
After try-catch-finally</pre></div>
<h2 id="with-return">Finally with Return</h2>
<p>The <code>finally</code> block runs even when a <code>return</code> statement is encountered inside <code>try</code>:</p>
<pre><code>int divide(int a, int b) {
  try {
    print('Dividing $a by $b');
    return a ~/ b;
  } catch (e) {
    print('Error: $e');
    return -1;
  } finally {
    print('Division attempt complete');  // always prints
  }
}

void main() {
  print(divide(10, 2));   // 5
  print(divide(10, 0));   // -1
}</code></pre>
<h2 id="resource">Resource Cleanup</h2>
<pre><code>class DatabaseConnection {
  bool isOpen = false;

  void open() {
    isOpen = true;
    print('Connection opened');
  }

  void close() {
    isOpen = false;
    print('Connection closed');
  }

  List&lt;String&gt; query(String sql) {
    if (!isOpen) throw StateError('Connection is closed');
    return ['row1', 'row2'];
  }
}

void runQuery() {
  var db = DatabaseConnection();
  try {
    db.open();
    var results = db.query('SELECT * FROM users');
    print('Results: $results');
  } catch (e) {
    print('Query failed: $e');
  } finally {
    db.close();  // always closes connection
  }
}

void main() => runQuery();</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. When does the finally block NOT execute?</strong></p>
<ul><li>A) When no exception occurs</li><li>B) When a return statement is hit in try</li><li>C) When Dart.exit() is called ✅ (process terminates)</li><li>D) When catch handles the exception</li></ul>
<p><strong>2. What is the primary use of the finally block?</strong></p>
<ul><li>A) To re-throw exceptions</li><li>B) To catch uncaught exceptions</li><li>C) To release resources and cleanup ✅</li><li>D) To log exceptions</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>finally</code> block always executes — after <code>try</code>, after <code>catch</code>, even after <code>return</code>. Use it to release resources like database connections, file handles, or network sockets. It runs regardless of whether an exception was thrown or handled.</p>
    $dart_65$,
    'Master the Dart finally block — guaranteed execution for resource cleanup, database connections, and file handles regardless of exceptions or returns.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,exceptions', NOW(), 65, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Custom Exceptions in Dart', 'dart-custom-exceptions',
    $dart_66$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#why">Why Custom Exceptions?</a></li><li><a href="#creating">Creating Custom Exceptions</a></li><li><a href="#hierarchy">Exception Hierarchy</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="why">Why Custom Exceptions?</h2>
<p>Dart's built-in exceptions (<code>FormatException</code>, <code>RangeError</code>, etc.) cover common cases, but domain-specific errors need custom exception classes to communicate meaningful context. A <code>PaymentDeclinedException</code> is far more informative than a generic <code>Exception</code>.</p>
<h2 id="creating">Creating Custom Exceptions</h2>
<pre><code>// Simplest form — implement Exception interface
class ValidationException implements Exception {
  final String field;
  final String message;
  ValidationException(this.field, this.message);

  @override
  String toString() => 'ValidationException[$field]: $message';
}

// With a factory constructor
class ApiException implements Exception {
  final int statusCode;
  final String message;
  final Map&lt;String, dynamic&gt;? details;

  const ApiException(this.statusCode, this.message, {this.details});

  factory ApiException.notFound(String resource) =>
      ApiException(404, '$resource not found');

  factory ApiException.unauthorized() =>
      ApiException(401, 'Authentication required');

  factory ApiException.serverError([String? detail]) =>
      ApiException(500, 'Internal server error', details: {'detail': detail});

  bool get isClientError => statusCode >= 400 &amp;&amp; statusCode &lt; 500;
  bool get isServerError => statusCode >= 500;

  @override
  String toString() => 'ApiException($statusCode): $message';
}</code></pre>
<h2 id="hierarchy">Exception Hierarchy</h2>
<pre><code>class AppException implements Exception {
  final String message;
  final String? code;
  AppException(this.message, {this.code});
  @override String toString() => code != null
      ? '[$code] $message' : message;
}

class AuthException extends AppException {
  AuthException(String msg) : super(msg, code: 'AUTH_ERROR');
}

class NetworkException extends AppException {
  final int statusCode;
  NetworkException(String msg, this.statusCode)
      : super(msg, code: 'NET_$statusCode');
}

class DatabaseException extends AppException {
  DatabaseException(String msg) : super(msg, code: 'DB_ERROR');
}</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>class UserService {
  final Map&lt;String, String&gt; _users = {'alice': 'pass123'};

  String login(String username, String password) {
    if (username.isEmpty) throw ValidationException('username', 'Cannot be empty');
    if (password.length &lt; 6) throw ValidationException('password', 'Min 6 chars');
    if (!_users.containsKey(username)) throw ApiException.notFound('User');
    if (_users[username] != password) throw AuthException('Invalid password');
    return 'token_$${username}_$${DateTime.now().millisecondsSinceEpoch}';
  }
}

void main() {
  var service = UserService();
  final tests = [
    ('', 'pass123'),
    ('alice', 'abc'),
    ('bob', 'pass123'),
    ('alice', 'wrong'),
    ('alice', 'pass123'),
  ];

  for (var (user, pass) in tests) {
    try {
      var token = service.login(user, pass);
      print('Login success: $token');
    } on ValidationException catch (e) {
      print('Validation: $e');
    } on ApiException catch (e) {
      print('API: $e');
    } on AuthException catch (e) {
      print('Auth: $e');
    }
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Validation: ValidationException[username]: Cannot be empty
Validation: ValidationException[password]: Min 6 chars
API: ApiException(404): User not found
Auth: [AUTH_ERROR] Invalid password
Login success: token_alice_...</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you create a custom exception in Dart?</strong></p>
<ul><li>A) extends Exception</li><li>B) implements Exception ✅</li><li>C) mixin Exception</li><li>D) abstract class Exception</li></ul>
<p><strong>2. What is the benefit of an exception hierarchy?</strong></p>
<ul><li>A) Makes code run faster</li><li>B) Allows catching at different granularity levels ✅</li><li>C) Removes the need for try-catch</li><li>D) Enforces compile-time error checking</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Create custom exceptions by implementing the <code>Exception</code> interface. Override <code>toString()</code> for meaningful messages. Use factory constructors for common error scenarios. Build exception hierarchies with <code>extends</code> so callers can catch at the right level of specificity.</p>
    $dart_66$,
    'Build custom exceptions in Dart — implement the Exception interface, create exception hierarchies with extends, and use factory constructors for clean error APIs.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,exceptions', NOW(), 66, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Typedef in Dart', 'dart-typedef',
    $dart_68$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is typedef?</a></li><li><a href="#function-type">Function Type Aliases</a></li><li><a href="#generic">Generic Typedef</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is typedef?</h2>
<p>A <code>typedef</code> creates an alias for a type — most commonly function types. It improves code readability by giving complex function signatures a meaningful name.</p>
<h2 id="function-type">Function Type Aliases</h2>
<pre><code>// Without typedef:
void Function(String, int) callback;
List&lt;Map&lt;String, dynamic&gt;&gt; Function(String) queryFn;

// With typedef:
typedef Callback = void Function(String message, int code);
typedef QueryFn = List&lt;Map&lt;String, dynamic&gt;&gt; Function(String sql);
typedef Predicate&lt;T&gt; = bool Function(T value);
typedef Transformer&lt;T, R&gt; = R Function(T input);

// Usage:
void runWithCallback(Callback callback) {
  callback('Success', 200);
}

void main() {
  Callback myCallback = (msg, code) =&gt; print('[$code] $msg');
  runWithCallback(myCallback);  // [200] Success
}</code></pre>
<h2 id="generic">Generic Typedef</h2>
<pre><code>typedef JSON = Map&lt;String, dynamic&gt;;
typedef JsonList = List&lt;Map&lt;String, dynamic&gt;&gt;;
typedef Mapper&lt;T, R&gt; = R Function(T);
typedef Comparator&lt;T&gt; = int Function(T a, T b);

// Use in a sortable list:
List&lt;T&gt; sortBy&lt;T&gt;(List&lt;T&gt; items, Comparator&lt;T&gt; compare) {
  var sorted = [...items];
  sorted.sort(compare);
  return sorted;
}

void main() {
  var numbers = [3, 1, 4, 1, 5, 9, 2, 6];
  var sorted = sortBy(numbers, (a, b) =&gt; a.compareTo(b));
  print(sorted);  // [1, 1, 2, 3, 4, 5, 6, 9]

  JSON user = {'name': 'Alice', 'age': 30};
  print(user['name']);  // Alice
}</code></pre>
<h2 id="example">Practical Example: Event System</h2>
<pre><code>typedef EventHandler&lt;T&gt; = void Function(T event);
typedef AsyncEventHandler&lt;T&gt; = Future&lt;void&gt; Function(T event);

class EventBus&lt;T&gt; {
  final List&lt;EventHandler&lt;T&gt;&gt; _handlers = [];

  void on(EventHandler&lt;T&gt; handler) =&gt; _handlers.add(handler);
  void emit(T event) { for (var h in _handlers) h(event); }
}

void main() {
  var bus = EventBus&lt;String&gt;();
  bus.on((msg) =&gt; print('Handler 1: $msg'));
  bus.on((msg) =&gt; print('Handler 2: $${msg.toUpperCase()}'));
  bus.emit('hello');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Handler 1: hello
Handler 2: HELLO</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does typedef primarily create?</strong></p>
<ul><li>A) A new class</li><li>B) A type alias ✅</li><li>C) An abstract interface</li><li>D) A mixin</li></ul>
<p><strong>2. Can typedef be generic?</strong></p>
<ul><li>A) No</li><li>B) Yes ✅</li><li>C) Only in Dart 3+</li><li>D) Only for non-function types</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p><code>typedef</code> creates named type aliases, primarily for function signatures. Use <code>typedef Name = ReturnType Function(ParamTypes)</code>. Generics work with typedef — <code>typedef Mapper&lt;T, R&gt; = R Function(T)</code>. Typedef improves readability and is great for callback patterns and event systems.</p>
    $dart_68$,
    'Learn Dart typedef — create readable type aliases for complex function signatures, generic typedefs, and build event systems with named callback types.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 68, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Records in Dart', 'dart-records',
    $dart_70$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Records?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#named">Named Fields</a></li><li><a href="#pattern">Destructuring Records</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Records?</h2>
<p>Records (introduced in Dart 3.0) are anonymous immutable aggregate types. They bundle multiple values together without creating a full class. Think of them as lightweight, typed tuples.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>// Create a record
(int, String) person = (25, 'Alice');
print(person.$1);  // 25  (positional access)
print(person.$2);  // Alice

// Record type annotation
(double, double) coordinates = (37.7749, -122.4194);

// Return multiple values from a function
(int, String) getUser() =&gt; (1, 'Bob');</code></pre>
<h2 id="named">Named Fields</h2>
<pre><code>// Named field records
({String name, int age, String email}) user = (
  name: 'Alice',
  age: 30,
  email: 'alice@example.com'
);

print(user.name);   // Alice
print(user.age);    // 30
print(user.email);  // alice@example.com

// Mix of positional and named
(int, {String name, bool isAdmin}) adminUser = (1, name: 'Bob', isAdmin: true);
print(adminUser.$1);         // 1
print(adminUser.name);       // Bob
print(adminUser.isAdmin);    // true</code></pre>
<h2 id="pattern">Destructuring Records</h2>
<pre><code>var (x, y) = (10, 20);
print('x=$x, y=$y');  // x=10, y=20

var (:name, :age) = (name: 'Alice', age: 30);
print('$name is $age');  // Alice is 30

// In a for loop
List&lt;(String, int)&gt; scores = [('Alice', 95), ('Bob', 87), ('Charlie', 92)];
for (var (name, score) in scores) {
  print('$name: $score');
}</code></pre>
<h2 id="example">Example: Multi-value Returns</h2>
<pre><code>({double min, double max, double average}) stats(List&lt;double&gt; numbers) {
  if (numbers.isEmpty) return (min: 0, max: 0, average: 0);
  var sorted = [...numbers]..sort();
  var sum = numbers.reduce((a, b) =&gt; a + b);
  return (
    min: sorted.first,
    max: sorted.last,
    average: sum / numbers.length,
  );
}

void main() {
  var result = stats([5.0, 3.0, 8.0, 1.0, 9.0]);
  print('Min: $${result.min}');      // Min: 1.0
  print('Max: $${result.max}');      // Max: 9.0
  print('Avg: $${result.average}');  // Avg: 5.2
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Min: 1.0
Max: 9.0
Avg: 5.2</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you access a positional field in a record?</strong></p>
<ul><li>A) record.first, record.second</li><li>B) record[0], record[1]</li><li>C) record.$1, record.$2 ✅</li><li>D) record.get(0)</li></ul>
<p><strong>2. Are records mutable in Dart?</strong></p>
<ul><li>A) Yes</li><li>B) No — records are immutable ✅</li><li>C) Only positional fields are mutable</li><li>D) Only named fields are mutable</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Records are anonymous immutable value types introduced in Dart 3.0. Use <code>(Type1, Type2)</code> for positional fields (accessed via <code>.$1</code>, <code>.$2</code>) and <code>({FieldType name})</code> for named fields. Records are great for returning multiple values from functions without creating a class.</p>
    $dart_70$,
    'Learn Dart Records (Dart 3.0) — anonymous immutable tuples with positional and named fields, multi-value returns, and pattern destructuring.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 70, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Pattern Matching in Dart', 'dart-pattern-matching',
    $dart_71$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Pattern Matching?</a></li><li><a href="#switch">Switch Expressions</a></li><li><a href="#record-patterns">Record Patterns</a></li><li><a href="#list-patterns">List Patterns</a></li><li><a href="#guards">Guard Clauses</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Pattern Matching?</h2>
<p>Pattern matching (Dart 3.0+) lets you match a value against a shape or structure and extract parts of it in one expression. Dart patterns work in <code>switch</code> statements and expressions, variable declarations, and for-loop variables.</p>
<h2 id="switch">Switch Expressions</h2>
<pre><code>// Switch expression (returns a value)
String describe(Object value) =&gt; switch (value) {
  int n when n &lt; 0  =&gt; 'negative int',
  int n when n == 0 =&gt; 'zero',
  int _             =&gt; 'positive int',
  double _          =&gt; 'a double',
  String s          =&gt; 'a string: "$s"',
  List l            =&gt; 'a list of $${l.length} items',
  _                 =&gt; 'unknown',
};

void main() {
  print(describe(-5));       // negative int
  print(describe(0));        // zero
  print(describe(42));       // positive int
  print(describe(3.14));     // a double
  print(describe('hello')); // a string: "hello"
  print(describe([1,2,3])); // a list of 3 items
}</code></pre>
<h2 id="record-patterns">Record Patterns</h2>
<pre><code>String describePoint((int, int) point) =&gt; switch (point) {
  (0, 0) =&gt; 'Origin',
  (int x, 0) =&gt; 'On x-axis at $x',
  (0, int y) =&gt; 'On y-axis at $y',
  (int x, int y) when x == y =&gt; 'On diagonal at ($x, $y)',
  (int x, int y) =&gt; 'Point at ($x, $y)',
};

void main() {
  print(describePoint((0, 0)));    // Origin
  print(describePoint((5, 0)));    // On x-axis at 5
  print(describePoint((3, 3)));    // On diagonal at (3, 3)
  print(describePoint((2, 7)));    // Point at (2, 7)
}</code></pre>
<h2 id="list-patterns">List Patterns</h2>
<pre><code>String describeList(List&lt;int&gt; list) =&gt; switch (list) {
  []       =&gt; 'empty',
  [var x]  =&gt; 'single: $x',
  [var x, var y] =&gt; 'pair: $x, $y',
  [var first, ..., var last] =&gt; 'starts $first, ends $last',
};

print(describeList([]));         // empty
print(describeList([42]));       // single: 42
print(describeList([1, 2]));     // pair: 1, 2
print(describeList([1,2,3,4,5]));// starts 1, ends 5</code></pre>
<h2 id="guards">Guard Clauses</h2>
<pre><code>// 'when' adds a boolean guard condition
String classify(int n) =&gt; switch (n) {
  0                 =&gt; 'zero',
  int x when x &lt; 0 =&gt; 'negative',
  int x when x % 2 == 0 =&gt; 'positive even',
  _                 =&gt; 'positive odd',
};</code></pre>
<h2 id="example">Practical Example</h2>
<pre><code>sealed class Shape {}
class Circle extends Shape { final double radius; Circle(this.radius); }
class Rectangle extends Shape { final double w, h; Rectangle(this.w, this.h); }
class Triangle extends Shape { final double b, height; Triangle(this.b, this.height); }

double area(Shape shape) =&gt; switch (shape) {
  Circle(radius: var r)         =&gt; 3.14159 * r * r,
  Rectangle(w: var w, h: var h) =&gt; w * h,
  Triangle(b: var b, height: var h) =&gt; 0.5 * b * h,
};

void main() {
  print(area(Circle(5)).toStringAsFixed(2));         // 78.54
  print(area(Rectangle(4, 6)).toStringAsFixed(2));   // 24.00
  print(area(Triangle(3, 8)).toStringAsFixed(2));    // 12.00
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is a switch expression in Dart?</strong></p>
<ul><li>A) A switch statement with expressions inside</li><li>B) A switch that returns a value ✅</li><li>C) A switch for matching expressions only</li><li>D) A Dart 2 feature</li></ul>
<p><strong>2. What does the when keyword add to a pattern case?</strong></p>
<ul><li>A) A type check</li><li>B) A boolean guard condition ✅</li><li>C) An optional field</li><li>D) A loop</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Dart 3.0 pattern matching enables switch expressions that return values, record/list patterns for structural matching, and <code>when</code> guards for conditional cases. Use <code>sealed</code> classes with pattern matching for exhaustive type checking. Patterns can appear in switch, variable declarations, and for loops.</p>
    $dart_71$,
    'Master Dart 3.0 pattern matching — switch expressions, record and list patterns, guard clauses with when, and sealed class hierarchies.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 71, 'PUBLISHED', 'ACT', 'system'
);
