-- C++ Tutorial Series — Intermediate Articles 32-40

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Namespaces', 'cpp-namespaces',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Namespaces</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 32 of 50</span></div>
<h2 id="what">What is a Namespace?</h2>
<p>A <strong>namespace</strong> groups related identifiers to prevent name collisions. This is how <code>std::cout</code> is organized — everything in the C++ standard library lives in the <code>std</code> namespace.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

namespace Math {
    const double PI = 3.14159265;
    double square(double x)  { return x * x; }
    double cube(double x)    { return x * x * x; }
}

namespace Geometry {
    double circleArea(double r) { return Math::PI * Math::square(r); }
}

int main() {
    cout &lt;&lt; Math::PI              &lt;&lt; endl;   // 3.14159265
    cout &lt;&lt; Math::square(4)      &lt;&lt; endl;   // 16
    cout &lt;&lt; Geometry::circleArea(5) &lt;&lt; endl; // 78.5398
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">3.14159265
16
78.5398</code></pre>

<h2 id="using">using Declarations</h2>
<pre><code class="language-cpp">using Math::PI;       // import specific name
using namespace Math; // import all names (use sparingly)
cout &lt;&lt; PI;           // no prefix needed</code></pre>

<h2 id="nested">Nested Namespaces (C++17)</h2>
<pre><code class="language-cpp">namespace App::Utils::String {
    bool isEmpty(const string&amp; s) { return s.empty(); }
}
// Use: App::Utils::String::isEmpty("test")</code></pre>

<div class="note-box"><strong>Best Practice:</strong> Avoid <code>using namespace std;</code> in header files — it pollutes the global namespace of every file that includes the header. It is fine in .cpp files.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create two namespaces: <code>Imperial</code> (with <code>milesPerHour</code> unit) and <code>Metric</code> (with <code>kmPerHour</code> unit). Write a conversion function in each that converts to the other. Test both.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

namespace Imperial {
    double toKph(double mph) { return mph * 1.60934; }
}
namespace Metric {
    double toMph(double kph) { return kph / 1.60934; }
}

int main() {
    cout &lt;&lt; "60 mph = " &lt;&lt; Imperial::toKph(60) &lt;&lt; " kph" &lt;&lt; endl;
    cout &lt;&lt; "100 kph = " &lt;&lt; Metric::toMph(100) &lt;&lt; " mph" &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What problem do namespaces solve?</p><ul><li>A) Slow compilation</li><li>B) Name collisions between identifiers from different libraries</li><li>C) Memory leaks</li><li>D) Type errors</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Namespaces group identifiers to prevent conflicts.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Namespaces group identifiers to avoid name conflicts.</li><li>Access members with <code>NamespaceName::identifier</code>.</li><li>Avoid <code>using namespace</code> in headers.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-exception-handling" class="btn btn-secondary">&larr; Previous: Exception Handling</a>
<a href="/tutorials/cpp/cpp-structures" class="btn btn-primary">Next: Structures &rarr;</a>
</div>
</div>$$,
'Learn C++ namespaces to organize code and prevent name collisions. Create, nest, and use namespaces with the using directive.',
'TUTORIAL', 'CamboFreelance', 'c++,namespaces,intermediate', NOW(), 32, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Structures', 'cpp-structures',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Structures</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 33 of 50</span></div>
<h2 id="what">What is a struct?</h2>
<p>A <strong>struct</strong> is like a class but with <code>public</code> as the default access specifier. Structs are ideal for plain data containers without complex behavior.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

struct Point {
    double x;
    double y;

    double distanceTo(const Point&amp; other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return sqrt(dx*dx + dy*dy);
    }
};

