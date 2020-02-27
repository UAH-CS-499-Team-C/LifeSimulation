// This is an incredibly stupid class with the sole purpose of fixing the 
// rediculous java.lang.UnsatisfiedLinkError that keeps popping up

package lifesimulation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author d4g0n
 */
public class Setup {
    
    // This will adjust your java library path to use the libraries that are
    // necessary for the simulation. It will alter the path based on your OS.
    public static void setup(){
        
        String os = System.getProperty("os.name");
        
        if (os == "Linux"){
            
            String regex = "slick_lib/lwjgl-2.9.3/native/linux";
            Pattern lib = Pattern.compile(regex);
            String path = System.getProperty("java.library.path");
            Matcher match = lib.matcher(System.getProperty("java.library.path"));
            
            if(!match.find()){
                System.setProperty("java.library.path", System.getProperty("java.library.path") + ":../../slick_lib/lwjgl-2.9.3/native/linux");
            }
            
        }
        else if(os == "Mac"){
            
            String regex = "slick_lib/lwjgl-2.9.3/native/macosx";
            Pattern lib = Pattern.compile(regex);
            String path = System.getProperty("java.library.path");
            Matcher match = lib.matcher(System.getProperty("java.library.path"));
            
            if(!match.find()){
                System.setProperty("java.library.path", System.getProperty("java.library.path") + ":../../slick_lib/lwjgl-2.9.3/native/macosx");
            }
            
        }
        else if(os == "Windows"){
            
            String regex = "slick_lib\\lwjgl-2.9.3\\native\\windows";
            Pattern lib = Pattern.compile(regex);
            String path = System.getProperty("java.library.path");
            Matcher match = lib.matcher(System.getProperty("java.library.path"));
            
            if(!match.find()){
                System.setProperty("java.library.path", System.getProperty("java.library.path") + ":..\\..\\slick_lib\\lwjgl-2.9.3\\native\\windows");
            }
            
        }
        
    }
    
}
