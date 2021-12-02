package inventorysystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Duy Hua
 */

/**This class create the Modify Product form*/
public class ModifyProductController implements Initializable {

    @FXML
    private TextField searchPart;
    @FXML
    private Button addBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private TableView<Part> availableParts;
    @FXML
    private TableColumn<Part, Integer> availablePartId;
    @FXML
    private TableColumn<Part, String> availablePartName;
    @FXML
    private TableColumn<Part, Integer> availablePartStock;
    @FXML
    private TableColumn<Part, Double> availablePartPrice;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TableView<Part> associatedParts;
    @FXML
    private TableColumn<Part, Integer> associatedPartId;
    @FXML
    private TableColumn<Part, String> associatedPartName;
    @FXML
    private TableColumn<Part, Integer> associatedPartStock;
    @FXML
    private TableColumn<Part, Double> associatedPartPrice;
    @FXML
    private TextField productName;
    @FXML
    private TextField productId;
    @FXML
    private TextField productStock;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMax;
    @FXML
    private TextField productMin;
    @FXML
    private Label exceptionLabel;
    
    private static ObservableList<Part> allAssociatedParts = FXCollections.observableArrayList();;
    private static ObservableList<Part> allAvailableParts = FXCollections.observableArrayList();;
    private ObservableList<Part> matchedParts = FXCollections.observableArrayList();
    
    private Product modifiedProduct;
    
    private int productIndex;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        productId.setDisable(true);
    }    
    
    /**
     * This method will fill all the information when user switch
     * to the Modify Product scene
     * @param selectedProduct
     * @param index 
     */
    public void preFillText(Product selectedProduct, int index) {
       productIndex = index;
       productId.setText(Integer.toString(selectedProduct.getId()));
       productName.setText(selectedProduct.getName());
       productPrice.setText(Double.toString(selectedProduct.getPrice()));
       productStock.setText(Integer.toString(selectedProduct.getStock()));
       productMin.setText(Integer.toString(selectedProduct.getMin()));
       productMax.setText(Integer.toString(selectedProduct.getMax()));
              
    }

    @FXML
    private void onSearchEnterKey(ActionEvent event) {
        matchedParts.clear();
        String searchInput = searchPart.getText();
        if (searchInput.isEmpty()) {
            availableParts.setItems(allAvailableParts);
        } else {
        for (Part part : allAvailableParts) {
            String matchedPart = part.getName();
            if (isNumber(searchInput)) {
                if (part.getId() == Integer.parseInt(searchInput)) {
                    matchedParts.add(part);
                }
            } else {
                for (int i = 0; i < matchedPart.length(); i++) {
                    for (int j = i+1; j <= matchedPart.length(); j++) {
                        if (matchedPart.substring(i,j).toLowerCase().equals(searchInput.toLowerCase())) {
                            matchedParts.add(part);
                        }
                    }
                }
            }

        }
        availableParts.setItems(matchedParts);
        }
    }

    /**
     * This method will add selected Part to the Associated Part
     * table when the add button is clicked
     * @param event 
     */
    @FXML
    private void onAddBtnPushed(ActionEvent event) {
        Part selectedPart = (Part)availableParts.getSelectionModel().getSelectedItem();
        
        allAssociatedParts.add(selectedPart);
    }

    /**
     * This method will remove the selected Part in the Associated Part
     * when the remove button is clicked
     * @param event 
     */
    @FXML
    private void onRemoveBtnPushed(ActionEvent event) {
        Part selectedPart = (Part)associatedParts.getSelectionModel().getSelectedItem();
        
        allAssociatedParts.remove(selectedPart);
    }

    /**
     * This method will validate user's inputs and update the
     * selected Product when Save button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onSaveBtnPushed(ActionEvent event) throws IOException {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();
        loader.setLocation(getClass().getResource("Inventory.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        InventoryController controller = loader.getController();
        
        String errorMessage = "Exception: ";
        
        boolean flag = true;
        
        // Check if the part name is empty
        if ("".equals(productName.getText())) {
            errorMessage += "Part name can not be empty\n";
            flag = false;
        }
        
        // Check if the partPrice is double
        try {
            Double.parseDouble(productPrice.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part price must be a double\n";
            flag = false;
        }
        
        // Check if part Stock is integer
        try {
            Integer.parseInt(productStock.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Inventory must be an interger\n";
            flag = false;
        }
        
        // Check if part Min is integer
        try {
            Integer.parseInt(productMin.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Min must be an interger\n";
            flag = false;
        }
        
        // Check if part Max is integer
        try {
            Integer.parseInt(productMax.getText());
        } catch (NumberFormatException e) {
            errorMessage += "Part Max must be an interger\n";
            flag = false;
        }
        
        if (flag) {
            // Check if Min value is smaller than Max
            if (Integer.parseInt(productMin.getText()) >= Integer.parseInt(productMax.getText())) {
                errorMessage += "Min value should be smaller than Max value\n";
                flag = false;
            }
            // Check if Inventory is between Min and Max
            else if (Integer.parseInt(productStock.getText()) > Integer.parseInt(productMax.getText()) ||
                Integer.parseInt(productStock.getText()) < Integer.parseInt(productMin.getText())) {
                errorMessage += "Inv must be between Min and Max\n";
                flag = false;
            }
        }
        
        // Return nothing if there is invalid input
        if (!flag) {
            exceptionLabel.setText(errorMessage);
            return;
        }
        
        modifiedProduct = new Product(
                            productName.getText(), 
                            Double.parseDouble(productPrice.getText()), 
                            Integer.parseInt(productStock.getText()), 
                            Integer.parseInt(productMin.getText()), 
                            Integer.parseInt(productMax.getText()));
        
        modifiedProduct.setAssociatedParts(allAssociatedParts);
        controller.updateProduct(productIndex, modifiedProduct);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method will switch back to the main scene 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void onCancelBtnPushed(ActionEvent event) throws IOException {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader();
        loader.setLocation(getClass().getResource("Inventory.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }
    
    public void inititalizeTable(ObservableList<Part> allParts, Product selectedProduct) {
        allAvailableParts = allParts;
        allAssociatedParts = selectedProduct.getAssociatedParts();
        
        availableParts.setItems(allAvailableParts);
        associatedParts.setItems(allAssociatedParts);
        
        availablePartId.setCellValueFactory(new PropertyValueFactory("id"));
        availablePartName.setCellValueFactory(new PropertyValueFactory("name"));
        availablePartPrice.setCellValueFactory(new PropertyValueFactory("price"));
        availablePartStock.setCellValueFactory(new PropertyValueFactory("stock"));
        
        associatedPartId.setCellValueFactory(new PropertyValueFactory("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory("name"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory("price"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory("stock"));
    }
    
    /**
     * This method will check if the input is numeric or not
     * @param input
     * @return 
     */
    private boolean isNumber (String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
}
