-- C++ Tutorial Series — Beginner Articles 11-15

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Strings',
'cpp-strings',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Strings</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 11 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#create">Creating Strings</a></li><li><a href="#concat">Concatenation</a></li><li><a href="#methods">String Methods</a></li><li><a href="#access">Accessing Characters</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="create">Creating Strings</h2>
<p>In C++, a <strong>string</strong> is an object of the <code>std::string</code> class. Include <code>&lt;string&gt;</code> to use it (often available via <code>&lt;iostream&gt;</code>).</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    string greeting = "Hello, World!";
    cout &lt;&lt; greeting &lt;&lt; endl;
    cout &lt;&lt; "Length: " &lt;&lt; greeting.length() &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello, World!
Length: 13</code></pre>

<h2 id="concat">String Concatenation</h2>
<p>Use the <code>+</code> operator to join strings:</p>
<pre><code class="language-cpp">string firstName = "John";
string lastName  = "Doe";
string fullName  = firstName + " " + lastName;
cout &lt;&lt; fullName;   // John Doe</code></pre>

<h2 id="methods">Useful String Methods</h2>
<table><thead><tr><th>Method</th><th>Description</th><th>Example</th></tr></thead><tbody>
<tr><td><code>.length()</code></td><td>Returns the number of characters</td><td><code>s.length()</code></td></tr>
<tr><td><code>.size()</code></td><td>Same as length()</td><td><code>s.size()</code></td></tr>
<tr><td><code>.toupper()</code></td><td>Use with <code>&lt;algorithm&gt;</code></td><td><code>transform(...)</code></td></tr>
<tr><td><code>.find()</code></td><td>Finds a substring</td><td><code>s.find("lo")</code></td></tr>
<tr><td><code>.substr()</code></td><td>Extracts a substring</td><td><code>s.substr(0,5)</code></td></tr>
<tr><td><code>.erase()</code></td><td>Removes characters</td><td><code>s.erase(0,3)</code></td></tr>
<tr><td><code>.replace()</code></td><td>Replaces a substring</td><td><code>s.replace(0,5,"Hi")</code></td></tr>
</tbody></table>

<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    string s = "Hello, World!";
    cout &lt;&lt; s.length()      &lt;&lt; endl;    // 13
    cout &lt;&lt; s.substr(7, 5)  &lt;&lt; endl;    // World
    cout &lt;&lt; s.find("World") &lt;&lt; endl;    // 7

    s.replace(7, 5, "C++");
    cout &lt;&lt; s &lt;&lt; endl;                  // Hello, C++!
    return 0;
}</code></pre>

<h2 id="access">Accessing Characters</h2>
<pre><code class="language-cpp">string s = "Hello";
cout &lt;&lt; s[0];       // H
cout &lt;&lt; s[4];       // o
cout &lt;&lt; s.at(1);    // e  (safe, throws if out of range)</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that reads a string from the user and prints: its length, the first character, the last character, and the string reversed using a loop.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    string s;
    cout &lt;&lt; "Enter a word: ";
    cin &gt;&gt; s;
    cout &lt;&lt; "Length: " &lt;&lt; s.length() &lt;&lt; endl;
    cout &lt;&lt; "First: "  &lt;&lt; s[0] &lt;&lt; endl;
    cout &lt;&lt; "Last: "   &lt;&lt; s[s.length()-1] &lt;&lt; endl;
    cout &lt;&lt; "Reversed: ";
    for (int i = s.length()-1; i >= 0; i--) cout &lt;&lt; s[i];
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which method returns the number of characters in a C++ string?</p><ul><li>A) <code>count()</code></li><li>B) <code>size()</code></li><li>C) <code>chars()</code></li><li>D) <code>len()</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>size()</code></strong> (also <code>length()</code> — both work identically).</p></details></li>
<li><p>Which operator concatenates two strings?</p><ul><li>A) <code>&amp;</code></li><li>B) <code>*</code></li><li>C) <code>+</code></li><li>D) <code>.</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>+</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>string</code> is a class from the <code>&lt;string&gt;</code> header.</li>
<li>Concatenate with <code>+</code>; access characters with <code>[i]</code> or <code>.at(i)</code>.</li>
<li>Key methods: <code>length()</code>, <code>substr()</code>, <code>find()</code>, <code>replace()</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-operators" class="btn btn-secondary">&larr; Previous: Operators</a>
<a href="/tutorials/cpp/cpp-math" class="btn btn-primary">Next: C++ Math &rarr;</a>
</div>
</div>$$,
'Learn how to work with strings in C++: create, concatenate, and use essential string methods like find, substr, and replace.',
'TUTORIAL', 'CamboFreelance', 'c++,strings,string-methods,beginner', NOW(), 11, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Math',
'cpp-math',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Math</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 12 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#cmath">cmath Library</a></li><li><a href="#functions">Common Functions</a></li><li><a href="#minmax">min and max</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="cmath">The &lt;cmath&gt; Library</h2>
<p>C++ provides powerful math functions through the <code>&lt;cmath&gt;</code> header. Include it to access square roots, powers, trigonometry, and more.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;cmath&gt;
using namespace std;

