-- Section 1: Introduction & Setup (Lessons 1–5)

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Introduction to Dart',
    'dart-introduction',
    $dart_01$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-is-dart">What is Dart?</a></li>
    <li><a href="#why-dart">Why Learn Dart?</a></li>
    <li><a href="#features">Key Features of Dart</a></li>
    <li><a href="#where-used">Where is Dart Used?</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-is-dart">What is Dart?</h2>
<p>Dart is a modern, open-source, object-oriented programming language developed by Google. Originally released in 2011, Dart was designed to build fast applications on any platform — web, mobile, server, and desktop. It is the primary language used with Flutter, Google's popular UI framework for cross-platform development.</p>
<p>Dart combines the familiarity of languages like Java and C# with modern features such as null safety, strong typing, and asynchronous programming. If you have experience with any C-style language, you will find Dart easy to pick up.</p>

<h2 id="why-dart">Why Learn Dart?</h2>
<p>Learning Dart opens the door to Flutter development — allowing you to build beautiful mobile, web, and desktop apps from a single codebase. Here are the top reasons to learn Dart:</p>
<ul>
  <li><strong>Flutter-powered:</strong> Dart is the only language supported by Flutter, which is used by millions of developers worldwide.</li>
  <li><strong>Fast execution:</strong> Dart compiles to native machine code (AOT) for maximum performance and can also run in a JIT mode for fast development cycles.</li>
  <li><strong>Strongly typed:</strong> Dart's type system catches errors at compile time, reducing bugs.</li>
  <li><strong>Null safety:</strong> Dart's sound null safety system prevents null reference errors — one of the most common programming mistakes.</li>
  <li><strong>Growing ecosystem:</strong> Pub.dev hosts thousands of packages to extend your Dart applications.</li>
</ul>

<h2 id="features">Key Features of Dart</h2>
<ul>
  <li>Object-oriented with classes, interfaces, and mixins</li>
  <li>Sound null safety (since Dart 2.12)</li>
  <li>Asynchronous programming with Future and Stream</li>
  <li>AOT and JIT compilation</li>
  <li>Rich standard library</li>
  <li>Strong type inference</li>
  <li>Hot reload (via Flutter) for rapid development</li>
</ul>

<h2 id="where-used">Where is Dart Used?</h2>
<table>
  <thead><tr><th>Platform</th><th>Use Case</th></tr></thead>
  <tbody>
    <tr><td>Mobile (iOS &amp; Android)</td><td>Flutter apps</td></tr>
    <tr><td>Web</td><td>Flutter Web, Dart Web</td></tr>
    <tr><td>Desktop</td><td>Flutter Desktop (Windows, macOS, Linux)</td></tr>
    <tr><td>Server</td><td>REST APIs, CLI tools</td></tr>
    <tr><td>Embedded</td><td>IoT with Flutter Embedded</td></tr>
  </tbody>
</table>

<div class="note-box">
  <strong>📝 Note:</strong> Dart was originally designed as a replacement for JavaScript for web development, but its scope expanded dramatically after Flutter's success. Today it is primarily known as the Flutter language.
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Who developed the Dart programming language?</strong></p>
  <ul><li>A) Microsoft</li><li>B) Google ✅</li><li>C) Oracle</li><li>D) Apple</li></ul>
  <p><strong>2. Which framework primarily uses Dart?</strong></p>
  <ul><li>A) React Native</li><li>B) Xamarin</li><li>C) Flutter ✅</li><li>D) Ionic</li></ul>
  <p><strong>3. What compilation modes does Dart support?</strong></p>
  <ul><li>A) Only interpreted</li><li>B) AOT and JIT ✅</li><li>C) Only AOT</li><li>D) Bytecode only</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Dart is a modern, strongly-typed, object-oriented language created by Google. It powers Flutter, supports null safety, and compiles to native code for high performance. Learning Dart is the first step toward building cross-platform applications with Flutter.</p>
    $dart_01$,
    'Learn what Dart is, why it was created by Google, its key features, and where it is used — from mobile apps with Flutter to server-side development.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,introduction',
    NOW(),
    1,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Installing Dart SDK',
    'dart-sdk-installation',
    $dart_02$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#prerequisites">Prerequisites</a></li>
    <li><a href="#windows">Install on Windows</a></li>
    <li><a href="#mac">Install on macOS</a></li>
    <li><a href="#linux">Install on Linux</a></li>
    <li><a href="#verify">Verify Installation</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="prerequisites">Prerequisites</h2>
