-- C++ Tutorial Series — Beginner Articles 1-5
-- Article Type: TUTORIAL | Workflow: PUBLISHED | Author: CamboFreelance

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Introduction',
'cpp-introduction',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Introduction</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">5 min read</span><span class="lesson-number">Lesson 1 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#what-is-cpp">What is C++?</a></li><li><a href="#why-cpp">Why Use C++?</a></li><li><a href="#example">Your First Program</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ul></div>

<h2 id="what-is-cpp">What is C++?</h2>
<p>C++ is a general-purpose, high-performance programming language created by <strong>Bjarne Stroustrup</strong> in 1979 as an extension of the C language. It was originally called "C with Classes" and was renamed C++ in 1983.</p>
<p>C++ is one of the world's most popular programming languages and is used to develop:</p>
<ul><li>Operating systems (Windows, Linux kernel)</li><li>Game engines (Unreal Engine)</li><li>Web browsers (Chrome, Firefox)</li><li>Databases (MySQL, MongoDB)</li><li>Embedded systems and IoT devices</li><li>High-frequency trading software</li></ul>

<h2 id="why-cpp">Why Learn C++?</h2>
<ul>
<li><strong>Speed:</strong> C++ programs run very fast — close to machine code performance.</li>
<li><strong>Control:</strong> You have direct control over memory and hardware.</li>
<li><strong>Versatility:</strong> Supports procedural, object-oriented, and generic programming.</li>
<li><strong>Foundation:</strong> Learning C++ makes it easier to learn Java, C#, and other languages.</li>
<li><strong>Jobs:</strong> C++ developers are in high demand with excellent salaries.</li>
</ul>

<h2 id="example">Your First C++ Program</h2>
<p>Every C++ journey starts with a simple "Hello, World!" program:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Hello, World!" &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello, World!</code></pre>

<h2 id="explanation">Code Explained</h2>
<ul>
<li><code>#include &lt;iostream&gt;</code> — Includes the input/output stream library so we can use <code>cout</code>.</li>
<li><code>using namespace std;</code> — Lets us use standard library names without the <code>std::</code> prefix.</li>
<li><code>int main()</code> — Every C++ program must have a <code>main()</code> function. Execution starts here.</li>
<li><code>cout &lt;&lt; "Hello, World!" &lt;&lt; endl;</code> — Prints text to the screen. <code>endl</code> adds a newline.</li>
<li><code>return 0;</code> — Signals that the program ended successfully.</li>
</ul>

<div class="note-box"><strong>Note:</strong> C++ is case-sensitive. <code>cout</code> is not the same as <code>Cout</code> or <code>COUT</code>.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Modify the Hello World program to print your own name on the screen.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Hello, my name is Alex!" &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Who created C++?</p><ul><li>A) Dennis Ritchie</li><li>B) Bjarne Stroustrup</li><li>C) James Gosling</li><li>D) Guido van Rossum</li></ul><details><summary>Answer</summary><p><strong>B) Bjarne Stroustrup</strong> — He created C++ in 1979 at Bell Labs.</p></details></li>
<li><p>What does <code>#include &lt;iostream&gt;</code> do?</p><ul><li>A) Defines the main function</li><li>B) Imports the math library</li><li>C) Includes the input/output stream library</li><li>D) Creates a namespace</li></ul><details><summary>Answer</summary><p><strong>C)</strong> It includes the iostream library which provides <code>cout</code> and <code>cin</code>.</p></details></li>
<li><p>Which function is the entry point of every C++ program?</p><ul><li>A) start()</li><li>B) run()</li><li>C) begin()</li><li>D) main()</li></ul><details><summary>Answer</summary><p><strong>D) main()</strong> — Every C++ program begins execution from the <code>main()</code> function.</p></details></li>
</ol>
</div>

