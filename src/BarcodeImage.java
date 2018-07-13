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

      // Start at the bottom left corner
      int dy = str_data.length - 1;
      for (int y = MAX_HEIGHT - 1; y >= 0; y--) {
         // If there is no more data left, bail out.
         if (dy < 0)
            break;

         // Get the row, each character is the value
         String value = str_data[dy--];

         // The string could be null, if it is this row
         // has no data
         if (value == null)
            continue;

         // Convert to char array
         char[] row = value.toCharArray();
         for (int x = 0; x < MAX_WIDTH; x++) {
            // Ensure there is still data in the char array
            if (x >= row.length)
               break;

            // If the char is a space or null there's no data here
            setPixel(x, y, row[x] != ' ' && row[x] != 0);
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

   /**
    * Deep clones the instance with a copy constructor.
    *
    * @return new BarcodeImage
    * @throws CloneNotSupportedException
    */
   @Override
   protected Object clone() throws CloneNotSupportedException {
      return new BarcodeImage(this);
   }
}
