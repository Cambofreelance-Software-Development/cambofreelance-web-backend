-- Section 9 OOP Part 1 (Lessons 51–56)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Classes and Objects in Dart', 'dart-classes-objects',
    $dart_51$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Classes and Objects?</a></li><li><a href="#defining">Defining a Class</a></li><li><a href="#creating">Creating Objects</a></li><li><a href="#accessing">Accessing Members</a></li><li><a href="#example">Full Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Classes and Objects?</h2>
<p>A <strong>class</strong> is a blueprint that defines the properties (fields) and behaviors (methods) of a type. An <strong>object</strong> is a specific instance of a class — it holds actual data following the blueprint.</p>
<h2 id="defining">Defining a Class</h2>
<pre><code>class Person {
  // Fields (properties)
  String name;
  int age;
  String email;

  // Constructor
  Person(this.name, this.age, this.email);

  // Methods (behaviors)
  void introduce() {
    print('Hi, I am $name, $age years old.');
  }

  bool isAdult() => age >= 18;

  @override
  String toString() => 'Person($name, age=$age)';
}</code></pre>
<h2 id="creating">Creating Objects</h2>
<pre><code>void main() {
  // Create objects (instances of Person)
  Person alice = Person('Alice', 28, 'alice@email.com');
  Person bob   = Person('Bob', 17, 'bob@email.com');

  // Or use var / final
  var carol = Person('Carol', 32, 'carol@email.com');
}</code></pre>
<h2 id="accessing">Accessing Members</h2>
<pre><code>void main() {
  var p = Person('Alice', 28, 'alice@email.com');

  // Access fields
  print(p.name);    // Alice
  print(p.age);     // 28

  // Call methods
  p.introduce();    // Hi, I am Alice, 28 years old.
  print(p.isAdult()); // true

  // Modify fields
  p.age = 29;
  print(p);         // Person(Alice, age=29)
}</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>class BankAccount {
  String owner;
  double balance;

  BankAccount(this.owner, this.balance);

  void deposit(double amount) {
    balance += amount;
    print('Deposited \$$${amount.toStringAsFixed(2)}. Balance: \$$${balance.toStringAsFixed(2)}');
  }

  void withdraw(double amount) {
    if (amount > balance) {
      print('Insufficient funds!');
      return;
    }
    balance -= amount;
    print('Withdrew \$$${amount.toStringAsFixed(2)}. Balance: \$$${balance.toStringAsFixed(2)}');
  }
}

void main() {
  var account = BankAccount('Alice', 1000.0);
  account.deposit(500.0);
  account.withdraw(200.0);
  account.withdraw(2000.0);
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Deposited $500.00. Balance: $1500.00
Withdrew $200.00. Balance: $1300.00
Insufficient funds!</pre></div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Create a <code>Car</code> class with fields: make, model, year, and a method <code>getInfo()</code>.</li>
<li>Create a <code>Rectangle</code> class with width and height. Add methods for area and perimeter.</li>
<li>Create a <code>Student</code> class and a list of 3 students, then print them all.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What is the difference between a class and an object?</strong></p>
<ul><li>A) They are the same thing</li><li>B) A class is a blueprint; an object is a specific instance ✅</li><li>C) An object is a blueprint; a class is an instance</li><li>D) Classes have methods; objects do not</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>A class defines the structure (fields) and behavior (methods) of a type. Objects are instances of a class. Create objects with <code>ClassName(args)</code>. Access fields and methods with the dot operator. Each object has its own copy of the field values.</p>
    $dart_51$,
    'Learn classes and objects in Dart — define blueprints, create instances, access fields and methods, and build your first object-oriented programs.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 51, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Constructors in Dart', 'dart-constructors',
    $dart_52$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is a Constructor?</a></li><li><a href="#default">Default Constructor</a></li><li><a href="#initializing">Initializing Formal Parameters</a></li><li><a href="#initializer-list">Initializer List</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is a Constructor?</h2>
<p>A constructor is a special method that runs when you create an object. It initializes the object's fields. In Dart, a constructor has the same name as the class.</p>
<h2 id="default">Default Constructor</h2>
<pre><code>class Point {
  double x;
  double y;

  // Constructor
  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
}

var p = Point(3.0, 4.0);
print('$${p.x}, $${p.y}');  // 3.0, 4.0</code></pre>
<h2 id="initializing">Initializing Formal Parameters</h2>
<p>Dart provides a shorthand to initialize fields directly in the constructor signature using <code>this.fieldName</code>:</p>
<pre><code>class Point {
  double x;
  double y;

  // Shorthand — automatically assigns x and y
  Point(this.x, this.y);
}