int main() {
    cout &lt;&lt; sqrt(25)       &lt;&lt; endl;   // 5
    cout &lt;&lt; pow(2, 10)     &lt;&lt; endl;   // 1024
    cout &lt;&lt; abs(-15)       &lt;&lt; endl;   // 15
    cout &lt;&lt; ceil(4.2)      &lt;&lt; endl;   // 5
    cout &lt;&lt; floor(4.9)     &lt;&lt; endl;   // 4
    cout &lt;&lt; round(4.5)     &lt;&lt; endl;   // 5
    cout &lt;&lt; log(M_E)       &lt;&lt; endl;   // 1 (natural log)
    cout &lt;&lt; log10(1000)    &lt;&lt; endl;   // 3
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">5
1024
15
5
4
5
1
3</code></pre>

<h2 id="functions">Common Math Functions</h2>
<table><thead><tr><th>Function</th><th>Description</th><th>Example</th></tr></thead><tbody>
<tr><td><code>sqrt(x)</code></td><td>Square root</td><td><code>sqrt(9)</code> → 3</td></tr>
<tr><td><code>pow(x, y)</code></td><td>x to the power y</td><td><code>pow(2,8)</code> → 256</td></tr>
<tr><td><code>abs(x)</code></td><td>Absolute value</td><td><code>abs(-7)</code> → 7</td></tr>
<tr><td><code>ceil(x)</code></td><td>Round up</td><td><code>ceil(3.1)</code> → 4</td></tr>
<tr><td><code>floor(x)</code></td><td>Round down</td><td><code>floor(3.9)</code> → 3</td></tr>
<tr><td><code>round(x)</code></td><td>Round to nearest</td><td><code>round(3.5)</code> → 4</td></tr>
<tr><td><code>sin/cos/tan</code></td><td>Trigonometry (radians)</td><td><code>sin(M_PI/2)</code> → 1</td></tr>
</tbody></table>

<h2 id="minmax">max() and min()</h2>
<p>These functions are available without any extra header:</p>
<pre><code class="language-cpp">cout &lt;&lt; max(10, 20);   // 20
cout &lt;&lt; min(10, 20);   // 10
cout &lt;&lt; max(3.5, 2.1); // 3.5</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that reads a number from the user and prints its square root, square (power of 2), and absolute value.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;cmath&gt;
using namespace std;

int main() {
    double n;
    cout &lt;&lt; "Enter a number: "; cin &gt;&gt; n;
    cout &lt;&lt; "Square root: " &lt;&lt; sqrt(n)    &lt;&lt; endl;
    cout &lt;&lt; "Squared: "     &lt;&lt; pow(n, 2)  &lt;&lt; endl;
    cout &lt;&lt; "Absolute: "    &lt;&lt; abs(n)     &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which header provides <code>sqrt()</code> and <code>pow()</code>?</p><ul><li>A) <code>&lt;math&gt;</code></li><li>B) <code>&lt;cmath&gt;</code></li><li>C> <code>&lt;algorithm&gt;</code></li><li>D) <code>&lt;numbers&gt;</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>&lt;cmath&gt;</code></strong></p></details></li>