<p>Before installing Dart, make sure your system meets these requirements:</p>
<ul>
  <li>Windows 10 / macOS 10.14+ / Ubuntu 18.04+</li>
  <li>Internet connection</li>
  <li>Administrator/sudo privileges</li>
</ul>

<h2 id="windows">Install on Windows</h2>
<p>The easiest way to install Dart on Windows is using the Chocolatey package manager.</p>
<pre><code>choco install dart-sdk</code></pre>
<p>Alternatively, download the Windows SDK installer from the official Dart website at <strong>dart.dev/get-dart</strong> and follow the installation wizard.</p>

<h2 id="mac">Install on macOS</h2>
<p>Use Homebrew to install Dart on macOS:</p>
<pre><code>brew tap dart-lang/dart
brew install dart</code></pre>
<p>To upgrade an existing installation:</p>
<pre><code>brew upgrade dart</code></pre>

<h2 id="linux">Install on Linux</h2>
<p>On Debian/Ubuntu systems:</p>
<pre><code>sudo apt-get update
sudo apt-get install apt-transport-https
wget -qO- https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo gpg --dearmor -o /usr/share/keyrings/dart.gpg
echo 'deb [signed-by=/usr/share/keyrings/dart.gpg arch=amd64] https://storage.googleapis.com/download.dartlang.org/linux/debian stable main' | sudo tee /etc/apt/sources.list.d/dart_stable.list
sudo apt-get update
sudo apt-get install dart</code></pre>

<h2 id="verify">Verify Installation</h2>
<p>After installation, open your terminal and run:</p>
<pre><code>dart --version</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Dart SDK version: 3.x.x (stable) on "linux_x64"</pre>
</div>
<p>You should see the installed Dart version. Also verify the <code>dart</code> command works:</p>
<pre><code>dart help</code></pre>

<div class="note-box">
  <strong>📝 Note:</strong> If you plan to use Flutter, installing Flutter SDK also installs the compatible Dart SDK automatically. You do not need to install Dart separately when using Flutter.
</div>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Install Dart SDK on your operating system.</li>
    <li>Open a terminal and verify the installation with <code>dart --version</code>.</li>
    <li>Run <code>dart help</code> and explore the available commands.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Which command verifies the Dart installation?</strong></p>
  <ul><li>A) dart check</li><li>B) dart --version ✅</li><li>C) dart install</li><li>D) dart verify</li></ul>
  <p><strong>2. Which tool installs Dart on macOS?</strong></p>
  <ul><li>A) npm</li><li>B) apt-get</li><li>C) Homebrew ✅</li><li>D) pip</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>You can install the Dart SDK on Windows (Chocolatey), macOS (Homebrew), or Linux (apt-get). After installation, verify it with <code>dart --version</code>. If you use Flutter, the Dart SDK is included automatically.</p>
    $dart_02$,
    'Step-by-step guide to installing the Dart SDK on Windows, macOS, and Linux, plus how to verify your installation is working correctly.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,setup,installation',
    NOW(),
    2,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Setting Up VS Code for Dart',
    'dart-vscode-setup',
    $dart_03$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#install-vscode">Install VS Code</a></li>
    <li><a href="#dart-extension">Install Dart Extension</a></li>
    <li><a href="#create-project">Create a Dart Project</a></li>
    <li><a href="#run-program">Run Your First Program</a></li>
    <li><a href="#useful-extensions">Useful Extensions</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="install-vscode">Install VS Code</h2>
<p>Visual Studio Code (VS Code) is the recommended editor for Dart development. Download it from <strong>code.visualstudio.com</strong> and install it for your operating system.</p>

