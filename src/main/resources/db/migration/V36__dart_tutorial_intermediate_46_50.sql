-- Section 8: Null Safety (Lessons 46–50)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Null Safety in Dart', 'dart-null-safety',
    $dart_46$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Null Safety?</a></li><li><a href="#why">Why Does It Matter?</a></li><li><a href="#non-nullable">Non-Nullable by Default</a></li><li><a href="#nullable">Making Variables Nullable</a></li><li><a href="#operators">Null Safety Operators</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Null Safety?</h2>
<p>Null safety is a type system feature (introduced in Dart 2.12) that eliminates null reference errors — one of the most common and frustrating bugs in programming. With null safety, the compiler guarantees that a variable cannot be null unless you explicitly allow it.</p>
<h2 id="why">Why Does It Matter?</h2>
<p>In languages without null safety, any variable can be null at any time, causing <code>NullPointerException</code> errors at runtime. Dart's null safety catches these errors at compile time — before your code even runs.</p>
<pre><code>// Without null safety (old Dart / other languages):
String name = null;          // allowed — crashes at runtime
print(name.length);          // NullPointerException!

// With null safety (Dart 2.12+):
String name = null;          // COMPILE ERROR — String cannot be null
String? name = null;         // OK — String? allows null</code></pre>
<h2 id="non-nullable">Non-Nullable by Default</h2>
<p>In Dart with null safety, all types are non-nullable by default. The compiler guarantees they always hold a valid value:</p>
<pre><code>int age = 25;        // cannot be null
String name = 'Bob'; // cannot be null
bool flag = true;    // cannot be null

// age = null;  ← COMPILE ERROR</code></pre>
<h2 id="nullable">Making Variables Nullable</h2>
<p>Add <code>?</code> after the type to allow null values:</p>
<pre><code>int? age = null;         // allowed
String? name = null;     // allowed
double? score;           // defaults to null

// Must check before use:
if (age != null) {
  print(age + 1);        // safe
}
print(age?.toString());  // null-aware access</code></pre>
<h2 id="operators">Null Safety Operators</h2>
<table><thead><tr><th>Operator</th><th>Name</th><th>Use</th></tr></thead><tbody>
<tr><td><code>?</code></td><td>Nullable type</td><td><code>String? name</code></td></tr>
<tr><td><code>??</code></td><td>If-null (default)</td><td><code>name ?? 'Guest'</code></td></tr>
<tr><td><code>??=</code></td><td>Null-aware assign</td><td><code>name ??= 'Guest'</code></td></tr>
<tr><td><code>?.</code></td><td>Null-aware access</td><td><code>name?.length</code></td></tr>
<tr><td><code>!</code></td><td>Null assertion</td><td><code>name!</code></td></tr>
<tr><td><code>late</code></td><td>Late initialization</td><td><code>late String name</code></td></tr>
</tbody></table>
<h2 id="example">Example</h2>
<pre><code>void main() {
  String firstName = 'John';   // non-nullable
  String? lastName = null;     // nullable

  // Use ?? for a default
  print('$firstName $${lastName ?? 'Doe'}');  // John Doe

  // Null-aware access
  print(lastName?.toUpperCase());  // null

  // Assign lastName
  lastName = 'Smith';
  print(lastName.toUpperCase());   // SMITH (safe — no longer null)
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>John Doe
null
SMITH</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. In Dart null safety, can a String variable be null by default?</strong></p>
<ul><li>A) Yes</li><li>B) No ✅</li><li>C) Only if declared with var</li><li>D) Only at class level</li></ul>
<p><strong>2. What does adding ? after a type mean?</strong></p>
<ul><li>A) The variable is optional</li><li>B) The variable can be null ✅</li><li>C) The variable is checked at runtime</li><li>D) The variable is private</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Dart null safety (since 2.12) makes all types non-nullable by default. Add <code>?</code> to allow null. The compiler catches null errors at compile time, not runtime. Key operators: <code>??</code>, <code>??=</code>, <code>?.</code>, <code>!</code>, and <code>late</code> — each handles nullable values safely.</p>
    $dart_46$,
    'Understand Dart null safety — non-nullable types by default, making variables nullable with ?, and using null-aware operators to prevent runtime null errors.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,null-safety', NOW(), 46, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Nullable Variables in Dart', 'dart-nullable-variables',
    $dart_47$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#declaring">Declaring Nullable Variables</a></li><li><a href="#checking">Null Checks</a></li><li><a href="#promotion">Type Promotion</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="declaring">Declaring Nullable Variables</h2>