int main() {
    Point p1 = {0.0, 0.0};
    Point p2 = {3.0, 4.0};
    cout &lt;&lt; "Distance: " &lt;&lt; p1.distanceTo(p2) &lt;&lt; endl;   // 5
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Distance: 5</code></pre>

<h2 id="vs-class">struct vs class</h2>
<table><thead><tr><th>Feature</th><th>struct</th><th>class</th></tr></thead><tbody>
<tr><td>Default access</td><td>public</td><td>private</td></tr>
<tr><td>Default inheritance</td><td>public</td><td>private</td></tr>
<tr><td>Can have methods</td><td>Yes</td><td>Yes</td></tr>
<tr><td>Best for</td><td>Simple data bundles (POD)</td><td>Complex OOP with encapsulation</td></tr>
</tbody></table>

<h2 id="array">Array of Structs</h2>
<pre><code class="language-cpp">struct Student {
    string name;
    int    score;
};

Student students[3] = {
    {"Alice", 95},
    {"Bob",   82},
    {"Carol", 78}
};

for (const Student&amp; s : students) {
    cout &lt;&lt; s.name &lt;&lt; ": " &lt;&lt; s.score &lt;&lt; endl;
}</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Book</code> struct with <code>title</code>, <code>author</code>, <code>year</code>, and <code>price</code>. Declare an array of 3 books and print the title of the most expensive one.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

struct Book {
    string title, author;
    int year;
    double price;
};

int main() {
    Book books[] = {
        {"C++ Primer", "Lippman", 2012, 49.99},
        {"Effective C++", "Meyers", 2005, 39.99},
        {"The C++ Programming Language", "Stroustrup", 2013, 55.00}
    };
    Book& mostExpensive = books[0];
    for (Book& b : books)
        if (b.price &gt; mostExpensive.price) mostExpensive = b;
    cout &lt;&lt; "Most expensive: " &lt;&lt; mostExpensive.title &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What is the default access specifier for struct members?</p><ul><li>A) private</li><li>B) protected</li><li>C) public</li><li>D) internal</li></ul><details><summary>Answer</summary><p><strong>C) public</strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>Structs group related data; members are public by default.</li><li>Structs can have constructors and methods like classes.</li><li>Use structs for simple data bundles; classes for complex OOP.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-namespaces" class="btn btn-secondary">&larr; Previous: Namespaces</a>
<a href="/tutorials/cpp/cpp-enumerations" class="btn btn-primary">Next: Enumerations &rarr;</a>
</div>
</div>$$,
'Learn C++ structures (struct): group related data, compare struct vs class, and build arrays of structs for real-world data.',
'TUTORIAL', 'CamboFreelance', 'c++,structures,struct,intermediate', NOW(), 33, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Enumerations', 'cpp-enumerations',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Enumerations</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 34 of 50</span></div>
<h2 id="enum">enum</h2>
<p>An <strong>enumeration</strong> defines a named set of integer constants, making code more readable than magic numbers.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

enum Direction { NORTH, EAST, SOUTH, WEST };
// NORTH=0, EAST=1, SOUTH=2, WEST=3

int main() {
    Direction d = NORTH;
    if (d == NORTH) cout &lt;&lt; "Heading North!" &lt;&lt; endl;

    // Custom values
    enum Status { PENDING=1, ACTIVE=2, INACTIVE=3, DELETED=99 };
    Status s = ACTIVE;
    cout &lt;&lt; "Status code: " &lt;&lt; s &lt;&lt; endl;   // 2
    return 0;
}</code></pre>

<h2 id="enum-class">enum class (C++11 — Scoped Enum)</h2>
<p><code>enum class</code> prevents name collisions and implicit conversions to int:</p>
<pre><code class="language-cpp">enum class Color { RED, GREEN, BLUE };
enum class Fruit { RED, APPLE, BANANA };   // RED is separate from Color::RED

Color c = Color::RED;   // must use scope
// int x = Color::RED;  // ERROR — no implicit conversion

switch (c) {
    case Color::RED:   cout &lt;&lt; "Red"   &lt;&lt; endl; break;
    case Color::GREEN: cout &lt;&lt; "Green" &lt;&lt; endl; break;
    case Color::BLUE:  cout &lt;&lt; "Blue"  &lt;&lt; endl; break;
}</code></pre>

