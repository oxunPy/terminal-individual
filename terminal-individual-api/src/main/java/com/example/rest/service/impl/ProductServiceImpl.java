package com.example.rest.service.impl;

import com.example.rest.constant.CurrencyCode;
import com.example.rest.dto.ExchangeRateDto;
import com.example.rest.dto.ProductDto;
import com.example.rest.dto.dtoProjection.ProductDtoProjection;
import com.example.rest.dto.dtoProjection.ProductGroupDtoProjection;
import com.example.rest.dto.dtoProjection.ProductPriceDtoProjection;
import com.example.rest.dto.dtoProjection.UnitDtoProjection;
import com.example.rest.model.form.Select2Form;
import com.example.rest.model.form.file.FileForm;
import com.example.rest.model.form.product.ProductForm;
import com.example.rest.model.form.product.ProductPriceForm;
import com.example.rest.model.form.product.ProductSaveOrEditForm;
import com.example.rest.model.form.productGroup.ProductGroupForm;
import com.example.rest.model.form.tables.DataTablesForm;
import com.example.rest.model.form.tables.FilterForm;
import com.example.rest.model.form.tables.OrderForm;
import com.example.rest.model.form.unit.UnitForm;
import com.example.rest.repository.ProductRepository;
import com.example.rest.service.CurrencyService;
import com.example.rest.service.ProductService;
import com.example.rest.utils.CurrencyConverter;
import com.example.rest.utils.DateUtil;
import com.example.rest.utils.PoiUtils;
import com.example.rest.utils.R;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CurrencyService currencyService;

    @Value("${download.template.csv}")
    private String CSV_FOLDER;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Override
    public List<ProductDto> findAllProductDto() {
        return null;
    }

    @Override
    public ProductDto saveProductDto(ProductDto dto) {
        return null;
    }

    @Override
    public ProductDto updateProductDto(ProductDto dto) {
        return null;
    }

    @Override
    public ProductDto findProductDtoById(Long id) {
        return null;
    }

    @Override
    public List<ProductDto> findProductListByBarcode(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            throw new IllegalStateException("barcode is invalid!!!");
        }
        ExchangeRateDto lastExchangeRate = currencyService.getLastExchangeRate();

        List<ProductDtoProjection> pBarcodeList = productRepository.getProductListByBarcode(barcode);

        List<ProductDto> productList = new ArrayList<>();
        for (ProductDtoProjection productDtoProjection : pBarcodeList) {
            ProductDto dto = new ProductDto();
            dto.setProductName(productDtoProjection.getProduct_name());
            dto.setId(productDtoProjection.getId());
            dto.setGroupName(productDtoProjection.getGroup_name());
            dto.setCurrency(productDtoProjection.getCurrency());
            dto.setOriginalRate(productDtoProjection.getRate());
            dto.setRate(getProductRate(productDtoProjection, lastExchangeRate));
            productList.add(dto);
        }
        return productList;
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesForm<ProductForm> getProductsDataTable(FilterForm filter){
        ExchangeRateDto lastExchangeRateDto = currencyService.getLastExchangeRate();

        Sort sort = getSort(filter.orderMap());

        if(sort.isEmpty())
            sort = Sort.by(Sort.Direction.ASC, "id");

        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);
        Map<String, Object> filterMap = filter.getFilter();

        String name = filter.getSearchText();

        Page<ProductDtoProjection> pageProducts = productRepository.pageProductsByName(StringUtils.getIfBlank(name, () -> ""), pageRequest);

        List<ProductForm> productList = pageProducts.stream().
                map(p -> new ProductForm(p.getId(), p.getProduct_name(), getProductRate(p, lastExchangeRateDto), p.getRate(), p.getCurrency(), p.getGroup_name(), p.getBarcode())).collect(Collectors.toList());

        DataTablesForm<ProductForm> dataTablesForm = new DataTablesForm<>();
        dataTablesForm.setData(productList);
        dataTablesForm.setDraw(filter.getDraw());
        dataTablesForm.setRecordsTotal((int) pageProducts.getTotalElements());
        dataTablesForm.setRecordsFiltered((int) pageProducts.getTotalElements());
        return dataTablesForm;
    }

    @Override
    @Transactional(readOnly = true)
    public DataTablesForm<ProductPriceForm> getProductPriceDataTable(Long productId, FilterForm filter){
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE, sort);

        int type = 0;
        Map<String, Object> filterMap = filter.getFilter();
        if(filterMap != null) {
            if (filterMap.containsKey("buy") && MapUtils.getBoolean(filterMap, "buy")) type = 1;
            if (filterMap.containsKey("sell") && MapUtils.getBoolean(filterMap, "sell")) type = 0;
        }
        Page<ProductPriceDtoProjection> pageProductPrice = productRepository.pageProductPrices(productId, type, pageRequest);

        List<ProductPriceForm> listPPForm = pageProductPrice.getContent().stream().map(pp -> new ProductPriceForm(pp.getId(), DateUtil.format(DateUtil.parse(pp.getDate(), DateUtil.PATTERN1), DateUtil.PATTERN2), NumberFormat.getCurrencyInstance().format(pp.getRate()).substring(1) + " " + pp.getCurrency(), null,  pp.getCurrency())).collect(Collectors.toList());

        DataTablesForm<ProductPriceForm> datatable = new DataTablesForm<>();
        datatable.setDraw(filter.getDraw());
        datatable.setRecordsFiltered((int) pageProductPrice.getTotalElements());
        datatable.setRecordsTotal((int) pageProductPrice.getTotalElements());
        datatable.setData(listPPForm);
        return datatable;
    }

    @Override
    public DataTablesForm<ProductGroupForm> getProductGroupDataTable(FilterForm filter) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page< ProductGroupDtoProjection> pageProductGroup = productRepository.pageProductGroupByName(filter.getSearchText(), pageRequest);
        List<ProductGroupForm> groupForms = pageProductGroup.stream().map(pg -> new ProductGroupForm(pg.getId(), pg.getGroupName(), pg.getInfo(), pg.getParentId(), pg.getParentName())).collect(Collectors.toList());

        DataTablesForm<ProductGroupForm> datatable = new DataTablesForm<>();
        datatable.setDraw(filter.getDraw());
        datatable.setRecordsFiltered((int) pageProductGroup.getTotalElements());
        datatable.setRecordsTotal((int) pageProductGroup.getTotalElements());
        datatable.setData(groupForms);
        return datatable;
    }

    @Override
    public DataTablesForm<UnitForm> getUnitDataTable(FilterForm filter) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(filter.getStart() / filter.getLength(), filter.getLength(), sort);

        Page<UnitDtoProjection> pageUnit = productRepository.pageUnit(pageRequest);
        List<UnitForm> unitForms = pageUnit.stream().map(u -> new UnitForm(u.getId(), u.getName(), u.getSymbol())).collect(Collectors.toList());

        DataTablesForm<UnitForm> datatable = new DataTablesForm<>();
        datatable.setRecordsTotal((int) pageUnit.getTotalElements());
        datatable.setRecordsFiltered((int) pageUnit.getTotalElements());
        datatable.setDraw(filter.getDraw());
        datatable.setData(unitForms);
        return datatable;
    }



    @Override
    public List<ProductDto> findProductByNameLike(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalStateException("barcode is invalid!!!");
        }

        ExchangeRateDto lastExchangeRate = currencyService.getLastExchangeRate();
        String param1, param2, param3, param4;
        String[] params = productName.split(" ");
        param1 = params[0];
        if (params.length > 1 && params[1] != null && !params[1].isEmpty()) {
            param2 = params[1];
        } else {
            param2 = params[0];
        }

        if (params.length > 2 && params[2] != null && !params[2].isEmpty()) {
            param3 = params[2];
        } else {
            param3 = params[0];
        }

        if (params.length > 3 && params[3] != null && !params[3].isEmpty()) {
            param4 = params[3];
        } else {
            param4 = params[0];
        }


        List<ProductDtoProjection> searchedProductList = productRepository.getProductListByNameLike(param1, param2, param3, param4);
        List<ProductDto> result = new ArrayList<>();

        for (ProductDtoProjection searchedProduct : searchedProductList) {
            ProductDto dto = new ProductDto();
            dto.setProductName(searchedProduct.getProduct_name());
            dto.setId(searchedProduct.getId());
            dto.setGroupName(searchedProduct.getGroup_name());
            dto.setCurrency(searchedProduct.getCurrency());
            dto.setOriginalRate(searchedProduct.getRate());
            dto.setRate(getProductRate(searchedProduct, lastExchangeRate));
            result.add(dto);
        }
        return result;
    }

    @Override
    public BigDecimal getProductOstatokById(Integer productId, Integer warehouseId) {
        return productRepository.getProductOstatokByProductId(productId, warehouseId, new Date());
    }

    public Sort getSort(Map<String, OrderForm> orderMap){
        List<Sort.Order> orders = new ArrayList<>();
        if(orderMap != null){
            orderMap.forEach((column, orderForm) -> {
                String property = null;

                switch(column){
                    case "name":
                        property = "name";
                        break;
                    case "barcode":
                        property = "barcode";
                }
                Sort.Direction direction = Sort.Direction.fromString(orderForm.getDir());
                orders.add(direction.isAscending() ? Sort.Order.asc(Objects.requireNonNull(property)) : Sort.Order.desc(Objects.requireNonNull(property)));
            });
        }
        return Sort.by(orders);
    }

    private BigDecimal getProductRate(ProductDtoProjection searchedProduct, ExchangeRateDto lastExchangeRate){
        if ((searchedProduct.getCurrency() == null || searchedProduct.getCurrency().equals(CurrencyCode.USD.name())) && lastExchangeRate.getMainCurrency() != null
                                                                                                                     && lastExchangeRate.getMainCurrency().equals(CurrencyCode.USD.name())) {
            return CurrencyConverter.getRateUZS(lastExchangeRate, searchedProduct.getRate() != null ? searchedProduct.getRate() : BigDecimal.valueOf(0));
        }
        return searchedProduct.getRate();
    }

    @Override
    public Boolean uploadExcelProducts(FileForm fileForm){

        Connection conn = null;
        try{
            //connection to the database
            conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            conn.setAutoCommit(false);

            InputStream inStream = fileForm.getFile().getInputStream();
            Workbook workbook = new XSSFWorkbook(inStream);

            Sheet unitSheet = workbook.getSheetAt(0);
            Sheet groupSheet = workbook.getSheetAt(2);
            Sheet productSheet = workbook.getSheetAt(3);

            int count = 0;
            int batchSize = 20;

            //##################################################################
            // save unitSheet
            String sql1 = "INSERT INTO unit(created, status, server_id, name, symbol) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement statement1 = conn.prepareStatement(sql1);

            Iterator<Row> rowIteratorUnit = unitSheet.rowIterator();
            rowIteratorUnit.next();                 // skip the header now

            while(rowIteratorUnit.hasNext()){
                statement1.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement1.setInt(2, 5);
                statement1.setInt(3, 0);

                Row nextRow = rowIteratorUnit.next();
                count++;
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while(cellIterator.hasNext()){
                    Cell nextCell = cellIterator.next();

                    int colIndex = nextCell.getColumnIndex();
                    switch (colIndex){
                        case 0:
                            String name = nextCell.getStringCellValue();
                            statement1.setString(4, name);
                            break;
                        case 1:
                            String symbol = nextCell.getStringCellValue();
                            statement1.setString(5, symbol);
                            break;
                    }
                }
                if(nextRow.cellIterator().hasNext()) statement1.addBatch();

                if(count % batchSize == 0){
                    statement1.executeBatch();
                }
            }
            // execute remaining queries
            statement1.executeBatch();
            conn.commit();

            //###################################################
            // save groupSheet
            String sql3 = "INSERT INTO product_group(created, status, server_id, name) VALUES(?, ?, ?, ?)";
            PreparedStatement statement3 = conn.prepareStatement(sql3);

            Iterator<Row> rowIteratorGroup = groupSheet.rowIterator();
            rowIteratorGroup.next();            // skip the header now
            count = 0;

            while(rowIteratorGroup.hasNext()){
                statement3.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement3.setInt(2, 5);
                statement3.setInt(3, 0);

                Row nextRow = rowIteratorGroup.next();
                count++;
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while(cellIterator.hasNext()){
                    Cell nextCell = cellIterator.next();

                    int colIndex = nextCell.getColumnIndex();
                    switch(colIndex){
                        case 0:
                            String name = nextCell.getStringCellValue();
                            statement3.setString(4, name);
                            break;
                    }
                }
                if(nextRow.cellIterator().hasNext()) statement3.addBatch();

                if(count % batchSize == 0){
                    statement3.executeBatch();
                }
            }
            // execute remaining queries
            statement3.executeBatch();
            conn.commit();

            //############################################################
            // save productSheet
            String sql4 = "INSERT INTO product(created, status, server_id, name, barcode, group_id, base_unit_id, alternate_unit_id, base_unit_val, alternate_unit_val)\n" +
                          "VALUES(?, ?, ?, ?, ?, (SELECT id FROM product_group where name = ? limit 1), (select id from unit where symbol = ? limit 1), (select id from unit where symbol = ? limit 1), ?, ?)";
            //save purchase productRate
            String sql5 = "INSERT INTO product_rate(created, status, server_id, rate, currency_id, date, product_id, type)\n" +
                          "VALUES(?, ?, ?, ?, (SELECT id FROM currency WHERE code = ? limit 1), ?, ?, 1)";

            //save sale productRate
            String sql6 = "INSERT INTO product_rate(created, status, server_id, rate, currency_id, date, product_id, type)\n" +
                          "VALUES(?, ?, ?, ?, (SELECT id FROM currency WHERE code = ? limit 1), ?, ?, 0)";

            PreparedStatement statement4 = DriverManager.getConnection(dbUrl, dbUsername, dbPassword).prepareStatement(sql4);
            PreparedStatement statement5 = conn.prepareStatement(sql5);
            PreparedStatement statement6 = conn.prepareStatement(sql6);

            Iterator<Row> rowIteratorProduct = productSheet.iterator();
            rowIteratorProduct.next();          // skip the header now
            count = 0;
            while(rowIteratorProduct.hasNext()){
                statement4.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement4.setInt(2, 5);
                statement4.setInt(3, 0);

                statement5.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement5.setInt(2, 5);        //set status
                statement5.setInt(3, 0);        //set server-id

                statement6.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement6.setInt(2, 5);       // set status
                statement6.setInt(3, 0);       // set server-id

                Row nextRow = rowIteratorProduct.next();
                count++;
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while(cellIterator.hasNext()){
                    Cell nextCell = cellIterator.next();

                    int colIndex = nextCell.getColumnIndex();
                    switch(colIndex){
                        case 0:
                            String name = nextCell.getStringCellValue();
                            statement4.setString(4, name);
                            break;
                        case 1:
                            String barcode = PoiUtils.getStringValueFromCell(nextCell);
                            statement4.setString(5, barcode);
                            break;
                        case 2:
                            String group = nextCell.getStringCellValue();
                            statement4.setString(6, group);
                            break;
                        case 3:
                            String baseUnitSymbol = nextCell.getStringCellValue();
                            statement4.setString(7, baseUnitSymbol);
                            break;
                        case 4:
                            String alternateUnitSymbol = nextCell.getStringCellValue();
                            statement4.setString(8, alternateUnitSymbol);
                            break;
                        case 5:
                            double baseUnitVal = nextCell.getNumericCellValue();
                            statement4.setDouble(9, baseUnitVal);
                            break;
                        case 6:
                            double alternateUnitVal = nextCell.getNumericCellValue();
                            statement4.setDouble(10, alternateUnitVal);
                            break;
                        case 7:
                            double purchasePrice = nextCell.getNumericCellValue();
                            statement5.setDouble(4, purchasePrice);
                            break;
                        case 8:
                            String pCurrency = nextCell.getStringCellValue();
                            statement5.setString(5, pCurrency);
                            break;
                        case 9:
                            double salesPrice = nextCell.getNumericCellValue();
                            statement6.setDouble(4, salesPrice);
                            break;
                        case 10:
                            String sCurrency = nextCell.getStringCellValue();
                            statement6.setString(5, sCurrency);
                            break;
                        case 11:
                            String dateStr = nextCell.getStringCellValue();
                            statement5.setTimestamp(6, new Timestamp(DateUtil.parse(dateStr, "dd.MM.yyyy").getTime()));
                            statement6.setTimestamp(6, new Timestamp(DateUtil.parse(dateStr, "dd.MM.yyyy").getTime()));
                            break;
                    }
                }

                if(nextRow.cellIterator().hasNext()) {
                    statement4.execute();
                    statement5.setLong(7, productRepository.lastInsertProductId());
                    statement6.setLong(7, productRepository.lastInsertProductId());

                    statement5.addBatch();
                    statement6.addBatch();
                }

                if(count % batchSize == 0){
                    statement5.executeBatch();
                    statement6.executeBatch();
                }
            }
            // execute remaining queries
            statement5.executeBatch();
            statement6.executeBatch();
            conn.commit();
            conn.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    public void getProductExcelTemplate(HttpServletResponse response) {

        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + "shablon.xlsx");
        try {
            OutputStream outStream = response.getOutputStream();
            InputStream inStream = getClass().getResourceAsStream("/static/excel/shablon.xlsx");
            try {
                byte[] buffer = new byte[32768];
                int bytesRead = -1;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                inStream.close();
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UnitForm getUnit(Long unitId) {
        UnitDtoProjection uProjection = productRepository.getUnitById(unitId);
        if(uProjection == null) return null;
        return new UnitForm(uProjection.getId(), uProjection.getName(), uProjection.getSymbol());
    }

    @Override
    public ProductGroupForm getProductGroup(Long productGroupId) {
        ProductGroupDtoProjection pgProjection = productRepository.getProductGroupById(productGroupId);
        if(pgProjection == null) return null;
        return new ProductGroupForm(pgProjection.getId(), pgProjection.getGroupName(), pgProjection.getInfo(), pgProjection.getParentId(), pgProjection.getParentName());
    }

    @Override
    public Boolean createAndEditUnit(UnitForm unit) {
        if(unit == null) return false;
        return productRepository.createAndEditUnit(unit.getName(), unit.getSymbol(), unit.getId()) > 0;
    }

    @Override
    public Boolean createAndEditProductGroup(ProductGroupForm productGroup) {
        if(productGroup == null) return false;
        if(Objects.equals(productGroup.getId(), productGroup.getParentId())) return false;
        return productRepository.createAndEditProductGroup(productGroup.getId(), productGroup.getName(), productGroup.getInfo(), productGroup.getParentId()) > 0;
    }

    @Override
    public Boolean deleteUnit(Long unitId) {
        if(unitId == null) return false;
        return productRepository.deleteUnit(unitId);
    }

    @Override
    public Boolean deleteProductGroup(Long productGroupId) {
        if(productGroupId == null) return false;
        return productRepository.deleteProductGroup(productGroupId);
    }

    @Override
    public Boolean saveProduct(ProductSaveOrEditForm productSaveOrEditForm){
        return null;
    }

    @Override
    public Boolean deleteProduct(Long productId){
        return null;
    }

    @Override
    public ProductSaveOrEditForm getProduct(Long productId){
        return ProductSaveOrEditForm.builder().id(productId).productName("ASDDSADSD").barcode("4645565656").baseUnitId0(1L).baseUnitId1(2L).productGroupId(1L).build();
    }

    @Override
    public void getProductsExcel(Locale locale, String searchText, HttpServletResponse response){

        String fileName = "data_%s.%s";
        String date = DateUtil.format(new Date(), DateUtil.PATTERN9);
        String csvFile = String.format(fileName, date, "csv");

        File dir = new File(System.getProperty("user.home") + CSV_FOLDER);
        if(!dir.exists()) dir.mkdirs();

        String filePath = System.getProperty("user.home") + CSV_FOLDER + File.separator + csvFile;

        try {
            File file = new File(filePath);
            if (!file.exists() && !file.createNewFile()) {
                throw new EntityNotFoundException("CSV file yarata olmadi!");
            }

            ResourceBundle rBundle = R.bundle(locale);
            String id = StringUtils.replace(rBundle.getString("label.id"), " ", "_").replaceAll("\\p{Punct}", "_");
            String productName = StringUtils.replace(rBundle.getString("label.productName"), " ", "_").replaceAll("\\p{Punct}", "_");
            String groupName = StringUtils.replace(rBundle.getString("label.productGroupName"), " ", "_").replaceAll("\\p{Punct}", "_");
            String barcode = StringUtils.replace(rBundle.getString("label.barcode"), " ", "_").replaceAll("\\p{Punct}", "_");
            String rate = StringUtils.replace(rBundle.getString("label.rate"), " ", "_").replaceAll("\\p{Punct}", "_");
            String originalRate = StringUtils.replace(rBundle.getString("label.originalRate"), " ", "_").replaceAll("\\p{Punct}", "_");
            String currency = StringUtils.replace(rBundle.getString("label.currency"), " ", "_").replaceAll("\\p{Punct}", "_");

            String sql = "select " +
                                "id as " + id + ", \n" +
                                "rate as " + rate + ", \n" +
                                "currency as " + currency + ", \n" +
                                "product_name as " + productName + ", \n" +
                                "barcode as " + barcode + ", \n" +
                                "group_name as " + groupName + " \n" +
                         "from get_product_by_name(" + "'" + searchText + "'" + ", '', '', '')";

            Connection conn = null;
            FileOutputStream fileOutputStream = null;
            try {
                conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

                CopyManager copyManager = new CopyManager((BaseConnection) conn);
                fileOutputStream = new FileOutputStream(filePath);
                Long result = copyManager.copyOut("COPY (" + sql + ") TO STDOUT with (DELIMITER ',', ENCODING 'utf-8', FORMAT csv, HEADER)", fileOutputStream);
            } catch (SQLException e) {
                throw new Error("Problem", e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                        fileOutputStream.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            // output
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + csvFile);
            try {
                OutputStream outStream = response.getOutputStream();
                FileInputStream inStream = new FileInputStream(file);
                try {
                    byte[] buffer = new byte[32768];
                    int bytesRead = -1;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    inStream.close();
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Select2Form> findAllProduct() {
        return productRepository.getProductListByNameLike("", "", "", "").stream()
                .map(p -> new Select2Form(p.getId(), p.getProduct_name())).collect(Collectors.toList());
    }

    @Override
    public List<Select2Form> findAllProductGroup(){
        return productRepository.pageProductGroupByName(null, PageRequest.of(0, Integer.MAX_VALUE)).getContent().stream().
                map(pg -> new Select2Form(pg.getId(), pg.getGroupName())).collect(Collectors.toList());
    }

    @Override
    public List<Select2Form> findAllUnit(){
        Page<UnitDtoProjection> pageUnit = productRepository.pageUnit(PageRequest.of(0, Integer.MAX_VALUE));
        if(!pageUnit.getContent().isEmpty()){
            return pageUnit.getContent().stream().map(u -> new Select2Form(u.getId(), u.getSymbol())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
