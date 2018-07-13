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

   private boolean[][] image_data;

   /**
    * Default constructor
    */
   public BarcodeImage() {
      // This is all that is needed. Booleans are false
      // by default.
      this.image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
   }

   /**
    * Constructor that converts 1D data into 2D.
    *
    * @param str_data
    */
   public BarcodeImage(String[] str_data) {
      this();

      // Start at the bottom left corner
      int dx = 0;
      for (int y = MAX_HEIGHT - 1; y >= 0; y--) {
         for (int x = 0; x < MAX_WIDTH; x++) {
            // If dx is out of range in string skip
            if (dx >= str_data.length)
               break;

            // If the string have any value other than empty, space or null
            // this "bit" is true
            String value = str_data[dx++];
            setPixel(x, y,
               value != null
                  && !value.equals("")
                  && !value.equals(" ")
            );
         }
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
      return isValidOffset(row, col) && this.image_data[col][row];
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
      if (!isValidOffset(row, col))
         return false;

      this.image_data[col][row] = value;
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
      for (int y = 0; y < MAX_HEIGHT; y++) {
         for (int x = 0; x < MAX_WIDTH; x++) {
            int v = getPixel(x, y) ? 1 : 0;
            System.out.print(v + " ");
         }
         System.out.println();
      }

   }
}
