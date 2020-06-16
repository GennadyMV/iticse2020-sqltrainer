package rage.sqltrainer.databasecomparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableVo {

    String name;
    List<Map<String, Object>> data;

    public TableVo(String name, List<Map<String, Object>> data) {
        this.name = name;
        this.data = data;
    }
    
    public List<String> getHeaders() {
        if(getData().isEmpty()) {
            return new ArrayList<>();
        }
        
        ArrayList<String> headers = new ArrayList<>();
        Map<String, Object> row = getData().get(0);
        for (String header : row.keySet()) {
            headers.add(header);
        }
        return headers;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

}
