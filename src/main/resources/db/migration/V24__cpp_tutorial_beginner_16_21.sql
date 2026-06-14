-- C++ Tutorial Series — Beginner Articles 16-21

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Loops',
'cpp-loops',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Loops</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">9 min read</span><span class="lesson-number">Lesson 16 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#while">while Loop</a></li><li><a href="#dowhile">do-while Loop</a></li><li><a href="#for">for Loop</a></li><li><a href="#rangefor">Range-based for</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="while">while Loop</h2>
<p>The <code>while</code> loop repeats a block as long as the condition is true. The condition is checked <strong>before</strong> each iteration.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int i = 1;
    while (i &lt;= 5) {
        cout &lt;&lt; "i = " &lt;&lt; i &lt;&lt; endl;
        i++;
    }
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">i = 1
i = 2
i = 3
i = 4
i = 5</code></pre>

<h2 id="dowhile">do-while Loop</h2>
<p>The <code>do-while</code> loop always runs the block <strong>at least once</strong>, then checks the condition.</p>
<pre><code class="language-cpp">int n;
do {
    cout &lt;&lt; "Enter a positive number: ";
    cin &gt;&gt; n;
} while (n &lt;= 0);
cout &lt;&lt; "You entered: " &lt;&lt; n &lt;&lt; endl;</code></pre>

<h2 id="for">for Loop</h2>
<p>The <code>for</code> loop is perfect when you know how many times to iterate. It bundles init, condition, and update in one line.</p>
<pre><code class="language-cpp">for (int i = 0; i &lt; 5; i++) {
    cout &lt;&lt; "Iteration " &lt;&lt; i &lt;&lt; endl;
}</code></pre>

<pre><code class="language-cpp">// Print multiplication table of 7
for (int i = 1; i &lt;= 10; i++) {
    cout &lt;&lt; "7 x " &lt;&lt; i &lt;&lt; " = " &lt;&lt; 7 * i &lt;&lt; endl;
}</code></pre>

<h2 id="rangefor">Range-Based for Loop (C++11)</h2>
<p>The cleanest way to iterate over a collection:</p>
<pre><code class="language-cpp">int numbers[] = {10, 20, 30, 40, 50};
for (int n : numbers) {
    cout &lt;&lt; n &lt;&lt; " ";
}
// Output: 10 20 30 40 50</code></pre>

<div class="note-box"><strong>Tip:</strong> Use <code>for</code> when the number of iterations is known. Use <code>while</code> when it depends on a condition. Use <code>do-while</code> when you need at least one run.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Use a <code>for</code> loop to print the sum of all numbers from 1 to 100.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int sum = 0;
    for (int i = 1; i &lt;= 100; i++) {
        sum += i;
    }
    cout &lt;&lt; "Sum 1-100 = " &lt;&lt; sum &lt;&lt; endl;  // 5050
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which loop always executes its body at least once?</p><ul><li>A) <code>for</code></li><li>B) <code>while</code></li><li>C) <code>do-while</code></li><li>D) range-based for</li></ul><details><summary>Answer</summary><p><strong>C) <code>do-while</code></strong> — it checks the condition after the body runs.</p></details></li>
<li><p>What are the three parts of a <code>for</code> loop header?</p><ul><li>A) condition, body, update</li><li>B) init, condition, update</li><li>C) start, end, step</li><li>D) declare, loop, exit</li></ul><details><summary>Answer</summary><p><strong>B) init, condition, update</strong> — e.g., <code>for (int i=0; i&lt;5; i++)</code>.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>while</code> — checks condition first, runs 0 or more times.</li>
<li><code>do-while</code> — runs at least once, checks condition after.</li>
<li><code>for</code> — compact loop for a known number of iterations.</li>
<li>Range-based <code>for</code> — cleanly iterates over arrays and collections.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-switch" class="btn btn-secondary">&larr; Previous: Switch</a>
<a href="/tutorials/cpp/cpp-break-continue" class="btn btn-primary">Next: Break &amp; Continue &rarr;</a>
</div>
</div>$$,
'Master all three C++ loop types: while, do-while, and for. Learn the range-based for loop and when to use each loop style.',
'TUTORIAL', 'CamboFreelance', 'c++,loops,for,while,beginner', NOW(), 16, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Break and Continue',
'cpp-break-continue',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Break &amp; Continue</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">5 min read</span><span class="lesson-number">Lesson 17 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#break">break</a></li><li><a href="#continue">continue</a></li><li><a href="#nested">Nested Loops</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="break">break Statement</h2>
<p><code>break</code> immediately exits the nearest enclosing loop or switch.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    for (int i = 1; i &lt;= 10; i++) {
        if (i == 5) break;   // stop when i reaches 5
        cout &lt;&lt; i &lt;&lt; " ";
    }
    cout &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1 2 3 4</code></pre>

