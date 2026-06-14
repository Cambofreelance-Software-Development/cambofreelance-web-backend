-- C++ Tutorial Series — Intermediate Articles 22-31

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Classes',
'cpp-classes',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Classes</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 22 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#class">Defining a Class</a></li><li><a href="#members">Members &amp; Methods</a></li><li><a href="#access">Access Specifiers</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="class">What is a Class?</h2>
<p>A <strong>class</strong> is a user-defined blueprint that bundles data (attributes) and behavior (methods) into one unit. Classes are the foundation of Object-Oriented Programming (OOP).</p>
<pre><code class="language-cpp">class ClassName {
  public:          // access specifier
    dataType attribute;
    returnType method() { }
};</code></pre>

<h2 id="members">Defining and Using a Class</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Car {
  public:
    string brand;
    string model;
    int    year;

    void honk() {
        cout &lt;&lt; brand &lt;&lt; " goes Beep!" &lt;&lt; endl;
    }
};

int main() {
    Car myCar;
    myCar.brand = "Toyota";
    myCar.model = "Corolla";
    myCar.year  = 2022;

    cout &lt;&lt; myCar.brand &lt;&lt; " " &lt;&lt; myCar.model &lt;&lt; " (" &lt;&lt; myCar.year &lt;&lt; ")" &lt;&lt; endl;
    myCar.honk();
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Toyota Corolla (2022)
Toyota goes Beep!</code></pre>

<div class="note-box"><strong>Note:</strong> The class definition ends with a <strong>semicolon</strong> <code>;</code> — this is a common mistake to forget!</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Person</code> class with attributes <code>name</code> (string) and <code>age</code> (int), and a method <code>introduce()</code> that prints "Hi, I'm [name] and I'm [age] years old." Create two Person objects and call their method.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Person {
  public:
    string name;
    int    age;
    void introduce() {
        cout &lt;&lt; "Hi, I'm " &lt;&lt; name &lt;&lt; " and I'm " &lt;&lt; age &lt;&lt; " years old." &lt;&lt; endl;
    }
};

int main() {
    Person p1; p1.name = "Alice"; p1.age = 28; p1.introduce();
    Person p2; p2.name = "Bob";   p2.age = 34; p2.introduce();
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What does a class definition end with?</p><ul><li>A) A closing brace only</li><li>B) A semicolon after the closing brace</li><li>C) A period</li><li>D) The keyword <code>end</code></li></ul><details><summary>Answer</summary><p><strong>B) A semicolon</strong> — <code>};</code></p></details></li>
<li><p>What term describes the functions defined inside a class?</p><ul><li>A) Properties</li><li>B) Attributes</li><li>C) Methods</li><li>D) Modules</li></ul><details><summary>Answer</summary><p><strong>C) Methods</strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>A class is a blueprint combining data and behavior.</li>
<li>Class members are accessed using the dot operator <code>.</code>.</li>
<li>Always end a class definition with <code>};</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-pointers" class="btn btn-secondary">&larr; Previous: Pointers</a>
<a href="/tutorials/cpp/cpp-objects" class="btn btn-primary">Next: C++ Objects &rarr;</a>
</div>
</div>$$,
'Introduction to C++ classes — the blueprint of OOP. Learn to define classes, add attributes and methods, and create objects.',
'TUTORIAL', 'CamboFreelance', 'c++,classes,oop,intermediate', NOW(), 22, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Objects',
'cpp-objects',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Objects</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 23 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#create">Creating Objects</a></li><li><a href="#multiple">Multiple Objects</a></li><li><a href="#methods">Calling Methods</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="create">Creating Objects</h2>
<p>An <strong>object</strong> is an instance of a class. You can create many objects from the same class.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Rectangle {
  public:
    double width;
    double height;

    double area()      { return width * height; }
    double perimeter() { return 2 * (width + height); }
};

