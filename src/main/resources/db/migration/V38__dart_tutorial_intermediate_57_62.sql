-- Section 9 OOP Part 2 + Exception Handling + Advanced Dart (Lessons 62–72)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Extension Methods in Dart', 'dart-extension-methods',
    $dart_62$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Extension Methods?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#extending-built-in">Extending Built-in Types</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Extension Methods?</h2>
<p>Extension methods let you add new functionality to existing classes — including built-in types like <code>String</code>, <code>int</code>, <code>List</code> — without modifying or subclassing them. They are purely syntactic sugar: the compiler converts the call to a static function call.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>extension ExtensionName on ExistingType {
  ReturnType newMethod() {
    // 'this' refers to the instance
    return ...;
  }
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>extension StringExtension on String {
  String capitalize() {
    if (isEmpty) return this;
    return this[0].toUpperCase() + substring(1).toLowerCase();
  }

  bool get isValidEmail => contains('@') &amp;&amp; contains('.');

  String repeat(int times) => this * times;
}

void main() {
  print('hello'.capitalize());           // Hello
  print('WORLD'.capitalize());           // World
  print('user@example.com'.isValidEmail); // true
  print('ha'.repeat(3));                 // hahaha
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Hello
World
true
hahaha</pre></div>
<h2 id="extending-built-in">Extending Built-in Types</h2>
<pre><code>extension IntExtension on int {
  bool get isEven => this % 2 == 0;
  bool get isOdd => this % 2 != 0;
  int get squared => this * this;
  List&lt;int&gt; to(int end) => List.generate(end - this + 1, (i) => this + i);
}

extension ListExtension&lt;T&gt; on List&lt;T&gt; {
  T? get secondOrNull => length >= 2 ? this[1] : null;
}

void main() {
  print(7.isOdd);         // true
  print(4.squared);       // 16
  print(1.to(5));         // [1, 2, 3, 4, 5]

  var items = ['a', 'b', 'c'];
  print(items.secondOrNull);  // b
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does extension methods let you do?</strong></p>
<ul><li>A) Modify a class's source code</li><li>B) Add methods to existing types without subclassing ✅</li><li>C) Override existing methods</li><li>D) Make classes immutable</li></ul>
<p><strong>2. Inside an extension method, what does this refer to?</strong></p>
<ul><li>A) The extension itself</li><li>B) The class being extended</li><li>C) The instance on which the method is called ✅</li><li>D) null</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Extension methods add new methods to existing types using the <code>extension on</code> syntax. Use <code>this</code> to access the instance. They work on built-in types, third-party types, and your own classes. Extensions make code more expressive without modifying the original class.</p>
    $dart_62$,
    'Learn Dart extension methods — add new functionality to existing types like String, int, and List without modifying or subclassing them.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 62, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Exception Handling in Dart', 'dart-exceptions',
    $dart_63$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Exceptions?</a></li><li><a href="#try-catch">Try-Catch</a></li><li><a href="#on">on Clause</a></li><li><a href="#finally">Finally</a></li><li><a href="#throw">Throwing Exceptions</a></li><li><a href="#custom">Custom Exceptions</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Exceptions?</h2>
<p>An exception is an error that occurs during program execution. Without handling, exceptions crash the program. Dart provides <code>try-catch-finally</code> to gracefully handle errors and keep programs running.</p>
<h2 id="try-catch">Try-Catch</h2>
<pre><code>try {
  int result = 10 ~/ 0;  // throws IntegerDivisionByZeroException
  print(result);
} catch (e) {
  print('Caught: $e');
}
// Program continues...</code></pre>
<h2 id="on">on Clause</h2>
<p>Use <code>on</code> to catch specific exception types:</p>
<pre><code>try {
  var list = [1, 2, 3];
  print(list[10]);  // RangeError
} on RangeError catch (e) {
  print('Range error: $${e.message}');
} on FormatException catch (e) {
  print('Format error: $e');
} catch (e, stackTrace) {
  print('Unknown error: $e');
  print(stackTrace);
}</code></pre>
<h2 id="finally">Finally</h2>
<p>The <code>finally</code> block always runs — whether or not an exception occurred. Use it for cleanup (closing files, releasing resources):</p>
<pre><code>void readFile() {
  print('Opening file...');
  try {
    throw FormatException('Invalid data');
  } catch (e) {
    print('Error: $e');
  } finally {
    print('Closing file...');  // always runs
  }
}</code></pre>
<h2 id="throw">Throwing Exceptions</h2>
<pre><code>void validateAge(int age) {
  if (age < 0) throw ArgumentError('Age cannot be negative: $age');
  if (age > 150) throw RangeError('Unrealistic age: $age');
  print('Valid age: $age');
}

