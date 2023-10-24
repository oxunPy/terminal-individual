package com.example.rest.controller.web;

import com.example.rest.constant.ConstEndpoints;
import com.example.rest.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebDataTableController {

    @GetMapping(value = ConstEndpoints.URL_AJAX_DATA_TABLES)
    @Operation(hidden = true)
    public @ResponseBody String localisation(Locale locale){
        String jsonString  = "" +
                "{\n" +
                "  \"processing\": \"" + R.getString(locale, "datatables.processing") + "\",\n" +
                "  \"search\": \"" + R.getString(locale, "datatables.search") + "\",\n" +
                "  \"lengthMenu\": \"" + R.getString(locale, "datatables.lengthMenu") + "\",\n" +
                "  \"info\": \"" + R.getString(locale, "datatables.info") + "\",\n" +
                "  \"infoEmpty\": \"" + R.getString(locale, "datatables.infoEmpty") + "\",\n" +
                "  \"infoFiltered\": \"" + R.getString(locale, "datatables.infoFiltered") + "\",\n" +
                "  \"loadingRecords\": \"" + R.getString(locale, "datatables.loadingRecords") + "\",\n" +
                "  \"zeroRecords\": \"" + R.getString(locale, "datatables.zeroRecords") + "\",\n" +
                "  \"emptyTable\": \"" + R.getString(locale, "datatables.emptyTable") + "\",\n" +
                "  \"paginate\": {\n" +
                "    \"first\": \"" + R.getString(locale, "datatables.first") + "\",\n" +
                "    \"previous\": \"" + R.getString(locale, "datatables.previous") + "\",\n" +
                "    \"next\": \"" + R.getString(locale, "datatables.next") + "\",\n" +
                "    \"last\": \"" + R.getString(locale, "datatables.last") + "\"\n" +
                "  },\n" +
                "  \"aria\": {\n" +
                "    \"sortAscending\": \"" + R.getString(locale, "datatables.sortAscending") + "\",\n" +
                "    \"sortDescending\": \"" + R.getString(locale, "datatables.sortDescending") + "\"\n" +
                "  },\n" +
                "  \"select\": {\n" +
                "    \"rows\": {\n" +
                "      \"_\": \"" + R.getString(locale, "datatables._") + "\",\n" +
                "      \"0\": \"" + R.getString(locale, "datatables.0") + "\",\n" +
                "      \"1\": \"" + R.getString(locale, "datatables.1") + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JSONParser response = new JSONParser(jsonString);
        return jsonString;
    }
}