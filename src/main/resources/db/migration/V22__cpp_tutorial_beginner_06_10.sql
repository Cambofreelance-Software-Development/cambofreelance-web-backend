-- C++ Tutorial Series — Beginner Articles 6-10

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Variables',
'cpp-variables',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Variables</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 6 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#declare">Declaring Variables</a></li><li><a href="#types">Variable Types</a></li><li><a href="#naming">Naming Rules</a></li><li><a href="#multiple">Multiple Variables</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="declare">What is a Variable?</h2>
<p>A <strong>variable</strong> is a named storage location in memory that holds a value. You must declare the variable type before using it.</p>
<h2 id="syntax">Syntax</h2>
<pre><code class="language-cpp">type variableName = value;</code></pre>
<h2 id="example">Example</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int age = 25;
    double price = 9.99;
    char grade = 'A';
    bool isStudent = true;
    string name = "Alex";

    cout &lt;&lt; "Name: " &lt;&lt; name &lt;&lt; endl;
    cout &lt;&lt; "Age: " &lt;&lt; age &lt;&lt; endl;
    cout &lt;&lt; "Price: " &lt;&lt; price &lt;&lt; endl;
    cout &lt;&lt; "Grade: " &lt;&lt; grade &lt;&lt; endl;
    cout &lt;&lt; "Student: " &lt;&lt; isStudent &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Name: Alex
Age: 25
Price: 9.99
Grade: A
Student: 1</code></pre>

<h2 id="types">Common Variable Types</h2>
<table><thead><tr><th>Type</th><th>Description</th><th>Example</th></tr></thead><tbody>
<tr><td><code>int</code></td><td>Integer numbers</td><td><code>int x = 10;</code></td></tr>
<tr><td><code>double</code></td><td>Decimal numbers (high precision)</td><td><code>double pi = 3.14;</code></td></tr>
<tr><td><code>float</code></td><td>Decimal numbers (less precision)</td><td><code>float f = 3.14f;</code></td></tr>
<tr><td><code>char</code></td><td>Single character</td><td><code>char c = 'A';</code></td></tr>
<tr><td><code>bool</code></td><td>True or false</td><td><code>bool b = true;</code></td></tr>
<tr><td><code>string</code></td><td>Text (sequence of characters)</td><td><code>string s = "Hi";</code></td></tr>
</tbody></table>

<h2 id="naming">Naming Rules</h2>
<ul>
<li>Names can contain letters, digits, and underscores.</li>
<li>Must start with a letter or underscore (not a digit).</li>
<li>Case-sensitive: <code>age</code> and <code>Age</code> are different variables.</li>
<li>Cannot use C++ reserved keywords (e.g., <code>int</code>, <code>return</code>).</li>
</ul>
<pre><code class="language-cpp">int myAge = 30;       // valid
int _count = 0;       // valid
int 1name = 5;        // INVALID — starts with digit
int int = 10;         // INVALID — reserved keyword</code></pre>

<h2 id="multiple">Declare Multiple Variables</h2>
<pre><code class="language-cpp">int x = 1, y = 2, z = 3;
cout &lt;&lt; x + y + z;    // Output: 6</code></pre>

<div class="note-box"><strong>Note:</strong> In C++, <code>string</code> requires the <code>#include &lt;string&gt;</code> header, but it is usually included automatically via <code>#include &lt;iostream&gt;</code>.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Declare variables to store: a product name (string), price (double), quantity (int), and whether it is in stock (bool). Print all four values.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    string productName = "Laptop";
    double price = 999.99;
    int quantity = 10;
    bool inStock = true;

    cout &lt;&lt; "Product: " &lt;&lt; productName &lt;&lt; endl;
    cout &lt;&lt; "Price: $" &lt;&lt; price &lt;&lt; endl;
    cout &lt;&lt; "Quantity: " &lt;&lt; quantity &lt;&lt; endl;
    cout &lt;&lt; "In Stock: " &lt;&lt; inStock &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which variable name is valid in C++?</p><ul><li>A) <code>2score</code></li><li>B) <code>my-score</code></li><li>C) <code>my_score</code></li><li>D) <code>return</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>my_score</code></strong> — underscores are allowed; hyphens are not; names cannot start with digits; reserved words cannot be used.</p></details></li>
