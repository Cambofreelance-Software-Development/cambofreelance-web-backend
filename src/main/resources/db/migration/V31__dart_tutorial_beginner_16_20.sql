-- Section 3: Operators (Lessons 16–20)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Arithmetic Operators in Dart',
    'dart-arithmetic-operators',
    $dart_16$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#operators">Arithmetic Operators</a></li>
    <li><a href="#example">Code Example</a></li>
    <li><a href="#output">Output</a></li>
    <li><a href="#explanation">Explanation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="intro">Introduction</h2>
<p>Arithmetic operators allow you to perform mathematical calculations on numeric values. Dart supports all standard arithmetic operators you would expect from a modern programming language.</p>

<h2 id="why">Why It Matters</h2>
<p>Every program that deals with numbers — from calculators to games to financial apps — relies on arithmetic operators. Understanding them is a fundamental building block of programming logic.</p>

<h2 id="operators">Arithmetic Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr></thead>
  <tbody>
    <tr><td><code>+</code></td><td>Addition</td><td><code>10 + 3</code></td><td><code>13</code></td></tr>
    <tr><td><code>-</code></td><td>Subtraction</td><td><code>10 - 3</code></td><td><code>7</code></td></tr>
    <tr><td><code>*</code></td><td>Multiplication</td><td><code>10 * 3</code></td><td><code>30</code></td></tr>
    <tr><td><code>/</code></td><td>Division (double)</td><td><code>10 / 3</code></td><td><code>3.333...</code></td></tr>
    <tr><td><code>~/</code></td><td>Integer division</td><td><code>10 ~/ 3</code></td><td><code>3</code></td></tr>
    <tr><td><code>%</code></td><td>Modulo (remainder)</td><td><code>10 % 3</code></td><td><code>1</code></td></tr>
    <tr><td><code>-expr</code></td><td>Unary minus (negate)</td><td><code>-10</code></td><td><code>-10</code></td></tr>
  </tbody>
</table>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  int a = 20;
  int b = 6;

  print(a + b);   // 26
  print(a - b);   // 14
  print(a * b);   // 120
  print(a / b);   // 3.3333333333333335
  print(a ~/ b);  // 3  (integer division, no decimal)
  print(a % b);   // 2  (remainder of 20 / 6)
  print(-a);      // -20

  // Increment and decrement
  int count = 5;
  count++;
  print(count);   // 6
  count--;
  print(count);   // 5

  // Combined with doubles
  double price = 19.99;
  double tax = price * 0.1;
  print(price + tax); // 21.989
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>26
14
120
3.3333333333333335
3
2
-20
6
5
21.989</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>+</code> adds two numbers together.</li>
  <li><code>-</code> subtracts the right operand from the left.</li>
  <li><code>*</code> multiplies two numbers.</li>
  <li><code>/</code> always returns a <code>double</code>, even if the result is a whole number.</li>
  <li><code>~/</code> returns only the integer part of division (truncates the decimal).</li>
  <li><code>%</code> returns the remainder after integer division — very useful for checking even/odd numbers.</li>
  <li><code>++</code> and <code>--</code> increment or decrement a variable by 1.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>In Dart, <code>10 / 2</code> returns <code>5.0</code> (a double), not <code>5</code> (an int). Use <code>~/</code> if you need an integer result.</li>
  <li>The <code>++</code> operator can be prefix (<code>++x</code>) or postfix (<code>x++</code>). Prefix increments before use; postfix increments after use.</li>
  <li>Dividing by zero with <code>/</code> on doubles produces <code>Infinity</code>; dividing integers with <code>~/</code> throws an exception.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Using <code>/</code> when you need an integer result — use <code>~/</code> instead.</li>
  <li>Forgetting that <code>int + double</code> produces a <code>double</code> in Dart.</li>
  <li>Confusing <code>%</code> (modulo) with percentage — <code>10 % 3</code> is <code>1</code>, not <code>0.1</code>.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a program that takes two integers <code>a = 17</code> and <code>b = 5</code> and prints the result of all six arithmetic operators.</li>
    <li>Check if the number 42 is even using the modulo operator.</li>
    <li>Calculate the area of a rectangle with width <code>8.5</code> and height <code>4.2</code>.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does <code>15 ~/ 4</code> return in Dart?</strong></p>
  <ul><li>A) 3.75</li><li>B) 3 ✅</li><li>C) 4</li><li>D) 3.0</li></ul>
  <p><strong>2. Which operator gives the remainder of division?</strong></p>
  <ul><li>A) /</li><li>B) ~/</li><li>C) % ✅</li><li>D) rem</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart's arithmetic operators cover addition (<code>+</code>), subtraction (<code>-</code>), multiplication (<code>*</code>), division (<code>/</code>), integer division (<code>~/</code>), and modulo (<code>%</code>). Remember that <code>/</code> always returns a <code>double</code>. Use <code>~/</code> when you need a whole-number result and <code>%</code> to find remainders.</p>
    $dart_16$,
    'Learn all arithmetic operators in Dart — addition, subtraction, multiplication, division, integer division, and modulo with real code examples.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,operators',
    NOW(),
    16,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Assignment Operators in Dart',
    'dart-assignment-operators',
    $dart_17$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#basic">Basic Assignment</a></li>
    <li><a href="#compound">Compound Assignment Operators</a></li>
    <li><a href="#example">Code Example</a></li>
    <li><a href="#output">Output</a></li>
    <li><a href="#explanation">Explanation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="intro">Introduction</h2>
