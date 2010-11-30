package core.io.repr;

/**
 *
 * @author Rekin
 */
public class Switch {

    boolean on;

    public Switch(int code) {
        on = code == 1 ? true : false;
    }

    public boolean isOn() {
        return on;
    }
    
}

