-- Section 2: Dart Basics Part 2 (Lessons 11–15)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Numbers in Dart',
    'dart-numbers',
    $dart_11$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#int">Integer (int)</a></li>
    <li><a href="#double">Double</a></li>
    <li><a href="#num">The num Type</a></li>
    <li><a href="#parsing">Parsing Numbers</a></li>
    <li><a href="#math">Math Operations</a></li>
    <li><a href="#dart-math">dart:math Library</a></li>
    <li><a href="#example">Example</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="int">Integer (int)</h2>
<p>The <code>int</code> type stores whole numbers (positive, negative, or zero) without a decimal point. Dart integers can be arbitrarily large.</p>
<pre><code>int a = 100;
int b = -50;
int hex = 0xFF;          // hexadecimal = 255
int binary = 0b1010;     // binary = 10 (Dart 3+)
print(a + b);            // 50
print(hex);              // 255</code></pre>

<h2 id="double">Double</h2>
<p>The <code>double</code> type stores 64-bit floating-point numbers (numbers with decimals).</p>
<pre><code>double pi = 3.14159;
double e = 2.71828;
double sci = 1.5e6;      // scientific notation = 1500000.0
print(pi.toStringAsFixed(2));  // 3.14</code></pre>

<h2 id="num">The num Type</h2>
<p><code>num</code> is the parent type of both <code>int</code> and <code>double</code>. Use it when a variable might hold either type:</p>
<pre><code>num x = 10;     // int
x = 3.14;       // now double — allowed with num
print(x);       // 3.14</code></pre>

<h2 id="parsing">Parsing Numbers</h2>
<p>Convert strings to numbers using <code>int.parse()</code> and <code>double.parse()</code>:</p>
<pre><code>String s1 = '42';
String s2 = '3.14';

int n1 = int.parse(s1);       // 42
double n2 = double.parse(s2); // 3.14

// Safe parsing (returns null on failure):
int? n3 = int.tryParse('abc'); // null
print(n3 ?? 0);                // 0</code></pre>

<h2 id="math">Math Operations</h2>
<table>
  <thead><tr><th>Operator</th><th>Description</th><th>Example</th><th>Result</th></tr></thead>
  <tbody>
    <tr><td><code>+</code></td><td>Addition</td><td><code>10 + 3</code></td><td><code>13</code></td></tr>
    <tr><td><code>-</code></td><td>Subtraction</td><td><code>10 - 3</code></td><td><code>7</code></td></tr>
    <tr><td><code>*</code></td><td>Multiplication</td><td><code>10 * 3</code></td><td><code>30</code></td></tr>
    <tr><td><code>/</code></td><td>Division (double)</td><td><code>10 / 3</code></td><td><code>3.333</code></td></tr>
    <tr><td><code>~/</code></td><td>Integer division</td><td><code>10 ~/ 3</code></td><td><code>3</code></td></tr>
    <tr><td><code>%</code></td><td>Modulo (remainder)</td><td><code>10 % 3</code></td><td><code>1</code></td></tr>
  </tbody>
</table>

<h2 id="dart-math">dart:math Library</h2>
<pre><code>import 'dart:math';

void main() {
  print(sqrt(16));       // 4.0
  print(pow(2, 8));      // 256.0
  print(max(10, 20));    // 20
  print(min(10, 20));    // 10
  print(pi);             // 3.141592653589793

  // Random number 0–99
  var random = Random();
  print(random.nextInt(100));
}</code></pre>

