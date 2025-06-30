import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

public class WindowsCalculator extends JFrame implements ActionListener, KeyListener {
    // Display components
    private JTextField display;
    private JLabel historyLabel;
    private JTextArea historyArea;
    private JScrollPane historyScrollPane;
    
    // Calculator state
    private double num1 = 0, num2 = 0, result = 0;
    private String operator = "";
    private boolean operatorPressed = false;
    private boolean newCalculation = true;
    private boolean scientificMode = false;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##########");
    
    // Memory operations
    private double memory = 0;
    private JLabel memoryIndicator;
    
    // History
    private ArrayList<String> history = new ArrayList<>();
    private boolean historyVisible = false;
    
    // UI Components
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel displayPanel;
    private JPanel topPanel;
    private JMenuBar menuBar;
    
    // Color scheme (Windows Calculator style)
    private final Color BACKGROUND_COLOR = new Color(243, 243, 243);
    private final Color BUTTON_NUMBER_COLOR = new Color(249, 249, 249);
    private final Color BUTTON_OPERATOR_COLOR = new Color(240, 240, 240);
    private final Color BUTTON_EQUALS_COLOR = new Color(0, 120, 215);
    private final Color BUTTON_FUNCTION_COLOR = new Color(240, 240, 240);
    private final Color BUTTON_HOVER_COLOR = new Color(229, 229, 229);
    private final Color TEXT_COLOR = new Color(0, 0, 0);
    private final Color DISPLAY_COLOR = new Color(255, 255, 255);

    public WindowsCalculator() {
        initializeGUI();
        addKeyListener(this);
        setFocusable(true);
    }

