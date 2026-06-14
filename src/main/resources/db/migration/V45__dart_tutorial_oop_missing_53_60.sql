-- OOP Lessons 53, 54, 55, 56, 58, 60 (missing from V37/V38 — fills the gaps)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Getters and Setters in Dart', 'dart-getters-setters',
    $dart_53$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#what">What are Getters and Setters?</a></li><li><a href="#getter">Defining a Getter</a></li><li><a href="#setter">Defining a Setter</a></li><li><a href="#computed">Computed Properties</a></li><li><a href="#encapsulation">Encapsulation with Private Fields</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Getters and Setters?</h2>
<p>Getters and setters are special methods that allow you to read and write the value of a property. They look like property accesses to the caller but execute method logic behind the scenes. This is how Dart implements <strong>encapsulation</strong> — hiding internal implementation details while providing controlled access.</p>
<h2 id="getter">Defining a Getter</h2>
<p>A getter uses the <code>get</code> keyword and has no parameter list:</p>
<pre><code>class Circle {
  double radius;
  Circle(this.radius);

  // Getter: computed from radius
  double get diameter => radius * 2;
  double get area => 3.14159 * radius * radius;
  double get circumference => 2 * 3.14159 * radius;
}

void main() {
  var c = Circle(5.0);
  print(c.diameter);       // 10.0
  print(c.area.toStringAsFixed(2));          // 78.54
  print(c.circumference.toStringAsFixed(2)); // 31.42
}</code></pre>
<h2 id="setter">Defining a Setter</h2>
<p>A setter uses the <code>set</code> keyword and accepts exactly one parameter. It allows validation before assigning a value:</p>
<pre><code>class Temperature {
  double _celsius = 0.0;  // private backing field

  // Getter
  double get celsius => _celsius;

  // Setter — validates before assigning
  set celsius(double value) {
    if (value < -273.15) {
      throw ArgumentError(''Temperature below absolute zero: $value'');
    }
    _celsius = value;
  }

  // Computed getters for other units
  double get fahrenheit => _celsius * 9 / 5 + 32;
  double get kelvin => _celsius + 273.15;
}

void main() {
  var temp = Temperature();
  temp.celsius = 100;         // uses setter
  print(temp.fahrenheit);     // 212.0
  print(temp.kelvin);         // 373.15

  try {
    temp.celsius = -300;      // throws ArgumentError
  } on ArgumentError catch (e) {
    print(e.message);
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>212.0
373.15
Temperature below absolute zero: -300.0</pre></div>
<h2 id="computed">Computed Properties</h2>
<p>Getters excel at computed properties — values derived from other fields. They update automatically when the underlying data changes:</p>
<pre><code>class ShoppingCart {
  final List&lt;double&gt; _prices = [];

  void addItem(double price) => _prices.add(price);
  void removeItem(double price) => _prices.remove(price);

  double get subtotal => _prices.fold(0.0, (sum, p) => sum + p);
  double get tax => subtotal * 0.08;
  double get total => subtotal + tax;
  int get itemCount => _prices.length;
  bool get isEmpty => _prices.isEmpty;
}

void main() {
  var cart = ShoppingCart();
  cart.addItem(29.99);
  cart.addItem(14.99);
  cart.addItem(4.99);

  print(''Items: \$${cart.itemCount}'');
  print(''Subtotal: \$\$${cart.subtotal.toStringAsFixed(2)}'');
  print(''Tax: \$\$${cart.tax.toStringAsFixed(2)}'');
  print(''Total: \$\$${cart.total.toStringAsFixed(2)}'');
}</code></pre>
<h2 id="encapsulation">Encapsulation with Private Fields</h2>
<p>In Dart, fields starting with <code>_</code> are private to the library file. Getters and setters provide controlled public access:</p>
<pre><code>class BankAccount {
  String _owner;
  double _balance;

  BankAccount(this._owner, double initialBalance)
      : _balance = initialBalance >= 0 ? initialBalance : 0;

  String get owner => _owner;
  double get balance => _balance;  // read-only — no setter

  void deposit(double amount) {
    if (amount <= 0) throw ArgumentError(''Deposit must be positive'');
    _balance += amount;
  }

  void withdraw(double amount) {
    if (amount <= 0) throw ArgumentError(''Withdrawal must be positive'');
    if (amount > _balance) throw StateError(''Insufficient funds'');
    _balance -= amount;
  }
}

void main() {
  var account = BankAccount(''Alice'', 1000.0);
  print(account.balance);  // 1000.0
  account.deposit(500);
  print(account.balance);  // 1500.0
  // account._balance = 999999;  // ERROR: private field
}</code></pre>
<h2 id="example">Full Example: Product Inventory</h2>
<pre><code>class Product {
  final String id;
  String _name;
  double _price;
  int _stock;

  Product({required this.id, required String name, required double price, int stock = 0})
      : _name = name, _price = price, _stock = stock;

  String get name => _name;
  set name(String value) {
    if (value.trim().isEmpty) throw ArgumentError(''Name cannot be blank'');
    _name = value.trim();
  }

  double get price => _price;
  set price(double value) {
    if (value < 0) throw ArgumentError(''Price cannot be negative'');
    _price = value;
  }

  int get stock => _stock;
  bool get inStock => _stock > 0;
  bool get lowStock => _stock > 0 &amp;&amp; _stock &lt;= 5;

  void restock(int units) {
    if (units &lt;= 0) throw ArgumentError(''Units must be positive'');
    _stock += units;
  }

  bool sell(int units) {
    if (units &gt; _stock) return false;
    _stock -= units;
    return true;
  }

  @override
  String toString() => ''Product($id, $_name, \$\$${_price.toStringAsFixed(2)}, stock=$_stock)'';
}

void main() {
  var p = Product(id: ''P001'', name: ''Dart Book'', price: 39.99, stock: 10);
  print(p);
  p.sell(3);
  print(''In stock: \$${p.inStock}, Low stock: \$${p.lowStock}'');
  p.price = 34.99;
  print(p);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Product(P001, Dart Book, $39.99, stock=10)
In stock: true, Low stock: false
Product(P001, Dart Book, $34.99, stock=7)</pre></div>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Create a <code>Rectangle</code> class with private <code>_width</code> and <code>_height</code> fields, validated setters (must be positive), and getters for <code>area</code>, <code>perimeter</code>, and <code>isSquare</code>.</li>
<li>Create a <code>Password</code> class with a setter that requires at least 8 characters, one digit, and one uppercase letter. The getter should return the hashed version (simulate with reversed string).</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. What keyword defines a getter in Dart?</strong></p>
<ul><li>A) property</li><li>B) get ✅</li><li>C) read</li><li>D) accessor</li></ul>
<p><strong>2. Can a Dart getter have parameters?</strong></p>
<ul><li>A) Yes, any number</li><li>B) Yes, exactly one</li><li>C) No, getters take no parameters ✅</li><li>D) Only optional parameters</li></ul>
<p><strong>3. What is the main benefit of a setter over direct field assignment?</strong></p>
<ul><li>A) Faster performance</li><li>B) Validation and encapsulation — logic runs before the value is stored ✅</li><li>C) Automatic type conversion</li><li>D) Thread safety</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Getters (<code>get</code>) and setters (<code>set</code>) provide controlled property access in Dart. Define getters for computed properties that derive from other fields. Use setters to validate input before assigning to private backing fields. Fields prefixed with <code>_</code> are private to the library file, enforcing encapsulation. Callers access getters and setters exactly like regular fields — the logic is transparent.</p>
    $dart_53$,
    'Learn Dart getters and setters — computed properties, validated setters, private backing fields, and encapsulation patterns for clean OOP design.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 53, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Static Members in Dart', 'dart-static-members',
    $dart_54$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#what">What are Static Members?</a></li><li><a href="#static-fields">Static Fields</a></li><li><a href="#static-methods">Static Methods</a></li><li><a href="#constants">Static Constants</a></li><li><a href="#factory">Static Factory Methods</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Static Members?</h2>