<div class="faq-box" id="faq">
<h2>FAQ</h2>
<details><summary>Is C++ hard to learn?</summary><p>C++ has a steeper learning curve than Python or JavaScript, but with structured practice it is very learnable. Start with the basics and build up gradually.</p></details>
<details><summary>C++ vs C — what is the difference?</summary><p>C++ includes everything C has, plus object-oriented programming (classes, objects, inheritance, polymorphism) and the Standard Template Library (STL).</p></details>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>C++ was created by Bjarne Stroustrup in 1979.</li>
<li>It is a fast, powerful, general-purpose language.</li>
<li>Every C++ program has a <code>main()</code> function.</li>
<li><code>cout</code> is used to print output to the screen.</li>
<li><code>#include &lt;iostream&gt;</code> must be included to use <code>cout</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-getting-started" class="btn btn-primary">Next: C++ Getting Started &rarr;</a>
</div>
</div>$$,
'Learn what C++ is, why it is one of the most powerful programming languages in the world, and write your very first C++ program.',
'TUTORIAL',
'CamboFreelance',
'c++,programming,beginner,introduction',
NOW(), 1, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Getting Started',
'cpp-getting-started',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Getting Started</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 2 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#install">Install a Compiler</a></li><li><a href="#ide">Choose an IDE</a></li><li><a href="#first-program">Compile &amp; Run</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ul></div>

<h2 id="install">Step 1 — Install a C++ Compiler</h2>
<p>A <strong>compiler</strong> converts your C++ source code into an executable program. The most common compilers are:</p>
<table><thead><tr><th>Compiler</th><th>Platform</th><th>Notes</th></tr></thead><tbody>
<tr><td>GCC (g++)</td><td>Linux / macOS / Windows</td><td>Free, open-source, most widely used</td></tr>
<tr><td>Clang</td><td>macOS / Linux / Windows</td><td>Fast, excellent error messages</td></tr>
<tr><td>MSVC</td><td>Windows</td><td>Built into Visual Studio</td></tr>
</tbody></table>

<h3>Install on Windows (MinGW/GCC)</h3>
<ol><li>Download <strong>MSYS2</strong> from msys2.org.</li><li>Open the MSYS2 terminal and run: <code>pacman -S mingw-w64-x86_64-gcc</code></li><li>Add <code>C:\msys64\mingw64\bin</code> to your PATH environment variable.</li><li>Verify: open Command Prompt and type <code>g++ --version</code></li></ol>

<h3>Install on macOS</h3>
<pre><code class="language-bash">xcode-select --install</code></pre>
<p>This installs Clang (Apple's C++ compiler). Verify with <code>clang++ --version</code>.</p>

<h3>Install on Linux (Ubuntu/Debian)</h3>
<pre><code class="language-bash">sudo apt update
sudo apt install g++</code></pre>

<h2 id="ide">Step 2 — Choose an IDE or Editor</h2>
<table><thead><tr><th>IDE / Editor</th><th>Best For</th></tr></thead><tbody>
<tr><td>Visual Studio Code</td><td>Lightweight, cross-platform, great extensions</td></tr>
<tr><td>Visual Studio</td><td>Windows, full-featured, built-in MSVC</td></tr>
<tr><td>CLion</td><td>Professional C++ development (paid)</td></tr>
<tr><td>Code::Blocks</td><td>Beginners, lightweight, free</td></tr>
<tr><td>Online: cpp.sh</td><td>No install needed, browser-based</td></tr>
</tbody></table>

<h2 id="first-program">Step 3 — Compile and Run Your First Program</h2>
<p>Create a file named <strong>hello.cpp</strong> with the following code:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Hello, World!" &lt;&lt; endl;
    return 0;
}</code></pre>

<p>Open your terminal, navigate to the file location, then compile and run:</p>
<pre><code class="language-bash">g++ hello.cpp -o hello
./hello</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello, World!</code></pre>

