/// Description: reads in a file containing men and women's preferences and uses
/// the stable matching algorithm to find the best match for each of them.
///
/// Author: Ryan Nowak rcn8263
///

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Stable {

    /**
     * Checks if a man is free and hasn't proposed to every woman
     * @param MFree array containing each men and indicates if they are free
     * @param MProposed array containing the women that each man has proposed to
     * @return true if a man is free and hasn't proposed to every woman. Otherwise, return false
     */
    private static boolean isFree(int[] MFree, int[][] MProposed) {
        for (int i = 0; i < MFree.length; i++) {
            for (int j = 0; j < MProposed[i].length; j++) {
                if (MFree[i] == 0 && MProposed[i][j] == 0) {
                    return true;

                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File input = new File(args[0]);
        Scanner scan = new Scanner(input);

        String line = scan.nextLine().trim();
        String[] splitLine;

        int numOfPairs = Integer.parseInt(line);
        int[] MPairs = new int[numOfPairs]; //pairs of men and women that are currently together
        int[] WPairs = new int[numOfPairs]; //pairs of men and women that are currently together

        //get input data for men
        int[][] MPreference = new int[numOfPairs][numOfPairs]; //preferences of each man, lower index indicates higher preference
        int[][] MProposed = new int[numOfPairs][numOfPairs]; //who each man has proposed to
        for (int i = 0; i < numOfPairs; i++) {
            line = scan.nextLine().trim();
            splitLine = line.split("\\s+");
            for (int j = 0; j < numOfPairs; j++) {
                MPreference[i][j] = Integer.parseInt(splitLine[j+1])-1; //set the preferences for each man
            }
            MPairs[i] = -1; //no pairs exist yet
            WPairs[i] = -1;
        }

        //get input data for women
        int[][] WPreference = new int[numOfPairs][numOfPairs]; //preferences of each woman
        //int[][] WProposed = new int[numOfPairs][numOfPairs]; //who each woman has been proposed to
        for (int i = 0; i < numOfPairs; i++) {
            line = scan.nextLine().trim();
            splitLine = line.split("\\s+");
            for (int j = 0; j < numOfPairs; j++) {
                WPreference[i][j] = Integer.parseInt(splitLine[j+1])-1; //set the preferences for each woman
            }
        }

        //Initialize each man and woman to be free - 0 is free, 1 is taken
        int[] MFree = new int[numOfPairs];
        int[] WFree = new int[numOfPairs];

        //some man is free and hasn't proposed to every woman
        while (isFree(MFree, MProposed)) {
            int m = -1; //index of man who is free
            for (int i = 0; i < numOfPairs; i++) {
                if (MFree[i] == 0) {
                    m = i;
                    break;
                }
            }

            int w = -1; //index of highest preference woman who m hasn't proposed to yet
            for (int i = 0; i < numOfPairs; i++) {
                if (MProposed[m][MPreference[m][i]] == 0) {
                    w = MPreference[m][i];
                    break;
                }
            }

            //m proposes to w
            MProposed[m][w] = 1;

            //m,w become engaged
            if (WFree[w] == 0) {
                MFree[m] = 1;
                WFree[w] = 1;
                MPairs[m] = w;
                WPairs[w] = m;
            }
            else {
                //get w's preference of m and fiance
                int WPrefM = -1;
                int WPrefFiance = -1;
                for (int i = 0; i < numOfPairs; i++) {
                    if (WPreference[w][i] == m) {
                        WPrefM = i;
                    } else if (WPreference[w][i] == WPairs[w]) {
                        WPrefFiance = i;
                    }
                }

                //w prefers m  to her fiance
                if (WPrefM < WPrefFiance) {
                    MFree[m] = 1; //m becomes taken
                    MFree[WPairs[w]] = 0; //fiance becomes free
                    MPairs[m] = w;
                    WPairs[w] = m;
                }
            }
        }

        File output = new File(args[1]);
        FileWriter writer = new FileWriter(output, false);

        //System.out.println(numOfPairs);
        writer.write(numOfPairs + "\n");

        //Print men preferences
        for (int i = 0; i < numOfPairs; i++) {
            //System.out.print("m" + (i + 1));
            writer.write("m" + (i + 1));
            for (int j = 0; j < numOfPairs; j++) {
                //System.out.print(" " + (MPreference[i][j]+1));
                writer.write(" " + (MPreference[i][j]+1));
            }
            //System.out.println();
            writer.write("\n");
        }
        //Print women preferences
        for (int i = 0; i < numOfPairs; i++) {
            //System.out.print("w" + (i + 1));
            writer.write("w" + (i + 1));
            for (int j = 0; j < numOfPairs; j++) {
                //System.out.print(" " + (WPreference[i][j]+1));
                writer.write(" " + (WPreference[i][j]+1));
            }
            //System.out.println();
            writer.write("\n");
        }

        //Print pairs
        for (int i = 0; i < numOfPairs; i++) {
            //System.out.println("m" + (i+1) + " w" + (MPairs[i]+1));
            writer.write("m" + (i+1) + " w" + (MPairs[i]+1) + "\n");
        }

        writer.close();
    }
}
