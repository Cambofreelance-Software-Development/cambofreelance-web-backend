-- Section 14 continued: Dart CLI tools, code style, and server-side Dart (86-88)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Dart CLI Applications', 'dart-cli-apps',
    $dart_86$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#setup">Project Setup</a></li><li><a href="#args">Command-line Arguments</a></li><li><a href="#stdin">Reading stdin</a></li><li><a href="#ansi">Colored Output</a></li><li><a href="#example">CLI App Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="setup">Project Setup</h2>
<pre><code># Create a CLI app:
dart create --template=console my_cli

# Structure:
# my_cli/
#   bin/my_cli.dart    ← entry point
#   lib/               ← library code
#   pubspec.yaml</code></pre>
<h2 id="args">Command-line Arguments</h2>
<pre><code>// bin/my_cli.dart
import 'dart:io';

void main(List&lt;String&gt; args) {
  if (args.isEmpty) {
    print('Usage: my_cli &lt;command&gt; [options]');
    exit(1);
  }

  switch (args[0]) {
    case 'hello':
      var name = args.length &gt; 1 ? args[1] : 'World';
      print('Hello, $name!');
    case 'add':
      if (args.length &lt; 3) {
        stderr.writeln('Usage: add &lt;num1&gt; &lt;num2&gt;');
        exit(1);
      }
      var a = double.parse(args[1]);
      var b = double.parse(args[2]);
      print('$${args[1]} + $${args[2]} = $${a + b}');
    default:
      stderr.writeln('Unknown command: $${args[0]}');
      exit(1);
  }
}

// Or use the 'args' package for structured parsing:
// dart pub add args</code></pre>
<h2 id="stdin">Reading stdin</h2>
<pre><code>import 'dart:io';

void main() {
  stdout.write('Enter your name: ');
  var name = stdin.readLineSync();

  stdout.write('Enter your age: ');
  var ageStr = stdin.readLineSync() ?? '0';
  var age = int.tryParse(ageStr) ?? 0;

  print('Hello, $name! You are $age years old.');

  // Read multiple lines until EOF:
  print('Enter text (Ctrl+D to finish):');
  String? line;
  var lines = &lt;String&gt;[];
  while ((line = stdin.readLineSync()) != null) {
    lines.add(line!);
  }
  print('You entered $${lines.length} lines');
}</code></pre>
<h2 id="ansi">Colored Output</h2>
<pre><code>// ANSI color codes for terminal output
class Colors {
  static const reset = '\x1B[0m';
  static const red = '\x1B[31m';
  static const green = '\x1B[32m';
  static const yellow = '\x1B[33m';
  static const blue = '\x1B[34m';
  static const bold = '\x1B[1m';

  static String colorize(String text, String color) =&gt; '$color$text$reset';
  static String error(String text) =&gt; colorize('[ERROR] $text', red);
  static String success(String text) =&gt; colorize('[OK] $text', green);
  static String warning(String text) =&gt; colorize('[WARN] $text', yellow);
  static String info(String text) =&gt; colorize('[INFO] $text', blue);
}

void main() {
  print(Colors.success('Server started'));
  print(Colors.warning('Low disk space'));
  print(Colors.error('Connection refused'));
  print(Colors.info('Processing 100 items'));
}</code></pre>
<h2 id="example">CLI App Example: Task Manager</h2>
<pre><code>import 'dart:io';
import 'dart:convert';

class TaskManager {
  final String _file = 'tasks.json';
  List&lt;Map&lt;String, dynamic&gt;&gt; _tasks = [];

  void load() {
    var f = File(_file);
    if (f.existsSync()) {
      _tasks = List.from(jsonDecode(f.readAsStringSync()));
    }
  }

  void save() =&gt; File(_file).writeAsStringSync(jsonEncode(_tasks));

  void add(String title) {
    _tasks.add({'id': DateTime.now().millisecondsSinceEpoch, 'title': title, 'done': false});
    save();
    print('Added: $title');
  }

  void list() {
    if (_tasks.isEmpty) { print('No tasks'); return; }
    for (var t in _tasks) {
      print('[$${t['done'] ? 'x' : ' '}] $${t['id']}: $${t['title']}');
    }
  }

  void complete(int id) {
    var task = _tasks.firstWhere((t) =&gt; t['id'] == id, orElse: () =&gt; {});
    if (task.isEmpty) { print('Task not found'); return; }
    task['done'] = true;
    save();
    print('Completed: $${task['title']}');
  }
}

