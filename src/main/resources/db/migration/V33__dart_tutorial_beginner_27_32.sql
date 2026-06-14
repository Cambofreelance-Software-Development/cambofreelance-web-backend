-- Section 5: Loops (Lessons 27–32)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'For Loop in Dart', 'dart-for-loop',
    $dart_27$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#for-in">For-In Loop</a></li><li><a href="#for-each">forEach()</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="syntax">Syntax</h2>
<p>The <code>for</code> loop repeats a block of code a specific number of times. It has three parts: initializer, condition, and increment.</p>
<pre><code>for (initializer; condition; increment) {
  // code to repeat
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Count 1 to 5
  for (int i = 1; i <= 5; i++) {
    print('Count: $i');
  }

  // Sum of 1 to 100
  int sum = 0;
  for (int i = 1; i <= 100; i++) {
    sum += i;
  }
  print('Sum 1–100: $sum');  // 5050

  // Loop backwards
  for (int i = 5; i >= 1; i--) {
    print('Countdown: $i');
  }
}</code></pre>
<div class="note-box"><strong>Output (partial):</strong><pre>Count: 1
Count: 2
Count: 3
Count: 4
Count: 5
Sum 1–100: 5050
Countdown: 5 ... 1</pre></div>
<h2 id="for-in">For-In Loop</h2>
<p>Iterate over every element in a collection without a counter:</p>
<pre><code>List&lt;String&gt; fruits = ['apple', 'banana', 'cherry'];
for (String fruit in fruits) {
  print(fruit);
}
// apple  banana  cherry</code></pre>
<h2 id="for-each">forEach()</h2>
<p>Collections also have a <code>forEach()</code> method that accepts a function:</p>
<pre><code>fruits.forEach((fruit) => print(fruit.toUpperCase()));
// APPLE  BANANA  CHERRY</code></pre>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Print the multiplication table for 7 (7×1 to 7×10).</li>
<li>Print all even numbers between 1 and 20.</li>
<li>Calculate the factorial of 10 using a for loop.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What are the three parts of a for loop?</strong></p>
<ul><li>A) start, stop, step ✅ (initializer, condition, increment)</li><li>B) init, body, end</li><li>C) begin, check, next</li><li>D) value, range, step</li></ul>
<p><strong>2. Which loop is best for iterating over a List?</strong></p>
<ul><li>A) while</li><li>B) do-while</li><li>C) for-in ✅</li><li>D) repeat</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>for</code> loop repeats a block a set number of times with an initializer, condition, and increment. Use <code>for-in</code> to iterate over collections. Use <code>forEach()</code> for functional-style iteration with a callback.</p>
    $dart_27$,
    'Master the Dart for loop — classic for, for-in collection iteration, and forEach() — with countdown, sum, and multiplication table examples.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,loops', NOW(), 27, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'While Loop in Dart', 'dart-while-loop',
    $dart_28$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#syntax">Syntax</a></li><li><a href="#example">Example</a></li><li><a href="#when">When to Use While</a></li><li><a href="#infinite">Infinite Loop Warning</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="syntax">Syntax</h2>
<p>A <code>while</code> loop repeats as long as its condition remains <code>true</code>. The condition is checked <strong>before</strong> each iteration.</p>
<pre><code>while (condition) {
  // runs while condition is true
}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Count 1 to 5
  int i = 1;
  while (i <= 5) {
    print('i = $i');
    i++;
  }

  // Sum until total exceeds 50
  int total = 0;
  int num = 1;
  while (total <= 50) {
    total += num;
    num++;
  }
  print('Total: $total after adding $num numbers');

  // Simulate user input validation
  int attempts = 0;
  String password = 'dart123';
  String input = 'wrong';
  while (input != password &amp;&amp; attempts < 3) {
    attempts++;
    print('Attempt $attempts: wrong password');
    if (attempts < 3) input = 'wrong'; else input = 'dart123';
  }
  print('Access granted: $${input == password}');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>i = 1 ... i = 5
