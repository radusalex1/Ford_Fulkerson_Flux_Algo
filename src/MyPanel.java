import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class MyPanel extends JPanel {

    private int nodeNr = 1;
    private final int node_diam = 30;

    private final Vector<Nod> listaNoduri;
    private final Vector<Arc> listaArce;

    Point pointStart = null;
    Point pointEnd = null;

    boolean isDragging = false;
    boolean moving=false;

    int maximumFlow=0;

    int graph[][] = new int[][] {
            { 0, 16, 13, 0, 0, 0 },
            { 0, 0, 10, 12, 0, 0 },
            { 0, 4, 0, 0, 14, 0 },
            { 0, 0, 9, 0, 0, 20 },
            { 0, 0, 0, 7, 0, 4 },
            { 0, 0, 0, 0, 0, 0 }
    };

    public MyPanel()
    {
        listaNoduri = new Vector<Nod>();
        listaArce = new Vector<Arc>();

        setBorder(BorderFactory.createLineBorder(Color.black));

        JButton b = new JButton("Get Max Flow");

        b.setBounds(50,50,100,300);

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("flow max");

                displayCostMatrix(CreateCapacityMatrix());

                Scanner input = new Scanner(System.in);
                System.out.print("nodul start");
                int start = input.nextInt();
                System.out.print("nodul final" );
                int finish = input.nextInt();

                FordFulkerson ff = new FordFulkerson(6);

                maximumFlow=ff.fordFulkerson(graph,start-1,finish-1);

                repaint();

                //nr noduri = listanoduri.size();
                //apelez cu(start-1,finish-1);

            }
        });

        this.add(b);

        addMouseListener(new MouseAdapter()
        {
            //evenimentul care se produce la apasarea mousse-ului
            public void mousePressed(MouseEvent e)
            {
                pointStart = e.getPoint();
                if(e.getClickCount()==2 && !e.isConsumed() && moving==false)
                {
                    for(Nod n:listaNoduri)
                    {
                        if(ReturnDistancePoints(pointStart.x,pointStart.y,n.getMiddleX(),n.getMiddleY())<=15)
                        {
                            n.setSelected(true);
                            moving=true;
                        }
                    }
                }
                if(e.getButton()==MouseEvent.BUTTON3 && moving ==true)
                {
                    moving=false;
                    for(Nod n:listaNoduri)
                    {
                        n.setSelected(false);
                    }
                }
            }
            //evenimentul care se produce la eliberarea mousse-ului
            public void mouseReleased(MouseEvent e) {

                if (!isDragging)
                {
                    if(!CheckForNotOverleaping(e) && moving==false && e.getButton()==MouseEvent.BUTTON1)
                    {
                        addNode(e.getX(), e.getY());
                    }
                }
                else
                {
                    if(ICanDrawArc() && moving==false)
                    {
                        Arc arc = new Arc(pointStart, pointEnd);
                        if(e.getButton()==MouseEvent.BUTTON1)
                        {

                            SearchForStartFinishVertex(arc,pointStart,pointEnd);

                            Scanner input = new Scanner(System.in);

                            System.out.print("capacitate:" + arc.getStartNode().getNumber()+"-"+arc.getEndNode().getNumber());

                            int capacitate = input.nextInt();
/*
                            System.out.print("flux:" + arc.getStartNode().getNumber()+"-"+arc.getEndNode().getNumber());

                            int flux = input.nextInt();*/

                            arc.setCapacitate(capacitate);
                            // arc.setFlux(flux);

                            listaArce.add(arc);

                        }
                    }
                }

                pointStart = null;
                isDragging = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            //evenimentul care se produce la drag&drop pe mousse
            public void mouseDragged(MouseEvent e) {
                pointEnd = e.getPoint();
                isDragging = true;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionHandler());
    }

    private class MouseMotionHandler extends MouseMotionAdapter {

        Point delta = new Point();
        public void mouseDragged(MouseEvent e)
        {
            Nod node=null;
            if(moving)
            {
                for(Nod n:listaNoduri)
                {
                    if(n.getSelected())
                    {
                        node=n;
                    }
                }
                node.setCoordX(e.getX());
                node.setCoordY(e.getY());
            }
            else
            {
                pointEnd=e.getPoint();
            }
            repaint();
        }
    }


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(maximumFlow!=0)
        {
            g.drawString("Maximum Flow is:"+maximumFlow, 10,50);
        }
        if(moving)
        {
            for(Nod n:listaNoduri)
            {
                if(n.getSelected())
                {
                    g.drawString("Ai selectat nodul" + n.getNumber(),10,34);
                }
            }
        }
        else
        {
            g.drawString("Niciun nod nu e selectat",10,34);
        }

        for(Arc a:listaArce)
        {
            Nod start = a.getStartNode();
            Nod end = a.getEndNode();
            a.drawArrowLine(g, start.getMiddleX(), start.getMiddleY(), end.getMiddleX(), end.getMiddleY(), 40, 5);
        }

        if(pointStart!=null && moving!=true)
        {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
        }
        for(int i=0; i<listaNoduri.size(); i++) {
            listaNoduri.elementAt(i).drawNode(g, node_diam);
        }
    }


    private double ReturnDistancePoints(double x1,double y1, double x2,double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
    private Boolean CheckForNotOverleaping(MouseEvent e) {

        boolean IsOverlap=false;
        double minDist=10000;

        for (Nod n : listaNoduri)
        {
            double Xmiddle = (2*n.getCoordX()+30) /2 ;
            double Ymiddle = (2*n.getCoordY()+30) /2 ;

            double dis=Math. sqrt((e.getX()-Xmiddle)*(e.getX()-Xmiddle) + (e.getY()-Ymiddle)*(e.getY()-Ymiddle));

            if(minDist>dis)
            {
                minDist=dis;
            }

            if(n.getCoordX() == e.getX() && n.getCoordY() == e.getY())
            {
                IsOverlap =true;
            }
        }
        if(minDist<55)
        {
            IsOverlap=true;
        }
        return IsOverlap;

    }
    private void addNode(int x, int y) {
        Nod node = new Nod(x, y, nodeNr);
        listaNoduri.add(node);
        nodeNr++;
        repaint();
    }
    private boolean ICanDrawArc() {

        double dStartMin=10000;
        double dFinisMin=10000;

        for(Nod n: listaNoduri)
        {
            double dStart = ReturnDistancePoints(pointStart.x,pointStart.y,n.getMiddleX(),n.getMiddleY());
            double dFinish = ReturnDistancePoints(pointEnd.x,pointEnd.y,n.getMiddleX(),n.getMiddleY());

            if(dStart<dStartMin)
            {
                dStartMin=dStart;
            }
            if(dFinish<dFinisMin)
            {
                dFinisMin=dFinish;
            }
        }
        return dFinisMin <= 15 && dStartMin <= 15;
    }
    private void SearchForStartFinishVertex(Arc arc, Point pointStart, Point pointEnd) {
        for(Nod n:listaNoduri)
        {
            double d1 = ReturnDistancePoints(n.getMiddleX(),n.getMiddleY(),arc.getPointStart().x,
                    arc.getPointStart().y);
            if(d1<=node_diam/2)
            {
                arc.setStartNode(n);
                Point p = new Point(n.getMiddleX(),n.getMiddleY());
                arc.setStartPoint(p);
            }

            double d2 = ReturnDistancePoints(n.getMiddleX(),n.getMiddleY(),arc.getPointEnd().x,
                    arc.getPointEnd().y);

            if(d2<=node_diam/2)
            {
                arc.setEndNode(n);
                Point p = new Point(n.getMiddleX(),n.getMiddleY());
                arc.setEndPoint(p);
            }
        }
    }
    public int[][] CreateCapacityMatrix()
    {
        int[][] costMatrix = new int[listaNoduri.size()+1][listaNoduri.size()+1];
        for (int i = 1; i < listaNoduri.size() + 1; i++) {
            for (int j = 1; j < listaNoduri.size() + 1; j++) {
                costMatrix[i][j]=0;
            }
        }
        for(Arc a:listaArce)
        {
            Nod n;
            Nod m;
            n=a.getStartNode();
            m=a.getEndNode();
            costMatrix[n.getNumber()-1][m.getNumber()-1]=a.getCapacitate();
        }
        return costMatrix;

    }
    public void displayCostMatrix(int [][] costMatrix)
    {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("Costmatrix.txt"));
            output.write("Indexarea este de la 0!");
            output.newLine();
            int nrNod = listaNoduri.size();
            output.write(String.valueOf(nrNod));
            output.newLine();
            for (int i = 0; i < listaNoduri.size(); i++) {
                for (int j = 0; j < listaNoduri.size(); j++) {
                    output.write(costMatrix[i][j] + " ");
                }
                output.newLine();
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
