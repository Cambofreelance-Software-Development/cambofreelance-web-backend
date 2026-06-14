-- C++ Tutorial Series — Advanced Articles 41-50

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Smart Pointers', 'cpp-smart-pointers',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Smart Pointers</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">10 min read</span><span class="lesson-number">Lesson 41 of 50</span></div>
<h2 id="what">Why Smart Pointers?</h2>
<p>Raw pointers require manual <code>new</code>/<code>delete</code>, risking memory leaks. <strong>Smart pointers</strong> (C++11, <code>&lt;memory&gt;</code>) manage memory automatically using RAII.</p>

<h2 id="unique">unique_ptr — Exclusive Ownership</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;memory&gt;
using namespace std;

struct Resource {
    string name;
    Resource(string n) : name(n) { cout &lt;&lt; name &lt;&lt; " created\n"; }
    ~Resource()                  { cout &lt;&lt; name &lt;&lt; " destroyed\n"; }
};

int main() {
    {
        unique_ptr&lt;Resource&gt; p1 = make_unique&lt;Resource&gt;("DB Connection");
        cout &lt;&lt; "Using: " &lt;&lt; p1-&gt;name &lt;&lt; endl;
    }   // p1 goes out of scope — automatically destroyed
    cout &lt;&lt; "After scope" &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">DB Connection created
Using: DB Connection
DB Connection destroyed
After scope</code></pre>

<h2 id="shared">shared_ptr — Shared Ownership</h2>
<p>Multiple <code>shared_ptr</code>s can own the same resource. It is destroyed when the last owner goes out of scope (reference counting).</p>
<pre><code class="language-cpp">shared_ptr&lt;Resource&gt; p1 = make_shared&lt;Resource&gt;("Config");
{
    shared_ptr&lt;Resource&gt; p2 = p1;   // shared ownership
    cout &lt;&lt; "Use count: " &lt;&lt; p1.use_count() &lt;&lt; endl;  // 2
}   // p2 destroyed, use_count drops to 1
cout &lt;&lt; "Use count: " &lt;&lt; p1.use_count() &lt;&lt; endl;      // 1
// When p1 goes out of scope, Resource is finally destroyed</code></pre>

<h2 id="weak">weak_ptr — Non-Owning Observer</h2>
<p><code>weak_ptr</code> observes a <code>shared_ptr</code> without extending its lifetime — breaks circular references:</p>
<pre><code class="language-cpp">shared_ptr&lt;int&gt; sp = make_shared&lt;int&gt;(42);
weak_ptr&lt;int&gt; wp = sp;

if (auto locked = wp.lock()) {   // check if still alive
    cout &lt;&lt; *locked &lt;&lt; endl;     // 42
}</code></pre>

<div class="note-box"><strong>Rule:</strong> Prefer <code>unique_ptr</code> by default. Use <code>shared_ptr</code> when ownership is genuinely shared. Use <code>weak_ptr</code> to break circular <code>shared_ptr</code> cycles.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>FileLogger</code> class. Use <code>unique_ptr</code> to manage it in main. Transfer ownership to a function <code>process(unique_ptr&lt;FileLogger&gt;)</code> using <code>std::move</code>.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;memory&gt;
using namespace std;

class FileLogger {
  public:
    FileLogger()  { cout &lt;&lt; "Logger opened\n"; }
    ~FileLogger() { cout &lt;&lt; "Logger closed\n"; }
    void log(const string&amp; msg) { cout &lt;&lt; "[LOG] " &lt;&lt; msg &lt;&lt; endl; }
};

void process(unique_ptr&lt;FileLogger&gt; logger) {
    logger-&gt;log("Processing...");
}   // logger destroyed here

int main() {
    auto logger = make_unique&lt;FileLogger&gt;();
    logger-&gt;log("Starting");
    process(move(logger));   // transfer ownership
    // logger is now null
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>Which smart pointer allows only one owner?</p><ul><li>A) shared_ptr</li><li>B) weak_ptr</li><li>C) unique_ptr</li><li>D) auto_ptr</li></ul><details><summary>Answer</summary><p><strong>C) unique_ptr</strong></p></details></li>
<li><p>What does weak_ptr prevent?</p><ul><li>A) Memory allocation</li><li>B) Circular references that prevent destruction</li><li>C) Multiple ownership</li><li>D) Stack overflow</li></ul><details><summary>Answer</summary><p><strong>B)</strong> weak_ptr breaks circular shared_ptr cycles that would cause memory leaks.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>unique_ptr</code> — single owner, zero overhead, use <code>move()</code> to transfer.</li><li><code>shared_ptr</code> — reference-counted shared ownership.</li><li><code>weak_ptr</code> — non-owning observer, breaks cycles.</li><li>Always use <code>make_unique</code> and <code>make_shared</code> — never use raw <code>new</code>.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-stacks" class="btn btn-secondary">&larr; Previous: Stacks</a>
<a href="/tutorials/cpp/cpp-templates" class="btn btn-primary">Next: Templates &rarr;</a>
</div>
</div>$$,
'Master C++ smart pointers: unique_ptr for exclusive ownership, shared_ptr for shared ownership, and weak_ptr to break cycles.',
'TUTORIAL', 'CamboFreelance', 'c++,smart-pointers,memory,advanced', NOW(), 41, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Templates', 'cpp-templates',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Templates</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">10 min read</span><span class="lesson-number">Lesson 42 of 50</span></div>
<h2 id="what">What are Templates?</h2>
<p><strong>Templates</strong> allow writing generic code that works with any data type. The compiler generates the actual type-specific code at compile time.</p>

