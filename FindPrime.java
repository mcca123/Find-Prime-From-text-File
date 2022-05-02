import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Map;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FindPrime {
    static Scanner sc = new Scanner(System.in);
    public static String filename;
    public static int bit;

    public static void getInput() {
        System.out.print("input filename : ");
        filename = sc.nextLine() + ".txt";
    }

    public static void main(String[] args) {
        getInput();

        System.out.println("===Result===");
        String content = readFile(filename);
        printResult("Content is -> " + content);
        String binaryResult = convertStringToBinary(content);

        //store binary Length
        int keyLength = binaryResult.length();
        System.out.println("keyLength : " + keyLength);

        printResult(binaryResult);

        //block Size => " " 
        int blockSize = 7 ;


        
        //convertStringToArray
        String [] plaintextString = printPrettyBinary(binaryResult,blockSize);
        char[] ch = convertStringToArray(binaryResult);

        //padding
        String [] pad = padding(plaintextString,blockSize);
        System.out.println("padding : " + Arrays.toString(pad));

        long decimal = convertStringBinaryToDecimal(selectBit(ch));
        printResult("base 10 : " + decimal);
        while (true) {
            System.out.println("find prime of " + decimal);
            if (findPrime(decimal)) {
                System.out.print(decimal + " is prime");
                if (findPrime((decimal - 1) / 2)) {
                    System.out.println(" and safe prime");
                    break;
                } else
                    System.out.println(" but not safe prime");
            } else {
                System.out.println(decimal + " is not prime");
            }
            if (decimal % 2 == 0)
                decimal += 1;
            else
                decimal += 2;
        }
        System.out.println("Safeprime :" + decimal);
        long keygen = KeyGen.KeyGenerator(decimal);
        System.out.println("key Generator is " + keygen);
        Map<String, Long> key = KeyGen.GenKey(keygen, decimal);
        System.out.println("==Public Key==");
        System.out.println("g :" + key.get("g"));
        System.out.println("p :" + key.get("p"));
        System.out.println("y :" + key.get("y"));
        System.out.println("==Private Key==");
        System.out.println("u :" + key.get("u"));

    }

    //Print Result Binary divide by 8 for beautiful
    public static String[] printPrettyBinary(String text,int blockSize){

        String k = prettyBinary(text, blockSize, " ");
        //split sting by " " fot covent to Array
        String[] plaintextString = k.split(" ");
        System.out.println("conent string to Long array : " + Arrays.toString(plaintextString));
        
        return plaintextString;  
    }

    public static String[] padding(String plaintextString[] , int blockSize) {
        
        for (int i = 0; i < plaintextString.length; i++) {
            if(plaintextString[i].length() < blockSize){
                int temp = blockSize - plaintextString[i].length();
                for (int j = 0; j < temp; j++) {
                    StringBuilder pad = new StringBuilder(plaintextString[i]);
                    pad.append("0");
                    plaintextString[i] = pad.toString();
                }
            }
        }

        return plaintextString;
    }


    public static void printResult(String text) {
        // print text
        System.out.println(text);
    }

    public static String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();

        // append text in textfile to string birany
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar)) // char -> int, auto-cast
                            .replaceAll(" ", "0") // zero pads
            );
        }
        return result.toString();

    }

    public static String prettyBinary(String binary, int blockSize, String separator) {
        List<String> result = new ArrayList<>();
        int index = 0;
  
        //
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }
  
        return result.stream().collect(Collectors.joining(separator));
    }

    public static String readFile(String filename) {
        String data = "";
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            // we use While Loop because if textfile have nextLine while loop can fix it
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }

            // System.out.println("Number of bits is : " + bit);
            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred, File is not found");
            e.printStackTrace();
        }
        return data;
    }

    public static char[] convertStringToArray(String Binary) {

        String str = Binary;

        // Creating array of string length
        char[] ch = new char[str.length()];

        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            ch[i] = str.charAt(i);
        }

        return ch;
    }

    public static String selectBit(char[] ch) {
        int countZero = 0;

        // Check 0 in front until find 1
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '1') {
                break;
            } else if (ch[i] == '0') {
                countZero++;
            }
        }

        // accept Bit is bits without 0 in front
        int acceptBit = ch.length - countZero;

        // we accept Bit 0-63
        if (acceptBit > 63) {
            acceptBit = 63;
        }

        // bit input
        System.out.println("Accept Bit range is (1," + acceptBit + ")");
        System.out.print("input bit length : ");
        bit = sc.nextInt();

        // if bit input is error, try aggin unit it correct
        while (bit > acceptBit || bit <= 0 || bit > 63) {
            System.out.println("Accept Bit range is (1," + acceptBit + ")");
            System.out.print("input bit length : ");
            bit = sc.nextInt();
        }

        // binary that we select from Bit length without 0 in front
        String tempBit = "";
        for (int i = countZero; i <= bit; i++) {
            tempBit += ch[i];
        }
        System.out.println("Select Bit : " + tempBit);
        return tempBit;

    }

    public static long convertStringBinaryToDecimal(String Bit) {
        // Convert string Binary To Decimal
        long foo = Long.parseLong(Bit, 2);
        return foo;
    }

    private static boolean findPrime(long n) {
        // find prime
        if (n % 2 == 0)
            return false;
        boolean isPrime = false;
        long leftLimit = 1L;
        long rightLimit = n;
        long a;

        for (int i = 1; i <= 100; i++) {
            // random a
            a = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
            // System.out.println("result"+fastExponent(a, (n - 1) / 2, n));
            if (n / a == 0) {
                isPrime = false;
            } else {
                if (gcd(a, n) > 1) {
                    isPrime = false;
                } else if (fastExponent(a, (n - 1) / 2, n) == 1 || fastExponent(a, (n - 1) / 2, n) == n - 1) {
                    isPrime = true;
                } else
                    isPrime = false;
            }
        }

        return isPrime;

    }

    public static long gcd(long n1, long n2) {
        // find gcd
        long gcd = 0;
        long q = n1 / n2;
        long r = n1 % n2;
        long a1 = 1, a2 = 0, b1 = 0, b2 = 1, t;

        while (r != 0) {
            n1 = n2;
            n2 = r;
            t = a2;
            a2 = a1 - q * a2;
            a1 = t;
            t = b2;
            b2 = b1 - q * b2;
            b1 = t;
            q = n1 / n2;
            r = n1 % n2;
        }
        gcd = n2;
        return gcd;

    }

    public static long fastExponent(long base, long expo, long mod) {
        // find power

        long result = 1;
        while (expo > 0) {
            // if last bit is 0
            if ((expo & 1) != 0) {
                result = (result * base) % mod;
            }

            // base = fastExponent(base % mod, 2, mod) % mod;
            base = /* (base*base) % mod; */((base % mod) * (base % mod)) % mod;
            expo = expo >> 1;

        }

        return result;
    }
}