<p>Assignment operators assign values to variables. Dart provides a basic assignment operator (<code>=</code>) as well as compound operators that combine arithmetic with assignment, making code shorter and more readable.</p>

<h2 id="why">Why It Matters</h2>
<p>Compound assignment operators reduce repetitive code. Instead of writing <code>x = x + 5</code>, you can write <code>x += 5</code>. This makes your code cleaner and less error-prone.</p>

<h2 id="basic">Basic Assignment</h2>
<pre><code>int x = 10;    // assigns 10 to x
String name = 'Dart';
bool isReady = true;</code></pre>

<h2 id="compound">Compound Assignment Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Equivalent To</th><th>Example (x=10)</th><th>Result</th></tr></thead>
  <tbody>
    <tr><td><code>+=</code></td><td><code>x = x + n</code></td><td><code>x += 5</code></td><td><code>15</code></td></tr>
    <tr><td><code>-=</code></td><td><code>x = x - n</code></td><td><code>x -= 3</code></td><td><code>7</code></td></tr>
    <tr><td><code>*=</code></td><td><code>x = x * n</code></td><td><code>x *= 2</code></td><td><code>20</code></td></tr>
    <tr><td><code>/=</code></td><td><code>x = x / n</code></td><td><code>x /= 4</code></td><td><code>2.5</code></td></tr>
    <tr><td><code>~/=</code></td><td><code>x = x ~/ n</code></td><td><code>x ~/= 3</code></td><td><code>3</code></td></tr>
    <tr><td><code>%=</code></td><td><code>x = x % n</code></td><td><code>x %= 3</code></td><td><code>1</code></td></tr>
    <tr><td><code>??=</code></td><td>assign if null</td><td><code>y ??= 5</code></td><td><code>5</code> (if y is null)</td></tr>
  </tbody>