<h2 id="example">Example</h2>
<pre><code>void main() {
  double principal = 1000.0;
  double rate = 5.0;
  int years = 3;

  double interest = (principal * rate * years) / 100;
  double total = principal + interest;

  print('Principal: \$$${principal.toStringAsFixed(2)}');
  print('Interest: \$$${interest.toStringAsFixed(2)}');
  print('Total after $years years: \$$${total.toStringAsFixed(2)}');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Principal: $1000.00
Interest: $150.00
Total after 3 years: $1150.00</pre>
</div>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a program to calculate the area and circumference of a circle (use <code>dart:math</code> for <code>pi</code>).</li>
    <li>Parse the string <code>'2024'</code> to an integer and print it doubled.</li>
    <li>Find the remainder when 100 is divided by 7 using the <code>%</code> operator.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What operator performs integer (truncating) division in Dart?</strong></p>
  <ul><li>A) /</li><li>B) //</li><li>C) ~/ ✅</li><li>D) div</li></ul>
  <p><strong>2. Which method safely parses a string to int (returning null on failure)?</strong></p>
  <ul><li>A) int.parse()</li><li>B) int.tryParse() ✅</li><li>C) int.convert()</li><li>D) int.from()</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart supports <code>int</code> for whole numbers and <code>double</code> for decimals. Use <code>num</code> when the type can be either. Parse strings with <code>int.parse()</code> or the safe <code>int.tryParse()</code>. Import <code>dart:math</code> for mathematical functions like <code>sqrt()</code>, <code>pow()</code>, and <code>Random()</code>.</p>
    $dart_11$,
    'Deep dive into Dart number types — int, double, and num. Learn parsing, arithmetic operators, integer division, and the dart:math library.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,numbers',
    NOW(),
    11,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Booleans in Dart',
    'dart-booleans',
    $dart_12$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-is-bool">What is a Boolean?</a></li>
    <li><a href="#declaring">Declaring Booleans</a></li>
    <li><a href="#operators">Boolean Operators</a></li>
    <li><a href="#comparisons">Comparison Results</a></li>
    <li><a href="#example">Example</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-is-bool">What is a Boolean?</h2>
<p>A Boolean is a data type with only two possible values: <code>true</code> or <code>false</code>. Booleans are the foundation of conditional logic — every <code>if</code> statement, loop condition, and logical expression evaluates to a boolean.</p>

<h2 id="declaring">Declaring Booleans</h2>
<pre><code>bool isLoggedIn = true;
bool hasErrors = false;
bool isAdult = true;

var isDarkMode = false;   // type inferred as bool</code></pre>

<h2 id="operators">Boolean Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr></thead>
  <tbody>
    <tr><td><code>&amp;&amp;</code></td><td>AND</td><td><code>true &amp;&amp; false</code></td><td><code>false</code></td></tr>
    <tr><td><code>||</code></td><td>OR</td><td><code>true || false</code></td><td><code>true</code></td></tr>
    <tr><td><code>!</code></td><td>NOT</td><td><code>!true</code></td><td><code>false</code></td></tr>
  </tbody>
</table>
<pre><code>bool a = true;
bool b = false;

print(a &amp;&amp; b);   // false (both must be true)
print(a || b);   // true  (at least one must be true)
print(!a);       // false (NOT true = false)
print(!b);       // true  (NOT false = true)</code></pre>

<h2 id="comparisons">Comparison Results</h2>
<p>Comparison operators always return a boolean:</p>
<pre><code>print(5 > 3);    // true
print(5 < 3);    // false
print(5 == 5);   // true
print(5 != 3);   // true
print(5 >= 5);   // true
print(5 <= 4);   // false</code></pre>

