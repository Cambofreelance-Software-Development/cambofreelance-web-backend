-- Section 2: Dart Basics Part 1 (Lessons 6–10)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Dart Syntax',
    'dart-syntax',
    $dart_06$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#overview">Syntax Overview</a></li>
    <li><a href="#statements">Statements &amp; Expressions</a></li>
    <li><a href="#case-sensitivity">Case Sensitivity</a></li>
    <li><a href="#whitespace">Whitespace &amp; Indentation</a></li>
    <li><a href="#keywords">Dart Keywords</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="overview">Syntax Overview</h2>
<p>Dart syntax is similar to C, Java, and JavaScript. Understanding the basic syntax rules helps you write valid Dart code and avoid errors.</p>

<h2 id="statements">Statements &amp; Expressions</h2>
<p>A <strong>statement</strong> is a complete instruction that performs an action. Every statement in Dart ends with a semicolon (<code>;</code>).</p>
<pre><code>int age = 25;           // declaration statement
print(age);             // expression statement
age = age + 1;          // assignment statement</code></pre>
<p>An <strong>expression</strong> is a piece of code that evaluates to a value:</p>
<pre><code>5 + 3          // evaluates to 8
age > 18       // evaluates to true or false
'Hello'        // evaluates to the string "Hello"</code></pre>

<h2 id="case-sensitivity">Case Sensitivity</h2>
<p>Dart is <strong>case-sensitive</strong>. <code>name</code>, <code>Name</code>, and <code>NAME</code> are three different identifiers.</p>
<pre><code>String name = 'Alice';
// String Name = 'Bob';  ← different variable!
print(name);  // Alice</code></pre>

<h2 id="whitespace">Whitespace &amp; Indentation</h2>
<p>Dart ignores extra whitespace and blank lines. However, consistent indentation (2 or 4 spaces) makes code readable. The <code>dart format</code> tool formats code automatically.</p>
<pre><code>// Both are valid but the second is more readable:
void main(){print('Hi');}

void main() {
  print('Hi');
}</code></pre>

<h2 id="keywords">Dart Keywords</h2>
<p>Keywords are reserved words with special meaning. You cannot use them as variable names:</p>
<pre><code>abstract  as        assert    async     await
break     case      catch     class     const
continue  default   do        else      enum
extends   false     final     finally   for
if        import    in        is        late
new       null      return    static    super
switch    this      throw     true      try
var       void      while     with</code></pre>

<h2 id="example">Full Example</h2>
<pre><code>void main() {
  // Variables and types
  String language = 'Dart';
  int version = 3;
  bool isAwesome = true;

  // Output with string interpolation
  print('$language version $version is awesome: $isAwesome');

  // Simple arithmetic
  int a = 10;
  int b = 3;
  print('Sum: $${a + b}');
  print('Product: $${a * b}');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Dart version 3 is awesome: true
Sum: 13
Product: 30</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li>Forgetting the semicolon at the end of a statement.</li>
  <li>Using a keyword as a variable name (e.g., <code>var class = 5;</code>).</li>
  <li>Mixing up capitalization (e.g., calling <code>Print()</code> instead of <code>print()</code>).</li>
</ul>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What ends a statement in Dart?</strong></p>
  <ul><li>A) Newline</li><li>B) Semicolon ✅</li><li>C) Colon</li><li>D) Period</li></ul>
  <p><strong>2. Is Dart case-sensitive?</strong></p>
  <ul><li>A) No</li><li>B) Yes ✅</li><li>C) Only for keywords</li><li>D) Only for class names</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart syntax is C-style with semicolons ending statements. It is case-sensitive. Whitespace is ignored but indentation improves readability. Dart keywords are reserved and cannot be used as identifiers.</p>
    $dart_06$,
    'Learn the fundamental rules of Dart syntax — statements, expressions, case sensitivity, keywords, and proper indentation for clean code.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,syntax',
    NOW(),
    6,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Comments in Dart',
    'dart-comments',
    $dart_07$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#why-comments">Why Use Comments?</a></li>
    <li><a href="#single-line">Single-Line Comments</a></li>
    <li><a href="#multi-line">Multi-Line Comments</a></li>
    <li><a href="#doc-comments">Documentation Comments</a></li>
    <li><a href="#example">Example</a></li>
    <li><a href="#best-practices">Best Practices</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="why-comments">Why Use Comments?</h2>
