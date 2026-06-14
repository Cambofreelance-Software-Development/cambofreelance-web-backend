-- Section 6: Functions (Lessons 33–38)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Functions in Dart', 'dart-functions',
    $dart_33$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Function?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#return">Return Types</a></li><li><a href="#void">Void Functions</a></li><li><a href="#example">Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Function?</h2>
<p>A function is a reusable block of code that performs a specific task. Functions help you avoid code repetition, improve readability, and make your code easier to test and maintain.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>returnType functionName(parameters) {
  // function body
  return value;  // if returnType is not void
}</code></pre>
<h2 id="return">Return Types</h2>
<pre><code>int add(int a, int b) {
  return a + b;
}

double circleArea(double radius) {
  return 3.14159 * radius * radius;
}

String greet(String name) {
  return 'Hello, $name!';
}

bool isEven(int n) {
  return n % 2 == 0;
}</code></pre>
<h2 id="void">Void Functions</h2>
<p>Functions that do not return a value use <code>void</code> as return type:</p>
<pre><code>void printLine() {
  print('─' * 30);
}

void showInfo(String name, int age) {
  print('Name: $name, Age: $age');
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Call functions
  int result = add(10, 20);
  print('Sum: $result');                   // 30

  double area = circleArea(7.0);
  print('Area: $${area.toStringAsFixed(2)}'); // 153.94

  print(greet('Dart'));                    // Hello, Dart!
  print(isEven(42));                       // true
  printLine();
  showInfo('Alice', 25);
}

int add(int a, int b) => a + b;

double circleArea(double r) => 3.14159 * r * r;

String greet(String name) => 'Hello, $name!';

bool isEven(int n) => n % 2 == 0;

void printLine() => print('─' * 30);

void showInfo(String name, int age) =>
    print('Name: $name, Age: $age');</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Sum: 30
Area: 153.94
Hello, Dart!
true
──────────────────────────────
Name: Alice, Age: 25</pre></div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Write a function <code>max(a, b)</code> that returns the larger of two numbers.</li>
<li>Write a function <code>isPrime(n)</code> that returns true if n is a prime number.</li>
<li>Write a function <code>celsiusToFahrenheit(c)</code> that converts temperature.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What return type does a function that returns nothing use?</strong></p>
<ul><li>A) null</li><li>B) empty</li><li>C) void ✅</li><li>D) none</li></ul>
<p><strong>2. Where can you define functions in Dart?</strong></p>
<ul><li>A) Only inside main()</li><li>B) Only in classes</li><li>C) Top-level or inside classes ✅</li><li>D) Only in separate files</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Functions in Dart are defined with a return type, name, parameters, and body. Use <code>void</code> for functions that return nothing. Functions can be defined at the top level or inside classes. They eliminate repetition and make code reusable.</p>
    $dart_33$,
    'Learn to define and call functions in Dart — return types, void functions, parameters, and how functions make code reusable and maintainable.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,functions', NOW(), 33, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Optional Parameters in Dart', 'dart-optional-parameters',
    $dart_35$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#positional">Optional Positional Parameters</a></li><li><a href="#named">Optional Named Parameters</a></li><li><a href="#defaults">Default Values</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="positional">Optional Positional Parameters</h2>
<p>Wrap parameters in square brackets <code>[ ]</code> to make them optional. Optional positional parameters must come after required ones:</p>
<pre><code>String greet(String name, [String greeting = 'Hello']) {
  return '$greeting, $name!';
}

void main() {
  print(greet('Alice'));            // Hello, Alice!
  print(greet('Bob', 'Welcome'));   // Welcome, Bob!
}</code></pre>
<h2 id="named">Optional Named Parameters</h2>
<p>Wrap parameters in curly braces <code>{ }</code> to make them named. Named parameters can be passed in any order:</p>
<pre><code>void createUser({String name = 'Guest', int age = 0, String role = 'user'}) {
  print('Name: $name | Age: $age | Role: $role');
}