<div class="note-box"><strong>Best Practice:</strong> Prefer <code>enum class</code> over plain <code>enum</code> in modern C++. It avoids pollution of the enclosing scope and provides type safety.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create an <code>enum class Season</code> with SPRING, SUMMER, AUTUMN, WINTER. Write a function that takes a Season and returns the average temperature for that season as a string.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

enum class Season { SPRING, SUMMER, AUTUMN, WINTER };

string getTemp(Season s) {
    switch (s) {
        case Season::SPRING: return "~20°C";
        case Season::SUMMER: return "~35°C";
        case Season::AUTUMN: return "~15°C";
        case Season::WINTER: return "~5°C";
    }
    return "Unknown";
}

int main() {
    cout &lt;&lt; "Summer: " &lt;&lt; getTemp(Season::SUMMER) &lt;&lt; endl;
    cout &lt;&lt; "Winter: " &lt;&lt; getTemp(Season::WINTER) &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What is the default starting value of the first enum member?</p><ul><li>A) 1</li><li>B) -1</li><li>C) 0</li><li>D) Undefined</li></ul><details><summary>Answer</summary><p><strong>C) 0</strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>enum</code> creates named integer constants.</li><li><code>enum class</code> (scoped enum) prevents name collisions and is type-safe.</li><li>Always prefer <code>enum class</code> in modern C++ code.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-structures" class="btn btn-secondary">&larr; Previous: Structures</a>
<a href="/tutorials/cpp/cpp-stl-introduction" class="btn btn-primary">Next: STL Introduction &rarr;</a>
</div>
</div>$$,
'Learn C++ enumerations: create named constants with enum, use scoped enum class for type safety, and apply them in switch statements.',
'TUTORIAL', 'CamboFreelance', 'c++,enumerations,enum,intermediate', NOW(), 34, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ STL Introduction', 'cpp-stl-introduction',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; STL Introduction</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 35 of 50</span></div>
<h2 id="what">What is the STL?</h2>
<p>The <strong>Standard Template Library (STL)</strong> is a powerful set of ready-to-use C++ template classes and functions. It provides generic containers, algorithms, and iterators.</p>
<table><thead><tr><th>Component</th><th>Description</th><th>Examples</th></tr></thead><tbody>
<tr><td>Containers</td><td>Store collections of data</td><td>vector, map, set, queue, stack</td></tr>
<tr><td>Algorithms</td><td>Common operations on containers</td><td>sort, find, count, transform</td></tr>
<tr><td>Iterators</td><td>Navigate through containers</td><td>begin(), end(), rbegin()</td></tr>
</tbody></table>

<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;algorithm&gt;
using namespace std;

int main() {
    vector&lt;int&gt; nums = {5, 3, 8, 1, 9, 2};

    sort(nums.begin(), nums.end());

    for (int n : nums) cout &lt;&lt; n &lt;&lt; " ";
    cout &lt;&lt; endl;

    auto it = find(nums.begin(), nums.end(), 8);
    if (it != nums.end())
        cout &lt;&lt; "Found 8 at position " &lt;&lt; (it - nums.begin()) &lt;&lt; endl;

    cout &lt;&lt; "Count of 3: " &lt;&lt; count(nums.begin(), nums.end(), 3) &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">1 2 3 5 8 9
Found 8 at position 4
Count of 3: 1</code></pre>

<h2 id="algorithms">Common STL Algorithms</h2>
<table><thead><tr><th>Algorithm</th><th>Description</th></tr></thead><tbody>
<tr><td><code>sort()</code></td><td>Sort a range</td></tr>
<tr><td><code>find()</code></td><td>Find first element matching value</td></tr>
<tr><td><code>count()</code></td><td>Count occurrences</td></tr>
<tr><td><code>reverse()</code></td><td>Reverse a range</td></tr>
<tr><td><code>max_element()</code></td><td>Find the maximum element</td></tr>
<tr><td><code>min_element()</code></td><td>Find the minimum element</td></tr>
<tr><td><code>accumulate()</code></td><td>Sum all elements (<code>&lt;numeric&gt;</code>)</td></tr>
</tbody></table>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a vector of 10 integers. Use STL algorithms to: sort it, find the max and min, and calculate the sum.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
#include &lt;algorithm&gt;
#include &lt;numeric&gt;
using namespace std;

int main() {
    vector&lt;int&gt; v = {7,2,9,1,5,3,8,4,6,10};
    sort(v.begin(), v.end());
    cout &lt;&lt; "Min: " &lt;&lt; *min_element(v.begin(), v.end()) &lt;&lt; endl;
    cout &lt;&lt; "Max: " &lt;&lt; *max_element(v.begin(), v.end()) &lt;&lt; endl;
    cout &lt;&lt; "Sum: " &lt;&lt; accumulate(v.begin(), v.end(), 0) &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>Which header contains STL algorithms like sort() and find()?</p><ul><li>A) <code>&lt;vector&gt;</code></li><li>B) <code>&lt;algorithm&gt;</code></li><li>C) <code>&lt;sort&gt;</code></li><li>D) <code>&lt;stl&gt;</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>&lt;algorithm&gt;</code></strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li>STL provides containers (vector, map, set), algorithms, and iterators.</li><li>Include <code>&lt;algorithm&gt;</code> for sort, find, count, reverse, etc.</li><li>All algorithms work with iterators: <code>begin()</code> to <code>end()</code>.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-enumerations" class="btn btn-secondary">&larr; Previous: Enumerations</a>