int main() {
    Rectangle r1, r2;

    r1.width = 5.0; r1.height = 3.0;
    r2.width = 8.5; r2.height = 4.2;

    cout &lt;&lt; "R1 Area: "      &lt;&lt; r1.area()      &lt;&lt; endl;
    cout &lt;&lt; "R1 Perimeter: " &lt;&lt; r1.perimeter() &lt;&lt; endl;
    cout &lt;&lt; "R2 Area: "      &lt;&lt; r2.area()      &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">R1 Area: 15
R1 Perimeter: 16
R2 Area: 35.7</code></pre>

<h2 id="this">The this Pointer</h2>
<p>Inside a method, <code>this</code> is a pointer to the current object:</p>
<pre><code class="language-cpp">class Box {
  public:
    int size;
    void setSize(int size) {
        this-&gt;size = size;   // 'this->size' is the member; 'size' is the parameter
    }
};</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>BankAccount</code> class with <code>owner</code> (string), <code>balance</code> (double). Add methods <code>deposit(double)</code>, <code>withdraw(double)</code>, and <code>printBalance()</code>. Test with two accounts.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class BankAccount {
  public:
    string owner;
    double balance;
    void deposit(double amount)  { balance += amount; }
    void withdraw(double amount) { if (amount &lt;= balance) balance -= amount; }
    void printBalance()          { cout &lt;&lt; owner &lt;&lt; ": $" &lt;&lt; balance &lt;&lt; endl; }
};

int main() {
    BankAccount acc;
    acc.owner = "Alice"; acc.balance = 1000.0;
    acc.deposit(500);
    acc.withdraw(200);
    acc.printBalance();   // Alice: $1300
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What is the relationship between a class and an object?</p><ul><li>A) They are the same thing</li><li>B) A class is an instance of an object</li><li>C) An object is an instance of a class</li><li>D) Objects contain classes</li></ul><details><summary>Answer</summary><p><strong>C)</strong> A class is the blueprint; an object is a concrete instance of that blueprint.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Objects are instances of classes created using the class name as a type.</li>
<li>Each object has its own copy of member variables.</li>
<li><code>this</code> is a pointer to the current object inside a method.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-classes" class="btn btn-secondary">&larr; Previous: Classes</a>
<a href="/tutorials/cpp/cpp-constructors" class="btn btn-primary">Next: Constructors &rarr;</a>
</div>
</div>$$,
'Learn how to create and use C++ objects. Understand the relationship between classes and objects, and the this pointer.',
'TUTORIAL', 'CamboFreelance', 'c++,objects,oop,intermediate', NOW(), 23, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Constructors',
'cpp-constructors',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Constructors</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 24 of 50</span></div>
<div class="toc-box"><h3>In This Lesson</h3><ul><li><a href="#default">Default Constructor</a></li><li><a href="#param">Parameterized Constructor</a></li><li><a href="#copy">Copy Constructor</a></li><li><a href="#initializer">Initializer List</a></li><li><a href="#quiz">Quiz</a></li></ul></div>

<h2 id="default">Default Constructor</h2>
<p>A <strong>constructor</strong> is a special method that runs automatically when an object is created. It has the same name as the class and no return type.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Student {
  public:
    string name;
    int    grade;

    // Default constructor
    Student() {
        name  = "Unknown";
        grade = 0;
        cout &lt;&lt; "Student created!" &lt;&lt; endl;
    }
};

int main() {
    Student s;   // Constructor called automatically
    cout &lt;&lt; s.name &lt;&lt; ", Grade: " &lt;&lt; s.grade &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Student created!
Unknown, Grade: 0</code></pre>

<h2 id="param">Parameterized Constructor</h2>
<pre><code class="language-cpp">class Student {
  public:
    string name;
    int    grade;

    Student(string n, int g) {
        name  = n;
        grade = g;
    }

    void display() {
        cout &lt;&lt; name &lt;&lt; " — Grade: " &lt;&lt; grade &lt;&lt; endl;
    }
};

int main() {
    Student s1("Alice", 95);
    Student s2("Bob",   82);
    s1.display();
    s2.display();
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Alice — Grade: 95
Bob — Grade: 82</code></pre>

<h2 id="initializer">Member Initializer List</h2>
<p>A more efficient way to initialize members — required for <code>const</code> and reference members:</p>
<pre><code class="language-cpp">class Circle {
  public:
    const double PI;
    double radius;

    Circle(double r) : PI(3.14159), radius(r) { }

    double area() { return PI * radius * radius; }
};

Circle c(5.0);
cout &lt;&lt; c.area();   // 78.54</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Product</code> class with a parameterized constructor that sets <code>name</code>, <code>price</code>, and <code>quantity</code>. Add a method <code>totalValue()</code> that returns price × quantity.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Product {
  public:
    string name;
    double price;
    int    quantity;

    Product(string n, double p, int q) : name(n), price(p), quantity(q) { }

    double totalValue() { return price * quantity; }
};

int main() {
    Product p("Widget", 9.99, 50);
    cout &lt;&lt; p.name &lt;&lt; " total: $" &lt;&lt; p.totalValue() &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>When is a constructor called?</p><ul><li>A) When a method is invoked</li><li>B) Automatically when an object is created</li><li>C) When the object is destroyed</li><li>D) Only when explicitly called</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Constructors run automatically when the object is instantiated.</p></details></li>
