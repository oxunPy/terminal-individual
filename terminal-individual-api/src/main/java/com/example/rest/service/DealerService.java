package com.example.rest.service;


import com.example.rest.dto.DealerDto;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.dealer.DealerForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;

import java.util.List;

public interface DealerService {

    List<DealerDto> getDealersWithName(String name);

    DataTablesForm<DealerForm> getDealersDataTable(FilterForm filterForm);

    List<Select2Form> findAllDealer();
}