void main() {
  createUser();
  createUser(name: 'Alice', age: 25);
  createUser(role: 'admin', name: 'Bob', age: 30);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Name: Guest | Age: 0 | Role: user
Name: Alice | Age: 25 | Role: user
Name: Bob | Age: 30 | Role: admin</pre></div>
<h2 id="defaults">Default Values</h2>
<p>Both positional and named optional parameters support default values:</p>
<pre><code>double power(double base, [int exponent = 2]) {
  double result = 1;
  for (int i = 0; i < exponent; i++) result *= base;
  return result;
}

print(power(3));     // 9.0  (3^2)
print(power(3, 3));  // 27.0 (3^3)</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Build a formatted message
  print(buildMessage('Error'));
  print(buildMessage('Success', emoji: '✅'));
  print(buildMessage('Warning', emoji: '⚠️', repeat: 3));
}

String buildMessage(String msg, {String emoji = '❌', int repeat = 1}) {
  return ('$emoji $msg ' * repeat).trim();
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>❌ Error
✅ Success
⚠️ Warning ⚠️ Warning ⚠️ Warning</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you define optional positional parameters?</strong></p>
<ul><li>A) (param)</li><li>B) {param}</li><li>C) [param] ✅</li><li>D) &lt;param&gt;</li></ul>
<p><strong>2. Can named parameters be passed in any order?</strong></p>
<ul><li>A) No, must match declaration order</li><li>B) Yes ✅</li><li>C) Only if they have defaults</li><li>D) Only for two parameters</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Optional positional parameters use <code>[ ]</code> and must follow required parameters. Optional named parameters use <code>{ }</code> and can be passed in any order. Both support default values. Use named parameters for clarity when a function has many optional arguments.</p>
    $dart_35$,
    'Learn optional positional and named parameters in Dart with default values — write flexible functions that callers can customize as needed.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,functions', NOW(), 35, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Arrow Functions in Dart', 'dart-arrow-functions',
    $dart_37$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Arrow Functions?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Examples</a></li><li><a href="#lambdas">Arrow Functions as Arguments</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Arrow Functions?</h2>
<p>An arrow function (also called a lambda or fat-arrow function) is a concise syntax for writing single-expression functions. Instead of <code>{ return expression; }</code>, you write <code>=> expression</code>.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>// Full function
int add(int a, int b) {
  return a + b;
}

// Arrow function (equivalent)
int add(int a, int b) => a + b;</code></pre>
<h2 id="example">Examples</h2>
<pre><code>// Math functions
int square(int n) => n * n;
double circleArea(double r) => 3.14159 * r * r;
bool isAdult(int age) => age >= 18;
String greet(String name) => 'Hello, $name!';

void main() {
  print(square(5));                          // 25
  print(circleArea(3).toStringAsFixed(2));   // 28.27
  print(isAdult(20));                        // true
  print(greet('Dart'));                      // Hello, Dart!

  // Arrow function as variable
  var double_ = (int n) => n * 2;
  print(double_(7));                         // 14
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>25
28.27
true
Hello, Dart!
14</pre></div>
<h2 id="lambdas">Arrow Functions as Arguments</h2>
<p>Arrow functions are commonly passed as callbacks to higher-order functions:</p>
<pre><code>List&lt;int&gt; numbers = [1, 2, 3, 4, 5, 6, 7, 8];

// Filter even numbers
var evens = numbers.where((n) => n % 2 == 0).toList();
print(evens);  // [2, 4, 6, 8]

// Map to squares
var squares = numbers.map((n) => n * n).toList();
print(squares); // [1, 4, 9, 16, 25, 36, 49, 64]

// Reduce to sum
var sum = numbers.reduce((a, b) => a + b);
print(sum);     // 36</code></pre>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li>Using arrow syntax for multi-statement functions — <code>=&gt;</code> only works for a single expression.</li>
<li>Forgetting that arrow functions implicitly return the expression value.</li>
</ul>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does <code>int triple(int n) => n * 3;</code> return for n=4?</strong></p>
<ul><li>A) 4</li><li>B) 3</li><li>C) 12 ✅</li><li>D) Error</li></ul>
<p><strong>2. Can an arrow function contain multiple statements?</strong></p>
<ul><li>A) Yes</li><li>B) No ✅</li><li>C) Only with semicolons</li><li>D) Only inside classes</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Arrow functions use <code>=&gt; expression</code> as a shorthand for single-expression functions. They implicitly return the expression value. They are widely used as callbacks in <code>where()</code>, <code>map()</code>, <code>reduce()</code>, and other higher-order functions.</p>
    $dart_37$,
    'Learn Dart arrow functions (fat-arrow syntax) — concise single-expression functions used as callbacks in map(), where(), and reduce().',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,functions', NOW(), 37, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Recursive Functions in Dart', 'dart-recursive-functions',
    $dart_38$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Recursion?</a></li><li><a href="#base-case">Base Case</a></li><li><a href="#example">Examples</a></li><li><a href="#tail">Tail Recursion</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Recursion?</h2>
