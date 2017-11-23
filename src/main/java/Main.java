public class Main {

    public static void main(String[] args) {

        Payload p1 = new Payload();
        Payload p2 = new Payload();
        Payload p3 = new Payload();
        Payload p4 = new Payload();
        Payload p5 = new Payload();
        Payload p6 = new Payload();

        p1.setId(1); p1.setMessage("First");
        p2.setId(2); p2.setMessage("Second");
        p3.setId(3); p3.setMessage("Third");
        p4.setId(4); p4.setMessage("Fourth");
        p5.setId(5); p5.setMessage("Fifth");
        p6.setId(6); p6.setMessage("Sixth");

        Node n6 = new Node (p6, 7000);
        Node n5 = new Node (p5, 7001);
        Node n4 = new Node (p4, 7002);
        Node n3 = new Node (p3, 7003);
        Node n2 = new Node(p2, 7004);
        Node n1 = new Node (p1, 7005);

        n1.addNode(n2); n1.addNode(n3);
        n2.addNode(n1); n2.addNode(n4);
        n3.addNode(n1); n3.addNode(n5);
        n4.addNode(n2); n4.addNode(n5);
        n5.addNode(n3); n5.addNode(n4);

        Thread t1 = new Thread(n1);
        Thread t2 = new Thread(n2);
        Thread t3 = new Thread(n3);
        Thread t4 = new Thread(n4);
        Thread t5 = new Thread(n5);
        Thread t6 = new Thread(n6);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

    }
}
