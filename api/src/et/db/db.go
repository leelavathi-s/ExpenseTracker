package db

import (
    "database/sql"
    _ "github.com/go-sql-driver/mysql"
    "time"
)

func getConnection() (con *sql.DB) {
    con, err := sql.Open ("mysql", "expense_tracker:expense_tracker@/expensetracker")
    
    if err != nil {
        panic (err)
    }
    
    return
}

type OrderData struct {
    Quantity float32
    ProductId int
    Price float32
    ShopId int
    BrandId int
    CategoryId int
    SubcategoryId int
    Comments string
    OrderDate time.Time
}

type FindResult struct {
    Id int
    Value string
}

func find (query string, param string) (results []FindResult, err error) {
    results = []FindResult {}
    con := getConnection()
    defer con.Close()

    rows, err := con.Query (query, "%" + param + "%")
    
    if err != nil {
        panic (err)
    }
    
    for rows.Next() {
        var result FindResult
        rows.Scan (&result.Id, &result.Value)

        results = append (results, result)
    }

    return
}

func FindProducts (searchString string) (products [] FindResult, err error) {
    return find ("SELECT ProductId, ProductName from product where productname LIKE ? order by productname", searchString)
}

func FindBrands (searchString string) (brands [] FindResult, err error) {
    return find ("SELECT BrandId, BrandName from brand where brandname LIKE ? order by brandname", searchString)
}

func FindCategories (searchString string) (categories [] FindResult, err error) {
    return find ("SELECT CategoryId, CategoryName from category where categoryname LIKE ? order by categoryname", searchString)
}

func FindShops (searchString string) (shops [] FindResult, err error) {
    return find ("SELECT ShopId, ShopName from shop where shopname LIKE ? order by shopname", searchString)
}

func Save (order OrderData) (err error) {
    con := getConnection()
    defer con.Close()

    stmt, err := con.Prepare ("INSERT INTO purchaseorder (quantity,productID,price,shopId,brandId,categoryId,subCategoryId,comments,orderDate) values(?,?,?,?,?,?,?,?,?)")
    
    if err != nil {
        return 
    }

    _, err = stmt.Exec (order.Quantity, order.ProductId, order.Price, order.ShopId, order.BrandId, order.CategoryId, order.SubcategoryId, order.Comments, order.OrderDate)
    return
}