<a href="/tutorials/cpp/cpp-vectors" class="btn btn-primary">Next: Vectors &rarr;</a>
</div>
</div>$$,
'Introduction to the C++ Standard Template Library (STL): containers, algorithms, and iterators. Learn sort, find, and accumulate.',
'TUTORIAL', 'CamboFreelance', 'c++,stl,algorithms,intermediate', NOW(), 35, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Vectors', 'cpp-vectors',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Vectors</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 36 of 50</span></div>
<h2 id="what">What is a Vector?</h2>
<p>A <code>vector</code> is a <strong>dynamic array</strong> that can grow or shrink at runtime. It is the most commonly used STL container.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
using namespace std;

int main() {
    vector&lt;int&gt; scores;

    scores.push_back(90);
    scores.push_back(85);
    scores.push_back(78);
    scores.push_back(92);

    cout &lt;&lt; "Size: "     &lt;&lt; scores.size()  &lt;&lt; endl;
    cout &lt;&lt; "First: "    &lt;&lt; scores.front() &lt;&lt; endl;
    cout &lt;&lt; "Last: "     &lt;&lt; scores.back()  &lt;&lt; endl;
    cout &lt;&lt; "Index 2: "  &lt;&lt; scores[2]      &lt;&lt; endl;

    scores.pop_back();   // removes last
    cout &lt;&lt; "After pop: " &lt;&lt; scores.size() &lt;&lt; endl;

    for (int s : scores) cout &lt;&lt; s &lt;&lt; " ";
    cout &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Size: 4
First: 90
Last: 92
Index 2: 78
After pop: 3
90 85 78</code></pre>

<h2 id="methods">Key Vector Operations</h2>
<table><thead><tr><th>Method</th><th>Description</th></tr></thead><tbody>
<tr><td><code>push_back(val)</code></td><td>Add to end</td></tr>
<tr><td><code>pop_back()</code></td><td>Remove from end</td></tr>
<tr><td><code>insert(pos, val)</code></td><td>Insert at iterator position</td></tr>
<tr><td><code>erase(pos)</code></td><td>Remove at iterator position</td></tr>
<tr><td><code>size()</code></td><td>Number of elements</td></tr>
<tr><td><code>empty()</code></td><td>True if size == 0</td></tr>
<tr><td><code>clear()</code></td><td>Remove all elements</td></tr>
<tr><td><code>at(i)</code></td><td>Bounds-checked access</td></tr>
</tbody></table>

<pre><code class="language-cpp">vector&lt;string&gt; fruits = {"Apple", "Banana", "Cherry"};
fruits.insert(fruits.begin() + 1, "Blueberry");
fruits.erase(fruits.begin());   // removes "Apple"
for (const string&amp; f : fruits) cout &lt;&lt; f &lt;&lt; " ";
// Blueberry Banana Cherry</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a vector of integers. Read numbers from the user until they enter 0. Then print the average of all entered numbers (excluding 0).</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;vector&gt;
using namespace std;

int main() {
    vector&lt;int&gt; nums;
    int n;
    cout &lt;&lt; "Enter numbers (0 to stop): " &lt;&lt; endl;
    while (cin &gt;&gt; n &amp;&amp; n != 0) nums.push_back(n);

    if (nums.empty()) { cout &lt;&lt; "No numbers entered." &lt;&lt; endl; return 0; }

    double sum = 0;
    for (int x : nums) sum += x;
    cout &lt;&lt; "Average: " &lt;&lt; sum / nums.size() &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>Which method adds an element to the end of a vector?</p><ul><li>A) <code>add()</code></li><li>B> <code>append()</code></li><li>C) <code>push_back()</code></li><li>D> <code>insert_end()</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>push_back()</code></strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>vector</code> is a dynamic array that resizes automatically.</li><li>Key operations: <code>push_back</code>, <code>pop_back</code>, <code>insert</code>, <code>erase</code>, <code>size</code>, <code>clear</code>.</li><li>Prefer <code>at(i)</code> over <code>[i]</code> for bounds-checked access.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-stl-introduction" class="btn btn-secondary">&larr; Previous: STL Introduction</a>