<li><p>What is the return type of a constructor?</p><ul><li>A) <code>void</code></li><li>B) The class type</li><li>C) <code>int</code></li><li>D) No return type</li></ul><details><summary>Answer</summary><p><strong>D)</strong> Constructors have no return type — not even <code>void</code>.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Constructors initialize objects when they are created.</li>
<li>The default constructor takes no arguments.</li>
<li>Parameterized constructors accept arguments to set initial values.</li>
<li>Member initializer lists are more efficient and required for const members.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-objects" class="btn btn-secondary">&larr; Previous: Objects</a>
<a href="/tutorials/cpp/cpp-access-specifiers" class="btn btn-primary">Next: Access Specifiers &rarr;</a>
</div>
</div>$$,
'Learn C++ constructors: default, parameterized, and copy constructors. Master member initializer lists for efficient initialization.',
'TUTORIAL', 'CamboFreelance', 'c++,constructors,oop,intermediate', NOW(), 24, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Access Specifiers',
'cpp-access-specifiers',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Access Specifiers</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">6 min read</span><span class="lesson-number">Lesson 25 of 50</span></div>
<h2 id="specifiers">public, private, protected</h2>
<table><thead><tr><th>Specifier</th><th>Accessible From</th></tr></thead><tbody>
<tr><td><code>public</code></td><td>Anywhere — inside and outside the class</td></tr>
<tr><td><code>private</code></td><td>Only inside the class (default for class members)</td></tr>
<tr><td><code>protected</code></td><td>Inside the class and derived (child) classes</td></tr>
</tbody></table>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Employee {
  private:
    double salary;   // cannot be accessed directly from outside

  public:
    string name;
    Employee(string n, double s) : name(n), salary(s) { }
    double getSalary()              { return salary; }
    void   setSalary(double s)      { if (s &gt; 0) salary = s; }
};

int main() {
    Employee e("Alice", 5000);
    cout &lt;&lt; e.name &lt;&lt; " earns $" &lt;&lt; e.getSalary() &lt;&lt; endl;
    e.setSalary(5500);
    cout &lt;&lt; "New salary: $" &lt;&lt; e.getSalary() &lt;&lt; endl;
    // e.salary = -999;  // ERROR — private member
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Alice earns $5000
New salary: $5500</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Temperature</code> class with a private <code>celsius</code> field. Add public methods to set (validate &gt;= -273.15), get celsius, and get fahrenheit (F = C * 9/5 + 32).</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Temperature {
  private:
    double celsius;
  public:
    void setCelsius(double c) {
        if (c >= -273.15) celsius = c;
    }
    double getCelsius()    { return celsius; }
    double getFahrenheit() { return celsius * 9.0/5.0 + 32; }
};

int main() {
    Temperature t;
    t.setCelsius(100);
    cout &lt;&lt; t.getCelsius()    &lt;&lt; "C" &lt;&lt; endl;
    cout &lt;&lt; t.getFahrenheit() &lt;&lt; "F" &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which access specifier restricts access to the class itself only?</p><ul><li>A) public</li><li>B) protected</li><li>C) private</li><li>D) internal</li></ul><details><summary>Answer</summary><p><strong>C) private</strong></p></details></li>
<li><p>What is the default access specifier for class members in C++?</p><ul><li>A) public</li><li>B) protected</li><li>C) private</li><li>D) internal</li></ul><details><summary>Answer</summary><p><strong>C) private</strong> — unlike <code>struct</code>, where the default is <code>public</code>.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>public</code> — accessible everywhere.</li>
<li><code>private</code> — accessible only inside the class (default).</li>
<li><code>protected</code> — accessible inside the class and subclasses.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-constructors" class="btn btn-secondary">&larr; Previous: Constructors</a>
<a href="/tutorials/cpp/cpp-encapsulation" class="btn btn-primary">Next: Encapsulation &rarr;</a>
</div>
</div>$$,
'Understand C++ access specifiers: public, private, and protected. Learn how they control class member visibility and enforce data hiding.',
'TUTORIAL', 'CamboFreelance', 'c++,access-specifiers,oop,intermediate', NOW(), 25, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Encapsulation',
'cpp-encapsulation',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Encapsulation</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 26 of 50</span></div>
<h2 id="what">What is Encapsulation?</h2>
<p><strong>Encapsulation</strong> is the OOP principle of hiding internal data and only exposing it through controlled public methods (getters and setters). It protects data integrity and reduces coupling.</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;string&gt;
using namespace std;

class User {
  private:
    string username;
    string password;
    int    loginAttempts;

  public:
    User(string u, string p) : username(u), password(p), loginAttempts(0) { }

