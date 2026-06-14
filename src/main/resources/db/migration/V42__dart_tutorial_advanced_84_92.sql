-- Section 14: Packages & pub.dev (84-88) + Section 15: Flutter Prep (89-92)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Dart Packages and pub.dev', 'dart-packages',
    $dart_84$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is pub.dev?</a></li><li><a href="#pubspec">pubspec.yaml</a></li><li><a href="#add">Adding Packages</a></li><li><a href="#popular">Popular Packages</a></li><li><a href="#create">Creating Your Own Package</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is pub.dev?</h2>
<p><a href="https://pub.dev">pub.dev</a> is the official package repository for Dart and Flutter. It hosts thousands of community and official packages. The <code>dart pub</code> command manages packages — installing, updating, and removing dependencies.</p>
<h2 id="pubspec">pubspec.yaml</h2>
<p>Every Dart project has a <code>pubspec.yaml</code> that declares metadata and dependencies:</p>
<pre><code>name: my_dart_app
description: A sample Dart application
version: 1.0.0

environment:
  sdk: '&gt;=3.0.0 &lt;4.0.0'

dependencies:
  http: ^1.1.0
  intl: ^0.18.0
  path: ^1.8.0
  equatable: ^2.0.5

dev_dependencies:
  test: ^1.24.0
  lints: ^3.0.0</code></pre>
<h2 id="add">Adding Packages</h2>
<pre><code># Add a dependency
dart pub add http

# Add a dev dependency
dart pub add --dev test

# Get all dependencies
dart pub get

# Update all to latest compatible:
dart pub upgrade

# Upgrade to potentially-breaking latest:
dart pub upgrade --major-versions

# Remove a package:
dart pub remove http</code></pre>
<h2 id="popular">Popular Packages</h2>
<table>
<tr><th>Package</th><th>Purpose</th></tr>
<tr><td><code>http</code></td><td>HTTP requests</td></tr>
<tr><td><code>dio</code></td><td>Advanced HTTP with interceptors</td></tr>
<tr><td><code>intl</code></td><td>Internationalization, date formatting</td></tr>
<tr><td><code>path</code></td><td>Cross-platform file path manipulation</td></tr>
<tr><td><code>json_serializable</code></td><td>Code-gen for JSON models</td></tr>
<tr><td><code>freezed</code></td><td>Immutable classes, unions, copyWith</td></tr>
<tr><td><code>equatable</code></td><td>Value equality for classes</td></tr>
<tr><td><code>test</code></td><td>Dart testing framework</td></tr>
<tr><td><code>mockito</code></td><td>Mocking for tests</td></tr>
<tr><td><code>shelf</code></td><td>Dart HTTP server framework</td></tr>
</table>
<h2 id="create">Creating Your Own Package</h2>
<pre><code># Create a new package:
dart create --template=package my_package

# Package structure:
# my_package/
#   lib/
#     my_package.dart        ← public API entry point
#     src/
#       feature_one.dart     ← internal implementation
#   test/
#     my_package_test.dart
#   pubspec.yaml
#   README.md
#   CHANGELOG.md</code></pre>
<pre><code>// lib/my_package.dart — export only public API
library my_package;

export 'src/string_utils.dart';
export 'src/math_utils.dart';

// lib/src/string_utils.dart
extension StringExtensions on String {
  String capitalize() =&gt; isEmpty ? this : '$${this[0].toUpperCase()}$${substring(1)}';
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What file declares a Dart project's dependencies?</strong></p>
<ul><li>A) package.json</li><li>B) dart.yaml</li><li>C) pubspec.yaml ✅</li><li>D) dependencies.yaml</li></ul>
<p><strong>2. What does the ^ prefix mean in a version constraint like ^1.1.0?</strong></p>
<ul><li>A) Exactly version 1.1.0</li><li>B) Any version &gt;= 1.1.0 and &lt; 2.0.0 ✅</li><li>C) Any version &gt;= 1.1.0</li><li>D) Beta version</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p><code>pub.dev</code> is Dart's package repository. Declare dependencies in <code>pubspec.yaml</code> and install with <code>dart pub get</code>. Use <code>dart pub add &lt;package&gt;</code> for quick installs. The <code>^</code> prefix means "compatible with this version". Create packages with <code>dart create --template=package</code> and expose your API via <code>export</code> in the library's entry point.</p>
    $dart_84$,
    'Learn Dart package management — pub.dev, pubspec.yaml, dart pub commands, popular packages, version constraints, and creating your own Dart package.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,packages', NOW(), 84, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Testing in Dart', 'dart-testing',
    $dart_85$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#setup">Setup</a></li><li><a href="#unit">Unit Tests</a></li><li><a href="#group">Test Groups</a></li><li><a href="#matchers">Matchers</a></li><li><a href="#async">Async Tests</a></li><li><a href="#mocks">Mocking</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="setup">Setup</h2>