<div class="note-box"><strong>Note:</strong> The <code>-o hello</code> flag names the output executable <code>hello</code>. On Windows the output will be <code>hello.exe</code>.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a file <code>myprogram.cpp</code> that prints <code>C++ is awesome!</code> and compile it using <code>g++</code>.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "C++ is awesome!" &lt;&lt; endl;
    return 0;
}</code></pre>
<pre><code class="language-bash">g++ myprogram.cpp -o myprogram
./myprogram</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does a compiler do?</p><ul><li>A) Edits code</li><li>B) Converts source code to an executable</li><li>C) Runs code line by line</li><li>D) Manages memory</li></ul><details><summary>Answer</summary><p><strong>B)</strong> A compiler translates your C++ source code into machine code (an executable).</p></details></li>
<li><p>Which command compiles a file named <code>main.cpp</code> using g++?</p><ul><li>A) <code>run main.cpp</code></li><li>B) <code>cpp main.cpp</code></li><li>C) <code>g++ main.cpp -o main</code></li><li>D) <code>compile main.cpp</code></li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>g++ main.cpp -o main</code> compiles the file and names the output <code>main</code>.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>You need a <strong>compiler</strong> (GCC/g++, Clang, or MSVC) to run C++ programs.</li>
<li>C++ source files use the <strong>.cpp</strong> extension.</li>
<li>Compile with <code>g++ filename.cpp -o output</code>, run with <code>./output</code>.</li>
<li>Online compilers like cpp.sh let you practice without installing anything.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-introduction" class="btn btn-secondary">&larr; Previous: Introduction</a>
<a href="/tutorials/cpp/cpp-syntax" class="btn btn-primary">Next: C++ Syntax &rarr;</a>
</div>
</div>$$,
'Set up your C++ development environment. Learn how to install a compiler, choose an IDE, and compile and run your first C++ program.',
'TUTORIAL',
'CamboFreelance',
'c++,setup,compiler,gcc,beginner',
NOW(), 2, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Syntax',
'cpp-syntax',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Syntax</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 3 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#structure">Program Structure</a></li><li><a href="#statements">Statements &amp; Semicolons</a></li><li><a href="#whitespace">Whitespace</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="structure">C++ Program Structure</h2>
<p>Every C++ program follows a basic structure. Let us look at the parts:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;       // 1. Include libraries
using namespace std;       // 2. Use the standard namespace

int main() {               // 3. Main function — entry point
    cout &lt;&lt; "Hello!";     // 4. Statement
    return 0;              // 5. Return value
}</code></pre>
<table><thead><tr><th>Part</th><th>Description</th></tr></thead><tbody>
<tr><td><code>#include &lt;iostream&gt;</code></td><td>Header file for input/output operations</td></tr>
<tr><td><code>using namespace std;</code></td><td>Avoids writing <code>std::</code> before every standard name</td></tr>
<tr><td><code>int main()</code></td><td>Entry point — every program starts here</td></tr>
<tr><td><code>{ }</code></td><td>Curly braces define a code block</td></tr>
<tr><td><code>return 0;</code></td><td>Returns 0 to the OS, signalling success</td></tr>
</tbody></table>

<h2 id="statements">Statements and Semicolons</h2>
<p>Every <strong>statement</strong> in C++ must end with a <strong>semicolon</strong> (<code>;</code>). Missing semicolons are one of the most common beginner errors.</p>
<pre><code class="language-cpp">cout &lt;&lt; "Line one";    // correct
cout &lt;&lt; "Line two"     // ERROR: missing semicolon</code></pre>

<h2 id="whitespace">Whitespace and Indentation</h2>
<p>C++ ignores extra spaces, tabs, and newlines. They are used only to make code readable. The two programs below are identical to the compiler:</p>
<pre><code class="language-cpp">// Readable
int main() {
    cout &lt;&lt; "Hello";
    return 0;
}

// Minified (valid but unreadable)
int main(){cout&lt;&lt;"Hello";return 0;}</code></pre>
<div class="note-box"><strong>Best Practice:</strong> Always indent code inside curly braces by 4 spaces or 1 tab for readability.</div>

<h2 id="example">Full Example</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "C++ Syntax is simple!" &lt;&lt; endl;
    cout &lt;&lt; "Each statement ends with a semicolon." &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">C++ Syntax is simple!
Each statement ends with a semicolon.</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Find and fix the syntax errors in the following code:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std

int main()
    cout &lt;&lt; "Fix me!" &lt;&lt; endl
    return 0;
}</code></pre>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;    // added semicolon

