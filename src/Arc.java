import java.awt.*;

public class Arc {

    private Point startPoint;
    private Point endPoint;
    private Nod endNode;
    private Nod startNode;
    private int capacitate;
    private int flux=0;

    public Arc(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public int getCapacitate() {
        return capacitate;
    }
    public int getFlux() {
        return flux;
    }
    public void setCapacitate(int capacitate) {
        this.capacitate = capacitate;
    }
    public void setFlux(int flux) {
        this.flux = flux;
    }
    public Point getPointStart()
    {
        return startPoint;
    }
    public Point getPointEnd()
    {
        return endPoint;
    }
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }
    public void setEndNode(Nod endNode) {
        this.endNode = endNode;
    }
    public void setStartNode(Nod startNode) {
        this.startNode = startNode;
    }
    public Point getStartPoint() {
        return startPoint;
    }
    public Point getEndPoint() {
        return endPoint;
    }
    public Nod getEndNode() {
        return endNode;
    }
    public Nod getStartNode() {
        return startNode;
    }

    public void drawArc(Graphics g,Nod startNode,Nod endNode)
    {
        if(startNode!=null)
        {
            g.setColor(java.awt.Color.RED);
            g.drawLine(startNode.getMiddleX(), startNode.getMiddleY(), endNode.getMiddleX(), endNode.getMiddleY());
        }
    }

    public void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
        g.setColor(java.awt.Color.BLUE);
        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);

        g.setColor(java.awt.Color.black);
        g.drawString("C:"+((Integer)capacitate).toString()+" && F:"+((Integer)flux).toString(),(endNode.getCoordX()+startNode.getCoordX())/2,(endNode.getCoordY()+startNode.getCoordY())/2);
    }

}
