/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* First ever Iteration of the software |DESIGN ONLY| == v.0.0.1: 14072021 --> id 56825
/* Software version 1.0.0: 23072021 == id 51275*/
/* Software version 2, iteration 1.0.1: 10082021 == id 50975 */
/* Software version 3, iteration number: 1.0.2: 13092021 == id 51075*/
/* Software version 3.1, iteration number: 1.0.2.1: 15092021 == id */
/* Software version 4.0, iteration number: 1.1.0.0: 22092021 == id */
/* Software version 4.1, iteration number: 1.1.1.0: 06102021 == id */
/* Software version 4.2, iteration number: 1.2.0.0: 08102021 == id */
/* Software version 4.3, iteration number: 1.3.16.0: 11102021 == id51050 */ //CURRENT

package segistelui_mt;

//import java.io.FileWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 * @author AliS2019
 */
public class SegistelUI_MT {

    /**
     * @param args the command line arguments
     */
    
  /*  private String infolen(String symbol){
       String txt;
        for(int i = 0; i <= symbol.length(); i++){
            
        }
        return 
    }
*/
    public static void main(String[] args) {
    String nameOS = "os.name";  
    String versionOS = "os.version";  
    String architectureOS = "os.arch";
    DateTimeFormatter systime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    
    
  System.out.println("\n  System Information:");
  
  System.out.println("\nOS Type: " + System.getProperty(nameOS));
  
  System.out.println("OS Version: " + System.getProperty(versionOS));
  
  System.out.println("OS Architecture: " + System.getProperty(architectureOS));
          
  System.out.println("System Time: " + systime.format(now));
  
  
  if(System.getProperty(nameOS).equals("Mac OS X")){
      System.out.printf("%n=======================================================================%n");
      System.err.println("This version of SegistelUI is not supported on Mac OS Systems");
      System.out.println("You can make this app work on Unix Based computers but it won't ");
      System.out.println("come with required libraries and will become useless in practice.");
      System.out.println("Use this App at your own risk! Not that it will destroy your computer >:)");
      System.out.printf("=========================================================================%n"); 
     
      new ResolveUI().setVisible(false);  
      new SegistelUI().setVisible(true);
  }else{
      new SegistelUI().setVisible(true);  
      System.out.println("Window manager is Active!");
  }
        
    }
}