<h2 id="example">Example</h2>
<pre><code>void main() {
  int age = 20;
  bool hasID = true;
  double balance = 50.0;

  bool canEnter = age >= 18 &amp;&amp; hasID;
  bool canAfford = balance >= 30.0;

  print('Can enter: $canEnter');          // true
  print('Can afford ticket: $canAfford'); // true
  print('All good: $${canEnter &amp;&amp; canAfford}'); // true

  // Negation
  bool isMinor = !(age >= 18);
  print('Is minor: $isMinor');            // false
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Can enter: true
Can afford ticket: true
All good: true
Is minor: false</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li>Using <code>=</code> (assignment) instead of <code>==</code> (comparison): <code>if (x = 5)</code> is invalid in Dart.</li>
  <li>Treating non-zero integers as truthy — Dart does NOT allow <code>if (1)</code>. You must use an actual <code>bool</code>.</li>
  <li>Confusing <code>&amp;&amp;</code> with <code>&amp;</code> — use <code>&amp;&amp;</code> for logical AND in conditions.</li>
</ul>

<div class="note-box">
  <strong>📝 Note:</strong> Unlike JavaScript or C, Dart does not have "truthy" or "falsy" values. Only actual <code>bool</code> values (<code>true</code>/<code>false</code>) can be used in conditions.
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What is the result of <code>true &amp;&amp; false</code>?</strong></p>
  <ul><li>A) true</li><li>B) false ✅</li><li>C) null</li><li>D) Error</li></ul>
  <p><strong>2. Can you use <code>if (1)</code> in Dart?</strong></p>
  <ul><li>A) Yes, 1 is truthy</li><li>B) No, Dart requires a bool ✅</li><li>C) Only in debug mode</li><li>D) Yes, it means true</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Booleans in Dart hold only <code>true</code> or <code>false</code>. Use <code>&amp;&amp;</code> (AND), <code>||</code> (OR), and <code>!</code> (NOT) for boolean logic. Comparison operators return booleans. Dart requires actual boolean values in conditions — non-zero integers are NOT truthy.</p>
    $dart_12$,
    'Learn about Dart booleans — the true/false type that powers all conditional logic. Master AND, OR, NOT operators and avoid common boolean mistakes.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,booleans',
    NOW(),
    12,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Type Conversion in Dart',
    'dart-type-conversion',
    $dart_13$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-is">What is Type Conversion?</a></li>
    <li><a href="#string-to-number">String to Number</a></li>
    <li><a href="#number-to-string">Number to String</a></li>
    <li><a href="#int-to-double">int to double</a></li>
    <li><a href="#double-to-int">double to int</a></li>
    <li><a href="#bool-conversions">Boolean Conversions</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-is">What is Type Conversion?</h2>
<p>Type conversion (or type casting) is the process of changing a value from one data type to another. In Dart this is always <strong>explicit</strong> — you must intentionally convert a value; Dart will not silently convert types for you.</p>

<h2 id="string-to-number">String to Number</h2>
<pre><code>// String → int
String s1 = '42';
int n1 = int.parse(s1);
print(n1 + 8);   // 50

// String → double
String s2 = '3.14';
double n2 = double.parse(s2);
print(n2 * 2);   // 6.28

// Safe parsing (no exception on failure)
int? safe = int.tryParse('abc');
print(safe);     // null</code></pre>

<h2 id="number-to-string">Number to String</h2>
<pre><code>int n = 100;
double d = 3.14159;

String s1 = n.toString();           // '100'
String s2 = d.toString();           // '3.14159'
String s3 = d.toStringAsFixed(2);   // '3.14'
String s4 = n.toRadixString(16);    // '64' (hex)

print(s1 + ' dollars');             // 100 dollars</code></pre>

<h2 id="int-to-double">int to double</h2>
<pre><code>int i = 5;
double d = i.toDouble();   // 5.0
double d2 = i / 1;         // also 5.0 (division always returns double)
print(d);  // 5.0</code></pre>

<h2 id="double-to-int">double to int</h2>
<pre><code>double d = 9.99;
int i1 = d.toInt();       // 9  (truncates decimal)
int i2 = d.round();       // 10 (rounds to nearest)
int i3 = d.ceil();        // 10 (rounds up)
int i4 = d.floor();       // 9  (rounds down)
print('$i1 $i2 $i3 $i4'); // 9 10 10 9</code></pre>