<h2 id="function-template">Function Templates</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

template &lt;typename T&gt;
T maximum(T a, T b) {
    return (a &gt; b) ? a : b;
}

template &lt;typename T&gt;
void printArray(T arr[], int size) {
    for (int i = 0; i &lt; size; i++) cout &lt;&lt; arr[i] &lt;&lt; " ";
    cout &lt;&lt; endl;
}

int main() {
    cout &lt;&lt; maximum(3, 7)        &lt;&lt; endl;    // int version: 7
    cout &lt;&lt; maximum(3.14, 2.71)  &lt;&lt; endl;    // double: 3.14
    cout &lt;&lt; maximum('a', 'z')    &lt;&lt; endl;    // char: z

    int    ints[]   = {1, 2, 3, 4, 5};
    double doubles[] = {1.1, 2.2, 3.3};
    printArray(ints,    5);
    printArray(doubles, 3);
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">7
3.14
z
1 2 3 4 5
1.1 2.2 3.3</code></pre>

<h2 id="class-template">Class Templates</h2>
<pre><code class="language-cpp">template &lt;typename T&gt;
class Stack {
  private:
    vector&lt;T&gt; data;
  public:
    void push(T val)   { data.push_back(val); }
    void pop()         { data.pop_back(); }
    T    top()         { return data.back(); }
    bool empty()       { return data.empty(); }
    int  size()        { return data.size(); }
};

Stack&lt;int&gt;    intStack;
Stack&lt;string&gt; strStack;

intStack.push(10); intStack.push(20);
cout &lt;&lt; intStack.top() &lt;&lt; endl;   // 20

strStack.push("hello");
cout &lt;&lt; strStack.top() &lt;&lt; endl;   // hello</code></pre>

<h2 id="multiple-types">Multiple Type Parameters</h2>
<pre><code class="language-cpp">template &lt;typename K, typename V&gt;
class Pair {
  public:
    K key; V value;
    Pair(K k, V v) : key(k), value(v) { }
    void print() { cout &lt;&lt; key &lt;&lt; " -&gt; " &lt;&lt; value &lt;&lt; endl; }
};

Pair&lt;string, int&gt;    p1("age", 25);
Pair&lt;int,    double&gt; p2(1, 3.14);
p1.print();   // age -> 25
p2.print();   // 1 -> 3.14</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a function template <code>swap(T&amp; a, T&amp; b)</code> that swaps two values of any type. Test it with int, double, and string.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

template &lt;typename T&gt;
void swapValues(T&amp; a, T&amp; b) {
    T temp = a; a = b; b = temp;
}

int main() {
    int    x = 5,   y = 10;   swapValues(x, y); cout &lt;&lt; x &lt;&lt; " " &lt;&lt; y &lt;&lt; endl;
    double a = 1.5, b = 2.5;  swapValues(a, b); cout &lt;&lt; a &lt;&lt; " " &lt;&lt; b &lt;&lt; endl;
    string s1 = "hi", s2 = "bye"; swapValues(s1, s2); cout &lt;&lt; s1 &lt;&lt; " " &lt;&lt; s2 &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What keyword introduces a template parameter?</p><ul><li>A) <code>generic</code></li><li>B) <code>template</code> with <code>typename</code></li><li>C) <code>type</code></li><li>D) <code>auto</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>template &lt;typename T&gt;</code></strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Templates enable generic, type-independent code.</li><li>Function templates work with any type: <code>template &lt;typename T&gt; T func(T a)</code>.</li><li>Class templates create generic data structures.</li><li>The compiler instantiates the correct version at compile time.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-smart-pointers" class="btn btn-secondary">&larr; Previous: Smart Pointers</a>
<a href="/tutorials/cpp/cpp-lambda-functions" class="btn btn-primary">Next: Lambda Functions &rarr;</a>
</div>
</div>$$,
'Learn C++ templates to write generic reusable code. Create function templates and class templates that work with any data type.',
'TUTORIAL', 'CamboFreelance', 'c++,templates,generics,advanced', NOW(), 42, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Lambda Functions', 'cpp-lambda-functions',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Lambda Functions</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">9 min read</span><span class="lesson-number">Lesson 43 of 50</span></div>
<h2 id="what">What is a Lambda?</h2>
<p>A <strong>lambda</strong> (C++11) is an anonymous, inline function — defined where you need it without naming it. Syntax:</p>
<pre><code class="language-cpp">[capture](parameters) -&gt; returnType { body }</code></pre>

<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;algorithm&gt;
using namespace std;

int main() {
    // Simple lambda
    auto greet = [](string name) {
        cout &lt;&lt; "Hello, " &lt;&lt; name &lt;&lt; "!" &lt;&lt; endl;
    };
    greet("Alice");   // Hello, Alice!

    // Lambda with return type
    auto add = [](int a, int b) -&gt; int { return a + b; };
    cout &lt;&lt; add(3, 5) &lt;&lt; endl;   // 8

    // Lambda with STL
    vector&lt;int&gt; nums = {5, 2, 8, 1, 9, 3};
    sort(nums.begin(), nums.end(), [](int a, int b) { return a &gt; b; }); // descending
    for (int n : nums) cout &lt;&lt; n &lt;&lt; " ";   // 9 8 5 3 2 1
    cout &lt;&lt; endl;
    return 0;
}</code></pre>

