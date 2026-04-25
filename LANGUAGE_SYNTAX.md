# SutraLang - Hinglish Programming Language Syntax Guide

## 📚 Quick Reference

### Syntax Overview

SutraLang is a Hinglish (Hindi + English blend) programming language designed for Indian developers. It combines Hindi keywords with familiar programming logic.

---

## 🔤 Keywords

| Keyword          | Purpose | English | Example |
|------------------|---------|---------|---------|
| `rakho`          | Declare variable | Store/Keep | `rakho x = 10;` |
| `dikhao`         | Print output | Show/Display | `dikhao x;` |
| `agar`           | Conditional | If | `agar (x > 5) { }` |
| `jabtak`         | While loop | Until | `jabtak (i < 10) { }` |
| `i=0 se 100 tak` | For loop | From/Count | `i=0 se 100 tak { }` |
| `tak`            | Range end | Till/To | Used with `kahandar` |

---

## 📝 Basic Syntax Examples

### 1. Variables (rakho)

**Syntax**:
```
rakho variable_name = value;
```

**Examples**:
```
rakho name = "Arjun";           // String
rakho age = 25;                 // Integer
rakho height = 5.9;             // Float/Double
rakho score = 95.5;             // Decimal
```

**Notes**:
- Variable names must not contain spaces
- Values must match the type
- Use quotes for strings: `"text"`
- Numbers don't need quotes

---

### 2. Print Output (dikhao)

**Syntax**:
```
dikhao variable_name;
dikhao "string";
dikhao expression;
```

**Examples**:
```
dikhao name;                    // Print variable
dikhao "Hello World";           // Print text
dikhao 100;                     // Print number
dikhao x + y;                   // Print expression
```

**Output**: Text appears in OUTPUT panel

---

### 3. Arithmetic Operations

**Supported**: `+`, `-`, `*`, `/`

```
rakho a = 10;
rakho b = 20;
rakho sum = a + b;              // Addition: 30
rakho diff = a - b;             // Subtraction: -10
rakho product = a * b;          // Multiplication: 200
rakho quotient = b / a;         // Division: 2.0
dikhao sum;                     // Prints: 30
```

**Rules**:
- Integer ÷ Integer → may give decimal
- Division by zero → ERROR
- Order of operations: `*`, `/` before `+`, `-`

---

### 4. Comparisons

**Operators**: `>`, `<`, `==`, `!=`

```
x > 5      // Greater than
x < 10     // Less than
x == 10    // Equal to
x != 5     // Not equal to
```

**Examples**:
```
agar (age > 18) { 
    dikhao "Adult";
}

agar (marks == 100) {
    dikhao "Perfect!";
}

agar (status != "active") {
    dikhao "Inactive user";
}
```

---

### 5. If Condition (agar)

**Syntax**:
```
agar (condition) {
    // code if true
}
```

**Example 1** - Simple if:
```
rakho num = 15;
agar (num > 10) {
    dikhao "Number is greater than 10";
}
```

**Example 2** - With comparison:
```
rakho score = 75;
agar (score >= 60) {
    dikhao "Pass";
}
```

**Example 3** - Nested:
```
rakho age = 25;
rakho income = 50000;
agar (age > 18) {
    agar (income > 40000) {
        dikhao "Eligible for credit card";
    }
}
```

---

### 6. While Loop (jabtak)

**Syntax**:
```
jabtak (condition) {
    // code repeats while true
}
```

**Example 1** - Count 0 to 4:
```
rakho i = 0;
jabtak (i < 5) {
    dikhao i;
}
```

**Output**:
```
0
1
2
3
4
```

**Example 2** - Sum calculation:
```
rakho sum = 0;
rakho i = 1;
jabtak (i <= 5) {
    sum = sum + i;
}
dikhao sum;    // Output: 15
```

**IMPORTANT**: Loop must update counter, else infinite!

---

### 7. For Loop (kahandar)

**Syntax**:
```
kahandar variable = start tak end {
    // code repeats
}
```

**Example 1** - Simple count:
```
kahandar i = 0 tak 5 {
    dikhao i;
}
```

**Output**:
```
0
1
2
3
4
```

**Example 2** - Print 1 to 10:
```
kahandar num = 1 tak 11 {
    dikhao num;
}
```

**Notes**:
- `i = 0 tak 5` means 0, 1, 2, 3, 4 (NOT including 5)
- Auto-increments by 1
- Cleaner than while loops

---

## 🎯 Complete Program Examples

### Program 1: Hello World
```
dikhao "Namaste Duniya!";
```

**Output**:
```
Namaste Duniya!
```

---