<h2 id="bool-conversions">Boolean Conversions</h2>
<p>Dart does not automatically convert other types to bool. Use explicit comparisons:</p>
<pre><code>int n = 0;
// bool b = n;  ← ERROR in Dart
bool b = n != 0;    // explicit conversion: false
bool b2 = n == 0;   // true

String s = '';
bool isEmpty = s.isEmpty;  // true</code></pre>

<h2 id="example">Full Example</h2>
<pre><code>void main() {
  // Simulating user input as strings
  String ageInput = '25';
  String heightInput = '5.9';

  int age = int.parse(ageInput);
  double height = double.parse(heightInput);

  print('Age: $age ($${age.runtimeType})');
  print('Height: $height ($${height.runtimeType})');

  // Convert back to string for display
  String summary = 'Age $age, Height $${height.toStringAsFixed(1)} ft';
  print(summary);

  // int ↔ double
  double ageDouble = age.toDouble();
  int heightInt = height.toInt();
  print('Age as double: $ageDouble');
  print('Height as int: $heightInt');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Age: 25 (int)
Height: 5.9 (double)
Age 25, Height 5.9 ft
Age as double: 25.0
Height as int: 5</pre>
</div>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Parse <code>'100'</code> and <code>'45.5'</code> from strings, add them, and print the result.</li>
    <li>Convert the double <code>7.8</code> to an int using <code>round()</code>, <code>floor()</code>, and <code>ceil()</code> — print all three.</li>
    <li>Convert the integer <code>255</code> to its hexadecimal string representation.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What does <code>double.toInt()</code> do to <code>3.9</code>?</strong></p>
  <ul><li>A) Returns 4 (rounds)</li><li>B) Returns 3 (truncates) ✅</li><li>C) Returns 3.9</li><li>D) Throws an error</li></ul>
  <p><strong>2. Which method safely converts a string to int without throwing an exception?</strong></p>
  <ul><li>A) int.parse()</li><li>B) int.convert()</li><li>C) int.tryParse() ✅</li><li>D) int.cast()</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart type conversion is always explicit. Convert strings to numbers with <code>int.parse()</code> / <code>double.parse()</code>. Convert numbers to strings with <code>toString()</code> / <code>toStringAsFixed()</code>. Convert between int and double with <code>toDouble()</code> / <code>toInt()</code>. Use <code>tryParse()</code> for safe parsing that returns <code>null</code> on failure.</p>
    $dart_13$,
    'Learn explicit type conversion in Dart — converting between String, int, double, and bool safely using parse(), toString(), toInt(), and toDouble().',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,type-conversion',
    NOW(),
    13,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Constants in Dart: final and const',
    'dart-constants',
    $dart_14$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-are">What are Constants?</a></li>
    <li><a href="#final">The final Keyword</a></li>
    <li><a href="#const">The const Keyword</a></li>
    <li><a href="#difference">final vs const</a></li>
    <li><a href="#const-objects">const Objects &amp; Lists</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-are">What are Constants?</h2>
<p>A constant is a variable whose value cannot be changed after it is set. Using constants makes your code safer (prevents accidental modification), more readable (meaningful names), and sometimes more performant (compile-time constants).</p>

<h2 id="final">The final Keyword</h2>
<p>A <code>final</code> variable can be set <strong>only once</strong>, at runtime. Its value is determined when the program runs.</p>
<pre><code>final String name = 'Alice';
final int year = DateTime.now().year;  // runtime value OK

// name = 'Bob';  ← ERROR: can't change a final variable

final List&lt;int&gt; numbers = [1, 2, 3];
numbers.add(4);  // ✅ OK — the list itself can be modified
// numbers = [5, 6];  ← ERROR — can't reassign the reference</code></pre>

<h2 id="const">The const Keyword</h2>
<p>A <code>const</code> variable is a <strong>compile-time constant</strong>. Its value must be known at compile time (cannot use runtime values like <code>DateTime.now()</code>).</p>
<pre><code>const double pi = 3.14159;
const int maxItems = 100;
const String appName = 'MyApp';

