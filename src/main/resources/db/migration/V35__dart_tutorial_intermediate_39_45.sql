-- Section 7: Collections (Lessons 39–45)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Lists in Dart', 'dart-lists',
    $dart_39$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a List?</a></li><li><a href="#creating">Creating Lists</a></li><li><a href="#access">Accessing Elements</a></li><li><a href="#methods">Common Methods</a></li><li><a href="#example">Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a List?</h2>
<p>A <code>List</code> is an ordered collection of items. Lists allow duplicate values and maintain insertion order. They are the most commonly used collection type in Dart.</p>
<h2 id="creating">Creating Lists</h2>
<pre><code>// Fixed-type list
List&lt;int&gt; numbers = [1, 2, 3, 4, 5];
List&lt;String&gt; names = ['Alice', 'Bob', 'Carol'];

// Type inference
var fruits = ['apple', 'banana', 'cherry'];

// Empty list
List&lt;double&gt; scores = [];

// Fixed-length list (cannot add/remove)
var fixed = List&lt;int&gt;.filled(5, 0); // [0, 0, 0, 0, 0]

// Generated list
var squares = List&lt;int&gt;.generate(5, (i) => (i + 1) * (i + 1));
// [1, 4, 9, 16, 25]</code></pre>
<h2 id="access">Accessing Elements</h2>
<pre><code>var fruits = ['apple', 'banana', 'cherry'];
print(fruits[0]);          // apple (first)
print(fruits.last);        // cherry (last)
print(fruits.length);      // 3
print(fruits.isEmpty);     // false
print(fruits.isNotEmpty);  // true</code></pre>
<h2 id="methods">Common Methods</h2>
<table><thead><tr><th>Method</th><th>Description</th></tr></thead><tbody>
<tr><td><code>add(item)</code></td><td>Add one item to the end</td></tr>
<tr><td><code>addAll(list)</code></td><td>Add all items from another list</td></tr>
<tr><td><code>insert(i, item)</code></td><td>Insert at index i</td></tr>
<tr><td><code>remove(item)</code></td><td>Remove first occurrence</td></tr>
<tr><td><code>removeAt(i)</code></td><td>Remove item at index i</td></tr>
<tr><td><code>contains(item)</code></td><td>Check if item exists</td></tr>
<tr><td><code>indexOf(item)</code></td><td>Find index of item</td></tr>
<tr><td><code>sort()</code></td><td>Sort in place</td></tr>
<tr><td><code>reversed</code></td><td>Iterable in reverse order</td></tr>
<tr><td><code>sublist(start, end)</code></td><td>Extract sub-list</td></tr>
</tbody></table>
<h2 id="example">Example</h2>
<pre><code>void main() {
  List&lt;String&gt; tasks = ['Write code', 'Test code', 'Deploy'];

  tasks.add('Monitor');
  tasks.insert(0, 'Plan');
  print(tasks);  // [Plan, Write code, Test code, Deploy, Monitor]

  tasks.remove('Test code');
  print(tasks.length);      // 4

  print(tasks.contains('Deploy'));  // true
  print(tasks.indexOf('Deploy'));   // 2

  var numbers = [3, 1, 4, 1, 5, 9, 2, 6];
  numbers.sort();
  print(numbers);           // [1, 1, 2, 3, 4, 5, 6, 9]
  print(numbers.sublist(2, 5)); // [2, 3, 4]
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>[Plan, Write code, Test code, Deploy, Monitor]
4
true
2
[1, 1, 2, 3, 4, 5, 6, 9]
[2, 3, 4]</pre></div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Create a list of 5 favourite movies, add two more, then print only the first 3.</li>
<li>Sort a list of numbers in descending order.</li>
<li>Find the maximum value in a list without using any built-in max method.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you access the last element of a Dart list?</strong></p>
<ul><li>A) list[-1]</li><li>B) list.last ✅</li><li>C) list.end()</li><li>D) list[list.length]</li></ul>
<p><strong>2. Which method removes an element by value (not index)?</strong></p>
<ul><li>A) removeAt()</li><li>B) delete()</li><li>C) remove() ✅</li><li>D) pop()</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Dart Lists are ordered, indexed, and allow duplicates. Create them with literal syntax <code>[...]</code> or <code>List.generate()</code>. Key methods: <code>add()</code>, <code>remove()</code>, <code>sort()</code>, <code>contains()</code>, <code>sublist()</code>. Access elements by index (zero-based) or with <code>.first</code>/<code>.last</code>.</p>
    $dart_39$,
    'Master Dart Lists — ordered collections with add, remove, sort, and search operations. Learn indexing, common methods, and list generation.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,collections', NOW(), 39, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Sets in Dart', 'dart-sets',
    $dart_40$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Set?</a></li><li><a href="#creating">Creating Sets</a></li><li><a href="#operations">Set Operations</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Set?</h2>