<li><p>What type stores a single character in C++?</p><ul><li>A) <code>string</code></li><li>B) <code>int</code></li><li>C) <code>char</code></li><li>D) <code>letter</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>char</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Variables are declared as <code>type name = value;</code></li>
<li>Common types: <code>int</code>, <code>double</code>, <code>char</code>, <code>bool</code>, <code>string</code>.</li>
<li>Names are case-sensitive and cannot start with a digit.</li>
<li>Multiple variables of the same type can be declared on one line.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-comments" class="btn btn-secondary">&larr; Previous: Comments</a>
<a href="/tutorials/cpp/cpp-data-types" class="btn btn-primary">Next: C++ Data Types &rarr;</a>
</div>
</div>$$,
'Learn how to declare and use variables in C++. Understand variable types, naming rules, and how to store different kinds of data.',
'TUTORIAL', 'CamboFreelance', 'c++,variables,data-types,beginner', NOW(), 6, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Data Types',
'cpp-data-types',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Data Types</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 7 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#basic">Basic Types</a></li><li><a href="#sizes">Type Sizes</a></li><li><a href="#overflow">Overflow</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="basic">C++ Basic Data Types</h2>
<p>C++ is a <strong>statically typed</strong> language, meaning every variable must have a declared type. The type determines what kind of data is stored and how much memory it uses.</p>

<h2 id="integers">Integer Types</h2>
<table><thead><tr><th>Type</th><th>Size</th><th>Range</th></tr></thead><tbody>
<tr><td><code>short</code></td><td>2 bytes</td><td>-32,768 to 32,767</td></tr>
<tr><td><code>int</code></td><td>4 bytes</td><td>-2,147,483,648 to 2,147,483,647</td></tr>
<tr><td><code>long</code></td><td>4–8 bytes</td><td>at least 32-bit range</td></tr>
<tr><td><code>long long</code></td><td>8 bytes</td><td>±9.2 × 10¹⁸</td></tr>
</tbody></table>

<h2 id="floats">Floating-Point Types</h2>
<table><thead><tr><th>Type</th><th>Size</th><th>Precision</th></tr></thead><tbody>
<tr><td><code>float</code></td><td>4 bytes</td><td>~6–7 decimal digits</td></tr>
<tr><td><code>double</code></td><td>8 bytes</td><td>~15–16 decimal digits</td></tr>
<tr><td><code>long double</code></td><td>8–16 bytes</td><td>even higher precision</td></tr>
</tbody></table>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int    a = 1000000;
    double b = 3.14159265358979;
    float  c = 3.14f;           // Use 'f' suffix for float literals
    char   d = 'Z';
    bool   e = false;

    cout &lt;&lt; "int:    " &lt;&lt; a &lt;&lt; endl;
    cout &lt;&lt; "double: " &lt;&lt; b &lt;&lt; endl;
    cout &lt;&lt; "float:  " &lt;&lt; c &lt;&lt; endl;
    cout &lt;&lt; "char:   " &lt;&lt; d &lt;&lt; endl;
    cout &lt;&lt; "bool:   " &lt;&lt; e &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">int:    1000000
double: 3.14159265358979
float:  3.14
char:   Z
bool:   0</code></pre>

<h2 id="sizes">Checking Type Sizes with sizeof</h2>
<pre><code class="language-cpp">cout &lt;&lt; "int size:    " &lt;&lt; sizeof(int)    &lt;&lt; " bytes" &lt;&lt; endl;
cout &lt;&lt; "double size: " &lt;&lt; sizeof(double) &lt;&lt; " bytes" &lt;&lt; endl;
cout &lt;&lt; "char size:   " &lt;&lt; sizeof(char)   &lt;&lt; " bytes" &lt;&lt; endl;</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">int size:    4 bytes
double size: 8 bytes
char size:   1 bytes</code></pre>

