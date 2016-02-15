package soen390.mapx.database;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.model.Node;
import soen390.mapx.model.Storyline;

/**
 * Class to generate dummy data
 */
public class DummyData {

    public static void populateDb() {

       populateStorylines();
        populatePOIs();


    }

    private static void populateStorylines(){

        List<Storyline> list = new ArrayList<>();

        Storyline st = new Storyline();
        st.setTitle("Title1");
        st.setDescription("Description En description description description description description description description description description description");
        st.setImagePath("TODO");
        list.add(st);

        Storyline st2 = new Storyline();
        st2.setTitle("Title2");
        st2.setDescription("Description En description description description description description description description description description description");
        st2.setImagePath("TODO");
        list.add(st2);

        Storyline st3 = new Storyline();
        st3.setTitle("Title3");
        st3.setDescription("Description En description description description description description description description description description description");
        st3.setImagePath("TODO");
        list.add(st3);

        Storyline st4 = new Storyline();
        st4.setTitle("Title4");
        st4.setDescription("Description En description description description description description description description description description description");
        st4.setImagePath("TODO");
        list.add(st4);

        Storyline st5 = new Storyline();
        st5.setTitle("Title5");
        st5.setDescription("Description En description description description description description description description description description description");
        st5.setImagePath("TODO");
        list.add(st5);

        Storyline st6 = new Storyline();
        st6.setTitle("Title6");
        st6.setDescription("Description En description description description description description description description description description description");
        st6.setImagePath("TODO");
        list.add(st6);

        Storyline.saveInTx(list);

    }

    private static void populatePOIs() {
        Node node = new Node("POI1",1,1,Node.POI_TYPE, null, null, null, null);
        node.save();
    }
}
