package P1;
import IA.Gasolina.CentrosDistribucion;
import IA.Gasolina.Gasolinera;
import IA.Gasolina.Gasolineras;
import IA.Gasolina.CentrosDistribucion;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.lang.Math;

public class P1Estat {
    // Constants problema
    public final static int PreuDiposit = 1000;
    public final static int PreuKm = 2;
    public final static int KmPermesos = 640;

    // Atributs
    // Vector de camions, Vector que representa la ruta dels camions, Vector<Integer> amb posGas, peticio y si centreDist
    private ArrayList<ArrayList<ArrayList<Integer>>> estat;
    // Vector que guarda els beneficis que porta cada camio amb tots els diposits omplerts
    private ArrayList<Integer> diposit;
    // Gasolineras del problema
    private static Gasolineras gasolineras;
    // Centres de distribucio del problema
    private static CentrosDistribucion centres;

    // Metodes privats
    private int calcDistancia(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private int distanciaAnteriorSeguent(int idCamio, int ruta) {
        int posXGas0 = estat.get(idCamio).get(ruta - 1).get(0);
        int posYGas0 = estat.get(idCamio).get(ruta - 1).get(1);
        int posXGas1 = estat.get(idCamio).get(ruta).get(0);
        int posYGas1 = estat.get(idCamio).get(ruta).get(1);
        int posXGas2 = estat.get(idCamio).get(ruta + 1).get(0);
        int posYGas2 = estat.get(idCamio).get(ruta + 1).get(1);

        int result = 0;
        result += calcDistancia(posXGas0, posYGas0, posXGas1, posYGas1);
        result += calcDistancia(posXGas1, posYGas1, posXGas2, posYGas2);
        return result;
    }

    // Simular un estat en el que poder anar a una gasolinera mes al final de la ruta
    private void ferSimulacio(int idCamio, int ruta){
        ArrayList<Integer> centreNou = new ArrayList<>(3);
        centreNou.add(centres.get(idCamio).getCoordX());
        centreNou.add(centres.get(idCamio).getCoordY());
        centreNou.add(-1);
        estat.get(idCamio).add(ruta, centreNou);
        //viatges.set(idCamio, viatges.get(idCamio) + 1);
    }

    // Desfer la simulacio del metode ferSimulacio
    private void desferSimulacio(int idCamio, int ruta){
        estat.get(idCamio).remove(ruta);
        //viatges.set(idCamio, viatges.get(idCamio)-1);
    }

    // Constructura per defecte
    public P1Estat() {
        //Inicialitzacio del estat
        estat = new ArrayList<ArrayList<ArrayList<Integer>>>();
        // Inicialitzacio dels diposits
        diposit = new ArrayList<Integer>();
    }

    // Constructura amb parametres
    public P1Estat(ArrayList<ArrayList<ArrayList<Integer>>> e, ArrayList<Integer> dip) {
        //Inicialitzacio del estat
        estat = new ArrayList<ArrayList<ArrayList<Integer>>>();
        // Inicialitzacio dels diposits
        diposit = new ArrayList<Integer>();

        // Inicialitzar estats
        for(int i=0;i<e.size();i++){
            estat.add(new ArrayList<ArrayList<Integer>>());
            for(int j=0;j<e.get(i).size();j++){
                estat.get(i).add(new ArrayList<Integer>());
                for(int k=0;k<e.get(i).get(j).size();k++) estat.get(i).get(j).add(e.get(i).get(j).get(k));
            }
        }

        // Inicialitzar diposits
        for(int i=0;i<dip.size();i++) diposit.add(dip.get(i));
    }

    // Operacions
    public void move(int idCamio1, int idCamio2, int ruta1, int ruta2){
        ArrayList<Integer> gas1 = estat.get(idCamio1).get(ruta1);
        ArrayList<Integer> gasNext1 = estat.get(idCamio1).get(ruta1+1);
        ArrayList<Integer> gasPrev1 = estat.get(idCamio1).get(ruta1-1);
        ArrayList<Integer> gas2 = estat.get(idCamio2).get(ruta2);
        ArrayList<Integer> gasPrev2 = estat.get(idCamio2).get(ruta2-1);

        // Esborrem la parada de la ruta1
        estat.get(idCamio1).remove(ruta1);
        // Afegim la parada a la ruta2
        estat.get(idCamio2).add(ruta2, gas1);


        int idPeticio = gas1.get(2);
        // Si la parada que movem es una gasolinera li restem el benefi a un camio i li donem a l'altre.
        if (idPeticio != -1){
            if(idPeticio == 0){
                diposit.set(idCamio1, diposit.get(idCamio1) - PreuDiposit);
                diposit.set(idCamio2, diposit.get(idCamio2) + PreuDiposit);
            }
            else{
                diposit.set(idCamio1, diposit.get(idCamio1) - (int)(PreuDiposit*((100 - Math.pow(2,idPeticio))/100)));
                diposit.set(idCamio2, diposit.get(idCamio2) + (int)(PreuDiposit*((100 + Math.pow(2,idPeticio))/100)));
            }
        }

        // Si quedan dos centres de distribucio junts el camio fa un viatge menys
        if(estat.get(idCamio1).size() > 2 && gasPrev1.get(2) == -1 && gasNext1.get(2) == -1) {
            estat.get(idCamio1).remove(ruta1 - 1);
        }
    }

    public void swap(int idCamio1, int idCamio2, int ruta1, int ruta2) {
        ArrayList<Integer> gas1 = estat.get(idCamio1).get(ruta1);
        ArrayList<Integer> gas2 = estat.get(idCamio2).get(ruta2);
        estat.get(idCamio1).set(ruta1, gas2);
        estat.get(idCamio2).set(ruta2, gas1);

        int idPeticio1 = gas1.get(2);
        int idPeticio2 = gas2.get(2);

        // Si la parada que movem es una gasolinera li restem el benefi a un camio i li donem a l'altre.
        if (idPeticio1 != -1){
            if(idPeticio1 == 0){
                diposit.set(idCamio1, diposit.get(idCamio1) - PreuDiposit);
                diposit.set(idCamio2, diposit.get(idCamio2) + PreuDiposit);
            }
            else{
                diposit.set(idCamio1, diposit.get(idCamio1) - (int)(PreuDiposit*((100 - Math.pow(2,idPeticio1))/100)));
                diposit.set(idCamio2, diposit.get(idCamio2) + (int)(PreuDiposit*((100 - Math.pow(2,idPeticio1))/100)));
            }
        }

        if (idPeticio2 != -1){
            if(idPeticio2 == 0){
                diposit.set(idCamio2, diposit.get(idCamio2) - PreuDiposit);
                diposit.set(idCamio1, diposit.get(idCamio1) + PreuDiposit);
            }
            else{
                diposit.set(idCamio2, diposit.get(idCamio2) - (int)(PreuDiposit*((100 - Math.pow(2,idPeticio2))/100)));
                diposit.set(idCamio1, diposit.get(idCamio1) + (int)(PreuDiposit*((100 - Math.pow(2,idPeticio2))/100)));
            }
        }
    }

    public void add(int idCamio, int x, int y, int idPeticio, int Pos) {
        ArrayList<ArrayList<Integer>> parades = estat.get(idCamio);
        ArrayList<Integer> gasNext = parades.get(Pos);
        ArrayList<Integer> gasPrev = parades.get(Pos-1);

        //Afegir la Benzinera a la Ruta
        ArrayList<Integer> gasNew = new ArrayList<>(3);
        gasNew.add(x);
        gasNew.add(y);
        gasNew.add(idPeticio);
        estat.get(idCamio).add(Pos, gasNew);

        // Incrementar el contador de diposits o de viatges
        if (idPeticio != -1){
            if(idPeticio == 0) diposit.set(idCamio, diposit.get(idCamio) + PreuDiposit);
            else diposit.set(idCamio, diposit.get(idCamio) + (int)(PreuDiposit*((100 - Math.pow(2,idPeticio))/100)));
        }

    }

    public void replace(int idCamio, int x, int y, int idPeticio, int ruta)
    {
        remove(idCamio,ruta);
        add(idCamio,x,y,idPeticio,ruta);
    }

    public void remove(int idCamio, int ruta) {
        ArrayList<ArrayList<Integer>> parades = estat.get(idCamio);
        ArrayList<Integer> gasNext = parades.get(ruta + 1);
        ArrayList<Integer> gasPrev = parades.get(ruta - 1);

        // Variable per saber si esborrem una benzinera o un centre de distribucio
        int idPeticio = estat.get(idCamio).get(ruta).get(2);

        //Borra la benzinera de la ruta
        estat.get(idCamio).remove(ruta);

        // Si s'elimina una gasolinera i per aquest motiu queden dos centres de distribucio junts es pot eliminar un
        if (gasPrev.get(2) == -1 && gasNext.get(2) == -1) estat.get(idCamio).remove(ruta);

        // Decrementar el contador de diposits o viatges
        if (idPeticio != -1){
            if(idPeticio == 0) diposit.set(idCamio, diposit.get(idCamio) - PreuDiposit);
            else diposit.set(idCamio, diposit.get(idCamio) - (int)(PreuDiposit*((100 - Math.pow(2,idPeticio))/100)));
        }

    }

    // Metodes publics
    public ArrayList<ArrayList<ArrayList<Integer>>> getEstat() {
        return estat;
    }

    public ArrayList<Integer> getDiposit() {
        return diposit;
    }

    public static Gasolineras getGasolineras(){
        return gasolineras;
    }

    public static CentrosDistribucion getCentres(){
        return centres;
    }

    public int getBeneficisTotals(){
        int beneficisAvui = 0;
        // Calcular els beneficis d'avui
        for(int i=0; i < diposit.size();i++){
            beneficisAvui += (diposit.get(i) - (P1Estat.PreuKm*getDistancia(i)));
        }
        return beneficisAvui;
    }

    public int size()
    {
        return this.estat.size();
    }

    public int getDistancia(int i) {
        int d = 0;
        ArrayList<ArrayList<Integer>> camio = estat.get(i);
        for (int j = 1; j < camio.size(); j++)
        {
            d += calcDistancia(camio.get(j-1).get(0), camio.get(j-1).get(1), camio.get(j).get(0), camio.get(j).get(1));
        }
        return d;
    }

    public int getViatges(int i) {
        int v = 0;
        ArrayList<ArrayList<Integer>> camio = estat.get(i);
        for (int j = 1; j < camio.size(); j++)
        {
            if (camio.get(j).get(2) == -1) v++;
        }
        return v;
    }

    public boolean is_replace_possible(int idCamio, int x, int y, int idPeticio, int ruta)
    {
        if (idPeticio == -1 || ruta <= 0 || ruta >= estat.get(idCamio).size()-2 || estat.get(idCamio).get(ruta).get(2) == -1) return false;
        int d = getDistancia(idCamio);
        d -= distanciaAnteriorSeguent(idCamio,ruta);
        int x0 = estat.get(idCamio).get(ruta-1).get(0);
        int y0 = estat.get(idCamio).get(ruta-1).get(1);
        int x1 = estat.get(idCamio).get(ruta+1).get(0);
        int y1 = estat.get(idCamio).get(ruta+1).get(1);
        d += calcDistancia(x0,y0,x,y)+calcDistancia(x1,y1,x,y);
        return(d < 640);
    }

    public boolean is_move_possible(int idCamio1, int idCamio2, int ruta1, int ruta2){
        int sizeRuta1 = estat.get(idCamio1).size();
        int sizeRuta2 = estat.get(idCamio2).size();
        int paradaX = estat.get(idCamio1).get(ruta1).get(0);
        int paradaY = estat.get(idCamio1).get(ruta1).get(1);
        int idPeticio = estat.get(idCamio1).get(ruta1).get(2);

        boolean simulacio = false;

        // Si es vol afegir una gasolinera al final de la ruta es simula que hi ha un centre de distribucio al final
        if(sizeRuta2 == ruta2 && idPeticio != -1) simulacio = true;

        if(ruta1 <= 0 || ruta1 >= sizeRuta1-1 || ruta2 <= 0 || ruta2 >= estat.get(idCamio2).size()-1) return false;

        // No es pot moure un centre de distribucio a una ruta que no es del mateix camio
        if(idCamio1 != idCamio2 && idPeticio == -1) return false;
        // Retorna si es posible afegir la ruta1 a la posicio ruta2 de la ruta del camio2 i si es posible eliminar la parada ruta1 de la ruta del camio1
        if(!is_add_possible(idCamio2, paradaX, paradaY, idPeticio, ruta2) || !is_remove_possible(idCamio1, ruta1)){
            if(simulacio) desferSimulacio(idCamio2,ruta2);
            return false;
        }

        return true;
    }

    public boolean is_swap_possible(int idCamio1, int idCamio2, int ruta1, int ruta2) {
        if(idCamio1 == idCamio2) return false;
        // Vector amb la ruta del camio1
        ArrayList<ArrayList<Integer>> rutaCamio1 = estat.get(idCamio1);
        // Vector amb la ruta del camio2
        ArrayList<ArrayList<Integer>> rutaCamio2 = estat.get(idCamio2);

        // No es pot fer swap ni al principi ni al final d'una ruta, sempre comença i acaba en un centre de distribucio
        if (ruta1 <= 0 || ruta1 >= rutaCamio1.size() - 1 || ruta2 <= 0 || ruta2 >= rutaCamio2.size() - 1) return false;

        // Un camio nomes pot anar al seu centre de distribucio
        if (idCamio1 != idCamio2 && (rutaCamio1.get(ruta1).get(2) == -1 || rutaCamio2.get(ruta2).get(2) == -1))
            return false;

        // Si a la ruta1 va a parar una gasolinera i a la ruta2 va a parar un centre de distribucio
        if (rutaCamio1.get(ruta1).get(2) == -1 && rutaCamio2.get(ruta2).get(2) != -1) {
            // No podem tenir 3 gasolineras juntas a la ruta1
            if (ruta1 + 1 <= rutaCamio1.size() - 1
                    && rutaCamio1.get(ruta1 - 1).get(2) != -1 && rutaCamio1.get(ruta1 + 1).get(2) != -1) return false;

            // No podem tenir 2 centres de distribucio junts a la ruta2
            if (ruta2 + 1 <= rutaCamio2.size() - 1
                    && (rutaCamio2.get(ruta2 - 1).get(2) == -1 || rutaCamio2.get(ruta2 + 1).get(2) != -1)) return false;
        }
        // Si a la ruta1 va a parar un centre de distribucio i a la ruta2 va a parar una gasolinera
        if (rutaCamio1.get(ruta1).get(2) != -1 && rutaCamio2.get(ruta2).get(2) == -1) {
            // No podem tenir 3 gasolineras juntas a la ruta2
            if (ruta2 + 1 <= rutaCamio2.size() - 1
                    && rutaCamio2.get(ruta2 - 1).get(2) != -1 && rutaCamio2.get(ruta2 + 1).get(2) != -1) return false;

            // No podem tenir 2 centres de distribucio junts a la ruta1
            if (ruta1 + 1 <= rutaCamio1.size() - 1
                    && (rutaCamio1.get(ruta1 - 1).get(2) == -1 || rutaCamio1.get(ruta1 + 1).get(2) != -1)) return false;
        }

        int distancia1 = getDistancia(idCamio1);
        int distancia2 = getDistancia(idCamio2);
        distancia1 -= distanciaAnteriorSeguent(idCamio1, ruta1);
        distancia2 -= distanciaAnteriorSeguent(idCamio2, ruta2);

        distancia1 += calcDistancia(rutaCamio1.get(ruta1-1).get(0), rutaCamio1.get(ruta1-1).get(1), rutaCamio2.get(ruta2).get(0), rutaCamio2.get(ruta2).get(1));
        distancia1 += calcDistancia(rutaCamio1.get(ruta1+1).get(0), rutaCamio1.get(ruta1+1).get(1), rutaCamio2.get(ruta2).get(0), rutaCamio2.get(ruta2).get(1));
        distancia2 += calcDistancia(rutaCamio2.get(ruta2-1).get(0), rutaCamio2.get(ruta2-1).get(1), rutaCamio1.get(ruta1).get(0), rutaCamio1.get(ruta1).get(1));
        distancia2 += calcDistancia(rutaCamio2.get(ruta2+1).get(0), rutaCamio2.get(ruta2+1).get(1), rutaCamio1.get(ruta1).get(0), rutaCamio1.get(ruta1).get(1));

        return (distancia1 <= KmPermesos && distancia2 <= KmPermesos);
    }

    public boolean is_add_possible(int idCamio, int x, int y, int idPeticio, int ruta) {
        boolean simulacio = false;
        // Si es vol afegir una gasolinera al final de la ruta es simula que hi ha un centre de distribucio al final
        if(estat.get(idCamio).size() == ruta && idPeticio != -1 && getViatges(idCamio) < 5){
            ferSimulacio(idCamio,ruta);
            simulacio = true;
        }

        // No es pot afegir ni al principi ni al final de la ruta, sempre comença i acaba en un centre de distribucio
        if (ruta <= 0 || ruta >= estat.get(idCamio).size()) return false;

        // Vector amb la ruta del camio que volem afegir una parada nova
        ArrayList<ArrayList<Integer>> rutaCamio = estat.get(idCamio);
        // Parada anterior a la que volem afegir
        ArrayList<Integer> paradaPrev = rutaCamio.get(ruta - 1);
        // Parada seguent a la que volem afegir
        ArrayList<Integer> paradaNext = rutaCamio.get(ruta);

        // No podem fer mes de 5 viatges
        if (idPeticio == -1 && getViatges(idCamio) + 1 > 5) return false;

        // No es pot afegir si hi ha 3 gasolineras juntas (add al mig de dos gasolineras)
        if (idPeticio != -1 && paradaPrev.get(2) != -1 && paradaNext.get(2) != -1) return false;
        // Comprovar tercera gasolinera add a la dreta
        if (ruta - 2 >= 0 && rutaCamio.get(ruta - 2).get(2) != -1 && paradaPrev.get(2) != -1 && idPeticio != -1)
            return false;
        // Comprovar tercera gasolinera add a la esquerra
        if (ruta + 1 <= estat.get(idCamio).size() - 1 && rutaCamio.get(ruta + 1).get(2) != -1 && paradaNext.get(2) != -1 && idPeticio != -1)
            return false;

        // No es pot afegir un centre de distribucio al costat d'un altre
        if (idPeticio == -1 && (paradaPrev.get(2) == -1 || paradaNext.get(2) == -1)) return false;

        // La distancia nova no pot ser mes gran a KmPermesos km
        int distanciaTotal = getDistancia(idCamio);
        distanciaTotal -= calcDistancia(paradaNext.get(0), paradaNext.get(1), paradaPrev.get(0), paradaPrev.get(1));
        distanciaTotal += calcDistancia(paradaPrev.get(0), paradaPrev.get(1), x, y) +
                calcDistancia(paradaNext.get(0), paradaNext.get(1), x, y);

        if(distanciaTotal > KmPermesos){
            if(simulacio) desferSimulacio(idCamio,ruta);
            return false;
        }

        return true;
    }

    public boolean is_remove_possible(int idCamio, int ruta) {
        // No es pot eliminar ni al principi ni al final de la ruta, sempre comença i acaba en un centre de distribucio
        if (ruta <= 0 || ruta >= estat.get(idCamio).size() - 2) return false;

        // Vector amb la ruta del camio que volem afegir una parada nova
        ArrayList<ArrayList<Integer>> rutaCamio = estat.get(idCamio);
        // Parada anterior a la que volem afegir
        ArrayList<Integer> paradaPrev = rutaCamio.get(ruta - 1);
        // Parada seguent a la que volem afegir
        ArrayList<Integer> paradaNext = rutaCamio.get(ruta + 1);

        int idPeticio = rutaCamio.get(ruta).get(2);

        // Si esborrem un centre de Distribucio i hi ha dues gasolineras juntes pot haber mes de dues en total juntes
        if (idPeticio == -1 && paradaPrev.get(2) != -1 && paradaNext.get(2) != -1) {
            // Comprovar tercera gasolinera a l'esquerra
            if (ruta - 2 >= 0 && rutaCamio.get(ruta - 2).get(2) != -1) return false;
            // Comprovar tercera gasolinera a la dreta
            if (ruta + 2 <= estat.get(idCamio).size() - 1 && rutaCamio.get(ruta + 2).get(2) != -1) return false;
        }

        return true;
    }

    public boolean estatPeticio(int x, int y, int peticio){

        for(int i=0; i<estat.size();i++) {
            for (int j = 0; j < estat.get(i).size(); j++) {
                    if (x == estat.get(i).get(j).get(0) && y == estat.get(i).get(j).get(1) && peticio == estat.get(i).get(j).get(2))
                        return false;
            }
        }
        return true;
    }

    public void escriu() {
        double guanys_totals = 0;
        int camions = estat.size();
        int ruta;
        int x, y, peticio;
        for (int i = 0; i < camions; i++) {
            System.out.println("################################");
            System.out.println("          Ruta Camio " + i);
            System.out.println("################################");
            ruta = estat.get(i).size();
            for (int j = 0; j < ruta; j++) {
                x = estat.get(i).get(j).get(0);
                y = estat.get(i).get(j).get(1);
                peticio = estat.get(i).get(j).get(2);
                if (peticio == -1) System.out.println("Centre de distribució: " + x + " " + y);
                else System.out.println("Gasolinera: " + x + " " + y + " - Peticio " + peticio);
            }
            System.out.println();
            int dist = getDistancia(i);
            double dip = diposit.get(i);
            int v = getViatges(i);
            double benefici = dip - PreuKm * dist;
            System.out.println("Distancia total: " + dist);
            System.out.println("Beneficis totals: " + dip);
            System.out.println("Viatges totals: " + v);
            System.out.println("Beneficis totals - km: " + benefici);
            System.out.println("--------------------------------");
            guanys_totals += benefici;
        }
        System.out.println("=======================");
        System.out.println("Guanys totals:");
        System.out.println("------>" + guanys_totals + "<--------");
        System.out.println("=======================");
    }

    public static void generar_problema() {
        // Generar dos numeros random del 1 al 100
        Random r = new Random();
        int rand1 = r.nextInt(100) + 1;
        int rand2 = r.nextInt(100) + 1;

        // Es generan les gasolineras amb un seed random
        //gasolineras = new Gasolineras(1000, 1234 * rand1 + rand2);
        // Es generen els centres de distribucio amb un seed random
        //centres = new CentrosDistribucion(10, 1, 1234 * rand2 + rand1);

        // Punt extra
        gasolineras = new Gasolineras(100, 1234);
        centres = new CentrosDistribucion(10, 1, 1234);
    }

    // Solucio en la que cap camio va a cap gasolinera
    public void generar_solucio1() {
        int camions = centres.size();
        // Tots els camions tindran una ruta buida
        //for (int i = 0; i < camions; i++) distancia.add(0);
        for (int i = 0; i < camions; i++) diposit.add(0);
        //for (int i = 0; i < camions; i++) viatges.add(1);

        // Els camions començen i acaben al centre de distribució
        for (int i = 0; i < camions; i++) {
            // Inicialització vector d'un camio i
            estat.add(i, new ArrayList<ArrayList<Integer>>());

            // Situarlo en el seu centre de distribució
            estat.get(i).add(0, new ArrayList<Integer>(3));
            estat.get(i).get(0).add(centres.get(i).getCoordX());
            estat.get(i).get(0).add(centres.get(i).getCoordY());
            estat.get(i).get(0).add(-1);

            // El final de la ruta es tornar al centreDistribucio
            estat.get(i).add(1, new ArrayList<Integer>(3));
            estat.get(i).get(1).add(centres.get(i).getCoordX());
            estat.get(i).get(1).add(centres.get(i).getCoordY());
            estat.get(i).get(1).add(-1);
        }
    }

    // Solucio en la que cada camio va a la gasolinera que te mes proxima si pot amb limit de km
    public void generar_solucio2() {
        // Guardem quantes gasolineras i quants camions hi ha
        int camions = centres.size();
        int gas = gasolineras.size();
        // Partim de que cada centre comença i acaba en el centre de distribucio
        generar_solucio1();

        for (int i = 0; i < gas; i++) {
            // Si la gasolinera te peticions pendents
            if (gasolineras.get(i).getPeticiones().size() > 0) {
                // Agafem les seves coordenades i la peticio
                int xgas = gasolineras.get(i).getCoordX();
                int ygas = gasolineras.get(i).getCoordY();
                int peticio = gasolineras.get(i).getPeticiones().get(0);
                // Guardarem dos posibles candidats, en cas que el primer no pogues anar-hi, hi aniria el segon
                ArrayList<Pair> candidats = new ArrayList<>(2);
                // Inicialitzem els dos candidats
                candidats.add(new Pair(-1, -1));
                candidats.add(new Pair(-1, -1));

                for (int j = 0; j < camions; j++) {
                    // Guardem la mida de la ruta que ja porta el camio
                    int midaRutaCamio = estat.get(j).size();
                    // Guardem les coordenades de la seva posicio actual en la ruta sense contar l'ultima parada
                    int camioX = estat.get(j).get(midaRutaCamio - 2).get(0);
                    int camioY = estat.get(j).get(midaRutaCamio - 2).get(1);
                    // Calculem la distancia de la gasolinera al camio
                    int dist = calcDistancia(xgas, ygas, camioX, camioY);
                    Pair nou = new Pair(j, dist);
                    // Si no tenim cap candidat
                    if (candidats.get(0).getY() == -1) candidats.set(0, nou);
                        // Si no tenim segon candidat i la distancia es pitjor que el primer
                    else if (dist <= candidats.get(0).getY() && candidats.get(1).getY() == -1) candidats.set(1, nou);
                        // Si la distancia es millor que el primer candidat
                    else if (dist > candidats.get(0).getY()) {
                        candidats.set(1, candidats.get(0));
                        candidats.set(0, nou);
                    }
                    // Si la distancia es millor que el segon candidat
                    else if (dist > candidats.get(1).getY()) candidats.set(1, nou);
                }
                // Agafem les dades dels dos candidats a anar a la gasolinera
                int candidat1 = candidats.get(0).getX();
                int candidatDist1 = candidats.get(0).getY();
                int midaRutaCandidat1 = estat.get(candidat1).size();
                int candidat2 = candidats.get(1).getX();
                int candidatDist2 = candidats.get(1).getY();
                int midaRutaCandidat2 = estat.get(candidat2).size();
                // Si s'ha trobat candidat1
                if (candidat1 != -1) {
                    // Si es posible afegir la gasolinera al candidat1
                    if (is_add_possible(candidat1, xgas, ygas, peticio, midaRutaCandidat1 - 1)) {
                        add(candidat1, xgas, ygas, peticio, midaRutaCandidat1 - 1);

                        //gasolineras.get(i).getPeticiones().set(0,-1);
                    } else {
                        // Si no es posible afegir al candidat 1 pero no te tots els kilometres
                        if (getDistancia(candidat1) + candidatDist1 < (KmPermesos/2) && estat.get(candidat1).get(midaRutaCandidat1 - 2).get(2) != -1)
                            add(candidat1, centres.get(candidat1).getCoordX(), centres.get(candidat1).getCoordY(), -1, midaRutaCandidat1 - 1);
                        // Si existeix candidat2
                        if (candidat2 != -1) {
                            // Si es posible afegir la gasolinera al candidat2
                            if (is_add_possible(candidat2, xgas, ygas, peticio, midaRutaCandidat2 - 1)) {
                                add(candidat2, xgas, ygas, peticio, midaRutaCandidat2 - 1);

                                //gasolineras.get(i).getPeticiones().set(0,-1);
                            }
                            // Si no es posible afegir al candidat 1 pero no te tots els kilometres
                            else if (getDistancia(candidat2) + candidatDist2 < (KmPermesos/2) && estat.get(candidat2).get(midaRutaCandidat2 - 2).get(2) != -1)
                                add(candidat2, centres.get(candidat2).getCoordX(), centres.get(candidat2).getCoordY(), -1, midaRutaCandidat2 - 1);
                        }
                    }
                }
            }
        }
        // Esborra dos centres de distribucio junts al final de la ruta.
        for (int i = 0; i < camions; i++) {
            int mida = estat.get(i).size();
            int tipus = estat.get(i).get(mida - 2).get(2);
            if (mida > 2 && tipus == -1) remove(i, mida - 2);
        }
    }

    // Solucio en la que cada camio va a la gasolinera que te mes proxima si pot
    public void generar_solucio3() {
        // Guardem quantes gasolineras i quants camions hi ha
        int camions = centres.size();
        int gas = gasolineras.size();
        // Partim de que cada centre comença i acaba en el centre de distribucio
        generar_solucio1();

        for (int i = 0; i < gas; i++) {
            // Si la gasolinera te peticions pendents
            if (gasolineras.get(i).getPeticiones().size() > 0) {
                // Agafem les seves coordenades i la peticio
                int xgas = gasolineras.get(i).getCoordX();
                int ygas = gasolineras.get(i).getCoordY();
                int peticio = gasolineras.get(i).getPeticiones().get(0);
                // Guardarem dos posibles candidats, en cas que el primer no pogues anar-hi, hi aniria el segon
                ArrayList<Pair> candidats = new ArrayList<>(2);
                // Inicialitzem els dos candidats
                candidats.add(new Pair(-1, -1));
                candidats.add(new Pair(-1, -1));

                for (int j = 0; j < camions; j++) {
                    // Guardem la mida de la ruta que ja porta el camio
                    int midaRutaCamio = estat.get(j).size();
                    // Guardem les coordenades de la seva posicio actual en la ruta sense contar l'ultima parada
                    int camioX = estat.get(j).get(midaRutaCamio - 2).get(0);
                    int camioY = estat.get(j).get(midaRutaCamio - 2).get(1);
                    // Calculem la distancia de la gasolinera al camio
                    int dist = calcDistancia(xgas, ygas, camioX, camioY);
                    Pair nou = new Pair(j, dist);
                    // Si no tenim cap candidat
                    if (candidats.get(0).getY() == -1) candidats.set(0, nou);
                        // Si no tenim segon candidat i la distancia es pitjor que el primer
                    else if (dist <= candidats.get(0).getY() && candidats.get(1).getY() == -1) candidats.set(1, nou);
                        // Si la distancia es millor que el primer candidat
                    else if (dist > candidats.get(0).getY()) {
                        candidats.set(1, candidats.get(0));
                        candidats.set(0, nou);
                    }
                    // Si la distancia es millor que el segon candidat
                    else if (dist > candidats.get(1).getY()) candidats.set(1, nou);
                }
                // Agafem les dades dels dos candidats a anar a la gasolinera
                int candidat1 = candidats.get(0).getX();
                int candidatDist1 = candidats.get(0).getY();
                int midaRutaCandidat1 = estat.get(candidat1).size();
                int candidat2 = candidats.get(1).getX();
                int candidatDist2 = candidats.get(1).getY();
                int midaRutaCandidat2 = estat.get(candidat2).size();
                // Si s'ha trobat candidat1
                if (candidat1 != -1) {
                    // Si es posible afegir la gasolinera al candidat1
                    if (is_add_possible(candidat1, xgas, ygas, peticio, midaRutaCandidat1 - 1)) {
                        add(candidat1, xgas, ygas, peticio, midaRutaCandidat1 - 1);

                        //gasolineras.get(i).getPeticiones().set(0,-1);
                    } else {
                        // Si no es posible afegir al candidat 1 pero no te tots els kilometres
                        if (getDistancia(candidat1) + candidatDist1 < KmPermesos && estat.get(candidat1).get(midaRutaCandidat1 - 2).get(2) != -1)
                            add(candidat1, centres.get(candidat1).getCoordX(), centres.get(candidat1).getCoordY(), -1, midaRutaCandidat1 - 1);
                        // Si existeix candidat2
                        if (candidat2 != -1) {
                            // Si es posible afegir la gasolinera al candidat2
                            if (is_add_possible(candidat2, xgas, ygas, peticio, midaRutaCandidat2 - 1)) {
                                add(candidat2, xgas, ygas, peticio, midaRutaCandidat2 - 1);

                                //gasolineras.get(i).getPeticiones().set(0,-1);
                            }
                            // Si no es posible afegir al candidat 1 pero no te tots els kilometres
                            else if (getDistancia(candidat2) + candidatDist2 < KmPermesos && estat.get(candidat2).get(midaRutaCandidat2 - 2).get(2) != -1)
                                add(candidat2, centres.get(candidat2).getCoordX(), centres.get(candidat2).getCoordY(), -1, midaRutaCandidat2 - 1);
                        }
                    }
                }
            }
        }
        // Esborra dos centres de distribucio junts al final de la ruta.
        for (int i = 0; i < camions; i++) {
            int mida = estat.get(i).size();
            int tipus = estat.get(i).get(mida - 2).get(2);
            if (mida > 2 && tipus == -1) remove(i, mida - 2);
        }
    }

}

