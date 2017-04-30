package pt.isec.cubi.tp1_cubi_2016_17;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RegistosController {

    private Registo reg;

    private static RegistosController instance = null;

    protected RegistosController() {
        reg = new Registo();
    }

    public static RegistosController getInstance() {
        if(instance == null) {
            instance = new RegistosController();
        }
        return instance;
    }

    public Registo getRegisto(){
        if (reg.isComplete()){
            saveRegisto();
            reg = new Registo();
        }
        return reg;
    }

    public void setRegisto(Registo registo){
        reg = registo;
        if (reg.isComplete()) {
            saveRegisto();
            reg = new Registo();
        }
    }
    private void saveRegisto(){
        // TODO
    }
}
