import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class KDTree{
    Point2D point = new Point2D.Double();
    List<Point2D> points, xsorted, ysorted;
    Point2D min, max;
    KDTree left;
    KDTree right;

    KDTree(){}
    KDTree(List<Point2D> points){
        this.points = xsorted = ysorted = points;
        xsorted.sort(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if(o1.getX()<=o2.getX()) return -1;
                else return 1;
            }
        });
        min = new Point2D.Double();
        max = new Point2D.Double();
        ((Point2D.Double) min).x = xsorted.get(0).getX();
        ((Point2D.Double) max).x = xsorted.get(xsorted.size()-1).getX();
        ysorted.sort(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if(o1.getY()<=o2.getY()) return -1;
                else return 1;
            }
        });
        ((Point2D.Double) min).y = ysorted.get(0).getY();
        ((Point2D.Double) max).y = ysorted.get(ysorted.size()-1).getY();
        if(points.size()==1) point = points.get(0);
        left = right = null;
    }

    KDTree buildKDTree(List<Point2D> points, int depth){
        List<Point2D> points1=null, points2 = null;
        KDTree v = new KDTree(points);
        //System.out.println(depth+" depth : size "+points.size());

        if(points.size()==1) return new KDTree(points);
        else if(depth%2==0){
            points.sort(new Comparator<Point2D>() {
                @Override
                public int compare(Point2D o1, Point2D o2) {
                    if(o1.getX()<=o2.getX()) return -1;
                    else return 1;
                }
            });
            points1 = points.subList(0, points.size()/2);
            points2 = points.subList(points.size()/2, points.size());
        }
        else if(depth%2==1){
            points.sort(new Comparator<Point2D>() {
                @Override
                public int compare(Point2D o1, Point2D o2) {
                    if(o1.getY()<=o2.getY()) return -1;
                    else return 1;
                }
            });
            points1 = points.subList(0, points.size()/2);
            points2 = points.subList(points.size()/2, points.size());
        }

        v.point = points.get(points.size()/2-1);
        //v.points = points;
        //System.out.print("Left: ");
        v.left = buildKDTree(points1, depth+1);
        //System.out.print("right: ");
        v.right = buildKDTree(points2, depth+1);

        return v;
    }

    char areALLInside(Point2D min, Point2D max, Point2D p1, Point2D p2){
        if(min.getX()>=p1.getX() && max.getX()<=p2.getX() && min.getY()>=p1.getY() && max.getY()<=p2.getY()) return 'C';
        else if(max.getX()<p1.getX() || min.getX()>p2.getX() || max.getY()<p1.getY() || min.getY()>p2.getY()) return 'N';

        return 'I';
    }

    void searchKDTree(KDTree root, Point2D p1, Point2D p2, AtomicInteger num){
        if(root.points.size()==1){
            if(areALLInside(root.min, root.max, p1, p2)=='C') {num.set(num.get()+1);System.out.print("("+root.point.getX()+","+root.point.getY()+") ");}
            return;
        }
        else if(areALLInside(root.min, root.max, p1, p2)=='C'){
            List<Point2D> temp = root.left.points;
            num.set(num.get()+temp.size());
            for(int i=0;i<temp.size();i++) System.out.print("("+temp.get(i).getX()+","+temp.get(i).getY()+") ");
        }
        else if(areALLInside(root.min, root.max, p1, p2)=='I') searchKDTree(root.left, p1, p2, num);

        if(areALLInside(root.min, root.max, p1, p2)=='C'){
            List<Point2D> temp = root.right.points;
            num.set(num.get()+temp.size());
            for(int i=0;i<temp.size();i++) System.out.print("("+temp.get(i).getX()+","+temp.get(i).getY()+") ");
        }
        else if(areALLInside(root.min, root.max, p1, p2)=='I') searchKDTree(root.right, p1, p2, num);

    }

    Point2D neighbour(KDTree root, Point2D p, int depth){
        if(root.points.size()==1) return root.point;
        if(depth%2==0){
            System.out.println(depth);
            if(p.getX()<root.point.getX()) return neighbour(root.left, p, depth+1);
            else return neighbour(root.right, p, depth+1);
        }
        else{
            System.out.println(depth);
            if(p.getY()<root.point.getY()) return neighbour(root.left, p, depth+1);
            else return neighbour(root.right, p, depth+1);
        }
    }

    void printKD(KDTree root, int depth){
        System.out.print(depth+" : "+"("+root.point.getX()+","+root.point.getY()+")  "+root.points.size()+ " ::  ");
        for(int i=0;i< root.points.size();i++)
            System.out.print("("+root.points.get(i).getX()+","+root.points.get(i).getY()+")  ");
        System.out.println();
        if(root.points.size()==1) return;

        System.out.print("Left: ");
        printKD(root.left, depth+1);
        System.out.print("right: ");
        printKD(root.right, depth+1);
    }

}
