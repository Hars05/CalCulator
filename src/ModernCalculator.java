import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernCalculator extends JFrame implements ActionListener {
    private JTextField display;
    private double num1 = 0, num2 = 0, result = 0;
    private char operator;
    private boolean startNewNumber = true;

    public ModernCalculator() {
        // Frame setup
        setTitle("Calculator");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Colors
        Color background = new Color(18, 18, 18);
        Color numBtnColor = new Color(45, 45, 45);
        Color opBtnColor = new Color(0, 122, 255);
        Color specialBtnColor = new Color(200, 60, 60);
        Color textColor = Color.WHITE;

        // Display
        display = new JTextField("0");
        display.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(30, 30, 30));
        display.setForeground(textColor);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(display, BorderLayout.NORTH);

        // Buttons layout
        JPanel panel = new JPanel(new GridLayout(5, 4, 12, 12));
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        String[] buttonLabels = {
            "C", "←", "÷", "×",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "±", "0", ".", ""
        };

        for (String label : buttonLabels) {
            if (label.isEmpty()) {
                panel.add(new JLabel());
                continue;
            }
            JButton button = new JButton(label);
            button.setFont(new Font("Poppins", Font.BOLD, 22));
            button.setFocusPainted(false);
            button.setForeground(textColor);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Button colors
            if (label.equals("C")) {
                button.setBackground(specialBtnColor);
            } else if (label.equals("←") || label.equals("±")) {
                button.setBackground(new Color(90, 90, 90));
            } else if (label.matches("[÷×\\-+=]")) {
                button.setBackground(opBtnColor);
            } else {
                button.setBackground(numBtnColor);
            }

            // Rounded button style
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    JButton b = (JButton) c;
                    int arc = 25;

                    g2.setColor(b.getBackground());
                    g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), arc, arc);

                    FontMetrics fm = g2.getFontMetrics();
                    int textX = (b.getWidth() - fm.stringWidth(b.getText())) / 2;
                    int textY = (b.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setColor(b.getForeground());
                    g2.drawString(b.getText(), textX, textY);

                    g2.dispose();
                }
            });

            // Hover effects
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(button.getBackground().brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (label.equals("C")) button.setBackground(specialBtnColor);
                    else if (label.equals("←") || label.equals("±")) button.setBackground(new Color(90, 90, 90));
                    else if (label.matches("[÷×\\-+=]")) button.setBackground(opBtnColor);
                    else button.setBackground(numBtnColor);
                }
            });

            button.addActionListener(this);
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);
        getContentPane().setBackground(background);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            switch (command) {
                case "C":
                    display.setText("0");
                    num1 = num2 = result = 0;
                    operator = '\0';
                    startNewNumber = true;
                    break;

                case "←":
                    String text = display.getText();
                    if (text.length() > 1) {
                        display.setText(text.substring(0, text.length() - 1));
                    } else {
                        display.setText("0");
                    }
                    break;

                case "±":
                    double val = Double.parseDouble(display.getText());
                    display.setText(removeTrailingZeros(-val));
                    break;

                case "+":
                case "-":
                case "×":
                case "÷":
                    num1 = Double.parseDouble(display.getText());
                    operator = command.charAt(0);
                    startNewNumber = true;
                    break;

                case "=":
                    num2 = Double.parseDouble(display.getText());
                    switch (operator) {
                        case '+': result = num1 + num2; break;
                        case '-': result = num1 - num2; break;
                        case '×': result = num1 * num2; break;
                        case '÷':
                            if (num2 == 0) {
                                display.setText("Error");
                                return;
                            }
                            result = num1 / num2;
                            break;
                        default: return;
                    }
                    display.setText(removeTrailingZeros(result));
                    num1 = result;
                    startNewNumber = true;
                    break;

                case ".":
                    if (!display.getText().contains(".")) {
                        display.setText(display.getText() + ".");
                    }
                    break;

                default: // numbers
                    if (startNewNumber || display.getText().equals("0")) {
                        display.setText(command);
                        startNewNumber = false;
                    } else {
                        display.setText(display.getText() + command);
                    }
            }
        } catch (Exception ex) {
            display.setText("Error");
        }
    }

    private String removeTrailingZeros(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%s", value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernCalculator().setVisible(true));
    }
}