<pre><code># pubspec.yaml
dev_dependencies:
  test: ^1.24.0

# Run tests:
dart test
dart test test/my_test.dart
dart test --coverage=coverage</code></pre>
<h2 id="unit">Unit Tests</h2>
<pre><code>import 'package:test/test.dart';

int add(int a, int b) =&gt; a + b;
double divide(double a, double b) {
  if (b == 0) throw ArgumentError('Division by zero');
  return a / b;
}

void main() {
  test('add returns sum of two numbers', () {
    expect(add(2, 3), equals(5));
    expect(add(-1, 1), equals(0));
    expect(add(0, 0), equals(0));
  });

  test('divide throws ArgumentError on zero', () {
    expect(() =&gt; divide(10, 0), throwsArgumentError);
  });

  test('divide returns correct result', () {
    expect(divide(10, 2), closeTo(5.0, 0.001));
  });
}</code></pre>
<h2 id="group">Test Groups</h2>
<pre><code>class Calculator {
  int add(int a, int b) =&gt; a + b;
  int subtract(int a, int b) =&gt; a - b;
  int multiply(int a, int b) =&gt; a * b;
  double divide(int a, int b) {
    if (b == 0) throw ArgumentError('Cannot divide by zero');
    return a / b;
  }
}

void main() {
  late Calculator calc;

  setUp(() =&gt; calc = Calculator());    // runs before each test
  tearDown(() =&gt; print('Test done')); // runs after each test

  group('Calculator', () {
    group('add', () {
      test('positive numbers', () =&gt; expect(calc.add(2, 3), 5));
      test('negative numbers', () =&gt; expect(calc.add(-1, -1), -2));
    });

    group('divide', () {
      test('returns double', () =&gt; expect(calc.divide(10, 3), closeTo(3.333, 0.001)));
      test('throws on zero', () =&gt; expect(() =&gt; calc.divide(5, 0), throwsArgumentError));
    });
  });
}</code></pre>
<h2 id="matchers">Matchers</h2>
<pre><code>expect(value, equals(42));
expect(value, isNull);
expect(value, isNotNull);
expect(value, isTrue);
expect(value, isFalse);
expect(value, isA&lt;String&gt;());
expect(value, contains('dart'));
expect(value, startsWith('Hello'));
expect(value, hasLength(5));
expect(value, inInclusiveRange(0, 100));
expect(value, closeTo(3.14, 0.01));
expect(list, containsAll([1, 2, 3]));
expect(fn, throwsA(isA&lt;FormatException&gt;()));</code></pre>
<h2 id="async">Async Tests</h2>
<pre><code>Future&lt;String&gt; fetchData() async {
  await Future.delayed(Duration(milliseconds: 100));
  return 'data';
}

void main() {
  test('fetches data asynchronously', () async {
    var result = await fetchData();
    expect(result, equals('data'));
  });

  test('stream emits values', () async {
    var stream = Stream.fromIterable([1, 2, 3]);
    expect(stream, emitsInOrder([1, 2, 3, emitsDone]));
  });
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which function runs before each test in a group?</strong></p>
<ul><li>A) beforeEach</li><li>B) setUp ✅</li><li>C) init</li><li>D) prepare</li></ul>
<p><strong>2. How do you test that a function throws a specific exception?</strong></p>
<ul><li>A) expect(fn(), throws)</li><li>B) expect(() =&gt; fn(), throwsA(isA&lt;ExceptionType&gt;())) ✅</li><li>C) try { fn() } catch ...</li><li>D) assertThrows(fn)</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use the <code>test</code> package for Dart unit testing. Write tests with <code>test()</code>, group them with <code>group()</code>, and use <code>setUp()</code>/<code>tearDown()</code> for initialization. Assert with <code>expect(value, matcher)</code>. Test async code with <code>async/await</code> in tests. Run with <code>dart test</code>.</p>
    $dart_85$,
    'Learn Dart testing with the test package — unit tests, test groups, setUp/tearDown, matchers, async test support, and best practices for reliable test suites.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,testing', NOW(), 85, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Introduction to Flutter', 'dart-flutter-intro',
    $dart_89$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Flutter?</a></li><li><a href="#architecture">Flutter Architecture</a></li><li><a href="#setup">Setup and Installation</a></li><li><a href="#first-app">First Flutter App</a></li><li><a href="#widgets">Everything is a Widget</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Flutter?</h2>
