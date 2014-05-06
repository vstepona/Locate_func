import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.event.*;        //for action events
import java.awt.font.TextAttribute;
import java.awt.image.ColorModel;
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
import java.util.Map;


public class My_Editor extends JFrame {
	protected static final String buttonString = "JButton";
	private JLabel lblFile = new JLabel("");
	private JTextPane textPane = new JTextPane();
	private JList<File> list = new JList<File>();
	private List<File> fList = new ArrayList<File>();
	private LocateFunc dataRecords = new LocateFunc();

	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					My_Editor frame = new My_Editor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// EventQueue.invokeLater(r);
		});
	}


	// Create the frame.
	public My_Editor() {
		//JFrame frame = new JFrame();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 991, 597);
		getContentPane().setLayout(null);


		//-----Button to perform parsing and highlight function calls------
		JButton btnNewButton = new JButton("Get func. calls");

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				System.out.println("get library list: ");

				//get libraries list
				//String [] libs = loc_func.getLibraries();
				String [] libs = new String[fList.size()];
				//convert from File list to String Array
				int j = 0;
				for (File file_iter : fList){
					libs[j] = file_iter.toString();
					j++;
				}

				//print files to be parsed 
				for(int i = 0; i < libs.length; i++){
					System.out.println("file " + libs[i]);
				}

				//---------------------- parse libraries ----------------------------
				System.out.println("parse libaries: ");
				dataRecords.parseFiles(libs);

				//wait for the threads to finish
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// print errors if could not parse files
					e.printStackTrace();
				}

				System.out.println("Get functions calls list in the open file: ");

				//show parser results 
				dataRecords.dumpFunctionTable();
				dataRecords.dumpLineTable();
				
				//------------- Highlight function calls ------------------
				System.out.println("Highlit func calls ");

				String fname = lblFile.getText().replace("File: ","");

				List<LocateFunc.Position> pos =	dataRecords.callsLocations(fname);
				
				

				for(LocateFunc.Position rec: pos){
					//--------------------------------------- set style ------------------------------------------------
					StyleContext sc = StyleContext.getDefaultStyleContext();
					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, sc);

					aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
					aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
					aset = sc.addAttribute(aset, StyleConstants.Underline, true);
					aset = sc.addAttribute(aset, StyleConstants.ColorConstants.Foreground, Color.RED);
					aset = sc.addAttribute(aset, StyleConstants.Bold, true);


					//select section of text
					textPane.select(rec.begin, rec.end); 
					System.out.println("rec.begin, rec.end);" + rec.begin + "  " + rec.end);

					//change selected text style
					textPane.setCharacterAttributes(aset, false);

				}
				//release pos memory
				pos = null;

			}
		});


		btnNewButton.setBounds(18, 524, 180, 23);
		getContentPane().add(btnNewButton);
		btnNewButton.setFocusPainted(false);  //remove focus

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(372, 80, 572, 413);
		getContentPane().add(scrollPane);
		textPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					//get mouse position character
					Point pt = new Point(e.getX(), e.getY());
					int pos = textPane.viewToModel(pt);

					//get start and end of selected word
					int fun_start =  textPane.getSelectionStart();
					int fun_end = textPane.getSelectionEnd();

					System.out.println("mose clicked on character position in the file " + pos);
					System.out.println("mose clicked on word in the file start " + fun_start);
					System.out.println("mose clicked on word in the file end " + fun_end);
//
//					StyleContext sc = StyleContext.getDefaultStyleContext();
//					AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, sc);
//
//					aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
//					aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
//					aset = sc.addAttribute(aset, StyleConstants.Underline, true);
//					aset = sc.addAttribute(aset, StyleConstants.ColorConstants.Foreground, Color.RED);
//					aset = sc.addAttribute(aset, StyleConstants.Bold, true);
//
					//select section of text
					textPane.select(fun_start, fun_end); 
					//change selection style