</table>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  int score = 100;

  score += 10;
  print(score);   // 110

  score -= 20;
  print(score);   // 90

  score *= 2;
  print(score);   // 180

  score ~/= 3;
  print(score);   // 60

  score %= 7;
  print(score);   // 4  (60 % 7 = 4)

  // Null-aware assignment
  int? bonus;
  bonus ??= 50;
  print(bonus);   // 50 (was null, so assigned)

  bonus ??= 100;
  print(bonus);   // 50 (already has value, no change)
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>110
90
180
60
4
50
50</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>+=</code> adds a value to the variable and stores the result back.</li>
  <li><code>-=</code> subtracts a value from the variable.</li>
  <li><code>*=</code> multiplies the variable by a value.</li>
  <li><code>~/=</code> performs integer division and updates the variable.</li>
  <li><code>%=</code> finds the remainder and stores it.</li>
  <li><code>??=</code> only assigns if the variable is currently <code>null</code> — very useful for default values.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>All compound operators are shorthand — they read and write the same variable.</li>
  <li>The <code>??=</code> operator is unique to Dart and works with nullable types.</li>
  <li>Using <code>/=</code> on an <code>int</code> variable will cause a compile error since the result is a <code>double</code>. Declare it as <code>double</code> or use <code>~/=</code>.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Confusing <code>=</code> (assignment) with <code>==</code> (equality comparison).</li>
  <li>Using <code>/=</code> with an <code>int</code> variable — the result is a double, causing a type error.</li>
  <li>Expecting <code>??=</code> to reassign a variable that already has a non-null value.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Start with <code>int points = 50</code>. Add 25, then multiply by 2, then subtract 30. Print the final value.</li>
    <li>Declare a nullable <code>String? username</code>. Use <code>??=</code> to assign a default name if it is null.</li>
    <li>Rewrite <code>x = x * 3 + 5</code> using compound operators (hint: two separate statements).</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does <code>x ??= 10</code> do if x is NOT null?</strong></p>
  <ul><li>A) Sets x to 10</li><li>B) Does nothing ✅</li><li>C) Throws an error</li><li>D) Sets x to null</li></ul>
  <p><strong>2. Which compound operator performs integer division?</strong></p>
  <ul><li>A) /=</li><li>B) //=</li><li>C) ~/= ✅</li><li>D) div=</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Assignment operators in Dart assign values to variables. Compound operators like <code>+=</code>, <code>-=</code>, <code>*=</code>, <code>~/=</code>, and <code>%=</code> combine arithmetic with assignment in a single step. The special <code>??=</code> operator assigns a value only when the variable is <code>null</code>, making it ideal for setting defaults in null-safe code.</p>
    $dart_17$,
    'Master Dart assignment operators — from basic assignment to compound operators like +=, -=, *=, and the null-aware ??= operator.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,operators',
    NOW(),
    17,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Comparison Operators in Dart',
    'dart-comparison-operators',
    $dart_18$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#operators">Comparison Operators</a></li>
    <li><a href="#example">Code Example</a></li>
    <li><a href="#output">Output</a></li>
    <li><a href="#explanation">Explanation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="intro">Introduction</h2>
<p>Comparison operators compare two values and return a <code>bool</code> (<code>true</code> or <code>false</code>). They are the backbone of decision-making in programs — every <code>if</code> statement uses them.</p>

<h2 id="why">Why It Matters</h2>
<p>Without comparison operators, you cannot build logic like "if the user is older than 18" or "if the password is correct." Every conditional statement in Dart depends on a boolean expression formed by comparison operators.</p>

<h2 id="operators">Comparison Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr></thead>
  <tbody>
    <tr><td><code>==</code></td><td>Equal to</td><td><code>5 == 5</code></td><td><code>true</code></td></tr>
    <tr><td><code>!=</code></td><td>Not equal to</td><td><code>5 != 3</code></td><td><code>true</code></td></tr>
    <tr><td><code>&gt;</code></td><td>Greater than</td><td><code>7 &gt; 3</code></td><td><code>true</code></td></tr>
    <tr><td><code>&lt;</code></td><td>Less than</td><td><code>2 &lt; 9</code></td><td><code>true</code></td></tr>
    <tr><td><code>&gt;=</code></td><td>Greater than or equal</td><td><code>5 &gt;= 5</code></td><td><code>true</code></td></tr>
    <tr><td><code>&lt;=</code></td><td>Less than or equal</td><td><code>4 &lt;= 6</code></td><td><code>true</code></td></tr>
  </tbody>
