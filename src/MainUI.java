import java.util.Scanner;
import java.util.Set;

public class MainUI {

    // Retrieve data to the end of the line as an argument for a method call
    // Include two special kinds of arguments:
    //   "null" asks us to return no string
    //   "empty" asks us to return an empty string
    private static String getEndingString(Scanner userInput ) {
        String userArgument = null;

        userArgument = userInput.next();
        userArgument = userArgument.trim();

        // Include a "hack" to provide null and empty strings for testing
        if (userArgument.equalsIgnoreCase("empty")) {
            userArgument = "";
        } else if (userArgument.equalsIgnoreCase("null")) {
            userArgument = null;
        } else if(userArgument.equalsIgnoreCase("\"\"")){
            userArgument = "";
        }

        return userArgument;
    }

    // Main program to process user commands.
    // This method is not robust.  When it asks for a command, it expects all arguments to be there.

    public static void main(String[] args) {
        // Command options

        String add = "add";
        String cluster = "cluster";
        String quitCommand = "quit";

        // Define variables to manage user input

        String userCommand = "";
        String userArgument = "";
        Scanner userInput = new Scanner( System.in );

        // Define the recommender that we will be testing.

        VertexCluster vertexCluster = new VertexCluster();

        // Define variables to catch the return values of the methods

        boolean booleanOutcome;
        String vertex1 = null;
        String vertex2 = null;
        int edgeWeight = 0;
        float tolerance = 0.0f;
        Set<Set<String>> clusters = null;

        // Let the user know how to use this interface

        System.out.println("Please find below the commands available:");
        System.out.println("  " + add + " <vertex1> <vertex2> <weight>");
        System.out.println("  " + cluster + " <tolerance>");

        // Process the user input until they provide the command "quit"
        try {
            do {
                // Find out what the user wants to do
                userCommand = userInput.next();

                /* Do what the user asked for. */

                if (userCommand.equalsIgnoreCase(add)) {
                    // Get the parameters.

                    vertex1 = getEndingString(userInput);
                    vertex2 = getEndingString(userInput);
                    edgeWeight = userInput.nextInt();

                    // Call the method
                    booleanOutcome = vertexCluster.addEdge(vertex1, vertex2, edgeWeight);
                    System.out.println(userCommand + " vertex1: " + vertex1 + " vertex2: " + vertex2 +
                            " weight: " + edgeWeight + " outcome " + booleanOutcome);
                } else if (userCommand.equalsIgnoreCase(cluster)) {
                    // Get the parameters.

                    tolerance = userInput.nextFloat();

                    // Call the method

                    clusters = vertexCluster.clusterVertices(tolerance);
                    System.out.println(userCommand + " \"" + tolerance + "\" outcome " + clusters);
                } else if (userCommand.equalsIgnoreCase(quitCommand)) {
                    System.out.println(userCommand);
                } else {
                    System.out.println("Bad command: " + userCommand);
                }
            } while (!userCommand.equalsIgnoreCase("quit"));
        }
        catch (Exception e){
            System.out.println("Program faced an unexpected exception.");
        }

        // The user is done so close the stream of user input before ending.
        userInput.close();
    }
}