<h2 id="dart-extension">Install Dart Extension</h2>
<p>After opening VS Code:</p>
<ol>
  <li>Press <code>Ctrl+Shift+X</code> (or <code>Cmd+Shift+X</code> on Mac) to open Extensions.</li>
  <li>Search for <strong>Dart</strong>.</li>
  <li>Install the <strong>Dart</strong> extension by Dart Code.</li>
  <li>Optionally install the <strong>Flutter</strong> extension if you plan to use Flutter.</li>
</ol>
<p>The Dart extension provides:</p>
<ul>
  <li>Syntax highlighting</li>
  <li>Code completion (IntelliSense)</li>
  <li>Debugging support</li>
  <li>Code formatting</li>
  <li>Quick fixes and refactoring</li>
</ul>

<h2 id="create-project">Create a Dart Project</h2>
<p>Open VS Code terminal (<code>Ctrl+`</code>) and run:</p>
<pre><code>dart create my_first_app
cd my_first_app</code></pre>
<p>This creates a Dart project with the following structure:</p>
<pre><code>my_first_app/
├── bin/
│   └── my_first_app.dart   ← entry point
├── lib/
│   └── my_first_app.dart   ← library code
├── test/
│   └── my_first_app_test.dart
└── pubspec.yaml            ← project config</code></pre>

<h2 id="run-program">Run Your First Program</h2>
<p>Open <code>bin/my_first_app.dart</code> and you'll see:</p>
<pre><code>void main() {
  print('Hello, World!');
}</code></pre>
<p>Run it from the terminal:</p>
<pre><code>dart run</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Hello, World!</pre>
</div>
<p>You can also press <code>F5</code> in VS Code to launch the debugger.</p>

<h2 id="useful-extensions">Useful Extensions</h2>
<table>
  <thead><tr><th>Extension</th><th>Purpose</th></tr></thead>
  <tbody>
    <tr><td>Dart</td><td>Core Dart language support</td></tr>
    <tr><td>Flutter</td><td>Flutter framework support</td></tr>
    <tr><td>Pubspec Assist</td><td>Easily add pub.dev packages</td></tr>
    <tr><td>Error Lens</td><td>Inline error messages</td></tr>
    <tr><td>Bracket Pair Colorizer</td><td>Color-coded brackets</td></tr>
  </tbody>
</table>

<div class="note-box">
  <strong>📝 Tip:</strong> Enable format-on-save in VS Code settings (<code>editor.formatOnSave: true</code>) so your Dart code is automatically formatted using <code>dart format</code> every time you save.
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. Which extension provides Dart support in VS Code?</strong></p>
  <ul><li>A) Dart Code ✅</li><li>B) Dart Plus</li><li>C) DartIDE</li><li>D) Dart Studio</li></ul>
  <p><strong>2. What command creates a new Dart project?</strong></p>
  <ul><li>A) dart init</li><li>B) dart new</li><li>C) dart create ✅</li><li>D) dart start</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Set up Dart development in VS Code by installing the Dart extension, creating a project with <code>dart create</code>, and running it with <code>dart run</code> or <code>F5</code>. Enable format-on-save for clean code automatically.</p>
    $dart_03$,
    'Set up a professional Dart development environment in Visual Studio Code — install the Dart extension, create your first project, and run it in seconds.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,setup,vscode',
    NOW(),
    3,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'DartPad Online Editor',
    'dart-dartpad-editor',
    $dart_04$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#what-is-dartpad">What is DartPad?</a></li>
    <li><a href="#how-to-use">How to Use DartPad</a></li>
    <li><a href="#features">Features of DartPad</a></li>
    <li><a href="#example">Try It: Hello World</a></li>
    <li><a href="#limitations">Limitations</a></li>
    <li><a href="#notes">Notes</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="what-is-dartpad">What is DartPad?</h2>
<p>DartPad is a free, browser-based Dart editor provided by the Dart team at Google. It lets you write, run, and share Dart code directly in your web browser — no installation required. It is the perfect tool for learning Dart, experimenting with code snippets, and sharing examples.</p>
<p>Access DartPad at <strong>dartpad.dev</strong></p>

<h2 id="how-to-use">How to Use DartPad</h2>
<ol>
  <li>Open your browser and go to <strong>dartpad.dev</strong>.</li>
  <li>You will see a code editor on the left and output panel on the right.</li>
  <li>Write your Dart code in the editor panel.</li>
  <li>Click the <strong>Run</strong> button (▶) to execute the code.</li>
  <li>View the output in the right panel.</li>
  <li>Use the <strong>Share</strong> button to generate a link to your code.</li>
</ol>

<h2 id="features">Features of DartPad</h2>
<ul>
  <li>✅ No installation needed — runs in any modern browser</li>
  <li>✅ Syntax highlighting and code completion</li>
  <li>✅ Support for Dart and Flutter snippets</li>
  <li>✅ Dark and light theme toggle</li>
  <li>✅ Shareable links for collaboration</li>
  <li>✅ Integrated documentation panel</li>
  <li>✅ Always uses the latest stable Dart SDK</li>
</ul>

<h2 id="example">Try It: Hello World</h2>
<p>Open DartPad and enter this code:</p>
<pre><code>void main() {
  print('Hello from DartPad!');

  // Try changing the message below
  String name = 'Dart Learner';
  print('Welcome, $name!');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Hello from DartPad!
Welcome, Dart Learner!</pre>
</div>
<p>Click Run to see the output instantly. Try modifying the <code>name</code> variable to your own name.</p>

<h2 id="limitations">Limitations</h2>
<ul>
  <li>Cannot access the file system or network (for security)</li>
  <li>No package imports beyond core Dart libraries and a few Flutter packages</li>
  <li>Not suitable for full application development (use VS Code for that)</li>
  <li>Requires internet connection</li>
</ul>

<div class="note-box">
  <strong>📝 Note:</strong> Throughout this tutorial series, every lesson includes a "Try It Yourself" section. Open DartPad and practice the examples directly in your browser for the best learning experience.
</div>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Open DartPad at dartpad.dev.</li>
    <li>Write a program that prints your name, age, and favourite programming language.</li>
    <li>Share your code using the Share button and copy the URL.</li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What is the URL for DartPad?</strong></p>
  <ul><li>A) dart.run</li><li>B) dartpad.dev ✅</li><li>C) play.dart.dev</li><li>D) dart.google.com</li></ul>
  <p><strong>2. Which of these is NOT a feature of DartPad?</strong></p>
  <ul><li>A) Code completion</li><li>B) File system access ✅</li><li>C) Shareable links</li><li>D) Syntax highlighting</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>DartPad is a free browser-based Dart editor at dartpad.dev. Use it to write and run Dart code instantly without any installation. It is perfect for learning and sharing code snippets, though it has limitations for full application development.</p>
    $dart_04$,
    'Discover DartPad — the free, browser-based Dart editor. Learn how to write, run, and share Dart code online without installing anything.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,setup,dartpad',
    NOW(),
    4,
    'PUBLISHED',
    'ACT',
    'system'
);

INSERT INTO public.articles (id, title, slug, content, excerpt, type, author_name, tags, published_at, sort_order, workflow_status, status, created_by) VALUES (
    gen_random_uuid(),
    'Hello World in Dart',
    'dart-hello-world',
    $dart_05$
<div class="toc-box">
  <h3>📚 Table of Contents</h3>
  <ol>
    <li><a href="#first-program">Your First Dart Program</a></li>
    <li><a href="#anatomy">Anatomy of the Program</a></li>
    <li><a href="#run">How to Run</a></li>
    <li><a href="#variations">Variations</a></li>
    <li><a href="#common-mistakes">Common Mistakes</a></li>
    <li><a href="#exercise">Exercise</a></li>
    <li><a href="#quiz">Quiz</a></li>
    <li><a href="#summary">Summary</a></li>
  </ol>
</div>

<h2 id="first-program">Your First Dart Program</h2>
<p>Every programming journey begins with "Hello, World!" — a simple program that displays a message on the screen. In Dart it looks like this:</p>
<pre><code>void main() {
  print('Hello, World!');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Hello, World!</pre>
</div>

<h2 id="anatomy">Anatomy of the Program</h2>
<table>
  <thead><tr><th>Part</th><th>Meaning</th></tr></thead>
  <tbody>
    <tr><td><code>void</code></td><td>The return type of the function — <code>void</code> means the function returns nothing.</td></tr>
    <tr><td><code>main()</code></td><td>The entry point of every Dart program. Execution starts here.</td></tr>
    <tr><td><code>{ }</code></td><td>Curly braces define the body (block) of the function.</td></tr>
    <tr><td><code>print()</code></td><td>A built-in function that outputs text to the console.</td></tr>
    <tr><td><code>'Hello, World!'</code></td><td>A string literal — text enclosed in single or double quotes.</td></tr>
    <tr><td><code>;</code></td><td>Semicolon marks the end of a statement in Dart.</td></tr>
  </tbody>
</table>

<h2 id="run">How to Run</h2>
<p><strong>Option 1 — DartPad:</strong> Paste the code into dartpad.dev and click Run.</p>
<p><strong>Option 2 — Terminal:</strong></p>
<pre><code># Save as hello.dart, then:
dart run hello.dart</code></pre>
<p><strong>Option 3 — VS Code:</strong> Open the project folder, then press <code>F5</code>.</p>

<h2 id="variations">Variations</h2>
<p>You can print multiple lines and use string interpolation:</p>
<pre><code>void main() {
  print('Hello, World!');
  print('Welcome to Dart Programming!');

  String name = 'Alice';
  int year = 2024;
  print('Hello, $name! It is $year.');
}</code></pre>
<div class="note-box">
  <strong>Output:</strong>
  <pre>Hello, World!
Welcome to Dart Programming!
Hello, Alice! It is 2024.</pre>
</div>

<h2 id="common-mistakes">⚠️ Common Mistakes</h2>
<ul>
  <li><strong>Missing semicolon:</strong> <code>print('Hello')</code> without <code>;</code> causes a syntax error.</li>
  <li><strong>Wrong function name:</strong> The entry point must be exactly <code>main()</code>, not <code>Main()</code> or <code>MAIN()</code>.</li>
  <li><strong>Missing quotes:</strong> <code>print(Hello)</code> tries to find a variable named <code>Hello</code> — wrap text in quotes.</li>
  <li><strong>Mismatched braces:</strong> Every <code>{</code> must have a matching <code>}</code>.</li>
</ul>

<div class="exercise-box">
  <h3>💪 Exercise</h3>
  <ol>
    <li>Write a Dart program that prints your full name.</li>
    <li>Print your name, city, and favourite food on separate lines.</li>
    <li>Use string interpolation to print: <em>"My name is [name] and I am [age] years old."</em></li>
  </ol>
</div>

<h2 id="quiz">🧠 Quiz</h2>
<div class="quiz-box">
  <p><strong>1. What is the entry point of a Dart program?</strong></p>
  <ul><li>A) start()</li><li>B) run()</li><li>C) main() ✅</li><li>D) begin()</li></ul>
  <p><strong>2. Which function prints output to the console in Dart?</strong></p>
  <ul><li>A) console.log()</li><li>B) System.out.println()</li><li>C) print() ✅</li><li>D) echo()</li></ul>
  <p><strong>3. What does <code>void</code> mean in <code>void main()</code>?</strong></p>
  <ul><li>A) The function is empty</li><li>B) The function returns nothing ✅</li><li>C) The function is optional</li><li>D) The function is private</li></ul>
</div>

<h2 id="summary">Summary</h2>
<p>Every Dart program starts with the <code>main()</code> function. Use <code>print()</code> to display output. Statements end with a semicolon. You can run Dart programs using DartPad, the terminal (<code>dart run</code>), or VS Code (<code>F5</code>).</p>
    $dart_05$,
    'Write your first Dart program — Hello World. Learn the anatomy of a Dart program, how to run it, and the most common beginner mistakes to avoid.',
    'TUTORIAL',
    'Dart Team',
    'dart,programming,beginner,hello-world',
    NOW(),
    5,
    'PUBLISHED',
    'ACT',
    'system'
);