    bool login(string pass) {
        if (loginAttempts >= 3) {
            cout &lt;&lt; "Account locked!" &lt;&lt; endl;
            return false;
        }
        if (pass == password) {
            loginAttempts = 0;
            cout &lt;&lt; "Welcome, " &lt;&lt; username &lt;&lt; "!" &lt;&lt; endl;
            return true;
        }
        loginAttempts++;
        cout &lt;&lt; "Wrong password. Attempt " &lt;&lt; loginAttempts &lt;&lt; "/3" &lt;&lt; endl;
        return false;
    }

    string getUsername() { return username; }
};

int main() {
    User u("alice", "secret123");
    u.login("wrong");
    u.login("secret123");
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Wrong password. Attempt 1/3
Welcome, alice!</code></pre>

<div class="note-box"><strong>Why Encapsulation?</strong> If <code>loginAttempts</code> were public, anyone could reset it to bypass the lockout. Private data forces all access through the <code>login()</code> method which enforces the business rule.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Counter</code> class with a private <code>count</code> (int, starts at 0). Add: <code>increment()</code>, <code>decrement()</code> (min 0), <code>reset()</code>, and <code>getCount()</code>.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Counter {
  private:
    int count;
  public:
    Counter() : count(0) { }
    void increment()     { count++; }
    void decrement()     { if (count &gt; 0) count--; }
    void reset()         { count = 0; }
    int  getCount()      { return count; }
};

int main() {
    Counter c;
    c.increment(); c.increment(); c.increment();
    cout &lt;&lt; c.getCount() &lt;&lt; endl;   // 3
    c.decrement();
    cout &lt;&lt; c.getCount() &lt;&lt; endl;   // 2
    c.reset();
    cout &lt;&lt; c.getCount() &lt;&lt; endl;   // 0
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What is the purpose of encapsulation?</p><ul><li>A) To make all data public</li><li>B) To hide internal data and control access through methods</li><li>C) To inherit from multiple classes</li><li>D) To speed up execution</li></ul><details><summary>Answer</summary><p><strong>B)</strong> Encapsulation hides internal state and exposes only a controlled interface.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Encapsulation = private data + public getters/setters.</li>
<li>Prevents invalid state by validating input in setters.</li>
<li>Reduces coupling — callers don't depend on internal implementation.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-access-specifiers" class="btn btn-secondary">&larr; Previous: Access Specifiers</a>
<a href="/tutorials/cpp/cpp-inheritance" class="btn btn-primary">Next: Inheritance &rarr;</a>
</div>
</div>$$,
'Master C++ encapsulation: hide private data, expose public getters and setters, and enforce business rules within classes.',
'TUTORIAL', 'CamboFreelance', 'c++,encapsulation,oop,intermediate', NOW(), 26, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Inheritance',
'cpp-inheritance',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Inheritance</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">9 min read</span><span class="lesson-number">Lesson 27 of 50</span></div>
<h2 id="what">What is Inheritance?</h2>
<p><strong>Inheritance</strong> lets a class (child/derived) acquire attributes and methods of another class (parent/base), promoting code reuse.</p>
<pre><code class="language-cpp">class DerivedClass : accessSpecifier BaseClass { };</code></pre>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

// Base class
class Animal {
  public:
    string name;
    void eat()   { cout &lt;&lt; name &lt;&lt; " is eating." &lt;&lt; endl; }
    void sleep() { cout &lt;&lt; name &lt;&lt; " is sleeping." &lt;&lt; endl; }
};

// Derived class
class Dog : public Animal {
  public:
    void bark() { cout &lt;&lt; name &lt;&lt; " says: Woof!" &lt;&lt; endl; }
};

class Cat : public Animal {
  public:
    void meow() { cout &lt;&lt; name &lt;&lt; " says: Meow!" &lt;&lt; endl; }
};