// Equivalent to the verbose version above</code></pre>
<h2 id="initializer-list">Initializer List</h2>
<p>Run code before the constructor body using an initializer list (after <code>:</code>). Useful for computing final field values or validation:</p>
<pre><code>class Circle {
  final double radius;
  final double area;

  Circle(double r)
      : radius = r,
        area = 3.14159 * r * r {
    print('Circle created with radius $radius');
  }
}

var c = Circle(5.0);
print('Area: $${c.area.toStringAsFixed(2)}');</code></pre>
<div class="note-box"><strong>Output:</strong><pre>Circle created with radius 5.0
Area: 78.54</pre></div>
<h2 id="example">Example</h2>
<pre><code>class Employee {
  final String id;
  final String name;
  final String department;
  double salary;

  Employee({
    required this.id,
    required this.name,
    required this.department,
    this.salary = 50000.0,
  });

  void applyRaise(double percent) {
    salary *= (1 + percent / 100);
    print('$name raised to \$$${salary.toStringAsFixed(2)}');
  }
}

void main() {
  var emp1 = Employee(id: 'E001', name: 'Alice', department: 'Engineering');
  var emp2 = Employee(id: 'E002', name: 'Bob', department: 'Sales', salary: 45000);

  emp1.applyRaise(10);  // Alice raised to $55000.00
  emp2.applyRaise(5);   // Bob raised to $47250.00
}</code></pre>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What does <code>Point(this.x, this.y)</code> do?</strong></p>
<ul><li>A) Calls a method</li><li>B) Automatically assigns constructor parameters to fields ✅</li><li>C) Creates a copy</li><li>D) Initializes x and y to 0</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Constructors initialize object fields when an instance is created. Use the <code>this.field</code> shorthand for concise initialization. Initializer lists (<code>:</code>) run before the constructor body, useful for final fields and computed values. Named parameters with <code>required</code> make constructors explicit and safe.</p>
    $dart_52$,
    'Learn Dart constructors — default, shorthand (this.field), initializer lists, and named parameter constructors for clean object initialization.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 52, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Inheritance in Dart', 'dart-inheritance',
    $dart_57$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What is Inheritance?</a></li><li><a href="#extends">extends Keyword</a></li><li><a href="#super">super Keyword</a></li><li><a href="#override">Overriding Methods</a></li><li><a href="#example">Example</a></li><li><a href="#exercise">Exercise</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What is Inheritance?</h2>
<p>Inheritance lets a class (child/subclass) inherit all the fields and methods of another class (parent/superclass). The child class can then add its own members or override inherited ones. This promotes code reuse and establishes an "is-a" relationship.</p>
<h2 id="extends">extends Keyword</h2>
<pre><code>class Animal {
  String name;
  Animal(this.name);

  void eat() => print('$name is eating.');
  void breathe() => print('$name is breathing.');
}

class Dog extends Animal {
  String breed;
  Dog(String name, this.breed) : super(name);

  void bark() => print('$name says: Woof!');
}

class Cat extends Animal {
  Cat(String name) : super(name);
  void meow() => print('$name says: Meow!');
}</code></pre>
<h2 id="super">super Keyword</h2>
<p><code>super</code> refers to the parent class. Use it to call the parent constructor or access parent methods:</p>
<pre><code>Dog rex = Dog('Rex', 'German Shepherd');
rex.eat();    // Rex is eating.    (inherited from Animal)
rex.breathe(); // Rex is breathing. (inherited)
rex.bark();   // Rex says: Woof!  (Dog's own method)</code></pre>
<h2 id="override">Overriding Methods</h2>
<p>A subclass can redefine a parent method using <code>@override</code>:</p>
<pre><code>class Animal {
  String name;
  Animal(this.name);
  void speak() => print('$name makes a sound.');
}

class Dog extends Animal {
  Dog(String name) : super(name);

  @override
  void speak() => print('$name says: Woof!');
}

class Cat extends Animal {
  Cat(String name) : super(name);

  @override
  void speak() => print('$name says: Meow!');
}</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>class Shape {
  String color;
  Shape(this.color);

  double area() => 0;
  void describe() => print('A $color shape with area $${area().toStringAsFixed(2)}');
}

class Circle extends Shape {
  double radius;
  Circle(String color, this.radius) : super(color);

  @override
  double area() => 3.14159 * radius * radius;
}

class Rectangle extends Shape {
  double width, height;
  Rectangle(String color, this.width, this.height) : super(color);