<p>A <code>Set</code> is an unordered collection of <strong>unique</strong> values. Duplicate items are automatically ignored. Sets are ideal when you need uniqueness or fast membership checks.</p>
<h2 id="creating">Creating Sets</h2>
<pre><code>Set&lt;int&gt; primes = {2, 3, 5, 7, 11};
Set&lt;String&gt; colors = {'red', 'green', 'blue'};

// Adding a duplicate — silently ignored
colors.add('red');
print(colors.length);  // 3 (still)

// Empty set — must use Set(), not {} (that creates a Map)
var empty = &lt;String&gt;{};  // or: Set&lt;String&gt;()

// From list (removes duplicates)
var nums = [1, 2, 2, 3, 3, 3];
var unique = nums.toSet();  // {1, 2, 3}</code></pre>
<h2 id="operations">Set Operations</h2>
<pre><code>var a = {1, 2, 3, 4, 5};
var b = {3, 4, 5, 6, 7};

// Union — all elements from both
print(a.union(b));         // {1, 2, 3, 4, 5, 6, 7}

// Intersection — elements in both
print(a.intersection(b)); // {3, 4, 5}

// Difference — in a but not b
print(a.difference(b));   // {1, 2}</code></pre>
<h2 id="example">Example</h2>
<pre><code>void main() {
  Set&lt;String&gt; enrolled = {'Alice', 'Bob', 'Carol', 'Dave'};
  Set&lt;String&gt; paid = {'Bob', 'Carol', 'Eve'};

  // Who enrolled AND paid?
  var confirmed = enrolled.intersection(paid);
  print('Confirmed: $confirmed');  // {Bob, Carol}

  // Who enrolled but NOT paid?
  var unpaid = enrolled.difference(paid);
  print('Unpaid: $unpaid');  // {Alice, Dave}

  // Everyone involved
  var all = enrolled.union(paid);
  print('All: $all');  // {Alice, Bob, Carol, Dave, Eve}

  // Membership check — O(1)
  print(enrolled.contains('Alice'));  // true
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Confirmed: {Bob, Carol}
Unpaid: {Alice, Dave}
All: {Alice, Bob, Carol, Dave, Eve}
true</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What happens when you add a duplicate to a Set?</strong></p>
<ul><li>A) Error is thrown</li><li>B) It is silently ignored ✅</li><li>C) The old value is replaced</li><li>D) A second copy is added</li></ul>
<p><strong>2. How do you create an empty Set in Dart?</strong></p>
<ul><li>A) {}</li><li>B) []</li><li>C) &lt;String&gt;{} ✅</li><li>D) Set()</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Sets are unordered collections of unique values. Duplicates are ignored. Key operations: <code>union()</code>, <code>intersection()</code>, <code>difference()</code>. Membership checks are O(1). Use <code>&lt;Type&gt;{}</code> to create an empty set (not <code>{}</code> which creates a Map).</p>
    $dart_40$,
    'Learn Dart Sets — unordered unique collections. Master union, intersection, and difference operations for powerful set-based logic.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,collections', NOW(), 40, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Maps in Dart', 'dart-maps',
    $dart_41$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Map?</a></li><li><a href="#creating">Creating Maps</a></li><li><a href="#access">Accessing Values</a></li><li><a href="#methods">Common Methods</a></li><li><a href="#example">Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Map?</h2>
