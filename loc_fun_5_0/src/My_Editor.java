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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import loc_fun_5_0.loc_func;

import java.awt.ScrollPane;




import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//import org.apache.commons.io.output.ByteArrayOutputStream;







public class My_Editor extends JFrame {
	private String newline = "\n";
	protected static final String buttonString = "JButton";
	private static final Font ITALIC = null;
	private JTextPane textPane = new JTextPane();
	private JList list = new JList();
	private List<File> fList = new ArrayList<File>();






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
		setBounds(100, 100, 991, 597);
		getContentPane().setLayout(null);


		//-----Button to perform parsing and highlight function calls------
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




				System.out.println("Highlit func calls ");






			}
		});







		btnNewButton.setBounds(10, 524, 180, 23);
		getContentPane().add(btnNewButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(372, 15, 572, 478);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(textPane);

		StyledDocument doc = textPane.getStyledDocument();

		JButton btnOpen = new JButton("Open");
		btnOpen.setBounds(384, 500, 89, 23);
		getContentPane().add(btnOpen);



		//save button in main editor
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(493, 500, 89, 23);
		getContentPane().add(btnSave);

		//save button in main editor
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//create window for choosing file from file system
				JFileChooser saveFile = new JFileChooser();

				saveFile.showSaveDialog(null);

				//get file which we will save to
				File file = saveFile.getSelectedFile();

				//file pointer
				PrintWriter writer = null;
				try {
					//pointer to file we want to save(in UTF-8)
					writer = new PrintWriter(file, "UTF-8");

					//get text from currently open file
					String our_file = textPane.getText();

					//write to file
					writer.print(our_file);

					//	writer.println("The second line");

				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					//	e.printStackTrace();
				} catch (Exception e){
					//e.printStackTrace();
				}
				//if did not open file there is no need to close it
				finally{
					if (writer != null)
						writer.close();
				}
			}
		});


		//open button in main editor
		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser openFile = new JFileChooser();
				openFile.showOpenDialog(null);

				String everything = null;

				//read file contents in to the string "everything"
				BufferedReader br = null;

				//get file which we will save to
				File file = openFile.getSelectedFile();

				try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();

					while (line != null) {
						sb.append(line);
						sb.append(System.lineSeparator());
						line = br.readLine();
					}

					//full file contents in the string
					everything = sb.toString();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				//put string to text editor
				textPane.setText(everything);

				System.out.println("before" + fList.size());

				fList.add(file);				 

				System.out.println("after add" + fList.size());
			}
		});



		//file options label
		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(328, 504, 46, 14);
		getContentPane().add(lblFile);



		//when click on file list field
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				//File directory = new File("");

				//get all the files from a directory
				//fList = directory.listFiles();

				//for (File file : fList){

				for (File file_iter : fList){
					System.out.println("file: " + file_iter.getName());
				}

				//Since list can only take array we have to convert arrayList to File array
				File[] file_list = fList.toArray(new File[0]);
				
				//display file list
				list.setListData(file_list);
				
				
				
				
				//	System.out.println(file.getName());
				//}
			}
		});


		//list object properties
		list.setBounds(10, 15, 344, 478);
		//add list ot content pane in main editor
		getContentPane().add(list);



		//initial data in editor
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
