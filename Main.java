package P1;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;
import java.util.Properties;
import java.util.Iterator;

public class Main{
    public static void main(String[] args){
        // Creacio d'un estat
        P1Estat board = new P1Estat();
        // Generem les gasolineras i els centres de distribucio
        P1Estat.generar_problema();
        // Generem un solucio inicial
        board.generar_solucio3();
        // Cridar a HillClimbing
        debug2(board);
        //hillClimbing(board);
        // Cridar a SimulatedAnnealing
        simulatedAnnealing(board,10000,100,5,0.0005);
    }

    private static void hillClimbing(P1Estat board){
        try {
            long start_time = System.nanoTime();
            Problem problem = new Problem(board, new P1Successor(), new P1Goal(), new P1Heuristica());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            long end_time = System.nanoTime();
            double diferencia = (end_time-start_time) / 1e6;

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            P1Estat sol = (P1Estat) search.getGoalState();
            sol.escriu();
            System.out.println("\n\nTemps: " + (diferencia/1e3) + " segons.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void simulatedAnnealing(P1Estat board, int steps, int stiter, int k, double lamb) {
        try {
            long start_time = System.nanoTime();
            Problem problem = new Problem(board, new P1SuccessorSA(), new P1Goal(), new P1Heuristica());
            Search search = new SimulatedAnnealingSearch(steps,stiter,k,lamb);
            SearchAgent agent = new SearchAgent(problem, search);
            long end_time = System.nanoTime();
            double diferencia = (end_time - start_time) / 1e6;

            System.out.println();
            //printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            P1Estat sol = (P1Estat) search.getGoalState();
            sol.escriu();
            System.out.println("\n\nTemps: " + (diferencia / 1e3) + " segons.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    private static void debug2(P1Estat board)
    {
        for (int i = 0; i < board.size(); i++)
        {
            System.out.println(board.getDistancia(i));
        }
    }

}