</table>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  int a = 10;
  int b = 20;

  print(a == b);   // false
  print(a != b);   // true
  print(a &gt; b);    // false
  print(a &lt; b);    // true
  print(a &gt;= 10);  // true
  print(b &lt;= 20);  // true

  // Used in conditions
  int age = 17;
  if (age &gt;= 18) {
    print('Access granted');
  } else {
    print('Access denied');
  }

  // Comparing strings
  String lang = 'Dart';
  print(lang == 'Dart');    // true
  print(lang == 'Python');  // false
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>false
true
false
true
true
true
Access denied
true
false</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>==</code> checks value equality. For objects, it checks if both references point to the same value (Dart overrides <code>==</code> for common types like <code>String</code> and <code>int</code>).</li>
  <li><code>!=</code> is the opposite of <code>==</code>.</li>
  <li><code>&gt;</code> and <code>&lt;</code> compare numeric magnitude or lexicographic order for strings.</li>
  <li><code>&gt;=</code> and <code>&lt;=</code> include the boundary value in the comparison.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>Dart uses <code>==</code> for value comparison. Unlike Java, you do NOT need <code>.equals()</code> for strings.</li>
  <li>All comparison operators return <code>bool</code> — they never return numbers.</li>
  <li>You can compare objects of custom classes by overriding the <code>==</code> operator.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Using <code>=</code> instead of <code>==</code> in a condition — this is an assignment, not a comparison.</li>
  <li>Comparing objects with <code>==</code> expecting reference equality — Dart compares by value for built-in types.</li>
  <li>Expecting string comparison to be case-insensitive — <code>'dart' == 'Dart'</code> is <code>false</code>.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a program that checks whether a temperature <code>double temp = 37.5</code> is above, below, or equal to normal body temperature (36.6).</li>
    <li>Compare two strings <code>'apple'</code> and <code>'banana'</code> for equality and inequality.</li>
    <li>Check if the number 100 is both greater than 50 AND less than 200 using two separate comparisons.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does <code>'Dart' == 'dart'</code> return?</strong></p>
  <ul><li>A) true</li><li>B) false ✅</li><li>C) error</li><li>D) null</li></ul>
  <p><strong>2. Which operator checks if two values are NOT equal?</strong></p>
  <ul><li>A) !==</li><li>B) &lt;&gt;</li><li>C) != ✅</li><li>D) not=</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Comparison operators in Dart (<code>==</code>, <code>!=</code>, <code>&gt;</code>, <code>&lt;</code>, <code>&gt;=</code>, <code>&lt;=</code>) compare two values and always return a <code>bool</code>. They are essential for writing conditional logic. Remember that Dart compares strings by value using <code>==</code>, and always use <code>==</code> (double equals) for comparison, not <code>=</code> (single equals) which is assignment.</p>
    $dart_18$,
    'Understand Dart comparison operators — ==, !=, >, <, >=, <= — and how they return boolean values used in conditional logic.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,operators',
    NOW(),
    18,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Logical Operators in Dart',
    'dart-logical-operators',
    $dart_19$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#operators">Logical Operators</a></li>
    <li><a href="#truth-table">Truth Table</a></li>
    <li><a href="#example">Code Example</a></li>
    <li><a href="#output">Output</a></li>
    <li><a href="#explanation">Explanation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="intro">Introduction</h2>
<p>Logical operators combine multiple boolean expressions. Dart provides three logical operators: AND (<code>&amp;&amp;</code>), OR (<code>||</code>), and NOT (<code>!</code>). They are used to build complex conditions.</p>

<h2 id="why">Why It Matters</h2>
<p>Real-world conditions are rarely simple. A user might need to be logged in AND have an admin role, or a form field might be valid if it is not empty OR has a default value. Logical operators let you express these multi-part conditions concisely.</p>

<h2 id="operators">Logical Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Name</th><th>Description</th></tr></thead>
  <tbody>
    <tr><td><code>&amp;&amp;</code></td><td>AND</td><td>Returns <code>true</code> only if BOTH sides are <code>true</code></td></tr>
    <tr><td><code>||</code></td><td>OR</td><td>Returns <code>true</code> if AT LEAST ONE side is <code>true</code></td></tr>
    <tr><td><code>!</code></td><td>NOT</td><td>Reverses the boolean value</td></tr>
  </tbody>
</table>

<h2 id="truth-table">Truth Table</h2>
<table>
  <thead><tr><th>A</th><th>B</th><th>A &amp;&amp; B</th><th>A || B</th><th>!A</th></tr></thead>
  <tbody>
    <tr><td>true</td><td>true</td><td>true</td><td>true</td><td>false</td></tr>
    <tr><td>true</td><td>false</td><td>false</td><td>true</td><td>false</td></tr>
    <tr><td>false</td><td>true</td><td>false</td><td>true</td><td>true</td></tr>
    <tr><td>false</td><td>false</td><td>false</td><td>false</td><td>true</td></tr>
  </tbody>