<p>Comments are notes written in source code that the compiler ignores. They help you and other developers understand what the code does. Good comments explain <em>why</em> code exists, not just what it does.</p>

<h2 id="single-line">Single-Line Comments</h2>
<p>Use <code>//</code> to write a single-line comment. Everything after <code>//</code> on that line is ignored.</p>
<pre><code>void main() {
  // This is a single-line comment
  print('Hello'); // This comment is at the end of a line

  // TODO: Add input validation later
  int score = 95;
}</code></pre>

<h2 id="multi-line">Multi-Line Comments</h2>
<p>Use <code>/* ... */</code> for comments that span multiple lines.</p>
<pre><code>void main() {
  /*
    This is a multi-line comment.
    It can span as many lines as needed.
    Useful for temporarily disabling blocks of code.
  */
  print('Multi-line comments work!');
}</code></pre>

<h2 id="doc-comments">Documentation Comments</h2>
<p>Use <code>///</code> for documentation comments. These are read by the <code>dart doc</code> tool to generate API documentation.</p>
<pre><code>/// Calculates the area of a rectangle.
///
/// [width] is the width of the rectangle.
/// [height] is the height of the rectangle.
/// Returns the area as a double.
double calculateArea(double width, double height) {
  return width * height;
}</code></pre>

<h2 id="example">Example</h2>
<pre><code>void main() {
  // Step 1: Define the items and prices
  double price = 49.99;
  int quantity = 3;

  /*
    Step 2: Calculate total
    Apply 10% discount for quantities > 2
  */
  double total = price * quantity;
  if (quantity > 2) {
    total = total * 0.9; // 10% discount
  }

  print('Total: \$$${total.toStringAsFixed(2)}');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Total: $134.97</pre>
</div>

<h2 id="best-practices">Best Practices</h2>
<ul>
  <li>Write comments that explain <em>why</em>, not <em>what</em> (the code shows what).</li>
  <li>Keep comments up to date when you change the code.</li>
  <li>Use <code>///</code> doc comments for public APIs and functions.</li>
  <li>Use <code>// TODO:</code> to mark incomplete features.</li>
  <li>Avoid commenting obvious code like <code>// increment i by 1</code> before <code>i++</code>.</li>
</ul>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Which symbol starts a single-line comment in Dart?</strong></p>
  <ul><li>A) #</li><li>B) --</li><li>C) // ✅</li><li>D) **</li></ul>
  <p><strong>2. Which comment style is used for API documentation in Dart?</strong></p>
  <ul><li>A) /* */</li><li>B) //!</li><li>C) /// ✅</li><li>D) /** */</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart supports three types of comments: single-line (<code>//</code>), multi-line (<code>/* */</code>), and documentation (<code>///</code>). Comments are ignored by the compiler. Use them to explain your code and generate documentation with <code>dart doc</code>.</p>
    $dart_07$,
    'Learn how to write single-line, multi-line, and documentation comments in Dart to make your code readable and self-documenting.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,comments',
    NOW(),
    7,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Variables in Dart',
    'dart-variables',
    $dart_08$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-is-variable">What is a Variable?</a></li>
    <li><a href="#declaring">Declaring Variables</a></li>
    <li><a href="#var">The var Keyword</a></li>
    <li><a href="#dynamic">The dynamic Type</a></li>
    <li><a href="#naming">Naming Rules</a></li>
    <li><a href="#example">Example</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-is-variable">What is a Variable?</h2>
<p>A variable is a named storage location in memory that holds a value. Think of it as a labelled box where you can store data. In Dart, every variable has a type that determines what kind of data it can store.</p>

<h2 id="declaring">Declaring Variables</h2>
<p>You can declare a variable with an explicit type or using <code>var</code>:</p>
<pre><code>// Explicit type declaration
String name = 'Alice';
int age = 25;
double salary = 55000.50;
bool isEmployed = true;

// Using var (type is inferred)
var city = 'Phnom Penh';    // inferred as String
var count = 10;              // inferred as int</code></pre>
<p>Once a variable is declared with <code>var</code>, its type is fixed at compile time and cannot change.</p>

<h2 id="var">The var Keyword</h2>
<p>The <code>var</code> keyword lets Dart infer the type from the value:</p>
<pre><code>var x = 42;         // int
var pi = 3.14;      // double
var msg = 'Hello';  // String
var flag = true;    // bool

// x = 'text';  ← ERROR: can't assign String to int</code></pre>

