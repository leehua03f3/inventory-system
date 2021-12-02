package inventorysystem;

/**
 *
 * @author Duy Hua
 */

/**This class define InHouse object inherits from Part*/
public class InHouse extends Part {
    private int machineId = 0;
    
    public InHouse(String name, double price, int stock, int min, int max) {
        super(name, price, stock, min, max);
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
    
}