</table>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  bool isLoggedIn = true;
  bool isAdmin = false;
  bool hasSubscription = true;

  // AND: both must be true
  print(isLoggedIn &amp;&amp; isAdmin);          // false
  print(isLoggedIn &amp;&amp; hasSubscription);  // true

  // OR: at least one must be true
  print(isAdmin || hasSubscription);     // true
  print(isAdmin || false);               // false

  // NOT: reverses the value
  print(!isAdmin);        // true
  print(!isLoggedIn);     // false

  // Complex condition
  int age = 25;
  bool hasId = true;

  if (age &gt;= 18 &amp;&amp; hasId) {
    print('Entry allowed');
  }

  // Short-circuit evaluation
  int x = 0;
  if (x != 0 &amp;&amp; (100 ~/ x) &gt; 5) {
    print('Safe division');
  } else {
    print('Skipped to avoid division by zero');
  }
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>false
true
true
false
true
false
Entry allowed
Skipped to avoid division by zero</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>&amp;&amp;</code> (AND) returns <code>true</code> only when all operands are <code>true</code>. It short-circuits: if the left side is <code>false</code>, the right side is never evaluated.</li>
  <li><code>||</code> (OR) returns <code>true</code> when at least one operand is <code>true</code>. It short-circuits: if the left side is <code>true</code>, the right side is never evaluated.</li>
  <li><code>!</code> (NOT) flips <code>true</code> to <code>false</code> and <code>false</code> to <code>true</code>.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>Short-circuit evaluation is a performance optimization and a safety feature — use it to avoid null dereferences or division by zero.</li>
  <li>Use parentheses to clarify operator precedence in complex expressions: <code>(a &amp;&amp; b) || c</code> vs <code>a &amp;&amp; (b || c)</code>.</li>
  <li>Dart does NOT support bitwise logical operators (<code>&amp;</code>, <code>|</code>) on booleans the same way — always use <code>&amp;&amp;</code> and <code>||</code> for boolean logic.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Using single <code>&amp;</code> or <code>|</code> instead of <code>&amp;&amp;</code> or <code>||</code> for boolean logic — these are bitwise operators.</li>
  <li>Forgetting short-circuit behavior and placing expensive or side-effect operations on the right side of <code>&amp;&amp;</code> when the left may be false.</li>
  <li>Double-negating unnecessarily: <code>!!isAdmin</code> is just <code>isAdmin</code>.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a login check: a user can access the dashboard if they are logged in AND (are an admin OR have a subscription).</li>
    <li>Write a condition that prints "Valid input" if a string is not empty and its length is less than 50.</li>
    <li>Use the NOT operator to toggle a boolean <code>isDarkMode</code> variable.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does <code>true &amp;&amp; false || true</code> evaluate to?</strong></p>
  <ul><li>A) false</li><li>B) true ✅</li><li>C) null</li><li>D) error</li></ul>
  <p><strong>2. Which feature prevents the right side of <code>&amp;&amp;</code> from being evaluated when the left is false?</strong></p>
  <ul><li>A) Lazy loading</li><li>B) Short-circuit evaluation ✅</li><li>C) Null safety</li><li>D) Type inference</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Logical operators (<code>&amp;&amp;</code>, <code>||</code>, <code>!</code>) combine boolean expressions to form complex conditions. AND requires all conditions to be true; OR requires at least one; NOT reverses a value. Dart uses short-circuit evaluation, meaning the second operand is only evaluated when necessary. Use parentheses to control precedence in complex expressions.</p>
    $dart_19$,
    'Learn logical operators in Dart — AND (&&), OR (||), NOT (!) — with truth tables, short-circuit evaluation, and real-world examples.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,operators',
    NOW(),
    19,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Null-Aware Operators in Dart',
    'dart-null-aware-operators',
    $dart_20$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#operators">Null-Aware Operators</a></li>
    <li><a href="#example">Code Example</a></li>
    <li><a href="#output">Output</a></li>
    <li><a href="#explanation">Explanation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="intro">Introduction</h2>
<p>Null-aware operators are special Dart operators that handle <code>null</code> values safely. They prevent the dreaded <code>Null check operator used on a null value</code> runtime error and make null-safe code more elegant.</p>