<a href="/tutorials/cpp/cpp-maps" class="btn btn-primary">Next: Maps &rarr;</a>
</div>
</div>$$,
'Master C++ vectors: dynamic arrays that grow and shrink. Learn push_back, pop_back, insert, erase, and iteration patterns.',
'TUTORIAL', 'CamboFreelance', 'c++,vectors,stl,intermediate', NOW(), 36, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Maps', 'cpp-maps',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Maps</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 37 of 50</span></div>
<h2 id="what">What is a Map?</h2>
<p>A <code>map</code> stores <strong>key-value pairs</strong> where each key is unique and automatically sorted. Include <code>&lt;map&gt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;map&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    map&lt;string, int&gt; wordCount;

    wordCount["hello"]  = 3;
    wordCount["world"]  = 2;
    wordCount["cpp"]    = 5;
    wordCount["hello"]++;   // increment existing

    for (const auto&amp; [word, count] : wordCount) {
        cout &lt;&lt; word &lt;&lt; ": " &lt;&lt; count &lt;&lt; endl;
    }
    cout &lt;&lt; "Contains 'cpp': " &lt;&lt; wordCount.count("cpp") &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output (sorted by key):</h3>
<pre><code class="language-text">cpp: 5
hello: 4
world: 2
Contains 'cpp': 1</code></pre>

<h2 id="methods">Map Operations</h2>
<table><thead><tr><th>Operation</th><th>Description</th></tr></thead><tbody>
<tr><td><code>m[key] = val</code></td><td>Insert or update</td></tr>
<tr><td><code>m.at(key)</code></td><td>Access with bounds check</td></tr>
<tr><td><code>m.find(key)</code></td><td>Returns iterator (end() if not found)</td></tr>
<tr><td><code>m.count(key)</code></td><td>1 if key exists, 0 if not</td></tr>
<tr><td><code>m.erase(key)</code></td><td>Remove by key</td></tr>
<tr><td><code>m.size()</code></td><td>Number of entries</td></tr>
</tbody></table>

<pre><code class="language-cpp">map&lt;string, string&gt; capitals;
capitals["Cambodia"] = "Phnom Penh";
capitals["France"]   = "Paris";
capitals["Japan"]    = "Tokyo";

if (capitals.find("Japan") != capitals.end())
    cout &lt;&lt; "Japan capital: " &lt;&lt; capitals["Japan"] &lt;&lt; endl;

capitals.erase("France");
cout &lt;&lt; "Size: " &lt;&lt; capitals.size() &lt;&lt; endl;   // 2</code></pre>