void main() {
  try {
    validateAge(-5);
  } on ArgumentError catch (e) {
    print('Invalid argument: $${e.message}');
  }
}</code></pre>
<h2 id="custom">Custom Exceptions</h2>
<pre><code>class InsufficientFundsException implements Exception {
  final double amount;
  InsufficientFundsException(this.amount);

  @override
  String toString() => 'InsufficientFundsException: Need \$$amount more.';
}

class BankAccount {
  double balance;
  BankAccount(this.balance);

  void withdraw(double amount) {
    if (amount > balance) throw InsufficientFundsException(amount - balance);
    balance -= amount;
  }
}

void main() {
  var account = BankAccount(100.0);
  try {
    account.withdraw(150.0);
  } on InsufficientFundsException catch (e) {
    print(e);  // InsufficientFundsException: Need $50.0 more.
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Invalid argument: Age cannot be negative: -5
InsufficientFundsException: Need $50.0 more.</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which block always runs regardless of exceptions?</strong></p>
<ul><li>A) catch</li><li>B) try</li><li>C) finally ✅</li><li>D) on</li></ul>
<p><strong>2. How do you create a custom exception in Dart?</strong></p>
<ul><li>A) extends Exception</li><li>B) implements Exception ✅</li><li>C) mixin Exception</li><li>D) with Exception</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>try-catch</code> to handle exceptions. Use <code>on ExceptionType</code> to catch specific types. The <code>finally</code> block always runs for cleanup. Throw exceptions with <code>throw</code>. Create custom exceptions by implementing the <code>Exception</code> interface and overriding <code>toString()</code>.</p>
    $dart_63$,
    'Master Dart exception handling — try-catch-finally, on clauses for specific types, throwing exceptions, and building custom exception classes.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,exceptions', NOW(), 63, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Generics in Dart', 'dart-generics',
    $dart_67$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Generics?</a></li><li><a href="#why">Why Use Generics?</a></li><li><a href="#generic-class">Generic Classes</a></li><li><a href="#generic-function">Generic Functions</a></li><li><a href="#constraints">Type Constraints</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Generics?</h2>
<p>Generics allow you to write code that works with multiple types while remaining type-safe. Instead of writing separate classes for <code>List&lt;int&gt;</code> and <code>List&lt;String&gt;</code>, one generic <code>List&lt;T&gt;</code> handles both.</p>
<h2 id="why">Why Use Generics?</h2>
<ul>
<li><strong>Type safety:</strong> Catches type errors at compile time</li>
<li><strong>Code reuse:</strong> One implementation works for many types</li>
<li><strong>No casting:</strong> Avoids unsafe dynamic casts</li>
</ul>
<h2 id="generic-class">Generic Classes</h2>
<pre><code>class Box&lt;T&gt; {
  T value;
  Box(this.value);

  T getValue() => value;
  void setValue(T newValue) { value = newValue; }

  @override
  String toString() => 'Box&lt;$T&gt;($value)';
}

void main() {
  var intBox = Box&lt;int&gt;(42);
  var strBox = Box&lt;String&gt;('Hello');
  var dblBox = Box&lt;double&gt;(3.14);

  print(intBox);  // Box&lt;int&gt;(42)
  print(strBox);  // Box&lt;String&gt;(Hello)
  print(dblBox);  // Box&lt;double&gt;(3.14)

  intBox.setValue(100);
  print(intBox.getValue() + 1);  // 101
}</code></pre>
<h2 id="generic-function">Generic Functions</h2>
<pre><code>T first&lt;T&gt;(List&lt;T&gt; list) => list.first;
T last&lt;T&gt;(List&lt;T&gt; list) => list.last;

Pair&lt;A, B&gt; makePair&lt;A, B&gt;(A first, B second) => Pair(first, second);