<h2 id="why">Why It Matters</h2>
<p>Dart has sound null safety, meaning a variable cannot be <code>null</code> unless explicitly declared nullable. Null-aware operators let you work with nullable values gracefully — providing defaults, skipping null calls, and assigning values conditionally.</p>

<h2 id="operators">Null-Aware Operators</h2>
<table>
  <thead><tr><th>Operator</th><th>Name</th><th>Description</th></tr></thead>
  <tbody>
    <tr><td><code>??</code></td><td>If-null (null coalescing)</td><td>Returns the right side if the left is <code>null</code></td></tr>
    <tr><td><code>??=</code></td><td>Null-aware assignment</td><td>Assigns only if the variable is <code>null</code></td></tr>
    <tr><td><code>?.</code></td><td>Null-aware access</td><td>Calls method/property only if not null</td></tr>
    <tr><td><code>?[]</code></td><td>Null-aware index</td><td>Accesses index only if the object is not null</td></tr>
    <tr><td><code>!</code></td><td>Null assertion</td><td>Asserts the value is not null (throws if null)</td></tr>
  </tbody>
</table>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  // ?? operator: provide a default
  String? username;
  String display = username ?? 'Guest';
  print(display);   // Guest

  username = 'Alice';
  display = username ?? 'Guest';
  print(display);   // Alice

  // ??= operator: assign if null
  int? count;
  count ??= 0;
  print(count);   // 0

  count ??= 99;
  print(count);   // 0 (already assigned, no change)

  // ?. operator: safe method call
  String? city;
  print(city?.toUpperCase());   // null (no error!)

  city = 'phnom penh';
  print(city?.toUpperCase());   // PHNOM PENH

  // Chaining null-aware operators
  String? firstName;
  int? len = firstName?.length;
  print(len ?? 0);   // 0
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>Guest
Alice
0
0
null
PHNOM PENH
0</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>??</code> returns the left operand if it is not null; otherwise returns the right operand (the default).</li>
  <li><code>??=</code> assigns the right value to the variable only when it is currently null.</li>
  <li><code>?.</code> calls a method or accesses a property on an object only if the object is not null. If null, the entire expression evaluates to null without throwing.</li>
  <li>These operators work together with Dart''s sound null safety type system.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>The <code>??</code> operator is right-associative: <code>a ?? b ?? c</code> is read as <code>a ?? (b ?? c)</code>.</li>
  <li>Use <code>?.</code> when chaining calls on nullable objects: <code>user?.address?.city</code>.</li>
  <li>Avoid overusing the null assertion operator (<code>!</code>) — if you assert incorrectly, you get a runtime exception.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Forgetting <code>?</code> in the type declaration (<code>String name</code> vs <code>String? name</code>) — non-nullable variables cannot be null.</li>
  <li>Using <code>!</code> (null assertion) carelessly — it throws at runtime if the value is actually null.</li>
  <li>Ignoring the return value of <code>?.</code> — it returns null, so store or handle it accordingly.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Declare a nullable <code>String? email</code>. Use <code>??</code> to print a default message if it is null.</li>
    <li>Declare a nullable <code>List&lt;int&gt;? scores</code>. Use <code>?.</code> to safely call <code>.length</code> and print 0 if null.</li>
    <li>Use <code>??=</code> to initialize a nullable counter to 1 only if it has not been set yet.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does <code>null ?? 'default'</code> return?</strong></p>
  <ul><li>A) null</li><li>B) 'default' ✅</li><li>C) error</li><li>D) false</li></ul>
  <p><strong>2. What happens when you use <code>?.</code> on a null object?</strong></p>
  <ul><li>A) Throws a NullPointerException</li><li>B) Returns null without throwing ✅</li><li>C) Returns false</li><li>D) Skips the entire program</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart''s null-aware operators make null handling safe and expressive. Use <code>??</code> to provide fallback values, <code>??=</code> to assign defaults, and <code>?.</code> to safely chain method calls on nullable objects. These operators are at the heart of Dart''s sound null safety system and help you write robust, crash-resistant code.</p>
    $dart_20$,
    'Master Dart null-aware operators — ??, ??=, and ?. — to handle null values safely and write robust null-safe code.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,operators,null-safety',
    NOW(),
    20,
    'PUBLISHED',
    'ACT',
    'system'
);