<p>Flutter is Google's open-source UI framework for building natively compiled applications for mobile (iOS, Android), web, and desktop from a single Dart codebase. Unlike React Native (which bridges to native components), Flutter draws its own pixels using the Skia/Impeller rendering engine — making it extremely performant and visually consistent across platforms.</p>
<h2 id="architecture">Flutter Architecture</h2>
<ul>
<li><strong>Dart Layer:</strong> Your app code + Flutter framework (widgets, animation, gestures)</li>
<li><strong>Engine Layer:</strong> C++ rendering engine (Skia/Impeller), text layout, Dart runtime</li>
<li><strong>Embedder Layer:</strong> Platform-specific shell (iOS, Android, Windows, Linux, macOS, web)</li>
</ul>
<h2 id="setup">Setup and Installation</h2>
<pre><code># 1. Install Flutter SDK from flutter.dev
# 2. Add to PATH
export PATH="$PATH:/path/to/flutter/bin"

# 3. Check setup
flutter doctor

# 4. Create a new Flutter app
flutter create my_app
cd my_app

# 5. Run the app
flutter run</code></pre>
<h2 id="first-app">First Flutter App</h2>
<pre><code>// lib/main.dart
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Hello Flutter',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Hello Flutter')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Welcome to Flutter!',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () =&gt; print('Button pressed!'),
              child: const Text('Press Me'),
            ),
          ],
        ),
      ),
    );
  }
}</code></pre>
<h2 id="widgets">Everything is a Widget</h2>
<p>In Flutter, every UI element is a Widget — from the entire screen (<code>Scaffold</code>) to a single pixel of padding. Widgets are immutable descriptions of the UI. Flutter rebuilds widgets when state changes.</p>
<ul>
<li><strong>StatelessWidget:</strong> Immutable widget — UI depends only on constructor parameters</li>
<li><strong>StatefulWidget:</strong> Mutable widget — has a <code>State</code> object that can change over time</li>
<li><strong>InheritedWidget:</strong> Passes data down the widget tree (basis of Provider, etc.)</li>
</ul>
<pre><code>// Common layout widgets:
// Column, Row, Stack — arrange children
// Container — box with padding, margin, decoration
// Expanded, Flexible — fill available space
// ListView, GridView — scrollable lists
// Padding, Center, Align — positioning
// Text, Image, Icon, Button — content</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does Flutter use to render UI?</strong></p>
<ul><li>A) Native platform components</li><li>B) HTML/CSS/JavaScript</li><li>C) Its own Skia/Impeller rendering engine ✅</li><li>D) OpenGL directly</li></ul>
<p><strong>2. What is the difference between StatelessWidget and StatefulWidget?</strong></p>
<ul><li>A) StatelessWidget is faster; StatefulWidget is slower</li><li>B) StatelessWidget is immutable; StatefulWidget has mutable State that triggers rebuilds ✅</li><li>C) StatefulWidget is deprecated in Flutter 3</li><li>D) No difference</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Flutter is a cross-platform UI framework built on Dart. It renders its own pixels for consistent performance. Every UI element is a widget — either <code>StatelessWidget</code> (fixed) or <code>StatefulWidget</code> (dynamic). Start with <code>flutter create</code>, and use <code>MaterialApp + Scaffold</code> as the app skeleton. Your Dart knowledge transfers directly — Flutter is pure Dart.</p>
    $dart_89$,
    'Introduction to Flutter — cross-platform UI framework, architecture, setup, first app structure, StatelessWidget vs StatefulWidget, and the widget tree concept.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,flutter', NOW(), 89, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Flutter Widgets and Layouts', 'dart-flutter-widgets',
    $dart_90$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#layout">Layout Widgets</a></li><li><a href="#display">Display Widgets</a></li><li><a href="#input">Input Widgets</a></li><li><a href="#stateful">StatefulWidget Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="layout">Layout Widgets</h2>
<pre><code>// Column — vertical arrangement
Column(
  mainAxisAlignment: MainAxisAlignment.center,
  crossAxisAlignment: CrossAxisAlignment.start,
  children: [
    Text('First'),
    Text('Second'),
    Text('Third'),
  ],
)