class Pair&lt;A, B&gt; {
  A first;
  B second;
  Pair(this.first, this.second);
  @override String toString() => '($first, $second)';
}

void main() {
  print(first([1, 2, 3]));          // 1
  print(last(['a', 'b', 'c']));     // c
  print(makePair('key', 42));       // (key, 42)
}</code></pre>
<h2 id="constraints">Type Constraints</h2>
<p>Use <code>extends</code> to restrict which types can be used with a generic:</p>
<pre><code>// Only numeric types
T sum&lt;T extends num&gt;(List&lt;T&gt; values) {
  return values.reduce((a, b) => (a + b) as T);
}

print(sum([1, 2, 3, 4]));       // 10
print(sum([1.1, 2.2, 3.3]));   // 6.6
// sum(['a', 'b']); ← COMPILE ERROR</code></pre>
<h2 id="example">Example: Generic Stack</h2>
<pre><code>class Stack&lt;T&gt; {
  final List&lt;T&gt; _items = [];

  void push(T item) => _items.add(item);
  T pop() => _items.removeLast();
  T get peek => _items.last;
  bool get isEmpty => _items.isEmpty;
  int get size => _items.length;
}

void main() {
  var stack = Stack&lt;int&gt;();
  stack.push(1); stack.push(2); stack.push(3);
  print(stack.peek);  // 3
  print(stack.pop()); // 3
  print(stack.size);  // 2
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>3
3
2</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does &lt;T&gt; represent in a generic class?</strong></p>
<ul><li>A) A template literal</li><li>B) A type parameter that is specified when using the class ✅</li><li>C) A type check</li><li>D) A generic interface</li></ul>
<p><strong>2. What does &lt;T extends num&gt; do?</strong></p>
<ul><li>A) T must be exactly num</li><li>B) T must be a subtype of num ✅</li><li>C) T extends the num class</li><li>D) T is optional</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Generics write type-safe, reusable code with type parameters (<code>&lt;T&gt;</code>). Generic classes and functions work with any type while preserving type information. Use <code>T extends Type</code> to constrain which types are allowed. Built-in types like <code>List&lt;T&gt;</code>, <code>Map&lt;K, V&gt;</code>, and <code>Set&lt;T&gt;</code> are all generic.</p>
    $dart_67$,
    'Learn Dart generics — write type-safe reusable code with type parameters, generic classes, generic functions, and type constraints.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 67, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Enums in Dart', 'dart-enums',
    $dart_69$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Enums?</a></li><li><a href="#basic">Basic Enums</a></li><li><a href="#enhanced">Enhanced Enums (Dart 2.17+)</a></li><li><a href="#switch">Enums in Switch</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Enums?</h2>
<p>An enum (enumeration) is a type with a fixed set of named constant values. Enums make code more readable and type-safe by replacing magic strings or integers with meaningful names.</p>
<h2 id="basic">Basic Enums</h2>
<pre><code>enum Direction { north, south, east, west }
enum Status { active, inactive, pending, banned }
enum Color { red, green, blue }

void main() {
  Direction dir = Direction.north;
  print(dir);           // Direction.north
  print(dir.name);      // north
  print(dir.index);     // 0

  // All values
  print(Direction.values);  // [Direction.north, Direction.south, ...]
}</code></pre>
<h2 id="enhanced">Enhanced Enums (Dart 2.17+)</h2>
<p>Dart 2.17+ allows enums to have fields, methods, and constructors:</p>
<pre><code>enum Planet {
  mercury(3.7),
  venus(8.87),
  earth(9.81),
  mars(3.72);

  final double gravity;  // m/s²
  const Planet(this.gravity);

  double weightOn(double mass) => mass * gravity;
}