//					textPane.setCharacterAttributes(aset, false);

					// get function file path
					LocateFunc.Definition def = dataRecords.callDefinitionLocation(textPane.getSelectedText());



					if ("NOTFOUND".equalsIgnoreCase(def.fileName) )
						System.out.println("Function not found");
					else {
						File file = new File(def.fileName); 
						Open_File(file);
						working_label(file);
					}

				}


			}
		});
		scrollPane.setViewportView(textPane);
		scrollPane.setBorder(new LineBorder(Color.gray));

		StyledDocument doc = textPane.getStyledDocument();

		JButton btnOpen = new JButton("Open new");
		btnOpen.setBounds(239, 46, 123, 23);
		getContentPane().add(btnOpen);
		btnOpen.setFocusPainted(false);  //remove focus


		//save as button in main editor
		JButton btnSave = new JButton("Save As");
		btnSave.setBounds(855, 46, 89, 23);
		getContentPane().add(btnSave);
		btnSave.setFocusPainted(false);  //remove focus

		//save button in main editor
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//create window for choosing file from file system
				JFileChooser saveFile = new JFileChooser();

				saveFile.showSaveDialog(null);

				//get file which we will save to
				File file = saveFile.getSelectedFile();

				Save_File(file);

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

				//label shows currently open file
				working_label(file);


				//Since list can only take array we have to convert arrayList to File array
				File[] file_list = fList.toArray(new File[0]);

				//display file list in the editor
				list.setListData(file_list);

				//get file position in the list
				int index = -1;
				for (File file_iter : fList){
					if (file_iter.equals(file_list))
						break;
					index++;
				}

				//set focus on recently opened file
				if (index != -1)
					list.setSelectedIndex(index);
				
			}

		});
		//file options label
		lblFile.setBounds(372, 55, 46, 14);
		getContentPane().add(lblFile);


		//		Border emptyBorder = BorderFactory.createEmptyBorder();
		//		scrollPane.setBorder(emptyBorder);

		list.setBorder(new LineBorder(Color.gray));

		//when click on file list field
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				//get list of files
				JList<File> list = (JList)evt.getSource();

				//when double clicked
				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());

					//save current file before switching to another one
					//Save_File(list.getSelectedValue());

					//set label to show current file open
					lblFile.setText("File: " + list.getSelectedValue());


					//switch to double clicked file
					Open_File(list.getSelectedValue());

					working_label(list.getSelectedValue());

				} else if (evt.getClickCount() == 3) {   // Triple-click
					int index = list.locationToIndex(evt.getPoint());
					System.out.println("triple click");

				}
			}
		});


		//list object properties
		list.setBounds(18, 80, 344, 413);
		//add list to content pane in main editor
		getContentPane().add(list);

		JLabel lblOpenFiles = new JLabel("Open Files:");
		lblOpenFiles.setBounds(18, 51, 89, 23);
		getContentPane().add(lblOpenFiles);



		//Save button implementation
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//save file
				Save_File(list.getSelectedValue());
			}
		});



		button.setFocusPainted(false);
		button.setBounds(759, 46, 89, 23);
		getContentPane().add(button);

		JButton btnNewButton_1 = new JButton("Test button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, sc);

				aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
				aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
				aset = sc.addAttribute(aset, StyleConstants.Underline, true);
				aset = sc.addAttribute(aset, StyleConstants.ColorConstants.Foreground, Color.RED);
				aset = sc.addAttribute(aset, StyleConstants.Bold, true);


				//select section of text
				textPane.select(15, 20);
				//change selection style
				textPane.setCharacterAttributes(aset, false);

				//	textPane.getSelectedText();
				//textPane.replaceSelection("!!!!!!!!!!!!!!!!!!!!!!");


			}
		});
		btnNewButton_1.setBounds(372, 524, 89, 23);
		getContentPane().add(btnNewButton_1);

		//print welcome message
		textPane.setText("Hello,\n\n"

						+ "thank you for chosing this editor.\n\n" +
						"Instructions:\n" +
						"\t1. Open files that you will be using for your project\n" +
						"\t2. Click \"Get func. calls\"\n" +
						"\t3. Now you can edit your files. Do not forget to save the progress when\n" +
						"\t   swithching between them!\n" +
						"\t4. Double clicking on function call will take you to file that it is defined\n" +
				"\t5. You can save a copy of a file by chosing \"Save As\"\n");
	}


	//Open file and display it in the editor  
	public void Open_File(File file) {

		String everything = null;

		//read file contents in to the string "everything"
		BufferedReader br = null;

		//try to open file
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file to read!");
			JOptionPane.showMessageDialog(null, "Could not open file to read!");
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
			e.printStackTrace();
		} 

		//if file was opened; close it
		finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//put string to text editor
		textPane.setText(everything);
	}


	//Save file  
	public void Save_File(File file) {

		//file pointer
		PrintWriter writer = null;
		try {
			//pointer to file we want to save(in UTF-8)
			writer = new PrintWriter(file, "UTF-8");

			//get text from currently open file
			String our_file = textPane.getText();

			//write to file
			writer.print(our_file);

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			//	e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File not found!");
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "Something went wrong in save_file function!");
			//e.printStackTrace();
		}
		//if did not open file there is no need to close it
		finally{
			if (writer != null)
				writer.close();
		}
	}


	//set currently working on file label
	private void working_label(File file){
		//display file that currently working
		String working_on = "File: " + file.getAbsolutePath();
		int width = working_on.length()*20;
		lblFile.setBounds(372, 55, width, 14);
		lblFile.setText(working_on);
	}
}