<h2 id="dynamic">The dynamic Type</h2>
<p>Use <code>dynamic</code> when a variable can hold values of different types at runtime. Avoid it unless necessary — it disables type checking.</p>
<pre><code>dynamic value = 'Hello';
print(value);       // Hello

value = 42;
print(value);       // 42

value = true;
print(value);       // true</code></pre>

<h2 id="naming">Naming Rules</h2>
<ul>
  <li>Must start with a letter or underscore (<code>_</code>).</li>
  <li>Can contain letters, digits, and underscores.</li>
  <li>Cannot be a Dart keyword.</li>
  <li>Use <strong>camelCase</strong> by convention: <code>firstName</code>, <code>totalPrice</code>.</li>
  <li>Private variables (library-scoped) start with <code>_</code>: <code>_count</code>.</li>
</ul>

<h2 id="example">Example</h2>
<pre><code>void main() {
  String firstName = 'John';
  String lastName = 'Doe';
  int age = 30;
  double height = 5.9;
  bool isDeveloper = true;

  print('Name: $firstName $lastName');
  print('Age: $age');
  print('Height: $height ft');
  print('Developer: $isDeveloper');

  // Reassigning a variable
  age = 31;
  print('Next year age: $age');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Name: John Doe
Age: 30
Height: 5.9 ft
Developer: true
Next year age: 31</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li>Using a variable before declaring it: <code>print(x);</code> before <code>var x = 5;</code> causes an error.</li>
  <li>Assigning a wrong type: <code>int age = 'twenty';</code> is a type error.</li>
  <li>Starting a variable name with a digit: <code>var 1name = 'x';</code> is invalid.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Create variables to store your name, age, city, and GPA.</li>
    <li>Print all values in a formatted message.</li>
    <li>Try using <code>var</code> and let Dart infer the types.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What keyword lets Dart infer the type of a variable?</strong></p>
  <ul><li>A) auto</li><li>B) let</li><li>C) var ✅</li><li>D) type</li></ul>
  <p><strong>2. Which naming convention does Dart use for variables?</strong></p>
  <ul><li>A) snake_case</li><li>B) PascalCase</li><li>C) camelCase ✅</li><li>D) UPPER_CASE</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Variables store data in named memory locations. In Dart, declare variables with an explicit type (<code>String</code>, <code>int</code>) or use <code>var</code> for type inference. Use <code>dynamic</code> only when the type changes at runtime. Follow camelCase naming conventions.</p>
    $dart_08$,
    'Learn how to declare and use variables in Dart using explicit types, the var keyword, and dynamic. Understand naming rules and best practices.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,variables',
    NOW(),
    8,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Data Types in Dart',
    'dart-data-types',
    $dart_09$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#overview">Overview of Data Types</a></li>
    <li><a href="#numbers">Numbers</a></li>
    <li><a href="#strings">Strings</a></li>
    <li><a href="#booleans">Booleans</a></li>
    <li><a href="#lists">Lists</a></li>
    <li><a href="#maps">Maps</a></li>
    <li><a href="#null">Null</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="overview">Overview of Data Types</h2>
<p>Dart is a statically typed language — every value has a type. Dart's type system is sound, meaning it catches type errors at compile time. The core built-in types are:</p>
<table>
  <thead><tr><th>Type</th><th>Description</th><th>Example</th></tr></thead>
  <tbody>
    <tr><td><code>int</code></td><td>Integer (whole numbers)</td><td><code>42</code></td></tr>
    <tr><td><code>double</code></td><td>Floating-point numbers</td><td><code>3.14</code></td></tr>
    <tr><td><code>num</code></td><td>Either int or double</td><td><code>100</code> or <code>3.5</code></td></tr>
    <tr><td><code>String</code></td><td>Text / character sequences</td><td><code>'Hello'</code></td></tr>
    <tr><td><code>bool</code></td><td>True or false</td><td><code>true</code></td></tr>
    <tr><td><code>List</code></td><td>Ordered collection</td><td><code>[1, 2, 3]</code></td></tr>
    <tr><td><code>Set</code></td><td>Unordered unique collection</td><td><code>{1, 2, 3}</code></td></tr>
    <tr><td><code>Map</code></td><td>Key-value pairs</td><td><code>{'a': 1}</code></td></tr>
    <tr><td><code>Null</code></td><td>Absence of value</td><td><code>null</code></td></tr>
  </tbody>
</table>

<h2 id="numbers">Numbers</h2>
<pre><code>int age = 25;
int hexValue = 0xCAFE;      // hexadecimal
double pi = 3.14159;
double scientfic = 1.5e4;   // 15000.0
num score = 98;             // can be int or double</code></pre>