int main() {
    Dog d; d.name = "Rex";
    d.eat();    // inherited from Animal
    d.bark();   // Dog's own method

    Cat c; c.name = "Whiskers";
    c.sleep();  // inherited
    c.meow();
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Rex is eating.
Rex says: Woof!
Whiskers is sleeping.
Whiskers says: Meow!</code></pre>

<h2 id="constructor">Constructor Inheritance</h2>
<p>The derived constructor calls the base constructor using the initializer list:</p>
<pre><code class="language-cpp">class Vehicle {
  public:
    string brand;
    Vehicle(string b) : brand(b) { }
};

class ElectricCar : public Vehicle {
  public:
    int range;
    ElectricCar(string b, int r) : Vehicle(b), range(r) { }
    void info() {
        cout &lt;&lt; brand &lt;&lt; " — Range: " &lt;&lt; range &lt;&lt; "km" &lt;&lt; endl;
    }
};

ElectricCar tesla("Tesla", 600);
tesla.info();   // Tesla — Range: 600km</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a base class <code>Shape</code> with a <code>color</code> attribute and a <code>describe()</code> method. Derive <code>Circle</code> (with <code>radius</code>) and <code>Square</code> (with <code>side</code>), each with an <code>area()</code> method.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;cmath&gt;
using namespace std;

class Shape {
  public:
    string color;
    Shape(string c) : color(c) { }
    void describe() { cout &lt;&lt; "Color: " &lt;&lt; color &lt;&lt; endl; }
};

class Circle : public Shape {
  public:
    double radius;
    Circle(string c, double r) : Shape(c), radius(r) { }
    double area() { return 3.14159 * radius * radius; }
};

class Square : public Shape {
  public:
    double side;
    Square(string c, double s) : Shape(c), side(s) { }
    double area() { return side * side; }
};

int main() {
    Circle ci("Red", 5);
    ci.describe();
    cout &lt;&lt; "Circle area: " &lt;&lt; ci.area() &lt;&lt; endl;

    Square sq("Blue", 4);
    sq.describe();
    cout &lt;&lt; "Square area: " &lt;&lt; sq.area() &lt;&lt; endl;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which keyword is used to inherit from a base class?</p><ul><li>A) extends</li><li>B) inherits</li><li>C) : (colon)</li><li>D) &lt;&lt;</li></ul><details><summary>Answer</summary><p><strong>C) Colon</strong> — e.g., <code>class Dog : public Animal</code>.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Inheritance enables a derived class to reuse base class members.</li>
<li>Use <code>class Child : public Parent</code> syntax.</li>
<li>Derived constructors call base constructors via initializer lists.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-encapsulation" class="btn btn-secondary">&larr; Previous: Encapsulation</a>
<a href="/tutorials/cpp/cpp-polymorphism" class="btn btn-primary">Next: Polymorphism &rarr;</a>
</div>
</div>$$,
'Learn C++ inheritance: derive classes from a base class, reuse code, call base constructors, and build class hierarchies.',
'TUTORIAL', 'CamboFreelance', 'c++,inheritance,oop,intermediate', NOW(), 27, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Polymorphism',
'cpp-polymorphism',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Polymorphism</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">9 min read</span><span class="lesson-number">Lesson 28 of 50</span></div>
<h2 id="what">What is Polymorphism?</h2>
<p><strong>Polymorphism</strong> means "many forms". A single interface (base class pointer) can call different implementations depending on the actual object type at runtime.</p>

<h2 id="virtual">Virtual Functions</h2>
<p>Declare a method <code>virtual</code> in the base class to enable runtime polymorphism:</p>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Shape {
  public:
    virtual void draw() {
        cout &lt;&lt; "Drawing a shape" &lt;&lt; endl;
    }
};

class Circle : public Shape {
  public:
    void draw() override {
        cout &lt;&lt; "Drawing a Circle" &lt;&lt; endl;
    }
};

class Triangle : public Shape {
  public:
    void draw() override {
        cout &lt;&lt; "Drawing a Triangle" &lt;&lt; endl;
    }
};

int main() {
    Shape* shapes[3];
    shapes[0] = new Shape();
    shapes[1] = new Circle();
    shapes[2] = new Triangle();

    for (int i = 0; i &lt; 3; i++) {
        shapes[i]-&gt;draw();   // correct method called for each type
    }

    // cleanup
    for (int i = 0; i &lt; 3; i++) delete shapes[i];
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Drawing a shape
Drawing a Circle
Drawing a Triangle</code></pre>

<div class="note-box"><strong>override keyword:</strong> Always use <code>override</code> on derived class methods. It tells the compiler to verify that you are indeed overriding a virtual function — catching typos and signature mismatches at compile time.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create a <code>Animal</code> base class with virtual <code>speak()</code>. Derive <code>Dog</code>, <code>Cat</code>, <code>Bird</code> — each with their own <code>speak()</code>. Store all three in an array of base pointers and call <code>speak()</code> on each.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Animal {
  public:
    virtual void speak() { cout &lt;&lt; "..." &lt;&lt; endl; }
    virtual ~Animal() { }
};

class Dog   : public Animal { public: void speak() override { cout &lt;&lt; "Woof!" &lt;&lt; endl; } };
class Cat   : public Animal { public: void speak() override { cout &lt;&lt; "Meow!" &lt;&lt; endl; } };
class Bird  : public Animal { public: void speak() override { cout &lt;&lt; "Tweet!" &lt;&lt; endl; } };

int main() {
    Animal* animals[] = { new Dog(), new Cat(), new Bird() };
    for (Animal* a : animals) { a-&gt;speak(); delete a; }
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>What keyword enables runtime polymorphism in C++?</p><ul><li>A) <code>abstract</code></li><li>B) <code>virtual</code></li><li>C> <code>override</code></li><li>D) <code>poly</code></li></ul><details><summary>Answer</summary><p><strong>B) <code>virtual</code></strong> — declaring a method <code>virtual</code> in the base class enables runtime dispatch.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Polymorphism allows different objects to respond to the same interface differently.</li>
<li>Use <code>virtual</code> in the base class and <code>override</code> in derived classes.</li>
<li>Base class pointers/references can hold derived objects.</li>
<li>Add a virtual destructor to base classes when using polymorphism.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-inheritance" class="btn btn-secondary">&larr; Previous: Inheritance</a>
<a href="/tutorials/cpp/cpp-abstraction" class="btn btn-primary">Next: Abstraction &rarr;</a>
</div>
</div>$$,
'Understand C++ polymorphism with virtual functions and override. Learn runtime dispatch, base class pointers, and practical OOP design.',
'TUTORIAL', 'CamboFreelance', 'c++,polymorphism,virtual,oop,intermediate', NOW(), 28, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Abstraction',
'cpp-abstraction',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Abstraction</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">7 min read</span><span class="lesson-number">Lesson 29 of 50</span></div>
<h2 id="what">What is Abstraction?</h2>
<p><strong>Abstraction</strong> hides complex implementation details and exposes only what is necessary. In C++, abstract classes with <strong>pure virtual functions</strong> define an interface.</p>

