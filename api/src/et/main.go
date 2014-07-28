package main

import "fmt"
import "io/ioutil"
import "et/db"
import "net/http"
import "encoding/json"
import "strconv"

/*

func getVendorsHandler (w http.ResponseWriter, r *http.Request) {
    searchString := r.FormValue ("name")
    items, _ := db.FindVendors (searchString)

    w.Header ().Add ("Content-Type", "application/json")
    
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func getItemsToBuyHandler (w http.ResponseWriter, r *http.Request) {
    items, _ := db.GetItemsToBuy ()

    w.Header ().Add ("Content-Type", "application/json")
    
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func getMonthlySpendingsHandler (w http.ResponseWriter, r *http.Request) {
    spendings, _ := db.GetMonthSpendings()
    w.Header ().Add ("Content-Type", "application/json")
    
    data, _ := json.Marshal (spendings);

    fmt.Fprintf (w, "%s", data)
}

func getRecentPurchasesHandler (w http.ResponseWriter, r *http.Request) {
    body, err := ioutil.ReadAll(r.Body)
    if err != nil {
        panic(err)
    }
    
    type JsonData struct {
        Item string
    }
    
    var jsonData JsonData
    json.Unmarshal (body, &jsonData)
    
    defer r.Body.Close()

    items, _ := db.GetRecentPurchasesOfItem (jsonData.Item)
    data, _ := json.Marshal (items)

    w.Header ().Add ("Content-Type", "application/json")
    fmt.Fprintf (w, "%s", data)
}

func removeFromItemsToBuyHandler (w http.ResponseWriter, r *http.Request) {
    body, err := ioutil.ReadAll(r.Body)
    if err != nil {
        panic(err)
    }
    
    type JsonData struct {
        Item string
    }
    
    var jsonData JsonData
    json.Unmarshal (body, &jsonData)
    
    defer r.Body.Close()
    
    err = db.RemoveFromItemsToBuy (jsonData.Item)
    
    getItemsToBuyHandler (w, r)
}

func addHandler (w http.ResponseWriter, r *http.Request) {
    body, err := ioutil.ReadAll(r.Body)
    if err != nil {
        panic(err)
    }
    
    type JsonData struct {
        Item string
    }

    var jsonData JsonData
    json.Unmarshal (body, &jsonData)
    
    defer r.Body.Close()

    err = db.AddItemToBuy (jsonData.Item)

    getItemsToBuyHandler (w, r)
}

*/
func getBrandsForProductHandler (w http.ResponseWriter, r *http.Request) {
    productId,_ := strconv.Atoi (r.FormValue ("pid"))
    items, _ := db.FindBrandsForProduct ("", productId);

    w.Header ().Add ("Content-Type", "application/json")
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func getBrandsHandler (w http.ResponseWriter, r *http.Request) {
    searchString := r.FormValue ("name")
    items, _ := db.FindBrands (searchString);

    w.Header ().Add ("Content-Type", "application/json")
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func getShopsHandler (w http.ResponseWriter, r *http.Request) {
    searchString := r.FormValue ("name")
    items, _ := db.FindShops (searchString)

    w.Header ().Add ("Content-Type", "application/json")
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func getProductsHandler (w http.ResponseWriter, r *http.Request) {
    searchString := r.FormValue ("name")
    items, _ := db.FindProducts (searchString)

    w.Header ().Add ("Content-Type", "application/json")
    data, _ := json.Marshal (items);

    fmt.Fprintf (w, "%s", data)
}

func saveHandler (w http.ResponseWriter, r *http.Request) {
    body, err := ioutil.ReadAll(r.Body)
    if err != nil {
        panic(err)
    }
    type JsonRetData struct {
        Msg string
    }
    
    var jsonData db.OrderData
    json.Unmarshal (body, &jsonData)
    
    defer r.Body.Close()

    err = db.Save (jsonData)
    
    
    var jsonRetData JsonRetData
    jsonRetData.Msg = "Saved"
    
    if err != nil {
        fmt.Println (err)
        jsonRetData.Msg = err.Error()
    }
    
    w.Header ().Add ("Content-Type", "application/json")
    data, err := json.Marshal (jsonRetData)

    fmt.Fprintf (w, "%s", data)
}

func initWebServer() {
    http.HandleFunc ("/save", saveHandler)
    http.HandleFunc ("/products", getProductsHandler)
    http.HandleFunc ("/shops", getShopsHandler)
    http.HandleFunc ("/brands_by_product", getBrandsForProductHandler)
    http.HandleFunc ("/brands", getBrandsHandler)
    /*
    http.HandleFunc ("/add", addHandler)
    http.HandleFunc ("/recent", getRecentPurchasesHandler)
    http.HandleFunc ("/get_items_to_buy", getItemsToBuyHandler)
    http.HandleFunc ("/remove_item_to_buy", removeFromItemsToBuyHandler)
    http.HandleFunc ("/vendors", getVendorsHandler)
    http.HandleFunc ("/get_monthly_spendings", getMonthlySpendingsHandler)
    */
    http.Handle ("/view/", http.StripPrefix("/view/", http.FileServer(http.Dir("/home/saaadhu/code/git/ExpenseTracker/api/src/et/www"))))
    http.ListenAndServe(":8083", nil)
}

func main() {
    initWebServer()
}