<h2 id="overflow">Integer Overflow</h2>
<p>If you store a value too large for its type, <strong>overflow</strong> occurs — the value wraps around unexpectedly:</p>
<pre><code class="language-cpp">short x = 32767;
x++;
cout &lt;&lt; x;   // Output: -32768 (overflow!)</code></pre>
<div class="note-box"><strong>Warning:</strong> Always choose a data type large enough to hold the values you expect.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Declare variables for: the population of Earth (use <code>long long</code>), the speed of light in m/s (use <code>double</code>), the first letter of the alphabet (use <code>char</code>). Print all three.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    long long earthPopulation = 8000000000LL;
    double speedOfLight = 299792458.0;
    char firstLetter = 'A';

    cout &lt;&lt; "Population: " &lt;&lt; earthPopulation &lt;&lt; endl;
    cout &lt;&lt; "Speed of Light: " &lt;&lt; speedOfLight &lt;&lt; endl;
    cout &lt;&lt; "First Letter: " &lt;&lt; firstLetter &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which type uses 8 bytes and stores high-precision decimals?</p><ul><li>A) <code>float</code></li><li>B) <code>int</code></li><li>C) <code>double</code></li><li>D) <code>char</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>double</code></strong> — it uses 8 bytes and provides ~15 decimal digits of precision.</p></details></li>
<li><p>What operator returns the size in bytes of a data type?</p><ul><li>A) <code>size()</code></li><li>B) <code>sizeof</code></li><li>C) <code>length()</code></li><li>D) <code>bytes()</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>sizeof</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>C++ has several integer types: <code>short</code>, <code>int</code>, <code>long</code>, <code>long long</code>.</li>
<li>Floating-point types: <code>float</code>, <code>double</code>, <code>long double</code>.</li>
<li><code>sizeof(type)</code> returns the number of bytes used.</li>
<li>Use <code>long long</code> for very large integers to avoid overflow.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-variables" class="btn btn-secondary">&larr; Previous: Variables</a>
<a href="/tutorials/cpp/cpp-constants" class="btn btn-primary">Next: C++ Constants &rarr;</a>
</div>
</div>$$,
'Explore all C++ data types including integers, floats, chars, and booleans. Learn sizes, ranges, and how to avoid overflow.',
'TUTORIAL', 'CamboFreelance', 'c++,data-types,int,double,beginner', NOW(), 7, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Constants',
'cpp-constants',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Constants</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">5 min read</span><span class="lesson-number">Lesson 8 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#const">const Keyword</a></li><li><a href="#define">#define Macro</a></li><li><a href="#constexpr">constexpr</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="const">The const Keyword</h2>
<p>Use <code>const</code> to declare a variable whose value <strong>cannot be changed</strong> after initialization. Constants make code safer and more readable.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    const double PI = 3.14159265;
    const int MAX_SIZE = 100;
    const string GREETING = "Hello";

    cout &lt;&lt; "PI = " &lt;&lt; PI &lt;&lt; endl;
    cout &lt;&lt; "Max Size = " &lt;&lt; MAX_SIZE &lt;&lt; endl;
    cout &lt;&lt; GREETING &lt;&lt; endl;

    // PI = 3.0; // ERROR: cannot assign to const variable
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">PI = 3.14159265
Max Size = 100
Hello</code></pre>

<h2 id="define">#define Preprocessor Macro</h2>
<p>Before C++11, <code>#define</code> was the common way to create constants. It is a preprocessor directive — the compiler replaces every use of the name with the value before compiling.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#define MAX_SCORE 100
#define GAME_NAME "Space Shooter"
using namespace std;

int main() {
    cout &lt;&lt; "Game: " &lt;&lt; GAME_NAME &lt;&lt; endl;
    cout &lt;&lt; "Max Score: " &lt;&lt; MAX_SCORE &lt;&lt; endl;
    return 0;
}</code></pre>
<div class="note-box"><strong>Prefer <code>const</code> over <code>#define</code>:</strong> <code>const</code> is type-safe, respects scope, and shows up in debuggers. Use <code>#define</code> only when necessary for legacy code.</div>

<h2 id="constexpr">constexpr (C++11)</h2>
<p><code>constexpr</code> means the value is computed at <strong>compile time</strong>, making programs faster:</p>
<pre><code class="language-cpp">constexpr int SQUARE(int x) { return x * x; }