<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

// Abstract class (interface)
class IDatabase {
  public:
    virtual void connect()    = 0;   // pure virtual
    virtual void disconnect() = 0;   // pure virtual
    virtual void query(string sql) = 0;
    virtual ~IDatabase() { }
};

class MySQLDatabase : public IDatabase {
  public:
    void connect()    override { cout &lt;&lt; "MySQL: Connected" &lt;&lt; endl; }
    void disconnect() override { cout &lt;&lt; "MySQL: Disconnected" &lt;&lt; endl; }
    void query(string sql) override {
        cout &lt;&lt; "MySQL executing: " &lt;&lt; sql &lt;&lt; endl;
    }
};

class PostgresDatabase : public IDatabase {
  public:
    void connect()    override { cout &lt;&lt; "PostgreSQL: Connected" &lt;&lt; endl; }
    void disconnect() override { cout &lt;&lt; "PostgreSQL: Disconnected" &lt;&lt; endl; }
    void query(string sql) override {
        cout &lt;&lt; "PostgreSQL executing: " &lt;&lt; sql &lt;&lt; endl;
    }
};

int main() {
    IDatabase* db = new MySQLDatabase();
    db-&gt;connect();
    db-&gt;query("SELECT * FROM users");
    db-&gt;disconnect();
    delete db;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">MySQL: Connected
MySQL executing: SELECT * FROM users
MySQL: Disconnected</code></pre>

<div class="note-box"><strong>Key Rules:</strong> A class with at least one pure virtual function (<code>= 0</code>) is abstract. You cannot instantiate an abstract class directly — only its concrete subclasses.</div>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Create an abstract class <code>Notification</code> with a pure virtual method <code>send(string message)</code>. Implement <code>EmailNotification</code> and <code>SMSNotification</code> subclasses.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
using namespace std;

class Notification {
  public:
    virtual void send(string msg) = 0;
    virtual ~Notification() { }
};

class EmailNotification : public Notification {
  public:
    void send(string msg) override {
        cout &lt;&lt; "Email: " &lt;&lt; msg &lt;&lt; endl;
    }
};

class SMSNotification : public Notification {
  public:
    void send(string msg) override {
        cout &lt;&lt; "SMS: " &lt;&lt; msg &lt;&lt; endl;
    }
};

int main() {
    Notification* n1 = new EmailNotification();
    Notification* n2 = new SMSNotification();
    n1-&gt;send("Welcome!");
    n2-&gt;send("Your code is 1234");
    delete n1; delete n2;
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>How is a pure virtual function declared?</p><ul><li>A) <code>virtual void f() {}</code></li><li>B) <code>abstract void f();</code></li><li>C) <code>virtual void f() = 0;</code></li><li>D) <code>void f() = pure;</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>virtual void f() = 0;</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li>Abstract classes define an interface with pure virtual functions (<code>= 0</code>).</li>
<li>Cannot instantiate abstract classes — only concrete subclasses.</li>
<li>Abstraction separates "what" from "how" — a key OOP principle.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-polymorphism" class="btn btn-secondary">&larr; Previous: Polymorphism</a>
<a href="/tutorials/cpp/cpp-files" class="btn btn-primary">Next: C++ Files &rarr;</a>
</div>
</div>$$,
'Learn C++ abstraction with abstract classes and pure virtual functions. Build flexible interfaces that separate what from how.',
'TUTORIAL', 'CamboFreelance', 'c++,abstraction,abstract-class,oop,intermediate', NOW(), 29, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Files',
'cpp-files',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Files</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 30 of 50</span></div>
<h2 id="fstream">File I/O with fstream</h2>
<p>C++ uses the <code>&lt;fstream&gt;</code> header for file operations. Three classes:</p>
<table><thead><tr><th>Class</th><th>Purpose</th></tr></thead><tbody>
<tr><td><code>ofstream</code></td><td>Write to files (output file stream)</td></tr>
<tr><td><code>ifstream</code></td><td>Read from files (input file stream)</td></tr>
<tr><td><code>fstream</code></td><td>Read and write</td></tr>
</tbody></table>

