package Entity;

import java.util.ArrayList;

public class TestRun {
    public static void main(String[] args) {
        VIP vip = new VIP("a", "1",1);
        vip.addEvent(1);
        ArrayList<Integer> lst = vip.getEventList();
        System.out.println(lst.get(1));
    }
}