void main(List&lt;String&gt; args) {
  var manager = TaskManager()..load();
  switch (args.isEmpty ? 'list' : args[0]) {
    case 'add':
      manager.add(args.skip(1).join(' '));
    case 'done':
      manager.complete(int.parse(args[1]));
    default:
      manager.list();
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you write to stderr in Dart CLI?</strong></p>
<ul><li>A) print()</li><li>B) console.error()</li><li>C) stderr.writeln() ✅</li><li>D) System.err.println()</li></ul>
<p><strong>2. What does exit(1) signify in a CLI app?</strong></p>
<ul><li>A) The app ran successfully</li><li>B) The app exited with an error (non-zero code) ✅</li><li>C) Dart-specific success code</li><li>D) Restart the app</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Dart CLI apps start with <code>void main(List&lt;String&gt; args)</code>. Read stdin with <code>stdin.readLineSync()</code>, write to stdout/stderr, and exit with <code>exit(code)</code>. Use the <code>args</code> package for complex argument parsing. Use ANSI codes for colored terminal output. Dart CLI apps compile to native binaries with <code>dart compile exe</code>.</p>
    $dart_86$,
    'Build Dart CLI applications — command-line argument parsing, stdin reading, colored terminal output with ANSI codes, and a complete task manager CLI example.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,cli', NOW(), 86, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Dart Code Style and Best Practices', 'dart-code-style',
    $dart_87$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#naming">Naming Conventions</a></li><li><a href="#formatting">Formatting</a></li><li><a href="#effective">Effective Dart Guidelines</a></li><li><a href="#lints">Lints and Analysis</a></li><li><a href="#example">Refactoring Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="naming">Naming Conventions</h2>
<pre><code>// Classes, enums, typedefs — UpperCamelCase:
class UserProfile {}
enum AccountStatus { active, inactive }
typedef EventHandler = void Function(String);

// Variables, functions, parameters — lowerCamelCase:
var userName = 'Alice';
int calculateTotal(int price, int quantity) =&gt; price * quantity;

// Constants — lowerCamelCase (not SCREAMING_SNAKE):
const maxRetries = 3;
const defaultTimeout = Duration(seconds: 30);

// Private members — prefix with underscore:
class _InternalHelper {}
int _privateCounter = 0;

// Libraries and packages — lowercase_with_underscores:
// my_library.dart, user_service.dart

// Booleans — use positive names:
bool isActive = true;   // ✅
bool hasPermission = true;  // ✅
bool notDisabled = true; // ❌ (negative name)</code></pre>
<h2 id="formatting">Formatting</h2>
<pre><code># Format all files:
dart format .

# Check without modifying:
dart format --output=none --set-exit-if-changed .

# Dart uses 2-space indentation
# Max line length: 80 characters (soft limit)
# Trailing commas in multi-line collections preserve formatting:</code></pre>
<pre><code>// Good — trailing commas keep items on separate lines after formatting:
var config = Config(
  host: 'localhost',
  port: 5432,
  database: 'mydb',
  ssl: true,   // ← trailing comma
);

// Good — short enough to be on one line:
var point = Point(x: 10, y: 20);</code></pre>
<h2 id="effective">Effective Dart Guidelines</h2>
<pre><code>// PREFER final for variables that don't change:
final userName = 'Alice';   // ✅
var userName = 'Alice';     // ❌ (misleads — looks mutable)

// USE type inference — don't add obvious types:
var count = 0;              // ✅
int count = 0;              // ❌ (redundant)
List&lt;User&gt; users = [];     // ✅ (type adds info)

// AVOID using dynamic:
dynamic value = 'text';    // ❌
Object? value = 'text';    // ✅ (type-safe)

// PREFER expression bodies for simple functions:
String greet(String name) =&gt; 'Hello, $name!';   // ✅
String greet(String name) { return 'Hello, $name!'; } // ❌

// USE ?? for null defaults:
String display = value ?? 'N/A';  // ✅
String display = value != null ? value : 'N/A'; // ❌

// USE collection if/for:
var items = [
  'base',
  if (isAdmin) 'admin-panel',
  for (var tag in tags) 'tag:$tag',
];</code></pre>
<h2 id="lints">Lints and Analysis</h2>
<pre><code># analysis_options.yaml
include: package:lints/recommended.yaml

linter:
  rules:
    prefer_final_locals: true
    avoid_print: true
    prefer_const_constructors: true
    sort_constructors_first: true

# Run analysis:
dart analyze</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What naming convention do Dart classes use?</strong></p>
<ul><li>A) snake_case</li><li>B) SCREAMING_SNAKE_CASE</li><li>C) UpperCamelCase ✅</li><li>D) lower-kebab-case</li></ul>
<p><strong>2. What does dart format . do?</strong></p>
<ul><li>A) Checks for errors</li><li>B) Automatically formats all Dart files in the directory ✅</li><li>C) Compiles the project</li><li>D) Generates documentation</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Follow Effective Dart: use <code>UpperCamelCase</code> for types, <code>lowerCamelCase</code> for variables and functions, and <code>_prefix</code> for private members. Prefer <code>final</code> over <code>var</code>, use type inference, and write expression bodies for simple functions. Format with <code>dart format</code> and check with <code>dart analyze</code>. Add an <code>analysis_options.yaml</code> with recommended lints.</p>
    $dart_87$,
    'Dart code style and best practices — Effective Dart naming conventions, formatting with dart format, type inference, lints with analysis_options.yaml, and idiomatic patterns.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,best-practices', NOW(), 87, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Server-side Dart with Shelf', 'dart-shelf-server',
    $dart_88$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#setup">Setup</a></li><li><a href="#basic">Basic Server</a></li><li><a href="#routing">Routing</a></li><li><a href="#middleware">Middleware</a></li><li><a href="#json-api">JSON REST API</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="setup">Setup</h2>
