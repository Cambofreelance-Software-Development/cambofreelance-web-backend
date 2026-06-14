-- Section 4: Control Flow (Lessons 21–26)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'If Statement in Dart', 'dart-if-statement',
    $dart_21$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is an If Statement?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is an If Statement?</h2>
<p>An <code>if</code> statement executes a block of code only when a condition is <code>true</code>. It is the most fundamental control flow tool in Dart.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>if (condition) {
  // code runs when condition is true
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  int temperature = 35;

  if (temperature > 30) {
    print('It is hot outside!');
  }

  int score = 85;
  if (score >= 60) {
    print('You passed the exam.');
  }

  bool isRaining = false;
  if (!isRaining) {
    print('No need for an umbrella.');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>It is hot outside!
You passed the exam.
No need for an umbrella.</pre></div>
<p>If the condition is <code>false</code>, the block is simply skipped — no error occurs.</p>
<div class="note-box"><strong>📝 Note:</strong> The condition inside <code>if()</code> must evaluate to a <code>bool</code>. Dart does not accept non-boolean expressions like <code>if (1)</code> or <code>if ("text")</code>.</div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Write a program that prints "Teenager" if age is between 13 and 19.</li>
<li>Check if a number is positive and print a message.</li>
<li>Check if a string is empty and print an appropriate message.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What happens if an if condition is false?</strong></p>
<ul><li>A) Error is thrown</li><li>B) The block is skipped ✅</li><li>C) Program stops</li><li>D) null is returned</li></ul>
<p><strong>2. Which type must the if condition evaluate to?</strong></p>
<ul><li>A) int</li><li>B) String</li><li>C) bool ✅</li><li>D) Any type</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>if</code> statement runs a block of code only when its boolean condition is <code>true</code>. If the condition is false, the block is skipped. Always use a proper boolean expression — Dart does not support truthy/falsy non-boolean values in conditions.</p>
    $dart_21$,
    'Understand the if statement in Dart — conditionally execute code blocks based on boolean conditions, with practical real-world examples.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,control-flow', NOW(), 21, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'If Else in Dart', 'dart-if-else',
    $dart_22$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="syntax">Syntax</h2>
<p>An <code>if-else</code> block runs one of two code blocks depending on whether the condition is <code>true</code> or <code>false</code>:</p>
<pre><code>if (condition) {
  // runs when condition is true
} else {
  // runs when condition is false
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  int age = 16;

  if (age >= 18) {
    print('You are an adult.');
  } else {
    print('You are a minor.');
  }

  double temperature = 15.0;
  if (temperature >= 20) {
    print('Wear a t-shirt.');
  } else {
    print('Wear a jacket.');
  }

  int number = 7;
  if (number % 2 == 0) {
    print('$number is even.');
  } else {
    print('$number is odd.');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>You are a minor.
Wear a jacket.
7 is odd.</pre></div>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li>Adding a semicolon after <code>if(condition)</code> — this creates an empty if block.</li>
<li>Using <code>=</code> instead of <code>==</code> in the condition: <code>if (x = 5)</code> is a compile error in Dart.</li>
</ul>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Write a program to check if a year is a leap year (divisible by 4, with special cases for 100 and 400).</li>
<li>Check if a number is positive or negative or zero (hint: use if-else-if from the next lesson for zero).</li>
<li>Check if a student passed an exam (score &gt;= 50).</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. When does the else block execute?</strong></p>
<ul><li>A) Always</li><li>B) When the if condition is true</li><li>C) When the if condition is false ✅</li><li>D) Never</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>if-else</code> statement provides two execution paths. If the condition is <code>true</code>, the if block runs; otherwise the else block runs. Exactly one of the two blocks always executes.</p>
    $dart_22$,
    'Learn the if-else statement in Dart to execute different code blocks based on conditions — with even/odd checks, age verification, and temperature examples.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,control-flow', NOW(), 22, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Else If in Dart', 'dart-else-if',
    $dart_23$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#note">Note</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="syntax">Syntax</h2>
<p>Use <code>else if</code> to test multiple conditions in sequence. Dart evaluates them top-to-bottom and executes the first matching block:</p>
<pre><code>if (condition1) {
  // runs if condition1 is true
} else if (condition2) {
  // runs if condition2 is true
} else if (condition3) {
  // runs if condition3 is true
} else {
  // runs if none of the above are true
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  int score = 72;
  String grade;

  if (score >= 90) {
    grade = 'A';
  } else if (score >= 80) {
    grade = 'B';
  } else if (score >= 70) {
    grade = 'C';
  } else if (score >= 60) {
    grade = 'D';
  } else {
    grade = 'F';
  }

  print('Score: $score → Grade: $grade');

  // BMI classifier
  double bmi = 22.5;
  if (bmi < 18.5) {
    print('Underweight');
  } else if (bmi < 25.0) {
    print('Normal weight');
  } else if (bmi < 30.0) {
    print('Overweight');
  } else {
    print('Obese');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Score: 72 → Grade: C
Normal weight</pre></div>
<div class="note-box"><strong>📝 Note:</strong> Once a matching condition is found, the rest of the chain is skipped — even if later conditions would also be true.</div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Write a program that classifies a number as negative, zero, or positive.</li>
<li>Build a traffic light interpreter: given "red", "yellow", or "green", print the appropriate instruction.</li>
<li>Write a grade calculator for 5 grade levels (A, B, C, D, F).</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How many else-if blocks can you chain?</strong></p>
<ul><li>A) Maximum 3</li><li>B) Maximum 5</li><li>C) Unlimited ✅</li><li>D) Maximum 10</li></ul>
<p><strong>2. If condition1 and condition2 are both true, which block executes?</strong></p>
<ul><li>A) Both blocks</li><li>B) The condition1 block ✅</li><li>C) The condition2 block</li><li>D) Neither</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>else if</code> to chain multiple conditions. Dart evaluates them top-to-bottom and executes only the first matching block. An optional final <code>else</code> handles the case when no condition matches.</p>
    $dart_23$,
    'Master the else-if ladder in Dart to test multiple conditions in sequence — build grade calculators, BMI classifiers, and multi-branch decision logic.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,control-flow', NOW(), 23, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Switch Statement in Dart', 'dart-switch-statement',
    $dart_25$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Switch?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#dart3">Switch Expressions (Dart 3)</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Switch?</h2>
