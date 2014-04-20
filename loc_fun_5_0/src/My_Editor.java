import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import java.awt.event.*;        //for action events
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import loc_fun_5_0.loc_func;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class My_Editor extends JFrame {
	private String newline = "\n";
	protected static final String buttonString = "JButton";
	//private static final Font ITALIC = null;
	private JTextPane textPane = new JTextPane();
	private JList<File> list = new JList<File>();
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


				//get file which we will save to
				File file = openFile.getSelectedFile();

				//Check if file is already open
				Boolean not_open = true; 
				for (File file_iter : fList){
					if (file_iter.equals(file))
						not_open = false;
				}



				if (not_open){
					fList.add(file);
				}

				//read in file
				Open_File(file);


				//Since list can only take array we have to convert arrayList to File array
				File[] file_list = fList.toArray(new File[0]);

				//display file list in the editor
				list.setListData(file_list);

			}

		});





		//file options label
		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(328, 504, 46, 14);
		getContentPane().add(lblFile);



		//when click on file list field
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {

				JList<File> list = (JList)evt.getSource();

				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					System.out.println("double click");
					System.out.println("index: " + index);
					System.out.println("selected: " + list.getSelectedValue());
					
					Open_File(list.getSelectedValue());
					
				} else if (evt.getClickCount() == 3) {   // Triple-click
					int index = list.locationToIndex(evt.getPoint());
					System.out.println("triple click");

				}
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

	}



	public void Open_File(File file) {
		
		String everything = null;

		//read file contents in to the string "everything"
		BufferedReader br = null;

		//try to open file
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not open file to read!");
			e.printStackTrace();
		}


		//try to read file
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
		} 

		//if file was opened; close it
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



		//put string to text editor
		textPane.setText(everything);
		
		//return everything;



	}



}