Total: 56 after adding 11 numbers
Attempt 1: wrong password
Attempt 2: wrong password
Attempt 3: wrong password
Access granted: true</pre></div>
<h2 id="when">When to Use While</h2>
<p>Use <code>while</code> when you do not know the number of iterations in advance — for example, reading input until the user types "quit", or processing data until a condition changes.</p>
<h2 id="infinite">⚠️ Infinite Loop Warning</h2>
<p>Always ensure the condition eventually becomes <code>false</code>, or use <code>break</code> to exit. Forgetting to update the loop variable causes an infinite loop:</p>
<pre><code>// DANGER — infinite loop:
int x = 0;
while (x < 5) {
  print(x);
  // x is never incremented!
}</code></pre>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Print all powers of 2 that are less than 1000 using a while loop.</li>
<li>Use a while loop to find the first number greater than 100 that is divisible by 13.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. When is the condition of a while loop checked?</strong></p>
<ul><li>A) After the loop body</li><li>B) Before the loop body ✅</li><li>C) In the middle</li><li>D) Only once</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>while</code> loop checks its condition before each iteration. Use it when the number of iterations is unknown. Always update the condition variable inside the loop to avoid infinite loops.</p>
    $dart_28$,
    'Learn the Dart while loop — pre-condition iteration for unknown repetition counts, with password validation, sum calculation, and infinite loop warnings.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,loops', NOW(), 28, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Do While Loop in Dart', 'dart-do-while-loop',
    $dart_29$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#syntax">Syntax</a></li><li><a href="#difference">Difference from While</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="syntax">Syntax</h2>
<p>A <code>do-while</code> loop executes the body <strong>at least once</strong>, then checks the condition.</p>
<pre><code>do {
  // runs at least once
} while (condition);</code></pre>
<h2 id="difference">Difference from While</h2>
<table><thead><tr><th>Feature</th><th>while</th><th>do-while</th></tr></thead><tbody>
<tr><td>Condition check</td><td>Before body</td><td>After body</td></tr>
<tr><td>Minimum executions</td><td>0 (may never run)</td><td>1 (always runs once)</td></tr>
<tr><td>Use case</td><td>Unknown iterations</td><td>Must run at least once</td></tr>
</tbody></table>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Basic do-while
  int i = 1;
  do {
    print('i = $i');
    i++;
  } while (i <= 3);

  // Menu system — always show menu at least once
  int choice = 0;
  do {
    print('\n--- Menu ---');
    print('1. View Profile');
    print('2. Settings');
    print('3. Logout');
    choice = 3; // simulate user choosing 3
    print('Selected: $choice');
  } while (choice != 3);

  print('Logged out.');

  // Condition false from start — body still runs once
  int x = 10;
  do {
    print('This runs once even though x > 5');
  } while (x < 5);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>i = 1
i = 2
i = 3
--- Menu ---
...
Selected: 3
Logged out.
This runs once even though x > 5</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How many times will a do-while loop run if its condition is immediately false?</strong></p>
<ul><li>A) 0 times</li><li>B) 1 time ✅</li><li>C) Infinite</li><li>D) Error</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The <code>do-while</code> loop always executes its body at least once before checking the condition. Use it for menus, input validation prompts, or any scenario where the first execution is mandatory.</p>
    $dart_29$,
    'Understand the Dart do-while loop — guaranteed first execution before condition check, ideal for menus and input prompts.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,loops', NOW(), 29, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Break and Continue in Dart', 'dart-break-continue',
    $dart_30$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#break">Break Statement</a></li><li><a href="#continue">Continue Statement</a></li><li><a href="#labeled">Labeled Break/Continue</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="break">Break Statement</h2>