// const int year = DateTime.now().year;  ← ERROR (runtime value)

const List&lt;int&gt; primes = [2, 3, 5, 7, 11];
// primes.add(13);  ← ERROR — const list is immutable</code></pre>

<h2 id="difference">final vs const</h2>
<table>
  <thead><tr><th>Feature</th><th>final</th><th>const</th></tr></thead>
  <tbody>
    <tr><td>When value is set</td><td>Runtime</td><td>Compile time</td></tr>
    <tr><td>Can use runtime values</td><td>✅ Yes</td><td>❌ No</td></tr>
    <tr><td>Reassignable</td><td>❌ No</td><td>❌ No</td></tr>
    <tr><td>Object contents mutable</td><td>✅ Yes (list/map)</td><td>❌ No (deeply immutable)</td></tr>
    <tr><td>Memory sharing</td><td>Per instance</td><td>Shared canonical</td></tr>
  </tbody>
</table>

<h2 id="const-objects">const Objects &amp; Lists</h2>
<pre><code>// const list — deeply immutable
const List&lt;String&gt; colors = ['red', 'green', 'blue'];
// colors.add('yellow');  ← ERROR: Unsupported operation

// const map
const Map&lt;String, int&gt; statusCodes = {
  'ok': 200,
  'notFound': 404,
  'error': 500,
};
print(statusCodes['ok']);  // 200</code></pre>