<h2 id="strings">Strings</h2>
<pre><code>String single = 'Hello';
String double_ = "World";
String multi = '''
  This is a
  multi-line string
''';
String interpolated = 'Sum: $${3 + 4}';  // Sum: 7</code></pre>

<h2 id="booleans">Booleans</h2>
<pre><code>bool isLoggedIn = true;
bool hasError = false;
print(isLoggedIn &amp;&amp; !hasError);  // true</code></pre>

<h2 id="lists">Lists</h2>
<pre><code>List&lt;int&gt; numbers = [1, 2, 3, 4, 5];
List&lt;String&gt; fruits = ['apple', 'banana', 'cherry'];
print(fruits[0]);   // apple
print(fruits.length); // 3</code></pre>

<h2 id="maps">Maps</h2>
<pre><code>Map&lt;String, int&gt; scores = {
  'Alice': 95,
  'Bob': 88,
  'Carol': 92,
};
print(scores['Alice']);  // 95</code></pre>

<h2 id="null">Null</h2>
<p>In Dart with null safety, variables cannot be null by default. Add <code>?</code> to allow null:</p>
<pre><code>String? middleName = null;  // nullable
String lastName = 'Smith';   // non-nullable (cannot be null)</code></pre>

<h2 id="example">Full Example</h2>
<pre><code>void main() {
  int age = 20;
  double gpa = 3.85;
  String name = 'Maria';
  bool isGraduated = false;
  List&lt;String&gt; courses = ['Math', 'Physics', 'CS'];
  Map&lt;String, dynamic&gt; profile = {
    'name': name,
    'age': age,
    'gpa': gpa,
  };

  print('Student: $${profile['name']}, Age: $${profile['age']}');
  print('GPA: $gpa, Graduated: $isGraduated');
  print('Courses: $courses');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Student: Maria, Age: 20
GPA: 3.85, Graduated: false
Courses: [Math, Physics, CS]</pre>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Which type stores whole numbers in Dart?</strong></p>
  <ul><li>A) double</li><li>B) float</li><li>C) int ✅</li><li>D) long</li></ul>
  <p><strong>2. How do you make a variable nullable in Dart?</strong></p>
  <ul><li>A) Add ! after the type</li><li>B) Use dynamic</li><li>C) Add ? after the type ✅</li><li>D) Use null keyword before type</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart has a rich set of built-in types including <code>int</code>, <code>double</code>, <code>String</code>, <code>bool</code>, <code>List</code>, <code>Set</code>, <code>Map</code>, and <code>Null</code>. Dart's sound null safety prevents null reference errors by default. Add <code>?</code> to a type to allow null values.</p>
    $dart_09$,
    'Explore all built-in data types in Dart — numbers, strings, booleans, lists, maps, and null safety. Learn when and how to use each type.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,data-types',
    NOW(),
    9,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Strings in Dart',
    'dart-strings',
    $dart_10$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#creating">Creating Strings</a></li>
    <li><a href="#interpolation">String Interpolation</a></li>
    <li><a href="#multiline">Multi-line Strings</a></li>
    <li><a href="#properties">String Properties</a></li>
    <li><a href="#methods">Common String Methods</a></li>
    <li><a href="#escape">Escape Characters</a></li>
    <li><a href="#example">Full Example</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="creating">Creating Strings</h2>
<p>Strings in Dart are sequences of UTF-16 characters. You can use single quotes, double quotes, or triple quotes:</p>
<pre><code>String s1 = 'Single quotes';
String s2 = "Double quotes";
String s3 = 'It\'s a beautiful day';  // escaped apostrophe
String s4 = "She said \"hello\"";     // escaped quotes</code></pre>

<h2 id="interpolation">String Interpolation</h2>
<p>Embed variables and expressions directly into strings using <code>$variable</code> or <code>$${expression}</code>:</p>
<pre><code>String name = 'Dart';
int version = 3;
print('Welcome to $name!');
print('$name version $${version + 0} is great!');
print('2 + 2 = $${2 + 2}');  // 2 + 2 = 4</code></pre>