  @override
  double area() => width * height;
}

void main() {
  var shapes = [
    Circle('red', 5),
    Rectangle('blue', 4, 6),
  ];

  for (var s in shapes) {
    s.describe();
  }
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>A red shape with area 78.54
A blue shape with area 24.00</pre></div>
<div class="exercise-box"><h3>💪 Exercise</h3><ol>
<li>Create a <code>Vehicle</code> parent class and <code>Car</code>, <code>Truck</code>, <code>Motorcycle</code> subclasses.</li>
<li>Override a <code>fuelType()</code> method in each subclass.</li>
<li>Create a list of mixed Vehicle types and call <code>fuelType()</code> on each.</li>
</ol></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Which keyword is used to inherit from a parent class in Dart?</strong></p>
<ul><li>A) implements</li><li>B) inherits</li><li>C) extends ✅</li><li>D) from</li></ul>
<p><strong>2. What does @override mean?</strong></p>
<ul><li>A) Hides the parent method</li><li>B) Signals that a method intentionally replaces the parent version ✅</li><li>C) Deletes the parent method</li><li>D) Calls the parent method</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Inheritance uses <code>extends</code> to create a child class that reuses parent fields and methods. Use <code>super()</code> to call the parent constructor. Override methods with <code>@override</code> to customize behavior in subclasses. Dart supports single inheritance only (one parent per class).</p>
    $dart_57$,
    'Master Dart inheritance — extend parent classes, call super constructors, override methods, and build class hierarchies for code reuse.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop,inheritance', NOW(), 57, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Abstract Classes in Dart', 'dart-abstract-classes',
    $dart_59$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Abstract Classes?</a></li><li><a href="#syntax">Syntax</a></li><li><a href="#abstract-methods">Abstract Methods</a></li><li><a href="#example">Example</a></li><li><a href="#vs-interface">Abstract Class vs Interface</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Abstract Classes?</h2>
<p>An abstract class is a class that cannot be instantiated directly — you cannot create an object of an abstract class. It serves as a template for subclasses, defining which methods they MUST implement.</p>
<h2 id="syntax">Syntax</h2>
<pre><code>abstract class Animal {
  String name;
  Animal(this.name);

  // Abstract method — no body, must be implemented by subclasses
  void speak();

  // Concrete method — has a body, inherited as-is
  void breathe() => print('$name is breathing.');
}</code></pre>
<h2 id="abstract-methods">Abstract Methods</h2>
<p>Abstract methods have no implementation in the abstract class. Every non-abstract subclass MUST implement them:</p>
<pre><code>class Dog extends Animal {
  Dog(String name) : super(name);

  @override
  void speak() => print('$name barks!');  // must implement
}

class Cat extends Animal {
  Cat(String name) : super(name);

  @override
  void speak() => print('$name meows!');
}

// var a = Animal('X');  ← ERROR: abstract class cannot be instantiated</code></pre>
<h2 id="example">Example</h2>
<pre><code>abstract class Shape {
  String color;
  Shape(this.color);

  // Abstract methods — must implement
  double area();
  double perimeter();

  // Concrete method — shared logic
  void describe() {
    print('$color shape: area=$${area().toStringAsFixed(2)}, '
          'perimeter=$${perimeter().toStringAsFixed(2)}');
  }
}

class Circle extends Shape {
  double radius;
  Circle(String color, this.radius) : super(color);

  @override double area() => 3.14159 * radius * radius;
  @override double perimeter() => 2 * 3.14159 * radius;
}

class Rectangle extends Shape {
  double w, h;
  Rectangle(String color, this.w, this.h) : super(color);

  @override double area() => w * h;
  @override double perimeter() => 2 * (w + h);
}

void main() {
  List&lt;Shape&gt; shapes = [
    Circle('red', 7),
    Rectangle('blue', 4, 9),
  ];
  for (var s in shapes) s.describe();
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>red shape: area=153.94, perimeter=43.98
blue shape: area=36.00, perimeter=26.00</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. Can you create an object of an abstract class?</strong></p>
<ul><li>A) Yes</li><li>B) No ✅</li><li>C) Only with a factory constructor</li><li>D) Only if it has no abstract methods</li></ul>
<p><strong>2. Must subclasses implement all abstract methods?</strong></p>
<ul><li>A) No, they are optional</li><li>B) Yes, or they must also be abstract ✅</li><li>C) Only if they override them</li><li>D) Only public abstract methods</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Abstract classes define a contract — methods that all subclasses must implement. They cannot be instantiated directly. Use <code>abstract class</code> when you want to enforce a common interface while sharing some concrete implementation. Subclasses use <code>extends</code> to inherit and must implement all abstract methods.</p>
    $dart_59$,
    'Learn Dart abstract classes — define contracts with abstract methods that subclasses must implement, enabling polymorphism and enforced structure.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 59, 'PUBLISHED', 'ACT', 'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(), 'Mixins in Dart', 'dart-mixins',
    $dart_61$