<p>Static members belong to the <em>class itself</em> rather than to any specific instance (object). They are shared across all instances of the class. Declare them with the <code>static</code> keyword. Access them using the class name, not an object reference.</p>
<h2 id="static-fields">Static Fields</h2>
<pre><code>class Counter {
  // Static field — shared across ALL Counter objects
  static int _count = 0;

  final int id;

  Counter() : id = ++_count;

  static int get count => _count;
  static void reset() => _count = 0;
}

void main() {
  print(Counter.count);   // 0
  var a = Counter();
  var b = Counter();
  var c = Counter();
  print(Counter.count);   // 3
  print(a.id);            // 1
  print(b.id);            // 2
  Counter.reset();
  print(Counter.count);   // 0
}</code></pre>
<h2 id="static-methods">Static Methods</h2>
<p>Static methods have no access to <code>this</code> — they cannot reference instance fields or methods. They are utility functions tied logically to the class:</p>
<pre><code>class MathUtils {
  // Cannot be instantiated — all static
  MathUtils._();  // private constructor

  static double square(double x) => x * x;
  static double cube(double x) => x * x * x;
  static double clamp(double value, double min, double max) =>
      value &lt; min ? min : (value &gt; max ? max : value);
  static bool isPrime(int n) {
    if (n &lt; 2) return false;
    for (int i = 2; i * i &lt;= n; i++) {
      if (n % i == 0) return false;
    }
    return true;
  }
}

void main() {
  print(MathUtils.square(5));        // 25.0
  print(MathUtils.cube(3));          // 27.0
  print(MathUtils.clamp(150, 0, 100));  // 100.0
  print(MathUtils.isPrime(17));      // true
  print(MathUtils.isPrime(15));      // false
}</code></pre>
<h2 id="constants">Static Constants</h2>
<pre><code>class Config {
  static const String appName = ''CamboFreelance'';
  static const String version = ''2.0.0'';
  static const int maxRetries = 3;
  static const Duration timeout = Duration(seconds: 30);
  static const Map&lt;String, String&gt; headers = {
    ''Content-Type'': ''application/json'',
    ''Accept'': ''application/json'',
  };
}