<h2 id="capture">Capture Clauses</h2>
<p>Lambdas can capture variables from the surrounding scope:</p>
<pre><code class="language-cpp">int threshold = 5;

// Capture by value [=]
auto isAbove = [=](int n) { return n &gt; threshold; };

// Capture by reference [&amp;]
int count = 0;
auto countAbove = [&amp;count, threshold](int n) {
    if (n &gt; threshold) count++;
};

vector&lt;int&gt; data = {1, 7, 3, 9, 2, 6};
for (int n : data) countAbove(n);
cout &lt;&lt; "Above " &lt;&lt; threshold &lt;&lt; ": " &lt;&lt; count &lt;&lt; endl;   // 3</code></pre>

<h2 id="stl">Lambdas with STL</h2>
<pre><code class="language-cpp">vector&lt;string&gt; words = {"banana", "apple", "cherry", "date"};

// Sort by length
sort(words.begin(), words.end(),
     [](const string&amp; a, const string&amp; b) { return a.size() &lt; b.size(); });

// Filter with count_if
int shortWords = count_if(words.begin(), words.end(),
                          [](const string&amp; s) { return s.size() &lt;= 5; });
cout &lt;&lt; "Short words: " &lt;&lt; shortWords &lt;&lt; endl;</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Given a vector of integers, use a lambda with <code>remove_if</code> and <code>erase</code> to remove all odd numbers. Print the result.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;algorithm&gt;
using namespace std;

int main() {
    vector&lt;int&gt; nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    nums.erase(remove_if(nums.begin(), nums.end(),
               [](int n) { return n % 2 != 0; }), nums.end());
    for (int n : nums) cout &lt;&lt; n &lt;&lt; " ";   // 2 4 6 8 10
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does <code>[&amp;]</code> in a lambda capture clause mean?</p><ul><li>A) Capture nothing</li><li>B) Capture all variables by value</li><li>C) Capture all variables by reference</li><li>D) Capture only local variables</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>[&amp;]</code> captures all local variables by reference.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Lambdas are anonymous inline functions: <code>[capture](params){ body }</code>.</li><li>Capture by value with <code>[=]</code>, by reference with <code>[&amp;]</code>.</li><li>Perfect with STL algorithms: sort, find_if, count_if, transform.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-templates" class="btn btn-secondary">&larr; Previous: Templates</a>
<a href="/tutorials/cpp/cpp-move-semantics" class="btn btn-primary">Next: Move Semantics &rarr;</a>
</div>
</div>$$,
'Learn C++ lambda functions: anonymous functions, capture clauses (by value and reference), and use with STL algorithms.',
'TUTORIAL', 'CamboFreelance', 'c++,lambda,functional,advanced', NOW(), 43, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Move Semantics', 'cpp-move-semantics',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Move Semantics</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">11 min read</span><span class="lesson-number">Lesson 44 of 50</span></div>
<h2 id="what">lvalues and rvalues</h2>
<p>An <strong>lvalue</strong> has a name and a persistent memory address. An <strong>rvalue</strong> is a temporary value with no address.</p>
<pre><code class="language-cpp">int x = 42;      // x is lvalue; 42 is rvalue
string s = "hi"; // s is lvalue; "hi" is rvalue
string t = s;    // copy: expensive for large strings</code></pre>

<h2 id="move">std::move and Move Constructor</h2>
<p><code>std::move</code> transfers ownership of resources instead of copying them — no memory allocation, much faster for large objects:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;string&gt;
using namespace std;

class Buffer {
  public:
    vector&lt;int&gt; data;
    Buffer(int size) : data(size, 0) {
        cout &lt;&lt; "Constructed, size=" &lt;&lt; data.size() &lt;&lt; endl;
    }
    // Move constructor
    Buffer(Buffer&amp;&amp; other) noexcept : data(move(other.data)) {
        cout &lt;&lt; "Moved!" &lt;&lt; endl;
    }
};