<li><p>What does <code>ceil(4.1)</code> return?</p><ul><li>A) 4</li><li>B) 4.1</li><li>C) 5</li><li>D) 4.0</li></ul><details><summary>Answer</summary><p><strong>C) 5</strong> — <code>ceil</code> always rounds up to the next whole number.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Include <code>&lt;cmath&gt;</code> for math functions.</li>
<li>Key functions: <code>sqrt</code>, <code>pow</code>, <code>abs</code>, <code>ceil</code>, <code>floor</code>, <code>round</code>.</li>
<li><code>max()</code> and <code>min()</code> work without extra headers.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-strings" class="btn btn-secondary">&larr; Previous: Strings</a>
<a href="/tutorials/cpp/cpp-booleans" class="btn btn-primary">Next: C++ Booleans &rarr;</a>
</div>
</div>$$,
'Explore the C++ cmath library. Learn sqrt, pow, abs, ceil, floor, round, and trigonometric functions with practical examples.',
'TUTORIAL', 'CamboFreelance', 'c++,math,cmath,beginner', NOW(), 12, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Booleans',
'cpp-booleans',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Booleans</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">5 min read</span><span class="lesson-number">Lesson 13 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#bool">Boolean Type</a></li><li><a href="#expressions">Boolean Expressions</a></li><li><a href="#boolalpha">Printing true/false</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="bool">The Boolean Type</h2>
<p>A <strong>boolean</strong> variable can only be <code>true</code> (1) or <code>false</code> (0). Declare with the <code>bool</code> keyword.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    bool isRaining = true;
    bool isSunny   = false;

    cout &lt;&lt; isRaining &lt;&lt; endl;  // 1
    cout &lt;&lt; isSunny   &lt;&lt; endl;  // 0
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1
0</code></pre>

<h2 id="boolalpha">Printing true / false</h2>
<p>By default C++ prints <code>1</code> or <code>0</code>. Use <code>boolalpha</code> to print the words:</p>
<pre><code class="language-cpp">cout &lt;&lt; boolalpha;
cout &lt;&lt; true  &lt;&lt; endl;   // true
cout &lt;&lt; false &lt;&lt; endl;   // false</code></pre>

<h2 id="expressions">Boolean Expressions</h2>
<p>Any comparison expression evaluates to a boolean:</p>
<pre><code class="language-cpp">int x = 10;
cout &lt;&lt; boolalpha;
cout &lt;&lt; (x &gt; 5)   &lt;&lt; endl;   // true
cout &lt;&lt; (x == 10) &lt;&lt; endl;   // true
cout &lt;&lt; (x &lt; 3)   &lt;&lt; endl;   // false
cout &lt;&lt; (x != 10) &lt;&lt; endl;   // false</code></pre>

<pre><code class="language-cpp">// Boolean in conditions
bool hasPermission = true;
bool isLoggedIn    = true;

if (hasPermission &amp;&amp; isLoggedIn) {
    cout &lt;&lt; "Access granted." &lt;&lt; endl;
} else {
    cout &lt;&lt; "Access denied." &lt;&lt; endl;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Access granted.</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Declare two boolean variables <code>isAdult</code> (age &gt;= 18) and <code>hasID</code>. Print whether a person can enter a venue (both must be true).</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int age = 20;
    bool isAdult = (age >= 18);
    bool hasID   = true;
    bool canEnter = isAdult &amp;&amp; hasID;
    cout &lt;&lt; boolalpha;
    cout &lt;&lt; "Can enter: " &lt;&lt; canEnter &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What numeric value does <code>true</code> have in C++?</p><ul><li>A) -1</li><li>B) 0</li><li>C) 1</li><li>D) 2</li></ul><details><summary>Answer</summary><p><strong>C) 1</strong></p></details></li>