int main() {            // added opening brace
    cout &lt;&lt; "Fix me!" &lt;&lt; endl;  // added semicolon
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What character ends a statement in C++?</p><ul><li>A) Colon <code>:</code></li><li>B) Period <code>.</code></li><li>C) Semicolon <code>;</code></li><li>D) Comma <code>,</code></li></ul><details><summary>Answer</summary><p><strong>C) Semicolon</strong></p></details></li>
<li><p>Where does C++ program execution begin?</p><ul><li>A) The first line of the file</li><li>B) The <code>start()</code> function</li><li>C) The <code>main()</code> function</li><li>D) The <code>#include</code> line</li></ul><details><summary>Answer</summary><p><strong>C) The <code>main()</code> function.</strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>A C++ program starts with <code>#include</code> directives and a <code>main()</code> function.</li>
<li>Every statement must end with a <strong>semicolon</strong>.</li>
<li>Code blocks are enclosed in <strong>curly braces</strong> <code>{ }</code>.</li>
<li>Whitespace is ignored by the compiler but important for readability.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-getting-started" class="btn btn-secondary">&larr; Previous: Getting Started</a>
<a href="/tutorials/cpp/cpp-output" class="btn btn-primary">Next: C++ Output &rarr;</a>
</div>
</div>$$,
'Understand the basic syntax and structure of a C++ program including statements, semicolons, code blocks, and whitespace rules.',
'TUTORIAL',
'CamboFreelance',
'c++,syntax,structure,beginner',
NOW(), 3, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Output',
'cpp-output',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Output</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">5 min read</span><span class="lesson-number">Lesson 4 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#cout">cout</a></li><li><a href="#newline">New Lines</a></li><li><a href="#multiple">Multiple Outputs</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="cout">The cout Object</h2>
<p><code>cout</code> (pronounced "see-out") is used to output values/print text in C++. It is used together with the <strong>insertion operator</strong> <code>&lt;&lt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Hello World!";
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello World!</code></pre>

<h2 id="newline">New Lines</h2>
<p>There are two ways to insert a new line in C++ output:</p>
<h3>Method 1: endl</h3>
<pre><code class="language-cpp">cout &lt;&lt; "First line" &lt;&lt; endl;
cout &lt;&lt; "Second line" &lt;&lt; endl;</code></pre>
<h3>Method 2: \n (escape character)</h3>
<pre><code class="language-cpp">cout &lt;&lt; "First line\n";
cout &lt;&lt; "Second line\n";</code></pre>
<h3>Output (both methods):</h3>
<pre><code class="language-text">First line
Second line</code></pre>
<div class="note-box"><strong>Tip:</strong> <code>\n</code> is faster than <code>endl</code> because <code>endl</code> also flushes the output buffer. Prefer <code>\n</code> in performance-sensitive code.</div>

<h2 id="multiple">Multiple Outputs with &lt;&lt;</h2>
<p>You can chain multiple values in a single <code>cout</code> statement:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    int age = 25;
    cout &lt;&lt; "I am " &lt;&lt; age &lt;&lt; " years old." &lt;&lt; endl;
    cout &lt;&lt; "2 + 3 = " &lt;&lt; 2 + 3 &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">I am 25 years old.
2 + 3 = 5</code></pre>

<h3>Other Escape Sequences</h3>
<table><thead><tr><th>Escape</th><th>Meaning</th></tr></thead><tbody>
<tr><td><code>\n</code></td><td>New line</td></tr>
<tr><td><code>\t</code></td><td>Tab (horizontal)</td></tr>
<tr><td><code>\\</code></td><td>Backslash</td></tr>
<tr><td><code>\"</code></td><td>Double quote</td></tr>
</tbody></table>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that outputs three lines: your name, your age, and your city, each on a separate line using <code>\n</code>.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Name: Alex\n";
    cout &lt;&lt; "Age: 25\n";
    cout &lt;&lt; "City: Phnom Penh\n";
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which operator is used with <code>cout</code>?</p><ul><li>A) <code>&gt;&gt;</code></li><li>B) <code>&lt;&lt;</code></li><li>C) <code>+</code></li><li>D) <code>=</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>&lt;&lt;</code></strong> — the insertion operator sends data to the output stream.</p></details></li>
<li><p>What is the difference between <code>endl</code> and <code>\n</code>?</p><ul><li>A) No difference</li><li>B) <code>\n</code> only works in strings</li><li>C) <code>endl</code> also flushes the buffer; <code>\n</code> does not</li><li>D) <code>endl</code> adds two newlines</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>endl</code> flushes the output buffer in addition to adding a newline, making it slower.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>cout</code> is used to print output in C++.</li>
<li>Use <code>&lt;&lt;</code> (insertion operator) to send data to <code>cout</code>.</li>
<li><code>endl</code> or <code>\n</code> creates a new line; <code>\n</code> is faster.</li>
<li>You can chain multiple values with <code>&lt;&lt;</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-syntax" class="btn btn-secondary">&larr; Previous: Syntax</a>
<a href="/tutorials/cpp/cpp-comments" class="btn btn-primary">Next: C++ Comments &rarr;</a>
</div>
</div>$$,
'Learn how to print output in C++ using cout, the insertion operator, newlines with endl and \n, and escape sequences.',
'TUTORIAL',
'CamboFreelance',
'c++,output,cout,beginner',
NOW(), 4, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Comments',
'cpp-comments',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Comments</nav>
<div class="lesson-meta"><span class="badge badge-beginner">Beginner</span><span class="read-time">4 min read</span><span class="lesson-number">Lesson 5 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#single">Single-Line Comments</a></li><li><a href="#multi">Multi-Line Comments</a></li><li><a href="#uses">When to Use Comments</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="single">Single-Line Comments</h2>
<p>Single-line comments start with two forward slashes <code>//</code>. Everything after <code>//</code> on that line is ignored by the compiler.</p>
<pre><code class="language-cpp">// This is a single-line comment
cout &lt;&lt; "Hello World!"; // This comment is at end of a statement</code></pre>