<h2 id="write">Writing to a File</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;fstream&gt;
using namespace std;

int main() {
    ofstream file("students.txt");   // creates or overwrites
    if (!file) {
        cerr &lt;&lt; "Cannot open file!" &lt;&lt; endl;
        return 1;
    }
    file &lt;&lt; "Alice, 95" &lt;&lt; endl;
    file &lt;&lt; "Bob,   82" &lt;&lt; endl;
    file &lt;&lt; "Carol, 78" &lt;&lt; endl;
    file.close();
    cout &lt;&lt; "File written successfully." &lt;&lt; endl;
    return 0;
}</code></pre>

<h2 id="read">Reading from a File</h2>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;fstream&gt;
#include &lt;string&gt;
using namespace std;

int main() {
    ifstream file("students.txt");
    string line;
    while (getline(file, line)) {
        cout &lt;&lt; line &lt;&lt; endl;
    }
    file.close();
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">Alice, 95
Bob,   82
Carol, 78</code></pre>

<h2 id="append">Append Mode</h2>
<pre><code class="language-cpp">ofstream file("log.txt", ios::app);   // append without erasing
file &lt;&lt; "New log entry" &lt;&lt; endl;
file.close();</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a program that: (1) writes 5 numbers (1–5) to a file, one per line. (2) Reads them back and prints their sum.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;fstream&gt;
using namespace std;

int main() {
    // Write
    ofstream out("numbers.txt");
    for (int i = 1; i &lt;= 5; i++) out &lt;&lt; i &lt;&lt; endl;
    out.close();

    // Read and sum
    ifstream in("numbers.txt");
    int n, sum = 0;
    while (in &gt;&gt; n) sum += n;
    in.close();
    cout &lt;&lt; "Sum: " &lt;&lt; sum &lt;&lt; endl;   // 15
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which class is used to write to a file in C++?</p><ul><li>A) <code>ifstream</code></li><li>B) <code>fileout</code></li><li>C) <code>ofstream</code></li><li>D) <code>writefile</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>ofstream</code></strong></p></details></li>
<li><p>Which flag opens a file in append mode?</p><ul><li>A) <code>ios::in</code></li><li>B> <code>ios::append</code></li><li>C) <code>ios::app</code></li><li>D) <code>ios::add</code></li></ul><details><summary>Answer</summary><p><strong>C) <code>ios::app</code></strong></p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>#include &lt;fstream&gt;</code> for file I/O.</li>
<li><code>ofstream</code> writes; <code>ifstream</code> reads; <code>fstream</code> does both.</li>
<li>Always check if the file opened and close it when done.</li>
<li>Use <code>ios::app</code> to append without erasing existing content.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-abstraction" class="btn btn-secondary">&larr; Previous: Abstraction</a>
<a href="/tutorials/cpp/cpp-exception-handling" class="btn btn-primary">Next: Exception Handling &rarr;</a>
</div>
</div>$$,
'Learn C++ file I/O with fstream, ofstream, and ifstream. Read, write, and append files with error checking.',
'TUTORIAL', 'CamboFreelance', 'c++,files,fstream,file-io,intermediate', NOW(), 30, 'PUBLISHED', 'ACT', 'SYS'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
gen_random_uuid(),
'C++ Exception Handling',
'cpp-exception-handling',
$$<div class="tutorial-content">
<nav class="breadcrumb"><a href="/tutorials">Tutorials</a> &rsaquo; <a href="/tutorials/cpp">C++</a> &rsaquo; Exception Handling</nav>
<div class="lesson-meta"><span class="badge badge-intermediate">Intermediate</span><span class="read-time">8 min read</span><span class="lesson-number">Lesson 31 of 50</span></div>
<h2 id="what">What is Exception Handling?</h2>
<p>Exceptions are runtime errors. C++ uses <code>try</code>, <code>catch</code>, and <code>throw</code> to handle them gracefully instead of crashing.</p>

<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;stdexcept&gt;
using namespace std;