void main() {
  print(Config.appName);          // CamboFreelance
  print(Config.version);          // 2.0.0
  print(Config.timeout.inSeconds); // 30
}</code></pre>
<h2 id="factory">Static Factory Methods</h2>
<p>A common pattern is static factory methods — named constructors that return instances with validation or special initialization:</p>
<pre><code>class Color {
  final int r, g, b;

  const Color(this.r, this.g, this.b);

  // Static factory methods for named colors
  static const Color red   = Color(255, 0, 0);
  static const Color green = Color(0, 255, 0);
  static const Color blue  = Color(0, 0, 255);
  static const Color white = Color(255, 255, 255);
  static const Color black = Color(0, 0, 0);

  static Color fromHex(String hex) {
    hex = hex.replaceFirst(''#'', '''');
    return Color(
      int.parse(hex.substring(0, 2), radix: 16),
      int.parse(hex.substring(2, 4), radix: 16),
      int.parse(hex.substring(4, 6), radix: 16),
    );
  }

  @override
  String toString() => ''Color(r=$r, g=$g, b=$b)'';
}

void main() {
  print(Color.red);              // Color(r=255, g=0, b=0)
  print(Color.fromHex(''#1A2B3C''));  // Color(r=26, g=43, b=60)
}</code></pre>
<h2 id="example">Full Example: Singleton Pattern</h2>
<pre><code>class AppLogger {
  // Static field holds the single instance
  static AppLogger? _instance;
  static int _logCount = 0;

  // Private constructor
  AppLogger._();

  // Static getter returns the single instance
  static AppLogger get instance {
    _instance ??= AppLogger._();
    return _instance!;
  }

  void log(String level, String message) {
    _logCount++;
    final timestamp = DateTime.now().toIso8601String();
    print(''[$timestamp][$level] $message'');
  }

  void info(String msg) => log(''INFO'', msg);
  void error(String msg) => log(''ERROR'', msg);

  static int get logCount => _logCount;
}

void main() {
  var logger = AppLogger.instance;
  logger.info(''App started'');
  logger.error(''Something went wrong'');
  AppLogger.instance.info(''Same instance'');
  print(''Total logs: \$${AppLogger.logCount}'');
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>[timestamp][INFO] App started
[timestamp][ERROR] Something went wrong
[timestamp][INFO] Same instance
Total logs: 3</pre></div>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Create a <code>StringUtils</code> class with only static methods: <code>capitalize()</code>, <code>isPalindrome()</code>, <code>wordCount()</code>, and <code>reverse()</code>.</li>
<li>Implement a <code>UserIdGenerator</code> class with a static counter that auto-increments each time a new user ID is requested via a static method <code>nextId()</code>.</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. How do you access a static method?</strong></p>
<ul><li>A) Through an instance: <code>obj.method()</code></li><li>B) Through the class name: <code>ClassName.method()</code> ✅</li><li>C) Via the super keyword</li><li>D) Using the static keyword at the call site</li></ul>
<p><strong>2. Can a static method access instance fields?</strong></p>
<ul><li>A) Yes, directly</li><li>B) Yes, via this</li><li>C) No — static methods have no this reference ✅</li><li>D) Only if they are public</li></ul>
<p><strong>3. What keyword makes a class-level constant in Dart?</strong></p>
<ul><li>A) final</li><li>B) const (but not static)</li><li>C) static const ✅</li><li>D) readonly</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Static members belong to the class, not to instances. Static fields are shared across all objects of the class. Static methods cannot use <code>this</code> and are accessed via the class name. Use <code>static const</code> for compile-time constants. Common patterns include utility classes (all static methods), singleton instances (one shared object), and static factory methods for named constructors.</p>
    $dart_54$,
    'Master Dart static members — static fields shared across instances, static utility methods, static constants, factory methods, and the singleton pattern.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 54, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Interfaces in Dart', 'dart-interfaces',
    $dart_55$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#what">What are Interfaces?</a></li><li><a href="#implicit">Implicit Interfaces</a></li><li><a href="#implements">implements Keyword</a></li><li><a href="#multiple">Implementing Multiple Interfaces</a></li><li><a href="#abstract-interface">Abstract Classes as Interfaces</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Interfaces?</h2>
<p>An interface defines a <strong>contract</strong> — a set of methods and properties that a class promises to implement. Unlike inheritance (<code>extends</code>), implementing an interface does not share any code. The implementing class must provide its own implementation of every member. Dart has no dedicated <code>interface</code> keyword — every class implicitly defines an interface.</p>
<h2 id="implicit">Implicit Interfaces</h2>
<p>In Dart, every class automatically has an implicit interface. Any other class can implement it using <code>implements</code>. When you implement a class, you must provide your own implementation of ALL public members — you get no inherited code:</p>
<pre><code>class Animal {
  String name;
  Animal(this.name);

  void breathe() => print(''$name breathes'');
  void eat() => print(''$name eats'');
}

// Implements Animal''s interface — must define ALL methods
class Robot implements Animal {
  @override
  String name;  // must declare all fields too

  Robot(this.name);

  @override
  void breathe() => print(''$name uses power cells'');

  @override
  void eat() => print(''$name recharges'');
}</code></pre>
<h2 id="implements">implements Keyword</h2>
<pre><code>abstract class Printable {
  void print();
}

abstract class Saveable {
  Future&lt;bool&gt; save(String path);
}

abstract class Shareable {
  void share(String recipient);
}

class Document implements Printable, Saveable, Shareable {
  String content;
  Document(this.content);

  @override
  void print() => dart_core.print(''Printing: $content'');

  @override
  Future&lt;bool&gt; save(String path) async {
    dart_core.print(''Saving to $path'');
    return true;
  }

  @override
  void share(String recipient) =>
    dart_core.print(''Sharing with $recipient'');
}</code></pre>
<h2 id="multiple">Implementing Multiple Interfaces</h2>
<p>A class can implement multiple interfaces simultaneously — this is one of the key advantages of interfaces over inheritance:</p>
<pre><code>abstract class Flyable {
  void fly();
  double get maxAltitude;
}

abstract class Swimmable {
  void swim();
  double get maxDepth;
}

abstract class Runnable {
  void run();
  double get maxSpeed;
}

// Duck implements all three
class Duck implements Flyable, Swimmable, Runnable {
  final String name;
  Duck(this.name);

  @override void fly()  => print(''$name is flying!'');
  @override void swim() => print(''$name is swimming!'');
  @override void run()  => print(''$name is running!'');

  @override double get maxAltitude => 100.0;
  @override double get maxDepth => 2.0;
  @override double get maxSpeed => 10.0;
}

// Penguin can only swim and run
class Penguin implements Swimmable, Runnable {
  final String name;
  Penguin(this.name);

