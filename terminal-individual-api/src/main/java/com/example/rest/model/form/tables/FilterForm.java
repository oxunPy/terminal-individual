package com.example.rest.model.form.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterForm implements Serializable {
    private static final long serialVersionUID = -1183975305038088044L;

    private int draw;
    private int start;
    private int length;

    private List<ColumnsForm> columns;

    private List<OrderForm> order;

    private SearchForm search;

    private Map<String, Object> filter;

    public String getSearchText(){
        if(search != null){
            return search.getValue();
        }
        return null;
    }

    public Map<String, OrderForm> orderMap(){
        if(order == null) return null;
        return order
                    .stream().filter(o -> columns.size() > o.getColumn())
                    .collect(Collectors.toMap(o -> columns.get(o.getColumn()).getData(), o -> o));
    }

}
