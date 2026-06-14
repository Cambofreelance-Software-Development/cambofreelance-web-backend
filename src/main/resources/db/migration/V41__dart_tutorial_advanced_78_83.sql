-- Section 13: File I/O & JSON (Lessons 78–83, Advanced)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'File Operations in Dart', 'dart-file-operations',
    $dart_78$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#intro">Introduction</a></li><li><a href="#read">Reading Files</a></li><li><a href="#write">Writing Files</a></li><li><a href="#append">Appending to Files</a></li><li><a href="#check">Checking and Deleting Files</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="intro">Introduction</h2>
<p>Dart's <code>dart:io</code> library provides the <code>File</code> class for reading and writing files on the filesystem. File operations are asynchronous (return Futures) and synchronous (with <code>Sync</code> suffix). Always prefer async versions in production code.</p>
<div class="note-box"><strong>Note:</strong> <code>dart:io</code> is available only in Dart native (CLI, server, Flutter). It is NOT available in Dart web/DartPad.</div>
<h2 id="read">Reading Files</h2>
<pre><code>import 'dart:io';

Future&lt;void&gt; readExamples() async {
  var file = File('example.txt');

  // Read entire file as String:
  String content = await file.readAsString();
  print(content);

  // Read line by line (efficient for large files):
  List&lt;String&gt; lines = await file.readAsLines();
  for (var line in lines) {
    print(line);
  }

  // Read as bytes:
  List&lt;int&gt; bytes = await file.readAsBytes();
  print('File size: $${bytes.length} bytes');

  // Stream large files (memory-efficient):
  var stream = file.openRead();
  await for (var chunk in stream) {
    // process chunk (List&lt;int&gt;)
  }
}</code></pre>
<h2 id="write">Writing Files</h2>
<pre><code>Future&lt;void&gt; writeExamples() async {
  var file = File('output.txt');

  // Write a string (overwrites existing content):
  await file.writeAsString('Hello, Dart File I/O!\n');

  // Write with encoding:
  await file.writeAsString(
    'Second write',
    mode: FileMode.write,  // default — overwrite
    encoding: const Utf8Codec(),
  );

  // Write bytes:
  await file.writeAsBytes([72, 101, 108, 108, 111]);  // 'Hello' in ASCII
}</code></pre>
<h2 id="append">Appending to Files</h2>
<pre><code>Future&lt;void&gt; appendExample() async {
  var file = File('log.txt');
  var timestamp = DateTime.now().toIso8601String();

  await file.writeAsString(
    '$timestamp - Log entry\n',
    mode: FileMode.append,  // append instead of overwrite
  );
}</code></pre>
<h2 id="check">Checking and Deleting Files</h2>
<pre><code>Future&lt;void&gt; fileManagement() async {
  var file = File('temp.txt');

  // Check existence:
  bool exists = await file.exists();
  print('Exists: $exists');

  // Get file stat:
  var stat = await file.stat();
  print('Size: $${stat.size} bytes');
  print('Modified: $${stat.modified}');

  // Copy a file:
  await file.copy('temp_backup.txt');

  // Rename/move:
  var renamed = await file.rename('renamed.txt');
  print('Renamed to: $${renamed.path}');

  // Delete:
  if (await renamed.exists()) await renamed.delete();

  // Working with directories:
  var dir = Directory('my_folder');
  await dir.create(recursive: true);
  var listing = await dir.list().toList();
  print('Files: $${listing.length}');
}</code></pre>
<h2 id="example">Full Example: CSV Logger</h2>
<pre><code>import 'dart:io';

class CsvLogger {
  final File _file;

  CsvLogger(String path) : _file = File(path);

  Future&lt;void&gt; initialize() async {
    if (!await _file.exists()) {
      await _file.writeAsString('timestamp,level,message\n');
    }
  }

  Future&lt;void&gt; log(String level, String message) async {
    var row = '$${DateTime.now().toIso8601String()},$level,"$message"\n';
    await _file.writeAsString(row, mode: FileMode.append);
  }

  Future&lt;List&lt;String&gt;&gt; readLogs() =&gt; _file.readAsLines();
}