  @override void swim() => print(''$name swims fast!'');
  @override void run()  => print(''$name waddles!'');

  @override double get maxDepth => 500.0;
  @override double get maxSpeed => 5.0;
}

void main() {
  var duck = Duck(''Donald'');
  duck.fly();   // Donald is flying!
  duck.swim();  // Donald is swimming!

  var penguin = Penguin(''Tux'');
  penguin.swim(); // Tux swims fast!

  // Type checking
  print(duck is Flyable);     // true
  print(penguin is Flyable);  // false
  print(penguin is Swimmable); // true
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Donald is flying!
Donald is swimming!
Tux swims fast!
true
false
true</pre></div>
<h2 id="abstract-interface">Abstract Classes as Interfaces</h2>
<p>The most common pattern in Dart is to use <code>abstract class</code> to define interfaces. This gives you type checking via <code>is</code> and <code>as</code>, and you can add concrete helper methods:</p>
<pre><code>abstract class Repository&lt;T&gt; {
  Future&lt;T?&gt; findById(int id);
  Future&lt;List&lt;T&gt;&gt; findAll();
  Future&lt;T&gt; save(T entity);
  Future&lt;bool&gt; delete(int id);

  // Concrete helper — uses abstract methods
  Future&lt;bool&gt; exists(int id) async {
    return await findById(id) != null;
  }
}

class UserRepository implements Repository&lt;Map&lt;String, dynamic&gt;&gt; {
  final Map&lt;int, Map&lt;String, dynamic&gt;&gt; _store = {};
  int _nextId = 1;

  @override
  Future&lt;Map&lt;String, dynamic&gt;?&gt; findById(int id) async => _store[id];

  @override
  Future&lt;List&lt;Map&lt;String, dynamic&gt;&gt;&gt; findAll() async => _store.values.toList();

  @override
  Future&lt;Map&lt;String, dynamic&gt;&gt; save(Map&lt;String, dynamic&gt; entity) async {
    var id = entity[''id''] as int? ?? _nextId++;
    _store[id] = {...entity, ''id'': id};
    return _store[id]!;
  }

  @override
  Future&lt;bool&gt; delete(int id) async => _store.remove(id) != null;
}

void main() async {
  var repo = UserRepository();
  await repo.save({''name'': ''Alice'', ''email'': ''alice@example.com''});
  await repo.save({''name'': ''Bob'',   ''email'': ''bob@example.com''});
  print(await repo.findAll());
  print(await repo.exists(1));  // true
  print(await repo.exists(99)); // false
}</code></pre>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Define abstract classes <code>Readable</code>, <code>Writable</code>, and <code>Seekable</code>. Create a <code>FileStream</code> class that implements all three.</li>
<li>Define a <code>Comparable&lt;T&gt;</code>-like interface with <code>compareTo(T other)</code>. Implement it on a <code>Student</code> class that compares by GPA.</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. In Dart, what keyword is used to implement an interface?</strong></p>
<ul><li>A) extends</li><li>B) interface</li><li>C) implements ✅</li><li>D) uses</li></ul>
<p><strong>2. When you implement a class, do you inherit its method bodies?</strong></p>
<ul><li>A) Yes, all of them</li><li>B) Yes, only public ones</li><li>C) No — you must provide your own implementation for all members ✅</li><li>D) Only if you use @override</li></ul>
<p><strong>3. Can a Dart class implement more than one interface?</strong></p>
<ul><li>A) No — only one</li><li>B) Yes — multiple with implements A, B, C ✅</li><li>C) Only two</li><li>D) Only if they are abstract</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Every Dart class implicitly defines an interface. Use <code>implements</code> to adopt the contract of one or more classes without inheriting their code. The implementing class must override every public member. Abstract classes are the preferred way to define interfaces in Dart because they support both abstract method signatures and concrete helper methods. Multiple interfaces can be implemented simultaneously, unlike the single-parent restriction of <code>extends</code>.</p>
    $dart_55$,
    'Learn Dart interfaces — implicit interfaces, the implements keyword, implementing multiple interfaces, abstract classes as interface definitions, and the Repository pattern.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 55, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Polymorphism in Dart', 'dart-polymorphism',
    $dart_56$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#what">What is Polymorphism?</a></li><li><a href="#subtype">Subtype Polymorphism</a></li><li><a href="#runtime">Runtime Type Dispatch</a></li><li><a href="#interface-poly">Interface-Based Polymorphism</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Polymorphism?</h2>
<p>Polymorphism (from Greek: "many forms") means that a single interface can represent objects of different types. In Dart, polymorphism lets you write code that works with a parent type, while at runtime different subclass behaviors execute. This is the foundation of flexible, extensible object-oriented design.</p>
<h2 id="subtype">Subtype Polymorphism</h2>
<p>A subclass object can be used anywhere the parent class type is expected:</p>
<pre><code>class Animal {
  String name;
  Animal(this.name);

  // This method will be overridden by subclasses
  String speak() => ''...'';

  void describe() => print(''$name says: \$${speak()}'');
}

class Dog extends Animal {
  Dog(String name) : super(name);
  @override String speak() => ''Woof!'';
}

class Cat extends Animal {
  Cat(String name) : super(name);
  @override String speak() => ''Meow!'';
}

class Parrot extends Animal {
  final String phrase;
  Parrot(String name, this.phrase) : super(name);
  @override String speak() => phrase;
}

