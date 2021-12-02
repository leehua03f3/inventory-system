package inventorysystem;

/**
 *
 * @author Duy Hua
 */

/**This class define Outsourced object inherits from Part*/
public class Outsourced extends Part {
    private String companyName = "None";
    
    public Outsourced(String name, double price, int stock, int min, int max) {
        super(name, price, stock, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