<h2 id="continue">continue Statement</h2>
<p><code>continue</code> skips the rest of the current iteration and jumps to the next one.</p>
<pre><code class="language-cpp">for (int i = 1; i &lt;= 10; i++) {
    if (i % 2 == 0) continue;   // skip even numbers
    cout &lt;&lt; i &lt;&lt; " ";
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1 3 5 7 9</code></pre>

<h2 id="nested">break in Nested Loops</h2>
<p><code>break</code> only exits the <strong>innermost</strong> loop it is inside:</p>
<pre><code class="language-cpp">for (int i = 1; i &lt;= 3; i++) {
    for (int j = 1; j &lt;= 3; j++) {
        if (j == 2) break;   // exits inner loop only
        cout &lt;&lt; i &lt;&lt; "," &lt;&lt; j &lt;&lt; "  ";
    }
    cout &lt;&lt; endl;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1,1
2,1
3,1</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Use a <code>while</code> loop and <code>break</code> to keep asking the user for a number until they enter 0. Print each number entered.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int n;
    while (true) {
        cout &lt;&lt; "Enter a number (0 to quit): ";
        cin &gt;&gt; n;
        if (n == 0) break;
        cout &lt;&lt; "You entered: " &lt;&lt; n &lt;&lt; endl;
    }
    cout &lt;&lt; "Goodbye!" &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does <code>continue</code> do in a loop?</p><ul><li>A) Exits the loop</li><li>B) Restarts the program</li><li>C) Skips the current iteration and goes to the next</li><li>D) Pauses execution</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>continue</code> skips the remaining code in the current iteration and begins the next one.</p></details></li>
<li><p>If you have nested loops and use <code>break</code>, which loop exits?</p><ul><li>A) The outermost loop</li><li>B) All loops</li><li>C) The innermost loop</li><li>D) The loop with the <code>break</code> label</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>break</code> exits only the innermost enclosing loop.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>break</code> exits the current loop or switch immediately.</li>
<li><code>continue</code> skips the rest of the current iteration.</li>
<li>In nested loops, <code>break</code> only exits the innermost loop.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-loops" class="btn btn-secondary">&larr; Previous: Loops</a>
<a href="/tutorials/cpp/cpp-arrays" class="btn btn-primary">Next: C++ Arrays &rarr;</a>
</div>
</div>$$,
'Learn how break and continue control loop execution in C++. See practical examples of stopping loops early and skipping iterations.',
'TUTORIAL', 'CamboFreelance', 'c++,break,continue,loops,beginner', NOW(), 17, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Arrays',
'cpp-arrays',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Arrays</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 18 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#declare">Declaring Arrays</a></li><li><a href="#access">Accessing Elements</a></li><li><a href="#loop">Looping Arrays</a></li><li><a href="#multidim">Multi-dimensional</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="declare">What is an Array?</h2>
<p>An <strong>array</strong> stores multiple values of the same type in a single variable. Elements are stored in contiguous memory.</p>
<pre><code class="language-cpp">// Syntax: type name[size] = {values};
int scores[5] = {90, 85, 78, 92, 88};</code></pre>