    private void initializeGUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(350, 500));
        
        // Set Windows look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        createMenuBar();
        createDisplayPanel();
        createButtonPanel();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        
        JMenuItem standardItem = new JMenuItem("Standard");
        standardItem.addActionListener(e -> switchToStandard());
        
        JMenuItem scientificItem = new JMenuItem("Scientific");
        scientificItem.addActionListener(e -> switchToScientific());
        
        JMenuItem historyItem = new JMenuItem("History");
        historyItem.addActionListener(e -> toggleHistory());
        
        viewMenu.add(standardItem);
        viewMenu.add(scientificItem);
        viewMenu.addSeparator();
        viewMenu.add(historyItem);
        
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }
    
    private void createDisplayPanel() {
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(DISPLAY_COLOR);
        displayPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        // Memory indicator
        memoryIndicator = new JLabel("");
        memoryIndicator.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memoryIndicator.setForeground(Color.GRAY);
        memoryIndicator.setHorizontalAlignment(SwingConstants.LEFT);
        
        // History label (shows current operation)
        historyLabel = new JLabel(" ");
        historyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyLabel.setForeground(Color.GRAY);
        historyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Main display
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 48));
        display.setBackground(DISPLAY_COLOR);
        display.setBorder(BorderFactory.createEmptyBorder());
        display.setForeground(TEXT_COLOR);
        
        // Top panel for memory and history
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(DISPLAY_COLOR);
        topPanel.add(memoryIndicator, BorderLayout.WEST);
        topPanel.add(historyLabel, BorderLayout.EAST);
        
        displayPanel.add(topPanel, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.CENTER);
    }
    
    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 2, 2));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        createStandardButtons();
    }
    
    private void createStandardButtons() {
        buttonPanel.removeAll();
        
        String[] buttonLabels;
        
        if (scientificMode) {
            buttonPanel.setLayout(new GridLayout(8, 6, 2, 2));
            buttonLabels = new String[] {
                "(", ")", "MC", "MR", "M+", "M-",
                "2nd", "π", "e", "C", "⌫", "÷",
                "x²", "1/x", "|x|", "7", "8", "9", "×", "√",
                "xʸ", "log", "ln", "4", "5", "6", "-", "n!",
                "sin", "cos", "tan", "1", "2", "3", "+", "1/x",
                "EXP", "mod", "±", "0", ".", "=", "=", "="
            };
        } else {
            buttonPanel.setLayout(new GridLayout(6, 4, 2, 2));
            buttonLabels = new String[] {
                "MC", "MR", "M+", "M-",
                "C", "CE", "⌫", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "±", "0", ".", "="
            };
        }
        
        for (String label : buttonLabels) {
            JButton button = createStyledButton(label);
            buttonPanel.add(button);
        }
        
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
    
    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.addActionListener(this);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(173, 173, 173), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Button styling based on type
        if (label.matches("[0-9]")) {
            styleNumberButton(button);
        } else if (label.equals("=")) {
            styleEqualsButton(button);
        } else if (label.matches("[+\\-×÷]")) {
            styleOperatorButton(button);
        } else if (label.matches("M[CR+\\-]")) {
            styleMemoryButton(button);
        } else {
            styleFunctionButton(button);
        }
        
        return button;
    }
    
    private void styleNumberButton(JButton button) {
        button.setBackground(BUTTON_NUMBER_COLOR);
        button.setForeground(TEXT_COLOR);
        addHoverEffect(button, BUTTON_NUMBER_COLOR);
    }
    
    private void styleOperatorButton(JButton button) {
        button.setBackground(BUTTON_OPERATOR_COLOR);
        button.setForeground(TEXT_COLOR);
        addHoverEffect(button, BUTTON_OPERATOR_COLOR);
    }
    
    private void styleEqualsButton(JButton button) {
        button.setBackground(BUTTON_EQUALS_COLOR);
        button.setForeground(Color.WHITE);
        addHoverEffect(button, BUTTON_EQUALS_COLOR);
    }
    
    private void styleFunctionButton(JButton button) {
        button.setBackground(BUTTON_FUNCTION_COLOR);
        button.setForeground(TEXT_COLOR);
        addHoverEffect(button, BUTTON_FUNCTION_COLOR);
    }
    
    private void styleMemoryButton(JButton button) {
        button.setBackground(BUTTON_FUNCTION_COLOR);
        button.setForeground(TEXT_COLOR);
        addHoverEffect(button, BUTTON_FUNCTION_COLOR);
    }
    
    private void addHoverEffect(JButton button, Color originalColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (originalColor.equals(BUTTON_EQUALS_COLOR)) {
                    button.setBackground(new Color(0, 100, 180));
                } else {
                    button.setBackground(BUTTON_HOVER_COLOR);
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }
    
    private void layoutComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(displayPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private void switchToStandard() {
        scientificMode = false;
        createStandardButtons();
        setSize(400, 600);
        setTitle("Calculator");
    }
    
    private void switchToScientific() {
        scientificMode = true;
        createStandardButtons();
        setSize(600, 700);
        setTitle("Calculator - Scientific");
    }
    
    private void toggleHistory() {
        historyVisible = !historyVisible;
        
        if (historyVisible) {
            if (historyArea == null) {
                createHistoryPanel();
            }
            mainPanel.add(historyScrollPane, BorderLayout.EAST);
            setSize(getWidth() + 250, getHeight());
        } else {
            if (historyScrollPane != null) {
                mainPanel.remove(historyScrollPane);
                setSize(getWidth() - 250, getHeight());
            }
        }
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void createHistoryPanel() {
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyArea.setBackground(BACKGROUND_COLOR);
        historyArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        historyScrollPane = new JScrollPane(historyArea);
        historyScrollPane.setPreferredSize(new Dimension(250, 0));
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("History"));
        
        updateHistoryDisplay();
    }
    
    private void addToHistory(String calculation) {
        history.add(calculation);
        updateHistoryDisplay();
    }
    
    private void updateHistoryDisplay() {
        if (historyArea != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = history.size() - 1; i >= 0; i--) {
                sb.append(history.get(i)).append("\n\n");
            }
            historyArea.setText(sb.toString());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        try {
            if (command.matches("[0-9]")) {
                handleNumber(command);
            } else if (command.equals(".")) {
                handleDecimal();
            } else if (command.matches("[+\\-×÷]")) {
                handleOperator(command);
            } else if (command.equals("=")) {
                handleEquals();
            } else if (command.equals("C")) {
                handleClear();
            } else if (command.equals("CE")) {
                handleClearEntry();
            } else if (command.equals("⌫")) {
                handleBackspace();
            } else if (command.equals("±")) {
                handlePlusMinus();
            } else if (command.equals("√")) {
                handleSquareRoot();
            } else if (command.equals("x²")) {
                handleSquare();
            } else if (command.equals("1/x")) {
                handleReciprocal();
            } else if (command.startsWith("M")) {
                handleMemory(command);
            } else if (scientificMode) {
                handleScientificFunction(command);
            }
        } catch (Exception ex) {
            display.setText("Error");
            resetCalculator();
        }
    }
    
    private void handleNumber(String number) {
        if (newCalculation || operatorPressed || display.getText().equals("0")) {
            display.setText(number);
            newCalculation = false;
            operatorPressed = false;
        } else {
            String currentText = display.getText();
            if (currentText.length() < 15) { // Limit display length
                display.setText(currentText + number);
            }
        }
    }
    
    private void handleDecimal() {
        if (newCalculation || operatorPressed) {
            display.setText("0.");
            newCalculation = false;
            operatorPressed = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }
    
    private void handleOperator(String op) {
        if (!operator.isEmpty() && !operatorPressed) {
            handleEquals();
        }
        
        num1 = Double.parseDouble(display.getText());
        operator = op;
        operatorPressed = true;
        newCalculation = false;
        
        historyLabel.setText(decimalFormat.format(num1) + " " + op);
    }
    
    private void handleEquals() {
        if (operator.isEmpty() || newCalculation) return;
        
        num2 = Double.parseDouble(display.getText());
        String calculation = decimalFormat.format(num1) + " " + operator + " " + decimalFormat.format(num2) + " =";
        
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "×":
                result = num1 * num2;
                break;
            case "÷":
                if (num2 == 0) {
                    display.setText("Cannot divide by zero");
                    historyLabel.setText("");
                    resetCalculator();
                    return;
                }
                result = num1 / num2;
                break;
        }
        
        String resultText = decimalFormat.format(result);
        display.setText(resultText);
        
        addToHistory(calculation + " " + resultText);
        historyLabel.setText("");
        
        num1 = result;
        operator = "";
        newCalculation = true;
    }
    
    private void handleClear() {
        resetCalculator();
        display.setText("0");
        historyLabel.setText("");
    }
    
    private void handleClearEntry() {
        display.setText("0");
        operatorPressed = false;
    }
    
    private void handleBackspace() {
        String text = display.getText();
        if (text.length() > 1 && !text.equals("0")) {
            display.setText(text.substring(0, text.length() - 1));
        } else {
            display.setText("0");
        }
    }
    
    private void handlePlusMinus() {
        double value = Double.parseDouble(display.getText());
        value = -value;
        display.setText(decimalFormat.format(value));
    }
    
    private void handleSquareRoot() {
        double value = Double.parseDouble(display.getText());
        if (value < 0) {
            display.setText("Invalid input");
            return;
        }
        result = Math.sqrt(value);
        display.setText(decimalFormat.format(result));
        addToHistory("√(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
        newCalculation = true;
    }
    
    private void handleSquare() {
        double value = Double.parseDouble(display.getText());
        result = value * value;
        display.setText(decimalFormat.format(result));
        addToHistory("sqr(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
        newCalculation = true;
    }
    
    private void handleReciprocal() {
        double value = Double.parseDouble(display.getText());
        if (value == 0) {
            display.setText("Cannot divide by zero");
            return;
        }
        result = 1 / value;
        display.setText(decimalFormat.format(result));
        addToHistory("1/(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
        newCalculation = true;
    }
    
    private void handleMemory(String command) {
        double value = Double.parseDouble(display.getText());
        
        switch (command) {
            case "MC":
                memory = 0;
                memoryIndicator.setText("");
                break;
            case "MR":
                display.setText(decimalFormat.format(memory));
                newCalculation = true;
                break;
            case "M+":
                memory += value;
                memoryIndicator.setText("M");
                break;
            case "M-":
                memory -= value;
                memoryIndicator.setText("M");
                break;
        }
    }
    
    private void handleScientificFunction(String function) {
        double value = Double.parseDouble(display.getText());
        
        switch (function) {
            case "sin":
                result = Math.sin(Math.toRadians(value));
                display.setText(decimalFormat.format(result));
                addToHistory("sin(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
                break;
            case "cos":
                result = Math.cos(Math.toRadians(value));
                display.setText(decimalFormat.format(result));
                addToHistory("cos(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
                break;
            case "tan":
                result = Math.tan(Math.toRadians(value));
                display.setText(decimalFormat.format(result));
                addToHistory("tan(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
                break;
            case "log":
                if (value <= 0) {
                    display.setText("Invalid input");
                    return;
                }
                result = Math.log10(value);
                display.setText(decimalFormat.format(result));
                addToHistory("log(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
                break;
            case "ln":
                if (value <= 0) {
                    display.setText("Invalid input");
                    return;
                }
                result = Math.log(value);
                display.setText(decimalFormat.format(result));
                addToHistory("ln(" + decimalFormat.format(value) + ") = " + decimalFormat.format(result));
                break;
            case "π":
                display.setText(decimalFormat.format(Math.PI));
                newCalculation = true;
                break;
            case "e":
                display.setText(decimalFormat.format(Math.E));
                newCalculation = true;
                break;
        }
        
        if (!function.equals("π") && !function.equals("e")) {
            newCalculation = true;
        }
    }
    
    private void resetCalculator() {
        num1 = 0;
        num2 = 0;
        result = 0;
        operator = "";
        operatorPressed = false;
        newCalculation = true;
    }
    
    // Keyboard support
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (Character.isDigit(c)) {
            handleNumber(String.valueOf(c));
        } else if (c == '.') {
            handleDecimal();
        } else if (c == '+') {
            handleOperator("+");
        } else if (c == '-') {
            handleOperator("-");
        } else if (c == '*') {
            handleOperator("×");
        } else if (c == '/') {
            handleOperator("÷");
        } else if (c == '=' || c == '\n') {
            handleEquals();
        } else if (c == 'c' || c == 'C') {
            handleClear();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            handleBackspace();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            handleClear();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowsCalculator();
        });
    }
}