<pre><code>String? username;      // null by default
int? age = null;       // explicitly null
double? score = 98.5;  // has a value, but could be null later

// Nullable parameters in functions
void printName(String? name) {
  print(name ?? 'Anonymous');
}

printName(null);     // Anonymous
printName('Alice');  // Alice</code></pre>
<h2 id="checking">Null Checks</h2>
<p>Before using a nullable variable, check if it is null:</p>
<pre><code>String? email = getUserEmail(); // might return null

// Method 1: if check (promotes to non-nullable inside if)
if (email != null) {
  print(email.toUpperCase()); // safe — email is String here
}

// Method 2: ?? operator
print(email ?? 'no-email@example.com');

// Method 3: ?.  null-aware access
print(email?.toLowerCase());</code></pre>
<h2 id="promotion">Type Promotion</h2>
<p>Inside an <code>if (variable != null)</code> check, Dart automatically promotes the nullable type to its non-nullable counterpart:</p>
<pre><code>String? text = maybeGetText();

if (text != null) {
  // Here, text is treated as String (not String?)
  // All String methods are available without ?
  int len = text.length;  // no error
  print(text.toUpperCase());
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>String? getUserEmail() => 'user@example.com';
int? getAge() => null;

void main() {
  String? email = getUserEmail();
  int? age = getAge();

  // Pattern: check and use
  if (email != null) {
    print('Email domain: $${email.split("@").last}');
  }

  // Pattern: provide default
  int userAge = age ?? 0;
  print('Age: $userAge');  // 0

  // Pattern: early return
  void processAge(int? a) {
    if (a == null) { print('Age unknown'); return; }
    print('In $${18 - a} years you will be 18');
  }
  processAge(age);   // Age unknown
  processAge(14);    // In 4 years you will be 18
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Email domain: example.com
Age: 0
Age unknown
In 4 years you will be 18</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is type promotion in Dart?</strong></p>
<ul><li>A) Converting int to double</li><li>B) Dart treating a nullable as non-nullable after a null check ✅</li><li>C) Casting with as</li><li>D) Upgrading a variable type</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Nullable variables hold either a value or null. Always check with <code>if (x != null)</code>, <code>??</code>, or <code>?.</code> before using them. Dart's type promotion automatically narrows the type inside null checks so you can use all non-nullable methods safely.</p>
    $dart_47$,
    'Learn to work with nullable variables in Dart — null checks, type promotion, default values, and safe access patterns with ??, ?., and if-null guards.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,null-safety', NOW(), 47, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Null Assertion Operator in Dart', 'dart-null-assertion',
    $dart_48$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is the Null Assertion Operator?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#when">When to Use It</a></li><li><a href="#danger">Danger: Runtime Error</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is the Null Assertion Operator?</h2>
<p>The null assertion operator <code>!</code> tells Dart: "I know this nullable value is not null right now — trust me." It converts a nullable type (<code>T?</code>) to a non-nullable type (<code>T</code>) at runtime. If the value IS null, it throws a <code>Null check operator used on a null value</code> error.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>String? name = 'Alice';
String nonNullable = name!;  // asserts name is not null
print(nonNullable.length);   // 5</code></pre>
<h2 id="when">When to Use It</h2>
<p>Use <code>!</code> only when you are absolutely certain the value cannot be null at that point, and the type system cannot figure it out automatically:</p>
<pre><code>// Example: you queried a database and know the record exists
Map&lt;String, String&gt; cache = {'userId': 'abc123'};
String userId = cache['userId']!;  // you know this key exists
print(userId);  // abc123