<pre><code># pubspec.yaml
dependencies:
  shelf: ^1.4.0
  shelf_router: ^1.1.0</code></pre>
<h2 id="basic">Basic Server</h2>
<pre><code>import 'dart:io';
import 'package:shelf/shelf.dart';
import 'package:shelf/shelf_io.dart' as shelf_io;

Response _handler(Request request) {
  return Response.ok('Hello, World!', headers: {'Content-Type': 'text/plain'});
}

void main() async {
  var handler = const Pipeline()
      .addMiddleware(logRequests())
      .addHandler(_handler);

  var server = await shelf_io.serve(handler, '0.0.0.0', 8080);
  print('Server running on http://localhost:$${server.port}');
}</code></pre>
<h2 id="routing">Routing</h2>
<pre><code>import 'package:shelf_router/shelf_router.dart';

void main() async {
  var router = Router();

  router.get('/', (Request req) =&gt; Response.ok('Welcome!'));
  router.get('/hello/&lt;name&gt;', (Request req, String name) =&gt;
      Response.ok('Hello, $name!'));
  router.get('/users', (Request req) =&gt; Response.ok('All users'));
  router.post('/users', (Request req) async {
    var body = await req.readAsString();
    return Response.ok('Created: $body', headers: {'Content-Type': 'application/json'});
  });

  var handler = const Pipeline()
      .addMiddleware(logRequests())
      .addHandler(router.call);

  await shelf_io.serve(handler, 'localhost', 8080);
  print('Running on http://localhost:8080');
}</code></pre>
<h2 id="middleware">Middleware</h2>
<pre><code>// CORS middleware
Middleware corsMiddleware() {
  return (Handler handler) {
    return (Request request) async {
      if (request.method == 'OPTIONS') {
        return Response.ok('', headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
          'Access-Control-Allow-Headers': 'Content-Type, Authorization',
        });
      }
      final response = await handler(request);
      return response.change(headers: {
        'Access-Control-Allow-Origin': '*',
      });
    };
  };
}

// Auth middleware
Middleware authMiddleware(String secretKey) {
  return (Handler handler) {
    return (Request req) async {
      var auth = req.headers['Authorization'];
      if (auth == null || !auth.startsWith('Bearer ')) {
        return Response.forbidden('Unauthorized');
      }
      return handler(req);
    };
  };
}</code></pre>
<h2 id="json-api">JSON REST API</h2>
<pre><code>import 'dart:convert';
import 'package:shelf/shelf.dart';
import 'package:shelf_router/shelf_router.dart';
import 'package:shelf/shelf_io.dart' as io;

final _users = &lt;int, Map&lt;String, dynamic&gt;&gt;{
  1: {'id': 1, 'name': 'Alice', 'email': 'alice@example.com'},
  2: {'id': 2, 'name': 'Bob',   'email': 'bob@example.com'},
};

Response jsonResponse(dynamic data, {int status = 200}) =&gt; Response(
  status,
  body: jsonEncode(data),
  headers: {'Content-Type': 'application/json'},
);

void main() async {
  var router = Router()
    ..get('/users', (req) =&gt; jsonResponse(_users.values.toList()))
    ..get('/users/&lt;id&gt;', (req, String id) {
      var user = _users[int.tryParse(id)];
      return user != null ? jsonResponse(user) : Response.notFound('Not found');
    })
    ..post('/users', (req) async {
      var body = jsonDecode(await req.readAsString());
      var id = _users.length + 1;
      _users[id] = {'id': id, ...body};
      return jsonResponse(_users[id], status: 201);
    });

  var handler = Pipeline().addMiddleware(logRequests()).addHandler(router.call);
  await io.serve(handler, 'localhost', 8080);
  print('API on http://localhost:8080');
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What Dart package provides HTTP server functionality?</strong></p>
<ul><li>A) dart:http</li><li>B) shelf ✅</li><li>C) dart:server</li><li>D) http_server</li></ul>
<p><strong>2. What does Pipeline().addMiddleware().addHandler() create?</strong></p>
<ul><li>A) A router</li><li>B) A chain of request handlers where middleware wraps the final handler ✅</li><li>C) An async stream</li><li>D) A web socket connection</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use the <code>shelf</code> package for server-side Dart HTTP servers. Define routes with <code>shelf_router</code>'s <code>Router</code>. Chain middleware (logging, CORS, auth) with <code>Pipeline().addMiddleware()</code>. Return JSON with a helper that sets <code>Content-Type: application/json</code>. Start the server with <code>shelf_io.serve(handler, host, port)</code>.</p>
    $dart_88$,
    'Build server-side Dart applications with Shelf — HTTP routing with shelf_router, middleware for CORS and auth, and a complete JSON REST API server.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,server', NOW(), 88, 'PUBLISHED', 'ACT', 'system'
);