int main() {
    constexpr int result = SQUARE(5);   // Computed at compile time
    cout &lt;&lt; result;   // Output: 25
}</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Declare constants for: the number of days in a week (int), the value of gravity (double = 9.8), and the name of your app (string). Print all three.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    const int DAYS_PER_WEEK = 7;
    const double GRAVITY = 9.8;
    const string APP_NAME = "MyApp";

    cout &lt;&lt; "Days/Week: " &lt;&lt; DAYS_PER_WEEK &lt;&lt; endl;
    cout &lt;&lt; "Gravity: " &lt;&lt; GRAVITY &lt;&lt; endl;
    cout &lt;&lt; "App: " &lt;&lt; APP_NAME &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What happens if you try to change a <code>const</code> variable?</p><ul><li>A) The value changes silently</li><li>B) A compile-time error occurs</li><li>C) A runtime error occurs</li><li>D) Nothing happens</li></ul><details><summary>Answer</summary><p><strong>B)</strong> The compiler will reject the assignment and produce an error.</p></details></li>
<li><p>Which is type-safe: <code>const</code> or <code>#define</code>?</p><ul><li>A) <code>#define</code></li><li>B) Both are equally safe</li><li>C) <code>const</code></li><li>D) Neither</li></ul><details><summary>Answer</summary><p><strong>C) <code>const</code></strong> — it has a type, respects scope, and is visible to the debugger.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>const</code> declares a variable that cannot be modified after initialization.</li>
<li><code>#define</code> creates preprocessor text-replacement macros (legacy approach).</li>
<li><code>constexpr</code> evaluates the value at compile time for performance.</li>
<li>Prefer <code>const</code> or <code>constexpr</code> over <code>#define</code> in modern C++.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-data-types" class="btn btn-secondary">&larr; Previous: Data Types</a>
<a href="/tutorials/cpp/cpp-user-input" class="btn btn-primary">Next: C++ User Input &rarr;</a>
</div>
</div>$$,
'Learn how to declare constants in C++ using const, #define, and constexpr. Understand when and why to use each approach.',
'TUTORIAL', 'CamboFreelance', 'c++,constants,const,beginner', NOW(), 8, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ User Input',
'cpp-user-input',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; User Input</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 9 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#cin">cin — Basic Input</a></li><li><a href="#getline">getline — Full Lines</a></li><li><a href="#multiple">Multiple Inputs</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="cin">cin — Reading Input</h2>
<p><code>cin</code> (pronounced "see-in") reads input from the keyboard. It uses the <strong>extraction operator</strong> <code>&gt;&gt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int age;
    cout &lt;&lt; "Enter your age: ";
    cin &gt;&gt; age;
    cout &lt;&lt; "You are " &lt;&lt; age &lt;&lt; " years old." &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Sample Run:</h3>
<pre><code class="language-text">Enter your age: 25
You are 25 years old.</code></pre>

<h2 id="getline">getline — Reading a Full Line</h2>
<p><code>cin &gt;&gt;</code> stops reading at the first whitespace. To read a whole line including spaces, use <code>getline()</code>:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    string fullName;
    cout &lt;&lt; "Enter your full name: ";
    getline(cin, fullName);
    cout &lt;&lt; "Hello, " &lt;&lt; fullName &lt;&lt; "!" &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Sample Run:</h3>
<pre><code class="language-text">Enter your full name: John Smith
Hello, John Smith!</code></pre>

<h2 id="multiple">Reading Multiple Inputs</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    string name;
    int age;
    double height;

    cout &lt;&lt; "Name: ";   cin &gt;&gt; name;
    cout &lt;&lt; "Age: ";    cin &gt;&gt; age;
    cout &lt;&lt; "Height (m): "; cin &gt;&gt; height;

    cout &lt;&lt; "\n--- Profile ---" &lt;&lt; endl;
    cout &lt;&lt; "Name: "   &lt;&lt; name   &lt;&lt; endl;
    cout &lt;&lt; "Age: "    &lt;&lt; age    &lt;&lt; endl;
    cout &lt;&lt; "Height: " &lt;&lt; height &lt;&lt; "m" &lt;&lt; endl;
    return 0;
}</code></pre>