<p>A recursive function is a function that calls itself to solve a smaller version of the same problem. Each recursive call moves closer to a base case that stops the recursion.</p>
<h2 id="base-case">Base Case</h2>
<p>Every recursive function must have a <strong>base case</strong> — a condition that stops the recursion. Without it, the function calls itself infinitely until a stack overflow error occurs.</p>
<h2 id="example">Examples</h2>
<pre><code>// Factorial: n! = n × (n-1) × ... × 1
int factorial(int n) {
  if (n <= 1) return 1;       // base case
  return n * factorial(n - 1); // recursive call
}

// Fibonacci: F(n) = F(n-1) + F(n-2)
int fibonacci(int n) {
  if (n <= 1) return n;        // base case
  return fibonacci(n - 1) + fibonacci(n - 2);
}

// Sum of digits
int digitSum(int n) {
  if (n < 10) return n;        // base case
  return n % 10 + digitSum(n ~/ 10);
}

void main() {
  print(factorial(5));   // 120
  print(factorial(10));  // 3628800

  for (int i = 0; i < 8; i++) {
    print('fib($i) = $${fibonacci(i)}');
  }

  print(digitSum(12345)); // 15 (1+2+3+4+5)
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>120
3628800
fib(0) = 0  fib(1) = 1  fib(2) = 1  fib(3) = 2
fib(4) = 3  fib(5) = 5  fib(6) = 8  fib(7) = 13
15</pre></div>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li><strong>Missing base case:</strong> causes infinite recursion and a stack overflow.</li>
<li><strong>Wrong base case:</strong> factorial(0) should return 1, not 0.</li>
<li><strong>Exponential recursion:</strong> naive Fibonacci is O(2^n) — use memoization or iteration for large n.</li>
</ul>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Write a recursive function to calculate the power: <code>power(base, exp)</code>.</li>
<li>Write a recursive function to reverse a string.</li>
<li>Write a recursive function to count the number of digits in an integer.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What happens without a base case in a recursive function?</strong></p>
<ul><li>A) Returns 0</li><li>B) Returns null</li><li>C) Stack overflow ✅</li><li>D) Compiles with warning</li></ul>
<p><strong>2. What is factorial(0) by mathematical convention?</strong></p>
<ul><li>A) 0</li><li>B) 1 ✅</li><li>C) undefined</li><li>D) Error</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Recursive functions call themselves with a smaller input until they reach a base case. Every recursive function needs a base case to prevent infinite recursion. Common uses: factorial, Fibonacci, tree traversal, and divide-and-conquer algorithms.</p>
    $dart_38$,
    'Master recursive functions in Dart — factorial, Fibonacci, digit sum — and learn why a base case is essential to prevent stack overflow.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,functions', NOW(), 38, 'PUBLISHED', 'ACT', 'system'
);
