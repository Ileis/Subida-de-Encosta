public class City {

    private static int id = 0;
    private int name = 0;
    private float x;
    private float y;

    public City(float x, float y){
        this.x = x;
        this.y = y;
        this.name = City.id;
        City.id++;
    }

    public char getName(){
        char letter = 'A';
        return (char) (letter + name);
    }

    public int getId(){
        return name;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float distance(City c){
        return Double.valueOf(Math.sqrt(Math.pow(this.getX() - c.getX(), 2) + Math.pow(this.getY() - c.getY(), 2))).floatValue();
    }

    @Override
    public String toString() {
        String out = "";

        out +="City: " + getName() + " Coord: " +  x + " " + y;

        return out;
    }
}
