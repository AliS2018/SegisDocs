/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package segistelui_mt;

import java.sql.Timestamp;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.CaretListener;
 
/**
 *
 * @author AliS2019
 */
public class SegistelUI extends javax.swing.JFrame implements ActionListener, FocusListener, MouseListener, KeyListener {

     public String[] getAllCountries() {
        String[] countries = new String[Locale.getISOCountries().length];
        String[] countryCodes = Locale.getISOCountries();
        for (int i = 0; i < countryCodes.length; i++) {
            Locale obj = new Locale("", countryCodes[i]);
            countries[i] = obj.getDisplayCountry(Locale.forLanguageTag("es-ES-x-lvariant-POSIX")).toUpperCase();
        }
        return countries;
    }
    public List<String> getAllMonths(){
        Calendar cal = Calendar.getInstance();
        Map<String, Integer> map = cal.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.forLanguageTag("es-ES-x-lvariant-POSIX"));
        
        List<String> MTR = new ArrayList<>(map.keySet());
        Collections.sort(MTR);
        
        return MTR;
       
    } 
      //Arrays to Populate ComboBoxes
    public void populateYears(){
 
    ArrayList<String> years_tmp = new ArrayList<>();
    for(int years = 1900; years<=Calendar.getInstance().get(Calendar.YEAR); years++) {
            years_tmp.add(years+"");
        }
    DefaultComboBoxModel dm = new DefaultComboBoxModel(years_tmp.toArray());
    year.setModel(dm);
    }

    //Various Algorithms + Methods
  @SuppressWarnings("UnusedAssignment")  
    private void doc_composer(){
      String NatField = nationality_field.getText();
      
      if(NatField.equals("ESPAÑA") || NatField.equals("ESPANA") && isTourist.getText().equals("Tourist")){
          doc_letter_combo.setEnabled(false);
          doc_letter_combo.setVisible(false);
          doc_letter_combo.setSelectedIndex(0);
          imageSwitch();
      }else if (!NatField.equals("ESPANA") && isTourist.getText().equals("Resident")){
          doc_letter_combo.setEnabled(false);
          doc_letter_combo.setVisible(false);
          imageSwitch();
      }else{
          doc_letter_combo.setEnabled(true);
          doc_letter_combo.setVisible(true);
          imageSwitch();
      }
            
  }
    //Govt Issued ID letter Calculating Method
  
    private String NIFCalculator(int id_loc){
        //Declare Constants (if needed)
        final int TOTALCHARACTERS = 23;
        // String Variables
        String letterList= "TRWAGMYFPDXBNJZSQVHLCKE";
        String num_concatenation;
        String letterCombo;
        // Integer Variables
        int module;
        int setNumber, converter;
        // Character Variables
        char letter_char;
        // Getting necessary letter from combox (NIE-type only)
        letterCombo = doc_letter_combo.getSelectedItem().toString();
        

        //Conditional for the combox
        if(letterCombo.equals("X")){
            setNumber = 0;
        }else if (letterCombo.equals("Y")){
            setNumber = 1; 
        }else{
            setNumber = 2;
        }
        
        //Concatenating 2 different variables into 1
        num_concatenation ="" + setNumber + id_loc;
        // Converting the string to Integer for the math part
        converter = Integer.parseInt(num_concatenation);
        
        // Checking the length of the passing-through variable 
        
        if(id_loc == 8){
           module = id_loc % TOTALCHARACTERS; 
        }else{
           module = converter % TOTALCHARACTERS;
        }
         
        letter_char = letterList.charAt(module);
        
        String result = letter_char + "";
        
        return result;
  }  


    //Character Translator, sets any available db letters to full fledged name (i.e Male or Female)
    private String CT_sex(String sex){
        String result;
        if(sex.equals("V")){
         sex_combo.setSelectedIndex(1);   
         result = sex_combo.getSelectedItem().toString();
        }else{
         sex_combo.setSelectedIndex(2);
         result = sex_combo.getSelectedItem().toString();
        }
        return result;
    }
    
    //Converting MM to full fledged Month
    private String CV_months(String MONTH){
        String result = month.getSelectedItem().toString();
        month_field.setText(result);
        switch (MONTH) {
          case "01" ->{
              month.setSelectedIndex(1); 
          }   
          case "02" ->{
              month.setSelectedIndex(2);
          }
          case "03" ->{ 
              month.setSelectedIndex(3);              
          }
          case "04" ->{
              month.setSelectedIndex(4);
          }   
          case "05" ->{
              month.setSelectedIndex(5);
          }
          case "06" ->{
              month.setSelectedIndex(6);             
          }
          case "07" ->{
              month.setSelectedIndex(7);
          }
          case "08" ->{
              month.setSelectedIndex(8);
          }
          case "09" ->{
              month.setSelectedIndex(9);
          }
          case "10" ->{
              month.setSelectedIndex(10);
          }
          case "11" ->{
              month.setSelectedIndex(11);
          }   
          case "12" ->{
              month.setSelectedIndex(12);
          }
          default ->{
              month.setSelectedIndex(0);
          }
    }
         return result;   
    }
    // Just a beauty for eyes, a small image switcher depending on a state app result is in.
    private void imageSwitch(){
        //location for each image
        String OFF = "/segistelui_mt/icons/placeholder_obj/0.png";
        String NIF = "/segistelui_mt/icons/placeholder_obj/1.png";
        String NIE = "/segistelui_mt/icons/placeholder_obj/2.png";
        String CIF = "/segistelui_mt/icons/placeholder_obj/3.png";
        String Pasaporte = "/segistelui_mt/icons/placeholder_obj/4.png";
        // get Text from Nationality Field
        String NF_SR = nationality_field.getText();
        // get Text From Button
        String getTFB = isTourist.getText();
        
        if(!getTFB.equals("Tourist")){
            image_controller.setIcon(new ImageIcon(getClass().getResource(Pasaporte)));
        }else if (NF_SR.equals("ESPAÑA")|| NF_SR.equals("ESPANA")){
            image_controller.setIcon(new ImageIcon(getClass().getResource(NIF)));
        }else if (getTFB.equals("Tourist")){
            image_controller.setIcon(new ImageIcon(getClass().getResource(NIE)));
        }else{
            image_controller.setIcon(new ImageIcon(getClass().getResource(OFF)));
        }
        
    }
    //Timestamps declaration
    private static final SimpleDateFormat currentdate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  
    // Application objects state (ON/OFF switch basically)
    public void signup_TRUE() {
        head_label.setText("New User");
        tabFeatures.setSelectedIndex(1);

        save.setEnabled(true);
        cancel.setEnabled(true);
        name_field.setEnabled(true);
        name2_field.setEnabled(true);
        surname_field.setEnabled(true);
        surname_2_field.setEnabled(true);
        day_field.setEnabled(true);
        month_field.setEnabled(true);
        year_field.setEnabled(true);
        nationality_field.setEnabled(true);
        id_local_field.setEnabled(true);
        sex_field.setEnabled(true);
        fax_field.setEnabled(true);
        email_field.setEnabled(true);
        email_addition.setEnabled(true);
        phone_field.setEnabled(true);
        addit_phone_field.setEnabled(true);
        sex_combo.setEnabled(true);
        day.setEnabled(true);
        month.setEnabled(true);
        year.setEnabled(true);
        country_array.setEnabled(true);
        doc_letter_combo.setEnabled(true);
        
        // 2 page
        via_combo.setEnabled(true);
        address_field.setEnabled(true);
        number_field.setEnabled(true);
        apt_field.setEnabled(true);
        door_field.setEnabled(true);
        staircase_field.setEnabled(true);
        block_field.setEnabled(true);
        city_field.setEnabled(true);
        city_combo.setEnabled(true);
        postalcode_field.setEnabled(true);
                
    }
    public void signup_FALSE() {
        head_label.setText("SegisDocs");
        tabFeatures.setSelectedIndex(0);
        save.setEnabled(false);
        cancel.setEnabled(false);
        name_field.setEnabled(false);
        name2_field.setEnabled(false);
        surname_field.setEnabled(false);
        surname_2_field.setEnabled(false);
        day_field.setEnabled(false);
        month_field.setEnabled(false);
        year_field.setEnabled(false);
        nationality_field.setEnabled(false);
        id_local_field.setEnabled(false);
        sex_field.setEnabled(false);
        fax_field.setEnabled(false);
        email_field.setEnabled(false);
        email_addition.setEnabled(false);
        phone_field.setEnabled(false);
        addit_phone_field.setEnabled(false);
        sex_combo.setEnabled(false);
        day.setEnabled(false);
        month.setEnabled(false);
        year.setEnabled(false);
        country_array.setEnabled(false);
        doc_letter_combo.setEnabled(false);
    }
    public void DB_Viewer(){
        name_field.setEnabled(true);
        name_field.setEditable(false);
        
        name2_field.setEnabled(true);
        name2_field.setEditable(false);
        
        surname_field.setEnabled(true);
        surname_field.setEditable(false);
        
        surname_2_field.setEnabled(true);
        surname_2_field.setEditable(false);
        
        day_field.setEnabled(true);
        day_field.setEditable(false);
        
        month_field.setEnabled(true);
        month_field.setEditable(false);
        
        year_field.setEnabled(true);
        year_field.setEditable(false);
        
        sex_field.setEnabled(true);
        sex_field.setEditable(false);
        
        nationality_field.setEnabled(true);
        nationality_field.setEditable(false);

        id_local_field.setEnabled(true);
        id_local_field.setEditable(false);
        
        email_field.setEnabled(true);
        email_field.setEditable(false);
        
        email_addition.setEnabled(true);
        email_addition.setEditable(false);
        
        phone_field.setEnabled(true);
        phone_field.setEditable(false);
        
        addit_phone_field.setEnabled(true);
        addit_phone_field.setEditable(false);
        
        fax_field.setEnabled(true);
        fax_field.setEditable(false);
           
        address_field.setEnabled(true);
        address_field.setEditable(false);
        
        number_field.setEnabled(true);
        number_field.setEditable(false);
        
        apt_field.setEnabled(true);
        apt_field.setEditable(false);
                
        door_field.setEnabled(true);
        door_field.setEditable(false);
        
        staircase_field.setEnabled(true);
        staircase_field.setEditable(false);
        
               
    }
    // Translation from XBase to SQL, DB Queries, etc
    public void translate()  {
        try {            
            Connection dbf_connector = null;
            Class.forName("com.dbschema.xbase.DbfJdbcDriver");
            dbf_connector = DriverManager.getConnection("jdbc:dbschema:dbf:/Users/AliS2019/NetBeansProjects/SegistelUI_MT/FxFinal");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SegistelUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void connect(){    
        try{ 
        Class.forName("org.h2.Driver");    
        Connection converter= DriverManager.getConnection("jdbc:h2:file:~/.DbSchema/jdbc-dbf-cache/11c2048b532236b589ff0bd3ce8ec94e;database_to_upper=false");
       // System.out.println("Connecting to the DB...");
       // System.out.println("Creating Statement for Execution...");
        Statement st = converter.createStatement();
        
        String fieldToText = NIF_FIELD.getText().toUpperCase().trim();
        String SQL = ("SELECT * FROM \"dblist_4app\" WHERE \"CODICLIENT\" = '" + fieldToText + "'");        
      //  st.executeUpdate(SQL);
     
        ResultSet rs = st.executeQuery(SQL);
        
        System.out.println("Waiting for results...");  
        if (rs.next()) { 
           // "CODICLIENT" decimal(6,0),
            String nombre = rs.getString("NOMBRE");
            String nombre_2 = rs.getString("NOMBRE2");
            String apellidos = rs.getString("APELLIDOS");
            String apellidos_2 = rs.getString("APELLIDO2");
            String via = rs.getString("VIA");
            String direccion = rs.getString("DIRECCION");
            String bloque = rs.getString("BLOQUE");
            String escalera = rs.getString("ESCALERA");
            String numero = rs.getString("NUMERO");
            String piso = rs.getString("PISO");
            String puerta = rs.getString("PUERTA");
            String domicilio = rs.getString("DOMICILIO");
            String poblacion = rs.getString("POBLACION");
            String cpost = rs.getString("CPOSTAL");
            String provincia = rs.getString("PROVINCIA");
            String tiponif = rs.getString("TIPONIF");
            String nif = rs.getString("NIF");
            String nacionalid = rs.getString("NACIONALID");
            String sexo = rs.getString("SEXO");
            String fechanac = rs.getString("FECHANAC");
            String telefono = rs.getString("TELEFONO");
            String telefono2 = rs.getString("TELEFONO2");
            String fax = rs.getString("FAX");
	
            int codiclient = rs.getInt("CODICLIENT"); 
            
            // Display values 
           
            name_field.setText(nombre); 
            name_field.setForeground(new Color(255, 255, 255));
            
            name2_field.setText(nombre_2 + "");
            name2_field.setForeground(new Color(255, 255, 255));
            
            surname_field.setText(apellidos + ""); 
            surname_field.setForeground(new Color(255, 255, 255));
            
            surname_2_field.setText(apellidos_2 + "");
            surname_2_field.setForeground(new Color(255, 255, 255));
            
            nationality_field.setText(nacionalid.toUpperCase() + "");
            nationality_field.setForeground(new Color(255,255,255));
            
            String NUMBERS = nif.substring(1,8);
            String isLetter = nif.substring(0,1).toUpperCase();
            
            if(isLetter.equals("X")|| isLetter.equals("Y")|| isLetter.equals("Z")){
                doc_letter_combo.setSelectedItem(isLetter);
            }
            id_local_field.setText(NUMBERS + "");            
            id_local_field.setForeground(new Color(153, 153, 153));
            
            String YEAR = fechanac.substring(0,4);
            String MONTH = fechanac.substring(5,7);
            String DAY = fechanac.substring(8,10);
                     
            day_field.setText(DAY);         
            day.setSelectedItem(DAY);
            year_field.setText(YEAR);
            
            via_combo.setSelectedItem(via);
            address_field.setText(direccion);
            number_field.setText(numero);
            apt_field.setText(piso);
            door_field.setText(puerta);
            
            phone_field.setText(telefono);
            addit_phone_field.setText(telefono2);
            fax_field.setText(fax);
            
            CT_sex(sexo);
            CV_months(MONTH);
            DB_Viewer();
            head_label.setText(nombre + " " + nombre_2 + " " + apellidos + " " + apellidos_2 + " ");
            System.out.println("Done!");
         }             
    }catch(SQLException ex){
        
        } catch (ClassNotFoundException ex) {  
          Logger.getLogger(SegistelUI.class.getName()).log(Level.SEVERE, null, ex);
      }  
  
}
   
    /* NAME PLACEHOLDER FOCUSES */
    public void NPlace_FG() {
        if (name_field.getText().trim().equals("Nombre")) {
            name_field.setText("");
            name_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void NPlace_FL() {
        if (name_field.getText().trim().equals("")) {
            name_field.setText("Nombre");
            name_field.setForeground(new Color(153, 153, 153));
        }
    }
    /* NAME 2 PLACEHOLDER FOCUSES */
    public void NPlace_2_FG() {
        if (name2_field.getText().trim().equals("Nombre 2")) {
            name2_field.setText("");
            name2_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void NPlace_2_FL() {
        if (name2_field.getText().equals("")) {
            name2_field.setText("Nombre 2");
            name2_field.setForeground(new Color(153, 153, 153));
        }       
    }
    
    /* LASTNAME/SURNAME PLACEHOLDER FOCUSES */
    public void LNPlace_FG() {
        if (surname_field.getText().trim().equals("Apellidos")) {
            surname_field.setText("");
            surname_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void LNPlace_FL() {
        if (surname_field.getText().equals("")) {
            surname_field.setText("Apellidos");
            surname_field.setForeground(new Color(153, 153, 153));
        }else{
            if(surname_field.getText().equals(" ")){
                surname_field.setText(" ");
            }
        }
    }
    /* LASTNAME/SURNAME 2 PLACEHOLDER FOCUSES */
    public void LNPlace_2_FG() {
        if (surname_2_field.getText().trim().equals("Apellidos 2")) {
            surname_2_field.setText("");
            surname_2_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void LNPlace_2_FL() {
        if (surname_2_field.getText().trim().equals("") || surname_2_field.getText().isEmpty()) {
            surname_2_field.setText("Apellidos 2");
            surname_2_field.setForeground(new Color(153, 153, 153));
        }
    }

    /* TELEPHONE 1 PLACEHOLDER FOCUSES */
    public void PFPlace_FG() {
        if (phone_field.getText().trim().equals("Numero de Telefono")) {
            phone_field.setText("");
            phone_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void PFPlace_FL() {
        if (phone_field.getText().trim().equals("")) {
            phone_field.setText("Numero de Telefono");
            phone_field.setForeground(new Color(153, 153, 153));
        }
    }
    /* TELEPHONE 2 PLACEHOLDER FOCUSES */
    public void PF2Place_FG() {
        if (addit_phone_field.getText().trim().equals("Numero de Telefono Adicional")) {
            addit_phone_field.setText("");
            addit_phone_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void PF2Place_FL() {
        if (addit_phone_field.getText().trim().equals("")) {
            addit_phone_field.setText("Numero de Telefono Adicional");
            addit_phone_field.setForeground(new Color(153, 153, 153));
        }
    }
    /* FAX PLACEHOLDER FOCUSES */
    public void FXPlace_FG() {
        if (fax_field.getText().trim().equals("Fax")) {
            fax_field.setText("");
            fax_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void FXPlace_FL() {
        if (fax_field.getText().trim().equals("")) {
            fax_field.setText("Fax");
            fax_field.setForeground(new Color(153, 153, 153));
        }
    }

    /* EMAIL PLACEHOLDER FOCUSES */ 
    public void EFPlace_FG() {
        if (email_field.getText().trim().equals("Correo Electronico (Email)")) {
            email_field.setText("");
            email_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void EFPlace_FL() {
        if (email_field.getText().trim().equals("")) {
            email_field.setText("Correo Electronico (Email)");
            email_field.setForeground(new Color(153, 153, 153));
        }
    }
    /* EMAIL 2 PLACEHOLDER FOCUSES */ 
    public void EF2Place_FG() {
        if (email_addition.getText().trim().equals("Correo Electronico Adicional")) {
            email_addition.setText("");
            email_addition.setForeground(new Color(255, 255, 255));
        }
    }
    public void EF2Place_FL() {
        if (email_addition.getText().trim().equals("")) {
            email_addition.setText("Correo Electronico Adicional");
            email_addition.setForeground(new Color(153, 153, 153));
        }
    }
    
    /* NIF PLACEHOLDER FOCUSES */ 
    public void NIFPlace_FG() {
        if (NIF_FIELD.getText().trim().equals("CIF | NIF | NIE | PASSPORT")) {
            NIF_FIELD.setText("");
            NIF_FIELD.setForeground(new Color(255, 255, 255));
        }
    }
    public void NIFPlace_FL() {
        if (NIF_FIELD.getText().trim().equals("")) {
            NIF_FIELD.setText("CIF | NIF | NIE | PASSPORT");
            NIF_FIELD.setForeground(new Color(153, 153, 153));
        }
    }
    /* DOCUMENT NUMBER MAIN FOCUSES */
    public void LocalFPlace_FG(){
        String ilfield = id_local_field.getText().trim();
        
        if(ilfield.equals("Num. documento") || ilfield.equals("Introduzca su numero de pasaporte")){
            id_local_field.setText("");
            id_local_field.setBackground(new Color(0,0,0));
            id_local_field.setForeground(new Color(255,255,255));
        }
    }
    public void LocalFPlace_FL(){
        String ilfield = id_local_field.getText().trim();
        
        if(ilfield.equals("")){
            if(isTourist.getText().equals("Tourist")){
                id_local_field.setText("Num. documento");
                id_local_field.setBackground(new Color(0,0,0));
            }else{
                id_local_field.setText("Introduzca su numero de pasaporte");
                id_local_field.setBackground(new Color(0,0,0));
            }
            id_local_field.setForeground(new Color(153,153,153));
        }
    }
   
    /* STREET ADDRESS FIELD FOCUSES */
    public void ADPlace_FG(){
        String GetPlace = address_field.getText().trim();
        
        if(GetPlace.equals("DIRECCION")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void ADPlace_FL(){
        String GetPlace = address_field.getText().trim();
        if(GetPlace.equals("")){
            address_field.setText("DIRECCION");
            address_field.setForeground(new Color(153,153,153));
            
        }
    }
   
    /* NUMBER FIELD FOCUSES */
    public void NUFPlace_FG(){
     String GetPlace = number_field.getText().trim();
     if(GetPlace.equals("NUMERO")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void NUFPlace_FL(){
     String GetPlace = number_field.getText().trim();
      if(GetPlace.equals("")){
            address_field.setText("NUMERO");
            address_field.setForeground(new Color(153,153,153));
        }
    }
    
    /* FLOOR FIELD FOCUSES */
    public void FLFPlace_FG(){
     String GetPlace = apt_field.getText().trim();   
     if(GetPlace.equals("PISO")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void FLFPlace_FL(){
     String GetPlace = apt_field.getText().trim();      
     if(GetPlace.equals("")){
            address_field.setText("PISO");
            address_field.setForeground(new Color(153,153,153));
     }
    }
    
    /* DOOR FIELD FOCUSES */
    public void DORPlace_FG(){
     String GetPlace = door_field.getText().trim();   
     if(GetPlace.equals("PUERTA")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void DORPlace_FL(){
     String GetPlace = door_field.getText().trim();    
        if(GetPlace.equals("")){
            address_field.setText("PUERTA");
            address_field.setForeground(new Color(153,153,153));
     }     
    }
    
    /* STAIRCASE FIELD FOCUSES */
    public void StairFPlace_FG(){
     String GetPlace = staircase_field.getText().trim();         
     if(GetPlace.equals("ESC.")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void StairFPlace_FL(){
     String GetPlace = staircase_field.getText().trim(); 
       if(GetPlace.equals("")){
            address_field.setText("ESC.");
            address_field.setForeground(new Color(153,153,153));
     }
    }
    
    /* BLOCK FIELD FOCUSES */
    public void BKFPlace_FG(){
     String GetPlace = block_field.getText().trim(); 
     if(GetPlace.equals("BLOQUE")){
            address_field.setText("");
            address_field.setForeground(new Color(255, 255, 255));
        }
    }
    public void BKFPlace_FL(){
     String GetPlace = block_field.getText().trim();  
            if(GetPlace.equals("")){
            address_field.setText("BLOQUE");
            address_field.setForeground(new Color(153,153,153));
     }
    }
   
    //MISC APP CONTROLS

    enum Browser {
        Chrome("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "https://google.com/chrome"),
        Firefox("C:\\Program Files\\Mozilla Firefox\\firefox.exe",
                "https://www.mozilla.org/en-GB/firefox/download/thanks/"),
        IExplorer("C:\\Program Files (x86)\\Internet Explorer\\iexplore.exe",
                "https://support.microsoft.com/en-us/windows/internet-explorer-downloads-d49e1f0d-571c-9a7b-d97e-be248806ca70"),
        Edge("C:\\Windows\\SystemApps\\Microsoft.MicrosoftEdge_8wekyb3d8bbwe\\MicrosoftEdge.exe",
                "https://www.microsoft.com/en-us/edge"),
        AAcrobat("C:\\Program File (x86)\\ Adobe\\ Adobe Acrobat 2021\\aeacr.exe", "https://adobe.com/acrobat");

        private final String _filePath, _downloadPageURL;

        Browser(String filePath, String downloadPage) {
            _filePath = filePath;
            _downloadPageURL = downloadPage;
        }

        private String getDownloadPageURL() {
            return _downloadPageURL;
        }

        private String getFilePath() {
            return _filePath;
        }

        public boolean exists() {
            return new File(getFilePath()).exists();
        }

        public void openDownloadPage() {
            try {
                Desktop.getDesktop().browse(new URI(getDownloadPageURL()));
            } catch (IOException | URISyntaxException e) {
                System.err.println(e);
            }
        }
    }
    private void OSInfo(){
        
        String nameOS = System.getProperty("os.name");
        String versionOS = System.getProperty("os.version") ;  
        String architectureOS = System.getProperty("os.arch");
        String JVM_version = System.getProperty("java.version");
    

            
        if(nameOS.equals("Mac OS X") || nameOS.equals("Linux")){
            
            os_1.setText(nameOS + " (Unsupported)");
            os_1.setForeground(Color.RED);
            // OS Name
            os_2.setText(versionOS);
            // Architecture Type
            os_3.setText(architectureOS);
            // Java Version
            
            if(JVM_version.startsWith("14") && nameOS.equals("mac OS") || nameOS.equals("Mac OS X")){
                os_4.setText(JVM_version.substring(0,4) + " (Native JVM)");
            }else{
                System.err.println("This java version is not compatible with this app!");
            }
                os_4.setForeground(Color.GREEN);
        
        }else{
            // OS Name
            os_1.setText(nameOS + " (Supported)");
            os_1.setForeground(Color.GREEN);
            // OS Version
            os_2.setText(versionOS);
            // Architecture Type
            os_3.setText(architectureOS);
            // Java Version
            os_4.setText(JVM_version.substring(0,4) + " (Non-Native JVM)");
        }
    }  
    private File Warning_MSG(File lsd){
    int answer = JOptionPane.showConfirmDialog(header,
    "Make sure the software you are about to launch is signed and verified",
    "You are about to start an External Application!",
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.WARNING_MESSAGE);
        switch (answer) {
            case JOptionPane.YES_OPTION:
             {
                 try{
                     Desktop.getDesktop().open(lsd);
                 }catch (IOException e){
                     System.out.println("Error");
                 }
             }
            break; 
            case JOptionPane.NO_OPTION: System.out.println("Action Aborted by User...");
            break;
            
            default: System.err.printf("Unknown Error | | TIMESTAMP -- >"+ currentdate.format(timestamp));
        
        }
        return lsd;
    } 
    private void ClearAllFields(){
        name_field.setText("");
        name2_field.setText("");
        surname_field.setText("");
        surname_2_field.setText("");
        email_field.setText("");
        email_addition.setText("");
        phone_field.setText("");
        addit_phone_field.setText("");
        fax_field.setText("");
        nationality_field.setText("");       
        sex_combo.setSelectedIndex(0);
        sex_field.setText("");   
        country_array.setSelectedIndex(0);
        day.setSelectedIndex(0);
        month.setSelectedIndex(0);
        year.setSelectedIndex(0);
        nationality_field.setText("");
        id_local_field.setText("");
        letter_result.setText("");
    }
    
    /**
     * Creates new form SegistelUI
     */
    public SegistelUI() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        about_us_frame = new javax.swing.JFrame();
        body_aus = new javax.swing.JPanel();
        logo_aus = new javax.swing.JLabel();
        close_action = new javax.swing.JButton();
        sv_label = new javax.swing.JLabel();
        comment_label = new javax.swing.JLabel();
        update_app = new javax.swing.JButton();
        OStype = new javax.swing.JLabel();
        os_1 = new javax.swing.JLabel();
        os_3 = new javax.swing.JLabel();
        os_2 = new javax.swing.JLabel();
        os_4 = new javax.swing.JLabel();
        header_aus1 = new javax.swing.JPanel();
        aus_head_label = new javax.swing.JLabel();
        header_aus2 = new javax.swing.JPanel();
        footnote = new javax.swing.JLabel();
        header = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        head_label = new javax.swing.JLabel();
        logo_head = new javax.swing.JLabel();
        body = new javax.swing.JPanel();
        Search_Engine = new javax.swing.JPanel();
        infolabel = new javax.swing.JLabel();
        NIF_FIELD = new javax.swing.JTextField();
        GO = new javax.swing.JButton();
        ea_head_label = new javax.swing.JPanel();
        head_label1 = new javax.swing.JLabel();
        tabFeatures = new javax.swing.JTabbedPane();
        homepage = new javax.swing.JPanel();
        welcome_sign = new javax.swing.JLabel();
        welcome_description = new javax.swing.JLabel();
        navbar = new javax.swing.JPanel();
        about_us = new javax.swing.JButton();
        signup_bd_pane = new javax.swing.JButton();
        localised = new javax.swing.JButton();
        edit_data = new javax.swing.JButton();
        pers_info = new javax.swing.JScrollPane();
        personal_info = new javax.swing.JPanel();
        personal_info_label = new javax.swing.JLabel();
        underline = new javax.swing.JSeparator();
        picture_frame = new javax.swing.JPanel();
        add_picture = new javax.swing.JButton();
        clear_field = new javax.swing.JButton();
        general_info = new javax.swing.JPanel();
        name_field = new javax.swing.JTextField();
        name1_label = new javax.swing.JLabel();
        name2_field = new javax.swing.JTextField();
        name2_label = new javax.swing.JLabel();
        surname_field = new javax.swing.JTextField();
        surname_label = new javax.swing.JLabel();
        surname_2_field = new javax.swing.JTextField();
        surname2_label = new javax.swing.JLabel();
        dob_label = new javax.swing.JLabel();
        day = new javax.swing.JComboBox<>();
        day_label = new javax.swing.JLabel();
        month = new javax.swing.JComboBox<>();
        month_combo_label = new javax.swing.JLabel();
        year = new javax.swing.JComboBox<>();
        year_combo_label = new javax.swing.JLabel();
        sex_combo = new javax.swing.JComboBox<>();
        sex_field = new javax.swing.JTextField();
        sex_label1 = new javax.swing.JLabel();
        month_field = new javax.swing.JTextField();
        day_field = new javax.swing.JTextField();
        year_field = new javax.swing.JTextField();
        geo_info = new javax.swing.JPanel();
        nat_label = new javax.swing.JLabel();
        nationality_field = new javax.swing.JTextField();
        country_array = new javax.swing.JComboBox<>(getAllCountries());
        id_label = new javax.swing.JLabel();
        doc_letter_combo = new javax.swing.JComboBox<>();
        id_local_field = new javax.swing.JTextField();
        letter_result = new javax.swing.JTextField();
        image_controller = new javax.swing.JLabel();
        contact_form = new javax.swing.JPanel();
        email_label = new javax.swing.JLabel();
        email_field = new javax.swing.JTextField();
        email_addition = new javax.swing.JTextField();
        email_label1 = new javax.swing.JLabel();
        phone_label = new javax.swing.JLabel();
        phone_field = new javax.swing.JTextField();
        addit_phone_field = new javax.swing.JTextField();
        phone2_label = new javax.swing.JLabel();
        fax_field = new javax.swing.JTextField();
        sex_label = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        isTourist = new javax.swing.JToggleButton();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        home_addr_pane = new javax.swing.JPanel();
        home_add_head_label = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        country_ctrl = new javax.swing.JComboBox<>();
        postalcode_field = new javax.swing.JTextField();
        postal_code_label = new javax.swing.JLabel();
        country_id = new javax.swing.JTextField();
        city_field = new javax.swing.JTextField();
        city_combo = new javax.swing.JComboBox<>();
        postal_code_label1 = new javax.swing.JLabel();
        main_address = new javax.swing.JPanel();
        via_combo = new javax.swing.JComboBox<>();
        via_label = new javax.swing.JLabel();
        address_field = new javax.swing.JTextField();
        address_label = new javax.swing.JLabel();
        number_field = new javax.swing.JTextField();
        address_number = new javax.swing.JLabel();
        exact_home = new javax.swing.JPanel();
        apt_field = new javax.swing.JTextField();
        door_field = new javax.swing.JTextField();
        staircase_field = new javax.swing.JTextField();
        block_field = new javax.swing.JTextField();
        via_label1 = new javax.swing.JLabel();
        via_label2 = new javax.swing.JLabel();
        via_label3 = new javax.swing.JLabel();
        via_label4 = new javax.swing.JLabel();
        main_app_scroller = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        internet_brs = new javax.swing.JScrollPane();
        internal_internet_pane = new javax.swing.JPanel();
        chrome = new javax.swing.JButton();
        firefox = new javax.swing.JButton();
        ie = new javax.swing.JButton();
        edge = new javax.swing.JButton();
        browsers_label = new javax.swing.JLabel();
        separation1 = new javax.swing.JSeparator();
        office_label = new javax.swing.JLabel();
        separation2 = new javax.swing.JSeparator();
        office_pane_main = new javax.swing.JScrollPane();
        office_pane_secondary = new javax.swing.JPanel();
        App = new javax.swing.JButton();
        App1 = new javax.swing.JButton();
        App2 = new javax.swing.JButton();
        App3 = new javax.swing.JButton();
        office_pane_main1 = new javax.swing.JScrollPane();
        office_pane_secondary1 = new javax.swing.JPanel();
        App4 = new javax.swing.JButton();
        App5 = new javax.swing.JButton();
        App6 = new javax.swing.JButton();
        App7 = new javax.swing.JButton();
        separation3 = new javax.swing.JSeparator();
        office_label1 = new javax.swing.JLabel();
        footer = new javax.swing.JPanel();
        copyright_label = new javax.swing.JLabel();
        sign_up_foot = new javax.swing.JButton();
        login = new javax.swing.JButton();
        about_us_show = new javax.swing.JButton();

        about_us_frame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        about_us_frame.setTitle("About Us");
        about_us_frame.setName("about_us_frame"); // NOI18N
        about_us_frame.setResizable(false);
        about_us_frame.setSize(new java.awt.Dimension(350, 560));

        body_aus.setBackground(new java.awt.Color(153, 153, 153));
        body_aus.setName("body_aus"); // NOI18N

        logo_aus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo_aus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/logo128x128.png"))); // NOI18N
        logo_aus.setName("logo_aus"); // NOI18N

        close_action.setBackground(new java.awt.Color(255, 51, 51));
        close_action.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        close_action.setForeground(new java.awt.Color(204, 204, 204));
        close_action.setText("Close");
        close_action.setName("close_action"); // NOI18N
        close_action.addMouseListener(this);

        sv_label.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        sv_label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sv_label.setText("<html>\n<p style=\"list-style-type: none; text-decoration=none; font-weight=bold;\">Software version: =>1.3.16.0: 51050 </p>\n</html>\n");
        sv_label.setName("sv_label"); // NOI18N

        comment_label.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        comment_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        comment_label.setText("<html>\n\n<p style=\"list-style-type: none; text-decoration=none;\">This piece of software was developed in Java. Additional drivers are used to run this software</p>\n\n</html>");
        comment_label.setName("comment_label"); // NOI18N

        update_app.setBackground(new java.awt.Color(0, 153, 51));
        update_app.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        update_app.setForeground(new java.awt.Color(204, 204, 204));
        update_app.setText("Update");
        update_app.setName("update_app"); // NOI18N
        update_app.addMouseListener(this);

        OStype.setFont(new java.awt.Font("PingFang TC", 1, 13)); // NOI18N
        OStype.setText("<html> <p style=\"margin-right: 20px;\" > OS Type:</p> <p style=\"margin-right: 20px;\"> OS Version:</p> <p style=\"margin-right: 20px;\"> OS Arch: </p><p style=\"margin-right: 20px;\"> Java Version: </p>");
        OStype.setName("OStype"); // NOI18N

        os_1.setText("1");
        os_1.setName("os_1"); // NOI18N

        os_3.setText("1");
        os_3.setName("os_3"); // NOI18N

        os_2.setText("1");
        os_2.setName("os_2"); // NOI18N

        os_4.setText("1");
        os_4.setName("os_4"); // NOI18N

        javax.swing.GroupLayout body_ausLayout = new javax.swing.GroupLayout(body_aus);
        body_aus.setLayout(body_ausLayout);
        body_ausLayout.setHorizontalGroup(
            body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logo_aus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(body_ausLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sv_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comment_label, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(body_ausLayout.createSequentialGroup()
                        .addGroup(body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(OStype, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(close_action, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(update_app, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(os_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(os_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(os_2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(os_4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        body_ausLayout.setVerticalGroup(
            body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(body_ausLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(logo_aus)
                .addGap(24, 24, 24)
                .addComponent(sv_label, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comment_label, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(body_ausLayout.createSequentialGroup()
                        .addComponent(os_1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(os_2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(os_3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(os_4))
                    .addComponent(OStype, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(body_ausLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(close_action, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update_app, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        header_aus1.setBackground(new java.awt.Color(51, 51, 51));
        header_aus1.setName("header_aus1"); // NOI18N

        aus_head_label.setFont(new java.awt.Font("Montserrat", 1, 40)); // NOI18N
        aus_head_label.setForeground(new java.awt.Color(255, 255, 255));
        aus_head_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aus_head_label.setText("About Us");
        aus_head_label.setName("aus_head_label"); // NOI18N

        javax.swing.GroupLayout header_aus1Layout = new javax.swing.GroupLayout(header_aus1);
        header_aus1.setLayout(header_aus1Layout);
        header_aus1Layout.setHorizontalGroup(
            header_aus1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, header_aus1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aus_head_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        header_aus1Layout.setVerticalGroup(
            header_aus1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header_aus1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(aus_head_label)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        header_aus2.setBackground(new java.awt.Color(51, 51, 51));
        header_aus2.setName("header_aus2"); // NOI18N

        footnote.setFont(new java.awt.Font("Montserrat", 0, 10)); // NOI18N
        footnote.setForeground(new java.awt.Color(255, 255, 255));
        footnote.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        footnote.setText("\nCopyright © 2021 JM Segistel SCP & Media Archive Ltd. All rights reserved.\n");
        footnote.setName("footnote"); // NOI18N

        javax.swing.GroupLayout header_aus2Layout = new javax.swing.GroupLayout(header_aus2);
        header_aus2.setLayout(header_aus2Layout);
        header_aus2Layout.setHorizontalGroup(
            header_aus2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footnote, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        header_aus2Layout.setVerticalGroup(
            header_aus2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footnote, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout about_us_frameLayout = new javax.swing.GroupLayout(about_us_frame.getContentPane());
        about_us_frame.getContentPane().setLayout(about_us_frameLayout);
        about_us_frameLayout.setHorizontalGroup(
            about_us_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header_aus2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(header_aus1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body_aus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        about_us_frameLayout.setVerticalGroup(
            about_us_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(about_us_frameLayout.createSequentialGroup()
                .addComponent(header_aus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(body_aus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(header_aus2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Segistel Management Tool");
        setName("Main Frame"); // NOI18N
        setResizable(false);

        header.setBackground(new java.awt.Color(51, 51, 51));
        header.setName("header"); // NOI18N
        header.setPreferredSize(new java.awt.Dimension(1440, 134));

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setName("logo"); // NOI18N

        head_label.setFont(new java.awt.Font("Montserrat", 1, 40)); // NOI18N
        head_label.setForeground(new java.awt.Color(255, 255, 255));
        head_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        head_label.setText("SegisDocs");
        head_label.setName("head_label"); // NOI18N

        logo_head.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/logo48x48.png"))); // NOI18N
        logo_head.setName("logo_head"); // NOI18N

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(172, 172, 172)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(headerLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(logo_head)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(head_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addComponent(logo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logo_head)
                    .addComponent(head_label))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        body.setBackground(new java.awt.Color(153, 153, 153));
        body.setName("body"); // NOI18N

        Search_Engine.setBackground(new java.awt.Color(102, 102, 102));
        Search_Engine.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        Search_Engine.setName("Search_Engine"); // NOI18N

        infolabel.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        infolabel.setForeground(new java.awt.Color(255, 255, 255));
        infolabel.setText("Introduzca su numero de documento:");
        infolabel.setName("infolabel"); // NOI18N

        NIF_FIELD.setBackground(new java.awt.Color(0, 0, 0));
        NIF_FIELD.setFont(new java.awt.Font("Montserrat", 1, 18)); // NOI18N
        NIF_FIELD.setForeground(new java.awt.Color(153, 153, 153));
        NIF_FIELD.setText("CIF | NIF | NIE | PASSPORT");
        NIF_FIELD.setActionCommand("<Not Set>");
        NIF_FIELD.setName("NIF_FIELD"); // NOI18N
        NIF_FIELD.setSelectionColor(new java.awt.Color(255, 51, 255));
        NIF_FIELD.addFocusListener(this);
        NIF_FIELD.addActionListener(this);

        GO.setBackground(new java.awt.Color(204, 204, 204));
        GO.setFont(new java.awt.Font("Montserrat", 3, 13)); // NOI18N
        GO.setForeground(new java.awt.Color(153, 153, 153));
        GO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/search32x32.png"))); // NOI18N
        GO.setText("Search");
        GO.setName("GO"); // NOI18N
        GO.addMouseListener(this);
        GO.addActionListener(this);

        ea_head_label.setBackground(new java.awt.Color(51, 51, 51));
        ea_head_label.setName("ea_head_label"); // NOI18N

        head_label1.setFont(new java.awt.Font("Montserrat", 1, 30)); // NOI18N
        head_label1.setForeground(new java.awt.Color(255, 255, 255));
        head_label1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        head_label1.setText("External Applications");
        head_label1.setName("head_label1"); // NOI18N

        javax.swing.GroupLayout ea_head_labelLayout = new javax.swing.GroupLayout(ea_head_label);
        ea_head_label.setLayout(ea_head_labelLayout);
        ea_head_labelLayout.setHorizontalGroup(
            ea_head_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(head_label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ea_head_labelLayout.setVerticalGroup(
            ea_head_labelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(head_label1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Search_EngineLayout = new javax.swing.GroupLayout(Search_Engine);
        Search_Engine.setLayout(Search_EngineLayout);
        Search_EngineLayout.setHorizontalGroup(
            Search_EngineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ea_head_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(Search_EngineLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Search_EngineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Search_EngineLayout.createSequentialGroup()
                        .addComponent(NIF_FIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(Search_EngineLayout.createSequentialGroup()
                        .addComponent(infolabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Search_EngineLayout.setVerticalGroup(
            Search_EngineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Search_EngineLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infolabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Search_EngineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NIF_FIELD, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ea_head_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        infolabel.getAccessibleContext().setAccessibleName("");

        tabFeatures.setBackground(new java.awt.Color(153, 153, 153));
        tabFeatures.setToolTipText("");
        tabFeatures.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabFeatures.setName("tabFeatures"); // NOI18N
        tabFeatures.addMouseListener(this);

        homepage.setBackground(new java.awt.Color(102, 102, 102));
        homepage.setName("homepage"); // NOI18N

        welcome_sign.setFont(new java.awt.Font("Montserrat", 1, 40)); // NOI18N
        welcome_sign.setForeground(new java.awt.Color(255, 255, 255));
        welcome_sign.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcome_sign.setText("Welcome to SegistelUI");
        welcome_sign.setName("welcome_sign"); // NOI18N

        welcome_description.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        welcome_description.setForeground(new java.awt.Color(255, 255, 255));
        welcome_description.setText("<html>   <li>  <p style=\" margin-right: 15px: text-align: justify; float:left;\">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed sagittis enim at leo tempor, sit amet blandit nisi lobortis.  Nunc dignissim nisi id justo efficitur dignissim. Proin odio libero, condimentum id molestie quis, scelerisque vitae nisi. Aliquam dignissim pharetra nisi, sit amet tincidunt sem finibus vel.  Donec cursus vel lacus ac efficitur. Ut malesuada justo lorem, id tincidunt lacus viverra et. Etiam laoreet finibus nunc, vel aliquet purus rutrum sed.  Aenean ultrices vulputate elit, ut dignissim nisl dictum sit amet.</p>  </li>  <li> <p style=\"margin-left: 5px; margin-right: 15px; text-align=justify;\">Donec pulvinar, tortor rutrum mollis malesuada, urna justo pellentesque eros, vel accumsan justo sem in magna. Sed sodales turpis non lorem dictum volutpat. Suspendisse fringilla rutrum viverra.  Mauris diam massa, egestas quis placerat nec, fringilla a ipsum. Vestibulum finibus eget ex et egestas. Ut ac enim a lacus tempor aliquam. Nulla tristique ex a diam scelerisque, non rutrum eros volutpat. Aenean quam mi, lacinia quis erat et, faucibus posuere justo.  Etiam tempor, nibh at dictum eleifend, augue urna suscipit augue, et luctus justo arcu eu ex. Aenean non leo ac risus malesuada tincidunt nec vitae quam.  Quisque eget molestie diam, eu porta ante. Aliquam lorem orci, efficitur ut convallis maximus, luctus et lorem. Pellentesque imperdiet mauris erat, ac bibendum eros lobortis ac. </p>  </li>   <li> <p style=\"margin-left: 5px; margin-right: 15px; text-align=justify;\">Curabitur elementum massa eget condimentum molestie.  Aliquam tincidunt ipsum in neque viverra, vel luctus mi dapibus. Phasellus eget rhoncus nulla. Suspendisse sodales sit amet dolor vitae rhoncus.  Aliquam pharetra varius purus malesuada aliquet. Integer dignissim metus ac dapibus efficitur. Cras orci velit, dignissim eu molestie vitae, lobortis et mi.  Aenean maximus odio non ullamcorper molestie. Nunc dui diam, ullamcorper id lacus vel, blandit posuere turpis.  Nunc vitae tellus ut libero tempor eleifend eget sit amet est. Vestibulum quis mi tellus. </p> </li>    </html> ");
        welcome_description.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        welcome_description.setName("welcome_description"); // NOI18N

        navbar.setBackground(new java.awt.Color(102, 102, 102));
        navbar.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        navbar.setName("navbar"); // NOI18N

        about_us.setBackground(new java.awt.Color(153, 51, 255));
        about_us.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        about_us.setForeground(new java.awt.Color(255, 255, 255));
        about_us.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/INF32x32.png"))); // NOI18N
        about_us.setText("About Us");
        about_us.setName("about_us"); // NOI18N
        about_us.addMouseListener(this);

        signup_bd_pane.setBackground(new java.awt.Color(255, 102, 0));
        signup_bd_pane.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        signup_bd_pane.setForeground(new java.awt.Color(255, 255, 255));
        signup_bd_pane.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/sing32x32.png"))); // NOI18N
        signup_bd_pane.setText("SIGN UP");
        signup_bd_pane.setName("signup_bd_pane"); // NOI18N
        signup_bd_pane.addMouseListener(this);

        localised.setText("CHANGE LANGUAGE");
        localised.setName("localised"); // NOI18N

        edit_data.setBackground(new java.awt.Color(0, 153, 153));
        edit_data.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        edit_data.setForeground(new java.awt.Color(255, 255, 255));
        edit_data.setText("EDIT DATA");
        edit_data.setName("edit_data"); // NOI18N

        javax.swing.GroupLayout navbarLayout = new javax.swing.GroupLayout(navbar);
        navbar.setLayout(navbarLayout);
        navbarLayout.setHorizontalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(signup_bd_pane, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(about_us, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(localised, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edit_data, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        navbarLayout.setVerticalGroup(
            navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(navbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(signup_bd_pane, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(about_us, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(localised, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edit_data, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homepageLayout = new javax.swing.GroupLayout(homepage);
        homepage.setLayout(homepageLayout);
        homepageLayout.setHorizontalGroup(
            homepageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepageLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(welcome_sign, javax.swing.GroupLayout.PREFERRED_SIZE, 885, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(homepageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homepageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcome_description, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(navbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        homepageLayout.setVerticalGroup(
            homepageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homepageLayout.createSequentialGroup()
                .addComponent(navbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcome_sign, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcome_description, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(305, Short.MAX_VALUE))
        );

        tabFeatures.addTab("Welcome", homepage);

        pers_info.setName("pers_info"); // NOI18N

        personal_info.setBackground(new java.awt.Color(102, 102, 102));
        personal_info.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        personal_info.setName("personal_info"); // NOI18N

        personal_info_label.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        personal_info_label.setForeground(new java.awt.Color(255, 255, 255));
        personal_info_label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        personal_info_label.setText("Personal Information");
        personal_info_label.setName("personal_info_label"); // NOI18N

        underline.setName("underline"); // NOI18N

        picture_frame.setBackground(new java.awt.Color(51, 51, 51));
        picture_frame.setName("picture_frame"); // NOI18N

        javax.swing.GroupLayout picture_frameLayout = new javax.swing.GroupLayout(picture_frame);
        picture_frame.setLayout(picture_frameLayout);
        picture_frameLayout.setHorizontalGroup(
            picture_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );
        picture_frameLayout.setVerticalGroup(
            picture_frameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 229, Short.MAX_VALUE)
        );

        add_picture.setText("Upload");
        add_picture.setActionCommand("q");
        add_picture.setName("add_picture"); // NOI18N

        clear_field.setText("clear");
        clear_field.setName("clear_field"); // NOI18N
        clear_field.addMouseListener(this);

        general_info.setBackground(new java.awt.Color(102, 102, 102));
        general_info.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 204)));
        general_info.setName("general_info"); // NOI18N

        name_field.setBackground(new java.awt.Color(0, 0, 0));
        name_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        name_field.setForeground(new java.awt.Color(204, 204, 204));
        name_field.setText("Nombre");
        name_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        name_field.setDropMode(javax.swing.DropMode.INSERT);
        name_field.setEnabled(false);
        name_field.setName("name_field"); // NOI18N
        name_field.setSelectionColor(new java.awt.Color(255, 102, 255));
        name_field.addFocusListener(this);

        name1_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        name1_label.setForeground(new java.awt.Color(255, 255, 255));
        name1_label.setText("Nombre*: ");
        name1_label.setName("name1_label"); // NOI18N

        name2_field.setBackground(new java.awt.Color(0, 0, 0));
        name2_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        name2_field.setForeground(new java.awt.Color(204, 204, 204));
        name2_field.setText("Nombre 2");
        name2_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        name2_field.setEnabled(false);
        name2_field.setName("name2_field"); // NOI18N
        name2_field.setSelectionColor(new java.awt.Color(255, 102, 255));
        name2_field.addFocusListener(this);

        name2_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        name2_label.setForeground(new java.awt.Color(255, 255, 255));
        name2_label.setText("Nombre 2: ");
        name2_label.setName("name2_label"); // NOI18N

        surname_field.setBackground(new java.awt.Color(0, 0, 0));
        surname_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        surname_field.setForeground(new java.awt.Color(204, 204, 204));
        surname_field.setText("Apellidos");
        surname_field.setToolTipText("");
        surname_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        surname_field.setEnabled(false);
        surname_field.setName("surname_field"); // NOI18N
        surname_field.setSelectionColor(new java.awt.Color(255, 102, 255));
        surname_field.addFocusListener(this);

        surname_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        surname_label.setForeground(new java.awt.Color(255, 255, 255));
        surname_label.setText("Apellidos 1 *: ");
        surname_label.setName("surname_label"); // NOI18N

        surname_2_field.setBackground(new java.awt.Color(0, 0, 0));
        surname_2_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        surname_2_field.setForeground(new java.awt.Color(204, 204, 204));
        surname_2_field.setText("Apellidos 2");
        surname_2_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        surname_2_field.setEnabled(false);
        surname_2_field.setName("surname_2_field"); // NOI18N
        surname_2_field.setSelectionColor(new java.awt.Color(255, 102, 255));
        surname_2_field.addFocusListener(this);

        surname2_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        surname2_label.setForeground(new java.awt.Color(255, 255, 255));
        surname2_label.setText("Apellidos 2: ");
        surname2_label.setName("surname2_label"); // NOI18N

        dob_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        dob_label.setForeground(new java.awt.Color(255, 255, 255));
        dob_label.setText("Fecha de Nacimiento*: ");
        dob_label.setName("dob_label"); // NOI18N

        day.setBackground(new java.awt.Color(51, 51, 51));
        day.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        day.setForeground(new java.awt.Color(255, 255, 255));
        day.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--DAY--", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        day.setEnabled(false);
        day.setName("day"); // NOI18N
        day.addActionListener(this);

        day_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        day_label.setForeground(new java.awt.Color(255, 255, 255));
        day_label.setText("Dia");
        day_label.setName("day_label"); // NOI18N

        month.setBackground(new java.awt.Color(51, 51, 51));
        month.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        month.setForeground(new java.awt.Color(255, 255, 255));
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--MONTH--", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        month.setEnabled(false);
        month.setName("month"); // NOI18N
        month.addActionListener(this);

        month_combo_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        month_combo_label.setForeground(new java.awt.Color(255, 255, 255));
        month_combo_label.setText("Mes");
        month_combo_label.setName("month_combo_label"); // NOI18N

        year.setBackground(new java.awt.Color(51, 51, 51));
        year.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        year.setForeground(new java.awt.Color(255, 255, 255));
        year.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        year.setEnabled(false);
        year.setName("year"); // NOI18N
        year.addActionListener(this);

        year_combo_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        year_combo_label.setForeground(new java.awt.Color(255, 255, 255));
        year_combo_label.setText("<html>\n<meta charset=\"ISO-8859-1\">\n<p>Año</p>\n</html>");
        year_combo_label.setName("year_combo_label"); // NOI18N

        sex_combo.setBackground(new java.awt.Color(51, 51, 51));
        sex_combo.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        sex_combo.setForeground(new java.awt.Color(255, 255, 255));
        sex_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--SEX--", "Male", "Female" }));
        sex_combo.setEnabled(false);
        sex_combo.setName("sex_combo"); // NOI18N
        sex_combo.addActionListener(this);

        sex_field.setEditable(false);
        sex_field.setBackground(new java.awt.Color(0, 0, 0));
        sex_field.setFont(new java.awt.Font("Montserrat", 0, 14)); // NOI18N
        sex_field.setForeground(new java.awt.Color(204, 204, 204));
        sex_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sex_field.setText("Sex");
        sex_field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        sex_field.setEnabled(false);
        sex_field.setName("sex_field"); // NOI18N
        sex_field.setSelectionColor(new java.awt.Color(255, 102, 255));

        sex_label1.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        sex_label1.setForeground(new java.awt.Color(255, 255, 255));
        sex_label1.setText("Sex*:");
        sex_label1.setName("sex_label1"); // NOI18N

        month_field.setEditable(false);
        month_field.setBackground(new java.awt.Color(0, 0, 0));
        month_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        month_field.setForeground(new java.awt.Color(255, 255, 255));
        month_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        month_field.setText("MON");
        month_field.setEnabled(false);
        month_field.setName("month_field"); // NOI18N

        day_field.setEditable(false);
        day_field.setBackground(new java.awt.Color(0, 0, 0));
        day_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        day_field.setForeground(new java.awt.Color(255, 255, 255));
        day_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        day_field.setText("DD");
        day_field.setEnabled(false);
        day_field.setName("day_field"); // NOI18N

        year_field.setEditable(false);
        year_field.setBackground(new java.awt.Color(0, 0, 0));
        year_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        year_field.setForeground(new java.awt.Color(255, 255, 255));
        year_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        year_field.setText("YYYY");
        year_field.setEnabled(false);
        year_field.setName("year_field"); // NOI18N

        javax.swing.GroupLayout general_infoLayout = new javax.swing.GroupLayout(general_info);
        general_info.setLayout(general_infoLayout);
        general_infoLayout.setHorizontalGroup(
            general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(general_infoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(general_infoLayout.createSequentialGroup()
                        .addComponent(sex_field, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sex_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, general_infoLayout.createSequentialGroup()
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(name_field, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(name1_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name2_label, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                            .addComponent(name2_field, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(general_infoLayout.createSequentialGroup()
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(surname_field)
                            .addComponent(surname_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dob_label, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(sex_label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(general_infoLayout.createSequentialGroup()
                                .addComponent(day_field, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(month_field, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(year_field)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(surname2_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(general_infoLayout.createSequentialGroup()
                                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(surname_2_field, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(general_infoLayout.createSequentialGroup()
                                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(general_infoLayout.createSequentialGroup()
                                                .addComponent(day_label, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(12, 12, 12)
                                                .addComponent(month_combo_label, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(general_infoLayout.createSequentialGroup()
                                                .addComponent(day, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(general_infoLayout.createSequentialGroup()
                                                .addComponent(year_combo_label)
                                                .addGap(5, 5, 5)))))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        general_infoLayout.setVerticalGroup(
            general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(general_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name1_label)
                    .addComponent(name2_label, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(name_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(name2_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surname2_label)
                    .addComponent(surname_label))
                .addGap(0, 0, 0)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surname_2_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(surname_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(year_combo_label, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(general_infoLayout.createSequentialGroup()
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(day_label, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(month_combo_label, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(day, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(general_infoLayout.createSequentialGroup()
                        .addComponent(dob_label)
                        .addGap(0, 0, 0)
                        .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(month_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(day_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(year_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sex_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(general_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sex_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sex_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        name_field.getAccessibleContext().setAccessibleName("Name");

        geo_info.setBackground(new java.awt.Color(102, 102, 102));
        geo_info.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 0)));
        geo_info.setName("geo_info"); // NOI18N

        nat_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        nat_label.setForeground(new java.awt.Color(255, 255, 255));
        nat_label.setText("Nacionalidad*: ");
        nat_label.setName("nat_label"); // NOI18N

        nationality_field.setEditable(false);
        nationality_field.setBackground(new java.awt.Color(0, 0, 0));
        nationality_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        nationality_field.setForeground(new java.awt.Color(204, 204, 204));
        nationality_field.setText("Nacionalidad");
        nationality_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        nationality_field.setDropTarget(null);
        nationality_field.setEnabled(false);
        nationality_field.setName("nationality_field"); // NOI18N
        nationality_field.setSelectionColor(new java.awt.Color(255, 102, 0));

        country_array.setBackground(new java.awt.Color(51, 51, 51));
        country_array.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        country_array.setForeground(new java.awt.Color(255, 255, 255));
        country_array.setEnabled(false);
        country_array.setName("country_array"); // NOI18N
        country_array.addActionListener(this);

        id_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        id_label.setForeground(new java.awt.Color(255, 255, 255));
        id_label.setText("Numero de Documento*: ");
        id_label.setName("id_label"); // NOI18N

        doc_letter_combo.setBackground(new java.awt.Color(51, 51, 51));
        doc_letter_combo.setFont(new java.awt.Font("PingFang SC", 0, 14)); // NOI18N
        doc_letter_combo.setForeground(new java.awt.Color(255, 255, 255));
        doc_letter_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "X", "Y", "Z" }));
        doc_letter_combo.setToolTipText("");
        doc_letter_combo.setEnabled(false);
        doc_letter_combo.setName("doc_letter_combo"); // NOI18N
        doc_letter_combo.addActionListener(this);

        id_local_field.setBackground(new java.awt.Color(0, 0, 0));
        id_local_field.setFont(new java.awt.Font("Montserrat", 0, 16)); // NOI18N
        id_local_field.setForeground(new java.awt.Color(204, 204, 204));
        id_local_field.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id_local_field.setText("Num. documento");
        id_local_field.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        id_local_field.setEnabled(false);
        id_local_field.setName("id_local_field"); // NOI18N
        id_local_field.setSelectionColor(new java.awt.Color(255, 102, 0));
        id_local_field.addFocusListener(this);
        id_local_field.addKeyListener(this);

        letter_result.setEditable(false);
        letter_result.setBackground(new java.awt.Color(0, 0, 0));
        letter_result.setFont(new java.awt.Font("Montserrat", 0, 16)); // NOI18N
        letter_result.setForeground(new java.awt.Color(204, 204, 204));
        letter_result.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        letter_result.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        letter_result.setName("letter_result"); // NOI18N
        letter_result.setSelectionColor(new java.awt.Color(204, 0, 204));

        image_controller.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/placeholder_obj/0.png"))); // NOI18N
        image_controller.setName("image_controller"); // NOI18N

        javax.swing.GroupLayout geo_infoLayout = new javax.swing.GroupLayout(geo_info);
        geo_info.setLayout(geo_infoLayout);
        geo_infoLayout.setHorizontalGroup(
            geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geo_infoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(geo_infoLayout.createSequentialGroup()
                        .addComponent(doc_letter_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_local_field)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(letter_result, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(geo_infoLayout.createSequentialGroup()
                        .addComponent(nationality_field, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(country_array, 0, 179, Short.MAX_VALUE))
                    .addGroup(geo_infoLayout.createSequentialGroup()
                        .addComponent(id_label, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(geo_infoLayout.createSequentialGroup()
                        .addComponent(nat_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(image_controller)))
                .addGap(10, 10, 10))
        );
        geo_infoLayout.setVerticalGroup(
            geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(geo_infoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(image_controller, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nat_label, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nationality_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(country_array, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(geo_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(id_local_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(letter_result, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(doc_letter_combo))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        contact_form.setBackground(new java.awt.Color(102, 102, 102));
        contact_form.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 255)));
        contact_form.setName("contact_form"); // NOI18N

        email_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        email_label.setForeground(new java.awt.Color(255, 255, 255));
        email_label.setText("Email Address: ");
        email_label.setName("email_label"); // NOI18N

        email_field.setBackground(new java.awt.Color(0, 0, 0));
        email_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        email_field.setForeground(new java.awt.Color(204, 204, 204));
        email_field.setText("Correo Electronico (Email)");
        email_field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        email_field.setEnabled(false);
        email_field.setName("email_field"); // NOI18N
        email_field.setSelectionColor(new java.awt.Color(153, 153, 255));
        email_field.addFocusListener(this);

        email_addition.setBackground(new java.awt.Color(0, 0, 0));
        email_addition.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        email_addition.setForeground(new java.awt.Color(204, 204, 204));
        email_addition.setText("Correo Electronico Adicional");
        email_addition.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        email_addition.setEnabled(false);
        email_addition.setName("email_addition"); // NOI18N
        email_addition.setSelectionColor(new java.awt.Color(153, 153, 255));
        email_addition.addFocusListener(this);

        email_label1.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        email_label1.setForeground(new java.awt.Color(255, 255, 255));
        email_label1.setText("Additional Email Address: ");
        email_label1.setName("email_label1"); // NOI18N

        phone_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        phone_label.setForeground(new java.awt.Color(255, 255, 255));
        phone_label.setText("Phone Number: ");
        phone_label.setName("phone_label"); // NOI18N

        phone_field.setBackground(new java.awt.Color(0, 0, 0));
        phone_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        phone_field.setForeground(new java.awt.Color(204, 204, 204));
        phone_field.setText("Numero de Telefono");
        phone_field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        phone_field.setEnabled(false);
        phone_field.setName("phone_field"); // NOI18N
        phone_field.setSelectionColor(new java.awt.Color(153, 153, 255));
        phone_field.addFocusListener(this);
        phone_field.addKeyListener(this);

        addit_phone_field.setBackground(new java.awt.Color(0, 0, 0));
        addit_phone_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        addit_phone_field.setForeground(new java.awt.Color(204, 204, 204));
        addit_phone_field.setText("Numero de Telefono Adicional");
        addit_phone_field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        addit_phone_field.setEnabled(false);
        addit_phone_field.setName("addit_phone_field"); // NOI18N
        addit_phone_field.setSelectionColor(new java.awt.Color(153, 153, 255));
        addit_phone_field.addFocusListener(this);

        phone2_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        phone2_label.setForeground(new java.awt.Color(255, 255, 255));
        phone2_label.setText("Phone Number 2: ");
        phone2_label.setName("phone2_label"); // NOI18N

        fax_field.setBackground(new java.awt.Color(0, 0, 0));
        fax_field.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        fax_field.setForeground(new java.awt.Color(204, 204, 204));
        fax_field.setText("Fax");
        fax_field.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        fax_field.setEnabled(false);
        fax_field.setName("fax_field"); // NOI18N
        fax_field.setSelectionColor(new java.awt.Color(153, 153, 255));
        fax_field.addFocusListener(this);
        fax_field.addActionListener(this);

        sex_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        sex_label.setForeground(new java.awt.Color(255, 255, 255));
        sex_label.setText("Fax:");
        sex_label.setName("sex_label"); // NOI18N

        javax.swing.GroupLayout contact_formLayout = new javax.swing.GroupLayout(contact_form);
        contact_form.setLayout(contact_formLayout);
        contact_formLayout.setHorizontalGroup(
            contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contact_formLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contact_formLayout.createSequentialGroup()
                        .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(email_label, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phone_label, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phone_field, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(phone2_label, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(email_addition)
                            .addComponent(email_label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addit_phone_field, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fax_field, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sex_label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contact_formLayout.setVerticalGroup(
            contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contact_formLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email_label, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email_label1))
                .addGap(0, 0, 0)
                .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email_addition, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phone_label)
                    .addComponent(phone2_label))
                .addGap(0, 0, 0)
                .addGroup(contact_formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phone_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addit_phone_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sex_label)
                .addGap(0, 0, 0)
                .addComponent(fax_field, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 0)));
        jPanel1.setName("jPanel1"); // NOI18N

        isTourist.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        isTourist.setText("Tourist");
        isTourist.setName("isTourist"); // NOI18N
        isTourist.addActionListener(this);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(isTourist, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(isTourist, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );

        save.setBackground(new java.awt.Color(51, 51, 51));
        save.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        save.setForeground(new java.awt.Color(204, 204, 204));
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/ok16x16.png"))); // NOI18N
        save.setText("Save");
        save.setEnabled(false);
        save.setName("save"); // NOI18N

        cancel.setBackground(new java.awt.Color(51, 51, 51));
        cancel.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        cancel.setForeground(new java.awt.Color(204, 204, 204));
        cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/cross16x16.png"))); // NOI18N
        cancel.setText("Cancel");
        cancel.setEnabled(false);
        cancel.setName("cancel"); // NOI18N
        cancel.addMouseListener(this);

        javax.swing.GroupLayout personal_infoLayout = new javax.swing.GroupLayout(personal_info);
        personal_info.setLayout(personal_infoLayout);
        personal_infoLayout.setHorizontalGroup(
            personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personal_infoLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personal_infoLayout.createSequentialGroup()
                        .addComponent(personal_info_label, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clear_field)
                        .addGap(567, 567, 567))
                    .addGroup(personal_infoLayout.createSequentialGroup()
                        .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(contact_form, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGroup(personal_infoLayout.createSequentialGroup()
                                    .addComponent(geo_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(personal_infoLayout.createSequentialGroup()
                                .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(underline, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(general_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(personal_infoLayout.createSequentialGroup()
                                        .addGap(75, 75, 75)
                                        .addComponent(add_picture, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(personal_infoLayout.createSequentialGroup()
                                        .addGap(57, 57, 57)
                                        .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(picture_frame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                        .addContainerGap(82, Short.MAX_VALUE))))
        );
        personal_infoLayout.setVerticalGroup(
            personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personal_infoLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(personal_info_label, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clear_field, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personal_infoLayout.createSequentialGroup()
                        .addComponent(underline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(picture_frame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(add_picture)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(save, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(personal_infoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(general_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(personal_infoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(geo_info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contact_form, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pers_info.setViewportView(personal_info);

        tabFeatures.addTab("Personal Information", pers_info);

        home_addr_pane.setBackground(new java.awt.Color(102, 102, 102));
        home_addr_pane.setName("home_addr_pane"); // NOI18N

        home_add_head_label.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        home_add_head_label.setForeground(new java.awt.Color(255, 255, 255));
        home_add_head_label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        home_add_head_label.setText("Home Address");
        home_add_head_label.setName("home_add_head_label"); // NOI18N

        jSeparator3.setName("jSeparator3"); // NOI18N

        country_ctrl.setBackground(new java.awt.Color(51, 51, 51));
        country_ctrl.setFont(new java.awt.Font("PT Sans Caption", 1, 13)); // NOI18N
        country_ctrl.setForeground(new java.awt.Color(255, 255, 255));
        country_ctrl.setMaximumRowCount(40);
        country_ctrl.setEnabled(false);
        country_ctrl.setName("country_ctrl"); // NOI18N
        country_ctrl.addMouseListener(this);
        country_ctrl.addActionListener(this);

        postalcode_field.setBackground(new java.awt.Color(0, 0, 0));
        postalcode_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        postalcode_field.setForeground(new java.awt.Color(204, 204, 204));
        postalcode_field.setText("CODIGO POSTAL");
        postalcode_field.setDropMode(javax.swing.DropMode.INSERT);
        postalcode_field.setEnabled(false);
        postalcode_field.setName("postalcode_field"); // NOI18N

        postal_code_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        postal_code_label.setForeground(new java.awt.Color(255, 255, 255));
        postal_code_label.setText("Código Postal: ");
        postal_code_label.setName("postal_code_label"); // NOI18N

        country_id.setEditable(false);
        country_id.setBackground(new java.awt.Color(0, 0, 0));
        country_id.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        country_id.setForeground(new java.awt.Color(204, 204, 204));
        country_id.setText("PAIS");
        country_id.setName("country_id"); // NOI18N
        country_id.addActionListener(this);

        city_field.setBackground(new java.awt.Color(0, 0, 0));
        city_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        city_field.setForeground(new java.awt.Color(204, 204, 204));
        city_field.setText("CIUDAD");
        city_field.setToolTipText("");
        city_field.setEnabled(false);
        city_field.setName("city_field"); // NOI18N

        city_combo.setBackground(new java.awt.Color(51, 51, 51));
        city_combo.setForeground(new java.awt.Color(255, 255, 255));
        city_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        city_combo.setEnabled(false);
        city_combo.setName("city_combo"); // NOI18N

        postal_code_label1.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        postal_code_label1.setForeground(new java.awt.Color(255, 255, 255));
        postal_code_label1.setText("Ciudad: ");
        postal_code_label1.setName("postal_code_label1"); // NOI18N

        main_address.setBackground(new java.awt.Color(102, 102, 102));
        main_address.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 153, 255)));
        main_address.setName("main_address"); // NOI18N

        via_combo.setBackground(new java.awt.Color(51, 51, 51));
        via_combo.setFont(new java.awt.Font("PingFang HK", 0, 13)); // NOI18N
        via_combo.setForeground(new java.awt.Color(255, 255, 255));
        via_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CARRER", "PASSEIG", "PLAÇA", "RAMBLA", "RONDA", "AVINGUDA", "VIA", "TRAVESSERA", "PASSATGE", "BULEVAR", "OTROS" }));
        via_combo.setEnabled(false);
        via_combo.setName("via_combo"); // NOI18N

        via_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        via_label.setForeground(new java.awt.Color(255, 255, 255));
        via_label.setText("VIA*: ");
        via_label.setName("via_label"); // NOI18N

        address_field.setBackground(new java.awt.Color(0, 0, 0));
        address_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        address_field.setForeground(new java.awt.Color(204, 204, 204));
        address_field.setText("DIRECCION");
        address_field.setToolTipText("");
        address_field.setEnabled(false);
        address_field.setName("address_field"); // NOI18N
        address_field.setSelectionColor(new java.awt.Color(102, 153, 255));
        address_field.addFocusListener(this);

        address_label.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        address_label.setForeground(new java.awt.Color(255, 255, 255));
        address_label.setText("Dirección*: ");
        address_label.setName("address_label"); // NOI18N

        number_field.setBackground(new java.awt.Color(0, 0, 0));
        number_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        number_field.setForeground(new java.awt.Color(204, 204, 204));
        number_field.setText("NUMERO");
        number_field.setToolTipText("");
        number_field.setEnabled(false);
        number_field.setName("number_field"); // NOI18N
        number_field.setSelectionColor(new java.awt.Color(102, 153, 255));
        number_field.addFocusListener(this);

        address_number.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        address_number.setForeground(new java.awt.Color(255, 255, 255));
        address_number.setText("Numero*: ");
        address_number.setName("address_number"); // NOI18N

        javax.swing.GroupLayout main_addressLayout = new javax.swing.GroupLayout(main_address);
        main_address.setLayout(main_addressLayout);
        main_addressLayout.setHorizontalGroup(
            main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_addressLayout.createSequentialGroup()
                .addGroup(main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_addressLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(via_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(main_addressLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(via_label, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(address_field, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(main_addressLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(address_label, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(address_number, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(number_field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        main_addressLayout.setVerticalGroup(
            main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_addressLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(via_label)
                    .addComponent(address_label)
                    .addComponent(address_number))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(main_addressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(via_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(address_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(number_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exact_home.setBackground(new java.awt.Color(102, 102, 102));
        exact_home.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 102)));
        exact_home.setName("exact_home"); // NOI18N

        apt_field.setBackground(new java.awt.Color(0, 0, 0));
        apt_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        apt_field.setForeground(new java.awt.Color(204, 204, 204));
        apt_field.setText("PISO");
        apt_field.setToolTipText("");
        apt_field.setEnabled(false);
        apt_field.setName("Piso"); // NOI18N
        apt_field.setSelectionColor(new java.awt.Color(255, 51, 102));
        apt_field.addFocusListener(this);

        door_field.setBackground(new java.awt.Color(0, 0, 0));
        door_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        door_field.setForeground(new java.awt.Color(204, 204, 204));
        door_field.setText("PUERTA");
        door_field.setToolTipText("");
        door_field.setEnabled(false);
        door_field.setName("door_field"); // NOI18N
        door_field.setSelectionColor(new java.awt.Color(255, 51, 102));
        door_field.addFocusListener(this);

        staircase_field.setBackground(new java.awt.Color(0, 0, 0));
        staircase_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        staircase_field.setForeground(new java.awt.Color(204, 204, 204));
        staircase_field.setText("ESC.");
        staircase_field.setToolTipText("");
        staircase_field.setEnabled(false);
        staircase_field.setName("staircase_field"); // NOI18N
        staircase_field.setSelectionColor(new java.awt.Color(255, 51, 102));
        staircase_field.addFocusListener(this);

        block_field.setBackground(new java.awt.Color(0, 0, 0));
        block_field.setFont(new java.awt.Font("Montserrat", 0, 15)); // NOI18N
        block_field.setForeground(new java.awt.Color(204, 204, 204));
        block_field.setText("BLOQUE");
        block_field.setToolTipText("");
        block_field.setEnabled(false);
        block_field.setName("block_field"); // NOI18N
        block_field.setSelectionColor(new java.awt.Color(255, 51, 102));
        block_field.addFocusListener(this);

        via_label1.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        via_label1.setForeground(new java.awt.Color(255, 255, 255));
        via_label1.setText("Piso: ");
        via_label1.setName("via_label1"); // NOI18N

        via_label2.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        via_label2.setForeground(new java.awt.Color(255, 255, 255));
        via_label2.setText("Puerta: ");
        via_label2.setName("via_label2"); // NOI18N

        via_label3.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        via_label3.setForeground(new java.awt.Color(255, 255, 255));
        via_label3.setText("Escalera: ");
        via_label3.setName("via_label3"); // NOI18N

        via_label4.setFont(new java.awt.Font("Montserrat", 1, 13)); // NOI18N
        via_label4.setForeground(new java.awt.Color(255, 255, 255));
        via_label4.setText("Bloque: ");
        via_label4.setName("via_label4"); // NOI18N

        javax.swing.GroupLayout exact_homeLayout = new javax.swing.GroupLayout(exact_home);
        exact_home.setLayout(exact_homeLayout);
        exact_homeLayout.setHorizontalGroup(
            exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exact_homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(via_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apt_field, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(door_field, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(via_label2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(staircase_field)
                    .addComponent(via_label3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addGap(40, 40, 40)
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(via_label4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(exact_homeLayout.createSequentialGroup()
                        .addComponent(block_field, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        exact_homeLayout.setVerticalGroup(
            exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exact_homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(via_label1)
                    .addComponent(via_label2)
                    .addComponent(via_label3)
                    .addComponent(via_label4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(exact_homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(apt_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(door_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staircase_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(block_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        via_label1.getAccessibleContext().setAccessibleName("Piso*: ");

        javax.swing.GroupLayout home_addr_paneLayout = new javax.swing.GroupLayout(home_addr_pane);
        home_addr_pane.setLayout(home_addr_paneLayout);
        home_addr_paneLayout.setHorizontalGroup(
            home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(home_addr_paneLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(home_addr_paneLayout.createSequentialGroup()
                        .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(home_addr_paneLayout.createSequentialGroup()
                                .addComponent(city_field, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(city_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(home_addr_paneLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(postal_code_label1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49)
                        .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(postal_code_label, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(postalcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(home_addr_paneLayout.createSequentialGroup()
                        .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exact_home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(main_address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(259, Short.MAX_VALUE))
                    .addGroup(home_addr_paneLayout.createSequentialGroup()
                        .addComponent(home_add_head_label, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(country_id, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(country_ctrl, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91))))
        );
        home_addr_paneLayout.setVerticalGroup(
            home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(home_addr_paneLayout.createSequentialGroup()
                .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(home_addr_paneLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(home_add_head_label, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(home_addr_paneLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(country_id, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(country_ctrl, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(0, 0, 0)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(main_address, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exact_home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postal_code_label)
                    .addComponent(postal_code_label1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(home_addr_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postalcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(city_field, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(city_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(417, Short.MAX_VALUE))
        );

        tabFeatures.addTab("Home Address", home_addr_pane);

        main_app_scroller.setBackground(new java.awt.Color(204, 204, 204));
        main_app_scroller.setName("main_app_scroller"); // NOI18N

        panel.setBackground(new java.awt.Color(102, 102, 102));
        panel.setName("panel"); // NOI18N

        internet_brs.setName("internet_brs"); // NOI18N

        internal_internet_pane.setBackground(new java.awt.Color(204, 204, 204));
        internal_internet_pane.setName("internal_internet_pane"); // NOI18N

        chrome.setBackground(new java.awt.Color(204, 0, 0));
        chrome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/chrome/48x48.png"))); // NOI18N
        chrome.setName("chrome"); // NOI18N
        chrome.addMouseListener(this);

        firefox.setBackground(new java.awt.Color(255, 153, 0));
        firefox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/firefox/48x48.png"))); // NOI18N
        firefox.setName("firefox"); // NOI18N
        firefox.addMouseListener(this);

        ie.setBackground(new java.awt.Color(204, 255, 255));
        ie.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/explorer/48x48.png"))); // NOI18N
        ie.setName("ie"); // NOI18N
        ie.addMouseListener(this);

        edge.setBackground(new java.awt.Color(204, 204, 255));
        edge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/edge/48x48.png"))); // NOI18N
        edge.setName("edge"); // NOI18N
        edge.addMouseListener(this);

        javax.swing.GroupLayout internal_internet_paneLayout = new javax.swing.GroupLayout(internal_internet_pane);
        internal_internet_pane.setLayout(internal_internet_paneLayout);
        internal_internet_paneLayout.setHorizontalGroup(
            internal_internet_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internal_internet_paneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chrome, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(firefox, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ie, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edge, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        internal_internet_paneLayout.setVerticalGroup(
            internal_internet_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(internal_internet_paneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(ie, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addComponent(firefox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chrome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(edge, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        internet_brs.setViewportView(internal_internet_pane);

        browsers_label.setBackground(new java.awt.Color(0, 0, 0));
        browsers_label.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        browsers_label.setForeground(new java.awt.Color(255, 255, 255));
        browsers_label.setText("Browsers");
        browsers_label.setName("browsers_label"); // NOI18N

        separation1.setName("separation1"); // NOI18N

        office_label.setBackground(new java.awt.Color(0, 0, 0));
        office_label.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        office_label.setForeground(new java.awt.Color(255, 255, 255));
        office_label.setText("Office");
        office_label.setName("office_label"); // NOI18N

        separation2.setName("separation2"); // NOI18N

        office_pane_main.setName("office_pane_main"); // NOI18N

        office_pane_secondary.setBackground(new java.awt.Color(255, 204, 51));
        office_pane_secondary.setName("office_pane_secondary"); // NOI18N

        App.setBackground(new java.awt.Color(255, 255, 255));
        App.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App.setName("App"); // NOI18N

        App1.setBackground(new java.awt.Color(255, 255, 255));
        App1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App1.setName("App1"); // NOI18N

        App2.setBackground(new java.awt.Color(255, 255, 255));
        App2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App2.setName("App2"); // NOI18N

        App3.setBackground(new java.awt.Color(255, 255, 255));
        App3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App3.setName("App3"); // NOI18N

        javax.swing.GroupLayout office_pane_secondaryLayout = new javax.swing.GroupLayout(office_pane_secondary);
        office_pane_secondary.setLayout(office_pane_secondaryLayout);
        office_pane_secondaryLayout.setHorizontalGroup(
            office_pane_secondaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(office_pane_secondaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(App, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        office_pane_secondaryLayout.setVerticalGroup(
            office_pane_secondaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(office_pane_secondaryLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(office_pane_secondaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(App3, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(App2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(App1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(App, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        office_pane_main.setViewportView(office_pane_secondary);

        office_pane_main1.setName("office_pane_main1"); // NOI18N

        office_pane_secondary1.setBackground(new java.awt.Color(204, 0, 204));
        office_pane_secondary1.setName("office_pane_secondary1"); // NOI18N

        App4.setBackground(new java.awt.Color(255, 255, 255));
        App4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App4.setName("App4"); // NOI18N

        App5.setBackground(new java.awt.Color(255, 255, 255));
        App5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App5.setName("App5"); // NOI18N

        App6.setBackground(new java.awt.Color(255, 255, 255));
        App6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App6.setName("App6"); // NOI18N

        App7.setBackground(new java.awt.Color(255, 255, 255));
        App7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/segistelui_mt/icons/apps/no-app-generic/48x48.png"))); // NOI18N
        App7.setName("App7"); // NOI18N

        javax.swing.GroupLayout office_pane_secondary1Layout = new javax.swing.GroupLayout(office_pane_secondary1);
        office_pane_secondary1.setLayout(office_pane_secondary1Layout);
        office_pane_secondary1Layout.setHorizontalGroup(
            office_pane_secondary1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, office_pane_secondary1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(App4, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(App7, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        office_pane_secondary1Layout.setVerticalGroup(
            office_pane_secondary1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(office_pane_secondary1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(office_pane_secondary1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(App7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(App6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(App5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(App4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        office_pane_main1.setViewportView(office_pane_secondary1);

        separation3.setName("separation3"); // NOI18N

        office_label1.setBackground(new java.awt.Color(0, 0, 0));
        office_label1.setFont(new java.awt.Font("Montserrat", 0, 18)); // NOI18N
        office_label1.setForeground(new java.awt.Color(255, 255, 255));
        office_label1.setText("Misc");
        office_label1.setName("Misc"); // NOI18N

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separation1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(separation2, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(browsers_label)
                            .addComponent(office_label))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separation3, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(office_label1))
                        .addGap(79, 79, 79))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(office_pane_main, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(internet_brs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(office_pane_main1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap())))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(browsers_label)
                .addGap(0, 0, 0)
                .addComponent(separation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(internet_brs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(office_label)
                .addGap(0, 0, 0)
                .addComponent(separation2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(office_pane_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(office_label1)
                .addGap(0, 0, 0)
                .addComponent(separation3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(office_pane_main1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        main_app_scroller.setViewportView(panel);

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Search_Engine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(main_app_scroller))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabFeatures, javax.swing.GroupLayout.PREFERRED_SIZE, 1032, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bodyLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(Search_Engine, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_app_scroller, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabFeatures, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tabFeatures.getAccessibleContext().setAccessibleName("NIF");

        footer.setBackground(new java.awt.Color(51, 51, 51));
        footer.setName("footer"); // NOI18N
        footer.setPreferredSize(new java.awt.Dimension(1440, 900));

        copyright_label.setFont(new java.awt.Font("Montserrat", 0, 14)); // NOI18N
        copyright_label.setForeground(new java.awt.Color(255, 255, 255));
        copyright_label.setText("Copyright © 2021 JM Segistel SCP & Media Archive Ltd. All rights reserved!");
        copyright_label.setName("copyright_label"); // NOI18N

        sign_up_foot.setBackground(new java.awt.Color(255, 102, 0));
        sign_up_foot.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        sign_up_foot.setForeground(new java.awt.Color(204, 204, 204));
        sign_up_foot.setText("Register");
        sign_up_foot.setName("sign_up_foot"); // NOI18N
        sign_up_foot.addMouseListener(this);

        login.setBackground(new java.awt.Color(0, 51, 255));
        login.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        login.setText("Login");
        login.setName("login"); // NOI18N

        about_us_show.setBackground(new java.awt.Color(153, 51, 255));
        about_us_show.setFont(new java.awt.Font("Montserrat", 0, 13)); // NOI18N
        about_us_show.setForeground(new java.awt.Color(204, 204, 204));
        about_us_show.setText("About Us");
        about_us_show.setName("about_us_show"); // NOI18N
        about_us_show.addMouseListener(this);

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(footerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(copyright_label, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(about_us_show, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sign_up_foot, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
            .addComponent(about_us_show, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sign_up_foot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(copyright_label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        sign_up_foot.getAccessibleContext().setAccessibleName("Sign Up");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(footer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1560, Short.MAX_VALUE)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 1560, Short.MAX_VALUE)
            .addComponent(body, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        header.getAccessibleContext().setAccessibleParent(header);

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == NIF_FIELD) {
            SegistelUI.this.NIF_FIELDActionPerformed(evt);
        }
        else if (evt.getSource() == GO) {
            SegistelUI.this.GOActionPerformed(evt);
        }
        else if (evt.getSource() == day) {
            SegistelUI.this.dayActionPerformed(evt);
        }
        else if (evt.getSource() == month) {
            SegistelUI.this.monthActionPerformed(evt);
        }
        else if (evt.getSource() == year) {
            SegistelUI.this.yearActionPerformed(evt);
        }
        else if (evt.getSource() == sex_combo) {
            SegistelUI.this.sex_comboActionPerformed(evt);
        }
        else if (evt.getSource() == country_array) {
            SegistelUI.this.country_arrayActionPerformed(evt);
        }
        else if (evt.getSource() == doc_letter_combo) {
            SegistelUI.this.doc_letter_comboActionPerformed(evt);
        }
        else if (evt.getSource() == fax_field) {
            SegistelUI.this.fax_fieldActionPerformed(evt);
        }
        else if (evt.getSource() == isTourist) {
            SegistelUI.this.isTouristActionPerformed(evt);
        }
        else if (evt.getSource() == country_ctrl) {
            SegistelUI.this.country_ctrlActionPerformed(evt);
        }
        else if (evt.getSource() == country_id) {
            SegistelUI.this.country_idActionPerformed(evt);
        }
    }

    public void focusGained(java.awt.event.FocusEvent evt) {
        if (evt.getSource() == NIF_FIELD) {
            SegistelUI.this.NIF_FIELDFocusGained(evt);
        }
        else if (evt.getSource() == name_field) {
            SegistelUI.this.name_fieldFocusGained(evt);
        }
        else if (evt.getSource() == name2_field) {
            SegistelUI.this.name2_fieldFocusGained(evt);
        }
        else if (evt.getSource() == surname_field) {
            SegistelUI.this.surname_fieldFocusGained(evt);
        }
        else if (evt.getSource() == surname_2_field) {
            SegistelUI.this.surname_2_fieldFocusGained(evt);
        }
        else if (evt.getSource() == id_local_field) {
            SegistelUI.this.id_local_fieldFocusGained(evt);
        }
        else if (evt.getSource() == email_field) {
            SegistelUI.this.email_fieldFocusGained(evt);
        }
        else if (evt.getSource() == email_addition) {
            SegistelUI.this.email_additionFocusGained(evt);
        }
        else if (evt.getSource() == phone_field) {
            SegistelUI.this.phone_fieldFocusGained(evt);
        }
        else if (evt.getSource() == addit_phone_field) {
            SegistelUI.this.addit_phone_fieldFocusGained(evt);
        }
        else if (evt.getSource() == fax_field) {
            SegistelUI.this.fax_fieldFocusGained(evt);
        }
        else if (evt.getSource() == address_field) {
            SegistelUI.this.address_fieldFocusGained(evt);
        }
        else if (evt.getSource() == number_field) {
            SegistelUI.this.number_fieldFocusGained(evt);
        }
        else if (evt.getSource() == apt_field) {
            SegistelUI.this.apt_fieldFocusGained(evt);
        }
        else if (evt.getSource() == door_field) {
            SegistelUI.this.door_fieldFocusGained(evt);
        }
        else if (evt.getSource() == staircase_field) {
            SegistelUI.this.staircase_fieldFocusGained(evt);
        }
        else if (evt.getSource() == block_field) {
            SegistelUI.this.block_fieldFocusGained(evt);
        }
    }

    public void focusLost(java.awt.event.FocusEvent evt) {
        if (evt.getSource() == NIF_FIELD) {
            SegistelUI.this.NIF_FIELDFocusLost(evt);
        }
        else if (evt.getSource() == name_field) {
            SegistelUI.this.name_fieldFocusLost(evt);
        }
        else if (evt.getSource() == name2_field) {
            SegistelUI.this.name2_fieldFocusLost(evt);
        }
        else if (evt.getSource() == surname_field) {
            SegistelUI.this.surname_fieldFocusLost(evt);
        }
        else if (evt.getSource() == surname_2_field) {
            SegistelUI.this.surname_2_fieldFocusLost(evt);
        }
        else if (evt.getSource() == id_local_field) {
            SegistelUI.this.id_local_fieldFocusLost(evt);
        }
        else if (evt.getSource() == email_field) {
            SegistelUI.this.email_fieldFocusLost(evt);
        }
        else if (evt.getSource() == email_addition) {
            SegistelUI.this.email_additionFocusLost(evt);
        }
        else if (evt.getSource() == phone_field) {
            SegistelUI.this.phone_fieldFocusLost(evt);
        }
        else if (evt.getSource() == addit_phone_field) {
            SegistelUI.this.addit_phone_fieldFocusLost(evt);
        }
        else if (evt.getSource() == fax_field) {
            SegistelUI.this.fax_fieldFocusLost(evt);
        }
        else if (evt.getSource() == address_field) {
            SegistelUI.this.address_fieldFocusLost(evt);
        }
        else if (evt.getSource() == number_field) {
            SegistelUI.this.number_fieldFocusLost(evt);
        }
        else if (evt.getSource() == apt_field) {
            SegistelUI.this.apt_fieldFocusLost(evt);
        }
        else if (evt.getSource() == door_field) {
            SegistelUI.this.door_fieldFocusLost(evt);
        }
        else if (evt.getSource() == staircase_field) {
            SegistelUI.this.staircase_fieldFocusLost(evt);
        }
        else if (evt.getSource() == block_field) {
            SegistelUI.this.block_fieldFocusLost(evt);
        }
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
    }

    public void keyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getSource() == id_local_field) {
            SegistelUI.this.id_local_fieldKeyReleased(evt);
        }
    }

    public void keyTyped(java.awt.event.KeyEvent evt) {
        if (evt.getSource() == id_local_field) {
            SegistelUI.this.id_local_fieldKeyTyped(evt);
        }
        else if (evt.getSource() == phone_field) {
            SegistelUI.this.phone_fieldKeyTyped(evt);
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == GO) {
            SegistelUI.this.GOMouseClicked(evt);
        }
        else if (evt.getSource() == tabFeatures) {
            SegistelUI.this.tabFeaturesMouseClicked(evt);
        }
        else if (evt.getSource() == about_us) {
            SegistelUI.this.about_usMouseClicked(evt);
        }
        else if (evt.getSource() == signup_bd_pane) {
            SegistelUI.this.signup_bd_paneMouseClicked(evt);
        }
        else if (evt.getSource() == clear_field) {
            SegistelUI.this.clear_fieldMouseClicked(evt);
        }
        else if (evt.getSource() == cancel) {
            SegistelUI.this.cancelMouseClicked(evt);
        }
        else if (evt.getSource() == country_ctrl) {
            SegistelUI.this.country_ctrlMouseClicked(evt);
        }
        else if (evt.getSource() == chrome) {
            SegistelUI.this.chromeMouseClicked(evt);
        }
        else if (evt.getSource() == firefox) {
            SegistelUI.this.firefoxMouseClicked(evt);
        }
        else if (evt.getSource() == ie) {
            SegistelUI.this.ieMouseClicked(evt);
        }
        else if (evt.getSource() == edge) {
            SegistelUI.this.edgeMouseClicked(evt);
        }
        else if (evt.getSource() == sign_up_foot) {
            SegistelUI.this.sign_up_footMouseClicked(evt);
        }
        else if (evt.getSource() == about_us_show) {
            SegistelUI.this.about_us_showMouseClicked(evt);
        }
        else if (evt.getSource() == close_action) {
            SegistelUI.this.close_actionMouseClicked(evt);
        }
        else if (evt.getSource() == update_app) {
            SegistelUI.this.update_appMouseClicked(evt);
        }
    }

    public void mouseEntered(java.awt.event.MouseEvent evt) {
    }

    public void mouseExited(java.awt.event.MouseEvent evt) {
    }

    public void mousePressed(java.awt.event.MouseEvent evt) {
    }

    public void mouseReleased(java.awt.event.MouseEvent evt) {
    }// </editor-fold>//GEN-END:initComponents
    private void sign_up_footMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sign_up_footMouseClicked
        signup_TRUE();
    }//GEN-LAST:event_sign_up_footMouseClicked
    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        signup_FALSE();
    }//GEN-LAST:event_cancelMouseClicked
    private void close_actionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close_actionMouseClicked
     about_us_frame.setVisible(false);
    }//GEN-LAST:event_close_actionMouseClicked
    private void update_appMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_update_appMouseClicked
        JOptionPane.showConfirmDialog(header, "You wanna Update?",
    "SUGMA First then LIGMA after",
    JOptionPane.YES_NO_OPTION,
    JOptionPane.WARNING_MESSAGE);
     
    }//GEN-LAST:event_update_appMouseClicked
    /*SOFTWARE SUPPORT Checks and Launchers*/
    @SuppressWarnings("CallToPrintStackTrace")
    private void chromeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chromeMouseClicked
      
       @SuppressWarnings("LocalVariableHidesMemberVariable")
       final Browser chrome = Browser.Chrome;
    if (!chrome.exists())
    {
        chrome.openDownloadPage();
        System.err.println("Error, Google Chrome is not found!\n");
    }else{             
       Warning_MSG(new File(chrome.getFilePath()));
    }
        
    }//GEN-LAST:event_chromeMouseClicked
    private void firefoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_firefoxMouseClicked
       @SuppressWarnings("LocalVariableHidesMemberVariable")
       final Browser firefox = Browser.Firefox;
    if (!firefox.exists())
    {
        firefox.openDownloadPage();
        System.err.println("Error, Mozilla Firefox is not found!\n");
    }else{             
       Warning_MSG(new File(firefox.getFilePath()));
    }
        
    }//GEN-LAST:event_firefoxMouseClicked
    private void ieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ieMouseClicked
      @SuppressWarnings("LocalVariableHidesMemberVariable")
      final Browser ie = Browser.IExplorer;
    if (!ie.exists())
    {
        ie.openDownloadPage();
        System.err.println("Error, Internet Explorer is not found!\n");
    }else{             
       Warning_MSG(new File(ie.getFilePath()));
    }
        
    }//GEN-LAST:event_ieMouseClicked
    private void edgeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edgeMouseClicked
      @SuppressWarnings("LocalVariableHidesMemberVariable")
      final Browser edge = Browser.Edge;
    if (!edge.exists())
    {
        edge.openDownloadPage();
        System.err.println("Error, Microsoft Edge is not found!\n");
    }else{             
       Warning_MSG(new File(edge.getFilePath()));
    }
        
    }//GEN-LAST:event_edgeMouseClicked

    private void about_us_showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_about_us_showMouseClicked
    OSInfo();
    about_us_frame.setVisible(true);
    }//GEN-LAST:event_about_us_showMouseClicked
    private void GOMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GOMouseClicked
        populateYears();
    }//GEN-LAST:event_GOMouseClicked

    private void country_arrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_country_arrayActionPerformed
        nationality_field.setText(country_array.getSelectedItem().toString().toUpperCase());
        nationality_field.setForeground(Color.red);      
   
        doc_composer();
    }//GEN-LAST:event_country_arrayActionPerformed

    private void about_usMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_about_usMouseClicked
        OSInfo();
        about_us_frame.setVisible(true);
    }//GEN-LAST:event_about_usMouseClicked

    private void signup_bd_paneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_bd_paneMouseClicked
        signup_TRUE();
 //       populateYears();
        country_ctrl.setEnabled(true);
        //listCountries();
//        setCountries();
    }//GEN-LAST:event_signup_bd_paneMouseClicked

    private void country_ctrlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_country_ctrlMouseClicked
        country_id.setText(country_ctrl.getSelectedItem().toString());
        country_id.setForeground(Color.red);
    }//GEN-LAST:event_country_ctrlMouseClicked

    private void tabFeaturesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabFeaturesMouseClicked
    }//GEN-LAST:event_tabFeaturesMouseClicked

    private void NIF_FIELDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NIF_FIELDActionPerformed
        translate();
        connect();
    }//GEN-LAST:event_NIF_FIELDActionPerformed

    private void country_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_country_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_country_idActionPerformed

    private void country_ctrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_country_ctrlActionPerformed
        country_id.setText(country_ctrl.getSelectedItem().toString().toUpperCase());
        country_id.setForeground(Color.red);
    }//GEN-LAST:event_country_ctrlActionPerformed
    
//NIF Field Focus GL
    private void NIF_FIELDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NIF_FIELDFocusGained
        NIFPlace_FG();
    }//GEN-LAST:event_NIF_FIELDFocusGained
    private void NIF_FIELDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NIF_FIELDFocusLost
        NIFPlace_FL();
    }//GEN-LAST:event_NIF_FIELDFocusLost
    
// Name Field Focus GL
    private void name_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_name_fieldFocusGained
        NPlace_FG();
    }//GEN-LAST:event_name_fieldFocusGained
    private void name_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_name_fieldFocusLost
        NPlace_FL();
    }//GEN-LAST:event_name_fieldFocusLost
    
// Name 2 Field Focus GL
    private void name2_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_name2_fieldFocusGained
        NPlace_2_FG();
    }//GEN-LAST:event_name2_fieldFocusGained
    private void name2_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_name2_fieldFocusLost
        NPlace_2_FL();
    }//GEN-LAST:event_name2_fieldFocusLost
    
// Lastname Field Focus GL
    private void surname_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surname_fieldFocusGained
        LNPlace_FG();     
    }//GEN-LAST:event_surname_fieldFocusGained
    private void surname_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surname_fieldFocusLost
        LNPlace_FL();
    }//GEN-LAST:event_surname_fieldFocusLost
    
// Lastname 2 Field Focus GL
    private void surname_2_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surname_2_fieldFocusGained
        LNPlace_2_FG();
    }//GEN-LAST:event_surname_2_fieldFocusGained
    private void surname_2_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_surname_2_fieldFocusLost
        LNPlace_2_FL();
    }//GEN-LAST:event_surname_2_fieldFocusLost
    
// Email Field Focus GL
    private void email_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_email_fieldFocusGained
        EFPlace_FG();
    }//GEN-LAST:event_email_fieldFocusGained
    private void email_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_email_fieldFocusLost
        EFPlace_FL();
    }//GEN-LAST:event_email_fieldFocusLost
    
// Email 2 Field Focus GL
    private void email_additionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_email_additionFocusGained
        EF2Place_FG();
    }//GEN-LAST:event_email_additionFocusGained
    private void email_additionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_email_additionFocusLost
        EF2Place_FL();
    }//GEN-LAST:event_email_additionFocusLost
    
// Clear All Fields
    private void clear_fieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clear_fieldMouseClicked
        ClearAllFields();
    }//GEN-LAST:event_clear_fieldMouseClicked
    
// Phone Field Focus GL
    private void phone_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phone_fieldFocusGained
        PFPlace_FG();
    }//GEN-LAST:event_phone_fieldFocusGained
    private void phone_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phone_fieldFocusLost
        PFPlace_FL();
    }//GEN-LAST:event_phone_fieldFocusLost
    
// Phone 2 Field Focus GL
    private void addit_phone_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addit_phone_fieldFocusGained
        PF2Place_FG();
    }//GEN-LAST:event_addit_phone_fieldFocusGained
    private void addit_phone_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_addit_phone_fieldFocusLost
        PF2Place_FL();
    }//GEN-LAST:event_addit_phone_fieldFocusLost

// Fax Field Focus GL
    private void fax_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fax_fieldFocusGained
        FXPlace_FG();
    }//GEN-LAST:event_fax_fieldFocusGained
    private void fax_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fax_fieldFocusLost
        FXPlace_FL();
    }//GEN-LAST:event_fax_fieldFocusLost

    private void phone_fieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phone_fieldKeyTyped
       if(phone_field.getText().length() == 9){
           evt.consume();
       }       
        char numbers =  evt.getKeyChar();
        if(!Character.isDigit(numbers)){
            evt.consume();
        }
    }//GEN-LAST:event_phone_fieldKeyTyped

    private void doc_letter_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doc_letter_comboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_doc_letter_comboActionPerformed

    private void fax_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fax_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fax_fieldActionPerformed

    private void isTouristActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isTouristActionPerformed
        
    if(isTourist.isSelected()== true){
        id_label.setText("Numero de Pasaporte*: ");
        id_local_field.setText("Introduzca su numero de pasaporte");
        
        doc_letter_combo.setVisible(false);
        letter_result.setVisible(false);
        
        isTourist.setText("Resident");
        imageSwitch();
        
    }else{
        id_label.setText("Numero de Documento*: ");
        id_local_field.setText("Num. documento");
        letter_result.setVisible(true);
        isTourist.setText("Tourist");
        doc_composer();
        imageSwitch();
    }
     
    }//GEN-LAST:event_isTouristActionPerformed

    /* ID Local Field Focus GL + key typed & released */
    private void id_local_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_local_fieldFocusGained
        LocalFPlace_FG();
    }//GEN-LAST:event_id_local_fieldFocusGained
    private void id_local_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_local_fieldFocusLost
        LocalFPlace_FL();
    }//GEN-LAST:event_id_local_fieldFocusLost
    private void id_local_fieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_local_fieldKeyTyped
        //declare main variables
        String nfield = nationality_field.getText();
        int locfield = id_local_field.getText().length();
        String carr = country_array.getSelectedItem().toString();
        
       if(nfield.equals("ESPAÑA") || nfield.equals("ESPANA")){
            if(locfield == 8){
                id_local_field.setBackground(new Color(0,204,51));
                id_local_field.setForeground(new Color(0,0,0));
                
            evt.consume();
            }else{
                id_local_field.setBackground(new Color(255,51,0));
                id_local_field.setForeground(new Color(0,0,0));
            }
            }else if (nfield.equals(carr) && !nfield.equals("ESPANA") || !nfield.equals("ESPAÑA")){
            if(locfield == 7){
                id_local_field.setBackground(new Color(0,204,51));
                id_local_field.setForeground(new Color(0,0,0));
                
            evt.consume();
            }else{
                id_local_field.setBackground(new Color(255,51,0));
                id_local_field.setForeground(new Color(0,0,0));
            }
        }else{
            if(locfield > 8){
                id_local_field.setBackground(new Color(0,204,51));
                id_local_field.setForeground(new Color(0,0,0));                
            }else{
                evt.consume();
                id_local_field.setBackground(new Color(255,51,0));
                id_local_field.setForeground(new Color(0,0,0));
            }
                    
        }
     
        char numbers =  evt.getKeyChar();
        if(!Character.isDigit(numbers)){
            evt.consume();
        }
    }//GEN-LAST:event_id_local_fieldKeyTyped
    private void id_local_fieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_local_fieldKeyReleased
        char numbers = evt.getKeyChar();
        if(!Character.isDigit(numbers)){
            evt.consume();
        }
        int num = Integer.parseInt(id_local_field.getText());
        letter_result.setText(NIFCalculator(num));
        
    }//GEN-LAST:event_id_local_fieldKeyReleased

    private void sex_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sex_comboActionPerformed
        String txt =sex_combo.getSelectedItem().toString();
        sex_field.setForeground(new Color(255,255,255));
        sex_field.setText(txt);
    }//GEN-LAST:event_sex_comboActionPerformed

    private void GOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GOActionPerformed
      connect();
      populateYears();
    }//GEN-LAST:event_GOActionPerformed

    private void dayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dayActionPerformed
       String dd = day.getSelectedItem().toString();
       day_field.setText(dd);
       //int numerication = Integer.parseInt(day_field.getText());
    }//GEN-LAST:event_dayActionPerformed

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
     String mon = month.getSelectedItem().toString();
        month_field.setText(mon);
    }//GEN-LAST:event_monthActionPerformed

    private void yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearActionPerformed
     String yyyy = year.getSelectedItem().toString();
        year_field.setText(yyyy);
    }//GEN-LAST:event_yearActionPerformed

    private void address_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_address_fieldFocusGained
        ADPlace_FG();
    }//GEN-LAST:event_address_fieldFocusGained

    private void address_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_address_fieldFocusLost
        ADPlace_FL();
    }//GEN-LAST:event_address_fieldFocusLost

    private void number_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_number_fieldFocusGained
        NUFPlace_FG();
    }//GEN-LAST:event_number_fieldFocusGained

    private void number_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_number_fieldFocusLost
        NUFPlace_FL();
    }//GEN-LAST:event_number_fieldFocusLost

    private void apt_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_apt_fieldFocusGained
        FLFPlace_FG();
    }//GEN-LAST:event_apt_fieldFocusGained

    private void apt_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_apt_fieldFocusLost
        FLFPlace_FL();
    }//GEN-LAST:event_apt_fieldFocusLost

    private void door_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_door_fieldFocusGained
        DORPlace_FG();
    }//GEN-LAST:event_door_fieldFocusGained

    private void door_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_door_fieldFocusLost
        DORPlace_FL();
    }//GEN-LAST:event_door_fieldFocusLost

    private void staircase_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staircase_fieldFocusGained
        StairFPlace_FG();
    }//GEN-LAST:event_staircase_fieldFocusGained

    private void staircase_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_staircase_fieldFocusLost
        StairFPlace_FL();
    }//GEN-LAST:event_staircase_fieldFocusLost

    private void block_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_block_fieldFocusGained
        BKFPlace_FG();
    }//GEN-LAST:event_block_fieldFocusGained

    private void block_fieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_block_fieldFocusLost
        BKFPlace_FL();
    }//GEN-LAST:event_block_fieldFocusLost
     
    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Dark Metal".equals(info.getClassName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SegistelUI.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
            System.err.println("Error, Class Not Found, Design Not Found, System Design is set as Default");
        }
        // </editor-fold>
        
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SegistelUI().setVisible(true);
        });
    
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton App;
    javax.swing.JButton App1;
    javax.swing.JButton App2;
    javax.swing.JButton App3;
    javax.swing.JButton App4;
    javax.swing.JButton App5;
    javax.swing.JButton App6;
    javax.swing.JButton App7;
    javax.swing.JButton GO;
    javax.swing.JTextField NIF_FIELD;
    javax.swing.JLabel OStype;
    javax.swing.JPanel Search_Engine;
    javax.swing.JButton about_us;
    javax.swing.JFrame about_us_frame;
    javax.swing.JButton about_us_show;
    javax.swing.JButton add_picture;
    javax.swing.JTextField addit_phone_field;
    javax.swing.JTextField address_field;
    javax.swing.JLabel address_label;
    javax.swing.JLabel address_number;
    javax.swing.JTextField apt_field;
    javax.swing.JLabel aus_head_label;
    javax.swing.JTextField block_field;
    javax.swing.JPanel body;
    javax.swing.JPanel body_aus;
    javax.swing.JLabel browsers_label;
    javax.swing.JButton cancel;
    javax.swing.JButton chrome;
    javax.swing.JComboBox<String> city_combo;
    javax.swing.JTextField city_field;
    javax.swing.JButton clear_field;
    javax.swing.JButton close_action;
    javax.swing.JLabel comment_label;
    javax.swing.JPanel contact_form;
    javax.swing.JLabel copyright_label;
    javax.swing.JComboBox<String> country_array;
    javax.swing.JComboBox<String> country_ctrl;
    javax.swing.JTextField country_id;
    javax.swing.JComboBox<String> day;
    javax.swing.JTextField day_field;
    javax.swing.JLabel day_label;
    javax.swing.JLabel dob_label;
    javax.swing.JComboBox<String> doc_letter_combo;
    javax.swing.JTextField door_field;
    javax.swing.JPanel ea_head_label;
    javax.swing.JButton edge;
    javax.swing.JButton edit_data;
    javax.swing.JTextField email_addition;
    javax.swing.JTextField email_field;
    javax.swing.JLabel email_label;
    javax.swing.JLabel email_label1;
    javax.swing.JPanel exact_home;
    javax.swing.JTextField fax_field;
    javax.swing.JButton firefox;
    javax.swing.JPanel footer;
    javax.swing.JLabel footnote;
    javax.swing.JPanel general_info;
    javax.swing.JPanel geo_info;
    javax.swing.JLabel head_label;
    javax.swing.JLabel head_label1;
    javax.swing.JPanel header;
    javax.swing.JPanel header_aus1;
    javax.swing.JPanel header_aus2;
    javax.swing.JLabel home_add_head_label;
    javax.swing.JPanel home_addr_pane;
    javax.swing.JPanel homepage;
    javax.swing.JLabel id_label;
    javax.swing.JTextField id_local_field;
    javax.swing.JButton ie;
    javax.swing.JLabel image_controller;
    javax.swing.JLabel infolabel;
    javax.swing.JPanel internal_internet_pane;
    javax.swing.JScrollPane internet_brs;
    javax.swing.JToggleButton isTourist;
    javax.swing.JPanel jPanel1;
    javax.swing.JSeparator jSeparator3;
    javax.swing.JTextField letter_result;
    javax.swing.JButton localised;
    javax.swing.JButton login;
    javax.swing.JLabel logo;
    javax.swing.JLabel logo_aus;
    javax.swing.JLabel logo_head;
    javax.swing.JPanel main_address;
    javax.swing.JScrollPane main_app_scroller;
    javax.swing.JComboBox<String> month;
    javax.swing.JLabel month_combo_label;
    javax.swing.JTextField month_field;
    javax.swing.JLabel name1_label;
    javax.swing.JTextField name2_field;
    javax.swing.JLabel name2_label;
    javax.swing.JTextField name_field;
    javax.swing.JLabel nat_label;
    javax.swing.JTextField nationality_field;
    javax.swing.JPanel navbar;
    javax.swing.JTextField number_field;
    javax.swing.JLabel office_label;
    javax.swing.JLabel office_label1;
    javax.swing.JScrollPane office_pane_main;
    javax.swing.JScrollPane office_pane_main1;
    javax.swing.JPanel office_pane_secondary;
    javax.swing.JPanel office_pane_secondary1;
    javax.swing.JLabel os_1;
    javax.swing.JLabel os_2;
    javax.swing.JLabel os_3;
    javax.swing.JLabel os_4;
    javax.swing.JPanel panel;
    javax.swing.JScrollPane pers_info;
    javax.swing.JPanel personal_info;
    javax.swing.JLabel personal_info_label;
    javax.swing.JLabel phone2_label;
    javax.swing.JTextField phone_field;
    javax.swing.JLabel phone_label;
    javax.swing.JPanel picture_frame;
    javax.swing.JLabel postal_code_label;
    javax.swing.JLabel postal_code_label1;
    javax.swing.JTextField postalcode_field;
    javax.swing.JButton save;
    javax.swing.JSeparator separation1;
    javax.swing.JSeparator separation2;
    javax.swing.JSeparator separation3;
    javax.swing.JComboBox<String> sex_combo;
    javax.swing.JTextField sex_field;
    javax.swing.JLabel sex_label;
    javax.swing.JLabel sex_label1;
    javax.swing.JButton sign_up_foot;
    javax.swing.JButton signup_bd_pane;
    javax.swing.JTextField staircase_field;
    javax.swing.JLabel surname2_label;
    javax.swing.JTextField surname_2_field;
    javax.swing.JTextField surname_field;
    javax.swing.JLabel surname_label;
    javax.swing.JLabel sv_label;
    javax.swing.JTabbedPane tabFeatures;
    javax.swing.JSeparator underline;
    javax.swing.JButton update_app;
    javax.swing.JComboBox<String> via_combo;
    javax.swing.JLabel via_label;
    javax.swing.JLabel via_label1;
    javax.swing.JLabel via_label2;
    javax.swing.JLabel via_label3;
    javax.swing.JLabel via_label4;
    javax.swing.JLabel welcome_description;
    javax.swing.JLabel welcome_sign;
    javax.swing.JComboBox<Integer> year;
    javax.swing.JLabel year_combo_label;
    javax.swing.JTextField year_field;
    // End of variables declaration//GEN-END:variables
}