int main() {
    Buffer b1(1000000);
    Buffer b2(move(b1));   // move, not copy
    cout &lt;&lt; "b1 size: " &lt;&lt; b1.data.size() &lt;&lt; endl;   // 0 (stolen)
    cout &lt;&lt; "b2 size: " &lt;&lt; b2.data.size() &lt;&lt; endl;   // 1000000
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Constructed, size=1000000
Moved!
b1 size: 0
b2 size: 1000000</code></pre>

<h2 id="rvalue-ref">Rvalue References</h2>
<pre><code class="language-cpp">void process(string&amp;&amp; s) {   // rvalue reference parameter
    cout &lt;&lt; "Moving: " &lt;&lt; s &lt;&lt; endl;
}

process("temporary");       // OK — rvalue
string name = "Alice";
process(move(name));        // cast lvalue to rvalue</code></pre>

<div class="note-box"><strong>Rule of Five:</strong> If you define a destructor, copy constructor, or copy assignment, also define move constructor and move assignment operator.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a vector of 5 strings. Use <code>std::move</code> to move (not copy) one string into a new variable. Verify the original is empty after the move.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    vector&lt;string&gt; words = {"alpha","beta","gamma","delta","epsilon"};
    string moved = move(words[2]);
    cout &lt;&lt; "Moved: "    &lt;&lt; moved    &lt;&lt; endl;   // gamma
    cout &lt;&lt; "Original: " &lt;&lt; words[2] &lt;&lt; endl;   // (empty)
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does <code>std::move(x)</code> actually do?</p><ul><li>A) Copies x to a new location</li><li>B) Casts x to an rvalue reference, enabling the move constructor</li><li>C> Deletes x</li><li>D) Swaps x with another variable</li></ul><details><summary>Answer</summary><p><strong>B)</strong> <code>std::move</code> is just a cast — it enables the move constructor/assignment to steal resources.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Move semantics transfer resource ownership instead of copying.</li><li><code>T&amp;&amp;</code> is an rvalue reference; <code>std::move()</code> casts to one.</li><li>Move constructors and move assignment operators avoid expensive deep copies.</li><li>Follow the Rule of Five when managing resources.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-lambda-functions" class="btn btn-secondary">&larr; Previous: Lambda Functions</a>
<a href="/tutorials/cpp/cpp-multithreading" class="btn btn-primary">Next: Multithreading &rarr;</a>
</div>
</div>$$,
'Understand C++ move semantics: rvalue references, std::move, move constructors, and move assignment for high-performance code.',
'TUTORIAL', 'CamboFreelance', 'c++,move-semantics,performance,advanced', NOW(), 44, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Multithreading', 'cpp-multithreading',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Multithreading</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">12 min read</span><span class="lesson-number">Lesson 45 of 50</span></div>
<h2 id="what">std::thread (C++11)</h2>
<p>C++11 introduced native threading via <code>&lt;thread&gt;</code>. Compile with <code>-std=c++11 -pthread</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;thread&gt;
#include &lt;chrono&gt;
using namespace std;

void task(int id) {
    cout &lt;&lt; "Thread " &lt;&lt; id &lt;&lt; " starting\n";
    this_thread::sleep_for(chrono::milliseconds(100 * id));
    cout &lt;&lt; "Thread " &lt;&lt; id &lt;&lt; " done\n";
}

int main() {
    thread t1(task, 1);
    thread t2(task, 2);
    thread t3(task, 3);

    t1.join();   // wait for t1 to finish
    t2.join();
    t3.join();

    cout &lt;&lt; "All threads completed." &lt;&lt; endl;
    return 0;
}</code></pre>

<h2 id="mutex">Mutex — Preventing Race Conditions</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;thread&gt;
#include &lt;mutex&gt;
using namespace std;

mutex mtx;
int counter = 0;

void increment(int n) {
    for (int i = 0; i &lt; n; i++) {
        lock_guard&lt;mutex&gt; lock(mtx);   // auto-unlocks when out of scope
        counter++;
    }
}

int main() {
    thread t1(increment, 10000);
    thread t2(increment, 10000);
    t1.join(); t2.join();
    cout &lt;&lt; "Counter: " &lt;&lt; counter &lt;&lt; endl;   // 20000 (thread-safe)
    return 0;
}</code></pre>

<h2 id="async">std::async and std::future</h2>
<pre><code class="language-cpp">#include &lt;future&gt;

auto result = async(launch::async, []() {
    // heavy computation
    int sum = 0;
    for (int i = 1; i &lt;= 1000000; i++) sum += i;
    return sum;
});

cout &lt;&lt; "Result: " &lt;&lt; result.get() &lt;&lt; endl;   // blocks until done</code></pre>

<div class="note-box"><strong>Warning:</strong> Always <code>join()</code> or <code>detach()</code> threads before they go out of scope. Destroying a joinable thread calls <code>std::terminate()</code>.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create two threads: one prints even numbers 0–10, the other prints odd numbers 1–9. Use a mutex to prevent interleaved output.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;thread&gt;
#include &lt;mutex&gt;
using namespace std;

mutex print_mtx;

void printEvens() {
    for (int i = 0; i &lt;= 10; i += 2) {
        lock_guard&lt;mutex&gt; lock(print_mtx);
        cout &lt;&lt; "Even: " &lt;&lt; i &lt;&lt; endl;
    }
}
void printOdds() {
    for (int i = 1; i &lt;= 9; i += 2) {
        lock_guard&lt;mutex&gt; lock(print_mtx);
        cout &lt;&lt; "Odd: " &lt;&lt; i &lt;&lt; endl;
    }
}

int main() {
    thread t1(printEvens), t2(printOdds);
    t1.join(); t2.join();
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What is a race condition?</p><ul><li>A) A thread running too fast</li><li>B> Multiple threads accessing shared data without synchronization, causing unpredictable results</li><li>C) Thread deadlock</li><li>D> Thread starvation</li></ul><details><summary>Answer</summary><p><strong>B)</strong> A race condition occurs when multiple threads access/modify shared data concurrently without proper synchronization.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>std::thread</code> creates concurrent threads; <code>join()</code> waits for them.</li><li>Use <code>mutex</code> with <code>lock_guard</code> to prevent race conditions.</li><li><code>std::async</code> runs tasks asynchronously and returns a <code>future</code>.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-move-semantics" class="btn btn-secondary">&larr; Previous: Move Semantics</a>
<a href="/tutorials/cpp/cpp-design-patterns" class="btn btn-primary">Next: Design Patterns &rarr;</a>
</div>
</div>$$,
'Introduction to C++ multithreading: std::thread, mutex, lock_guard, async, and future. Write safe concurrent programs.',
'TUTORIAL', 'CamboFreelance', 'c++,multithreading,concurrency,advanced', NOW(), 45, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Design Patterns', 'cpp-design-patterns',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Design Patterns</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">12 min read</span><span class="lesson-number">Lesson 46 of 50</span></div>
<h2 id="what">What are Design Patterns?</h2>
<p>Design patterns are proven, reusable solutions to common software design problems. We cover three key patterns: <strong>Singleton</strong>, <strong>Factory</strong>, and <strong>Observer</strong>.</p>

