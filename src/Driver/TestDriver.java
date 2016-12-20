package Driver;

import EAs.*;

/**
 * This TestDriver class is specifically written to test Ex2.
 *
 * @author Xiaogang
 */
public class TestDriver {
    
    public static void main(String[] args) {
        EAConfig config = new EAConfig();
        config.EAname = args[0];
        config.cityAmount = Integer.parseInt(args[1]);
        EA ea = EAFactory.getEA(config);
        ea.start();
        ea.report();
    }
    
}