<p><code>break</code> immediately exits the nearest enclosing loop or switch statement:</p>
<pre><code>for (int i = 1; i <= 10; i++) {
  if (i == 5) break;  // stop at 5
  print(i);
}
// Prints: 1 2 3 4</code></pre>
<h2 id="continue">Continue Statement</h2>
<p><code>continue</code> skips the rest of the current iteration and jumps to the next one:</p>
<pre><code>for (int i = 1; i <= 10; i++) {
  if (i % 2 == 0) continue;  // skip even numbers
  print(i);
}
// Prints: 1 3 5 7 9</code></pre>
<h2 id="labeled">Labeled Break/Continue</h2>
<p>Use labels to break out of or continue an outer loop from an inner loop:</p>
<pre><code>outer:
for (int i = 0; i < 3; i++) {
  for (int j = 0; j < 3; j++) {
    if (j == 1) continue outer;  // skip to next i
    print('i=$i, j=$j');
  }
}
// i=0,j=0  i=1,j=0  i=2,j=0</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>void main() {
  // Find first number divisible by both 3 and 7
  for (int n = 1; n <= 200; n++) {
    if (n % 3 == 0 &amp;&amp; n % 7 == 0) {
      print('First divisible by 3 and 7: $n');
      break;
    }
  }

  // Print only odd numbers 1-20
  print('Odd numbers:');
  for (int i = 1; i <= 20; i++) {
    if (i % 2 == 0) continue;
    print(i);
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>First divisible by 3 and 7: 21
Odd numbers: 1 3 5 7 9 11 13 15 17 19</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does break do in a loop?</strong></p>
<ul><li>A) Skips current iteration</li><li>B) Exits the loop entirely ✅</li><li>C) Restarts the loop</li><li>D) Pauses execution</li></ul>
<p><strong>2. What does continue do?</strong></p>
<ul><li>A) Exits the loop</li><li>B) Skips to the next iteration ✅</li><li>C) Restarts from beginning</li><li>D) Breaks out of a switch</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p><code>break</code> exits the loop immediately. <code>continue</code> skips the current iteration and moves to the next. Labels let you target outer loops. Use these to write efficient loop logic that handles special cases.</p>
    $dart_30$,
    'Learn Dart break and continue statements — exit loops early, skip iterations, and use labeled breaks to control nested loops.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,loops', NOW(), 30, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Nested Loops in Dart', 'dart-nested-loops',
    $dart_32$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Nested Loops?</a></li><li><a href="#example">Example</a></li><li><a href="#patterns">Printing Patterns</a></li><li><a href="#performance">Performance Note</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Nested Loops?</h2>
<p>A nested loop is a loop inside another loop. For each iteration of the outer loop, the inner loop completes all its iterations. They are commonly used for working with 2D data like grids, matrices, and patterns.</p>
<h2 id="example">Example</h2>
<pre><code>void main() {
  // Multiplication table (3x3)
  for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
      print('$i x $j = $${i * j}');
    }
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>1 x 1 = 1
1 x 2 = 2
1 x 3 = 3
2 x 1 = 2
...
3 x 3 = 9</pre></div>
<h2 id="patterns">Printing Patterns</h2>
<pre><code>// Right triangle of stars
for (int i = 1; i <= 5; i++) {
  String row = '';
  for (int j = 1; j <= i; j++) {
    row += '* ';
  }
  print(row);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>*
* *
* * *
* * * *
* * * * *</pre></div>
<h2 id="performance">⚠️ Performance Note</h2>
<p>Nested loops multiply execution time. A 3-level loop over N items runs N³ times. Keep nesting to a minimum and consider algorithmic alternatives for large datasets.</p>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Print a 5×5 multiplication table.</li>
<li>Print an inverted triangle pattern using nested loops.</li>
<li>Find all pairs of numbers from 1–10 whose product is greater than 50.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. If the outer loop runs 4 times and the inner loop runs 3 times, how many total iterations occur?</strong></p>
<ul><li>A) 7</li><li>B) 12 ✅</li><li>C) 4</li><li>D) 3</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Nested loops place one loop inside another. For each outer iteration, the inner loop completes fully. Use them for grids, patterns, and 2D data. Be mindful of the O(n²) or higher time complexity they introduce.</p>
    $dart_32$,
    'Learn nested loops in Dart — loops within loops for multiplication tables, star patterns, and 2D data processing, with performance considerations.',
    'TUTORIAL', 'Dart Team', 'dart,programming,beginner,loops', NOW(), 32, 'PUBLISHED', 'ACT', 'system'
);