<h2 id="singleton">Singleton Pattern</h2>
<p>Ensures only one instance of a class exists. Thread-safe using C++11 static initialization:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Logger {
  private:
    Logger() { cout &lt;&lt; "Logger created\n"; }
    Logger(const Logger&amp;) = delete;
    Logger&amp; operator=(const Logger&amp;) = delete;

  public:
    static Logger&amp; getInstance() {
        static Logger instance;   // created once, thread-safe in C++11
        return instance;
    }
    void log(const string&amp; msg) { cout &lt;&lt; "[LOG] " &lt;&lt; msg &lt;&lt; endl; }
};

int main() {
    Logger::getInstance().log("App started");
    Logger::getInstance().log("Processing...");
    return 0;
}</code></pre>

<h2 id="factory">Factory Pattern</h2>
<p>Creates objects without specifying the exact class — decouples creation from use:</p>
<pre><code class="language-cpp">class Button { public: virtual void render() = 0; virtual ~Button(){} };
class WinButton : public Button { public: void render() override { cout &lt;&lt; "[Windows Button]" &lt;&lt; endl; } };
class MacButton : public Button { public: void render() override { cout &lt;&lt; "[Mac Button]" &lt;&lt; endl; } };

class ButtonFactory {
  public:
    static unique_ptr&lt;Button&gt; create(const string&amp; type) {
        if (type == "windows") return make_unique&lt;WinButton&gt;();
        if (type == "mac")     return make_unique&lt;MacButton&gt;();
        throw invalid_argument("Unknown button type");
    }
};

auto btn = ButtonFactory::create("mac");
btn-&gt;render();   // [Mac Button]</code></pre>

<h2 id="observer">Observer Pattern</h2>
<p>Defines a one-to-many dependency — when one object changes, all dependents are notified:</p>
<pre><code class="language-cpp">class Observer { public: virtual void update(int value) = 0; };

class EventSource {
    vector&lt;Observer*&gt; observers;
    int value = 0;
  public:
    void subscribe(Observer* o)   { observers.push_back(o); }
    void setValue(int v) {
        value = v;
        for (auto o : observers) o-&gt;update(v);
    }
};

class Display : public Observer {
    string name;
  public:
    Display(string n) : name(n) {}
    void update(int v) override {
        cout &lt;&lt; name &lt;&lt; " received: " &lt;&lt; v &lt;&lt; endl;
    }
};</code></pre>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does the Singleton pattern guarantee?</p><ul><li>A) Fast object creation</li><li>B> Only one instance of the class exists</li><li>C) Thread safety by default</li><li>D> Multiple factory methods</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Singleton ensures only one instance is ever created.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><strong>Singleton</strong> — one shared instance, lazy initialization.</li><li><strong>Factory</strong> — decouple object creation from usage.</li><li><strong>Observer</strong> — event-driven notification to multiple subscribers.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-multithreading" class="btn btn-secondary">&larr; Previous: Multithreading</a>
<a href="/tutorials/cpp/cpp-memory-management" class="btn btn-primary">Next: Memory Management &rarr;</a>
</div>
</div>$$,
'Learn three essential C++ design patterns: Singleton for unique instances, Factory for decoupled creation, Observer for event notifications.',
'TUTORIAL', 'CamboFreelance', 'c++,design-patterns,singleton,factory,advanced', NOW(), 46, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Memory Management', 'cpp-memory-management',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Memory Management</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">11 min read</span><span class="lesson-number">Lesson 47 of 50</span></div>
<h2 id="stack-heap">Stack vs Heap</h2>
<table><thead><tr><th>Feature</th><th>Stack</th><th>Heap</th></tr></thead><tbody>
<tr><td>Allocation</td><td>Automatic</td><td>Manual (new/delete) or smart pointers</td></tr>
<tr><td>Speed</td><td>Very fast</td><td>Slower</td></tr>
<tr><td>Size</td><td>Limited (~1–8 MB)</td><td>Large (limited by RAM)</td></tr>
<tr><td>Lifetime</td><td>Scope-bound</td><td>Until freed</td></tr>
</tbody></table>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

int main() {
    // Stack allocation — automatic
    int stackVar = 42;

    // Heap allocation — manual
    int* heapVar = new int(42);
    cout &lt;&lt; *heapVar &lt;&lt; endl;
    delete heapVar;   // must free!
    heapVar = nullptr;

    // Heap array
    int* arr = new int[10];
    for (int i = 0; i &lt; 10; i++) arr[i] = i * 2;
    delete[] arr;   // use delete[] for arrays!
    return 0;
}</code></pre>

<h2 id="raii">RAII — Resource Acquisition Is Initialization</h2>
<p>The best memory management pattern: acquire resources in constructors, release in destructors. Smart pointers implement RAII automatically.</p>
<pre><code class="language-cpp">class FileHandle {
  private:
    FILE* file;
  public:
    FileHandle(const char* path) { file = fopen(path, "r"); }
    ~FileHandle()                { if (file) fclose(file); }  // auto-close!
    FILE* get()                  { return file; }
};</code></pre>

