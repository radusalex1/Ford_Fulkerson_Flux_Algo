import java.awt.*;

public class Nod {
    private int coordX;
    private int coordY;
    private int number;
    private Boolean selected=false;

    public Nod(int coordX, int coordY, int number)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
    }

    public int getMiddleX(){
        return (this.getCoordX()*2+30)/2;
    }
    public int getMiddleY(){
        return (this.getCoordY()*2+30)/2;
    }
    public int getCoordX() {
        return coordX;
    }
    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }
    public int getCoordY() {
        return coordY;
    }
    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public void drawNode(Graphics g, int node_diam)
    {

        g.setColor(new Color(0,255,0));


        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g.fillOval(coordX, coordY, node_diam, node_diam);

        g.setColor(Color.blue);//content color
        g.drawOval(coordX, coordY, node_diam, node_diam);

        if(number < 10)
            g.drawString(((Integer)number).toString(), coordX+13, coordY+20);
        else
            g.drawString(((Integer)number).toString(), coordX+8, coordY+20);
    }
}
