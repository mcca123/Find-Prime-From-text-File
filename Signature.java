import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Signature {
    public static String Signature;
    public static String hashCode;
    public static String cipherA;
    public static String SignatureB;
    public static boolean Compare;

    public static String hash(String cipherText){
        //call hash
        hashCode = Hash.hashThisString(cipherText);
        System.out.println("HashCode Generated by SHA-512 for: ");
        System.out.println(cipherText + " : " + hashCode);

        return hashCode;
    }
    public static String hashEncrypt(String hashCode){
        //hash code --> Encrypt
        System.out.println("==Hash Encryption==");
        Signature = Encryption.encryptString(hashCode, "privateKey.txt");
        System.out.println("Signature : " + Signature);
        return Signature;
    }
    public static void signatureTofile(String cipherText, String Signature){
        String CipherFileName = "CipherSignature.txt";
        PrintWriter writerCS = null;
        try {
                writerCS = new PrintWriter(CipherFileName);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        writerCS.flush();
        writerCS.append(cipherText);
        writerCS.append("-");
        writerCS.append(Signature);
        writerCS.close();
    }

    public static String [] splitFile (String FlieName){
        
        String[] cipherAndSignature = FindPrime.readFile(FlieName).split("-");
        cipherA = cipherAndSignature[0];
        SignatureB = cipherAndSignature[1];
        System.out.println("Sognature after split : " + SignatureB);
        System.out.println("Cipher after split : " + cipherA);

        return cipherAndSignature;
    }

    public static String hashDecrypt(String SignatureB){
        System.out.println("==Hash Decryption==");
        String plain = Encryption.decryptString(SignatureB, "publicKey.txt");
        System.out.println("PlainText :"+ (plain));

        return Signature;
    }

    public static String isCompare(String hashCodeCompare,String cipherCompare){
        if(hashCodeCompare.equals(cipherCompare)){
                Compare = true;
                System.out.println("Compare : Yes");
        }else{
                Compare = false;
                System.out.println("Compare : No");
        }
        return Signature;
    }

    public static void run(){
        //Signetuer
        Hash.hashCode = hash(Main.cipherText);
        Signature = hashEncrypt(hashCode);
        signatureTofile(Main.cipherText, Signature);
        //split
        String FlieName = "CipherSignature.txt";
        splitFile(FlieName);
        //
        String hashCodeCompare = hashDecrypt(SignatureB);
        String cipherCompare = hash(cipherA);
        isCompare(hashCodeCompare, cipherCompare);
    }
}