// Row — horizontal arrangement
Row(
  mainAxisAlignment: MainAxisAlignment.spaceBetween,
  children: [
    Icon(Icons.home),
    Text('Home'),
    Icon(Icons.arrow_forward),
  ],
)

// Stack — overlapping widgets
Stack(
  children: [
    Image.network('https://example.com/image.jpg'),
    Positioned(
      bottom: 8, right: 8,
      child: Text('Caption', style: TextStyle(color: Colors.white)),
    ),
  ],
)

// Container — box model
Container(
  width: 200,
  height: 100,
  margin: EdgeInsets.all(8),
  padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
  decoration: BoxDecoration(
    color: Colors.blue,
    borderRadius: BorderRadius.circular(12),
    boxShadow: [BoxShadow(blurRadius: 4, color: Colors.black26)],
  ),
  child: Text('Styled Box', style: TextStyle(color: Colors.white)),
)</code></pre>
<h2 id="display">Display Widgets</h2>
<pre><code>// Text with styling
Text(
  'Hello Flutter',
  style: TextStyle(
    fontSize: 24,
    fontWeight: FontWeight.bold,
    color: Colors.blue,
    letterSpacing: 1.2,
  ),
  textAlign: TextAlign.center,
  maxLines: 2,
  overflow: TextOverflow.ellipsis,
)

// Image
Image.network('https://flutter.dev/images/flutter-logo-sharing.png',
    width: 100, height: 100, fit: BoxFit.contain)

// Icon
Icon(Icons.favorite, color: Colors.red, size: 32)

// ListView
ListView.builder(
  itemCount: items.length,
  itemBuilder: (ctx, i) =&gt; ListTile(
    leading: Icon(Icons.check_circle),
    title: Text(items[i]),
    trailing: Icon(Icons.arrow_forward_ios),
  ),
)</code></pre>
<h2 id="input">Input Widgets</h2>
<pre><code>// Button variants
ElevatedButton(onPressed: () {}, child: Text('Elevated'))
OutlinedButton(onPressed: () {}, child: Text('Outlined'))
TextButton(onPressed: () {}, child: Text('Text'))
IconButton(icon: Icon(Icons.add), onPressed: () {})
FloatingActionButton(onPressed: () {}, child: Icon(Icons.add))

// TextField
TextField(
  controller: TextEditingController(),
  decoration: InputDecoration(
    labelText: 'Username',
    hintText: 'Enter your username',
    prefixIcon: Icon(Icons.person),
    border: OutlineInputBorder(),
  ),
  onChanged: (value) =&gt; print('Typing: $value'),
)</code></pre>
<h2 id="stateful">StatefulWidget Example</h2>
<pre><code>class CounterPage extends StatefulWidget {
  const CounterPage({super.key});
  @override State&lt;CounterPage&gt; createState() =&gt; _CounterPageState();
}

class _CounterPageState extends State&lt;CounterPage&gt; {
  int _count = 0;

  void _increment() =&gt; setState(() =&gt; _count++);
  void _decrement() =&gt; setState(() =&gt; _count--);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Counter')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text('$_count', style: Theme.of(context).textTheme.displayLarge),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                FloatingActionButton(onPressed: _decrement, child: Icon(Icons.remove)),
                SizedBox(width: 20),
                FloatingActionButton(onPressed: _increment, child: Icon(Icons.add)),
              ],
            ),
          ],
        ),
      ),
    );
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does setState() do in a StatefulWidget?</strong></p>
<ul><li>A) Updates the parent widget</li><li>B) Marks the widget dirty and schedules a rebuild ✅</li><li>C) Sends an event to the widget tree</li><li>D) Disposes the current state</li></ul>
<p><strong>2. Which widget arranges children horizontally?</strong></p>
<ul><li>A) Column</li><li>B) Stack</li><li>C) Row ✅</li><li>D) Flex</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Flutter's layout system uses <code>Column</code>, <code>Row</code>, <code>Stack</code>, and <code>Container</code> for positioning. Display content with <code>Text</code>, <code>Image</code>, <code>Icon</code>, and <code>ListView</code>. Accept input with <code>TextField</code>, <code>ElevatedButton</code>, and form widgets. Use <code>StatefulWidget</code> with <code>setState()</code> to respond to user interaction. Everything composes — widgets nest inside widgets.</p>
    $dart_90$,
    'Learn Flutter widgets and layouts — Column, Row, Stack, Container, Text, ListView, TextField, buttons, and building interactive UIs with StatefulWidget and setState.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,flutter', NOW(), 90, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Flutter State Management', 'dart-flutter-state',
    $dart_91$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#local">Local State</a></li><li><a href="#inherited">InheritedWidget</a></li><li><a href="#provider">Provider Package</a></li><li><a href="#riverpod">Riverpod Overview</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="local">Local State</h2>