<div class="note-box"><strong>Tip:</strong> When mixing <code>cin &gt;&gt;</code> and <code>getline()</code>, call <code>cin.ignore()</code> between them to discard the leftover newline character in the buffer.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that asks the user for two integers and prints their sum, difference, and product.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int a, b;
    cout &lt;&lt; "Enter first number: ";  cin &gt;&gt; a;
    cout &lt;&lt; "Enter second number: "; cin &gt;&gt; b;

    cout &lt;&lt; "Sum: "        &lt;&lt; a + b &lt;&lt; endl;
    cout &lt;&lt; "Difference: " &lt;&lt; a - b &lt;&lt; endl;
    cout &lt;&lt; "Product: "    &lt;&lt; a * b &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which operator does <code>cin</code> use to read input?</p><ul><li>A) <code>&lt;&lt;</code></li><li>B) <code>=</code></li><li>C) <code>&gt;&gt;</code></li><li>D) <code>+</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>&gt;&gt;</code></strong> — the extraction operator reads data from the input stream into a variable.</p></details></li>
<li><p>Which function reads an entire line including spaces?</p><ul><li>A) <code>cin &gt;&gt;</code></li><li>B) <code>readline()</code></li><li>C) <code>input()</code></li><li>D) <code>getline()</code></li></ul><details><summary>Answer</summary><p><strong>D) <code>getline()</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>cin &gt;&gt; variable</code> reads a single word or number from the keyboard.</li>
<li><code>getline(cin, str)</code> reads an entire line including spaces.</li>
<li>Use <code>cin.ignore()</code> when switching between <code>cin &gt;&gt;</code> and <code>getline()</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-constants" class="btn btn-secondary">&larr; Previous: Constants</a>
<a href="/tutorials/cpp/cpp-operators" class="btn btn-primary">Next: C++ Operators &rarr;</a>
</div>
</div>$$,
'Learn how to accept user input in C++ using cin and getline. Read integers, strings, and full lines from the keyboard.',
'TUTORIAL', 'CamboFreelance', 'c++,user-input,cin,getline,beginner', NOW(), 9, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Operators',
'cpp-operators',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Operators</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 10 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#arithmetic">Arithmetic Operators</a></li><li><a href="#assignment">Assignment Operators</a></li><li><a href="#comparison">Comparison Operators</a></li><li><a href="#logical">Logical Operators</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="arithmetic">Arithmetic Operators</h2>
<table><thead><tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr></thead><tbody>
<tr><td><code>+</code></td><td>Addition</td><td><code>5 + 3</code></td><td>8</td></tr>
<tr><td><code>-</code></td><td>Subtraction</td><td><code>5 - 3</code></td><td>2</td></tr>
<tr><td><code>*</code></td><td>Multiplication</td><td><code>5 * 3</code></td><td>15</td></tr>
<tr><td><code>/</code></td><td>Division</td><td><code>10 / 3</code></td><td>3 (integer)</td></tr>
<tr><td><code>%</code></td><td>Modulus (remainder)</td><td><code>10 % 3</code></td><td>1</td></tr>
<tr><td><code>++</code></td><td>Increment</td><td><code>x++</code></td><td>x + 1</td></tr>
<tr><td><code>--</code></td><td>Decrement</td><td><code>x--</code></td><td>x - 1</td></tr>
</tbody></table>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int a = 10, b = 3;
    cout &lt;&lt; "a + b = " &lt;&lt; a + b &lt;&lt; endl;   // 13
    cout &lt;&lt; "a - b = " &lt;&lt; a - b &lt;&lt; endl;   // 7
    cout &lt;&lt; "a * b = " &lt;&lt; a * b &lt;&lt; endl;   // 30
    cout &lt;&lt; "a / b = " &lt;&lt; a / b &lt;&lt; endl;   // 3 (integer division)
    cout &lt;&lt; "a % b = " &lt;&lt; a % b &lt;&lt; endl;   // 1
    return 0;
}</code></pre>