<p>The <code>switch</code> statement matches a value against a list of <code>case</code> patterns and executes the matching block. It is cleaner than a long chain of <code>else if</code> when comparing one variable against many fixed values.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>switch (expression) {
  case value1:
    // code
    break;
  case value2:
    // code
    break;
  default:
    // runs if no case matches
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  String day = 'Monday';

  switch (day) {
    case 'Monday':
      print('Start of the work week!');
      break;
    case 'Friday':
      print('Almost the weekend!');
      break;
    case 'Saturday':
    case 'Sunday':
      print('It is the weekend!');
      break;
    default:
      print('A regular weekday.');
  }

  int statusCode = 404;
  switch (statusCode) {
    case 200: print('OK'); break;
    case 201: print('Created'); break;
    case 404: print('Not Found'); break;
    case 500: print('Server Error'); break;
    default:  print('Unknown status');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Start of the work week!
Not Found</pre></div>
<h2 id="dart3">Switch Expressions (Dart 3)</h2>
<p>Dart 3 introduced switch <em>expressions</em> — a concise inline form that returns a value:</p>
<pre><code>String result = switch (statusCode) {
  200 => 'OK',
  404 => 'Not Found',
  500 => 'Server Error',
  _ => 'Unknown',
};
print(result);</code></pre>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li>Forgetting <code>break</code> — without it, execution falls through to the next case.</li>
<li>Using a non-constant expression in a case value.</li>
</ul>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What happens if no case matches and there is no default?</strong></p>
<ul><li>A) Error</li><li>B) Nothing happens ✅</li><li>C) First case runs</li><li>D) Program crashes</li></ul>
<p><strong>2. In Dart 3, what symbol separates the case pattern from the result in a switch expression?</strong></p>
<ul><li>A) :</li><li>B) -></li><li>C) => ✅</li><li>D) =</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>switch</code> statement matches a value against constant cases and executes the first match. Always use <code>break</code> to prevent fall-through. Dart 3 adds switch expressions for inline, concise value switching using <code>=&gt;</code>.</p>
    $dart_25$,
    'Learn the switch statement and Dart 3 switch expressions — match values against cases, use default, and write clean multi-branch logic.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,control-flow', NOW(), 25, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Ternary Operator in Dart', 'dart-ternary-operator',
    $dart_26$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is the Ternary Operator?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#nested">Nested Ternary</a></li><li><a href="#common-mistakes">Common Mistakes</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is the Ternary Operator?</h2>
<p>The ternary operator is a one-line shorthand for simple <code>if-else</code> statements. It is called "ternary" because it takes three operands: condition, true-value, and false-value.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>condition ? valueIfTrue : valueIfFalse</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  int age = 20;
  String status = age >= 18 ? 'Adult' : 'Minor';
  print(status);  // Adult

  double price = 100.0;
  bool isMember = true;
  double finalPrice = isMember ? price * 0.9 : price;
  print('Final price: \$$${finalPrice.toStringAsFixed(2)}');  // $90.00

  int x = 5;
  print(x % 2 == 0 ? 'Even' : 'Odd');  // Odd

  // In string interpolation
  bool isDarkMode = false;
  String theme = isDarkMode ? 'dark' : 'light';
  print('Theme: $theme');  // Theme: light
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Adult
Final price: $90.00
Odd
Theme: light</pre></div>
<h2 id="nested">Nested Ternary</h2>
<p>You can nest ternary operators, but use sparingly — it hurts readability:</p>
<pre><code>int score = 75;
String grade = score >= 90 ? 'A'
             : score >= 80 ? 'B'
             : score >= 70 ? 'C'
             : 'F';
print(grade);  // C</code></pre>
<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
<li>Using ternary for multi-line logic — use <code>if-else</code> instead.</li>
<li>Over-nesting ternary operators, making code unreadable.</li>
</ul>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does <code>5 > 3 ? 'yes' : 'no'</code> return?</strong></p>
<ul><li>A) 'no'</li><li>B) 'yes' ✅</li><li>C) true</li><li>D) Error</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The ternary operator <code>condition ? trueValue : falseValue</code> is a concise one-line if-else for simple conditions. Use it when both branches return a value. Avoid nesting more than two levels — use <code>if-else if</code> for complex logic instead.</p>
    $dart_26$,
    'Learn the Dart ternary operator — a concise one-line if-else for simple conditions, with member discounts, theme switching, and grade classification examples.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,control-flow', NOW(), 26, 'PUBLISHED', 'ACT', 'system'
);
