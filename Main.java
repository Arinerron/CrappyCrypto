import java.util.*;
import java.math.BigInteger;
import javax.xml.bind.DatatypeConverter;

public class Main {
    public static void main(String[] args) {
        if(args.length != 0) {
            if(args[0].equalsIgnoreCase("-e") || args[0].equalsIgnoreCase("--encrypt")) {
                Scanner scanner = new Scanner(System.in);
                StringBuilder builder = new StringBuilder();
                String line = "";
                while(line != null && scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    builder.append(line).append("\n");
                }

                System.out.println(encode(builder.toString().substring(0, builder.toString().length() - 1)));

                scanner.close();
            } else if(args[0].equalsIgnoreCase("-d") || args[0].equalsIgnoreCase("--decode")) {
                Scanner scanner = new Scanner(System.in);
                StringBuilder builder = new StringBuilder();
                String line = "";
                while(line != null && scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    builder.append(line).append("\n");
                }

                System.out.println(decode(builder.toString().substring(0, builder.toString().length() - 1)));

                scanner.close();
            }
        } else
            System.out.println("Encrypt:\n    echo \"this is not secure\" | crappycrypto -e\nDecrypt:\n    cat encrypted.txt | crappycrypto -d");
    }

    /* Encodes a string */
    public static String encode(String bytes) {
        return encode(bytes.getBytes());
    }

    /* Encodes an array of bytes */
    public static String encode(byte[] bytes) {
        /*
         * 1. Reverse bytes in array
         * 2. Add next byte to current byte then set next byte to difference
         * 3. Add the current byte to the next byte then set the current byte to the difference
         */

         // reverse bytes
         bytes = reverse(bytes);

         boolean flip = true;
         for(int i = 0; i < bytes.length; i++) {
             byte b = bytes[i];

             // if there is another byte coming
             if(i != bytes.length - 1 && flip) {
                byte next = bytes[i + 1];

                // subtract next byte from the current
                byte subtract = (byte)(b - next);

                // set byte to subtracted byte
                bytes[i] = subtract;
             }

             flip = !flip;
         }

         flip = false;
         for(int i = 0; i < bytes.length; i++) {
             byte b = bytes[i];

             // if there is another byte coming
             if(i != 0 && flip) {
                byte next = bytes[i - 1];

                // subtract next byte from the current
                byte subtract = (byte)(b - next);

                // set byte to subtracted byte
                bytes[i] = subtract;
             }

             flip = !flip;
         }

         return bytesToHex(bytes);
    }

    public static String decode(String hex) {
        return decode(hexToBytes(hex));
    }

    public static String decode(byte[] bytes) {
        boolean flip = false;
        for(int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];

            // if there is another byte coming
            if(i != 0 && flip) {
               byte next = bytes[i - 1];

               // subtract next byte from the current
               byte subtract = (byte)(b + next);

               // set byte to subtracted byte
               bytes[i] = subtract;
            }

            flip = !flip;
        }

        flip = true;
        for(int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];

            // if there is another byte coming
            if(i != bytes.length - 1 && flip) {
               byte next = bytes[i + 1];

               // subtract next byte from the current
               byte subtract = (byte)(b + next);

               // set byte to subtracted byte
               bytes[i] = subtract;
            }

            flip = !flip;
        }

        // reverse bytes
        bytes = reverse(bytes);

        return new String(bytes);
    }

    private static byte[] reverse(byte[] array) {
        if (array == null)
            return null;
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }

        return array;
    }

    /* Converts an array of bytes to hex*/
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static byte[] hexToBytes(String encoded) {System.out.println(encoded.length());
        if ((encoded.length() % 2) != 0)
        encoded += "0";

        final byte result[] = new byte[encoded.length()/2];
        final char enc[] = encoded.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i/2] = (byte) Integer.parseInt(curr.toString(), 16);
        }
        return result;
    }
}