<p>A <code>Map</code> stores data as key-value pairs. Each key must be unique, but values can repeat. Maps are ideal for looking up data by a meaningful label (like a dictionary or JSON object).</p>
<h2 id="creating">Creating Maps</h2>
<pre><code>// String keys, int values
Map&lt;String, int&gt; scores = {
  'Alice': 95,
  'Bob': 87,
  'Carol': 92,
};

// Dynamic values
Map&lt;String, dynamic&gt; user = {
  'name': 'John',
  'age': 30,
  'isAdmin': false,
};

// Empty map
var config = &lt;String, String&gt;{};</code></pre>
<h2 id="access">Accessing Values</h2>
<pre><code>print(scores['Alice']);      // 95
print(scores['Unknown']);    // null (key not found)

// Safe access with default
int bobScore = scores['Bob'] ?? 0;
print(bobScore);             // 87

// Check key existence
print(scores.containsKey('Carol'));   // true
print(scores.containsValue(100));     // false</code></pre>
<h2 id="methods">Common Methods</h2>
<table><thead><tr><th>Method/Property</th><th>Description</th></tr></thead><tbody>
<tr><td><code>keys</code></td><td>All keys as an Iterable</td></tr>
<tr><td><code>values</code></td><td>All values as an Iterable</td></tr>
<tr><td><code>entries</code></td><td>All key-value pairs</td></tr>
<tr><td><code>length</code></td><td>Number of entries</td></tr>
<tr><td><code>remove(key)</code></td><td>Remove an entry</td></tr>
<tr><td><code>putIfAbsent(k,f)</code></td><td>Add only if key missing</td></tr>
<tr><td><code>update(k,f)</code></td><td>Update value for key</td></tr>
</tbody></table>
<h2 id="example">Example</h2>
<pre><code>void main() {
  Map&lt;String, int&gt; inventory = {
    'apples': 50, 'bananas': 30, 'oranges': 20
  };

  // Add / update
  inventory['grapes'] = 15;
  inventory['apples'] = 45;

  // Iterate
  inventory.forEach((item, qty) {
    print('$item: $qty units');
  });

  // Keys and values
  print('Items: $${inventory.keys.toList()}');
  print('Total: $${inventory.values.reduce((a, b) => a + b)}');

  // Remove
  inventory.remove('bananas');
  print('After removal: $${inventory.length} items');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>apples: 45 units
bananas: 30 units
oranges: 20 units
grapes: 15 units
Items: [apples, bananas, oranges, grapes]
Total: 110
After removal: 3 items</pre></div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Create a phone book Map with 5 contacts. Add, update, and remove one.</li>
<li>Count the frequency of each character in a string using a Map.</li>
<li>Invert a Map (swap keys and values).</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does accessing a missing key in a Map return?</strong></p>
<ul><li>A) Error</li><li>B) 0</li><li>C) null ✅</li><li>D) empty string</li></ul>
<p><strong>2. Which property returns all keys of a Map?</strong></p>
<ul><li>A) map.keySet()</li><li>B) map.keys ✅</li><li>C) map.getKeys()</li><li>D) map.allKeys</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Maps store key-value pairs with unique keys. Access values with <code>map[key]</code> (returns null if missing). Use <code>forEach()</code> to iterate entries. Key properties: <code>keys</code>, <code>values</code>, <code>entries</code>, <code>length</code>. Use <code>??</code> for null-safe access with defaults.</p>
    $dart_41$,
    'Learn Dart Maps — key-value pair collections for dictionaries, JSON-like data, and fast lookups. Master CRUD operations and iteration.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,collections', NOW(), 41, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Collection Operations in Dart', 'dart-collection-operations',
    $dart_42$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#map-op">map()</a></li><li><a href="#where">where()</a></li><li><a href="#reduce">reduce() &amp; fold()</a></li><li><a href="#any-every">any() &amp; every()</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="map-op">map()</h2>
