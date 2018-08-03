import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

class Main extends JComponent{
    static ArrayList<Point2D> points = new ArrayList<>();
    static ArrayList<ArrayList<Point2D>> queries = new ArrayList<ArrayList<Point2D>>();
    static int query=0;
    static KDTree root;

    @Override
    public void paint(Graphics g) {
        // Draw a simple line using the Graphics2D draw() method.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(Color.black);
        g2.draw(new Line2D.Double(0, 300, 600, 300));
        g2.draw(new Line2D.Double(300, 0, 300, 600));
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(Color.RED);
        g2.translate(300,300);

        for(int i=0;i<points.size();i++) {
            g2.draw(new Line2D.Double(points.get(i).getX()*15,-points.get(i).getY()*15,points.get(i).getX()*15,-points.get(i).getY()*15));
            //System.out.println(points.get(i).getX()+" "+points.get(i).getY());
        }

        g2.setStroke(new BasicStroke(2f));
        if(queries.get(query).size()==2) {
            g2.draw(new Line2D.Double(queries.get(query).get(0).getX() * 15, -queries.get(query).get(0).getY() * 15, queries.get(query).get(1).getX() * 15, -queries.get(query).get(0).getY() * 15));
            g2.draw(new Line2D.Double(queries.get(query).get(1).getX() * 15, -queries.get(query).get(0).getY() * 15, queries.get(query).get(1).getX() * 15, -queries.get(query).get(1).getY() * 15));
            g2.draw(new Line2D.Double(queries.get(query).get(1).getX() * 15, -queries.get(query).get(1).getY() * 15, queries.get(query).get(0).getX() * 15, -queries.get(query).get(1).getY() * 15));
            g2.draw(new Line2D.Double(queries.get(query).get(0).getX() * 15, -queries.get(query).get(1).getY() * 15, queries.get(query).get(0).getX() * 15, -queries.get(query).get(0).getY() * 15));
        }
        else {
            Point2D p = queries.get(query).get(0);
            Point2D near = root.neighbour(root, p, 0);
            System.out.println("("+near.getX()+","+near.getY()+")");
            double dist = Math.sqrt((p.getX()-near.getX())*(p.getX()-near.getX())+(p.getY()-near.getY())*(p.getY()-near.getY()));
            Ellipse2D.Double shape = new Ellipse2D.Double(p.getX()*15, -p.getY()*15, dist*15, dist*15);
            g2.draw(shape);
            g2.setStroke(new BasicStroke(4f));
            g2.setColor(Color.GREEN);
            g2.draw(new Line2D.Double(p.getX()*15,-p.getY()*15, p.getX()*15,-p.getY()*15));
        }

    }

    public static void main(String[] args) throws IOException {
        int n=0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("1305072_input1.txt"));
            n = Integer.parseInt(br.readLine());
        } catch (Exception e) {}

        for(int i=1;i<=n;i++){
            StringTokenizer strt = new StringTokenizer(br.readLine(), " ");
            Point2D p = new Point2D.Double();
            ((Point2D.Double) p).x = Double.parseDouble(strt.nextToken());
            ((Point2D.Double) p).y = Double.parseDouble(strt.nextToken());
            points.add(p);
        }

        root = new KDTree(points);
        root = root.buildKDTree(points, 0);
        root.printKD(root, 0);

        String str;
        while ((str=br.readLine())!=null){
            StringTokenizer strt = new StringTokenizer(str, " ");
            String dir = strt.nextToken();
            if(dir.matches("R")){
                ArrayList<Point2D> query = new ArrayList<>();
                Point2D p1 = new Point2D.Double();
                ((Point2D.Double) p1).x = Double.parseDouble(strt.nextToken());
                ((Point2D.Double) p1).y = Double.parseDouble(strt.nextToken());
                query.add(p1);
                Point2D p2 = new Point2D.Double();
                ((Point2D.Double) p2).x = Double.parseDouble(strt.nextToken());
                ((Point2D.Double) p2).y = Double.parseDouble(strt.nextToken());
                if(p2.getX()<p1.getX()) query.add(0, p2);
                else query.add(p2);
                queries.add(query);

                AtomicInteger num = new AtomicInteger(0);
                root.searchKDTree(root, p1, p2, num);
                System.out.println();
                System.out.println(num);
            }
            else if(dir.matches("N")){
                ArrayList<Point2D> query = new ArrayList<>();
                Point2D p = new Point2D.Double();
                ((Point2D.Double) p).x = Double.parseDouble(strt.nextToken());
                ((Point2D.Double) p).y = Double.parseDouble(strt.nextToken());

                Point2D near = root.neighbour(root, p, 0);
                double dist = Math.sqrt((p.getX()-near.getX())*(p.getX()-near.getX())+(p.getY()-near.getY())*(p.getY()-near.getY()));
                System.out.println(dist+" ("+near.getX()+","+near.getY()+")");
                query.add(p);
                queries.add(query);
            }
        }

        //System.out.println(queries.get(0).get(0).getX()+" "+queries.get(0).get(0).getY()+" "+queries.get(0).get(1).getX()+" "+queries.get(0).get(0).getY());

        JFrame frame= new JFrame("offline 4");
        frame.getContentPane().add(new Main());
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar()=='1') query = (query+1)%queries.size();
                System.out.println(query);
                frame.getContentPane().repaint();

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        frame.addKeyListener(keyListener);
    }
}
