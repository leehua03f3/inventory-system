package inventorysystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Duy Hua
 */

/**This class create the Add Part form*/
public class AddPartController implements Initializable {

    @FXML
    private Label toggleLabel;
    @FXML
    private TextField partId;
    @FXML
    private TextField partName;
    @FXML
    private TextField partStock;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partToggle;
    @FXML
    private TextField partMin;
    @FXML
    private RadioButton radioBtnInHouse;
    @FXML
    private RadioButton radioBtnOutsourced;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Label exceptionLabel;
    
    private InHouse newPartInHouse;
    
    private Outsourced newPartOutsourced;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set "Machine ID" is the default toggle label
        toggleLabel.setText("Machine ID");
        
        //Set part ID text field to be disable
        partId.setDisable(true);
    }    

    /**
     * Change toggle label to Machine ID when In-House
     * radio button is selected
     * @param event
     */
    @FXML
    private void onInHouse(ActionEvent event) {
        toggleLabel.setText("Machine ID");
    }

    /**
     * Change toggle label to Company Name when Outsourced
     * radio button is selected
     * @param event 
     */
    @FXML
    private void onOutsourced(ActionEvent event) {
        toggleLabel.setText("Company Name");
    }

    
    /**
     * This method will validate user's inputs and save the new part
     * when the save button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void saveButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Inventory.fxml"));
        Parent mainFormParent = loader.load();

        Scene mainFormScene = new Scene(mainFormParent);

        InventoryController controller = loader.getController();
        
        String errorMessage = "Exception: ";
        
        boolean flag = true;
        
        // Check if the part name is empty
        if ("".equals(partName.getText())) {
            errorMessage += "Part name can not be empty\n";
            flag = false;
        }
        
        // Check if the partPrice is double
        try {
            Double.parseDouble(partPrice.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part price must be a double\n";
            flag = false;
        }
        
        // Check if part Stock is integer
        try {
            Integer.parseInt(partStock.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Inventory must be an interger\n";
            flag = false;
        }
        
        // Check if part Min is integer
        try {
            Integer.parseInt(partMin.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Min must be an interger\n";
            flag = false;
        }
        
        // Check if part Max is integer
        try {
            Integer.parseInt(partMax.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Max must be an interger\n";
            flag = false;
        }
        
        if ("Machine ID".equals(toggleLabel.getText())) {
            // Check if Machine ID is integer
            try {
                Integer.parseInt(partToggle.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Part Machine ID must be an interger\n";
                flag = false;
            }
        } else {
            // Check if Company Name is empty
            if ("".equals(partToggle.getText())) {
                errorMessage += "Part company name can not be empty\n";
                flag = false;
            }
        }
        
        if (flag) {
            // Check if Min value is smaller than Max
            if (Integer.parseInt(partMin.getText()) >= Integer.parseInt(partMax.getText())) {
                errorMessage += "Min value should be smaller than Max value\n";
                flag = false;
            }
            // Check if Inventory is between Min and Max
            else if (Integer.parseInt(partStock.getText()) > Integer.parseInt(partMax.getText()) ||
                Integer.parseInt(partStock.getText()) < Integer.parseInt(partMin.getText())) {
                errorMessage += "Inv must be between Min and Max\n";
                flag = false;
            }
        }
        
        // Return nothing if there is invalid input
        if (!flag) {
            exceptionLabel.setText(errorMessage);
            return;
        }
        
        if ("Machine ID".equals(toggleLabel.getText())) {
            newPartInHouse = new InHouse(
                                partName.getText(), 
                                Double.parseDouble(partPrice.getText()), 
                                Integer.parseInt(partStock.getText()), 
                                Integer.parseInt(partMin.getText()), 
                                Integer.parseInt(partMax.getText()) 
                                );
            newPartInHouse.setMachineId(Integer.parseInt(partToggle.getText()));
            controller.addPart(newPartInHouse);
        }
        else {
            newPartOutsourced = new Outsourced( 
                                partName.getText(), 
                                Double.parseDouble(partPrice.getText()), 
                                Integer.parseInt(partStock.getText()), 
                                Integer.parseInt(partMin.getText()), 
                                Integer.parseInt(partMax.getText())
                                );
            newPartOutsourced.setCompanyName(partToggle.getText());
            controller.addPart(newPartOutsourced);
        }

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainFormScene);
        window.show();
    }

    /**
     * This method will switch back to the previous scene when the assigned button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void cancelButtonPushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
}