### Program 2: Calculator
```
rakho x = 20;
rakho y = 10;
dikhao x + y;
dikhao x - y;
dikhao x * y;
dikhao x / y;
```

**Output**:
```
30
10
200
2.0
```

---

### Program 3: Times Table
```
rakho num = 5;
kahandar i = 1 tak 11 {
    rakho result = num * i;
    dikhao result;
}
```

**Output** (5 times table):
```
5
10
15
20
25
30
35
40
45
50
```

---

### Program 4: Number Check
```
rakho age = 20;
agar (age >= 18) {
    dikhao "You are an adult";
}
agar (age < 18) {
    dikhao "You are a minor";
}
```

**Output**:
```
You are an adult
```

---

### Program 5: Even/Odd Checker + Loop
```
kahandar i = 1 tak 6 {
    rakho remainder = i / 2;
    agar (remainder == 0) {
        dikhao i;
    }
}
```

**Output** (Even numbers 1-5):
```
2
4
```

---

### Program 6: Nested Loop - Times Table Matrix
```
kahandar i = 1 tak 4 {
    kahandar j = 1 tak 4 {
        rakho product = i * j;
        dikhao product;
    }
}
```

**Output**:
```
1
2
3
1
2
3
4
5
6
2
4
6
8
...
```

---

### Program 7: Sum of Numbers
```
rakho sum = 0;
kahandar i = 1 tak 6 {
    sum = sum + i;
}
dikhao sum;
```

**Output**:
```
15
```
(Sum of 1+2+3+4+5)

---

## ⚠️ Common Mistakes

### Mistake 1: Missing Semicolon
```
❌ rakho x = 10        // ERROR
✓ rakho x = 10;        // Correct
```

### Mistake 2: Variable Not Declared
```
❌ dikhao x;           // If x not declared
✓ rakho x = 5;
  dikhao x;           // Correct
```

### Mistake 3: String Issues
```
❌ dikhao hello;       // ERROR - hello not defined
✓ dikhao "hello";     // Correct
```

### Mistake 4: Infinite Loop
```
❌ jabtak (x > 5) {
    dikhao x;         // x never changes!
}
✓ jabtak (x > 0) {
    dikhao x;
    // Need: x = x - 1;
}
```

### Mistake 5: Wrong Range
```
❌ kahandar i = 1 tak 3 { }  // Runs 1, 2 (NOT 3)
✓ kahandar i = 1 tak 4 { }  // Runs 1, 2, 3
```

---

## 💡 Tips & Best Practices

1. **Always use semicolons** - Every statement ends with `;`
2. **Meaningful names** - Use `age`, `score` not `x`, `y`
3. **Proper spacing** - Easy to read code
4. **Test small parts** - Build programs step by step
5. **Use comments** - Add notes (when supported)
6. **Check output** - Verify each step works

---

## 📊 Data Types Supported

| Type | Example | Range |
|------|---------|-------|
| Integer | `10`, `100`, `-5` | Depends on system |
| Float | `5.5`, `3.14`, `2.0` | Decimal values |
| String | `"Hello"`, `"Arjun"` | Text (in quotes) |

---

## 🎓 Learning Path

### Beginner
1. Learn variables (rakho)
2. Learn print (dikhao)
3. Learn arithmetic
4. Learn if condition

### Intermediate
5. Learn comparisons
6. Learn while loops
7. Learn for loops
8. Combine concepts

### Advanced
9. Nested loops
10. Complex conditions
11. Real-world programs
12. Optimization

---

## 🚀 Challenge Programs

### Challenge 1: Factorial
```
Calculate 5! = 5 × 4 × 3 × 2 × 1 = 120
```

### Challenge 2: Fibonacci Series
```
Print first 10 Fibonacci numbers
```

### Challenge 3: Prime Number Check
```
Check if a number is prime
```

### Challenge 4: Multiplication Table
```
Print 1-10 times table in matrix format
```

---

## 📚 Reference Card

```
VARIABLE:   rakho varName = value;
PRINT:      dikhao value;
IF:         agar (condition) { statements }
WHILE:      jabtak (condition) { statements }
FOR:        kahandar var = start tak end { statements }

OPERATORS:
  Arithmetic: +  -  *  /
  Compare:    >  <  ==  !=
  Range:      tak (in for loops)
```

---

## 🎉 Conclusion

SutraLang makes programming accessible in Hinglish! Start with simple programs and gradually build your skills.

**Happy Coding in Hinglish! 🚀**

---

For more help:
1. Check example programs in the app
2. Read error messages carefully
3. Test code line by line
4. Use the code generation templates in the app

---
