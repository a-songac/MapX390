package soen390.mapx.database;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.model.Storyline;

/**
 * Created by Arnaud on 2016-02-09.
 */
public class DummyData {

    public static List<Storyline> getStorylinesList() {

        List<Storyline> list = new ArrayList<>();

        Storyline st = new Storyline();
        st.setTitle("Title1");
        st.setTitleFr("Titre1");
        st.setDescription("Description En description description description description description description description description description description");
        st.setDescriptionFr("Description En description description description description description description description description description description");
        st.setImagePath("TODO");
        list.add(st);

        Storyline st2 = new Storyline();
        st2.setTitle("Title1");
        st2.setTitleFr("Titre1");
        st2.setDescription("Description En description description description description description description description description description description");
        st2.setDescriptionFr("Description En description description description description description description description description description description");
        st2.setImagePath("TODO");
        list.add(st2);

        return list;
    }
}