<p>Transforms each element and returns a new iterable:</p>
<pre><code>var numbers = [1, 2, 3, 4, 5];
var doubled = numbers.map((n) => n * 2).toList();
print(doubled);  // [2, 4, 6, 8, 10]

var names = ['alice', 'bob', 'carol'];
var upper = names.map((n) => n.toUpperCase()).toList();
print(upper);  // [ALICE, BOB, CAROL]</code></pre>
<h2 id="where">where()</h2>
<p>Filters elements that match a condition:</p>
<pre><code>var nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
var evens = nums.where((n) => n % 2 == 0).toList();
print(evens);  // [2, 4, 6, 8, 10]

var scores = [45, 72, 88, 91, 55, 60];
var passed = scores.where((s) => s >= 60).toList();
print(passed);  // [72, 88, 91, 60]</code></pre>
<h2 id="reduce">reduce() &amp; fold()</h2>
<pre><code>var nums = [1, 2, 3, 4, 5];

// reduce — combine elements (no initial value)
int sum = nums.reduce((a, b) => a + b);
print(sum);  // 15

int product = nums.reduce((a, b) => a * b);
print(product);  // 120

// fold — combine with an initial value
int sumFrom10 = nums.fold(10, (acc, n) => acc + n);
print(sumFrom10);  // 25

// fold works on empty lists; reduce throws on empty
var empty = &lt;int&gt;[];
int result = empty.fold(0, (acc, n) => acc + n);
print(result);  // 0</code></pre>
<h2 id="any-every">any() &amp; every()</h2>
<pre><code>var ages = [15, 22, 30, 17, 45];

print(ages.any((a) => a >= 18));    // true (at least one adult)
print(ages.every((a) => a >= 18));  // false (not all adults)
print(ages.any((a) => a > 50));     // false</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>void main() {
  var students = [
    {'name': 'Alice', 'score': 88},
    {'name': 'Bob', 'score': 54},
    {'name': 'Carol', 'score': 92},
    {'name': 'Dave', 'score': 61},
  ];

  var passed = students.where((s) => s['score'] as int >= 60).toList();
  var names = passed.map((s) => s['name']).toList();
  var avg = students.fold&lt;int&gt;(0, (sum, s) => sum + (s['score'] as int))
            / students.length;

  print('Passed: $names');
  print('Average: $${avg.toStringAsFixed(1)}');
  print('All passed: $${students.every((s) => (s['score'] as int) >= 60)}');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Passed: [Alice, Carol, Dave]
Average: 73.8
All passed: false</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does where() return?</strong></p>
<ul><li>A) A single value</li><li>B) A filtered Iterable ✅</li><li>C) A Map</li><li>D) A bool</li></ul>
<p><strong>2. What is the difference between reduce() and fold()?</strong></p>
<ul><li>A) No difference</li><li>B) fold() takes an initial value and works on empty lists ✅</li><li>C) reduce() is faster</li><li>D) fold() only works on numbers</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Dart collections support functional operations: <code>map()</code> transforms, <code>where()</code> filters, <code>reduce()</code> combines, <code>fold()</code> combines with initial value, <code>any()</code> checks at least one match, <code>every()</code> checks all match. Chain them for powerful data processing pipelines.</p>
    $dart_42$,
    'Master Dart collection operations — map(), where(), reduce(), fold(), any(), and every() — for functional-style data transformation and filtering.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,collections', NOW(), 42, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Spread Operator and Collection Literals in Dart', 'dart-spread-collection-literals',
    $dart_43$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#spread">Spread Operator ...</a></li><li><a href="#null-spread">Null-Aware Spread ...?</a></li><li><a href="#collection-if">Collection If</a></li><li><a href="#collection-for">Collection For</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="spread">Spread Operator ...</h2>
<p>The spread operator <code>...</code> inserts all elements of a collection into another collection literal:</p>
<pre><code>var first = [1, 2, 3];
var second = [4, 5, 6];

var combined = [...first, ...second];
print(combined);  // [1, 2, 3, 4, 5, 6]