<div class="note-box"><strong>unordered_map:</strong> For faster average O(1) lookup (vs O(log n) for map), use <code>unordered_map</code> from <code>&lt;unordered_map&gt;</code>. It is not sorted but much faster for large datasets.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Read a string of words from the user (space-separated) and count the frequency of each word using a map. Print the word frequency table.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;map&gt;
#include &lt;sstream&gt;
using namespace std;

int main() {
    string line, word;
    cout &lt;&lt; "Enter text: ";
    getline(cin, line);

    map&lt;string, int&gt; freq;
    istringstream iss(line);
    while (iss &gt;&gt; word) freq[word]++;

    for (const auto&amp; [w, c] : freq)
        cout &lt;&lt; w &lt;&lt; ": " &lt;&lt; c &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does <code>map::count(key)</code> return when the key exists?</p><ul><li>A) The value associated with the key</li><li>B) 1</li><li>C) The number of duplicate keys</li><li>D) 0</li></ul><details><summary>Answer</summary><p><strong>B) 1</strong> — since map keys are unique, count is always 0 or 1.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>map&lt;K,V&gt;</code> stores unique sorted key-value pairs.</li><li>Access: <code>m[key]</code> or <code>m.at(key)</code>; search with <code>find()</code> or <code>count()</code>.</li><li>Use <code>unordered_map</code> for O(1) average lookups without ordering.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-vectors" class="btn btn-secondary">&larr; Previous: Vectors</a>
<a href="/tutorials/cpp/cpp-sets" class="btn btn-primary">Next: Sets &rarr;</a>
</div>
</div>$$,
'Learn C++ maps: key-value pair storage, insertion, lookup, deletion, and iteration. Compare map vs unordered_map.',
'TUTORIAL', 'CamboFreelance', 'c++,maps,stl,intermediate', NOW(), 37, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Sets', 'cpp-sets',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Sets</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 38 of 50</span></div>
<h2 id="what">What is a Set?</h2>
<p>A <code>set</code> stores <strong>unique, sorted elements</strong>. Duplicate insertions are silently ignored. Include <code>&lt;set&gt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;set&gt;
using namespace std;

int main() {
    set&lt;int&gt; primes = {2, 3, 5, 7, 11, 13};
    primes.insert(5);    // duplicate — ignored
    primes.insert(17);   // added

    cout &lt;&lt; "Size: " &lt;&lt; primes.size() &lt;&lt; endl;
    for (int p : primes) cout &lt;&lt; p &lt;&lt; " ";
    cout &lt;&lt; endl;

    cout &lt;&lt; "Contains 7:  " &lt;&lt; primes.count(7)  &lt;&lt; endl;
    cout &lt;&lt; "Contains 10: " &lt;&lt; primes.count(10) &lt;&lt; endl;
    primes.erase(3);
    cout &lt;&lt; "After erase 3, size: " &lt;&lt; primes.size() &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Size: 7
2 3 5 7 11 13 17
Contains 7:  1
Contains 10: 0
After erase 3, size: 6</code></pre>

<h2 id="use-cases">Common Use Cases</h2>
<ul>
<li>Removing duplicates from a list</li>
<li>Fast membership testing</li>
<li>Maintaining a sorted unique collection</li>
</ul>
<pre><code class="language-cpp">// Deduplicate a vector using a set
vector&lt;int&gt; nums = {5, 3, 8, 3, 5, 9, 1, 8};
set&lt;int&gt; unique(nums.begin(), nums.end());
for (int n : unique) cout &lt;&lt; n &lt;&lt; " ";
// 1 3 5 8 9</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Read a list of 10 integers. Use a set to find and print only the unique values in sorted order.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;set&gt;
using namespace std;

int main() {
    set&lt;int&gt; unique;
    for (int i = 0; i &lt; 10; i++) {
        int n; cin &gt;&gt; n;
        unique.insert(n);
    }
    cout &lt;&lt; "Unique values: ";
    for (int n : unique) cout &lt;&lt; n &lt;&lt; " ";
    cout &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What happens when you insert a duplicate into a set?</p><ul><li>A) An exception is thrown</li><li>B) The set grows</li><li>C) The duplicate is silently ignored</li><li>D) The old value is replaced</li></ul><details><summary>Answer</summary><p><strong>C)</strong> Sets silently ignore duplicates — each element is stored once.</p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>set&lt;T&gt;</code> stores unique, automatically sorted elements.</li><li>Use <code>insert()</code>, <code>erase()</code>, <code>count()</code>, <code>find()</code>.</li><li>Great for deduplication and fast membership checks.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-maps" class="btn btn-secondary">&larr; Previous: Maps</a>