void main() {
  // All stored as Animal type — polymorphic
  List&lt;Animal&gt; animals = [
    Dog(''Rex''),
    Cat(''Whiskers''),
    Parrot(''Polly'', ''Polly wants a cracker!''),
    Dog(''Buddy''),
  ];

  for (var animal in animals) {
    animal.describe();  // correct speak() called at runtime
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Rex says: Woof!
Whiskers says: Meow!
Polly says: Polly wants a cracker!
Buddy says: Woof!</pre></div>
<h2 id="runtime">Runtime Type Dispatch</h2>
<p>Dart dispatches method calls based on the actual runtime type of the object, not the declared type. Use <code>is</code> to check types and <code>as</code> to cast:</p>
<pre><code>void processAnimal(Animal animal) {
  animal.describe();  // polymorphic

  // Type-specific logic with is
  if (animal is Dog) {
    print(''  $${animal.name} can fetch!'');
  } else if (animal is Parrot) {
    print(''  $${animal.name} can mimic: "$${animal.phrase}"'');
  }
}

void main() {
  processAnimal(Dog(''Max''));
  processAnimal(Cat(''Luna''));
  processAnimal(Parrot(''Rio'', ''Pretty bird!''));
}</code></pre>
<h2 id="interface-poly">Interface-Based Polymorphism</h2>
<pre><code>abstract class Drawable {
  void draw();
  String get description;
}

class Circle extends Drawable {
  final double radius;
  Circle(this.radius);
  @override void draw() => print(''Drawing circle r=$radius'');
  @override String get description => ''Circle(r=$radius)'';
}

class Square extends Drawable {
  final double side;
  Square(this.side);
  @override void draw() => print(''Drawing square s=$side'');
  @override String get description => ''Square(s=$side)'';
}

class Triangle extends Drawable {
  final double base, height;
  Triangle(this.base, this.height);
  @override void draw() => print(''Drawing triangle b=$base, h=$height'');
  @override String get description => ''Triangle(b=$base, h=$height)'';
}

// Works with ANY Drawable — polymorphic
void drawAll(List&lt;Drawable&gt; shapes) {
  for (var shape in shapes) {
    shape.draw();
    print(''  -&gt; \$${shape.description}'');
  }
}

void main() {
  drawAll([Circle(5), Square(4), Triangle(3, 6), Circle(2)]);
}</code></pre>
<h2 id="example">Full Example: Payment Processing</h2>
<pre><code>abstract class PaymentMethod {
  String get name;
  bool validate();
  Future&lt;bool&gt; processPayment(double amount);
}

class CreditCard extends PaymentMethod {
  final String cardNumber;
  final String cvv;
  CreditCard(this.cardNumber, this.cvv);

  @override String get name => ''Credit Card'';
  @override bool validate() => cardNumber.length == 16 &amp;&amp; cvv.length == 3;

  @override
  Future&lt;bool&gt; processPayment(double amount) async {
    await Future.delayed(Duration(milliseconds: 200));
    print(''Credit card charged: \$\$${amount.toStringAsFixed(2)}'');
    return true;
  }
}

class PayPal extends PaymentMethod {
  final String email;
  PayPal(this.email);

  @override String get name => ''PayPal'';
  @override bool validate() => email.contains(''@'');

  @override
  Future&lt;bool&gt; processPayment(double amount) async {
    await Future.delayed(Duration(milliseconds: 150));
    print(''PayPal transfer: \$\$${amount.toStringAsFixed(2)} from $email'');
    return true;
  }
}

class PaymentProcessor {
  // Works with any PaymentMethod — polymorphic
  Future&lt;void&gt; charge(PaymentMethod method, double amount) async {
    print(''Processing via \$${method.name}...'');
    if (!method.validate()) {
      print(''Validation failed for \$${method.name}'');
      return;
    }
    final success = await method.processPayment(amount);
    print(success ? ''Payment successful!'' : ''Payment failed.'');
  }
}

void main() async {
  var processor = PaymentProcessor();
  await processor.charge(CreditCard(''1234567812345678'', ''123''), 49.99);
  await processor.charge(PayPal(''user@example.com''), 29.99);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Processing via Credit Card...
Credit card charged: $49.99
Payment successful!
Processing via PayPal...
PayPal transfer: $29.99 from user@example.com
Payment successful!</pre></div>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Create an abstract <code>Notification</code> class with <code>send(String message)</code>. Implement <code>EmailNotification</code>, <code>SMSNotification</code>, and <code>PushNotification</code>. Write a <code>NotificationService</code> that sends to a list of <code>Notification</code> objects.</li>
<li>Create a <code>Serializer</code> abstract class with <code>serialize(Map data)</code> and <code>deserialize(String input)</code>. Implement <code>JsonSerializer</code> and <code>CsvSerializer</code>.</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does polymorphism mean in OOP?</strong></p>
<ul><li>A) A class with many fields</li><li>B) An object that can take many forms — one interface, multiple implementations ✅</li><li>C) Multiple inheritance</li><li>D) A method with many parameters</li></ul>
<p><strong>2. What determines which overridden method is called at runtime?</strong></p>
<ul><li>A) The declared (compile-time) type</li><li>B) The actual runtime type of the object ✅</li><li>C) The order of class declaration</li><li>D) The variable name</li></ul>
<p><strong>3. What Dart keyword checks whether an object is a certain type?</strong></p>
<ul><li>A) typeof</li><li>B) as</li><li>C) is ✅</li><li>D) instanceof</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Polymorphism allows one interface to represent many types. Store different subclass objects in a parent-type list and call methods — each object responds with its own overridden behavior. Use <code>is</code> for runtime type checks (with automatic smart cast) and <code>as</code> for explicit downcasting. Interface-based polymorphism (via <code>abstract class</code> + <code>implements</code>) keeps code flexible and testable — swap implementations without changing calling code.</p>
    $dart_56$,
    'Master Dart polymorphism — subtype polymorphism, runtime method dispatch, interface-based polymorphism, the is/as operators, and real-world payment processing examples.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 56, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Method Overriding in Dart', 'dart-method-overriding',
    $dart_58$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#what">What is Method Overriding?</a></li><li><a href="#override">@override Annotation</a></li><li><a href="#super-call">Calling super</a></li><li><a href="#rules">Overriding Rules</a></li><li><a href="#covariant">covariant Keyword</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Method Overriding?</h2>