Future&lt;void&gt; main() async {
  var logger = CsvLogger('app.log');
  await logger.initialize();
  await logger.log('INFO', 'Application started');
  await logger.log('WARNING', 'Low memory');
  await logger.log('ERROR', 'Connection refused');
  var logs = await logger.readLogs();
  logs.forEach(print);
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which FileMode appends to an existing file instead of overwriting?</strong></p>
<ul><li>A) FileMode.write</li><li>B) FileMode.append ✅</li><li>C) FileMode.add</li><li>D) FileMode.open</li></ul>
<p><strong>2. Which dart:io class is used for file operations?</strong></p>
<ul><li>A) IO</li><li>B) FileSystem</li><li>C) File ✅</li><li>D) FileHandler</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>dart:io</code>'s <code>File</code> class for reading (<code>readAsString</code>, <code>readAsLines</code>, <code>readAsBytes</code>) and writing (<code>writeAsString</code>, <code>writeAsBytes</code>). Use <code>FileMode.append</code> to add to existing files. Always check <code>file.exists()</code> before operations. Use streaming APIs for large files to avoid loading everything into memory.</p>
    $dart_78$,
    'Learn Dart file operations with dart:io — read files as strings, lines or bytes, write and append, check existence, copy, rename, delete, and stream large files.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,file-io', NOW(), 78, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'JSON Parsing in Dart', 'dart-json-parsing',
    $dart_80$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is JSON?</a></li><li><a href="#decode">Decoding JSON</a></li><li><a href="#encode">Encoding to JSON</a></li><li><a href="#models">JSON to Model Classes</a></li><li><a href="#nested">Nested JSON</a></li><li><a href="#example">Full Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is JSON?</h2>
<p>JSON (JavaScript Object Notation) is the standard format for data exchange between clients and servers. Dart's <code>dart:convert</code> library provides <code>jsonDecode</code> and <code>jsonEncode</code> for parsing and producing JSON.</p>
<h2 id="decode">Decoding JSON</h2>
<pre><code>import 'dart:convert';

void main() {
  // JSON string to Dart object:
  String jsonStr = '{"name":"Alice","age":30,"active":true}';
  Map&lt;String, dynamic&gt; user = jsonDecode(jsonStr);
  print(user['name']);    // Alice
  print(user['age']);     // 30

  // JSON array:
  String jsonArray = '[1, 2, 3, 4, 5]';
  List&lt;dynamic&gt; numbers = jsonDecode(jsonArray);
  print(numbers.cast&lt;int&gt;());  // [1, 2, 3, 4, 5]

  // Complex nested JSON:
  String complex = '''
  {
    "users": [
      {"id": 1, "name": "Alice", "roles": ["admin", "user"]},
      {"id": 2, "name": "Bob",   "roles": ["user"]}
    ],
    "total": 2
  }
  ''';
  var data = jsonDecode(complex) as Map&lt;String, dynamic&gt;;
  var users = data['users'] as List;
  for (var u in users) {
    print('$${u['name']}: $${u['roles'].join(', ')}');
  }
}</code></pre>
<h2 id="encode">Encoding to JSON</h2>
<pre><code>import 'dart:convert';

void main() {
  var user = {
    'name': 'Bob',
    'age': 25,
    'scores': [98, 87, 92],
    'address': {'city': 'Phnom Penh', 'country': 'Cambodia'}
  };

  // Compact JSON:
  String compact = jsonEncode(user);
  print(compact);

  // Pretty-printed JSON:
  var encoder = JsonEncoder.withIndent('  ');
  print(encoder.convert(user));
}</code></pre>
<h2 id="models">JSON to Model Classes</h2>
<pre><code>class Product {
  final int id;
  final String name;
  final double price;
  final bool inStock;
  final List&lt;String&gt; tags;

  Product({
    required this.id,
    required this.name,
    required this.price,
    required this.inStock,
    required this.tags,
  });

  factory Product.fromJson(Map&lt;String, dynamic&gt; json) {
    return Product(
      id: json['id'] as int,
      name: json['name'] as String,
      price: (json['price'] as num).toDouble(),
      inStock: json['in_stock'] as bool,
      tags: List&lt;String&gt;.from(json['tags'] ?? []),
    );
  }

  Map&lt;String, dynamic&gt; toJson() =&gt; {
    'id': id,
    'name': name,
    'price': price,
    'in_stock': inStock,
    'tags': tags,
  };

  @override String toString() =&gt; 'Product($id: $name @ \$$price)';
}