<h2 id="access">Accessing Elements</h2>
<p>Access by <strong>index</strong>, starting at 0:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int scores[5] = {90, 85, 78, 92, 88};
    cout &lt;&lt; scores[0] &lt;&lt; endl;   // 90 (first)
    cout &lt;&lt; scores[4] &lt;&lt; endl;   // 88 (last)
    scores[2] = 100;              // modify element
    cout &lt;&lt; scores[2] &lt;&lt; endl;   // 100
    return 0;
}</code></pre>

<h2 id="loop">Looping Through an Array</h2>
<pre><code class="language-cpp">int nums[] = {10, 20, 30, 40, 50};
int size = sizeof(nums) / sizeof(nums[0]);   // calculate size

for (int i = 0; i &lt; size; i++) {
    cout &lt;&lt; nums[i] &lt;&lt; " ";
}
// Output: 10 20 30 40 50</code></pre>

<pre><code class="language-cpp">// Range-based for (cleaner)
for (int n : nums) {
    cout &lt;&lt; n &lt;&lt; " ";
}</code></pre>

<h2 id="multidim">Multi-Dimensional Arrays</h2>
<p>A 2D array is like a table of rows and columns:</p>
<pre><code class="language-cpp">int matrix[2][3] = {
    {1, 2, 3},
    {4, 5, 6}
};

for (int i = 0; i &lt; 2; i++) {
    for (int j = 0; j &lt; 3; j++) {
        cout &lt;&lt; matrix[i][j] &lt;&lt; " ";
    }
    cout &lt;&lt; endl;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1 2 3
4 5 6</code></pre>

<div class="note-box"><strong>Note:</strong> C++ does not check array bounds. Accessing <code>arr[10]</code> on a 5-element array is undefined behavior. Use <code>std::vector</code> or <code>std::array</code> for safe alternatives.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Declare an array of 5 temperatures. Find and print the highest and lowest values.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    double temps[] = {36.5, 37.1, 38.0, 35.9, 37.5};
    int n = sizeof(temps) / sizeof(temps[0]);
    double highest = temps[0], lowest = temps[0];

    for (int i = 1; i &lt; n; i++) {
        if (temps[i] &gt; highest) highest = temps[i];
        if (temps[i] &lt; lowest)  lowest  = temps[i];
    }
    cout &lt;&lt; "Highest: " &lt;&lt; highest &lt;&lt; endl;
    cout &lt;&lt; "Lowest: "  &lt;&lt; lowest  &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What is the index of the first element in a C++ array?</p><ul><li>A) 1</li><li>B) -1</li><li>C) 0</li><li>D) It depends on the type</li></ul><details><summary>Answer</summary><p><strong>C) 0</strong> — arrays are zero-indexed in C++.</p></details></li>
<li><p>How do you calculate the size of an array <code>int arr[]</code>?</p><ul><li>A) <code>arr.size()</code></li><li>B) <code>sizeof(arr) / sizeof(arr[0])</code></li><li>C) <code>len(arr)</code></li><li>D) <code>arr.length()</code></li></ul><details><summary>Answer</summary><p><strong>B)</strong> For C-style arrays, <code>sizeof(arr) / sizeof(arr[0])</code> gives the element count.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Arrays store multiple values of the same type, indexed from 0.</li>
<li>Declare: <code>type name[size] = {values};</code></li>
<li>Use <code>sizeof(arr)/sizeof(arr[0])</code> to get array length.</li>
<li>2D arrays use two index levels: <code>arr[row][col]</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-break-continue" class="btn btn-secondary">&larr; Previous: Break &amp; Continue</a>
<a href="/tutorials/cpp/cpp-functions" class="btn btn-primary">Next: C++ Functions &rarr;</a>
</div>
</div>$$,
'Learn C++ arrays: declare, access, and iterate over single and multi-dimensional arrays. Find max/min with practical examples.',
'TUTORIAL', 'CamboFreelance', 'c++,arrays,beginner,data-structures', NOW(), 18, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Functions',
'cpp-functions',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Functions</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">9 min read</span><span class="lesson-number">Lesson 19 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#define">Defining Functions</a></li><li><a href="#params">Parameters</a></li><li><a href="#return">Return Values</a></li><li><a href="#overload">Overloading</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="define">What is a Function?</h2>
<p>A <strong>function</strong> is a reusable block of code that performs a specific task. Functions reduce repetition and make code modular.</p>
<pre><code class="language-cpp">// Declaration (prototype)
returnType functionName(parameters);

