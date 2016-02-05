package soen390.mapx.webapp;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.model.POI;

import static org.junit.Assert.*;

public class POIJSInterfaceTest {

    private POIJSInterface poi_int;
    Context context = new MockContext();
    List<POI> list;

    @Before
    public void initialize() {
        list = new ArrayList<POI>();
        poi_int = new POIJSInterface(context);
        POI poi = new POI();
        poi.setId((long) 1);
        poi.setTitle("POI_1");
        poi.setType("e");
        poi.setFloorId((long) 1);
        poi.setxCoord(75);
        poi.setyCoord(100);
        list.add(poi);
    }

    @Test
    public void testgetPOIsJSON(){
        assertEquals("{\"poi\":[{\"x_coord\":75,\"y_coord\":100,\"_id\":1,\"title\":\"POI_1\",\"type\":\"e\",\"floor\":1}]}", poi_int.buildPOIJSON(list));

    }
}