void main() {
  double myMass = 70.0;  // kg on Earth
  for (var planet in Planet.values) {
    print('Weight on $${planet.name}: '
          '$${planet.weightOn(myMass).toStringAsFixed(1)} N');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Weight on mercury: 259.0 N
Weight on venus: 620.9 N
Weight on earth: 686.7 N
Weight on mars: 260.4 N</pre></div>
<h2 id="switch">Enums in Switch</h2>
<pre><code>Status userStatus = Status.pending;

switch (userStatus) {
  case Status.active:
    print('User is active');
    break;
  case Status.pending:
    print('Awaiting approval');
    break;
  case Status.banned:
    print('Access denied');
    break;
  default:
    print('Unknown status');
}

// Dart 3 pattern matching switch:
String label = switch (userStatus) {
  Status.active   => 'Active',
  Status.inactive => 'Inactive',
  Status.pending  => 'Pending',
  Status.banned   => 'Banned',
};</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does enum.name return?</strong></p>
<ul><li>A) The enum type name</li><li>B) The constant name as a String ✅</li><li>C) The index</li><li>D) null</li></ul>
<p><strong>2. Can Dart enums have methods?</strong></p>
<ul><li>A) No</li><li>B) Yes, since Dart 2.17 ✅</li><li>C) Only static methods</li><li>D) Only in abstract enums</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Enums define a fixed set of named constants. Use <code>enum.name</code> for the string name, <code>enum.index</code> for position, and <code>Enum.values</code> for all constants. Dart 2.17+ enhanced enums support fields, methods, and constructors. Use enums in switch statements for exhaustive pattern matching.</p>
    $dart_69$,
    'Learn Dart enums — define fixed named constants, enhanced enums with fields and methods, and use them in switch statements for type-safe branching.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 69, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Cascade Notation in Dart', 'dart-cascade-notation',
    $dart_72$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Cascade Notation?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#null-cascade">Null-Aware Cascade</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Cascade Notation?</h2>
<p>The cascade operator <code>..</code> lets you perform a sequence of operations on the same object without repeating the object name. It returns the original object, not the result of the operation.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>// Without cascade:
var paint = Paint();
paint.color = Color.blue;
paint.strokeWidth = 5.0;
paint.style = PaintingStyle.stroke;

// With cascade:
var paint = Paint()
  ..color = Color.blue
  ..strokeWidth = 5.0
  ..style = PaintingStyle.stroke;</code></pre>
<h2 id="example">Example</h2>
<pre><code>class QueryBuilder {
  String _table = '';
  List&lt;String&gt; _conditions = [];
  int _limit = 100;

  QueryBuilder from(String table) { _table = table; return this; }
  QueryBuilder where(String cond) { _conditions.add(cond); return this; }
  QueryBuilder limit(int n) { _limit = n; return this; }

  String build() =>
    'SELECT * FROM $_table WHERE $${_conditions.join(" AND ")} LIMIT $_limit';
}

void main() {
  // Using cascade
  var builder = QueryBuilder()
    ..from('users')
    ..where('age > 18')
    ..where('status = "active"')
    ..limit(50);

  print(builder.build());

  // List cascade
  var list = &lt;int&gt;[]
    ..add(1)
    ..add(2)
    ..addAll([3, 4, 5])
    ..sort();
  print(list);  // [1, 2, 3, 4, 5]
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>SELECT * FROM users WHERE age > 18 AND status = "active" LIMIT 50
[1, 2, 3, 4, 5]</pre></div>
<h2 id="null-cascade">Null-Aware Cascade</h2>
<p>Use <code>?..</code> to cascade only if the object is not null:</p>
<pre><code>List&lt;int&gt;? nums = null;
nums?..add(1)..add(2);  // safe — does nothing if nums is null
print(nums);            // null

nums = [];
nums?..add(1)..add(2);
print(nums);            // [1, 2]</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does the cascade operator .. return?</strong></p>
<ul><li>A) The result of the last operation</li><li>B) The original object ✅</li><li>C) null</li><li>D) A copy of the object</li></ul>
<p><strong>2. Which cascade operator is null-safe?</strong></p>
<ul><li>A) ..</li><li>B) ...</li><li>C) ?..</li><li>D) ??.. ✅ (actually ?.. is correct)</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The cascade operator <code>..</code> chains multiple operations on the same object in a single expression. It returns the original object, making it perfect for builder patterns and configuration. Use <code>?..</code> for null-safe cascades.</p>
    $dart_72$,
    'Learn Dart cascade notation (..) — chain multiple operations on the same object for cleaner builder patterns, configuration, and collection manipulation.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,advanced-dart', NOW(), 72, 'PUBLISHED', 'ACT', 'system'
);
