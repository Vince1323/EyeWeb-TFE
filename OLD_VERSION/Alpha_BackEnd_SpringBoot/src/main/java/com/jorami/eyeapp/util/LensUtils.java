package com.jorami.eyeapp.util;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class LensUtils {

    private static final Logger logger = Logger.getLogger(LensUtils.class);

    /**
     * returns a list of lists (can see this like a 2D array) containing all implants from url "https://iolcon.org/lensesTable.php"
     * each row of the list is an implant
     * at position 0 of a row : name of the implant
     * at position 1 of a row : Comment /Trade Name
     * at position 2 of a row : Nominal
     * at position 3 of a row : Haigis (string containing the 3 values of haigis a0, a1, a2) (need to be splitted)
     * at position 4 of a row : Hoffer Q(pACD)
     * at position 5 of a row : Holladay 1
     * at position 6 of a row : srk/t
     * at position 7 of a row : Castrop (string containing the 3 values of Castrop C, H, R) (need to be splitted)
     * at position 8 of a row : Name of the manufacturer
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<List<String>> getAllImplants()
    {
        List<List<String>> implants = new ArrayList<List<String>>();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        String searchUrl = "https://iolcon.org/lensesTable.php";
        HtmlPage page = null;
        try {
            page = client.getPage(searchUrl);
        } catch (FailingHttpStatusCodeException e) {
            logger.error(e);
        } catch (MalformedURLException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
        // position td[*]
        //  3: lens name
        //  4: comment
        // 30: nominal
        // 36: haigis A-a1-a2
        // 37: hofferQPACD
        // 38: holladay1
        // 39: srk/t
        // 40: castrop C-H-R
        List<HtmlElement> names = (List<HtmlElement>)(Object)page.getByXPath("//*/td[3]");
        List<HtmlElement> comments = (List<HtmlElement>)(Object)page.getByXPath("//*/td[4]");
        List<HtmlElement> nominal = (List<HtmlElement>)(Object)page.getByXPath("//*/td[30]");
        List<HtmlElement> haigis = (List<HtmlElement>)(Object)page.getByXPath("//*/td[31]");
        List<HtmlElement> hofferQPACD = (List<HtmlElement>)(Object)page.getByXPath("//*/td[32]");
        List<HtmlElement> holladay1 = (List<HtmlElement>)(Object)page.getByXPath("//*/td[33]");
        List<HtmlElement> srkt = (List<HtmlElement>)(Object)page.getByXPath("//*/td[34]");
        List<HtmlElement> haigisOptimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[36]");
        List<HtmlElement> hofferQPACDOptimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[37]");
        List<HtmlElement> holladay1Optimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[38]");
        List<HtmlElement> srktOptimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[39]");
        List<HtmlElement> castropOptimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[40]");
        List<HtmlElement> cookeOptimized = (List<HtmlElement>)(Object)page.getByXPath("//*/td[41]");

        for (int i = 0; i < names.size(); i++)
        {
            implants.add(new ArrayList<String>());
            implants.get(i).add(names.get(i).getVisibleText());
            implants.get(i).add(comments.get(i).getVisibleText());
            implants.get(i).add(concatenateIntFrac(nominal.get(i)));
            implants.get(i).add(haigisOptimized.get(i).getVisibleText());
            implants.get(i).add(hofferQPACDOptimized.get(i).getVisibleText());
            implants.get(i).add(holladay1Optimized.get(i).getVisibleText());
            implants.get(i).add(srktOptimized.get(i).getVisibleText());
            implants.get(i).add(castropOptimized.get(i).getVisibleText());
            implants.get(i).add(names.get(i).getEnclosingElement("tr").getAttribute("data-manufacturer"));
            implants.get(i).add(haigis.get(i).getVisibleText());
            implants.get(i).add(concatenateIntFrac(hofferQPACD.get(i)));
            implants.get(i).add(concatenateIntFrac(holladay1.get(i)));
            implants.get(i).add(concatenateIntFrac(srkt.get(i)));
            implants.get(i).add(cookeOptimized.get(i).getVisibleText());
        }

        client.close();
        return implants;
    }
    /**
     *
     * @param variables
     * @return
     */
    public static String formatNumbers(String variables)
    {
        String format ="";
        if(variables.length()<=1)
            return "";
        //1.8150.4 0.1
        int coma1 = variables.indexOf('.');
        int coma2 = variables.indexOf('.',coma1+1);
        int coma3 = variables.indexOf('.',coma2+1);

        if(variables.charAt(coma2-2)!= '-')
        {
            format = variables.substring(0, coma2-1)+ " ";
            if(variables.charAt(coma3-2)!= '-')
            {
                format+= variables.substring(coma2-1, coma3-1)+" ";
                format+= variables.substring(coma3-1);
            }
            else
            {
                format+= variables.substring(coma2-1, coma3-2)+" ";
                format+= variables.substring(coma3-2);
            }
        }
        else
        {
            format = variables.substring(0, coma2-2)+ " ";
            if(variables.charAt(coma3-2)!= '-')
            {
                format+= variables.substring(coma2-2, coma3-1)+" ";
                format+= variables.substring(coma3-1);
            }
            else
            {
                format+= variables.substring(coma2-2, coma3-2)+" ";
                format+= variables.substring(coma3-2);
            }
        }
        return format;
    }
    /**
     * Split the values of the string removing useless spaces
     * @param values : the values
     * @return
     */
    public static List<String> splittedValues(String values)
    {
        List<String> splitted = new ArrayList<String>();
        String var="";
        String[] split = values.split(" ");
        for (int i = 0; i < split.length; i++)
        {
            var = split[i];
            if(var.length()>1)
            {
                splitted.add(var);
            }
        }
        return splitted;
    }

    public static String verifyAndRemoveLast(String value){
        if(value == null){
            return null;
        } else{

            if(value.endsWith("N")){
                return value.substring(0, value.length()-1);
            } else {
                return value;
            }
        }
    }

    private static String concatenateIntFrac(HtmlElement element) {
        List<HtmlElement> intElements = element.getByXPath(".//span[@class='int']");
        List<HtmlElement> fracElements = element.getByXPath(".//span[@class='frac']");

        String intPart = intElements.isEmpty() ? "" : intElements.get(0).getTextContent();
        String fracPart = fracElements.isEmpty() ? "" : fracElements.get(0).getTextContent();

        return intPart + fracPart; // Concatène les parties "int" et "frac"
    }

    public static Float parseFloat(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        value = value.trim().replace(",", "");
        try {
            return Float.valueOf(value);
        } catch (NumberFormatException e) {
            // Log pour déboguer
            System.err.println("Invalid float format: " + value);
            return null;
        }
    }

}