<h2 id="leaks">Detecting Memory Leaks</h2>
<pre><code class="language-cpp">// Leak — every new needs a matching delete
int* p = new int(5);
// forgot: delete p;  &lt;-- leak!

// Fix: use smart pointers
auto p = make_unique&lt;int&gt;(5);
// auto-freed when p goes out of scope</code></pre>
<p>Tools: <strong>Valgrind</strong> (Linux/macOS), <strong>AddressSanitizer</strong> (<code>-fsanitize=address</code>), Visual Studio Diagnostic Tools.</p>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Implement a RAII class <code>MemoryBlock</code> that allocates a dynamic array of N ints in its constructor and frees it in its destructor. Use it to store and print 5 values.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class MemoryBlock {
  private:
    int* data;
    int  size;
  public:
    MemoryBlock(int n) : size(n), data(new int[n]) { cout &lt;&lt; "Allocated\n"; }
    ~MemoryBlock() { delete[] data; cout &lt;&lt; "Freed\n"; }
    int&amp; operator[](int i) { return data[i]; }
    int  getSize() { return size; }
};

int main() {
    MemoryBlock block(5);
    for (int i = 0; i &lt; block.getSize(); i++) block[i] = i * 10;
    for (int i = 0; i &lt; block.getSize(); i++) cout &lt;&lt; block[i] &lt;&lt; " ";
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What keyword deallocates a heap array?</p><ul><li>A) <code>delete ptr</code></li><li>B> <code>free(ptr)</code></li><li>C) <code>delete[] ptr</code></li><li>D> <code>remove(ptr)</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>delete[]</code></strong> — use <code>delete[]</code> for arrays allocated with <code>new[]</code>.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Stack is fast and automatic; heap is flexible but must be managed.</li><li>Always pair <code>new</code> with <code>delete</code>, and <code>new[]</code> with <code>delete[]</code>.</li><li>RAII wraps resources in objects with constructors/destructors.</li><li>Prefer smart pointers to eliminate memory leaks entirely.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-design-patterns" class="btn btn-secondary">&larr; Previous: Design Patterns</a>
<a href="/tutorials/cpp/cpp17-features" class="btn btn-primary">Next: C++17 Features &rarr;</a>
</div>
</div>$$,
'Deep dive into C++ memory management: stack vs heap, new/delete, RAII pattern, detecting memory leaks with modern tools.',
'TUTORIAL', 'CamboFreelance', 'c++,memory-management,raii,advanced', NOW(), 47, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++17 Features', 'cpp17-features',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; C++17 Features</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">10 min read</span><span class="lesson-number">Lesson 48 of 50</span></div>
<h2 id="what">Key C++17 Features</h2>
<p>C++17 added powerful features that make code cleaner and more expressive. Compile with <code>-std=c++17</code>.</p>

<h2 id="structured-bindings">Structured Bindings</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;map&gt;
#include &lt;tuple&gt;
using namespace std;

int main() {
    // Decompose pair
    map&lt;string,int&gt; scores = {{"Alice",95},{"Bob",82}};
    for (auto&amp; [name, score] : scores) {
        cout &lt;&lt; name &lt;&lt; ": " &lt;&lt; score &lt;&lt; endl;
    }

    // Decompose tuple
    auto [x, y, z] = make_tuple(1, 2.5, "hello");
    cout &lt;&lt; x &lt;&lt; " " &lt;&lt; y &lt;&lt; " " &lt;&lt; z &lt;&lt; endl;
    return 0;
}</code></pre>

<h2 id="optional">std::optional</h2>
<p>Represents a value that may or may not be present — safer than returning -1 or null:</p>
<pre><code class="language-cpp">#include &lt;optional&gt;

optional&lt;int&gt; divide(int a, int b) {
    if (b == 0) return nullopt;
    return a / b;
}

auto result = divide(10, 2);
if (result.has_value())
    cout &lt;&lt; "Result: " &lt;&lt; *result &lt;&lt; endl;  // 5

auto bad = divide(5, 0);
cout &lt;&lt; bad.value_or(-1) &lt;&lt; endl;           // -1</code></pre>

<h2 id="if-init">if with Initializer</h2>
<pre><code class="language-cpp">map&lt;string,int&gt; m = {{"key", 42}};

if (auto it = m.find("key"); it != m.end()) {
    cout &lt;&lt; "Found: " &lt;&lt; it-&gt;second &lt;&lt; endl;
}
// 'it' not visible outside the if</code></pre>

<h2 id="filesystem">std::filesystem</h2>
<pre><code class="language-cpp">#include &lt;filesystem&gt;
namespace fs = filesystem;