// Definition
returnType functionName(parameters) {
    // body
    return value;
}</code></pre>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

void greet() {
    cout &lt;&lt; "Hello from a function!" &lt;&lt; endl;
}

int main() {
    greet();   // call the function
    greet();   // call again
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello from a function!
Hello from a function!</code></pre>

<h2 id="params">Parameters and Arguments</h2>
<pre><code class="language-cpp">void printInfo(string name, int age) {
    cout &lt;&lt; name &lt;&lt; " is " &lt;&lt; age &lt;&lt; " years old." &lt;&lt; endl;
}

int main() {
    printInfo("Alice", 30);
    printInfo("Bob",   25);
    return 0;
}</code></pre>

<h3>Default Parameters</h3>
<pre><code class="language-cpp">void greet(string name = "Guest") {
    cout &lt;&lt; "Hello, " &lt;&lt; name &lt;&lt; "!" &lt;&lt; endl;
}

greet();          // Hello, Guest!
greet("Alice");   // Hello, Alice!</code></pre>

<h2 id="return">Return Values</h2>
<pre><code class="language-cpp">int add(int a, int b) {
    return a + b;
}

double circleArea(double r) {
    return 3.14159 * r * r;
}

int main() {
    cout &lt;&lt; add(5, 3)            &lt;&lt; endl;   // 8
    cout &lt;&lt; circleArea(5.0)      &lt;&lt; endl;   // 78.54
    return 0;
}</code></pre>

<h2 id="overload">Function Overloading</h2>
<p>Multiple functions can share the same name if they have different parameter lists:</p>
<pre><code class="language-cpp">int    multiply(int a, int b)       { return a * b; }
double multiply(double a, double b) { return a * b; }

cout &lt;&lt; multiply(3, 4);       // 12    (int version)
cout &lt;&lt; multiply(2.5, 3.0);   // 7.5  (double version)</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a function <code>isPrime(int n)</code> that returns <code>true</code> if n is prime. Use it to print all primes from 2 to 50.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

bool isPrime(int n) {
    if (n &lt; 2) return false;
    for (int i = 2; i * i &lt;= n; i++) {
        if (n % i == 0) return false;
    }
    return true;
}

int main() {
    for (int i = 2; i &lt;= 50; i++) {
        if (isPrime(i)) cout &lt;&lt; i &lt;&lt; " ";
    }
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What keyword is used for a function that does not return a value?</p><ul><li>A) <code>null</code></li><li>B) <code>none</code></li><li>C) <code>void</code></li><li>D) <code>empty</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>void</code></strong></p></details></li>
<li><p>What is function overloading?</p><ul><li>A) Calling a function too many times</li><li>B) Defining multiple functions with the same name but different parameters</li><li>C) A function calling itself</li><li>D) Running a function in parallel</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Overloading allows the same function name to handle different argument types/counts.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Functions are reusable code blocks with a name, parameters, and return type.</li>
<li>Use <code>void</code> for functions that return nothing.</li>
<li>Default parameters allow optional arguments.</li>
<li>Function overloading enables same name, different signatures.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-arrays" class="btn btn-secondary">&larr; Previous: Arrays</a>
<a href="/tutorials/cpp/cpp-references" class="btn btn-primary">Next: C++ References &rarr;</a>
</div>
</div>$$,
'Learn how to define and call C++ functions with parameters, return values, default arguments, and function overloading.',
'TUTORIAL', 'CamboFreelance', 'c++,functions,overloading,beginner', NOW(), 19, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ References',
'cpp-references',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; References</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 20 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#what">What is a Reference?</a></li><li><a href="#passref">Pass by Reference</a></li><li><a href="#constref">const References</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="what">What is a Reference?</h2>
<p>A <strong>reference</strong> is an alias — another name for an existing variable. Created with <code>&amp;</code>. Both names refer to the <strong>same memory location</strong>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int x = 10;
    int &amp;ref = x;   // ref is an alias for x

    cout &lt;&lt; x   &lt;&lt; endl;   // 10
    cout &lt;&lt; ref &lt;&lt; endl;   // 10

    ref = 99;
    cout &lt;&lt; x   &lt;&lt; endl;   // 99 (x changed through ref)
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">10
10
99</code></pre>