<a href="/tutorials/cpp/cpp-queues" class="btn btn-primary">Next: Queues &rarr;</a>
</div>
</div>$$,
'Learn C++ sets: store unique sorted elements, test membership, deduplicate collections, and iterate in sorted order.',
'TUTORIAL', 'CamboFreelance', 'c++,sets,stl,intermediate', NOW(), 38, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Queues', 'cpp-queues',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Queues</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 39 of 50</span></div>
<h2 id="what">What is a Queue?</h2>
<p>A <code>queue</code> is a <strong>FIFO</strong> (First-In-First-Out) container. Elements are added at the back and removed from the front — like a ticket line. Include <code>&lt;queue&gt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;queue&gt;
using namespace std;

int main() {
    queue&lt;string&gt; customers;

    customers.push("Alice");    // enqueue
    customers.push("Bob");
    customers.push("Carol");

    cout &lt;&lt; "Queue size: " &lt;&lt; customers.size() &lt;&lt; endl;

    while (!customers.empty()) {
        cout &lt;&lt; "Serving: " &lt;&lt; customers.front() &lt;&lt; endl;
        customers.pop();   // dequeue
    }
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Queue size: 3
Serving: Alice
Serving: Bob
Serving: Carol</code></pre>

<h2 id="methods">Queue Methods</h2>
<table><thead><tr><th>Method</th><th>Description</th></tr></thead><tbody>
<tr><td><code>push(val)</code></td><td>Add to back</td></tr>
<tr><td><code>pop()</code></td><td>Remove from front</td></tr>
<tr><td><code>front()</code></td><td>View front element</td></tr>
<tr><td><code>back()</code></td><td>View back element</td></tr>
<tr><td><code>empty()</code></td><td>True if queue is empty</td></tr>
<tr><td><code>size()</code></td><td>Number of elements</td></tr>
</tbody></table>

<h2 id="priority">Priority Queue</h2>
<p>A <code>priority_queue</code> always serves the highest-priority (largest by default) element first:</p>
<pre><code class="language-cpp">#include &lt;queue&gt;
priority_queue&lt;int&gt; pq;
pq.push(3); pq.push(1); pq.push(5); pq.push(2);
while (!pq.empty()) {
    cout &lt;&lt; pq.top() &lt;&lt; " ";   // 5 3 2 1
    pq.pop();
}</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Simulate a print queue: add 5 print jobs to a queue and process them one by one, printing "Printing: [job]" for each.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;queue&gt;
using namespace std;

int main() {
    queue&lt;string&gt; printQueue;
    printQueue.push("Report.pdf");
    printQueue.push("Invoice.docx");
    printQueue.push("Photo.jpg");
    printQueue.push("Presentation.pptx");
    printQueue.push("Letter.txt");

    while (!printQueue.empty()) {
        cout &lt;&lt; "Printing: " &lt;&lt; printQueue.front() &lt;&lt; endl;
        printQueue.pop();
    }
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does FIFO stand for?</p><ul><li>A) First In, Fast Out</li><li>B) First In, First Out</li><li>C) Final In, First Out</li><li>D) First In, Full Out</li></ul><details><summary>Answer</summary><p><strong>B) First In, First Out</strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>queue</code> is FIFO: push to back, pop from front.</li><li>Methods: <code>push</code>, <code>pop</code>, <code>front</code>, <code>back</code>, <code>empty</code>, <code>size</code>.</li><li><code>priority_queue</code> processes the highest-priority element first.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-sets" class="btn btn-secondary">&larr; Previous: Sets</a>
<a href="/tutorials/cpp/cpp-stacks" class="btn btn-primary">Next: Stacks &rarr;</a>
</div>
</div>$$,
'Learn the C++ queue container: FIFO operations, print queue simulation, and the priority_queue for ordered processing.',
'TUTORIAL', 'CamboFreelance', 'c++,queues,stl,intermediate', NOW(), 39, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(), 'C++ Stacks', 'cpp-stacks',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Stacks</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 40 of 50</span></div>
<h2 id="what">What is a Stack?</h2>
<p>A <code>stack</code> is a <strong>LIFO</strong> (Last-In-First-Out) container. Elements are pushed and popped from the same end — the "top". Include <code>&lt;stack&gt;</code>.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;stack&gt;
using namespace std;