fs::create_directory("output");
for (const auto&amp; entry : fs::directory_iterator(".")) {
    cout &lt;&lt; entry.path() &lt;&lt; endl;
}</code></pre>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does <code>std::optional&lt;int&gt;</code> represent?</p><ul><li>A) An int that can be negative</li><li>B> A value that might or might not be present</li><li>C) A thread-safe integer</li><li>D> An optional parameter</li></ul><details><summary>Answer</summary><p><strong>B)</strong> <code>std::optional</code> holds either a value or <code>nullopt</code>.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Structured bindings: <code>auto [a, b] = pair</code> — unpack tuples and maps cleanly.</li><li><code>std::optional</code>: express "value or nothing" without magic sentinel values.</li><li><code>if (init; cond)</code>: scoped variable in if condition.</li><li><code>std::filesystem</code>: portable file and directory operations.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-memory-management" class="btn btn-secondary">&larr; Previous: Memory Management</a>
<a href="/tutorials/cpp/cpp20-features" class="btn btn-primary">Next: C++20 Features &rarr;</a>
</div>
</div>$$,
'Explore C++17 features: structured bindings, std::optional, if initializers, std::filesystem, and if constexpr.',
'TUTORIAL', 'CamboFreelance', 'c++,cpp17,modern-cpp,advanced', NOW(), 48, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++20 Features', 'cpp20-features',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; C++20 Features</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">11 min read</span><span class="lesson-number">Lesson 49 of 50</span></div>
<h2 id="what">Key C++20 Features</h2>
<p>C++20 is one of the biggest C++ updates. Compile with <code>-std=c++20</code>.</p>

<h2 id="concepts">Concepts — Constrained Templates</h2>
<p>Concepts allow you to constrain template parameters with named requirements, producing much better error messages:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;concepts&gt;
using namespace std;

template &lt;typename T&gt;
concept Numeric = is_arithmetic_v&lt;T&gt;;

template &lt;Numeric T&gt;
T square(T x) { return x * x; }

int main() {
    cout &lt;&lt; square(5)    &lt;&lt; endl;    // 25
    cout &lt;&lt; square(3.14) &lt;&lt; endl;   // 9.8596
    // square("text");   // compile error — not Numeric
    return 0;
}</code></pre>

<h2 id="ranges">Ranges</h2>
<p>The Ranges library modernizes algorithms with composable, lazy views:</p>
<pre><code class="language-cpp">#include &lt;ranges&gt;
#include &lt;vector&gt;
#include &lt;iostream&gt;
using namespace std;

int main() {
    vector&lt;int&gt; nums = {1,2,3,4,5,6,7,8,9,10};

    // Pipeline: filter even, square, take first 3
    auto result = nums
        | views::filter([](int n) { return n % 2 == 0; })
        | views::transform([](int n) { return n * n; })
        | views::take(3);

    for (int n : result) cout &lt;&lt; n &lt;&lt; " ";  // 4 16 36
    cout &lt;&lt; endl;
    return 0;
}</code></pre>

<h2 id="coroutines">Coroutines (Overview)</h2>
<p>Coroutines are functions that can be suspended and resumed, enabling cooperative multitasking and generators without callbacks:</p>
<pre><code class="language-cpp">// Coroutines require co_await, co_yield, co_return keywords
// and a coroutine framework (std::generator in C++23)
// This pattern enables:
//   - Async I/O without threads
//   - Lazy generators
//   - Stateful state machines</code></pre>

<h2 id="modules">Modules (Overview)</h2>
<p>Modules replace header files with a faster, cleaner compilation model:</p>
<pre><code class="language-cpp">// math.cppm (module file)
export module math;
export int add(int a, int b) { return a + b; }

// main.cpp
import math;
int main() { return add(3, 4); }</code></pre>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What do C++20 Concepts do?</p><ul><li>A) Replace virtual functions</li><li>B) Constrain template parameters with named requirements</li><li>C> Add runtime type checking</li><li>D> Replace inheritance</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Concepts give template parameters named, readable constraints with better error messages.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><strong>Concepts</strong> — constrained templates with readable type requirements.</li><li><strong>Ranges</strong> — composable, lazy pipelines with <code>|</code> operator.</li><li><strong>Coroutines</strong> — suspendable functions for async and generators.</li><li><strong>Modules</strong> — replace headers with fast, clean compilation units.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp17-features" class="btn btn-secondary">&larr; Previous: C++17 Features</a>
<a href="/tutorials/cpp/cpp-performance-optimization" class="btn btn-primary">Next: Performance Optimization &rarr;</a>
</div>
</div>$$,
'Explore C++20 features: Concepts for constrained templates, Ranges for functional pipelines, Coroutines, and Modules.',
'TUTORIAL', 'CamboFreelance', 'c++,cpp20,concepts,ranges,advanced', NOW(), 49, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Performance Optimization', 'cpp-performance-optimization',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Performance Optimization</nav>
<div class="lesson-meta"><span class="badge badge-advanced">Advanced</span><span class="read-time">12 min read</span><span class="lesson-number">Lesson 50 of 50</span></div>
<h2 id="what">Why Optimize?</h2>
<p>C++ is chosen for performance-critical applications. Understanding where time is spent — and how to reduce it — is the final skill in mastering C++.</p>

<h2 id="profiling">Step 1 — Profile Before Optimizing</h2>
<pre><code class="language-cpp">// Measure execution time using chrono
#include &lt;chrono&gt;
using namespace std::chrono;

auto start = high_resolution_clock::now();
// ... code to measure ...
auto end   = high_resolution_clock::now();
auto ms    = duration_cast&lt;milliseconds&gt;(end - start).count();
cout &lt;&lt; "Elapsed: " &lt;&lt; ms &lt;&lt; "ms" &lt;&lt; endl;</code></pre>
<p>Use profiling tools: <strong>gprof</strong>, <strong>perf</strong> (Linux), <strong>Instruments</strong> (macOS), <strong>VTune</strong> (Intel).</p>

<h2 id="cache">Cache-Friendly Code</h2>
<p>Modern CPUs are much faster than RAM. Access memory sequentially (row-major) to leverage the cache:</p>
<pre><code class="language-cpp">const int N = 1000;
int matrix[N][N];