<li><p>Which manipulator prints "true" instead of "1"?</p><ul><li>A) <code>truealpha</code></li><li>B) <code>booltext</code></li><li>C) <code>boolalpha</code></li><li>D) <code>printbool</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>boolalpha</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>bool</code> stores only <code>true</code> (1) or <code>false</code> (0).</li>
<li>Use <code>boolalpha</code> to print the words "true" / "false".</li>
<li>Comparison and logical expressions evaluate to booleans.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-math" class="btn btn-secondary">&larr; Previous: Math</a>
<a href="/tutorials/cpp/cpp-conditions" class="btn btn-primary">Next: C++ Conditions &rarr;</a>
</div>
</div>$$,
'Understand the C++ bool data type, boolean expressions, true/false values, and how to use boolalpha to print readable output.',
'TUTORIAL', 'CamboFreelance', 'c++,booleans,bool,beginner', NOW(), 13, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Conditions',
'cpp-conditions',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Conditions</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 14 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#if">if Statement</a></li><li><a href="#else">else / else if</a></li><li><a href="#ternary">Ternary Operator</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="if">The if Statement</h2>
<p>Use <code>if</code> to run code only when a condition is true:</p>
<pre><code class="language-cpp">if (condition) {
    // code runs when condition is true
}</code></pre>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int score = 85;

    if (score >= 90) {
        cout &lt;&lt; "Grade: A" &lt;&lt; endl;
    }
    if (score >= 80) {
        cout &lt;&lt; "Grade: B" &lt;&lt; endl;
    }
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Grade: B</code></pre>

<h2 id="else">else and else if</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int score;
    cout &lt;&lt; "Enter score: "; cin &gt;&gt; score;

    if (score >= 90) {
        cout &lt;&lt; "A — Excellent!" &lt;&lt; endl;
    } else if (score >= 80) {
        cout &lt;&lt; "B — Good" &lt;&lt; endl;
    } else if (score >= 70) {
        cout &lt;&lt; "C — Average" &lt;&lt; endl;
    } else if (score >= 60) {
        cout &lt;&lt; "D — Below Average" &lt;&lt; endl;
    } else {
        cout &lt;&lt; "F — Fail" &lt;&lt; endl;
    }
    return 0;
}</code></pre>

<h2 id="ternary">Ternary Operator</h2>
<p>A shortcut for simple if-else in one line:</p>
<pre><code class="language-cpp">// Syntax: condition ? valueIfTrue : valueIfFalse
int age = 20;
string status = (age >= 18) ? "Adult" : "Minor";
cout &lt;&lt; status;   // Adult</code></pre>

<div class="note-box"><strong>Note:</strong> The ternary operator is great for simple assignments but becomes hard to read when nested. Prefer <code>if-else</code> for complex conditions.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that reads a temperature (in °C) and prints: "Hot" if &gt; 30, "Warm" if 15–30, "Cold" if below 15.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int temp;
    cout &lt;&lt; "Enter temperature (C): "; cin &gt;&gt; temp;

    if (temp &gt; 30) {
        cout &lt;&lt; "Hot" &lt;&lt; endl;
    } else if (temp >= 15) {
        cout &lt;&lt; "Warm" &lt;&lt; endl;
    } else {
        cout &lt;&lt; "Cold" &lt;&lt; endl;
    }
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does the <code>else</code> block execute?</p><ul><li>A) Always</li><li>B) When the <code>if</code> condition is true</li><li>C) When the <code>if</code> condition is false</li><li>D) Never</li></ul><details><summary>Answer</summary><p><strong>C)</strong> The <code>else</code> block runs when the preceding <code>if</code> condition evaluates to false.</p></details></li>
<li><p>What is the ternary operator syntax?</p><ul><li>A) <code>condition : true ? false</code></li><li>B) <code>condition ? true : false</code></li><li>C) <code>if(cond) ? val1 : val2</code></li><li>D) <code>cond &amp;&amp; val1 || val2</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>condition ? true : false</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>if</code> executes a block when the condition is true.</li>
<li><code>else if</code> adds additional conditions.</li>
<li><code>else</code> runs when no conditions matched.</li>
<li>The ternary operator <code>?:</code> is a compact one-line if-else.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-booleans" class="btn btn-secondary">&larr; Previous: Booleans</a>
<a href="/tutorials/cpp/cpp-switch" class="btn btn-primary">Next: C++ Switch &rarr;</a>
</div>
</div>$$,
'Learn to control program flow in C++ with if, else if, else statements, and the ternary operator. Includes grade calculator example.',
'TUTORIAL', 'CamboFreelance', 'c++,conditions,if-else,ternary,beginner', NOW(), 14, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Switch',
'cpp-switch',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Switch</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 15 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#syntax">Switch Syntax</a></li><li><a href="#break">break and default</a></li><li><a href="#fallthrough">Fall-Through</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="syntax">The switch Statement</h2>
<p><code>switch</code> is an alternative to a long chain of <code>if-else if</code>. It matches an expression against a list of constant values (<code>case</code> labels).</p>
<pre><code class="language-cpp">switch (expression) {
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // code if no case matches
}</code></pre>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int day;
    cout &lt;&lt; "Enter day (1-7): "; cin &gt;&gt; day;

    switch (day) {
        case 1: cout &lt;&lt; "Monday";    break;
        case 2: cout &lt;&lt; "Tuesday";   break;
        case 3: cout &lt;&lt; "Wednesday"; break;
        case 4: cout &lt;&lt; "Thursday";  break;
        case 5: cout &lt;&lt; "Friday";    break;
        case 6: cout &lt;&lt; "Saturday";  break;
        case 7: cout &lt;&lt; "Sunday";    break;
        default: cout &lt;&lt; "Invalid day";
    }
    cout &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Sample Run:</h3>
