package edu.utdallas.notepad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.myfirstapp.R;

//This class does functionality for saving file,save as and recent open documents.
//This is assignment for CS 6301.013 created by Poojan Khanpara(Net id:pdk130130),Parth Trivedi(Net id:pxt131830) and Vaishali Shah(Net id:vxs135730).

public class NotepadEdit extends Activity {

	private File Dir;
	private File settingFile;
	private boolean isOverWriteEnabled = false;
	SubMenu sub;
	final int recent_menu = 9;
	final int selectedItem_one = 10;
	final int selectedItem_two = 11;
	final int selectedItem_three = 12;
	final int selectedItem_four = 13;
	final int selectedItem_five = 14;
	public ArrayList<String> recentDocuments = new ArrayList<String>();

	//The below method call once when application is created on device.This method creates New Directory "Notepad_Folder" if it is not existed.
	//This method returns void.
	//This method is written by Poojan.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_edit);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		// Creating directory to store all the notes
		Dir = new File(Environment.getExternalStorageDirectory(),
				"Notepad_Folder");
		settingFile = new File(getFilesDir() + File.separator + "Setting.txt");

		if (!Dir.isDirectory()) {
			Dir.mkdir();
			Toast.makeText(this,R.string.initializing, Toast.LENGTH_SHORT)
					.show();
			Toast.makeText(
					this,R.string.defaultSavingLocationHint+
					"\n"+ Dir.getAbsolutePath(), Toast.LENGTH_LONG).show();

			// Create a setting file to store recent files
			try {
				settingFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//The below method inflate the menu.This adds items to the action bar if it is present.
	//This method returns boolean value.
	//This method is written by Parth.
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		
		sub = menu.addSubMenu(recent_menu, recent_menu,0, R.string.dropdown);
		sub.add(recent_menu, selectedItem_one, 2, "").setVisible(false);
		sub.add(recent_menu, selectedItem_two, 3, "").setVisible(false);
		sub.add(recent_menu, selectedItem_three, 4, "").setVisible(false);
		sub.add(recent_menu, selectedItem_four, 5, "").setVisible(false);
		sub.add(recent_menu, selectedItem_five, 6, "").setVisible(false);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//The below method is called when any item from action bar is selected.
	//This method returns boolean value.
	//This method is written by Poojan.
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, NotepadEdit.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.menu_exit:
			/* Saving the application data and exiting */
			finish();
			return true;
		case R.id.menu_save:
			EditText title = (EditText) findViewById(R.id.title);
			EditText body = (EditText) findViewById(R.id.body);
			writeFile(title.getText() + "", body.getText() + "");
			return true;
		case R.id.menu_save_as:
			saveAs();
			return true;
		case recent_menu:
			creatingRecentsMenu();
			return true;
		case selectedItem_one:
			displayText(item.getTitle());
			return true;
		case selectedItem_two:
			displayText(item.getTitle());
			return true;
		case selectedItem_three:
			displayText(item.getTitle());
			return true;
		case selectedItem_four:
			displayText(item.getTitle());
			return true;
		case selectedItem_five:
			displayText(item.getTitle());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//The below method retrieve the text from open file.
	//This method returns void value.
	//This method is written by Vaishali.
	private void displayText(CharSequence title) {
		CharSequence bodyText = readFile(title);
		EditText titleNew = (EditText) findViewById(R.id.title);
		EditText bodyNew = (EditText) findViewById(R.id.body);
		title = title.subSequence(0, title.length() - 4);
		titleNew.setText(title);
		bodyNew.setText(bodyText);
		isOverWriteEnabled = true;
	}

	//The below method read data from file.This method helps to get recent open files.
	//This method returns void value.
	//This method is written by Poojan.
	private CharSequence readFile(CharSequence title) {
		BufferedReader br = null;
		FileInputStream fstream = null;
		CharSequence bodyText = "";
		String currentLine = null;
		try {
			File file = new File(Dir.getAbsolutePath() + File.separator + title);
			if (file.exists()) {

				fstream = new FileInputStream(Dir.getAbsolutePath()
						+ File.separator + title);
				br = new BufferedReader(new InputStreamReader(fstream));

				do {
					if (currentLine != null) {
						// currentLine = br.readLine();
						bodyText = bodyText + currentLine + '\n';

					}
				} while ((currentLine = br.readLine()) != null);

			} else {
				System.out.println("File Doesn't Exists");
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (br != null)
					br.close();
				if (fstream != null)
					fstream.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return bodyText;

	}

	//The below method saves file using different name and implement save as functionality.
	//This method returns void value.
	//This method is written by Parth.
	
	private void saveAs() {
		// Display a dialog to prompt user to rename the text
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Setting the layout of the dialog
		LayoutInflater inflater = getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View dialogView = inflater.inflate(R.layout.save_as_popup, null);
		builder.setView(dialogView);
		// Adding the buttons
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked Save button
						EditText body = (EditText) findViewById(R.id.body);
						EditText title = (EditText) dialogView
								.findViewById(R.id.editTextRename);
						writeFile(title.getText() + "", body.getText() + "");
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		builder.show();
	}

	//The below method writes file in text format. 
	//This method returns void value.
	//This method is written by Vaishali.
	private void writeFile(String title, String body) {
		File file = new File(Dir.getAbsolutePath() + File.separator + title
				+ ".txt");
		// Untitled logic
		if (title == "") {
			for (int i = 1;; i++) {
				title = "Untitled_" + i;
				file = new File(Dir.getAbsolutePath() + File.separator + title
						+ ".txt");
				if (!file.isFile())
					break;
			}
		}
		if (file.isFile() && !isOverWriteEnabled) // user given titled file
													// already present and user
													// is not editing the
													// previously saved note
		{
			Toast.makeText(this,
					R.string.fileAlreadyExistsToast,
					Toast.LENGTH_LONG).show();

		} // if user titled file is not present write the file
		else {

			try {
				if (isOverWriteEnabled) {
					// Delete the previous file if its in editing mode
					file.delete();
				}
				// Create a new file
				if (file.createNewFile()) {
					// Write file
					BufferedWriter fileBufferWriter = new BufferedWriter(
							new java.io.FileWriter(file.getAbsoluteFile(), true));
					fileBufferWriter.write(body);
					fileBufferWriter.flush();
					fileBufferWriter.close();
					Toast.makeText(this,"The file is saved at \n" + file.getPath(),
							Toast.LENGTH_LONG).show();
					// Enter entry into settings file
						// Only enter a new entry if its a new file
					if (!isOverWriteEnabled) {
						BufferedWriter settingsBufferWriter = new BufferedWriter(
								new java.io.FileWriter(
										settingFile.getAbsoluteFile(), true));

						settingsBufferWriter.write("\n" + title + ".txt");
						settingsBufferWriter.flush();
						settingsBufferWriter.close();

						recentDocuments.add(title);
					}
					isOverWriteEnabled = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}

	//The below method create recent Menus.
	//This method returns void value.
	//This method is written by Vaishali.
	private void creatingRecentsMenu() {
		try {
			recentDocuments.clear();
			BufferedReader br = new BufferedReader(new FileReader(settingFile));
			String line = "";
			StringBuilder text = new StringBuilder();

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
				recentDocuments.add(line);
			}

			for (int i = 0; i < recentDocuments.size() - 1; i++) {

				if (i < 5) {
					sub.getItem(i)
							.setTitle(
									recentDocuments.get(recentDocuments.size()
											- i - 1));
					sub.getItem(i).setVisible(true);
				} else {
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
