package P1;

import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolineras;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class P1SuccessorSA implements SuccessorFunction{

    public List getSuccessors(Object o) {
        // Array de successors
        ArrayList retVal = new ArrayList();
        // P1Estat
        P1Estat board = (P1Estat) o;
        Gasolineras gas = P1Estat.getGasolineras();
        CentrosDistribucion centres = P1Estat.getCentres();
        ArrayList<ArrayList<ArrayList<Integer>>> estat = board.getEstat();
        ArrayList<Integer> dip = board.getDiposit();

        // Generar dos numeros random del 1 al 100
        Random ra = new Random();
        int addValue, swapValue, moveValue, removeValue;

        int pt = 0;
        for(int i=0;i<gas.size();i++){
            pt += gas.get(i).getPeticiones().size();
        }


        //pet.size()*estat.size()*estat.get(i).size()
        int c = 0;
        int pa = 0;
        for (int i = 0; i < estat.size(); i++) {
            c += estat.get(i).size();
            pa += estat.get(i).size()-board.getViatges(i)-1;
        }

        int pp = pt-pa;

        addValue = c*pp;
        moveValue = c*c;
        swapValue = c*c/2;
        removeValue = c;

        int total = addValue + moveValue + swapValue + removeValue;
        boolean seguir = true;

        while(seguir) {
            int rand = ra.nextInt(total);

            int peticioRandom = -1;
            if(pp > 0) peticioRandom = ra.nextInt(pp);

            int rutaRandom = ra.nextInt(c);
            int rutaRandom2 = ra.nextInt(c);


            P1Estat succesor = new P1Estat(estat, dip);

            int camio1=-1, camio2=-1, pos1 = 0, pos2 = 0;

            for(int i=0; i<estat.size();i++){
                for (int j=0; j<estat.get(i).size();j++){
                    if(rutaRandom==0){
                        camio1 = i;
                        pos1 = j;
                    }
                    rutaRandom--;
                }
            }

            for(int i=0; i<estat.size();i++){
                for (int j=0; j<estat.get(i).size();j++){
                    if(rutaRandom2==0){
                        camio2 = i;
                        pos2 = j;
                    }
                    rutaRandom2--;
                }
            }

            int peticioDias = -1, xPeticio = 0, yPeticio = 0, gasolinera = 0, pet = 0;

            if(peticioRandom > 0) {
                for (int i = 0; i < gas.size(); i++) {
                    for (int j = 0; j < gas.get(i).getPeticiones().size(); j++) {
                        if (board.estatPeticio(gas.get(i).getCoordX(), gas.get(i).getCoordY(), gas.get(i).getPeticiones().get(j))) {
                            if(peticioRandom == 0){
                                gasolinera = i;
                                pet = j;
                                peticioDias = gas.get(i).getPeticiones().get(j);
                                xPeticio = gas.get(i).getCoordX();
                                yPeticio = gas.get(i).getCoordY();
                            }
                            peticioRandom--;
                        }

                    }
                }
            }

            if (peticioRandom >= 0 && rand < addValue) {
                if(succesor.is_add_possible(camio1, xPeticio, yPeticio, peticioDias, pos1)){
                    succesor.add(camio1, xPeticio, yPeticio, peticioDias,pos1);
                    //succesor.peticioResolta(gasolinera, pet);
                    String s = "add gasolinera " + gasolinera + " al camio " + camio1;
                    retVal.add(new Successor(s, succesor));
                    seguir = false;
                }
            } else if (rand >= addValue && rand < addValue + moveValue) {
                /*
                if(succesor.is_move_possible(camio1,camio2,pos1,pos2)){
                    succesor.move(camio1,camio2,pos1,pos2);
                    String s = "move gasolinera " + pos1 + "/camio " + camio1 + " per gasolinera " + pos2 + "/camio " + camio2;
                    retVal.add(new Successor(s, succesor));
                    seguir = false;
                }
                 */
            } else if (rand >= addValue + moveValue && rand < addValue + moveValue + swapValue) {
                if(succesor.is_swap_possible(camio1,camio2,pos1,pos2)){
                    succesor.swap(camio1,camio2,pos1,pos2);
                    String s = "swap gasolinera " + pos1 + "/camio " + camio1 + " per gasolinera " + pos2 + "/camio " + camio2;
                    retVal.add(new Successor(s, succesor));
                    seguir = false;
                }
            } else {
                if(succesor.is_remove_possible(camio1,pos1)){
                    succesor.remove(camio1,pos1);
                    String s = "remove gasolinera "+pos1+" al camio ";
                    retVal.add(new Successor(s, succesor));
                    seguir = false;
                }
            }
        }

        return retVal;
    }
}