// Example: widget keys in Flutter
String? _text;
void initState() {
  _text = 'initialized';
}
// Later in build:
print(_text!);  // safe after initState ran</code></pre>
<h2 id="danger">⚠️ Danger: Runtime Error</h2>
<p>If the value is actually null, <code>!</code> throws at runtime — which is exactly the kind of crash null safety is meant to prevent:</p>
<pre><code>String? name = null;
print(name!);  // RUNTIME ERROR: Null check operator used on a null value

// Prefer safe alternatives:
print(name ?? 'default');  // safe
if (name != null) print(name);  // safe</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  List&lt;String&gt; items = ['apple', 'banana', 'cherry'];

  // indexOf returns -1 if not found, but here we know it exists
  int? idx;
  for (int i = 0; i < items.length; i++) {
    if (items[i] == 'banana') { idx = i; break; }
  }

  // We know idx is set — assert it
  print('banana is at index $${idx!}');  // 1

  // Safe version (better practice)
  print('banana is at index $${idx ?? -1}');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>banana is at index 1
banana is at index 1</pre></div>
<div class="note-box"><strong>📝 Best Practice:</strong> Prefer <code>??</code> and <code>?.</code> over <code>!</code>. Use <code>!</code> only as a last resort when you have domain knowledge that guarantees non-null and the type system cannot infer it.</div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does <code>name!</code> do if name is null?</strong></p>
<ul><li>A) Returns null</li><li>B) Returns empty string</li><li>C) Throws a runtime error ✅</li><li>D) Compiles but warns</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The null assertion operator <code>!</code> converts <code>T?</code> to <code>T</code> at runtime. Use it sparingly — only when you are 100% certain the value is not null. If the value is null, it throws immediately. Prefer <code>??</code>, <code>?.</code>, or null checks for safer code.</p>
    $dart_48$,
    'Learn the Dart null assertion operator (!) — when it is safe to use, the runtime danger of asserting null, and why safer alternatives are preferred.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,null-safety', NOW(), 48, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Late Keyword in Dart', 'dart-late-keyword',
    $dart_49$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is late?</a></li><li><a href="#when">When to Use late</a></li><li><a href="#lazy">Lazy Initialization</a></li><li><a href="#example">Example</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is late?</h2>
<p>The <code>late</code> keyword declares a non-nullable variable that will be initialized <strong>later</strong> (before it is first used). It tells Dart: "I promise this will be initialized before anyone reads it — skip the compile-time check."</p>
<pre><code>late String name;  // not initialized yet — no compile error

void setup() {
  name = 'Alice';  // initialized before use
}

void main() {
  setup();
  print(name);  // Alice — safe
}</code></pre>
<h2 id="when">When to Use late</h2>
<ul>
<li>Class fields that are initialized in a method (not the constructor)</li>
<li>Variables initialized after an async operation completes</li>
<li>Expensive computations you want to delay (lazy initialization)</li>
<li>Flutter: accessing controllers or services set up in <code>initState()</code></li>
</ul>
<h2 id="lazy">Lazy Initialization</h2>
<p>When a <code>late</code> variable has an initializer, the initializer runs only the first time the variable is accessed:</p>
<pre><code>late String expensiveResult = computeExpensiveValue();

String computeExpensiveValue() {
  print('Computing...');
  return 'result';
}