<h2 id="assignment">Assignment Operators</h2>
<table><thead><tr><th>Operator</th><th>Equivalent To</th></tr></thead><tbody>
<tr><td><code>x = 5</code></td><td>Assign 5 to x</td></tr>
<tr><td><code>x += 3</code></td><td><code>x = x + 3</code></td></tr>
<tr><td><code>x -= 3</code></td><td><code>x = x - 3</code></td></tr>
<tr><td><code>x *= 3</code></td><td><code>x = x * 3</code></td></tr>
<tr><td><code>x /= 3</code></td><td><code>x = x / 3</code></td></tr>
<tr><td><code>x %= 3</code></td><td><code>x = x % 3</code></td></tr>
</tbody></table>

<h2 id="comparison">Comparison Operators</h2>
<p>These operators return <code>true</code> (1) or <code>false</code> (0):</p>
<table><thead><tr><th>Operator</th><th>Meaning</th><th>Example</th></tr></thead><tbody>
<tr><td><code>==</code></td><td>Equal to</td><td><code>5 == 5</code> → true</td></tr>
<tr><td><code>!=</code></td><td>Not equal</td><td><code>5 != 3</code> → true</td></tr>
<tr><td><code>&gt;</code></td><td>Greater than</td><td><code>5 &gt; 3</code> → true</td></tr>
<tr><td><code>&lt;</code></td><td>Less than</td><td><code>3 &lt; 5</code> → true</td></tr>
<tr><td><code>&gt;=</code></td><td>Greater or equal</td><td><code>5 &gt;= 5</code> → true</td></tr>
<tr><td><code>&lt;=</code></td><td>Less or equal</td><td><code>3 &lt;= 5</code> → true</td></tr>
</tbody></table>

<h2 id="logical">Logical Operators</h2>
<table><thead><tr><th>Operator</th><th>Name</th><th>Example</th></tr></thead><tbody>
<tr><td><code>&amp;&amp;</code></td><td>AND</td><td><code>true &amp;&amp; false</code> → false</td></tr>
<tr><td><code>||</code></td><td>OR</td><td><code>true || false</code> → true</td></tr>
<tr><td><code>!</code></td><td>NOT</td><td><code>!true</code> → false</td></tr>
</tbody></table>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that reads two numbers and prints: their sum, whether they are equal, and whether the first is greater than the second.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int x, y;
    cout &lt;&lt; "Enter two numbers: "; cin &gt;&gt; x &gt;&gt; y;
    cout &lt;&lt; "Sum: "      &lt;&lt; x + y        &lt;&lt; endl;
    cout &lt;&lt; "Equal: "    &lt;&lt; (x == y)     &lt;&lt; endl;
    cout &lt;&lt; "x > y: "    &lt;&lt; (x > y)      &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does the <code>%</code> operator do?</p><ul><li>A) Percentage calculation</li><li>B) Returns the remainder of division</li><li>C) Converts to float</li><li>D) None of the above</li></ul><details><summary>Answer</summary><p><strong>B)</strong> <code>%</code> is the modulus operator — it returns the remainder after integer division.</p></details></li>
<li><p>What is the result of <code>10 / 3</code> when both are integers in C++?</p><ul><li>A) 3.33</li><li>B) 4</li><li>C) 3</li><li>D) 1</li></ul><details><summary>Answer</summary><p><strong>C) 3</strong> — integer division truncates the decimal part.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Arithmetic operators: <code>+</code> <code>-</code> <code>*</code> <code>/</code> <code>%</code> <code>++</code> <code>--</code></li>
<li>Assignment shortcuts: <code>+=</code> <code>-=</code> <code>*=</code> <code>/=</code></li>
<li>Comparison operators return <code>true</code> or <code>false</code>.</li>
<li>Logical operators: <code>&amp;&amp;</code> (AND), <code>||</code> (OR), <code>!</code> (NOT).</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-user-input" class="btn btn-secondary">&larr; Previous: User Input</a>
<a href="/tutorials/cpp/cpp-strings" class="btn btn-primary">Next: C++ Strings &rarr;</a>
</div>
</div>$$,
'Master all C++ operators: arithmetic, assignment, comparison, and logical. Learn how expressions are evaluated with real examples.',
'TUTORIAL', 'CamboFreelance', 'c++,operators,arithmetic,beginner', NOW(), 10, 'PUBLISHED', 'ACT', 'SYS'
);