<p>For state used only within one widget, <code>setState()</code> in a <code>StatefulWidget</code> is sufficient. It is simple, zero dependencies, and perfect for toggles, counters, and form fields.</p>
<pre><code>class ToggleButton extends StatefulWidget {
  const ToggleButton({super.key});
  @override State&lt;ToggleButton&gt; createState() =&gt; _ToggleButtonState();
}
class _ToggleButtonState extends State&lt;ToggleButton&gt; {
  bool _on = false;
  @override Widget build(BuildContext context) =&gt;
    Switch(value: _on, onChanged: (v) =&gt; setState(() =&gt; _on = v));
}</code></pre>
<h2 id="inherited">InheritedWidget</h2>
<p>When state must be shared across many widgets deep in the tree, <code>InheritedWidget</code> provides it without prop-drilling. This is what <code>Theme.of(context)</code> and <code>MediaQuery.of(context)</code> use internally.</p>
<pre><code>class CounterProvider extends InheritedWidget {
  final int count;
  final VoidCallback increment;

  const CounterProvider({
    super.key,
    required this.count,
    required this.increment,
    required super.child,
  });

  static CounterProvider of(BuildContext context) {
    return context.dependOnInheritedWidgetOfExactType&lt;CounterProvider&gt;()!;
  }

  @override
  bool updateShouldNotify(CounterProvider old) =&gt; count != old.count;
}

// Usage deep in the tree:
// var provider = CounterProvider.of(context);
// Text('$${provider.count}')</code></pre>
<h2 id="provider">Provider Package</h2>
<p><code>provider</code> is the most widely used state management solution — it wraps <code>InheritedWidget</code> with a clean API:</p>
<pre><code># pubspec.yaml
dependencies:
  provider: ^6.1.0</code></pre>
<pre><code>import 'package:provider/provider.dart';
import 'package:flutter/foundation.dart';

class CounterModel extends ChangeNotifier {
  int _count = 0;
  int get count =&gt; _count;

  void increment() {
    _count++;
    notifyListeners();  // triggers rebuild of consumers
  }
}

// In main:
void main() =&gt; runApp(
  ChangeNotifierProvider(
    create: (_) =&gt; CounterModel(),
    child: const MyApp(),
  ),
);

// In any widget:
class CounterDisplay extends StatelessWidget {
  const CounterDisplay({super.key});
  @override Widget build(BuildContext context) {
    var counter = context.watch&lt;CounterModel&gt;();  // rebuilds on change
    return Text('$${counter.count}');
  }
}

class IncrementButton extends StatelessWidget {
  const IncrementButton({super.key});
  @override Widget build(BuildContext context) {
    return ElevatedButton(
      // context.read — does NOT rebuild on change
      onPressed: () =&gt; context.read&lt;CounterModel&gt;().increment(),
      child: const Text('+'),
    );
  }
}</code></pre>
<h2 id="riverpod">Riverpod Overview</h2>
<p><code>riverpod</code> is a compile-safe, testable state management solution created by the Provider author:</p>
<pre><code>import 'package:flutter_riverpod/flutter_riverpod.dart';

final counterProvider = StateNotifierProvider&lt;CounterNotifier, int&gt;(
  (ref) =&gt; CounterNotifier(),
);

class CounterNotifier extends StateNotifier&lt;int&gt; {
  CounterNotifier() : super(0);
  void increment() =&gt; state++;
}