<h2 id="example">Full Example</h2>
<pre><code>void main() {
  const double pi = 3.14159;
  final double radius = 5.0;   // could be user input
  final double area = pi * radius * radius;

  print('Pi: $pi');
  print('Radius: $radius');
  print('Circle area: $${area.toStringAsFixed(2)}');

  const String appVersion = '1.0.0';
  final DateTime launchDate = DateTime.now();

  print('App: $appVersion launched at $launchDate');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Pi: 3.14159
Radius: 5.0
Circle area: 78.54
App: 1.0.0 launched at 2024-01-15 10:30:00.000</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li>Using <code>const</code> with a runtime value like <code>DateTime.now()</code> — use <code>final</code> instead.</li>
  <li>Trying to modify a <code>const</code> list — all elements are immutable.</li>
  <li>Confusing "the reference can't change" (final) with "the object can't change" (const).</li>
</ul>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Which keyword is used for compile-time constants?</strong></p>
  <ul><li>A) final</li><li>B) static</li><li>C) const ✅</li><li>D) readonly</li></ul>
  <p><strong>2. Can you add items to a <code>final List</code>?</strong></p>
  <ul><li>A) No</li><li>B) Yes ✅</li><li>C) Only if the list is empty</li><li>D) Only with special syntax</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Use <code>final</code> for variables set once at runtime, and <code>const</code> for compile-time constants. <code>final</code> allows mutable objects (you can modify a final list's contents), while <code>const</code> makes everything deeply immutable. Prefer <code>const</code> when the value is known at compile time for better performance.</p>
    $dart_14$,
    'Understand the difference between final and const in Dart — when to use each, compile-time vs runtime constants, and deep vs shallow immutability.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,constants,final,const',
    NOW(),
    14,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'User Input in Dart',
    'dart-user-input',
    $dart_15$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#stdin">Reading from stdin</a></li>
    <li><a href="#reading-string">Reading a String</a></li>
    <li><a href="#reading-number">Reading a Number</a></li>
    <li><a href="#null-safety">Null Safety with Input</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="stdin">Reading from stdin</h2>
<p>In Dart, user input from the command line is read using <code>stdin.readLineSync()</code> from the <code>dart:io</code> library. This reads one line of text entered by the user.</p>
<pre><code>import 'dart:io';

void main() {
  stdout.write('Enter your name: ');  // write without newline
  String? name = stdin.readLineSync();
  print('Hello, $name!');
}</code></pre>

<h2 id="reading-string">Reading a String</h2>
<pre><code>import 'dart:io';

void main() {
  print('What is your favourite language?');
  String? lang = stdin.readLineSync();
  print('Great choice: $lang!');
}</code></pre>
<div class="note-box">
  <strong>Note:</strong> <code>stdin.readLineSync()</code> returns <code>String?</code> (nullable String), because it can return <code>null</code> if the input stream is closed.
</div>

<h2 id="reading-number">Reading a Number</h2>
<p>User input is always a String. You must parse it to use it as a number:</p>
<pre><code>import 'dart:io';

void main() {
  stdout.write('Enter your age: ');
  String? input = stdin.readLineSync();
  int age = int.parse(input!);   // parse to int

  if (age >= 18) {
    print('You are an adult.');
  } else {
    print('You are a minor.');
  }
}</code></pre>

<h2 id="null-safety">Null Safety with Input</h2>
<p>Since <code>readLineSync()</code> returns <code>String?</code>, handle null values safely:</p>
<pre><code>import 'dart:io';

void main() {
  stdout.write('Enter a number: ');
  String? input = stdin.readLineSync();

  // Safe approach with null check
  if (input == null || input.isEmpty) {
    print('No input provided.');
    return;
  }

  int? number = int.tryParse(input);
  if (number == null) {
    print('Invalid number!');
  } else {
    print('Double: $${number * 2}');
  }
}</code></pre>

<h2 id="example">Full Example</h2>
<pre><code>import 'dart:io';

void main() {
  // Get student info
  stdout.write('Enter student name: ');
  String name = stdin.readLineSync() ?? 'Unknown';

  stdout.write('Enter score (0-100): ');
  String? scoreInput = stdin.readLineSync();
  double score = double.tryParse(scoreInput ?? '') ?? 0.0;

  // Calculate grade
  String grade;
  if (score >= 90) grade = 'A';
  else if (score >= 80) grade = 'B';
  else if (score >= 70) grade = 'C';
  else if (score >= 60) grade = 'D';
  else grade = 'F';

  print('\n--- Result ---');
  print('Student: $name');
  print('Score: $score');
  print('Grade: $grade');
}</code></pre>
<div class="note-box">
  <strong>Sample Run:</strong>
  <pre>Enter student name: Alice
Enter score (0-100): 88

--- Result ---
Student: Alice
Score: 88.0
Grade: B</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li>Forgetting <code>import 'dart:io';</code> — <code>stdin</code> is not available without it.</li>
  <li>Not parsing the input before arithmetic: <code>int age = stdin.readLineSync();</code> is a type error.</li>
  <li>Using <code>input!</code> (null assertion) without checking if input is null — causes a runtime error.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a program that asks for two numbers and prints their sum, difference, product, and quotient.</li>
    <li>Create a simple "Name Age City" profile input program that prints a formatted summary.</li>
    <li>Add validation: if the user enters a non-number when a number is expected, print an error message.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What library provides stdin in Dart?</strong></p>
  <ul><li>A) dart:core</li><li>B) dart:io ✅</li><li>C) dart:input</li><li>D) dart:console</li></ul>
  <p><strong>2. What type does <code>stdin.readLineSync()</code> return?</strong></p>
  <ul><li>A) String</li><li>B) String? ✅</li><li>C) int</li><li>D) dynamic</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Read user input in Dart using <code>stdin.readLineSync()</code> from <code>dart:io</code>. It returns a nullable <code>String?</code>, so always handle potential null values. Parse to numbers with <code>int.parse()</code> or safe <code>int.tryParse()</code>. Use <code>stdout.write()</code> for prompts without newlines.</p>
    $dart_15$,
    'Learn to read user input in Dart using stdin.readLineSync() from dart:io — handle strings, numbers, and null safety with practical examples.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,user-input',
    NOW(),
    15,
    'PUBLISHED',
    'ACT',
    'system'
);