void main() {
  var json = '{"id":1,"name":"Dart Book","price":29.99,"in_stock":true,"tags":["programming","dart"]}';
  var product = Product.fromJson(jsonDecode(json));
  print(product);  // Product(1: Dart Book @ $29.99)
  print(jsonEncode(product.toJson()));
}</code></pre>
<h2 id="nested">Nested JSON Models</h2>
<pre><code>class Address {
  final String city, country;
  Address({required this.city, required this.country});
  factory Address.fromJson(Map&lt;String, dynamic&gt; j) =&gt;
      Address(city: j['city'], country: j['country']);
  Map&lt;String, dynamic&gt; toJson() =&gt; {'city': city, 'country': country};
}

class User {
  final String name;
  final Address address;
  final List&lt;Product&gt; cart;

  User({required this.name, required this.address, required this.cart});

  factory User.fromJson(Map&lt;String, dynamic&gt; j) =&gt; User(
    name: j['name'],
    address: Address.fromJson(j['address']),
    cart: (j['cart'] as List).map((p) =&gt; Product.fromJson(p)).toList(),
  );
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which function converts a JSON string to a Dart object?</strong></p>
<ul><li>A) jsonParse</li><li>B) json.parse</li><li>C) jsonDecode ✅</li><li>D) fromJson</li></ul>
<p><strong>2. What does jsonEncode return?</strong></p>
<ul><li>A) A Map</li><li>B) A JSON-formatted String ✅</li><li>C) A Future</li><li>D) A List</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>jsonDecode(String)</code> to parse JSON into Dart maps/lists. Use <code>jsonEncode(Object)</code> to convert Dart objects to JSON strings. Create model classes with <code>fromJson</code> factory constructors and <code>toJson()</code> methods for type-safe JSON handling. Use <code>JsonEncoder.withIndent</code> for pretty-printed output.</p>
    $dart_80$,
    'Master Dart JSON parsing with dart:convert — decode JSON strings, encode Dart objects, build fromJson/toJson model classes, and handle nested JSON structures.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,json', NOW(), 80, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'HTTP Requests in Dart', 'dart-http-requests',
    $dart_82$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#setup">Setup</a></li><li><a href="#get">GET Requests</a></li><li><a href="#post">POST Requests</a></li><li><a href="#error">Error Handling</a></li><li><a href="#client">HTTP Client Wrapper</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="setup">Setup</h2>
<p>Add the <code>http</code> package to your <code>pubspec.yaml</code>:</p>
<pre><code>dependencies:
  http: ^1.1.0</code></pre>
<p>Then run <code>dart pub get</code> and import it:</p>
<pre><code>import 'package:http/http.dart' as http;
import 'dart:convert';</code></pre>
<h2 id="get">GET Requests</h2>
<pre><code>Future&lt;void&gt; fetchPosts() async {
  var url = Uri.parse('https://jsonplaceholder.typicode.com/posts');
  var response = await http.get(url);

  if (response.statusCode == 200) {
    var posts = jsonDecode(response.body) as List;
    print('Loaded $${posts.length} posts');
    for (var post in posts.take(3)) {
      print('- $${post['title']}');
    }
  } else {
    print('Failed: $${response.statusCode}');
  }
}