<h2 id="multiline">Multi-line Strings</h2>
<p>Use triple quotes (<code>'''</code> or <code>"""</code>) for strings that span multiple lines:</p>
<pre><code>String poem = '''
Roses are red,
Violets are blue,
Dart is awesome,
And so are you!
''';
print(poem);</code></pre>

<h2 id="properties">String Properties</h2>
<pre><code>String text = 'Hello, Dart!';
print(text.length);        // 12
print(text.isEmpty);       // false
print(text.isNotEmpty);    // true</code></pre>

<h2 id="methods">Common String Methods</h2>
<table>
  <thead><tr><th>Method</th><th>Description</th><th>Example</th></tr></thead>
  <tbody>
    <tr><td><code>toUpperCase()</code></td><td>All uppercase</td><td><code>'hello'.toUpperCase()</code> → <code>'HELLO'</code></td></tr>
    <tr><td><code>toLowerCase()</code></td><td>All lowercase</td><td><code>'HELLO'.toLowerCase()</code> → <code>'hello'</code></td></tr>
    <tr><td><code>trim()</code></td><td>Remove leading/trailing whitespace</td><td><code>' hi '.trim()</code> → <code>'hi'</code></td></tr>
    <tr><td><code>contains()</code></td><td>Check if string contains substring</td><td><code>'hello'.contains('ell')</code> → <code>true</code></td></tr>
    <tr><td><code>startsWith()</code></td><td>Check prefix</td><td><code>'dart'.startsWith('d')</code> → <code>true</code></td></tr>
    <tr><td><code>endsWith()</code></td><td>Check suffix</td><td><code>'dart'.endsWith('t')</code> → <code>true</code></td></tr>
    <tr><td><code>replaceAll()</code></td><td>Replace all occurrences</td><td><code>'aa'.replaceAll('a','b')</code> → <code>'bb'</code></td></tr>
    <tr><td><code>split()</code></td><td>Split into a List</td><td><code>'a,b,c'.split(',')</code> → <code>['a','b','c']</code></td></tr>
    <tr><td><code>substring()</code></td><td>Extract part of string</td><td><code>'Hello'.substring(1,3)</code> → <code>'el'</code></td></tr>
  </tbody>
</table>

<h2 id="escape">Escape Characters</h2>
<table>
  <thead><tr><th>Escape</th><th>Meaning</th></tr></thead>
  <tbody>
    <tr><td><code>\n</code></td><td>Newline</td></tr>
    <tr><td><code>\t</code></td><td>Tab</td></tr>
    <tr><td><code>\\</code></td><td>Backslash</td></tr>
    <tr><td><code>\'</code></td><td>Single quote</td></tr>
    <tr><td><code>\"</code></td><td>Double quote</td></tr>
    <tr><td><code>\$</code></td><td>Dollar sign (avoid interpolation)</td></tr>
  </tbody>
</table>

<h2 id="example">Full Example</h2>
<pre><code>void main() {
  String fullName = 'John Doe';
  String email = '  john@example.com  ';

  print(fullName.toUpperCase());       // JOHN DOE
  print(email.trim());                 // john@example.com
  print(fullName.contains('John'));    // true
  print(fullName.replaceAll('o', '0')); // J0hn D0e
  print(fullName.split(' '));          // [John, Doe]

  List&lt;String&gt; parts = fullName.split(' ');
  print('First: $${parts[0]}, Last: $${parts[1]}');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>JOHN DOE
john@example.com
true
J0hn D0e
[John, Doe]
First: John, Last: Doe</pre>
</div>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Create a string with your full name. Print it in uppercase and lowercase.</li>
    <li>Check if your name contains the letter 'a'.</li>
    <li>Split a comma-separated list of hobbies: <code>'reading,coding,gaming'</code> and print each hobby.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. How do you embed a variable in a Dart string?</strong></p>
  <ul><li>A) {variable}</li><li>B) $variable ✅</li><li>C) #variable</li><li>D) @variable</li></ul>
  <p><strong>2. Which method splits a string into a list?</strong></p>
  <ul><li>A) divide()</li><li>B) cut()</li><li>C) split() ✅</li><li>D) separate()</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart strings are created with single, double, or triple quotes. String interpolation (<code>$var</code> / <code>$${expr}</code>) embeds values. Key methods include <code>toUpperCase()</code>, <code>trim()</code>, <code>contains()</code>, <code>split()</code>, and <code>substring()</code>. Use escape characters for special characters within strings.</p>
    $dart_10$,
    'Master Dart strings — creating strings, interpolation, multi-line strings, essential methods like split() and trim(), and escape characters.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,strings',
    NOW(),
    10,
    'PUBLISHED',
    'ACT',
    'system'
);
