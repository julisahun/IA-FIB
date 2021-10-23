package P1;
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class P1Successor implements SuccessorFunction{

    public List getSuccessors(Object o) {
        // Array de successors
        ArrayList retVal = new ArrayList();
        // P1Estat
        P1Estat board = (P1Estat) o;
        Gasolineras gas = P1Estat.getGasolineras();
        CentrosDistribucion centres = P1Estat.getCentres();
        ArrayList<ArrayList<ArrayList<Integer>>> estat = board.getEstat();
        ArrayList<Integer> dip = board.getDiposit();


        //add
        for(int i=0; i<gas.size();i++){
            // For per totes les peticions d'una gasolinera
            for(int j=0; j<gas.get(i).getPeticiones().size();j++){
                if(board.estatPeticio(gas.get(i).getCoordX(),gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j))) {
                    // For per tots els camions
                    for (int t = 0; t < estat.size(); t++) {
                        // For per la ruta d'un camio
                        for (int r = 0; r <= estat.get(t).size(); r++) {
                            P1Estat succesor = new P1Estat(estat, dip);
                            if (succesor.is_add_possible(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r)) {
                                succesor.add(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r);
                                String s = "add gasolinera " + i + " al camio " + t;
                                retVal.add(new Successor(s, succesor));
                            }
//                            if (succesor.is_replace_possible(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r)) {
//                                succesor.replace(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r);
//                                String s = "replace gasolinera " + i + " al camio " + t;
//                                retVal.add(new Successor(s, succesor));
//                            }
                        }
                    }
                }
            }
        }
        //replace
//        for(int i=0; i<gas.size();i++) {
//            // For per totes les peticions d'una gasolinera
//            for (int j = 0; j < gas.get(i).getPeticiones().size(); j++) {
//                if (board.estatPeticio(gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j))) {
//                    // For per tots els camions
//                    for (int t = 0; t < estat.size(); t++) {
//                        // For per la ruta d'un camio
//                        for (int r = 0; r < estat.get(t).size(); r++) {
//                            P1Estat succesor = new P1Estat(estat, dip);
//                            if (succesor.is_replace_possible(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r)) {
//                                succesor.replace(t, gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j), r);
//                                String s = "replace gasolinera " + i + " al camio " + t;
//                                retVal.add(new Successor(s, succesor));
//                            }
//                        }
//                    }
//                }
//            }
//        }
        //move
//        for(int i=0; i<estat.size();i++){
//            // For per la ruta d'un camio1
//            for(int j=1; j<estat.get(i).size()-1; j++){
//                // For per tots els camions2
//                for(int t=0; t<estat.size();t++) {
//                    // For per la ruta d'un camio2
//                    for (int r = 1; r <= estat.get(t).size(); r++) {
//                        P1Estat succesor = new P1Estat(estat,dip);
//                        if (succesor.is_move_possible(i, t, j, r)) {
//                            succesor.move(i, t, j, r);
//                            String s = "move gasolinera " + j + "/camio " + i + " per gasolinera " + r + "/camio " + t;
//                            retVal.add(new Successor(s, succesor));
//                        }
//                    }
//                }
//            }
//        }
        //swap
        for(int i=0; i<estat.size();i++){
            // For per la ruta d'un camio1
            for(int j=1; j<estat.get(i).size()-1; j++){
                // For per tots els camions2
                for(int t=i; t<estat.size();t++) {
                    // For per la ruta d'un camio2
                    for (int r = j+1; r < estat.get(t).size() - 1; r++) {
                        P1Estat succesor = new P1Estat(estat, dip);
                        if (succesor.is_swap_possible(i, t, j, r)) {
                            succesor.swap(i, t, j, r);
                            String s = "swap gasolinera " + j + "/camio " + i + " per gasolinera " + r + "/camio " + t;
                            retVal.add(new Successor(s, succesor));
                        }
                    }
                }
            }
        }
        //remove
//        for (int i = 0; i < estat.size(); i++) {
//            // For per la ruta d'un camio
//            for (int j = 1; j < estat.get(i).size() - 1; j++) {
//                P1Estat succesor = new P1Estat(estat, dip);
//                if (succesor.is_remove_possible(i, j)) {
//                    succesor.remove(i, j);
//                    String s = "remove gasolinera " + j + " al camio " + i;
//                    retVal.add(new Successor(s, succesor));
//                }
//            }
//        }


        return retVal;
    }

}