// Works with sets and maps too
var setA = {1, 2, 3};
var setB = {3, 4, 5};
var merged = {...setA, ...setB};
print(merged);  // {1, 2, 3, 4, 5}

var defaults = {'theme': 'light', 'lang': 'en'};
var override = {'theme': 'dark'};
var config = {...defaults, ...override};
print(config);  // {theme: dark, lang: en}</code></pre>
<h2 id="null-spread">Null-Aware Spread ...?</h2>
<pre><code>List&lt;int&gt;? extra = null;
var result = [1, 2, ...?extra, 3];
print(result);  // [1, 2, 3] — null spread adds nothing</code></pre>
<h2 id="collection-if">Collection If</h2>
<p>Include elements conditionally inside a collection literal:</p>
<pre><code>bool isAdmin = true;
var menu = [
  'Home',
  'Profile',
  if (isAdmin) 'Admin Panel',
  'Logout',
];
print(menu);  // [Home, Profile, Admin Panel, Logout]</code></pre>
<h2 id="collection-for">Collection For</h2>
<p>Generate multiple elements inside a collection literal using a for loop:</p>
<pre><code>var squares = [for (int i = 1; i <= 5; i++) i * i];
print(squares);  // [1, 4, 9, 16, 25]

var greetings = [for (var name in ['Alice', 'Bob']) 'Hello, $name!'];
print(greetings);  // [Hello, Alice!, Hello, Bob!]</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>void main() {
  var baseItems = ['apple', 'banana'];
  var extraItems = ['cherry', 'date'];
  bool includePremium = true;

  var cart = [
    ...baseItems,
    ...?extraItems,
    if (includePremium) 'truffle',
    for (var i = 1; i <= 2; i++) 'gift_$i',
  ];

  print(cart);
  // [apple, banana, cherry, date, truffle, gift_1, gift_2]
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>[apple, banana, cherry, date, truffle, gift_1, gift_2]</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does the spread operator ... do?</strong></p>
<ul><li>A) Copies a reference</li><li>B) Inserts all elements of a collection inline ✅</li><li>C) Converts to Set</li><li>D) Reverses a list</li></ul>
<p><strong>2. What does ...? do differently than ...?</strong></p>
<ul><li>A) Nothing</li><li>B) Safely handles null collections ✅</li><li>C) Spreads into a Set</li><li>D) Only works with Maps</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>The spread operator <code>...</code> merges collections inline. Use <code>...?</code> for null-safe spreading. Collection <code>if</code> conditionally includes elements. Collection <code>for</code> generates multiple elements from a loop — all inside a single collection literal, without intermediate variables.</p>
    $dart_43$,
    'Learn Dart spread operators and collection literals — inline merging with ..., conditional elements with collection if, and generation with collection for.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,collections', NOW(), 43, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Collection If in Dart',
    'dart-collection-if',
    $dart_44$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#syntax">Syntax</a></li>
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
<p><strong>Collection if</strong> is a Dart feature that lets you conditionally include elements in a collection literal using an <code>if</code> expression directly inside the collection. No temporary variables or multi-step logic needed.</p>

<h2 id="why">Why It Matters</h2>
<p>Building dynamic lists or maps where certain items should only be included based on a condition is a common task. Collection <code>if</code> keeps this logic inline and readable, eliminating the need to build the collection in multiple steps.</p>

