package soen390.mapx.webapp;

import android.content.Context;
import android.test.mock.MockContext;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.List;

import soen390.mapx.model.Floor;
import soen390.mapx.model.Node;

public class NodeJSInterfaceTest {

    private NodeJSInterface poi_int;
    Context context = new MockContext();
    List<Node> list_node;
    List<Floor> list_floor;

    @Before
    public void initialize() {
        list_node = new ArrayList<Node>();
        poi_int = new NodeJSInterface(context);
        Node node = new Node();
        node.setId((long) 1);
//        node.setTitle("POI_1");
        node.setType("e");
        node.setFloorId((long) 1);
        node.setxCoord(75);
        node.setyCoord(100);
        list_node.add(node);

        list_floor = new ArrayList<Floor>();
        Floor floor = new Floor();
        floor.setId((long) 1);
        floor.setFloorId("1");
        floor.setImageFilePath("tiles/floor_1.jpg");
        floor.setImageHeight(700);
        floor.setImageWidth(1800);
        list_floor.add(floor);
    }

    /**
     * TODO No longer testable as junit test, requires to be tested on the device
     */
    @Ignore
    public void testgetPOIsJSON() {
        try {
            JSONAssert.assertEquals("{\"poi\":[{\"x_coord\":75,\"y_coord\":100,\"_id\":1,\"type\":\"e\",\"floor\":1}]}", poi_int.buildPOIJSON(list_node), true);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testgetFloorJSON(){
        try{
            JSONAssert.assertEquals("{\"floor\":[{\"floor_num\":\"1\",\"floor_path\":\"tiles/floor_1.jpg\",\"floor_width\":1800,\"floor_height\":700}]}", poi_int.buildFloorJSON(list_floor), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