// Slow — column-major, many cache misses
for (int j = 0; j &lt; N; j++)
    for (int i = 0; i &lt; N; i++)
        matrix[i][j] = 0;

// Fast — row-major, cache-friendly
for (int i = 0; i &lt; N; i++)
    for (int j = 0; j &lt; N; j++)
        matrix[i][j] = 0;</code></pre>

<h2 id="move">Avoid Unnecessary Copies</h2>
<pre><code class="language-cpp">// Bad — copies string
void process(string s) { ... }

// Good — no copy
void process(const string&amp; s) { ... }

// Best for rvalues — steal the string
void process(string&amp;&amp; s) { data = move(s); }</code></pre>

<h2 id="reserve">Reserve Vector Capacity</h2>
<pre><code class="language-cpp">vector&lt;int&gt; v;
v.reserve(1000000);   // pre-allocate — avoids repeated reallocations

for (int i = 0; i &lt; 1000000; i++) v.push_back(i);</code></pre>

<h2 id="compiler">Compiler Optimizations</h2>
<table><thead><tr><th>Flag</th><th>Level</th></tr></thead><tbody>
<tr><td><code>-O0</code></td><td>No optimization (debug)</td></tr>
<tr><td><code>-O1</code></td><td>Basic optimizations</td></tr>
<tr><td><code>-O2</code></td><td>Moderate (recommended release)</td></tr>
<tr><td><code>-O3</code></td><td>Aggressive (may increase binary size)</td></tr>
<tr><td><code>-Ofast</code></td><td>O3 + allow floating-point shortcuts</td></tr>
</tbody></table>

<h2 id="tips">Key Optimization Tips</h2>
<ul>
<li>Prefer <code>emplace_back</code> over <code>push_back</code> to construct in-place.</li>
<li>Use <code>unordered_map</code> instead of <code>map</code> for O(1) avg lookups.</li>
<li>Avoid dynamic allocation in hot loops — allocate once, reuse.</li>
<li>Use <code>const&amp;</code> for read-only parameters of non-trivial types.</li>
<li>Enable Link Time Optimization: <code>-flto</code>.</li>
<li>Mark hot functions <code>inline</code> or <code>__attribute__((hot))</code>.</li>
</ul>

<div class="exercise-box" id="exercise">
<h2>Final Exercise</h2>
<p>Benchmark two approaches to summing 10 million integers: (1) using a plain C array; (2) using a <code>vector</code> with <code>reserve()</code>. Measure and compare the time using <code>chrono</code>.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;chrono&gt;
#include &lt;numeric&gt;
using namespace std;
using namespace chrono;

int main() {
    const int N = 10000000;

    // Array approach
    auto t1 = high_resolution_clock::now();
    long long sum1 = 0;
    int* arr = new int[N];
    for (int i = 0; i &lt; N; i++) arr[i] = i;
    for (int i = 0; i &lt; N; i++) sum1 += arr[i];
    delete[] arr;
    auto t2 = high_resolution_clock::now();
    cout &lt;&lt; "Array sum: "  &lt;&lt; sum1 &lt;&lt; " in " &lt;&lt; duration_cast&lt;milliseconds&gt;(t2-t1).count() &lt;&lt; "ms\n";

    // Vector approach
    auto t3 = high_resolution_clock::now();
    vector&lt;int&gt; v; v.reserve(N);
    for (int i = 0; i &lt; N; i++) v.push_back(i);
    long long sum2 = accumulate(v.begin(), v.end(), 0LL);
    auto t4 = high_resolution_clock::now();
    cout &lt;&lt; "Vector sum: " &lt;&lt; sum2 &lt;&lt; " in " &lt;&lt; duration_cast&lt;milliseconds&gt;(t4-t3).count() &lt;&lt; "ms\n";
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>Why is row-major iteration faster than column-major for 2D arrays?</p><ul><li>A) Row-major uses less memory</li><li>B> Column-major triggers more cache misses because memory is accessed non-sequentially</li><li>C) Row-major uses fewer loops</li><li>D> The compiler automatically converts column-major</li></ul><details><summary>Answer</summary><p><strong>B)</strong> C++ stores 2D arrays in row-major order. Column-major access skips over cache lines, causing frequent cache misses and slowdowns.</p></details></li>
</ol></div>

<h2 id="summary">Summary &amp; Congratulations!</h2>
<p>You have completed the full C++ Tutorial Series — from Hello World to performance optimization!</p>
<ul>
<li>Profile first — optimize the bottleneck, not random code.</li>
<li>Write cache-friendly, sequential memory access patterns.</li>
<li>Avoid copies: use <code>const&amp;</code>, <code>&amp;&amp;</code>, and <code>std::move()</code>.</li>
<li>Pre-allocate with <code>reserve()</code> on vectors.</li>
<li>Use compiler optimization flags in release builds (<code>-O2</code> or <code>-O3</code>).</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp20-features" class="btn btn-secondary">&larr; Previous: C++20 Features</a>
<a href="/tutorials/cpp" class="btn btn-primary">Back to C++ Tutorials Home</a>
</div>
</div>$$,
'Complete your C++ journey with performance optimization: profiling, cache-friendly code, avoiding copies, vector reserve, and compiler flags.',
'TUTORIAL', 'CamboFreelance', 'c++,performance,optimization,advanced', NOW(), 50, 'PUBLISHED', 'ACT', 'SYS'
);
