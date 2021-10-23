package P1;

import IA.Gasolina.Gasolineras;
import aima.search.framework.HeuristicFunction;
import java.util.ArrayList;

public class P1Heuristica implements HeuristicFunction{

    public double getHeuristicValue(Object o) {
        // Agafem les gasolineras, totes les distancies recorregudes per tots els camios i els diposits que han omplert
        P1Estat board = (P1Estat)o;
        Gasolineras gas = P1Estat.getGasolineras();
        //ArrayList<Integer> distancia = board.getDistancia();
        ArrayList<Integer> diposit = board.getDiposit();

        int beneficisAvui = board.getBeneficisTotals();
        int beneficisDema = 0;

        // Calcular els beneficis de dema sense tenir en compta els kilometres recorreguts
        /*
        int idPeticio;
        for(int i=0;i < gas.size();i++){
            for(int j=0; j<gas.get(i).getPeticiones().size();j++){
                if(board.estatPeticio(i,j)) {
                    idPeticio = gas.get(i).getPeticiones().get(j);
                    beneficisDema += (P1Estat.PreuDiposit * ((100 - Math.pow(2, (idPeticio + 1))) / 100));
                }
            }
        }
*/
        return (-beneficisAvui);
        //return -(beneficisAvui-beneficisDema);
    }
}
