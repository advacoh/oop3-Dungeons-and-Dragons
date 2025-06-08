package game;

public class Position {
    public final int x, y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public double range(int x, int y){
        int dx = this.x - x;
        int dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy);

    }
    @Override
    public String toString(){
        return "(" + x + ", " + y + ")";
    }

    public Position shiftBy(char dir) {
        switch (dir) {
            case 'w': return new Position(x, y - 1);
            case 's': return new Position(x, y + 1);
            case 'a': return new Position(x - 1, y);
            case 'd': return new Position(x + 1, y);
            default: return this;
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}
