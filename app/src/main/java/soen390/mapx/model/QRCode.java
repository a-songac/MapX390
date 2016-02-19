package soen390.mapx.model;

import com.orm.SugarRecord;

/**
 * QRCode Model
 * on stand by, will probably be scoped out
 */
public class QRCode extends SugarRecord {

    private String link;

    public QRCode(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
