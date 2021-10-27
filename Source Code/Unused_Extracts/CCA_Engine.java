/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package segistelui_mt;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author AliS2019
 */

public class CCA_Engine {

    private void WorldList(){
        
        String[] WorldCountryList = {"--AFRICA--","Algeria","Angola","Benin","Botswana","Burkina Faso", "Burundi","Cameroon","Cape Verde", 
                                        "Central African Republic","Chad","Comoros","Congo, Dem.","Congo, Rep.", "Djibouti","Egypt","Equatorial Guinea",
                                        "Eritrea","Ethiopia","Gabon", "Gambia", "Ghana", "Guinea", "Guinea-Bissau", "Kenya", "Lesotho", "Liberia", "Libya", 
                                        "Madagascar", "Malawi", "Mali", "Mauritania","Mauritius", "Morocco", "Mozambique", "Namibia", "Niger", "Nigeria", "Rwanda", 
                                        "Sao Tome/Principe", "Senegal", "Seychelles", "Sierra Leone", "Somalia", "South Africa", "Sudan", "Swaziland", "Tanzania", "Togo", 
                                        "Tunisia", "Uganda", "Zambia","Zimbabwe", "--ANTARCTICA--", "Amundsen-Scott", "=ASIA=","Bangladesh","Bhutan","Brunei","Burma (Myanmar)",
                                        "Cambodia","China","East Timor","India","Indonesia","Japan","Kazakhstan","Korea (north)","Korea (south)","Laos","Malaysia","Maldives","Mongolia",
                                        "Nepal","Philippines","Russian Federation","Singapore","Sri Lanka","Taiwan", "Thailand" ,"Vietnam", "==AUSTRALIA OCEANIA==", "Australia","Fiji",
                                        "Kiribati","Micronesia","Nauru","New Zealand","Palau","Papua New Guinea","Samoa","Tonga","Tuvalu","Vanuatu", "=CARIBBEAN=", "Anguilla","Antigua/Barbuda",
                                        "Aruba","Bahamas","Barbados","Cozumel","Cuba","Dominica","Dominican Republic", "Grenada","Guadeloupe","Haiti", "Jamaica","Martinique","Montserrat",
                                        "Netherlands Antilles","Puerto Rico","St. Barts","St. Kitts/Nevis","St. Lucia","St. Martin/Sint Maarten", "St Vincent/Grenadines","San Andres","Trinidad/Tobago",
                                        "Turks/Caicos", "=CENTRAL AMERICA=", "Belize","Costa Rica","El Salvador","Guatemala","Honduras","Nicaragua","Panama", "=EUROPE=", "Albania","Andorra","Austria","Belarus",
                                        "Belgium","Bosnia-Herzegovina","Bulgaria","Croatia","Czech Republic","Denmark","Estonia","Finland","France", "Georgia","Germany","Greece","Hungary","Iceland","Ireland","Italy",
                                        "Latvia","Liechtenstein","Lithuania","Luxembourg","Macedonia","Malta","Moldova", "Monaco","Netherlands","Norway","Poland","Portugal","Romania","San Marino","Serbia/Montenegro (Yugoslavia)",
                                        "Slovakia","Slovenia","Spain","Sweden", "Switzerland","Ukraine","United Kingdom","Vatican City", "=ISLANDS=", "Arctic Ocean","Atlantic Ocean (North)","Atlantic Ocean (South)","Assorted","Caribbean Sea",
                                        "Greek Isles","Indian Ocean","Mediterranean Sea", "Oceania","Pacific Ocean (North)","Pacific Ocean (South)", "=MIDDLE EAST=","Afghanistan","Armenia","Azerbaijan","Bahrain","Cyprus","Iran","Iraq",
                                        "Israel","Jordan","Kuwait","Kyrgyzstan","Lebanon","Oman","Pakistan", "Qatar","Saudi Arabia","Syria","Tajikistan","Turkey","Turkmenistan","United Arab Emirates","Uzbekistan","Yemen",
                                        "=NORTH AMERICA=","Bermuda","Canada","Greenland","Mexico","United States", "=SOUTH AMERICA=", "Argentina","Bolivia","Brazil","Chile","Colombia","Ecuador","Guyana","Paraguay",
                                        "Peru","Suriname","Uruguay","Venezuela"
      };

      DefaultComboBoxModel WCL = new DefaultComboBoxModel(WorldCountryList);
    
    
    }
/*        
    private void CountryListByContinents(){
       
      String[] Africa = {"Algeria","Angola","Benin","Botswana","Burkina Faso", "Burundi","Cameroon","Cape Verde", "Central African Republic",
                        "Chad","Comoros","Congo, Dem.","Congo, Rep.", "Djibouti","Egypt","Equatorial Guinea", "Eritrea","Ethiopia","Gabon",
                        "Gambia", "Ghana", "Guinea", "Guinea-Bissau", "Kenya", "Lesotho", "Liberia", "Libya", "Madagascar", "Malawi", "Mali",
                        "Mauritania","Mauritius", "Morocco", "Mozambique", "Namibia", "Niger", "Nigeria", "Rwanda", "Sao Tome/Principe", "Senegal",
                        "Seychelles", "Sierra Leone", "Somalia", "South Africa", "Sudan", "Swaziland", "Tanzania", "Togo", "Tunisia", "Uganda", "Zambia","Zimbabwe"
      };
      String[] Antarctica= {"Amundsen-Scott"};
      String[] Asia = {"Bangladesh","Bhutan","Brunei","Burma (Myanmar)","Cambodia","China","East Timor","India","Indonesia","Japan","Kazakhstan","Korea (north)"
                        ,"Korea (south)","Laos","Malaysia","Maldives","Mongolia","Nepal","Philippines","Russian Federation","Singapore","Sri Lanka","Taiwan"
                        , "Thailand" ,"Vietnam"
      };
      String[] Australia_Oceania = {"Australia","Fiji","Kiribati","Micronesia","Nauru","New Zealand","Palau","Papua New Guinea","Samoa","Tonga","Tuvalu","Vanuatu"};
      String[] Caribbean= {"Anguilla","Antigua/Barbuda","Aruba","Bahamas","Barbados","Cozumel","Cuba","Dominica","Dominican Republic", "Grenada","Guadeloupe","Haiti",
                            "Jamaica","Martinique","Montserrat","Netherlands Antilles","Puerto Rico","St. Barts","St. Kitts/Nevis","St. Lucia","St. Martin/Sint Maarten",
                            "St Vincent/Grenadines","San Andres","Trinidad/Tobago","Turks/Caicos"
      };
      String[] Central_America = {"Belize","Costa Rica","El Salvador","Guatemala","Honduras","Nicaragua","Panama"};
      String[] Europe = {"Albania","Andorra","Austria","Belarus","Belgium","Bosnia-Herzegovina","Bulgaria","Croatia","Czech Republic","Denmark","Estonia","Finland","France",
                        "Georgia","Germany","Greece","Hungary","Iceland","Ireland","Italy","Latvia","Liechtenstein","Lithuania","Luxembourg","Macedonia","Malta","Moldova",
                        "Monaco","Netherlands","Norway","Poland","Portugal","Romania","San Marino","Serbia/Montenegro (Yugoslavia)","Slovakia","Slovenia","Spain","Sweden",
                        "Switzerland","Ukraine","United Kingdom","Vatican City"};
      
      String[] Islands = {"Arctic Ocean","Atlantic Ocean (North)","Atlantic Ocean (South)","Assorted","Caribbean Sea","Greek Isles","Indian Ocean","Mediterranean Sea",
                           "Oceania","Pacific Ocean (North)","Pacific Ocean (South)"
      };
      String[] Middle_East = {"Afghanistan","Armenia","Azerbaijan","Bahrain","Cyprus","Iran","Iraq","Israel","Jordan","Kuwait","Kyrgyzstan","Lebanon","Oman","Pakistan",
                                "Qatar","Saudi Arabia","Syria","Tajikistan","Turkey","Turkmenistan","United Arab Emirates","Uzbekistan","Yemen"};
      String[] North_America = {"Bermuda","Canada","Greenland","Mexico","United States"};
      String[] South_America = {"Argentina","Bolivia","Brazil","Chile","Colombia","Ecuador","Guyana","Paraguay","Peru","Suriname","Uruguay","Venezuela"};

    
    DefaultComboBoxModel af = new DefaultComboBoxModel(Africa);
    DefaultComboBoxModel ant = new DefaultComboBoxModel(Antarctica);
    DefaultComboBoxModel as = new DefaultComboBoxModel(Asia);
    DefaultComboBoxModel auoc = new DefaultComboBoxModel(Australia_Oceania);
    DefaultComboBoxModel car = new DefaultComboBoxModel(Caribbean);
    DefaultComboBoxModel cam = new DefaultComboBoxModel(Central_America);
    DefaultComboBoxModel eu = new DefaultComboBoxModel(Europe);
    DefaultComboBoxModel isl = new DefaultComboBoxModel(Islands);
    DefaultComboBoxModel miea = new DefaultComboBoxModel(Middle_East);
    DefaultComboBoxModel nam = new DefaultComboBoxModel(North_America);
    DefaultComboBoxModel sam = new DefaultComboBoxModel(South_America);
    
    
} 

    */
    
  /*  public void DoB_Controller(){
        
        if (date >= 1 && date <= 28){
            month.add("February");
            day.removeElement()
        }
    }
   */ 
} 
    