<h2 id="syntax">Syntax</h2>
<pre><code>var list = [
  element1,
  if (condition) element2,
  if (condition) element3 else element4,
];</code></pre>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  bool isAdmin = true;
  bool hasPremium = false;

  // Conditionally add menu items
  var menuItems = [
    'Home',
    'Profile',
    if (isAdmin) 'Admin Panel',
    if (hasPremium) 'Premium Content' else 'Upgrade to Premium',
  ];
  print(menuItems);

  // In a Map
  String? promoCode;
  var order = {
    'product': 'Dart Book',
    'price': 29.99,
    if (promoCode != null) 'promo': promoCode,
  };
  print(order);

  // With else
  bool isDarkMode = true;
  var themeColors = [
    if (isDarkMode) '#1a1a2e' else '#ffffff',
    if (isDarkMode) '#16213e' else '#f5f5f5',
  ];
  print(themeColors);

  // Nested conditions
  int age = 25;
  bool isStudent = false;
  var pricing = [
    'Standard: \$10',
    if (age &lt; 18) 'Child Discount: \$5',
    if (isStudent) 'Student Discount: \$7',
    if (age &gt;= 60) 'Senior Discount: \$6',
  ];
  print(pricing);
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>[Home, Profile, Admin Panel, Upgrade to Premium]
{product: Dart Book, price: 29.99}
[#1a1a2e, #16213e]
[Standard: $10]</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>if (condition) element</code> includes <code>element</code> in the collection only when <code>condition</code> is <code>true</code>.</li>
  <li><code>if (condition) elementA else elementB</code> includes <code>elementA</code> when true, otherwise <code>elementB</code>.</li>
  <li>Multiple <code>if</code> entries can appear in the same collection literal.</li>
  <li>Collection <code>if</code> works in <code>List</code>, <code>Set</code>, and <code>Map</code> literals.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>The condition inside collection <code>if</code> must be a boolean expression.</li>
  <li>You can only include one element per <code>if</code> — use the spread operator with a sub-list if you need multiple conditional elements.</li>
  <li>Collection <code>if</code> is evaluated at the time the collection literal is created.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Trying to include multiple elements with a single <code>if</code> — use <code>if (cond) ...[a, b]</code> instead.</li>
  <li>Forgetting that the else branch is optional — if omitted and the condition is false, nothing is added.</li>
  <li>Using collection <code>if</code> when a <code>where()</code> filter or <code>??</code> would be more appropriate.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Build a navigation menu list that includes a "Settings" item only if <code>isLoggedIn</code> is true, and a "Login" item otherwise.</li>
    <li>Create a product map that includes a "discount" key only if a discount percentage is greater than 0.</li>
    <li>Build a list of tags that conditionally includes "featured" and "sale" based on boolean flags.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does collection if do when its condition is false and there is no else?</strong></p>
  <ul><li>A) Adds null</li><li>B) Throws an error</li><li>C) Adds nothing ✅</li><li>D) Adds false</li></ul>
  <p><strong>2. Which collection types support collection if?</strong></p>
  <ul><li>A) Only List</li><li>B) List and Set only</li><li>C) List, Set, and Map ✅</li><li>D) None — it is not a real feature</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Collection <code>if</code> allows you to conditionally include elements directly inside collection literals. It supports <code>if</code> and <code>if-else</code> forms and works with <code>List</code>, <code>Set</code>, and <code>Map</code>. To conditionally include multiple elements, combine collection <code>if</code> with the spread operator. This feature makes dynamic collection construction clean and expressive.</p>
    $dart_44$,
    'Learn collection if in Dart — conditionally include elements inside list, set, and map literals without temporary variables.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,intermediate,collections',
    NOW(),
    44,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Collection For in Dart',
    'dart-collection-for',
    $dart_45$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#intro">Introduction</a></li>
    <li><a href="#why">Why It Matters</a></li>
    <li><a href="#syntax">Syntax</a></li>
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
<p><strong>Collection for</strong> is a Dart feature that lets you generate multiple elements inside a collection literal using a <code>for</code> loop, all in a single expression. It is a concise alternative to building collections with <code>map()</code> or traditional loop-and-add patterns.</p>

<h2 id="why">Why It Matters</h2>
<p>Generating collections from sequences — like turning a list of strings into a list of widgets, or creating a numbered list — is extremely common. Collection <code>for</code> keeps this inline and readable without needing temporary variables or helper functions.</p>

<h2 id="syntax">Syntax</h2>
<pre><code>var list = [
  for (var item in iterable) transform(item),
];

var list2 = [
  for (int i = start; i &lt; end; i++) expression,
];</code></pre>

