import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.event.*;

public class SwingLayout {

    // ### Instance variable ### //
    private JFrame frame;
    private JTextArea textArea;
    private JLabel lbInfo;
    private File currentFile; // to store the file path 
    private JPanel pnSounth;
    private JToolBar toolBar;
    private JLabel lbCursorPos;
    private UndoManager undoMng = new UndoManager();
    private JPanel topPanel;
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
        //frame.getContentPane().add(mnHauptmenue, "North");
        frame.setJMenuBar(mnHauptmenue);

        // Tool Bar
        toolBar = new JToolBar();
        placeToolBar(toolBar);
        frame.getContentPane().add(toolBar, BorderLayout.PAGE_START);

        // Top Panel
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(800, 40)); 
        frame.add(topPanel, BorderLayout.NORTH);
        
        // South Panel
        pnSounth = new JPanel();
        pnSounth.setPreferredSize(new Dimension(800,20));
        frame.getContentPane().add(pnSounth, "South");


        // Text Area
        textArea = new JTextArea(10,10);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
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
        textArea.getDocument().addUndoableEditListener(undoMng); // add the undo manager

        // Scroll bar
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane, "Center");

        // Function calls
        placeMenuElements(mnHauptmenue); //textArea, frame
        placeSouthPanelElements(pnSounth); // textArea, frame
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
        JButton btnNew = new JButton("Neu");
        btnNew.addActionListener(e -> newFile());
        toolBar.add(btnNew);

        //JButton btnOpen = new JButton(new JButton(new ImageIcon("icons/open.png")));
        JButton btnOpen = new JButton("Öffen");
        btnOpen.addActionListener(e -> openFile());
        toolBar.add(btnOpen);

        JButton btnSave = new JButton("Speichern");
        btnSave.addActionListener(e -> saveFile());
        toolBar.add(btnSave);

        JButton btnSaveAs = new JButton("Speichern unter");
        btnSaveAs.addActionListener(e -> saveFileAs());
        toolBar.add(btnSaveAs);

        JButton btnUndo = new JButton("Rückgängig");
        btnUndo.addActionListener(e -> {
            if(undoMng.canUndo()){
                undoMng.undo();
            }
        });
        toolBar.add(btnUndo);

        JButton btnRedo = new JButton("Wiederherstellen");
        btnRedo.addActionListener(e -> {
            if(undoMng.canRedo()){
                undoMng.redo();
            }
        });
        toolBar.add(btnRedo);

    }

    private void placeSouthPanelElements(JPanel panel) {
        // JTextArea textArea, JFrame frame
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

    private void openFile(){
        //JTextArea textArea,JFrame frame

        // Choose the file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Datei öffnen");
        int returnValue = fileChooser.showOpenDialog(frame);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            // JFileChooser.APPROVE_OPTION - click OK
            File selectedFile = fileChooser.getSelectedFile();

            // show the file path
            String path = selectedFile.getAbsolutePath();
            lbInfo.setText(path);
            currentFile = fileChooser.getSelectedFile();

            // Read the file and show it in the JTextArea
            try(BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Fehler beim Lesen der Datei: " + e.getMessage());
                // TODO: handle exception
            }
        }

    }

    private void saveFileAs() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showSaveDialog(frame); // เปิดหน้าต่างเลือกที่บันทึก
    if (result == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try (FileWriter writer = new FileWriter(fileToSave)) {
            textArea.write(writer); // บันทึกเนื้อหา JTextArea
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

    private void newFile() {
    int choice = JOptionPane.showConfirmDialog(
        frame,
        "Möchten Sie die aktuelle Datei schließen?",
        "Schließen bestätigen",
        JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
        textArea.setText("");      // ลบเนื้อหา
        currentFile = null;        // ไม่มีไฟล์ปัจจุบัน
        lbInfo.setText("Info");
    }
}


    // ### Main ### //
    public static void main(String[] args) {
        new SwingLayout(); // create the instance (here to show GUI) 
    }    


}