class CounterWidget extends ConsumerWidget {
  const CounterWidget({super.key});
  @override Widget build(BuildContext context, WidgetRef ref) {
    final count = ref.watch(counterProvider);
    return Column(children: [
      Text('$count'),
      ElevatedButton(
        onPressed: () =&gt; ref.read(counterProvider.notifier).increment(),
        child: Text('+'),
      ),
    ]);
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. When should you use setState() vs Provider?</strong></p>
<ul><li>A) Always use setState</li><li>B) setState for local widget state; Provider when multiple widgets need the same state ✅</li><li>C) Only use Provider — setState is deprecated</li><li>D) Only setState works with async code</li></ul>
<p><strong>2. What does context.watch&lt;T&gt;() do in Provider?</strong></p>
<ul><li>A) Reads the value without rebuilding</li><li>B) Reads the value and rebuilds the widget when it changes ✅</li><li>C) Subscribes to a stream</li><li>D) Dispatches an action</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Choose state management based on scope: <code>setState()</code> for widget-local state, <code>InheritedWidget</code> for tree-level sharing, <code>Provider</code> for most apps (simple, well-documented, Flutter-team recommended), and <code>Riverpod</code> for compile-safe, testable, complex state. All options use Dart streams, futures, and classes you already know.</p>
    $dart_91$,
    'Flutter state management guide — local setState, InheritedWidget, Provider with ChangeNotifier, and Riverpod for scalable, testable app state in Dart.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,flutter', NOW(), 91, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Flutter Navigation and Routing', 'dart-flutter-navigation',
    $dart_92$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#push">Push and Pop Navigation</a></li><li><a href="#named">Named Routes</a></li><li><a href="#go-router">GoRouter Package</a></li><li><a href="#passing">Passing Data</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="push">Push and Pop Navigation</h2>
<pre><code>// Navigate to a new screen:
Navigator.push(
  context,
  MaterialPageRoute(builder: (_) =&gt; const DetailsPage(id: 42)),
);

// Navigate back:
Navigator.pop(context);

// Navigate and return a result:
final result = await Navigator.push&lt;String&gt;(
  context,
  MaterialPageRoute(builder: (_) =&gt; const InputPage()),
);
print('Got: $result');  // from InputPage: Navigator.pop(context, 'hello')

// Replace current screen (no back button):
Navigator.pushReplacement(
  context,
  MaterialPageRoute(builder: (_) =&gt; const HomePage()),
);

// Go all the way back to first screen:
Navigator.popUntil(context, ModalRoute.withName('/'));</code></pre>
<h2 id="named">Named Routes</h2>
<pre><code>MaterialApp(
  initialRoute: '/',
  routes: {
    '/': (ctx) =&gt; const HomePage(),
    '/details': (ctx) =&gt; const DetailsPage(),
    '/settings': (ctx) =&gt; const SettingsPage(),
    '/login': (ctx) =&gt; const LoginPage(),
  },
)

// Navigate:
Navigator.pushNamed(context, '/details');
Navigator.pushNamed(context, '/details', arguments: {'id': 42});</code></pre>
<h2 id="go-router">GoRouter Package</h2>
<p><code>go_router</code> is Google's recommended solution for declarative, deep-linkable navigation:</p>
<pre><code>import 'package:go_router/go_router.dart';

final router = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (_, __) =&gt; const HomePage()),
    GoRoute(
      path: '/users/:id',
      builder: (ctx, state) {
        final id = state.pathParameters['id']!;
        return UserPage(userId: id);
      },
    ),
    GoRoute(path: '/settings', builder: (_, __) =&gt; const SettingsPage()),
  ],
);

// Usage: context.go('/users/42');
//        context.push('/settings');
//        context.pop();</code></pre>
<h2 id="passing">Passing Data Between Screens</h2>
<pre><code>// Option 1: Constructor parameters (simplest)
Navigator.push(context,
  MaterialPageRoute(builder: (_) =&gt; ProductPage(product: selectedProduct)));

// Option 2: Route arguments
Navigator.pushNamed(context, '/product', arguments: selectedProduct);
// In ProductPage:
final product = ModalRoute.of(context)!.settings.arguments as Product;

// Option 3: GoRouter path parameters + query params
context.go('/products/42?view=detail');</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does Navigator.pop(context, result) do?</strong></p>
<ul><li>A) Navigates to the root</li><li>B) Removes the current screen and optionally returns a value to the caller ✅</li><li>C) Clears the navigation stack</li><li>D) Pushes a new screen</li></ul>
<p><strong>2. Which navigation package is Google's recommended approach for declarative deep-linkable routing?</strong></p>
<ul><li>A) flutter_navigation</li><li>B) auto_route</li><li>C) go_router ✅</li><li>D) nav_2</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Flutter navigation uses a stack: <code>Navigator.push()</code> adds screens, <code>Navigator.pop()</code> removes them. Named routes (<code>routes</code> map in <code>MaterialApp</code>) are simple for small apps. Use <code>go_router</code> for deep linking, URL-based routing, and complex navigation flows. Pass data via constructor arguments, route arguments, or path parameters.</p>
    $dart_92$,
    'Flutter navigation guide — push/pop, named routes, GoRouter for declarative deep-linkable routing, passing data between screens, and navigation best practices.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,flutter', NOW(), 92, 'PUBLISHED', 'ACT', 'system'
);
