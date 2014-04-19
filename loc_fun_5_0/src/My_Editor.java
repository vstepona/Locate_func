import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.*;        //for action events
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import loc_fun_5_0.loc_func;

public class My_Editor extends JFrame {
	private String newline = "\n";
	protected static final String buttonString = "JButton";
	private static final Font ITALIC = null;
	private JTextPane textPane = new JTextPane();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					My_Editor frame = new My_Editor();
					frame.setVisible(true);
					///	new My_Editor().createUI();


				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// EventQueue.invokeLater(r);
		});
	}

	/**
	 * Create the frame.
	 */
	public My_Editor() {
		JFrame frame = new JFrame();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 608, 597);
		getContentPane().setLayout(null);


		//Button to perform parsing and highlight function calls
		JButton btnNewButton = new JButton("get func. calls");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("cet library list: ");

				//get libraries list
				String [] libs = loc_func.getLibraries();



				System.out.println("parse libaries: ");

				loc_func.parseFiles(libs);

				//wait for the threads to finish
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Call parser on me: ");



				//String[] files = {f1,

				System.out.println("Highlit func calls ");




			}
		});







		btnNewButton.setBounds(10, 524, 180, 23);
		getContentPane().add(btnNewButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 572, 478);
		getContentPane().add(scrollPane);

		//JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

		JButton btnOpen = new JButton("Open");
		btnOpen.setBounds(384, 500, 89, 23);
		getContentPane().add(btnOpen);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(493, 500, 89, 23);
		getContentPane().add(btnSave);



		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser saveFile = new JFileChooser();
				saveFile.showSaveDialog(null);
				File file = saveFile.getSelectedFile();


				PrintWriter writer = null;
				try {
					writer = new PrintWriter(file, "UTF-8");
					
					String our_file = textPane.getText();
					writer.print(our_file);
					
					writer.println("The second line");

				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					writer.close();
				}
			}
		});

		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser();
				openFile.showOpenDialog(null);
				
				
				
				
				
				
				
				

			}
		});




		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(328, 504, 46, 14);
		getContentPane().add(lblFile);




		String[] initString =
			{ "This is an editable JTextPane, ",            //regular
				"another ",                                   //italic
				"styled ",                                    //bold
				"text ",                                      //small
				"component, ",                                //large
				"which supports embedded components..." + newline,//regular
				" " + newline,                                //button
				"...and embedded icons..." + newline,         //regular
				" ",                                          //icon
				newline + "JTextPane is a subclass of JEditorPane that " +
				"uses a StyledEditorKit and StyledDocument, and provides " +
				"cover methods for interacting with those objects."
			};

		String[] initStyles =
			{ "regular", "italic", "bold", "small", "large",
				"regular", "button", "regular", "icon",
				"regular"
			};

		StyledDocument doc = textPane.getStyledDocument();
		//  addStylesToDocument(doc);

		//insert string
		try {
			for (int i=0; i < initString.length; i++) {
				doc.insertString(doc.getLength(), initString[i],
						doc.getStyle(initStyles[i]));
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}

		// return textPane;
	}




}