double divide(double a, double b) {
    if (b == 0) throw invalid_argument("Division by zero!");
    return a / b;
}

int main() {
    try {
        cout &lt;&lt; divide(10, 2)  &lt;&lt; endl;   // 5
        cout &lt;&lt; divide(10, 0)  &lt;&lt; endl;   // throws!
    } catch (const invalid_argument&amp; e) {
        cout &lt;&lt; "Error: " &lt;&lt; e.what() &lt;&lt; endl;
    }
    cout &lt;&lt; "Program continues..." &lt;&lt; endl;
    return 0;
}</code></pre>
<h3>Output:</h3>
<pre><code class="language-text">5
Error: Division by zero!
Program continues...</code></pre>

<h2 id="multiple">Multiple catch Blocks</h2>
<pre><code class="language-cpp">try {
    int x = -1;
    if (x &lt; 0)  throw out_of_range("Negative value");
    if (x == 0) throw runtime_error("Zero value");
}
catch (const out_of_range&amp; e) {
    cout &lt;&lt; "Range error: " &lt;&lt; e.what() &lt;&lt; endl;
}
catch (const runtime_error&amp; e) {
    cout &lt;&lt; "Runtime error: " &lt;&lt; e.what() &lt;&lt; endl;
}
catch (...) {
    cout &lt;&lt; "Unknown error" &lt;&lt; endl;
}</code></pre>

<h2 id="custom">Custom Exceptions</h2>
<pre><code class="language-cpp">class InsufficientFundsException : public exception {
  public:
    const char* what() const noexcept override {
        return "Insufficient funds in account";
    }
};

void withdraw(double balance, double amount) {
    if (amount &gt; balance) throw InsufficientFundsException();
}

try {
    withdraw(100.0, 200.0);
} catch (const InsufficientFundsException&amp; e) {
    cout &lt;&lt; e.what() &lt;&lt; endl;
}</code></pre>

<div class="exercise-box" id="exercise">
<h2>Exercise</h2>
<p>Write a function <code>getElement(int arr[], int size, int index)</code> that throws <code>out_of_range</code> if index is invalid, and returns the element otherwise. Test with valid and invalid indices.</p>
<details><summary>Show Solution</summary>
<pre><code class="language-cpp">#include &lt;iostream&gt;
#include &lt;stdexcept&gt;
using namespace std;

int getElement(int arr[], int size, int index) {
    if (index &lt; 0 || index &gt;= size)
        throw out_of_range("Index out of bounds: " + to_string(index));
    return arr[index];
}

int main() {
    int arr[] = {10, 20, 30, 40, 50};
    try {
        cout &lt;&lt; getElement(arr, 5, 2)  &lt;&lt; endl;   // 30
        cout &lt;&lt; getElement(arr, 5, 10) &lt;&lt; endl;   // throws
    } catch (const out_of_range&amp; e) {
        cout &lt;&lt; "Error: " &lt;&lt; e.what() &lt;&lt; endl;
    }
    return 0;
}</code></pre></details>
</div>

<div class="quiz-box" id="quiz">
<h2>Quiz</h2>
<ol>
<li><p>Which block contains code that might throw an exception?</p><ul><li>A) catch</li><li>B> throw</li><li>C) try</li><li>D> handle</li></ul><details><summary>Answer</summary><p><strong>C) try</strong></p></details></li>
<li><p>What does <code>catch (...)</code> catch?</p><ul><li>A) Only standard exceptions</li><li>B> Only runtime_error</li><li>C) Any exception of any type</li><li>D) Nothing</li></ul><details><summary>Answer</summary><p><strong>C)</strong> <code>catch (...)</code> is a catch-all handler for any exception type.</p></details></li>
</ol>
</div>

<h2 id="summary">Summary</h2>
<ul>
<li><code>throw</code> raises an exception; <code>try</code> wraps risky code; <code>catch</code> handles it.</li>
<li>Catch by <code>const reference</code> for efficiency.</li>
<li>Use <code>catch (...)</code> as a fallback for unknown exceptions.</li>
<li>Create custom exceptions by inheriting from <code>std::exception</code>.</li>
</ul>
<div class="lesson-nav-buttons">
<a href="/tutorials/cpp/cpp-files" class="btn btn-secondary">&larr; Previous: Files</a>
<a href="/tutorials/cpp/cpp-namespaces" class="btn btn-primary">Next: Namespaces &rarr;</a>
</div>
</div>$$,
'Master C++ exception handling with try, catch, and throw. Handle runtime errors gracefully and create custom exception classes.',
'TUTORIAL', 'CamboFreelance', 'c++,exceptions,error-handling,intermediate', NOW(), 31, 'PUBLISHED', 'ACT', 'SYS'
);