<h2 id="multi">Multi-Line Comments</h2>
<p>Multi-line comments start with <code>/*</code> and end with <code>*/</code>. They can span multiple lines.</p>
<pre><code class="language-cpp">/* This is a multi-line comment.
   It can span many lines.
   The compiler ignores everything between the delimiters. */
cout &lt;&lt; "Comments don't affect output!";</code></pre>

<h2 id="example">Full Example</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    // Print a greeting message
    cout &lt;&lt; "Hello!" &lt;&lt; endl;

    /* This block demonstrates
       multi-line comments */
    cout &lt;&lt; "Comments are for humans, not compilers." &lt;&lt; endl;

    return 0; // End of program
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Hello!
Comments are for humans, not compilers.</code></pre>

<h2 id="uses">When to Use Comments</h2>
<ul>
<li><strong>Explain complex logic</strong> that is not obvious from the code.</li>
<li><strong>Temporarily disable code</strong> during debugging.</li>
<li><strong>Document functions</strong> — what they do, parameters, return values.</li>
<li><strong>Leave TODOs</strong> for future improvements.</li>
</ul>
<div class="note-box"><strong>Best Practice:</strong> Write comments that explain <em>why</em> something is done, not <em>what</em> is being done. Good code is self-explanatory; comments fill in the reasoning.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Add a single-line comment above each <code>cout</code> line to explain what it prints.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    cout &lt;&lt; "Name: Alex" &lt;&lt; endl;
    cout &lt;&lt; "Age: 25" &lt;&lt; endl;
    return 0;
}</code></pre>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    // Print the user's name
    cout &lt;&lt; "Name: Alex" &lt;&lt; endl;
    // Print the user's age
    cout &lt;&lt; "Age: 25" &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which syntax starts a single-line comment in C++?</p><ul><li>A) <code>#</code></li><li>B) <code>--</code></li><li>C) <code>//</code></li><li>D) <code>**</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>//</code></strong></p></details></li>
<li><p>What happens to comments when the code is compiled?</p><ul><li>A) They are printed to the screen</li><li>B) They cause compile errors</li><li>C) They are completely ignored</li><li>D) They are stored in the executable</li></ul><details><summary>Answer</summary><p><strong>C)</strong> Comments are ignored by the compiler and have no effect on the program.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Use <code>//</code> for single-line comments.</li>
<li>Use <code>/* ... */</code> for multi-line comments.</li>
<li>Comments are ignored by the compiler.</li>
<li>Use comments to explain <em>why</em>, not <em>what</em>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-output" class="btn btn-secondary">&larr; Previous: Output</a>
<a href="/tutorials/cpp/cpp-variables" class="btn btn-primary">Next: C++ Variables &rarr;</a>
</div>
</div>$$,
'Learn how to write single-line and multi-line comments in C++ and understand best practices for documenting your code.',
'TUTORIAL',
'CamboFreelance',
'c++,comments,beginner,documentation',
NOW(), 5, 'PUBLISHED', 'ACT', 'SYS'
);
