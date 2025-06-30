# Windows Calculator in Java

## Overview
This is a Java Swing-based calculator application that mimics the functionality and appearance of the Windows Calculator. It includes both standard and scientific modes, memory functions, and a calculation history feature.

## Features

### Basic Operations
- Addition, subtraction, multiplication, division
- Clear (C) and Clear Entry (CE) functions
- Backspace (⌫) functionality
- Sign change (±)
- Decimal point input

### Scientific Functions (in Scientific Mode)
- Trigonometric functions (sin, cos, tan)
- Logarithmic functions (log, ln)
- Square root (√), square (x²), reciprocal (1/x)
- Constants (π, e)
- Factorial (n!), modulus (mod)
- Power (xʸ), absolute value (|x|)
- Parentheses for expression grouping

### Memory Operations
- Memory Clear (MC)
- Memory Recall (MR)
- Memory Add (M+)
- Memory Subtract (M-)

### Additional Features
- Calculation history with scrollable view
- Keyboard support for all basic operations
- Windows-style UI with hover effects
- Responsive design with resizable window
- Decimal formatting for clean display

## How to Use

### Basic Usage
1. Launch the application
2. Use the number buttons or keyboard to input values
3. Select an operation (+, -, ×, ÷)
4. Input the second number
5. Press "=" or Enter to see the result

### Switching Modes
- Use the View menu to switch between Standard and Scientific modes
- Scientific mode provides additional mathematical functions

### Memory Functions
- Store a value in memory with M+
- Recall with MR
- Clear memory with MC
- Subtract from memory with M-

### Viewing History
- Click "History" in the View menu to show/hide the history panel
- History shows previous calculations with their results

## Keyboard Shortcuts
- 0-9: Number input
- .: Decimal point
- +, -, *, /: Basic operations
- = or Enter: Calculate result
- Backspace: Delete last digit
- Escape: Clear all
- C: Clear (same as CE button)

## Requirements
- Java Runtime Environment (JRE) 8 or later

## Known Limitations
- No complex expression evaluation (operations are performed sequentially)
- Limited error handling for edge cases
- Scientific functions work in degrees only

## License
This project is open-source and free to use. Modify and distribute as needed.
