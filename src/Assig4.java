public class Assig4 {
   public static void main(String[] args) {
      // Feel free to delete all this
      // it was just to convince myself the
      // copy constructor and BarcodeImage class worked
      String[] s = {
         "1", "2", "3", "4", " ", "5"
      };
      BarcodeImage bi = new BarcodeImage(s);
      bi.displayToConsole();
      System.out.println();

      BarcodeImage bi2 = null;
      try {
         bi2 = (BarcodeImage) bi.clone();
         bi2.setPixel(0, 28, true);
         bi2.displayToConsole();
         System.out.println();
      } catch (CloneNotSupportedException e) {
         e.printStackTrace();
      }

      bi.displayToConsole();
   }
}
