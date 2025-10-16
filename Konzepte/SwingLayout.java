import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

public class SwingLayout {

    // ### Instance variable ### //
    private JFrame frame;
    // private JTextArea textArea;
    private JTextPane textArea;
    private StyledDocument doc;
    private JLabel lbInfo;
    private File currentFile; // to store the file path 
    private JPanel pnSouth;
    private JToolBar toolBar;
    private JLabel lbCursorPos;
    private UndoManager undoMng = new UndoManager();
    private JPanel topPanel;
    private JComboBox<String> fontCombo;
    private JComboBox<Integer> sizeCombo;
    private JButton colorBtn;
    private boolean isUpdatingStyle = false;

    /* 
    * These instances are private, can be accessed within this class only
    * When we create a new element within this class
    * We write only -> frame = new JFrame("Designbeispiel");
    * Instead of -> JFrame frame = new JFrame("Designbeispiel");
    */

    // ### Constructor ### //
    // Constructor has the same name as Class, no return 
    public SwingLayout() {
        // Frame
        frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        
        // Menu Bar
        JMenuBar mnHauptmenue = new JMenuBar();
        frame.setJMenuBar(mnHauptmenue);

        // Tool Bar
        toolBar = new JToolBar();
        placeToolBar(toolBar);
        frame.getContentPane().add(toolBar, BorderLayout.PAGE_START);

        // Top Panel
        //topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //topPanel.setPreferredSize(new Dimension(800, 45));
        //topPanel.add(toolBar);
        //frame.add(topPanel, BorderLayout.NORTH);
        
        // South Panel
        pnSouth = new JPanel();
        pnSouth.setPreferredSize(new Dimension(800,20));
        frame.getContentPane().add(pnSouth, "South");


        // Text Area
        textArea = new JTextPane();
        /*
        // textArea.setLineWrap(true);
        // textArea.setWrapStyleWord(true);
        textArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int caretPos = textArea.getCaretPosition();
                try {
                    int row = textArea.getLineOfOffset(caretPos); //current position start from 0
                    int col = caretPos - textArea.getLineStartOffset(row); //คำนวณจากระยะห่างระหว่าง caret กับต้นบรรทัด
                    lbCursorPos.setText("Cursorposition Zeile " + (row + 1) + ", Spalte " + (col + 1));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
        //textArea.getDocument().addUndoableEditListener(undoMng); // add the undo manager
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoMng.addEdit(e.getEdit());
            }
        });
         */
        // Replace Text Area with Text Pane

        updateStyleControls(); 
        doc = textArea.getStyledDocument();
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoMng.addEdit(e.getEdit());
            }
        });

        textArea.addCaretListener(e -> updateStyleControls());

        // Ctrl+Z for Undo
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"),"Undo");
        textArea.getActionMap().put("Undo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (undoMng.canUndo()){
                    undoMng.undo();
                }
            }
        });
        // Ctrl+Y for Redo
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"),"Redo");
        textArea.getActionMap().put("Redo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (undoMng.canRedo()){
                    undoMng.redo();
                }
            }
        });
        
        
        // Scroll bar
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane, "Center");

        // Function calls
        placeMenuElements(mnHauptmenue); //textArea, frame
        placeSouthPanelElements(pnSouth); // textArea, frame
        frame.setVisible(true);
    }

    // ### Methods ### //
    private void placeMenuElements(JMenuBar menu){

        // *** Menu Datei *** 
        JMenu mnDatei = new JMenu("Datei");
        mnDatei.setMnemonic('D');
        menu.add(mnDatei); // refer to mnHauptMenu

        // New file
        JMenuItem mnNew = new JMenuItem("Neu");
        mnNew.setMnemonic('N');
        mnNew.addActionListener(e -> newFile());
        mnDatei.add(mnNew);

        // Open
        JMenuItem mnOpen = new JMenuItem("Öffen");
        mnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        mnOpen.setMnemonic('Ö');
        mnDatei.add(mnOpen); 
        
        // Save
        JMenuItem mnSave = new JMenuItem("Speichen");
        mnSave.setMnemonic('S');
        mnSave.addActionListener(e -> saveFile());
        mnDatei.add(mnSave); 

        // Save as 
        JMenuItem mnSaveAs = new JMenuItem("Speichen unter"); 
        mnSaveAs.addActionListener(e -> saveFileAs());
        mnDatei.add(mnSaveAs);
        
        mnDatei.addSeparator(); // add a seperator

        // End programme
        JMenuItem mnEnd = new JMenuItem("Beenden");
        mnEnd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mnEnd.setMnemonic('B');  
        mnDatei.add(mnEnd);

        // *** Menu Bearbeiten ***
        JMenu mnEdit = new JMenu("Bearbeiten");
        mnEdit.setMnemonic('B'); 
        menu.add(mnEdit);

        // Select All
        JMenuItem mnSelectAll = new JMenuItem("Alles auswählen");
        mnSelectAll.addActionListener(e -> textArea.selectAll());
        mnEdit.add(mnSelectAll); 

        // Copy
        JMenuItem mnCopy = new JMenuItem("Kopieren");
        mnCopy.addActionListener(e -> textArea.copy()); 
        mnEdit.add(mnCopy);
        
        // Cut
        JMenuItem mnCut = new JMenuItem("Ausschneiden");
        mnCut.addActionListener(e -> textArea.cut()); 
        mnEdit.add(mnCut);

        // Paste
        JMenuItem mnPaste = new JMenuItem("Einfügen");
        mnPaste.addActionListener(e -> textArea.paste()); 
        mnEdit.add(mnPaste);

        mnEdit.addSeparator(); // add a seperator

        // Undo - need the Undomanager
        JMenuItem mnUndo = new JMenuItem("Rückgängig");
        mnUndo.addActionListener(e -> {
            if (undoMng.canUndo()){
                undoMng.undo();
            }
        }); 
        mnEdit.add(mnUndo);

        // Redo - need the Undomanager
        JMenuItem mnRedo = new JMenuItem("Wiederherstellen");
        mnRedo.addActionListener(e -> {
            if (undoMng.canRedo()){
                undoMng.redo();
            }
        }); 
        mnEdit.add(mnRedo);
         

    
    }

    private void placeToolBar(JToolBar toolBar){

        // Icons creations
        ImageIcon newIcon = new ImageIcon("images/new.png"); // create an ImageIcon
        // Image newIcon2 = newIcon.getImage().getScaledInstance(20,20, Image.SCALE_SMOOTH);
        // ImageIcon newIconResized = new ImageIcon(newIcon2);
        ImageIcon openIcon = new ImageIcon("images/open.png"); // create an ImageIcon
        ImageIcon saveIcon = new ImageIcon("images/save.png"); // create an ImageIcon
        ImageIcon saveAsIcon = new ImageIcon("images/save_as.png"); // create an ImageIcon
        ImageIcon undoIcon = new ImageIcon("images/undo.png"); // create an ImageIcon
        ImageIcon redoIcon = new ImageIcon("images/redo.png"); // create an ImageIcon

        // New
        JButton btnNew = new JButton("");
        btnNew.addActionListener(e -> newFile());
        btnNew.setIcon(newIcon);
        toolBar.add(btnNew);

        // Open
        // JButton btnOpen = new JButton(new JButton(new ImageIcon("icons/open.png")));
        JButton btnOpen = new JButton("");
        btnOpen.addActionListener(e -> openFile());
        btnOpen.setIcon(openIcon);
        toolBar.add(btnOpen);

        // Save
        JButton btnSave = new JButton("");
        btnSave.addActionListener(e -> saveFile());
        btnSave.setIcon(saveIcon);
        toolBar.add(btnSave);

        // SaveAs
        JButton btnSaveAs = new JButton("");
        btnSaveAs.addActionListener(e -> saveFileAs());
        btnSaveAs.setIcon(saveAsIcon);
        toolBar.add(btnSaveAs);

        // Undo
        JButton btnUndo = new JButton("");
        btnUndo.addActionListener(e -> {
            if(undoMng.canUndo()){
                undoMng.undo();
            }
        });
        btnUndo.setIcon(undoIcon);
        toolBar.add(btnUndo);

        // Redo
        JButton btnRedo = new JButton("");
        btnRedo.addActionListener(e -> {
            if(undoMng.canRedo()){
                undoMng.redo();
            }
        });
        btnRedo.setIcon(redoIcon);
        toolBar.add(btnRedo);

        // Bold
        JButton boldBtn = new JButton("B");
        boldBtn.setFont(new Font("Arial", Font.BOLD, 20));
        boldBtn.addActionListener(e -> toggleStyle(StyleConstants.Bold));
        toolBar.add(boldBtn);

        // Italic
        JButton italicBtn = new JButton("I");
        italicBtn.setFont(new Font("Arial", Font.BOLD, 20));
        italicBtn.addActionListener(e -> toggleStyle(StyleConstants.Italic));
        toolBar.add(italicBtn);

        // Undeline
        JButton underlineBtn = new JButton("U");
        underlineBtn.setFont(new Font("Arial", Font.BOLD, 20));
        underlineBtn.addActionListener(e -> toggleStyle(StyleConstants.Underline));
        toolBar.add(underlineBtn);

        // Font Family
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontCombo = new JComboBox<>(fonts);

        fontCombo.addActionListener(e -> {
            // ✅ ถ้ากำลังอัปเดตอยู่ ให้ข้าม
            if (isUpdatingStyle) return;

            String fontName = (String) fontCombo.getSelectedItem();
            int start = textArea.getSelectionStart();
            int end = textArea.getSelectionEnd();
            if (start == end) return;

            StyledDocument doc = textArea.getStyledDocument();
            SimpleAttributeSet style = new SimpleAttributeSet();
            StyleConstants.setFontFamily(style, fontName);
            doc.setCharacterAttributes(start, end - start, style, false);

        });
        toolBar.add(fontCombo);

        // Font Sizes
        Integer[] sizes = {8, 10, 12, 14, 16, 18, 20, 24, 28, 32, 36};
        sizeCombo = new JComboBox<>(sizes);

        sizeCombo.addActionListener(e -> {
            // ✅ ถ้ากำลังอัปเดตอยู่ ให้ข้าม
            if (isUpdatingStyle) return;

            int fontSize = (Integer) sizeCombo.getSelectedItem();
            int start = textArea.getSelectionStart();
            int end = textArea.getSelectionEnd();
            if (start == end) return; // ✅ ต้องมี selection ถึงจะเปลี่ยน

            StyledDocument doc = textArea.getStyledDocument();
            SimpleAttributeSet style = new SimpleAttributeSet();
            StyleConstants.setFontSize(style, fontSize);
            doc.setCharacterAttributes(start, end - start, style, false);
    
        });
        toolBar.add(sizeCombo);

        // Color Chooser
        colorBtn = new JButton();
        colorBtn.setForeground(Color.BLACK); // เริ่มต้นให้เป็นสีดำ
        colorBtn.setOpaque(true);
        colorBtn.setBorderPainted(true);
        colorBtn.setPreferredSize(new Dimension(35, 35)); // ขนาดปุ่มเล็กพอดีมือ
        colorBtn.setMaximumSize(new Dimension(35, 35));
        colorBtn.setMinimumSize(new Dimension(35, 35));
        colorBtn.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        colorBtn.addActionListener(e -> {
        Color color = JColorChooser.showDialog(frame, "Choose Text Color", Color.BLACK);
        if (color != null) {
                StyledDocument doc = textArea.getStyledDocument();
                SimpleAttributeSet style = new SimpleAttributeSet();
                StyleConstants.setForeground(style, color);
                doc.setCharacterAttributes(textArea.getSelectionStart(),
                                           textArea.getSelectionEnd() - textArea.getSelectionStart(),
                                           style, false);
            colorBtn.setBackground(color);
            }
        });
        toolBar.add(colorBtn);

    }

    private void resetTextStyle() {
        if (textArea == null) return;

        // 1. create a new StyledDocument and reassign it to the doc
        // This will NOT carry styles and undo history to the new document
        StyledDocument newDoc = new DefaultStyledDocument();
        textArea.setDocument(newDoc);
        doc = newDoc;

        // 2. create default style
        SimpleAttributeSet defaultStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(defaultStyle, "Arial");
        StyleConstants.setFontSize(defaultStyle, 12);
        StyleConstants.setBackground(defaultStyle, Color.BLACK); //text
        StyleConstants.setBold(defaultStyle, false);
        StyleConstants.setItalic(defaultStyle, false);
        StyleConstants.setUnderline(defaultStyle, false);

        // 3. apply default styles to the new document
        doc = textArea.getStyledDocument();
        doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
        
        // 4. Reset GUI Element
        // reset JComboBox
        if (fontCombo != null) fontCombo.setSelectedItem("Arial");
        if (sizeCombo != null) sizeCombo.setSelectedItem(12);
        if (colorBtn != null) colorBtn.setBackground(Color.BLACK);

        // 5. Reset undo manager history, to prevent error after reset
        if (undoMng != null) {
            undoMng.discardAllEdits();
        }

}

    private void toggleStyle(Object styleType) {
        StyledDocument doc = textArea.getStyledDocument();
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        if (start == end) return; // Early exit when nothing is selected

        Element element = doc.getCharacterElement(start);
        AttributeSet as = element.getAttributes();

        boolean isSet = false;
        if (styleType.equals(StyleConstants.Bold)) {
            isSet = StyleConstants.isBold(as);
        } else if (styleType.equals(StyleConstants.Italic)) {
            isSet = StyleConstants.isItalic(as);
        } else if (styleType.equals(StyleConstants.Underline)) {
            isSet = StyleConstants.isUnderline(as);
        }

        SimpleAttributeSet sas = new SimpleAttributeSet();
        if (styleType.equals(StyleConstants.Bold)) {
            StyleConstants.setBold(sas, !isSet);
        } else if (styleType.equals(StyleConstants.Italic)) {
            StyleConstants.setItalic(sas, !isSet);
        } else if (styleType.equals(StyleConstants.Underline)) {
            StyleConstants.setUnderline(sas, !isSet);
    }

    doc.setCharacterAttributes(start, end - start, sas, false);
}

    private void placeSouthPanelElements(JPanel panel) {
        panel.setLayout(new BorderLayout());
        
        lbInfo = new JLabel("Info");
        lbInfo.setBounds(0,0,300,20);

        lbCursorPos = new JLabel("Curserposition:");
        lbCursorPos.setBounds(700,0,100,20);

        // 🖱️ Mouse position listener for entire frame
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                lbCursorPos.setText("Cursorposition: X=" + e.getX() + ", Y=" + e.getY());
            }
        });

        // 🖱️ Also track inside textArea (important)
        textArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // ต้องบวก offset เพราะตำแหน่งของ textArea ไม่เริ่มที่ (0,0)
                Point p = SwingUtilities.convertPoint(textArea, e.getPoint(), frame.getContentPane());
                lbCursorPos.setText("Cursorposition: X=" + p.x + ", Y=" + p.y);
            }
        });

        panel.add(lbInfo, BorderLayout.WEST);
        panel.add(lbCursorPos, BorderLayout.EAST);

    }

    private void saveFile() {
        if (currentFile != null) {
            saveRTFFile(currentFile);
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            currentFile = fileToSave; // ตั้งไฟล์ปัจจุบัน
            saveRTFFile(fileToSave);
        }
    }

    private void saveRTFFile(File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            RTFEditorKit rtfKit = new RTFEditorKit();
            rtfKit.write(fos, textArea.getStyledDocument(), 0, textArea.getStyledDocument().getLength());
            JOptionPane.showMessageDialog(frame, "Datei erfolgreich gespeichert!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Fehler beim Speichern: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Datei öffnen");
        int returnValue = fileChooser.showOpenDialog(frame);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            lbInfo.setText(selectedFile.getAbsolutePath());
            currentFile = selectedFile;

            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                RTFEditorKit rtfKit = new RTFEditorKit();
                StyledDocument doc = new DefaultStyledDocument();
                rtfKit.read(fis, doc, 0);
                textArea.setDocument(doc);

                // ใส่ UndoManager ใหม่ให้กับ doc
                doc.addUndoableEditListener(new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent e) {
                        undoMng.addEdit(e.getEdit());
                    }
                });
                
                // // เพิ่ม caret listener เพื่อ update ComboBox
                // textArea.addCaretListener(e -> updateStyleControls());
                updateStyleControls(); 
                
                

            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Fehler beim Lesen der Datei: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /* inside here works with JTextArea but not with JTextPane 
    private void openFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Datei öffnen");
    int returnValue = fileChooser.showOpenDialog(frame);
    if(returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        lbInfo.setText(selectedFile.getAbsolutePath());
        currentFile = selectedFile;

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // ลบเนื้อหาเก่า
            textArea.setText("");


            // ใส่ข้อความใหม่พร้อม style
            doc = textArea.getStyledDocument();
            try {
                doc.insertString(0, content.toString(), null);

                // // **สำคัญ**: รีเซ็ต style ของข้อความทั้งหมดให้ตรง default
                // doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);

            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            // // **เรียกฟังก์ชัน resetTextStyle()**
            // resetTextStyle();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Fehler beim Lesen der Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

    private void saveFileAs() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showSaveDialog(frame); // เปิดหน้าต่างเลือกที่บันทึก
    if (result == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try (FileWriter writer = new FileWriter(fileToSave)) {
            textArea.write(writer); // บันทึกเนื้อหา
            JOptionPane.showMessageDialog(frame, "Datei erfolgreich gespeichert!"); // แจ้งสำเร็จ
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Fehler beim Speichern: " + e.getMessage());
        }
    }
}

    private void saveFile() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Fehler beim Speichern: " + e.getMessage());
            }
        } else {
            saveFileAs(); // ถ้ายังไม่มีไฟล์ ให้ใช้ Save As
        }
    }
    */

    private void newFile() {
        int choice = JOptionPane.showConfirmDialog(
            frame,
            "Möchten Sie die aktuelle Datei schließen?",
            "Schließen bestätigen",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            textArea.setText(""); // empty the text area
            currentFile = null;     // empty the current file path
            lbInfo.setText("Neue Datei"); // reset the file path label

            resetTextStyle();
            
        }
    }

    private void updateStyleControls() {

        // ✅ บอกว่ากำลังอัปเดต
    isUpdatingStyle = true;

        if (textArea.getDocument().getLength() == 0) {
        // ถ้า document ว่าง ให้ใช้ค่า default
        if (fontCombo != null) fontCombo.setSelectedItem("Arial");
        if (sizeCombo != null) sizeCombo.setSelectedItem(12);
        if (colorBtn != null) colorBtn.setBackground(Color.BLACK);
        isUpdatingStyle = false; // ✅ อย่าลืมปิด flag
        return; // ✅ จบเลย ไม่ต้องอ่านจาก document
    }
        
        int pos = textArea.getCaretPosition();

        StyledDocument doc = textArea.getStyledDocument();
        Element element;
        if (pos > 0) {
            element = doc.getCharacterElement(pos - 1);
        } else {
            element = doc.getCharacterElement(pos);
        }

        AttributeSet as = element.getAttributes();

        // อัปเดต Font
        String font = StyleConstants.getFontFamily(as);
        if (fontCombo != null && !font.equals(fontCombo.getSelectedItem())) {
            fontCombo.setSelectedItem(font);
        }

        // อัปเดต Size
        int size = StyleConstants.getFontSize(as);
        if (sizeCombo != null && size != (Integer) sizeCombo.getSelectedItem()) {
            sizeCombo.setSelectedItem(size);
        }

        // สามารถอัปเดตสีได้ด้วยถ้ามีปุ่มเลือกสี
        Color color = StyleConstants.getForeground(as);
        colorBtn.setBackground(color);
        // ✅ เสร็จแล้วปิด flag
         isUpdatingStyle = false;
    }

    // ### Main ### //
    public static void main(String[] args) {
        new SwingLayout(); // create the instance (here to show GUI) 
    }    



}