<div class="toc-box"><h3>📚 Table of Contents</h3><ol><li><a href="#what">What are Mixins?</a></li><li><a href="#defining">Defining a Mixin</a></li><li><a href="#using">Using Mixins with with</a></li><li><a href="#on">Restricting Mixins with on</a></li><li><a href="#example">Example</a></li><li><a href="#quiz">Quiz</a></li><li><a href="#summary">Summary</a></li></ol></div>
<h2 id="what">What are Mixins?</h2>
<p>Mixins provide a way to reuse code across multiple class hierarchies without inheritance. They let you "mix in" capabilities into a class. Since Dart only supports single inheritance, mixins solve the diamond problem elegantly.</p>
<h2 id="defining">Defining a Mixin</h2>
<pre><code>mixin Swimmable {
  void swim() => print('$runtimeType is swimming!');
}

mixin Flyable {
  void fly() => print('$runtimeType is flying!');
}

mixin Runnable {
  void run() => print('$runtimeType is running!');
}</code></pre>
<h2 id="using">Using Mixins with with</h2>
<pre><code>class Duck extends Animal with Swimmable, Flyable, Runnable {
  Duck(String name) : super(name);
}

class Penguin extends Animal with Swimmable, Runnable {
  Penguin(String name) : super(name);
}

void main() {
  var duck = Duck('Donald');
  duck.swim();  // Duck is swimming!
  duck.fly();   // Duck is flying!
  duck.run();   // Duck is running!

  var penguin = Penguin('Tux');
  penguin.swim();  // Penguin is swimming!
  penguin.run();   // Penguin is running!
  // penguin.fly(); ← ERROR: Penguin doesn't have Flyable
}</code></pre>
<h2 id="on">Restricting Mixins with on</h2>
<p>Use <code>on</code> to restrict a mixin to specific parent classes:</p>
<pre><code>mixin Logger on Animal {
  void log(String msg) => print('[$${name}] $msg');
}

class Robot extends Animal with Logger {
  Robot(String name) : super(name);
}

var r = Robot('R2D2');
r.log('System ready');  // [R2D2] System ready</code></pre>
<h2 id="example">Full Example</h2>
<pre><code>mixin Serializable {
  Map&lt;String, dynamic&gt; toJson();
  String serialize() => toJson().toString();
}

mixin Validatable {
  bool validate();
  void assertValid() {
    if (!validate()) throw Exception('Validation failed for $runtimeType');
  }
}

class User with Serializable, Validatable {
  String name;
  String email;

  User(this.name, this.email);

  @override
  Map&lt;String, dynamic&gt; toJson() => {'name': name, 'email': email};

  @override
  bool validate() => name.isNotEmpty &amp;&amp; email.contains('@');
}

void main() {
  var user = User('Alice', 'alice@example.com');
  user.assertValid();
  print(user.serialize());
}</code></pre>
<div class="note-box"><strong>Output:</strong><pre>{name: Alice, email: alice@example.com}</pre></div>
<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
<p><strong>1. What keyword applies a mixin to a class?</strong></p>
<ul><li>A) extends</li><li>B) implements</li><li>C) with ✅</li><li>D) include</li></ul>
<p><strong>2. What problem do mixins solve that single inheritance cannot?</strong></p>
<ul><li>A) Memory management</li><li>B) Reusing behavior from multiple sources without multiple inheritance ✅</li><li>C) Performance</li><li>D) Null safety</li></ul>
</div>
<h2 id="summary">Summary</h2>
<p>Mixins provide reusable behavior that can be applied to any class using <code>with</code>. Define mixins with the <code>mixin</code> keyword. Apply multiple mixins to a single class. Use <code>on</code> to restrict which classes can use a mixin. Mixins solve the limitation of single inheritance by enabling mixin composition.</p>
    $dart_61$,
    'Learn Dart mixins — reuse behavior across class hierarchies using with, define mixin capabilities, and apply multiple mixins to a single class.',
    'TUTORIAL', 'Dart Team', 'dart,programming,intermediate,oop', NOW(), 61, 'PUBLISHED', 'ACT', 'system'
);
