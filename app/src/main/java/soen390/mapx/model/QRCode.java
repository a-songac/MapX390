package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * QRCode Model
 */
public class QRCode extends SugarRecord {

    private String link;

    public QRCode(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

}
