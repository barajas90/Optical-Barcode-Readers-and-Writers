/**
 * BarcodeIO Specification
 *
 * @author Mercedes Garcia, Thomas Krause
 * @version 1.0
 */
public interface BarcodeIO {
   /**
    * Scans a barcode.
    *
    * @param bc barcode
    * @return success
    */
   public boolean scan(BarcodeImage bc);

   /**
    * Reads text.
    *
    * @param text
    * @return success
    */
   public boolean readText(String text);

   /**
    * Generate a barcode from text.
    *
    * @return success
    */
   public boolean generateImageFromText();

   /**
    * Parses a barcode into text.
    *
    * @return success
    */
   public boolean translateImageToText();

   /**
    * Outputs the text to the console.
    */
   public void displayTextToConsole();

   /**
    * Outputs the image to the console.
    */
   public void displayImageToConsole();
}