void main() {
  print('Before access');
  print(expensiveResult);  // Computing... then result
  print(expensiveResult);  // result (not computed again)
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Before access
Computing...
result
result</pre></div>
<h2 id="example">Example</h2>
<pre><code>class DatabaseService {
  late String connectionString;  // set during init, not constructor

  void initialize(String host, String db) {
    connectionString = 'host=$host;db=$db';
  }

  void query(String sql) {
    print('Running on $connectionString: $sql');
  }
}

void main() {
  var db = DatabaseService();
  db.initialize('localhost', 'myapp');
  db.query('SELECT * FROM users');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Running on host=localhost;db=myapp: SELECT * FROM users</pre></div>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li>Reading a <code>late</code> variable before it is initialized throws a <code>LateInitializationError</code> at runtime.</li>
<li>Using <code>late</code> to avoid initializing a variable — use nullable <code>T?</code> instead if the variable may genuinely never be set.</li>
</ul>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What error occurs if you read a late variable before initializing it?</strong></p>
<ul><li>A) NullPointerException</li><li>B) LateInitializationError ✅</li><li>C) TypeError</li><li>D) Compile error</li></ul>
<p><strong>2. When does the initializer of a lazy late variable run?</strong></p>
<ul><li>A) At program start</li><li>B) When the variable is declared</li><li>C) On first access ✅</li><li>D) Every time it is accessed</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>late</code> keyword defers initialization of non-nullable variables. It supports lazy initialization (only computed on first access). Use it for fields initialized in lifecycle methods or for expensive computations. Reading before initialization throws <code>LateInitializationError</code>.</p>
    $dart_49$,
    'Learn the Dart late keyword — defer non-nullable variable initialization, enable lazy computation, and use it safely in Flutter and class lifecycle methods.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,null-safety', NOW(), 49, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Required Keyword in Dart', 'dart-required-keyword',
    $dart_50$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is required?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#vs-optional">required vs Optional Parameters</a></li><li><a href="#classes">required in Classes</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is required?</h2>
<p>The <code>required</code> keyword marks a named parameter as mandatory — the caller must provide it. Without <code>required</code>, named parameters are optional by default. With null safety, <code>required</code> is essential for non-nullable named parameters.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>void greet({required String name, required int age}) {
  print('Hello $name, you are $age years old!');
}

void main() {
  greet(name: 'Alice', age: 25);  // OK
  // greet(name: 'Bob');           ← COMPILE ERROR: age is required
}</code></pre>
<h2 id="vs-optional">required vs Optional Parameters</h2>
<table><thead><tr><th>Scenario</th><th>Declaration</th><th>Must provide?</th></tr></thead><tbody>
<tr><td>Optional with default</td><td><code>{String name = 'Guest'}</code></td><td>No</td></tr>
<tr><td>Optional nullable</td><td><code>{String? name}</code></td><td>No (defaults to null)</td></tr>
<tr><td>Required non-nullable</td><td><code>{required String name}</code></td><td>Yes ✅</td></tr>
</tbody></table>
<h2 id="classes">required in Classes</h2>
<p>Use <code>required</code> in class constructors to enforce mandatory fields:</p>
<pre><code>class User {
  final String name;
  final String email;
  final int age;

  User({
    required this.name,
    required this.email,
    required this.age,
  });

  @override
  String toString() => 'User($name, $email, age=$age)';
}

void main() {
  var user = User(name: 'Alice', email: 'alice@example.com', age: 28);
  print(user);
  // User(Alice, alice@example.com, age=28)
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>class Product {
  final String id;
  final String name;
  final double price;
  final String? description;  // optional

  Product({
    required this.id,
    required this.name,
    required this.price,
    this.description,          // no required — defaults to null
  });
}

void main() {
  var p1 = Product(id: 'P001', name: 'Laptop', price: 999.99);
  var p2 = Product(
    id: 'P002',
    name: 'Phone',
    price: 499.99,
    description: 'Latest model smartphone',
  );
  print('$${p1.name}: \$$${p1.price}');
  print('$${p2.name}: $${p2.description}');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Laptop: $999.99
Phone: Latest model smartphone</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What happens if you omit a required named parameter when calling a function?</strong></p>
<ul><li>A) Runtime error</li><li>B) Compile error ✅</li><li>C) Uses null as default</li><li>D) Uses empty string</li></ul>
<p><strong>2. Can a required parameter also be nullable (String?)?</strong></p>
<ul><li>A) No</li><li>B) Yes ✅</li><li>C) Only if it has a default</li><li>D) Only in constructors</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>required</code> keyword enforces that named parameters must be provided by the caller. Omitting a required parameter is a compile-time error. Use it in functions and class constructors to make the API clear and safe, especially for non-nullable named parameters.</p>
    $dart_50$,
    'Learn the required keyword in Dart — enforce mandatory named parameters in functions and constructors, caught at compile time with null safety.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,null-safety', NOW(), 50, 'PUBLISHED', 'ACT', 'system'
);