<h2 id="passref">Pass by Reference</h2>
<p>By default, C++ passes arguments by <strong>value</strong> (a copy). Using <code>&amp;</code> in a parameter passes the original variable — changes persist after the function returns.</p>
<pre><code class="language-cpp">void addTen(int &amp;n) {   // reference parameter
    n += 10;
}

int main() {
    int score = 50;
    addTen(score);
    cout &lt;&lt; score;   // 60 — original was modified
}</code></pre>

<pre><code class="language-cpp">// Swap function using references
void swap(int &amp;a, int &amp;b) {
    int temp = a;
    a = b;
    b = temp;
}

int x = 5, y = 10;
swap(x, y);
cout &lt;&lt; x &lt;&lt; " " &lt;&lt; y;   // 10 5</code></pre>

<h2 id="constref">const References</h2>
<p>Use <code>const &amp;</code> to pass large objects efficiently without allowing modification:</p>
<pre><code class="language-cpp">void print(const string &amp;s) {
    cout &lt;&lt; s &lt;&lt; endl;
    // s = "change"; // ERROR — const prevents modification
}

print("Hello World");   // no copy made, no modification allowed</code></pre>

<div class="note-box"><strong>Best Practice:</strong> Pass strings and large objects by <code>const&amp;</code> to avoid expensive copies. Pass small types like <code>int</code> and <code>double</code> by value.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a function <code>square(int &amp;n)</code> that squares the value in place (modifies the original). Test it.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

void square(int &amp;n) {
    n = n * n;
}

int main() {
    int x = 7;
    square(x);
    cout &lt;&lt; "Squared: " &lt;&lt; x &lt;&lt; endl;   // 49
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>A reference variable is:</p><ul><li>A) A copy of another variable</li><li>B) An alias that points to the same memory</li><li>C) A pointer that needs dereferencing</li><li>D) A const variable</li></ul><details><summary>Answer</summary><p><strong>B)</strong> A reference is an alias — it shares the same memory address as the original variable.</p></details></li>
<li><p>How do you declare a reference parameter in a function?</p><ul><li>A) <code>int *n</code></li><li>B) <code>ref int n</code></li><li>C> <code>int &amp;n</code></li><li>D) <code>&amp;int n</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>int &amp;n</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>A reference is an alias for another variable, using <code>&amp;</code>.</li>
<li>Pass by reference allows functions to modify the caller's variables.</li>
<li><code>const &amp;</code> passes efficiently without allowing modification.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-functions" class="btn btn-secondary">&larr; Previous: Functions</a>
<a href="/tutorials/cpp/cpp-pointers" class="btn btn-primary">Next: C++ Pointers &rarr;</a>
</div>
</div>$$,
'Understand C++ references — aliases for variables that enable pass-by-reference, in-place modification, and efficient parameter passing.',
'TUTORIAL', 'CamboFreelance', 'c++,references,pass-by-reference,beginner', NOW(), 20, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Pointers',
'cpp-pointers',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Pointers</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">10 min read</span><span class="lesson-number">Lesson 21 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#address">Memory Addresses</a></li><li><a href="#pointers">Pointer Variables</a></li><li><a href="#deref">Dereferencing</a></li><li><a href="#null">Null Pointers</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="address">Memory Addresses</h2>
<p>Every variable is stored at a memory address. Use the <strong>address-of operator</strong> <code>&amp;</code> to get it:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int x = 42;
    cout &lt;&lt; "Value:   " &lt;&lt; x  &lt;&lt; endl;
    cout &lt;&lt; "Address: " &lt;&lt; &amp;x &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output (addresses vary):</h3>