// With headers and query parameters:
Future&lt;void&gt; fetchWithParams() async {
  var uri = Uri.https('api.example.com', '/users', {
    'page': '1',
    'limit': '10',
  });
  var response = await http.get(uri, headers: {
    'Authorization': 'Bearer your-token-here',
    'Accept': 'application/json',
  });
  print(response.body);
}</code></pre>
<h2 id="post">POST Requests</h2>
<pre><code>Future&lt;Map&lt;String, dynamic&gt;&gt; createPost(String title, String body) async {
  var url = Uri.parse('https://jsonplaceholder.typicode.com/posts');

  var response = await http.post(
    url,
    headers: {
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode({
      'title': title,
      'body': body,
      'userId': 1,
    }),
  );

  if (response.statusCode == 201) {
    return jsonDecode(response.body);
  }
  throw Exception('Failed to create post: $${response.statusCode}');
}

void main() async {
  var post = await createPost('My Dart Post', 'Learning HTTP in Dart');
  print('Created: $${post['id']}');
}</code></pre>
<h2 id="error">Error Handling</h2>
<pre><code>import 'package:http/http.dart' as http;

Future&lt;String&gt; safeFetch(String url) async {
  try {
    var response = await http.get(Uri.parse(url))
        .timeout(Duration(seconds: 10));  // timeout!

    if (response.statusCode &gt;= 400) {
      throw HttpException('HTTP $${response.statusCode}', uri: Uri.parse(url));
    }
    return response.body;
  } on SocketException {
    throw Exception('No internet connection');
  } on TimeoutException {
    throw Exception('Request timed out');
  } on HttpException catch (e) {
    throw Exception('HTTP error: $e');
  }
}</code></pre>
<h2 id="client">HTTP Client Wrapper</h2>
<pre><code>class ApiService {
  static const _base = 'https://jsonplaceholder.typicode.com';
  final _client = http.Client();

  Map&lt;String, String&gt; get _headers =&gt; {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  };

  Future&lt;T&gt; _get&lt;T&gt;(String path, T Function(dynamic) parser) async {
    final res = await _client.get(Uri.parse('$_base$path'), headers: _headers);
    if (res.statusCode != 200) throw Exception('Error $${res.statusCode}');
    return parser(jsonDecode(res.body));
  }

  Future&lt;List&lt;Map&gt;&gt; getPosts() =&gt;
      _get('/posts', (body) =&gt; (body as List).cast&lt;Map&gt;());

  Future&lt;Map&gt; getPost(int id) =&gt;
      _get('/posts/$id', (body) =&gt; body as Map);

  void dispose() =&gt; _client.close();
}

void main() async {
  var api = ApiService();
  try {
    var posts = await api.getPosts();
    print('Total: $${posts.length}');
    var post = await api.getPost(1);
    print('Post 1: $${post['title']}');
  } finally {
    api.dispose();
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What HTTP status code indicates a resource was successfully created?</strong></p>
<ul><li>A) 200</li><li>B) 201 ✅</li><li>C) 204</li><li>D) 301</li></ul>
<p><strong>2. Why should you use http.Client() instead of top-level http.get() in long-running apps?</strong></p>
<ul><li>A) It is faster</li><li>B) It reuses connections for better performance and allows proper cleanup ✅</li><li>C) It supports HTTPS</li><li>D) It handles JSON automatically</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use the <code>http</code> package (<code>dart pub add http</code>) for HTTP requests. <code>http.get()</code> for data retrieval, <code>http.post()</code> for creating resources. Always check <code>response.statusCode</code>. Use <code>http.Client()</code> for connection reuse in production. Add timeout with <code>.timeout(Duration(...))</code> and handle <code>SocketException</code> for network errors.</p>
    $dart_82$,
    'Learn HTTP requests in Dart using the http package — GET, POST, custom headers, error handling, timeouts, and building a reusable ApiService wrapper.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,http', NOW(), 82, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'REST API Client in Dart', 'dart-rest-api',
    $dart_83$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#design">API Client Design</a></li><li><a href="#auth">Authentication</a></li><li><a href="#crud">Full CRUD Example</a></li><li><a href="#interceptors">Interceptors Pattern</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="design">API Client Design</h2>
<p>A well-designed REST API client centralizes base URL, authentication, error handling, and response parsing. This prevents repetition and makes switching APIs easier.</p>
<pre><code>import 'package:http/http.dart' as http;
import 'dart:convert';
import 'dart:io';

class RestClient {
  final String baseUrl;
  final String? authToken;
  final http.Client _http;

  RestClient({required this.baseUrl, this.authToken})
      : _http = http.Client();

  Map&lt;String, String&gt; get _headers =&gt; {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    if (authToken != null) 'Authorization': 'Bearer $authToken',
  };

  Uri _uri(String path, [Map&lt;String, String&gt;? params]) =&gt;
      Uri.parse('$baseUrl$path').replace(queryParameters: params);

  Future&lt;dynamic&gt; get(String path, {Map&lt;String, String&gt;? params}) async {
    final res = await _http.get(_uri(path, params), headers: _headers)
        .timeout(Duration(seconds: 15));
    return _handleResponse(res);
  }

  Future&lt;dynamic&gt; post(String path, Map&lt;String, dynamic&gt; body) async {
    final res = await _http.post(_uri(path), headers: _headers, body: jsonEncode(body))
        .timeout(Duration(seconds: 15));
    return _handleResponse(res);
  }