<pre><code class="language-text">Enter day (1-7): 5
Friday</code></pre>

<h2 id="break">break and default</h2>
<ul>
<li><strong><code>break</code></strong> — exits the switch block. Without it, execution "falls through" to the next case.</li>
<li><strong><code>default</code></strong> — optional, runs when no case matches (like <code>else</code>).</li>
</ul>

<h2 id="fallthrough">Fall-Through Example</h2>
<p>Intentional fall-through lets multiple cases share code:</p>
<pre><code class="language-cpp">int month = 4;
switch (month) {
    case 4:
    case 6:
    case 9:
    case 11:
        cout &lt;&lt; "30 days" &lt;&lt; endl;   // April falls through to here
        break;
    case 2:
        cout &lt;&lt; "28 or 29 days" &lt;&lt; endl;
        break;
    default:
        cout &lt;&lt; "31 days" &lt;&lt; endl;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">30 days</code></pre>

<div class="note-box"><strong>Note:</strong> The <code>switch</code> expression must evaluate to an integer or <code>char</code> type. You cannot use <code>string</code> in a switch statement in C++.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a switch-based calculator: read two numbers and an operator (+, -, *, /), then print the result.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    double a, b;
    char op;
    cout &lt;&lt; "Enter: num op num (e.g. 5 + 3): ";
    cin &gt;&gt; a &gt;&gt; op &gt;&gt; b;

    switch (op) {
        case '+': cout &lt;&lt; a + b; break;
        case '-': cout &lt;&lt; a - b; break;
        case '*': cout &lt;&lt; a * b; break;
        case '/':
            if (b != 0) cout &lt;&lt; a / b;
            else cout &lt;&lt; "Error: divide by zero";
            break;
        default: cout &lt;&lt; "Unknown operator";
    }
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does <code>break</code> do inside a <code>switch</code>?</p><ul><li>A) Ends the entire program</li><li>B) Skips the current case</li><li>C) Exits the switch block</li><li>D) Goes to the default case</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>break</code> exits the switch block, preventing fall-through to the next case.</p></details></li>
<li><p>Can you use a <code>string</code> in a C++ switch expression?</p><ul><li>A) Yes, always</li><li>B) Only with C++20</li><li>C) No, only integer and char types</li><li>D) Only with a compiler flag</li></ul><details><summary>Answer</summary><p><strong>C)</strong> C++ switch only works with integral types (int, char, enum).</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>switch</code> matches an integer/char expression against <code>case</code> values.</li>
<li>Always use <code>break</code> to prevent unintended fall-through.</li>
<li><code>default</code> handles values that match no case.</li>
<li>Multiple cases can share code by stacking them without <code>break</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-conditions" class="btn btn-secondary">&larr; Previous: Conditions</a>
<a href="/tutorials/cpp/cpp-loops" class="btn btn-primary">Next: C++ Loops &rarr;</a>
</div>
</div>$$,
'Learn the C++ switch statement: match values with case labels, use break, default, and intentional fall-through with practical examples.',
'TUTORIAL', 'CamboFreelance', 'c++,switch,conditions,beginner', NOW(), 15, 'PUBLISHED', 'ACT', 'SYS'
);
