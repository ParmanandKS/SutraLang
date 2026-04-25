# SutraLang IDE - Complete Android Application

## 📱 Overview

SutraLang IDE is a revolutionary Hinglish programming language IDE for Android. It allows developers to write code in Hinglish (a blend of Hindi and English) with an intuitive syntax while maintaining the power of high-performance compilers.

---

## 📥 Download & Try
**Get the latest version of SutraLang IDE directly on your phone:**

[**⬇️ Download SutraLang.apk**](https://github.com/ParmanandKS/SutraLang/releases/download/v1.0.0/SutraLang.apk)  
*(Note: If you are downloading for the first time, you may need to allow "Install from Unknown Sources" in your Android settings.)*

---

## 🖼️ App Preview
Curious about how it looks? Check out our visual tour:  
👉 [**View Screenshots & Features (PREVIEW.md)**](PREVIEW.md)

---

## 🎯 Project Structure

```
com.sutralang.ide/
├── activities/          # All UI Activities
│   ├── SplashActivity.java
│   ├── AuthActivity.java
│   ├── MainActivity.java
│   ├── FileManagerActivity.java
│   └── AboutActivity.java
├── engine/              # SutraLang Interpreter
│   └── SutraEngine.java
├── utils/               # Utility Classes
│   ├── AuthManager.java
│   └── FileUtils.java
├── adapters/            # RecyclerView Adapters
│   └── SutraFileAdapter.java
└── models/              # Data Models
    └── SutraFile.java

resources/
├── layout/              # XML Layouts
├── drawable/            # UI Elements (backgrounds, buttons)
├── values/              # Colors, strings, themes
├── menu/                # Navigation menu
└── font/                # Custom fonts
```

---

## ⚙️ Key Features Implemented

### 1. **SutraEngine** - The Interpreter
Core interpreter supporting Hinglish syntax:

#### Supported Statements:
- **Variables**: `rakho x = 10;`
- **Print Output**: `dikhao x;` or `dikhao "Hello";`
- **Conditionals**: `agar (x > 5) { dikhao "True"; }`
- **While Loops**: `jabtak (i < 10) { dikhao i; }`
- **For Loops**: `kahandar i = 0 tak 10 { dikhao i; }`

#### Operators Supported:
- Arithmetic: `+`, `-`, `*`, `/`
- Comparison: `>`, `<`, `==`, `!=`
- Variable assignment and lookup

### 2. **Authentication** - Google Sign-In
- Uses Google Play Services
- Stores login state in SharedPreferences
- User data: ID, Name, Email
- Options to skip login (limited features)

### 3. **Code Editor** - MainActivity
- EditText for code entry
- Real-time syntax highlighting (via fonts)
- Output TextView for execution results
- Navigation drawer with code templates
- Save/Run buttons

#### Navigation Drawer Menu:
- 📝 Generate Variable
- 🔀 Generate If Statement
- 🔄 Generate While Loop
- 🔁 Generate For Loop
- 📂 My Files
- ℹ️ About
- 🚪 Logout

### 4. **File Management**
- Save .sutra files (requires login)
- Load existing files
- Delete files
- Rename files
- List all saved files

### 5. **UI Design**
- Glassmorphism design with transparency and blur simulation
- Dark theme with cyan, blue accents
- Responsive layouts
- Material Design components

### 6. **Activities Implemented**

#### SplashActivity
- Shows app branding
- 2-second display
- Routes based on login status

#### AuthActivity
- Google Sign-In button
- Skip button for guest access
- Privacy policy display

#### MainActivity (Editor)
- Code editor with monospace font
- Output display
- Run/Save buttons
- Navigation drawer with templates
- Syntax error handling

#### FileManagerActivity
- RecyclerView of saved files
- Click to open
- Long-press to delete/rename
- Context menu options

#### AboutActivity
- App information
- Feature description
- Supported syntax
- Developer info

---

## 📦 Dependencies

```gradle
// Core Android
androidx.appcompat:appcompat:1.6.1
androidx.constraintlayout:constraintlayout:2.1.4
androidx.drawerlayout:drawerlayout:1.2.0
androidx.recyclerview:recyclerview:1.3.1

// Material Design
com.google.android.material:material:1.10.0

// Google Sign-In
com.google.android.gms:play-services-auth:20.7.0
com.google.android.gms:play-services-base:18.2.0
```

---

## 🔐 Authentication Flow

```
App Launch
    ↓
SplashActivity
    ↓
Check SharedPreferences (isLoggedIn)
    ↓
    ├─→ TRUE → MainActivity
    └─→ FALSE → AuthActivity
            ↓
            ├─→ Google Sign-In → Save user data → MainActivity
            └─→ Skip → MainActivity (save disabled)
```

---

## 💾 File Storage

- **Location**: Internal Storage (`/data/data/com.sutralang.ide/files/SutraFiles/`)
- **Format**: `.sutra` files
- **Content**: Plain text code
- **Operations**: Create, Read, Update, Delete, List, Rename

### File Operations:
```java
FileUtils.saveFile(context, "program", code);      // Save
FileUtils.readFile(context, "program");             // Load
FileUtils.listFiles(context);                       // List all
FileUtils.deleteFile(context, "program");           // Delete
FileUtils.renameFile(context, oldName, newName);    // Rename
```

---

## 🎨 UI Components

### Color Palette
- **Primary**: #81ECFF (Cyan)
- **Secondary**: #10D5FF (Light Cyan)
- **Tertiary**: #70AAFF (Light Blue)
- **Background**: #060E20 (Dark Navy)
- **Surface**: Glassmorphic containers

### Glassmorphism Effects
- Transparent backgrounds with borders
- Blur simulation via shape layers
- Semi-transparent overlays
- Border radius: 8-12dp

### Typography
- **Headlines**: Space Grotesk Bold
- **Body**: Manrope Regular
- **Code**: JetBrains Mono

---

## 🧪 Error Handling

### SutraEngine Error Handling:
- Invalid variable declarations
- Unknown statements
- Division by zero
- Incorrect syntax
- Maximum loop iterations (prevents infinite loops)

### Activity Error Handling:
- Login required for saving
- File not found exceptions
- Invalid filename handling
- Toast messages for user feedback

---

## 🚀 Usage Examples

### Example 1: Simple Variable and Print
```
rakho name = "Student";
dikhao name;
```
Output: `Student`

### Example 2: Arithmetic
```
rakho x = 10;
rakho y = 20;
rakho sum = x + y;
dikhao sum;
```
Output: `30`

### Example 3: Conditional
```
rakho age = 25;
agar (age > 18) {
    dikhao "Adult";
}
```
Output: `Adult`

### Example 4: Loop
```
kahandar i = 0 tak 5 {
    dikhao i;
}
```
Output:
```
0
1
2
3
4
```

### Example 5: While Loop
```
rakho i = 0;
jabtak (i < 3) {
    dikhao i;
}
```

---

## 🔧 Setup Instructions

### Prerequisites:
- Android Studio Arctic Fox or later
- JDK 11+
- Android SDK 34+
- Google Play Services configured

### Steps:
1. Clone the project
2. Open in Android Studio
3. Sync Gradle dependencies
4. Configure Google Play Services:
   - Add your sha1 fingerprint in Firebase Console
   - Download google-services.json
   - Place in `app/` directory
5. Build using `./gradlew :app:assembleDebug`
6. Run on device using `./gradlew :app:installDebug` or the IDE Run button

### Google Sign-In Setup:
```
1. Go to Firebase Console
2. Create new project
3. Add Google Sign-In authentication
4. Download google-services.json
5. Place in app directory
6. Add SHA-1 fingerprint (get via ./gradlew signingReport)
```

---

## 📝 Code Comments

All code includes comprehensive comments explaining:
- Class purpose and responsibilities
- Method functionality and parameters
- Important implementation details
- Hinglish syntax explanations

---

## ✨ Features Highlights

✅ **Clean Architecture**: Separated concerns with packages for activities, engine, utils  
✅ **Production-Ready**: Error handling, null checks, resource management  
✅ **User-Friendly**: Intuitive UI with glassmorphism design  
✅ **Secure**: SharedPreferences for auth, internal storage for files  
✅ **Extensible**: Easy to add new language features  
✅ **Well-Documented**: Comments and README for maintainability  

---

## 🐛 Known Limitations

1. Basic parsing - doesn't handle complex nested structures
2. Integer/Float support only
3. Single scope (no local scopes)
4. Limited loop depth checking
5. No function definitions yet

---

## 🚀 Future Enhancements

- [ ] User-defined functions
- [ ] Local variable scopes
- [ ] More data types (arrays, strings)
- [ ] Cloud sync via Firebase
- [ ] Code syntax highlighting
- [ ] Debugger implementation
- [ ] Package/library system
- [ ] Multi-file projects

---

## 👨‍💻 Developer

**Project**: SutraLang IDE  
**Version**: 1.0.0 BETA
**Type**: Diploma Major Project  
**Lead**: ParmanandKS  

---

## 📄 License

This is an academic/diploma project. All code is provided for educational purposes.

---

## 🙏 Acknowledgments

- Material Design Components
- Google Play Services
- Android Architecture Components
- Community contributions

---

## 📞 Support

For issues, questions, or contributions:
1. Check the code comments
2. Review error messages
3. Consult Android documentation
4. Test with sample code on devices

---

**Happy Coding in Hinglish! 🚀**
#SutraLang