  Future&lt;dynamic&gt; put(String path, Map&lt;String, dynamic&gt; body) async {
    final res = await _http.put(_uri(path), headers: _headers, body: jsonEncode(body))
        .timeout(Duration(seconds: 15));
    return _handleResponse(res);
  }

  Future&lt;void&gt; delete(String path) async {
    final res = await _http.delete(_uri(path), headers: _headers)
        .timeout(Duration(seconds: 15));
    _handleResponse(res);
  }

  dynamic _handleResponse(http.Response res) {
    if (res.statusCode &gt;= 200 &amp;&amp; res.statusCode &lt; 300) {
      return res.body.isEmpty ? null : jsonDecode(res.body);
    }
    throw ApiException(res.statusCode, res.body);
  }

  void dispose() =&gt; _http.close();
}

class ApiException implements Exception {
  final int status;
  final String body;
  ApiException(this.status, this.body);
  @override String toString() =&gt; 'ApiException($status): $body';
}</code></pre>
<h2 id="crud">Full CRUD Example</h2>
<pre><code>class Todo {
  final int? id;
  final String title;
  final bool completed;

  Todo({this.id, required this.title, this.completed = false});

  factory Todo.fromJson(Map&lt;String, dynamic&gt; j) =&gt;
      Todo(id: j['id'], title: j['title'], completed: j['completed'] ?? false);

  Map&lt;String, dynamic&gt; toJson() =&gt;
      {'title': title, 'completed': completed, 'userId': 1};

  @override String toString() =&gt; '[$${completed ? 'x' : ' '}] $title (id: $id)';
}

class TodoRepository {
  final RestClient _client;
  TodoRepository(this._client);

  Future&lt;List&lt;Todo&gt;&gt; getAll() async {
    var data = await _client.get('/todos', params: {'_limit': '5'});
    return (data as List).map((j) =&gt; Todo.fromJson(j)).toList();
  }

  Future&lt;Todo&gt; getById(int id) async {
    var data = await _client.get('/todos/$id');
    return Todo.fromJson(data);
  }

  Future&lt;Todo&gt; create(Todo todo) async {
    var data = await _client.post('/todos', todo.toJson());
    return Todo.fromJson(data);
  }

  Future&lt;Todo&gt; update(int id, Todo todo) async {
    var data = await _client.put('/todos/$id', todo.toJson());
    return Todo.fromJson(data);
  }

  Future&lt;void&gt; delete(int id) =&gt; _client.delete('/todos/$id');
}

void main() async {
  var client = RestClient(baseUrl: 'https://jsonplaceholder.typicode.com');
  var repo = TodoRepository(client);

  try {
    var todos = await repo.getAll();
    todos.forEach(print);

    var newTodo = await repo.create(Todo(title: 'Learn Dart REST'));
    print('Created: $newTodo');

    var updated = await repo.update(newTodo.id!, Todo(title: 'Done!', completed: true));
    print('Updated: $updated');
  } on ApiException catch (e) {
    print('API Error: $e');
  } finally {
    client.dispose();
  }
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which HTTP method is typically used to update an entire resource?</strong></p>
<ul><li>A) POST</li><li>B) PATCH (partial) vs PUT (full) ✅</li><li>C) GET</li><li>D) DELETE</li></ul>
<p><strong>2. What does a REST API client's _handleResponse method typically do?</strong></p>
<ul><li>A) Sends the request</li><li>B) Checks status code and throws on errors, decodes body on success ✅</li><li>C) Adds headers</li><li>D) Serializes the request body</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Build REST API clients by centralizing base URL, headers, timeout, and error handling in a <code>RestClient</code> class. Pair it with a repository class that maps HTTP responses to domain models with <code>fromJson</code>. Handle <code>ApiException</code> for non-2xx status codes. Always call <code>client.dispose()</code> to close the underlying HTTP connection pool.</p>
    $dart_83$,
    'Build a complete Dart REST API client — centralized base URL, auth headers, CRUD operations, response handling, repository pattern, and error management.',
    'TUTORIAL', 'Dart Team', 'dart,programming,advanced,rest-api', NOW(), 83, 'PUBLISHED', 'ACT', 'system'
);