<pre><code class="language-text">Value:   42
Address: 0x7ffee4b2a8bc</code></pre>

<h2 id="pointers">Pointer Variables</h2>
<p>A <strong>pointer</strong> stores a memory address. Declare with <code>type*</code>:</p>
<pre><code class="language-cpp">int  x = 42;
int* ptr = &amp;x;    // ptr holds the address of x

cout &lt;&lt; ptr;      // prints address (same as &amp;x)
cout &lt;&lt; *ptr;     // prints 42 (value AT the address)</code></pre>

<h2 id="deref">Dereferencing</h2>
<p>Use <code>*</code> to access the value at the address a pointer holds:</p>
<pre><code class="language-cpp">int x = 10;
int* ptr = &amp;x;

*ptr = 99;          // change x through the pointer
cout &lt;&lt; x;          // 99</code></pre>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    double price = 29.99;
    double* p = &amp;price;

    cout &lt;&lt; "Price: $" &lt;&lt; *p  &lt;&lt; endl;   // 29.99
    *p = 19.99;
    cout &lt;&lt; "Sale:  $" &lt;&lt; price &lt;&lt; endl;  // 19.99
    return 0;
}</code></pre>

<h2 id="null">Null Pointers</h2>
<p>A pointer that points to nothing should be set to <code>nullptr</code> (C++11):</p>
<pre><code class="language-cpp">int* p = nullptr;   // safe — not pointing to garbage

if (p == nullptr) {
    cout &lt;&lt; "Pointer is null" &lt;&lt; endl;
}</code></pre>
<div class="note-box"><strong>Warning:</strong> Dereferencing a null or uninitialized pointer causes <strong>undefined behavior</strong> (usually a crash). Always initialize pointers.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create an integer variable, a pointer to it, and use the pointer to double the value. Print the result using both the original variable and the pointer.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int num = 15;
    int* ptr = &amp;num;

    *ptr = *ptr * 2;

    cout &lt;&lt; "Via variable: " &lt;&lt; num  &lt;&lt; endl;   // 30
    cout &lt;&lt; "Via pointer:  " &lt;&lt; *ptr &lt;&lt; endl;   // 30
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does the <code>&amp;</code> operator do when applied to a variable?</p><ul><li>A) Performs bitwise AND</li><li>B) Declares a reference</li><li>C) Returns the memory address of the variable</li><li>D) Dereferences a pointer</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>&amp;x</code> returns the memory address of variable <code>x</code>.</p></details></li>
<li><p>What does <code>*ptr</code> mean when <code>ptr</code> is a pointer?</p><ul><li>A) Multiply by ptr</li><li>B) Declare a pointer</li><li>C) Access the value at the address ptr holds</li><li>D) Get the address of ptr</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>*ptr</code> is the dereference operator — it gives the value stored at the address <code>ptr</code> points to.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>&amp;variable</code> returns the memory address of a variable.</li>
<li><code>type* ptr</code> declares a pointer that stores an address.</li>
<li><code>*ptr</code> dereferences — accesses the value at the stored address.</li>
<li>Always initialize pointers; use <code>nullptr</code> for "no address".</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-references" class="btn btn-secondary">&larr; Previous: References</a>
<a href="/tutorials/cpp/cpp-classes" class="btn btn-primary">Next: C++ Classes &rarr;</a>
</div>
</div>$$,
'Understand C++ pointers: memory addresses, pointer declaration, dereferencing, pointer arithmetic, and safe null pointer usage.',
'TUTORIAL', 'CamboFreelance', 'c++,pointers,memory,beginner', NOW(), 21, 'PUBLISHED', 'ACT', 'SYS'
);