int main() {
    stack&lt;int&gt; s;
    s.push(10);
    s.push(20);
    s.push(30);

    cout &lt;&lt; "Top: " &lt;&lt; s.top() &lt;&lt; endl;   // 30
    cout &lt;&lt; "Size: " &lt;&lt; s.size() &lt;&lt; endl; // 3

    while (!s.empty()) {
        cout &lt;&lt; s.top() &lt;&lt; " ";
        s.pop();
    }
    cout &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Top: 30
Size: 3
30 20 10</code></pre>

<h2 id="use-cases">Real-World Use Cases</h2>
<ul>
<li><strong>Undo/Redo</strong> in text editors</li>
<li><strong>Browser back button</strong> (page history)</li>
<li><strong>Function call stack</strong> in program execution</li>
<li><strong>Expression evaluation</strong> (balanced parentheses, RPN)</li>
</ul>

<pre><code class="language-cpp">// Check balanced parentheses using a stack
string expr = "{[()]}";
stack&lt;char&gt; st;
bool balanced = true;

for (char c : expr) {
    if (c=='(' || c=='[' || c=='{') {
        st.push(c);
    } else {
        if (st.empty()) { balanced = false; break; }
        char top = st.top(); st.pop();
        if ((c==')' &amp;&amp; top!='(') || (c==']' &amp;&amp; top!='[') || (c=='}' &amp;&amp; top!='{'))
            { balanced = false; break; }
    }
}
if (!st.empty()) balanced = false;
cout &lt;&lt; (balanced ? "Balanced" : "Not balanced") &lt;&lt; endl;  // Balanced</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Use a stack to reverse a string. Push each character onto a stack, then pop them off to form the reversed string.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;stack&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    string input = "Hello, World!";
    stack&lt;char&gt; s;
    for (char c : input) s.push(c);
    string reversed = "";
    while (!s.empty()) { reversed += s.top(); s.pop(); }
    cout &lt;&lt; reversed &lt;&lt; endl;   // !dlroW ,olleH
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz"><h2>Quiz</h2><ol>
<li><p>What does LIFO mean?</p><ul><li>A) Last In, Fast Out</li><li>B) Last In, First Out</li><li>C) Linear In, First Out</li><li>D) List In, Final Out</li></ul><details><summary>Answer</summary><p><strong>B) Last In, First Out</strong></p></details></li>
</ol></div>

<h2 id="summary">Summary</h2>
<ul><li><code>stack</code> is LIFO: push and pop from the top.</li><li>Methods: <code>push</code>, <code>pop</code>, <code>top</code>, <code>empty</code>, <code>size</code>.</li><li>Classic use cases: undo, browser history, expression parsing.</li></ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-queues" class="btn btn-secondary">&larr; Previous: Queues</a>
<a href="/tutorials/cpp/cpp-smart-pointers" class="btn btn-primary">Next: Smart Pointers &rarr;</a>
</div>
</div>$$,
'Learn the C++ stack container: LIFO operations with push, pop, top. Real-world examples including string reversal and bracket matching.',
'TUTORIAL', 'CamboFreelance', 'c++,stacks,stl,intermediate', NOW(), 40, 'PUBLISHED', 'ACT', 'SYS'
);
