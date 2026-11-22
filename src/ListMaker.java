
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class ListMaker
{
    public static void main(String[] args)
    {
        // Create an ArrayList to hold the list items
        ArrayList<String> myList = new ArrayList<>();

        Scanner console = new Scanner(System.in);

        boolean done = false;
        String command;

        // Main menu loop
        do
        {
            displayMenu();

            command = SafeInput.getRegExString(console,
                    "Enter your choice", "[AaDdIiPpOoSsQq]");

            command = command.toUpperCase();

            try
            {
                // Process the menu selection
                switch(command)
                {
                    case "A":
                        addItem(console, myList);
                        break;

                    case "D":
                        deleteItem(console, myList);
                        break;

                    case "I":
                        insertItem(console, myList);
                        break;

                    case "P":
                        printList(myList);
                        break;

                    case "O":
                        loadListFile(myList);
                        break;

                    case "S":
                        saveListFile(myList);
                        break;

                    case "Q":
                        done = quitProgram(console);
                        break;
                }
            }
            catch(IOException e)
            {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }

        } while(!done);

        console.close();
        System.out.println("Goodbye!");
    }

    // Display the menu
    public static void displayMenu()
    {
        System.out.println("\nList Maker Menu");
        System.out.println("A - Add an item");
        System.out.println("D - Delete an item");
        System.out.println("I - Insert an item");
        System.out.println("P - Print the list");
        System.out.println("O - Open a list file");
        System.out.println("S - Save the current list");
        System.out.println("Q - Quit");
    }

    // Print the list
    public static void printList(ArrayList<String> myList)
    {
        System.out.println("\nYour List:");

        if(myList.size() == 0)
        {
            System.out.println("The list is empty.");
        }
        else
        {
            for(int x = 0; x < myList.size(); x++)
            {
                System.out.println((x + 1) + ". " + myList.get(x));
            }
        }
    }

    // Add an item to the list
    public static void addItem(Scanner console, ArrayList<String> myList)
    {
        String item;

        item = SafeInput.getNonZeroLenString(console, "Enter item to add");

        myList.add(item);

        System.out.println(item + " was added to the list.");
    }

    // Delete an item from the list
    public static void deleteItem(Scanner console, ArrayList<String> myList)
    {
        if(myList.size() == 0)
        {
            System.out.println("The list is empty. Nothing to delete.");
        }
        else
        {
            printList(myList);

            int itemNumber = SafeInput.getRangedInt(console,
                    "Enter item number to delete", 1, myList.size());

            String removedItem = myList.remove(itemNumber - 1);

            System.out.println(removedItem + " was deleted.");
        }
    }

    // Insert an item into the list
    public static void insertItem(Scanner console, ArrayList<String> myList)
    {
        String item;

        item = SafeInput.getNonZeroLenString(console, "Enter item to insert");

        int location = SafeInput.getRangedInt(console,
                "Enter position to insert item", 1, myList.size() + 1);

        myList.add(location - 1, item);

        System.out.println(item + " was inserted into the list.");
    }

    // Load a list from a file
    public static void loadListFile(ArrayList<String> myList) throws IOException
    {
        // Create a JFileChooser object
        JFileChooser chooser = new JFileChooser();

        // Set the working directory
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        // Ask the user to select a file
        if(chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
        {
            System.out.println("No file selected. Returning to menu.");
            return;
        }

        // Store the selected file as a Path
        Path selectedPath = chooser.getSelectedFile().toPath();

        // Read all lines from the selected file
        java.util.List<String> lines = Files.readAllLines(selectedPath);

        // Replace the current list with the loaded list
        myList.clear();
        myList.addAll(lines);

        System.out.println("File loaded successfully!");
        System.out.println("File Name: " + selectedPath.getFileName());
        System.out.println("Items Loaded: " + myList.size());
    }

    // Save the current list to a file
    public static void saveListFile(ArrayList<String> myList) throws IOException
    {
        // Create a JFileChooser object
        JFileChooser chooser = new JFileChooser();

        // Set the working directory
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        // Set the save dialog title
        chooser.setDialogTitle("Save List File");

        // Ask the user where to save the file
        if(chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
        {
            System.out.println("No save location selected. Returning to menu.");
            return;
        }

        // Store the selected location as a Path
        Path filePath = chooser.getSelectedFile().toPath();

        // Make sure the file has a .txt extension
        if(!filePath.getFileName().toString().toLowerCase().endsWith(".txt"))
        {
            filePath = filePath.resolveSibling(
                    filePath.getFileName().toString() + ".txt");
        }

        // Write all list items to the file
        Files.write(filePath, myList);

        System.out.println("File saved successfully!");
        System.out.println("File Name: " + filePath.getFileName());
        System.out.println("Items Saved: " + myList.size());
    }

    // Confirm quitting the program
    public static boolean quitProgram(Scanner console)
    {
        return SafeInput.getYNConfirm(console,
                "Are you sure you want to quit?");
    }
}
