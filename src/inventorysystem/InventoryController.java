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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 *
 * @author Duy Hua
 */

/**This class create the main form for the Inventory Software*/
public class InventoryController implements Initializable {

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partId;
    @FXML
    private TableColumn<Part, String> partName;
    @FXML
    private TableColumn<Part, Integer> partInv;
    @FXML
    private TableColumn<Part, Double> partPrice;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productId;
    @FXML
    private TableColumn<Product, String> productName;
    @FXML
    private TableColumn<Product, Integer> productInv;
    @FXML
    private TableColumn<Product, Double> productPrice;
    @FXML
    private Button exitButton;
    @FXML
    private TextField searchPart;
    @FXML
    private TextField searchProduct;
    @FXML
    private Label errorMessage;
    
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Part> matchedParts = FXCollections.observableArrayList();
    private ObservableList<Product> matchedProducts = FXCollections.observableArrayList();
    
    /**
     * This method will add new part into the Observable List
     * @param newPart 
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    /**
     * This method will add new product into the Observable List
     * @param newProduct 
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
   
    /**
     * This method will update existing part
     * @param index
     * @param updatedPart 
     */
    public void updatePart(int index, Part updatedPart) {
        allParts.set(index, updatedPart);
    }
    
    /**
     * This method will update existing product
     * @param index
     * @param updatedProduct 
     */
    public void updateProduct(int index, Product updatedProduct) {
        allProducts.set(index, updatedProduct);
    }
        
    /**
     * This method will close the stage when the button is clicked
     * @param event 
     */
    @FXML
    private void exitButtonPushed(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method will switch to the "add part" scene when the assigned button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void partAddButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addPart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(scene);
        window.show();
    }

    /**
     * This method will switch to the "modify part" scene when the assigned button is clicked
     * @param event 
     */
    @FXML
    private void partModifyButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyPart.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        
        ModifyPartController controller = loader.getController();
        controller.preFillText(selectedPart, allParts.indexOf(selectedPart));
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method will delete the selected part on the table
     * when the assigned button is clicked
     * @param event 
     */
    @FXML
    private void partDeleteButtonPushed(ActionEvent event) {
        Part selectedPart = (Part)partTable.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null)
            return;
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Parts");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this part?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            allParts.remove(selectedPart);
        } else {
            return;
        }

    }

    /**
     * This method will switch to add Product scene when the assigned
     * button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void productAddButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addProduct.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        AddProductController controller = loader.getController();
        controller.inititalizeTable(allParts);
                
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method will switch to the modify product scene when
     * the assigned button is clicked
     * @param event
     * @throws IOException 
     */
    @FXML
    private void productModifyButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyProduct.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        
        ModifyProductController controller = loader.getController();
        controller.inititalizeTable(allParts, selectedProduct);
        controller.preFillText(selectedProduct, allProducts.indexOf(selectedProduct));
                
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * This method will delete the selected product on the table
     * when the assigned button is clicked
     * @param event 
     */
    @FXML
    private void productDeleteButtonPushed(ActionEvent event) {
        Product selectedProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        
        if (selectedProduct == null)
            return;
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Products");
        alert.setHeaderText("Delete");
        alert.setContentText("Do you want to delete this products?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // Raise error if the selected product has part
            if (!selectedProduct.getAssociatedParts().isEmpty()) {
                errorMessage.setText("This product has parts");
                return;
            } else {
                allProducts.remove(selectedProduct);
            }
        } else {
            return;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Setting up default Parts and Products when the software starts
        partTable.setItems(allParts);
        productTable.setItems(allProducts);
        
        partId.setCellValueFactory(new PropertyValueFactory("id"));
        partName.setCellValueFactory(new PropertyValueFactory("name"));
        partPrice.setCellValueFactory(new PropertyValueFactory("price"));
        partInv.setCellValueFactory(new PropertyValueFactory("stock"));
        
        productId.setCellValueFactory(new PropertyValueFactory("id"));
        productName.setCellValueFactory(new PropertyValueFactory("name"));
        productPrice.setCellValueFactory(new PropertyValueFactory("price"));
        productInv.setCellValueFactory(new PropertyValueFactory("stock"));
        
        if (allParts.isEmpty()) {
            allParts.add(new InHouse("Brakes", 15.00, 10, 1, 100));
            allParts.add(new Outsourced("Wheel", 11.00, 16, 1, 100));
        }
        
        if (allProducts.isEmpty()) {
            allProducts.add(new Product("Gaint Bike", 299.99, 5, 1, 100));
            allProducts.add(new Product("Tricycle", 99.99, 3, 1, 100));
        }
    }

    /**
     * This method will enable the Part search function when user hit
     * enter on the search box
     * @param event 
     */
    @FXML
    private void searchPartEnterKey(ActionEvent event) {
        matchedParts.clear();
        String searchInput = searchPart.getText();
        if (searchInput.isEmpty()) {
            partTable.setItems(allParts);
        } else {
        for (Part part : allParts) {
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
        partTable.setItems(matchedParts);
        }
    }
    
    /**
     * This method will enable the Product search function when user
     * hit enter on the search box
     * @param event 
     */
    @FXML
    private void searchProductEnterKey(ActionEvent event) {
        matchedProducts.clear();
        String searchInput = searchProduct.getText();
        if (searchInput.isEmpty()) {
            productTable.setItems(allProducts);
        } else {
        for (Product product : allProducts) {
            String matchedProduct = product.getName();
            if (isNumber(searchInput)) {
                if (product.getId() == Integer.parseInt(searchInput)) {
                    matchedProducts.add(product);
                }
            } else {
                for (int i = 0; i < matchedProduct.length(); i++) {
                    for (int j = i+1; j <= matchedProduct.length(); j++) {
                        if (matchedProduct.substring(i,j).toLowerCase().equals(searchInput.toLowerCase())) {
                            matchedProducts.add(product);
                        }
                    }
                }
            }

        }
        productTable.setItems(matchedProducts);
        }
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