<h2 id="example">Code Example</h2>
<pre><code>void main() {
  // Generate squares 1–5
  var squares = [for (int i = 1; i &lt;= 5; i++) i * i];
  print(squares);   // [1, 4, 9, 16, 25]

  // Transform strings to uppercase
  var fruits = ['apple', 'banana', 'cherry'];
  var upper = [for (var f in fruits) f.toUpperCase()];
  print(upper);     // [APPLE, BANANA, CHERRY]

  // Generate a Map from a list
  var names = ['Alice', 'Bob', 'Carol'];
  var nameMap = {
    for (int i = 0; i &lt; names.length; i++) i: names[i],
  };
  print(nameMap);   // {0: Alice, 1: Bob, 2: Carol}

  // Combine collection for with collection if
  var numbers = [1, 2, 3, 4, 5, 6];
  var evenSquares = [
    for (var n in numbers)
      if (n % 2 == 0) n * n,
  ];
  print(evenSquares);   // [4, 16, 36]

  // Generate Set with collection for
  var uniqueDoubles = {for (var n in [1, 2, 2, 3]) n * 2};
  print(uniqueDoubles);   // {2, 4, 6}
}</code></pre>

<div class="note-box">
  <strong>Output:</strong>
  <pre>[1, 4, 9, 16, 25]
[APPLE, BANANA, CHERRY]
{0: Alice, 1: Bob, 2: Carol}
[4, 16, 36]
{2, 4, 6}</pre>
</div>

<h2 id="explanation">Explanation</h2>
<ul>
  <li><code>for (var item in iterable)</code> iterates over each element and adds the resulting expression to the collection.</li>
  <li>Traditional C-style <code>for (int i = 0; i &lt; n; i++)</code> is also supported inside collection literals.</li>
  <li>Collection <code>for</code> can be nested and combined with collection <code>if</code> for powerful inline data transformations.</li>
  <li>Works in <code>List</code>, <code>Set</code>, and <code>Map</code> literals.</li>
</ul>

<h2 id="notes">Notes</h2>
<ul>
  <li>Collection <code>for</code> is equivalent to calling <code>map().toList()</code> but is often more readable for simple transformations.</li>
  <li>Use it to generate UI elements (e.g., Flutter widget lists) from data collections.</li>
  <li>Combining collection <code>for</code> with collection <code>if</code> produces concise filter-and-transform pipelines.</li>
</ul>

<h2 id="mistakes">Common Mistakes</h2>
<ul>
  <li>Forgetting to use <code>in</code> keyword in for-in form: <code>for (var x in list)</code>.</li>
  <li>Trying to add multiple elements per iteration without a spread: use <code>...[a, b]</code>.</li>
  <li>Using collection <code>for</code> for very complex multi-step transformations — a regular loop is more readable in those cases.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Use collection <code>for</code> to generate a list of the first 10 multiples of 3.</li>
    <li>Use collection <code>for</code> with collection <code>if</code> to build a list of all odd numbers from 1 to 20.</li>
    <li>Convert a list of product names to a Map where keys are the index and values are the names in title case.</li>
  </ol>
</div>

<div class="quiz-box">
  <h3>🧠 Quiz</h3>
  <p><strong>1. What does collection for produce?</strong></p>
  <ul><li>A) A single value</li><li>B) Multiple elements added to the collection ✅</li><li>C) A new function</li><li>D) A boolean</li></ul>
  <p><strong>2. Collection for can be combined with which other collection feature?</strong></p>
  <ul><li>A) Collection switch</li><li>B) Collection if ✅</li><li>C) Collection try</li><li>D) Collection while</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Collection <code>for</code> generates multiple elements inside a collection literal using a loop, working with both for-in and C-style for loops. It supports <code>List</code>, <code>Set</code>, and <code>Map</code> literals and can be combined with collection <code>if</code> for inline filter-and-transform operations. It is a clean alternative to <code>map().toList()</code> for simple data transformations.</p>
    $dart_45$,
    'Learn collection for in Dart — generate elements inside list, set, and map literals using for loops, combined with collection if for powerful inline transformations.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,intermediate,collections',
    NOW(),
    45,
    'PUBLISHED',
    'ACT',
    'system'
);
