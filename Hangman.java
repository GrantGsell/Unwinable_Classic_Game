import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Hangman {

    public static void main(String[] args) throws FileNotFoundException {
        boolean playAgain = true;
        int again = 1;
        while(playAgain) {
            Map<Integer, HashSet<String>> map;
            ArrayList<String> wordList = new ArrayList<>();
            map = generateSet();
            Scanner userInp = new Scanner(System.in);
            int userLenWordSentinel = 1;
            Integer guessLen = 0, userNumGuesses;
            while (userLenWordSentinel == 1) {
                System.out.print("Please enter the length of the word you would like to guess (2-22, 28):\n");
                guessLen = userInp.nextInt();
                if (guessLen < 2 || (guessLen > 22 && guessLen < 28) || guessLen > 28) {
                    System.out.print("\nUnfortunately you did not enter an aceeptable value. Please try again\n");
                } else {
                    userLenWordSentinel = 0;
                }
            }
            System.out.print("Please enter the number of guesses you would like:\n");
            userNumGuesses = userInp.nextInt();
            wordList = userLetterLength(map, guessLen);
            userGuess(wordList, guessLen, userNumGuesses);
            System.out.print("\nWould you like to play again? 1 for yes, 0 for no:\n");
            again = userInp.nextInt();
            if(again==1);
            else playAgain = false;
        }
    }

    public static Map<Integer, HashSet<String>> generateSet() throws FileNotFoundException {
        File dictionary = new File("dictionary.txt");
        Scanner scanner = new Scanner(dictionary);
        String str;
        Map<Integer, HashSet<String>> hash_Map = new HashMap<Integer, HashSet<String>>();
        while (scanner.hasNextLine()) {
            str = scanner.nextLine();
            Integer len = str.length();
            HashSet set = hash_Map.get(len);
            if(set==null){
                set = new HashSet<String>();
                hash_Map.put(len, set);
            }
            set.add(str);
        }
        return hash_Map;
    }

    public static ArrayList<String> userLetterLength(Map<Integer, HashSet<String>> map, Integer guessLen){
        Map<Integer, HashSet<String>> newMap = new HashMap<Integer, HashSet<String>>();
        newMap.putAll(map);
        Iterator itrVal = newMap.keySet().iterator();
        while(map.size()>1) {
            Integer nxtVal = (Integer) itrVal.next();
            if (!nxtVal.equals(guessLen)) {
                map.remove(nxtVal);
            }
        }
        for(Integer i=0; i<guessLen; i++){
            System.out.print(" ___ ");
        }
        ArrayList<String> wordList = new ArrayList<>(map.get(guessLen));
        return wordList;
    }

    public static void userGuess(ArrayList<String> arrOfWords, Integer numLetters, Integer userNumGuesses){
        Map<Integer, HashSet<String>> guessMap = new HashMap<Integer, HashSet<String>>();;
        ArrayList<String> outputList = new ArrayList<String>(numLetters);
        HashSet guessSet = guessMap.get(0);
        HashSet wrongGuessSet = guessMap.get(1);
        if(guessSet==null){
            guessSet = new HashSet<String>();
            guessMap.put(0, guessSet);
        }
        if(wrongGuessSet==null){
            wrongGuessSet = new HashSet<String>();
            guessMap.put(1, wrongGuessSet);
        }
        for(int i=0; i<numLetters; i++){
            outputList.add(null);
        }
        int wrongGuessCount = 0;
        String userGuessLo = "", userGuessUp = "", userGuess = "";
        while (wrongGuessCount<(userNumGuesses) ){
            ArrayList<String> replacementArr = new ArrayList<String>(arrOfWords);
            Scanner userInp = new Scanner(System.in);
            boolean newGuess = false;
            while(newGuess == false) {
                System.out.print("\nMake a one letter guess:\n");
                userGuess = userInp.next();
                //Make the guess both upper and lower case
                userGuessLo = userGuess.toLowerCase();
                userGuessUp = userGuess.toUpperCase();
                //Check to see if the user has already guessed this letter
                guessSet = guessMap.get(0);
                if (guessSet.contains(userGuessLo) || guessSet.contains(userGuessUp)) {
                    System.out.print("Sorry but you have already guessed that letter. Please enter another letter: \n");
                }
                else {
                    guessSet.add(userGuess);
                    newGuess = true;
                }
            }
            newGuess = false;
            /////////////////////////////
            System.out.println("Your letter guess is: " + userGuess);
            int ogLen = arrOfWords.size();
            for (int i = 0; i < arrOfWords.size(); i++) {
                if (arrOfWords.get(i).contains(userGuessUp) || arrOfWords.get(i).contains(userGuessLo) ){
                    arrOfWords.remove(i);
                    i--;
                }
            }
            int newLen = arrOfWords.size();
            if (newLen != 0 ) {
                System.out.print("\nSorry but that guess is incorrect!\n");
                wrongGuessSet.add(userGuess);
                wrongGuessCount++;
            }
            else {
                System.out.println("Congrats you guessed correctly!");
                arrOfWords = replacementArr;
                if(arrOfWords.size() == 1){
                    String lastWord = arrOfWords.get(0);
                    for(int i=0; i < numLetters; i++){
                        if((Character.toString(lastWord.charAt(i)).equals(userGuessLo)) || (Character.toString(lastWord.charAt(i)).equals(userGuessUp))){
                            outputList.set(i, userGuess);
                        }
                    }
                }
                else {
                    int temp2 = (int)numLetters;
                    ArrayList<Integer> posLetterCount = new ArrayList<Integer>(5);
                    for(int i =0; i<temp2; i++){
                        posLetterCount.add(0);
                    }
                    for (int i = 0; i < arrOfWords.size(); i++) {
                        String charCheck = arrOfWords.get(i);
                        int inst0 = 0;
                        for (int j = 0; j < numLetters; j++) {
                            if ((Character.toString(charCheck.charAt(j)).equals(userGuessLo)) || (Character.toString(charCheck.charAt(j)).equals(userGuessUp))) {
                                if (inst0 == 1) {
                                    arrOfWords.remove(i);
                                    i--;
                                } else {
                                    inst0 = 1;
                                }
                            }
                        }
                    }
                    if(arrOfWords.size() == 0){
                        arrOfWords = replacementArr;
                    }
                    for (int i = 0; i < arrOfWords.size(); i++) {
                        int index = 0;
                        String charCheck = arrOfWords.get(i);
                        for (int j = 0; j < numLetters; j++) {
                            if ((Character.toString(charCheck.charAt(j)).equals(userGuessLo)) || (Character.toString(charCheck.charAt(j)).equals(userGuessUp))) {
                                int temp = posLetterCount.get(j);
                                posLetterCount.set(index, (temp + 1));
                            }
                            index++;
                        }
                    }
                    Integer highSpot = 0;
                    int highIndex = 0;
                    for(int i=0; i<posLetterCount.size(); i++){
                        if(posLetterCount.get(i) > highSpot){
                            highIndex = i;
                        }
                    }
                    for(int i = 0; i < arrOfWords.size(); i++){
                        String word = arrOfWords.get(i);
                        if(word.indexOf(userGuessLo)!= highIndex|| word.indexOf(userGuessLo)!= highIndex ){
                            arrOfWords.remove(i);
                            i--;
                        }
                    }
                    outputList.set(highIndex, userGuess);
                }
            }
            if(!outputList.contains(null)){
                for (int i = 0; i < numLetters; i++) {
                    System.out.print(" _" + outputList.get(i) + "_ ");
                }
                System.out.print("\nCongratulations you WON!\n");
                return;
            }
            else {
                for (int i = 0; i < numLetters; i++) {
                    if (outputList.get(i) == null) {
                        System.out.print(" ___ ");
                    } else {
                        System.out.print(" _" + outputList.get(i) + "_ ");
                    }
                }
            }
            System.out.print("\nWrong Guesses: \n");
            for(String char0: guessMap.get(1)){
                System.out.print(char0 + " ");
            }
            System.out.print("\nNumber of guesses left: " + (userNumGuesses-wrongGuessCount));
            System.out.print("\n");
        }
        System.out.print("\nYou are out of guesses! YOU LOSE!\n");
        System.out.print("The correct word was: \n");
        Random r = new Random();
        int indexCorrectWord = r.nextInt((arrOfWords.size() - 0) + 1) + 0;
        if(arrOfWords.size()==1) indexCorrectWord = 0;
        String correctWord = arrOfWords.get(indexCorrectWord);
        for(int i=0;i < numLetters; i++){
            outputList.set(i, Character.toString(correctWord.charAt(i)));
        }
        for (int i = 0; i < numLetters; i++) {
            if (outputList.get(i) == null) {
                System.out.print(" ___ ");
            } else {
                System.out.print(" _" + outputList.get(i) + "_ ");
            }
        }
        System.out.print("\n");
    }
}