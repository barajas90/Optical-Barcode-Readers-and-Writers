/**
 * Barcode Image
 * CST 338 - Assignment 4
 *
 * Represents a 2D QR Code.
 *
 * @author Thomas Krause
 * @version 1.0
 */
public class BarcodeImage implements Cloneable {
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;

   private boolean[][] imageData;

   /**
    * Default constructor
    */
   public BarcodeImage() {
      // This is all that is needed. Booleans are false
      // by default.
      this.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }

   /**
    * Copy constructor.
    *
    * @param barcode
    */
   public BarcodeImage(BarcodeImage barcode) {
      // Init the array
      this();

      for (int y = 0; y < MAX_HEIGHT; y++) {
         for (int x = 0; x < MAX_WIDTH; x++) {
            // Sets the current pixel with the pixel from
            // the other barcode
            this.setPixel(x, y,
               barcode.getPixel(x, y)
            );
         }
      }
   }

   /**
    * Constructor that converts 1D data into 2D.
    *
    * @param str_data
    */
   public BarcodeImage(String[] str_data) {
      this();

      // Ensure size is valid
      if (! this.checkSize(str_data))
         return;

      int y = MAX_HEIGHT - 1;
      for (int i = str_data.length - 1; i >= 0; i--) {
         String chunk = str_data[i];
         // For each character in the chunk
         // if it is set, propagate that to the structure
         for (int x = 0; x < chunk.length(); x++) {
            this.setPixel(x, y,
               chunk.charAt(x) != ' ' && chunk.charAt(x) != 0
            );
         }

         y--;
      }
   }

   /**
    * Gets the value for a pixel.
    *
    * @param row x
    * @param col y
    * @return success|value
    */
   public boolean getPixel(int row, int col) {
      return isValidOffset(row, col) && this.imageData[col][row];
   }

   /**
    * Sets a pixel to a value.
    *
    * @param row   x
    * @param col   y
    * @param value value
    * @return success
    */
   public boolean setPixel(int row, int col, boolean value) {
      if (! isValidOffset(row, col))
         return false;

      this.imageData[col][row] = value;
      return true;
   }

   /**
    * Ensures incoming string data is valid.
    *
    * @param data
    * @return valid
    */
   private boolean checkSize(String[] data) {
      if (data == null || data.length > MAX_HEIGHT)
         return false;

      for (String string : data) {
         if (string == null || string.length() > MAX_WIDTH)
            return false;
      }

      return true;
   }

   /**
    * Is the location requested valid.
    *
    * @param row x
    * @param col y
    * @return success
    */
   private boolean isValidOffset(int row, int col) {
      return col >= 0 && col < MAX_HEIGHT
         && row >= 0 && row < MAX_WIDTH;
   }

   /**
    * Debugging until to display the contents of the matrix.
    */
   public void displayToConsole() {
      System.out.println(this.toString());
   }

   /**
    * Converts barcode image to string representation
    *
    * @return string
    */
   public String toString() {
      String out = "";

      for (int y = 0; y < MAX_HEIGHT; y++) {
         for (int x = 0; x < MAX_WIDTH; x++) {
            int v = getPixel(x, y) ? 1 : 0;
            out += v + " ";
         }
         out += "\n";
      }

      return out;
   }

   /**
    * Deep clones the instance with a copy constructor.
    *
    * @return new BarcodeImage
    * @throws CloneNotSupportedException
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      return new BarcodeImage(this);
   }
}
