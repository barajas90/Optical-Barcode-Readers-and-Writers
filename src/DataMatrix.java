public class DataMatrix implements BarcodeIO {
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';

   private BarcodeImage image;
   private String text = "";
   private int actualWidth = 0;
   private int actualHeight = 0;

   public DataMatrix() {
      this.image = new BarcodeImage();
   }

   public DataMatrix(BarcodeImage image) {
      this.scan(image);
   }

   //region Getters

   /**
    * Gets the width of the actual data.
    *
    * @return width
    */
   public int getActualWidth() {
      return actualWidth;
   }

   /**
    * Gets the height of the actual data.
    *
    * @return height
    */
   public int getActualHeight() {
      return actualHeight;
   }

   private int computeSignalWidth() {
      int width = 0;

      while (this.image.getPixel(width, BarcodeImage.MAX_HEIGHT - 1)) {
         width++;
      }

      return width;
   }

   /**
    * Calculate how tall the signal section is.
    *
    * @return
    */
   private int computeSignalHeight() {
      int offset = BarcodeImage.MAX_HEIGHT - 1;

      while (this.image.getPixel(0, offset)) {
         --offset;
      }

      // Compute actual height from the offset.
      return BarcodeImage.MAX_HEIGHT - offset - 1;
   }

   //endregion

   //region Setters

   //endregion

   @Override
   public boolean scan(BarcodeImage bc) {
      try {
         this.image = (BarcodeImage) bc.clone();
         this.cleanImage();

         this.actualHeight = this.computeSignalHeight();
         this.actualWidth = this.computeSignalWidth();
      } catch (CloneNotSupportedException e) {
         return false;
      }

      return true;
   }

   @Override
   public boolean readText(String text) {
      if (text == null)
         return false;

      this.text = text;

      return true;
   }

   /**
    * Writes some text to an image.
    * Currently will support up to 63 characters of text.
    *
    * @return success
    */
   @Override
   public boolean generateImageFromText() {
      this.image = new BarcodeImage();

      // Write left side border
      int col = 0;
      writeCharToCol(col++, 255);

      for (char c : this.text.toCharArray()) {
         writeCharToCol(col++, c);
      }

      // Write right side border
      writeCharToCol(col, 170);

      // Write bottom and top border
      for (int x = 0; x <= col; x++) {
         this.image.setPixel(x, BarcodeImage.MAX_HEIGHT - 1, true);
         this.image.setPixel(x, BarcodeImage.MAX_HEIGHT - 10, x % 2 == 0);
      }

      this.actualHeight = computeSignalHeight();
      this.actualWidth = computeSignalWidth();

      return true;
   }

   /**
    * Converts and image to it's text representation.
    *
    * @return text
    */
   @Override
   public boolean translateImageToText() {
      // Start at 1 to ignore the spine
      this.text = "";
      for (int x = 1; x < getActualWidth(); x++) {
         char c = readCharFromCol(x);
         // 170 is the delimiter on top and right
         if (c == 170 || c == 0)
            break;

         this.text += readCharFromCol(x);
      }

      return true;
   }

   /**
    * Display the text that was decoded.
    */
   @Override
   public void displayTextToConsole() {
      System.out.println(this.text);
   }

   /**
    * Display the image in ASCII format.
    */
   @Override
   public void displayImageToConsole() {
      this.printHorizontalBar(false);

      // Use the determined size to only display the content
      for (int y = BarcodeImage.MAX_HEIGHT - getActualHeight();
           y < BarcodeImage.MAX_HEIGHT; y++) {

         System.out.print('|');
         for (int x = 0; x < getActualWidth(); x++) {
            System.out.print(
               this.image.getPixel(x, y) ? BLACK_CHAR : WHITE_CHAR
            );
         }

         System.out.println('|');
      }
   }

   //region Helpers

   /**
    * Reads a character from a column.
    *
    * @param col column
    * @return character
    */
   private char readCharFromCol(int col) {
      int height = BarcodeImage.MAX_HEIGHT - 2;

      int value = 0;
      for (int offset = 0; offset < 8; offset++) {
         if (this.image.getPixel(col, height - offset)) {
            value += Math.pow(2, offset);
         }
      }

      // Convert the value to ascii
      return (char) value;
   }

   /**
    * Helper to write data to a column.
    *
    * @param col column
    * @param code code
    * @return success
    */
   private boolean writeCharToCol(int col, int code) {
      if (code > 255 || col == BarcodeImage.MAX_WIDTH)
         return false;

      int height = BarcodeImage.MAX_HEIGHT - 2;
      int value = code;

      for (int offset = 0; offset < 8; offset++) {
         this.image.setPixel(col, height - offset, value % 2 == 1);
         value /= 2;
      }

      return true;
   }

   /**
    * Shifts the image into the bottom left
    */
   private void cleanImage() {
      // min x should have a high value here
      // since we're trying to find min for x, and higher value for y
      int mx = BarcodeImage.MAX_WIDTH, my = 0;

      // Find the left and bottom most data
      for (int y = BarcodeImage.MAX_HEIGHT - 1; y >= 0; y--) {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++) {
            // No data here, keep going until we find some
            if (! this.image.getPixel(x, y))
               continue;

            // There was data found, is this the lowest value?
            mx = Math.min(x, mx);
            my = Math.max(y, my);
         }
      }

      // Since we had to make a high offset, if it's out of
      // bounds no shifting is needed for x
      if (mx >= BarcodeImage.MAX_WIDTH)
         mx = 0;

      // Shift the image by our offsets
      this.shiftImage(mx, BarcodeImage.MAX_HEIGHT - my - 1);
   }

   /**
    * Shifts the image in a direction.
    *
    * Positive dx value will shift left, negative will shift right
    * Positive dy value will shift down, negative will shift up
    *
    * @param dx distance x
    * @param dy distance y
    */
   private void shiftImage(int dx, int dy) {
      // They're just messing with us, no shifting required
      if (dx == 0 && dy == 0)
         return;

      for (int y = BarcodeImage.MAX_HEIGHT - 1; y >= 0; y--) {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++) {
            this.image.setPixel(
               x, y,
               this.image.getPixel(x + dx, y - dy)
            );
         }
      }
   }

   /**
    * Displays the raw representation of the image in a box
    * for debugging
    */
   public void displayRawImage() {
      this.printHorizontalBar(true);

      for (int y = 0; y < BarcodeImage.MAX_HEIGHT; y++) {
         System.out.print('|');
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; x++) {
            System.out.print(
               this.image.getPixel(x, y) ? BLACK_CHAR : WHITE_CHAR
            );
         }

         System.out.println('|');
      }

      this.printHorizontalBar(true);
   }

   /**
    * Helper method to print the horizontal bar
    * to create a box around the content
    */
   public void printHorizontalBar(boolean full) {
      int width = (
         full ? BarcodeImage.MAX_WIDTH : getActualWidth()
      ) + 2;

      for (int x = 0; x < width; x++) {
         System.out.print('-');
      }

      System.out.println();
   }

   //endregion
}
