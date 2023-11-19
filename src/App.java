import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {

        try{
            
            String fileName = args[0];
            ArrayList<City> Map = readCoord(fileName);
        
            Float adjMatrix[][] = new Float[Map.size()][Map.size()];
            for(int i = 0; i < Map.size(); i++){
                for(int j = 0; j < Map.size(); j++){
                    adjMatrix[i][j] = Map.get(i).distance(Map.get(j));
                }
            }

            ArrayList<Float> outHillClimbSwap = new ArrayList<Float>();
            ArrayList<Float> outHillClimbSwapRandom = new ArrayList<Float>();
            ArrayList<Float> outHillClimbReverse = new ArrayList<Float>();
            ArrayList<Float> outHillClimbReverseRandom = new ArrayList<Float>();
            ArrayList<Float> outHillClimbSwapShuffle = new ArrayList<Float>();
            ArrayList<Float> outHillClimbSwapRandomShuffle = new ArrayList<Float>();
            ArrayList<Float> outHillClimbReverseShuffle = new ArrayList<Float>();
            ArrayList<Float> outHillClimbReverseRandomShuffle = new ArrayList<Float>();



            String out = "Variation 01\t\tVariation 02\t\tVariation 03\t\tVariation 04\t\tVariation 05\t\tVariation 06\t\tVariation 07\t\tVariation 08\n";

            for(int i = 0; i < 30; i++){

                // 1. HC SWAP
                outHillClimbSwap.add(hillClimbSwap(Map, adjMatrix));
                out += String.format("%.10f", outHillClimbSwap.get(i)) + "\t\t";
                System.out.println();

                // 2. HC SWAP RAND
                outHillClimbSwapRandom.add(hillClimbSwapRandom(Map, adjMatrix));
                out += String.format("%.10f", outHillClimbSwapRandom.get(i)) + "\t\t";
                System.out.println();

                // 3. HC REV
                outHillClimbReverse.add(hillClimbReverse(Map, adjMatrix));
                out += String.format("%.10f", outHillClimbReverse.get(i)) + "\t\t";
                System.out.println();

                // 4. HC REV RAND
                outHillClimbReverseRandom.add(hillClimbReverseRandom(Map, adjMatrix));
                out += String.format("%.10f", outHillClimbReverseRandom.get(i)) + "\t\t";
                System.out.println();

                // 5. HC RAND STATE SWAP
                outHillClimbSwapShuffle.add(hillClimbSwap(stateShuffle(Map), adjMatrix));
                out += String.format("%.10f", outHillClimbSwapShuffle.get(i)) + "\t\t";
                System.out.println();

                // 6. HC RAND STATE SWAP RAND
                outHillClimbSwapRandomShuffle.add(hillClimbSwapRandom(stateShuffle(Map), adjMatrix));
                out += String.format("%.10f", outHillClimbSwapRandomShuffle.get(i)) + "\t\t";
                System.out.println();

                // 7. HC RAND STATE REV
                outHillClimbReverseShuffle.add(hillClimbReverse(stateShuffle(Map), adjMatrix));
                out += String.format("%.10f", outHillClimbReverseShuffle.get(i)) + "\t\t";
                System.out.println();

                // 8. HC RAND STATE REV RAND
                outHillClimbReverseRandomShuffle.add(hillClimbReverseRandom(stateShuffle(Map), adjMatrix));
                out += String.format("%.10f", outHillClimbReverseRandomShuffle.get(i)) + "\n";
                System.out.println();
            }

            out+= "\nMelhores resultados:\n";
            
            out += String.format("%.10f", Collections.min(outHillClimbSwap)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbSwapRandom)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbReverse)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbReverseRandom)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbSwapShuffle)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbSwapRandomShuffle)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbReverseShuffle)) + "\t\t";
            out += String.format("%.10f", Collections.min(outHillClimbReverseRandomShuffle)) + "\n";



            writeOutPut(out);

        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Nome do arquivo precisa ser inserido");
        }

    }

    public static boolean allFlagsTrue(Boolean[][] flag, int size){

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(!flag[i][j])
                    return false;
            }
        }

        return true;
    }

    public static Float hillClimbSwapRandom(ArrayList<City> State, Float adjMatrix[][]){

        ArrayList<City> newState = State;
        Float thisCost = stateCost(State, adjMatrix);

        printState(State);
        System.out.println(" " + thisCost);

        boolean isBetter = false;

        Boolean[][] repeatedNumbers = new Boolean[State.size()][State.size()];
        for(int i = 0; i < State.size(); i++){
            for(int j = 0; j < State.size(); j++){
                repeatedNumbers[i][j] = new Boolean(false);
            }
        }

        Random rand = new Random();

        while(!allFlagsTrue(repeatedNumbers, State.size())){
            int num1 = rand.nextInt(State.size());
            int num2 = rand.nextInt(State.size());

            if(!repeatedNumbers[num1][num2] || !repeatedNumbers[num2][num1]){

                repeatedNumbers[num1][num2] = repeatedNumbers[num2][num1] = true;
            
                newState = stateSwap(State, num1, num2);
                float cost = stateCost(newState, adjMatrix);

                if(cost < thisCost){
                    isBetter = true;
                    break;
                }
            }
        }

        if(isBetter) return hillClimbSwapRandom(newState, adjMatrix);

        return thisCost;
    }

    public static Float hillClimbSwap(ArrayList<City> State, Float adjMatrix[][]){

        ArrayList<City> newState = State;
        Float thisCost = stateCost(State, adjMatrix);

        printState(State);
        System.out.println(" " + thisCost);

        boolean isBetter = false;

        outerLoop:
        for(int i = 0; i < State.size(); i++){
            for(int j = i; j < State.size(); j++){
                newState = stateSwap(State, i, j);
                float cost = stateCost(newState, adjMatrix);

                if(cost < thisCost){
                    isBetter = true;
                    break outerLoop;
                }
            }
        }

        if(isBetter) return hillClimbSwap(newState, adjMatrix);

        return thisCost;
    }

    public static Float hillClimbReverseRandom(ArrayList<City> State, Float adjMatrix[][]){
        
        ArrayList<City> newState = State;
        Float thisCost = stateCost(State, adjMatrix);

        printState(State);
        System.out.println(" " + thisCost);

        boolean isBetter = false;

        Boolean[][] repeatedNumbers = new Boolean[State.size()][State.size()];

        for(int i = 0; i < State.size(); i++){
            for(int j = 0; j < State.size(); j++){
                repeatedNumbers[i][j] = new Boolean(false);
            }
        }

        Random rand = new Random();

        while(!allFlagsTrue(repeatedNumbers, State.size())){
            int num1 = rand.nextInt(State.size());
            int num2 = rand.nextInt(State.size());

            if(!repeatedNumbers[num1][num2]){

                repeatedNumbers[num1][num2] = true;
            
                newState = stateReverse(State, num1, num2);
                float cost = stateCost(newState, adjMatrix);

                if(cost < thisCost){
                    isBetter = true;
                    break;
                }
            }
        }

        if(isBetter) return hillClimbReverseRandom(newState, adjMatrix);

        return thisCost;

    }

    public static Float hillClimbReverse(ArrayList<City> State, Float adjMatrix[][]){

        ArrayList<City> newState = State;
        Float thisCost = stateCost(State, adjMatrix);

        printState(State);
        System.out.println(" " + thisCost);

        boolean isBetter = false;

        outerLoop:
        for(int i = 0; i < State.size(); i++){
            for(int j = 0; j < State.size(); j++){
                newState = stateReverse(State, i, j);
                float cost = stateCost(newState, adjMatrix);

                if(cost < thisCost){
                    isBetter = true;
                    break outerLoop;
                }
            }
        }

        if(isBetter) return hillClimbReverse(newState, adjMatrix);

        return thisCost;
    }

    public static ArrayList<City> stateShuffle(ArrayList<City> State){
        
        ArrayList<City> newState = new ArrayList<City>(State);
        Collections.shuffle(newState);

        return newState;
    }

    public static ArrayList<City> stateSwap(ArrayList<City> State, int index1, int index2){
        ArrayList<City> newState = new ArrayList<City>(State);
        Collections.swap(newState, index1, index2);

        return newState;
    }

    public static ArrayList<City> stateReverse(ArrayList<City> State, int index1, int index2){

        if(index1 <= index2){

            ArrayList<City> newState = new ArrayList<City>(State);
            ArrayList<City> subList = new ArrayList<City>(newState.subList(index1, index2 + 1));

            Collections.reverse(subList);
            
            for(int i = index1; i <= index2; i++){
                newState.set(i, subList.get(i - index1));
            }

            return newState;
        }

        ArrayList<City> newState = new ArrayList<City>(State);

        ArrayList<City> part1 = new ArrayList<City>(newState.subList(0, index2 + 1));
        ArrayList<City> part2 = new ArrayList<City>(newState.subList(index1, newState.size()));

        part2.addAll(part1);

        ArrayList<City> subList = new ArrayList<City>(part2);

        Collections.reverse(subList);

        for(int i = index1; i < newState.size(); i++){
            newState.set(i, subList.get(i - index1));
        }

        for(int i = 0; i <= index2; i++){
            newState.set(i, subList.get(i + (newState.size() - index1)));
        }

        return newState;
    }

    public static void printState(ArrayList<City> State){

        String out = "";

        for(City c : State){
            out += c.getName();
        }

        System.out.print(out);
    }

    public static Float stateCost(ArrayList<City> State, Float adjMatrix[][]){

        Float cost = new Float(0);

        for(int i = 0; i < State.size(); i++){
            cost += adjMatrix[State.get(i).getId()][State.get((i + 1) % State.size()).getId()];
        }

        return cost;
    }

    public static ArrayList<City> readCoord(String fileName){

        ArrayList<City> Map = new ArrayList<City>();

        ArrayList<Float> coordX = new ArrayList<Float>();
        ArrayList<Float> coordY = new ArrayList<Float>();


        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String data = "";

            if((data = reader.readLine()) != null){
                String out[] = data.split(" ");

                for(String value : out){
                    coordX.add(Float.parseFloat(value));
                }
            }
            
            data = "";

            if((data = reader.readLine()) != null){
                String out[] = data.split(" ");

                for(String value : out){
                    coordY.add(Float.parseFloat(value));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(coordX.size() == coordY.size()){
            for(int i = 0; i < coordX.size(); i++){
                Map.add(new City(coordX.get(i), coordY.get(i)));
            }
        }

        return Map;
    }

    public static void writeOutPut(String out){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            writer.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