<p>Method overriding lets a subclass provide its own implementation of a method that is already defined in the parent class. The subclass version replaces the parent version for objects of that subclass type. This is the mechanism that makes <strong>runtime polymorphism</strong> work in Dart.</p>
<h2 id="override">@override Annotation</h2>
<p>The <code>@override</code> annotation tells the Dart compiler that you intentionally override a parent member. It is technically optional, but strongly recommended — the compiler will warn you if the method doesn't actually exist in the parent class:</p>
<pre><code>class Vehicle {
  String make;
  Vehicle(this.make);

  String fuelType() => ''Petrol'';
  String describe() => ''$make runs on \$${fuelType()}'';
}

class ElectricCar extends Vehicle {
  int batteryCapacityKwh;
  ElectricCar(String make, this.batteryCapacityKwh) : super(make);

  @override
  String fuelType() => ''Electricity'';  // overrides parent

  @override
  String describe() =>
      ''$make is electric (\$${batteryCapacityKwh}kWh battery)'';
}

class HybridCar extends Vehicle {
  HybridCar(String make) : super(make);

  @override
  String fuelType() => ''Petrol + Electricity'';
  // describe() is NOT overridden — inherited from Vehicle
}

void main() {
  var vehicles = [
    Vehicle(''Toyota''),
    ElectricCar(''Tesla'', 100),
    HybridCar(''Prius''),
  ];

  for (var v in vehicles) {
    print(v.describe());
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Toyota runs on Petrol
Tesla is electric (100kWh battery)
Prius runs on Petrol + Electricity</pre></div>
<h2 id="super-call">Calling super</h2>
<p>An overriding method can call the parent version using <code>super.methodName()</code>. This is useful for extending (not replacing) parent behavior:</p>
<pre><code>class Logger {
  void log(String message) {
    print(''[LOG] $message'');
  }
}

class TimestampLogger extends Logger {
  @override
  void log(String message) {
    // Add timestamp, then call parent
    final timestamp = DateTime.now().toIso8601String();
    super.log(''[$timestamp] $message'');
  }
}

class PrefixLogger extends Logger {
  final String prefix;
  PrefixLogger(this.prefix);

  @override
  void log(String message) {
    super.log(''[$prefix] $message'');
  }
}

void main() {
  var tl = TimestampLogger();
  tl.log(''Server started'');
  // [LOG] [2024-01-15T10:30:00.000] Server started

  var pl = PrefixLogger(''AUTH'');
  pl.log(''User logged in'');
  // [LOG] [AUTH] User logged in
}</code></pre>
<h2 id="rules">Overriding Rules</h2>
<ul>
<li><strong>Return type</strong>: The overriding method can return a subtype of the parent return type (covariant return)</li>
<li><strong>Parameters</strong>: Must match the parent signature (same name, count, and types)</li>
<li><strong>Visibility</strong>: Cannot reduce visibility (cannot make a public method private)</li>
<li><strong>final methods</strong>: Cannot be overridden — Dart does not have <code>final</code> for methods, but you can document intent</li>
</ul>
<pre><code>class Animal {
  Animal create() => Animal();
}

class Dog extends Animal {
  // Covariant return: Dog is a subtype of Animal — allowed
  @override
  Dog create() => Dog();
}</code></pre>
<h2 id="covariant">covariant Keyword</h2>
<p>Normally, overriding parameter types must be the same or broader (contravariant). The <code>covariant</code> keyword allows a narrower type — useful for type-safe collections:</p>
<pre><code>class Animal {
  void eat(covariant Animal food) {
    print(''$runtimeType eats \$${food.runtimeType}'');
  }
}

class Dog extends Animal {
  @override
  void eat(Dog food) {  // narrowed to Dog — allowed via covariant
    print(''Dog eats dog food'');
  }
}</code></pre>
<h2 id="example">Full Example: Employee Bonus System</h2>
<pre><code>class Employee {
  final String name;
  final double baseSalary;

  Employee(this.name, this.baseSalary);

  double calculateBonus() => baseSalary * 0.10;

  void printPayslip() {
    final bonus = calculateBonus();
    print(''--- Payslip for $name ---'');
    print(''Base Salary: \$\$${baseSalary.toStringAsFixed(2)}'');
    print(''Bonus:       \$\$${bonus.toStringAsFixed(2)}'');
    print(''Total:       \$\$${(baseSalary + bonus).toStringAsFixed(2)}'');
  }
}

class Manager extends Employee {
  final int teamSize;
  Manager(String name, double salary, this.teamSize) : super(name, salary);

  @override
  double calculateBonus() => super.calculateBonus() + teamSize * 500;
}

class SalesRep extends Employee {
  final double salesRevenue;
  SalesRep(String name, double salary, this.salesRevenue) : super(name, salary);

  @override
  double calculateBonus() => salesRevenue * 0.05;
}

class Intern extends Employee {
  Intern(String name, double salary) : super(name, salary);

  @override
  double calculateBonus() => 0;  // no bonus for interns
}

void main() {
  List&lt;Employee&gt; staff = [
    Employee(''Alice'', 60000),
    Manager(''Bob'', 90000, 8),
    SalesRep(''Carol'', 55000, 200000),
    Intern(''Dave'', 25000),
  ];

  for (var e in staff) {
    e.printPayslip();
    print('''');
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>--- Payslip for Alice ---
Base Salary: $60000.00
Bonus:       $6000.00
Total:       $66000.00

--- Payslip for Bob ---
Base Salary: $90000.00
Bonus:       $13000.00
Total:       $103000.00

--- Payslip for Carol ---
Base Salary: $55000.00
Bonus:       $10000.00
Total:       $65000.00

--- Payslip for Dave ---
Base Salary: $25000.00
Bonus:       $0.00
Total:       $25000.00</pre></div>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Create an <code>Animal</code> class with a <code>move()</code> method. Override it in <code>Fish</code> (swims), <code>Bird</code> (flies), <code>Snake</code> (slithers). Store them in a <code>List&lt;Animal&gt;</code> and call <code>move()</code> on each.</li>
<li>Create a logging hierarchy: <code>BaseLogger</code> with <code>log(message)</code>, <code>FileLogger</code> that calls super and also writes to a list, <code>NetworkLogger</code> that calls super and adds an HTTP prefix.</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does the @override annotation do?</strong></p>
<ul><li>A) Forces the method to be called at compile time</li><li>B) Signals intentional replacement of a parent method and enables compiler checking ✅</li><li>C) Prevents further overriding</li><li>D) Calls the parent method automatically</li></ul>
<p><strong>2. How do you call the parent''s version of an overridden method?</strong></p>
<ul><li>A) parent.method()</li><li>B) base.method()</li><li>C) super.method() ✅</li><li>D) this.super.method()</li></ul>
<p><strong>3. What does covariant allow in a method parameter?</strong></p>
<ul><li>A) A broader (parent) type</li><li>B) A narrower (subtype) — normally disallowed in overrides ✅</li><li>C) Any type</li><li>D) Only nullable types</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Method overriding lets subclasses replace parent methods with customized implementations. Always use <code>@override</code> to signal intent and enable compiler checking. Call <code>super.method()</code> from an override when you want to extend (not replace) parent behavior. Overriding return types can be narrowed (covariant return), and the <code>covariant</code> keyword allows narrowing parameter types. Overriding is the runtime engine of polymorphism.</p>
    $dart_58$,
    'Master Dart method overriding — @override annotation, calling super, covariant keyword, overriding rules, and building polymorphic bonus calculators.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 58, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Abstract Classes vs Interfaces in Dart', 'dart-abstract-vs-interface',
    $dart_60$
<div class="toc-box"><h3>Table of Contents</h3><ol><li><a href="#overview">Overview</a></li><li><a href="#abstract">Abstract Classes</a></li><li><a href="#interface">Interfaces (implements)</a></li><li><a href="#comparison">Side-by-Side Comparison</a></li><li><a href="#when">When to Use Which</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="overview">Overview</h2>
<p>Dart does not have a separate <code>interface</code> keyword — every class can serve as both a superclass and an interface. Understanding when to use <code>extends</code> (inherit from an abstract class) vs <code>implements</code> (adopt a class as a pure interface contract) is a critical design decision in Dart OOP.</p>
<h2 id="abstract">Abstract Classes (extends)</h2>
<p>Use <code>abstract class</code> + <code>extends</code> when:</p>
<ul>
<li>You want to share code (concrete methods) with subclasses</li>
<li>You have a true "is-a" relationship (Dog IS AN Animal)</li>
<li>You want to enforce a template while providing defaults</li>
</ul>
<pre><code>abstract class Vehicle {
  String make;
  int year;
  Vehicle(this.make, this.year);

  // Abstract — must override
  double fuelEfficiency();

  // Concrete — shared by all subclasses
  int age() => DateTime.now().year - year;
  String describe() => ''$make ($year), efficiency: \$${fuelEfficiency()}mpg, age: \$${age()}yr'';
}

class GasCar extends Vehicle {
  final double mpg;
  GasCar(String make, int year, this.mpg) : super(make, year);

  @override double fuelEfficiency() => mpg;
}

class ElectricCar extends Vehicle {
  final int rangeMiles;
  final double kwhPer100Miles;
  ElectricCar(String make, int year, this.rangeMiles, this.kwhPer100Miles) : super(make, year);

  @override double fuelEfficiency() => rangeMiles / kwhPer100Miles;
}

void main() {
  List&lt;Vehicle&gt; fleet = [
    GasCar(''Toyota'', 2020, 32.5),
    ElectricCar(''Tesla'', 2023, 358, 25.0),
  ];
  for (var v in fleet) print(v.describe());
}</code></pre>
<h2 id="interface">Interfaces (implements)</h2>
<p>Use <code>implements</code> when:</p>
<ul>
<li>You want to enforce a contract without sharing code</li>
<li>The relationship is "can-do" rather than "is-a"</li>
<li>You need a class to fulfill multiple contracts</li>
<li>You want to mock/stub in tests</li>
</ul>
<pre><code>abstract class Cacheable {
  String get cacheKey;
  Duration get ttl;
}

abstract class Serializable {
  Map&lt;String, dynamic&gt; toJson();
  String toJsonString();
}

abstract class Validatable {
  List&lt;String&gt; validate();
  bool get isValid => validate().isEmpty;
}

class UserProfile implements Cacheable, Serializable, Validatable {
  final int id;
  final String name;
  final String email;

  UserProfile(this.id, this.name, this.email);

  @override String get cacheKey => ''user:$id'';
  @override Duration get ttl => const Duration(hours: 1);

  @override
  Map&lt;String, dynamic&gt; toJson() => {''id'': id, ''name'': name, ''email'': email};

  @override
  String toJsonString() => toJson().toString();

  @override
  List&lt;String&gt; validate() {
    var errors = &lt;String&gt;[];
    if (name.trim().isEmpty) errors.add(''Name is required'');
    if (!email.contains(''@'')) errors.add(''Invalid email'');
    return errors;
  }
}

void main() {
  var user = UserProfile(1, ''Alice'', ''alice@example.com'');
  print(user.cacheKey);        // user:1
  print(user.isValid);         // true
  print(user.toJsonString());  // {id: 1, name: Alice, email: alice@example.com}
}</code></pre>
<h2 id="comparison">Side-by-Side Comparison</h2>
<table>
<tr><th>Feature</th><th>Abstract Class (extends)</th><th>Interface (implements)</th></tr>
<tr><td>Keyword</td><td><code>abstract class</code> + <code>extends</code></td><td>Any class + <code>implements</code></td></tr>
<tr><td>Code sharing</td><td>Yes — concrete methods inherited</td><td>No — must implement everything</td></tr>
<tr><td>Multiple</td><td>One parent only</td><td>Many interfaces allowed</td></tr>
<tr><td>Relationship</td><td>"is-a"</td><td>"can-do"</td></tr>
<tr><td>Constructor</td><td>Inherited via super()</td><td>Not inherited</td></tr>
<tr><td>Typical use</td><td>Template Method pattern</td><td>Dependency injection, testing</td></tr>
</table>
<h2 id="when">When to Use Which</h2>
<div class="note-box">
<p><strong>Use abstract class + extends when:</strong></p>
<ul><li>Subclasses share significant code (DRY principle)</li><li>The hierarchy represents a true taxonomy (Animal → Dog)</li><li>You use the Template Method pattern</li></ul>
<p><strong>Use implements when:</strong></p>
<ul><li>You need multiple inheritance of behavior contracts</li><li>You want to decouple callers from implementations</li><li>You need to mock/test with fake implementations</li><li>Different classes share a capability, not an identity</li></ul>
</div>
<h2 id="example">Full Example: Data Layer Design</h2>
<pre><code>// Interface — contract only
abstract class DataSource {
  Future&lt;Map&lt;String, dynamic&gt;?&gt; get(String key);
  Future&lt;void&gt; set(String key, Map&lt;String, dynamic&gt; value);
  Future&lt;void&gt; delete(String key);
}

// Abstract class — shared caching logic
abstract class CachingRepository {
  final Map&lt;String, dynamic&gt; _cache = {};

  dynamic getFromCache(String key) => _cache[key];
  void putInCache(String key, dynamic value) => _cache[key] = value;
  void invalidateCache(String key) => _cache.remove(key);
  void clearCache() => _cache.clear();
}

// Combines both: inherits cache code, fulfills DataSource contract
class InMemoryDataSource extends CachingRepository implements DataSource {
  @override
  Future&lt;Map&lt;String, dynamic&gt;?&gt; get(String key) async {
    return getFromCache(key) as Map&lt;String, dynamic&gt;?;
  }

  @override
  Future&lt;void&gt; set(String key, Map&lt;String, dynamic&gt; value) async {
    putInCache(key, value);
  }

  @override
  Future&lt;void&gt; delete(String key) async {
    invalidateCache(key);
  }
}

// A function that works with any DataSource (polymorphic)
Future&lt;void&gt; saveUser(DataSource ds, Map&lt;String, dynamic&gt; user) async {
  await ds.set(''user:\$${user[''id'']}'', user);
  print(''Saved user \$${user[''name'']}'');
}

void main() async {
  DataSource ds = InMemoryDataSource();
  await saveUser(ds, {''id'': 1, ''name'': ''Alice''});
  var result = await ds.get(''user:1'');
  print(result);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Saved user Alice
{id: 1, name: Alice}</pre></div>
<div class="exercise-box"><h3>Exercise</h3><ol>
<li>Design a media player: abstract class <code>MediaPlayer</code> with shared <code>volume</code> management. Interfaces <code>Streamable</code> and <code>Downloadable</code>. Create <code>PremiumPlayer</code> that extends <code>MediaPlayer</code> and implements both interfaces.</li>
<li>Create an abstract <code>BaseController</code> with shared request parsing logic. Define interfaces <code>Authenticatable</code> and <code>Rateable</code>. Implement <code>UserController</code>.</li>
</ol></div>
<h2 id="quiz">Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is the key difference between extends and implements in Dart?</strong></p>
<ul><li>A) extends is for classes; implements is for interfaces only</li><li>B) extends inherits code; implements adopts the contract without inheriting code ✅</li><li>C) implements is slower</li><li>D) There is no difference</li></ul>
<p><strong>2. How many classes can a Dart class extend?</strong></p>
<ul><li>A) Unlimited</li><li>B) Two</li><li>C) One ✅</li><li>D) Zero — Dart doesn''t use extends</li></ul>
<p><strong>3. When is implements preferred over extends?</strong></p>
<ul><li>A) When you need code reuse</li><li>B) When you need multiple contracts, dependency injection, or test mocking ✅</li><li>C) When the parent has a constructor</li><li>D) Always</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Use <code>abstract class</code> + <code>extends</code> when subclasses share significant code and have a true "is-a" relationship. Use <code>implements</code> when you need to enforce a pure contract (can-do relationship), support multiple interfaces, or enable easy mocking in tests. Dart lets you combine both: a class can <code>extends</code> one abstract class while also <code>implements</code> multiple interfaces. This flexibility makes Dart's OOP system expressive and practical for real-world architecture.</p>
    $dart_60$,
    'Understand when to use abstract classes vs interfaces in Dart — extends for code reuse and is-a relationships, implements for contracts, multiple capability, and test mocking.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 60, 'PUBLISHED', 'ACT', 'system'
);
